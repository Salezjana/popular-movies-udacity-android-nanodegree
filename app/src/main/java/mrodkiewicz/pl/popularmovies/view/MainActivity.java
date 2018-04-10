package mrodkiewicz.pl.popularmovies.view;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mrodkiewicz.pl.popularmovies.BuildConfig;
import mrodkiewicz.pl.popularmovies.PopularMovies;
import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.adapter.MoviesRecyclerViewAdapter;
import mrodkiewicz.pl.popularmovies.api.APIService;
import mrodkiewicz.pl.popularmovies.db.FavouritesMoviesDatebaseHelper;
import mrodkiewicz.pl.popularmovies.helpers.Config;
import mrodkiewicz.pl.popularmovies.listeners.RecyclerViewItemClickListener;
import mrodkiewicz.pl.popularmovies.model.Movie;
import mrodkiewicz.pl.popularmovies.model.MoviesResponse;
import mrodkiewicz.pl.popularmovies.view.base.BaseAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Mikolaj Rodkiewicz on 19.02.2018.
 */

public class MainActivity extends BaseAppCompatActivity  implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.movies_recycler_view)
    RecyclerView moviesRecyclerView;
    private static final int TASK_LOADER_ID = 0;


    private ArrayList<Movie> movies;
    private PopularMovies popularMovies;
    private MoviesRecyclerViewAdapter moviesRecyclerViewAdapter;
    //SORTING STATE -> 0 = by popular, 1 = by highest grades, 2 = favoutires
    private int sorting_state;
    private Integer current_page;
    private CharSequence[] sorting_state_array;
    private SharedPreferences preferences;
    private GridLayoutManager gridLayoutManager;
    private Parcelable state;
    private ContentResolver contentResolver;
    private FavouritesMoviesDatebaseHelper favouritesMoviesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(
                                    Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(
                                    Stetho.defaultInspectorModulesProvider(this))
                            .build());
            Timber.plant(new Timber.DebugTree());
        }
        Timber.d("onCreate");

        contentResolver = getContentResolver();

        preferences = this.getSharedPreferences(
                Config.PREFERENCES_KEY, Context.MODE_PRIVATE);

        showProgressDialog(null, getString(R.string.download_movies));

        movies = new ArrayList<Movie>();
        popularMovies = new PopularMovies();

        favouritesMoviesDB = new FavouritesMoviesDatebaseHelper(this);


        sorting_state = preferences.getInt(Config.PREFERENCES_SORTING_POSITION, 0);
        sorting_state_array = new CharSequence[]{"by popular", "by highest grades", "favourites"};

        setupView();
        initListener();

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            if (sorting_state != 2) {
                movies.clear();
//                updateMoviesList(savedInstanceState.<Movie>getParcelableArrayList(Config.RECYCLEVIEW_LIST_KEY));
                moviesRecyclerView.scrollToPosition(savedInstanceState.getInt(Config.RECYCLEVIEW_POSITION_KEY));
                current_page = savedInstanceState.getInt(Config.RECYCLEVIEW_PAGE_KEY);
                hideProgressDialog();
            } else {
                current_page = 1;
                loadMovies(current_page, sorting_state);
            }
        } else {
            current_page = 1;
            loadMovies(current_page, sorting_state);

        }

    }

    private void setupView() {
        moviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(this, movies);
        moviesRecyclerView.setAdapter(moviesRecyclerViewAdapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(this, 2);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 4);
        }
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        gridLayoutManager.onRestoreInstanceState(state);
    }

    private void initListener() {
        moviesRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this,
                moviesRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @SuppressLint("BinaryOperationInTimber")
            @Override
            public void onItemClick(View view, int position) {
                if (movies.get(position).getTitle() == null) {
                    if (position == 0) {
                        Timber.d("onItemClick false");
                        Timber.d("onItemClick" + movies.get(position).getTitle());
                        Timber.d("onItemClick" + movies.get(position));
                        current_page -= 1;
                        loadMovies(current_page, sorting_state);
                    } else {
                        Timber.d("onItemClick false");
                        Timber.d("onItemClick" + movies.get(position).getTitle());
                        Timber.d("onItemClick" + movies.get(position));
                        current_page += 1;
                        loadMovies(current_page, sorting_state);
                    }
                } else {
                    startActivity(DetailActivity.getConfigureIntent(getApplicationContext(), movies.get(position)));
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void loadMovies(final int page, final int sorting_state) {
        Timber.d("loadMovies isOnline " + isInternetEnable());
        if (sorting_state == 2) {
            movies.clear();
            //updateMoviesList(favouritesMoviesDB.getAllMovies());
            Loader loader = getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

            moviesRecyclerView.scrollToPosition(0);
        } else {
            if (isInternetEnable()) {
                APIService apiService =
                        popularMovies.getClient().create(APIService.class);
                Call<MoviesResponse> call;
                if (sorting_state == 0) {
                    call = apiService.getMovies("popular", Config.API_KEY, page);
                } else {
                    call = apiService.getMovies("top_rated", Config.API_KEY, page);
                }
                call.enqueue(new Callback<MoviesResponse>() {
                    @SuppressLint("BinaryOperationInTimber")
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        List<Movie> moviesResposnse = response.body().getResults();
                        movies.clear();
                        if (page != 1) {
                            movies.add(new Movie());
                        }
                        updateMoviesList(moviesResposnse);
                        movies.add(new Movie());
                        moviesRecyclerView.scrollToPosition(0);
                        Timber.d("MoviesResponse getResults" + response.toString());
                        hideProgressDialog();
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Timber.d("MoviesResponse onFailure");
                        hideProgressDialog();
                    }
                });


            } else {
                hideProgressDialog();
                Snackbar.make(
                        findViewById(R.id.activity_main),
                        getString(R.string.no_internet),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.no_internet_button), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showProgressDialog(null, getString(R.string.download_movies));
                                loadMovies(current_page, sorting_state);
                            }
                        }).show();
                Timber.d("MoviesResponse internet off");
            }
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause");
        state = gridLayoutManager.onSaveInstanceState();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(Config.RECYCLEVIEW_LIST_KEY, movies);
        savedInstanceState.putInt(Config.RECYCLEVIEW_POSITION_KEY, gridLayoutManager.findFirstVisibleItemPosition());
        savedInstanceState.putInt(Config.RECYCLEVIEW_PAGE_KEY, current_page);
        Timber.d("onRestoreInstanceState");
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        current_page = savedInstanceState.getInt(Config.RECYCLEVIEW_PAGE_KEY);
        movies.clear();
        updateMoviesList(savedInstanceState.<Movie>getParcelableArrayList(Config.RECYCLEVIEW_LIST_KEY));
        moviesRecyclerView.scrollToPosition(savedInstanceState.getInt(Config.RECYCLEVIEW_POSITION_KEY));
        Timber.d("onRestoreInstanceState");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_title))
                        .setSingleChoiceItems(sorting_state_array, sorting_state, null)
                        .setPositiveButton(getString(R.string.dialog_button), new DialogInterface.OnClickListener() {
                            @SuppressLint("BinaryOperationInTimber")
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                                if (selectedPosition == 0) {
                                    if (sorting_state != selectedPosition) {
                                        current_page = 1;
                                    }
                                    sorting_state = selectedPosition;
                                    preferences.edit().putInt(Config.PREFERENCES_SORTING_POSITION, sorting_state).apply();
                                    showProgressDialog(null, getString(R.string.download_movies));
                                    loadMovies(current_page, sorting_state);
                                } else if (selectedPosition == 1) {
                                    if (sorting_state != selectedPosition) {
                                        current_page = 1;
                                    }
                                    sorting_state = selectedPosition;
                                    preferences.edit().putInt(Config.PREFERENCES_SORTING_POSITION, sorting_state).apply();
                                    showProgressDialog(null, getString(R.string.download_movies));
                                    loadMovies(current_page, sorting_state);
                                } else {
                                    sorting_state = selectedPosition;
                                    preferences.edit().putInt(Config.PREFERENCES_SORTING_POSITION, sorting_state).apply();
                                    showProgressDialog(null, getString(R.string.download_movies));
                                    loadMovies(current_page, sorting_state);
                                }
                                Timber.d("sorting_state " + sorting_state);
                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateMoviesList(List<Movie> newMovies) {
        movies.addAll(newMovies);
        moviesRecyclerViewAdapter.notifyDataSetChanged();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(Config.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            Config.MovieEntry.KEY_MOVIE_ID);

                } catch (Exception e) {
                    Timber.d("no sie popsulo");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };

    }


    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null){
            Timber.d(" Cursor data = null");
        }
        Timber.d("con" + Config.MovieEntry.CONTENT_URI);
        Timber.d("onLoadFinished");
        cursorToList(data);
        hideProgressDialog();
    }


    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Timber.d("onLoaderReset");
        hideProgressDialog();
    }

    public void cursorToList(Cursor cursor){
        ArrayList<Movie> tmpMoves = new ArrayList<Movie>();
        cursor.moveToFirst();
        Timber.d(String.valueOf(cursor.getInt(0)));
        Timber.d(cursor.getString(1));
        Timber.d(cursor.getString(2));
        updateMoviesList(tmpMoves);
    }




}