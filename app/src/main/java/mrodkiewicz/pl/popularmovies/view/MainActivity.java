package mrodkiewicz.pl.popularmovies.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.transition.BuildConfig;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mrodkiewicz.pl.popularmovies.PopularMovies;
import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.adapter.MoviesRecyclerViewAdapter;
import mrodkiewicz.pl.popularmovies.api.APIService;
import mrodkiewicz.pl.popularmovies.listeners.RecyclerViewItemClickListener;
import mrodkiewicz.pl.popularmovies.model.Movie;
import mrodkiewicz.pl.popularmovies.model.MoviesResponse;
import mrodkiewicz.pl.popularmovies.view.base.BaseAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static mrodkiewicz.pl.popularmovies.helpers.Config.API_KEY;

public class MainActivity extends BaseAppCompatActivity {

    private static Bundle bundle;
    @BindView(R.id.movies_recycler_view)
    RecyclerView moviesRecyclerView;
    private Integer current_page = 1;
    private ArrayList<Movie> movies;
    private PopularMovies popularMovies;
    private MoviesRecyclerViewAdapter moviesRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }


        showProgressDialog(null, getString(R.string.download_movies));
        movies = new ArrayList<Movie>();
        popularMovies = new PopularMovies();

        setupView();
        loadMovies(current_page);
        initListener();
    }


    private void setupView() {
        moviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(getApplicationContext(), movies, current_page);
        moviesRecyclerView.setAdapter(moviesRecyclerViewAdapter);
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        moviesRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void initListener() {
        moviesRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this,
                moviesRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (movies.get(position).getTitle() == null) {
                    if (position == 0) {
                        Timber.d("onItemClick false");
                        Timber.d("onItemClick" + movies.get(position).getTitle());
                        Timber.d("onItemClick" + movies.get(position));
                        current_page -= 1;
                        moviesRecyclerViewAdapter.setPage(current_page);
                        loadMovies(current_page);
                    } else {
                        Timber.d("onItemClick false");
                        Timber.d("onItemClick" + movies.get(position).getTitle());
                        Timber.d("onItemClick" + movies.get(position));
                        current_page += 1;
                        moviesRecyclerViewAdapter.setPage(current_page);
                        loadMovies(current_page);
                    }
                } else {
                    startActivity(DetailActivity.getConfigureIntent(getApplicationContext(), movies.get(position).getId()));
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void loadMovies(final int page) {
        if (isInternetEnable()) {
            APIService apiService =
                    popularMovies.getClient().create(APIService.class);

            Call<MoviesResponse> call = apiService.getMovies("popular", API_KEY, page);
            call.enqueue(new Callback<MoviesResponse>() {
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
                    moviesRecyclerView.scrollToPosition(1);

                    Timber.d("MoviesResponse getResults" + response.toString());
                    Timber.d("MoviesResponse getResults");
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Timber.d("MoviesResponse onFailure");
                }
            });
            hideProgressDialog();
        } else {
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .show();
            hideProgressDialog();
            Timber.d("MoviesResponse internet off");
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_sort:
                new MaterialDialog.Builder(this)
                        .title(R.string.download_details)
                        .items(movies)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 **/
                                return true;
                            }
                        })
                        .positiveText(R.string.action_favourite_false)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}