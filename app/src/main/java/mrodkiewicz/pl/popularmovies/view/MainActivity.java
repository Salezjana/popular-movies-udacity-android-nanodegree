package mrodkiewicz.pl.popularmovies.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.transition.BuildConfig;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mrodkiewicz.pl.popularmovies.PopularMovies;
import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.adapter.MoviesRecyclerViewAdapter;
import mrodkiewicz.pl.popularmovies.api.APIService;
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

public class MainActivity extends BaseAppCompatActivity {
    @BindView(R.id.movies_recycler_view)
    RecyclerView moviesRecyclerView;

    private ArrayList<Movie> movies;
    private PopularMovies popularMovies;
    private MoviesRecyclerViewAdapter moviesRecyclerViewAdapter;
    private int sorting_state, last_page;
    private Integer current_page = 1;
    private CharSequence[] sorting_state_array;
    private SharedPreferences preferences;
    private GridLayoutManager gridLayoutManager;
    private Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        preferences = this.getSharedPreferences(
                Config.PREFERENCES_KEY, Context.MODE_PRIVATE);

        showProgressDialog(null, getString(R.string.download_movies));

        movies = new ArrayList<Movie>();
        popularMovies = new PopularMovies();

        sorting_state = preferences.getInt(Config.PREFERENCES_SORTING_POSITION, 0);
        sorting_state_array = new CharSequence[]{"by popular", "by highest grades"};

        setupView();
        initListener();
        loadMovies(current_page, sorting_state);

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
                    movies.addAll(moviesResposnse);
                    movies.add(new Movie());
                    moviesRecyclerViewAdapter.notifyDataSetChanged();
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

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause");
        state = gridLayoutManager.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
                                Timber.d("onClick" + selectedPosition);
                                if (selectedPosition == 0) {
                                    if (sorting_state != selectedPosition) {
                                        current_page = 1;
                                    }
                                    sorting_state = selectedPosition;
                                    preferences.edit().putInt(Config.PREFERENCES_SORTING_POSITION, sorting_state).apply();
                                    showProgressDialog(null, getString(R.string.download_movies));
                                    loadMovies(current_page, sorting_state);
                                } else {
                                    if (sorting_state != selectedPosition) {
                                        current_page = 1;
                                    }
                                    sorting_state = selectedPosition;
                                    preferences.edit().putInt(Config.PREFERENCES_SORTING_POSITION, sorting_state).apply();
                                    showProgressDialog(null, getString(R.string.download_movies));
                                    loadMovies(current_page, sorting_state);
                                }
                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}