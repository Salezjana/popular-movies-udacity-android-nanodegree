package mrodkiewicz.pl.popularmovies.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.transition.BuildConfig;
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
import mrodkiewicz.pl.popularmovies.helpers.Favourites;
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
    private Integer current_page = 1;
    @BindView(R.id.movies_recycler_view)
    RecyclerView moviesRecyclerView;

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
        moviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(getApplicationContext(), movies,current_page);
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
                        Timber.d("onItemClick"+movies.get(position).getTitle());
                        Timber.d("onItemClick"+movies.get(position));
                        current_page -= 1;
                        moviesRecyclerViewAdapter.setPage(current_page);
                        loadMovies(current_page);
                    }else {
                        Timber.d("onItemClick false");
                        Timber.d("onItemClick"+movies.get(position).getTitle());
                        Timber.d("onItemClick"+movies.get(position));
                        current_page += 1;
                        moviesRecyclerViewAdapter.setPage(current_page);
                        loadMovies(current_page);
                    }
                } else {
                    Timber.d("onItemClick true");
                    Timber.d("onItemClick "+movies.get(position).getTitle());
                    Timber.d("onItemClick "+movies.get(position));
                    startActivity(DetailActivity.getConfigureIntent(getApplicationContext(), movies.get(position).getId()));
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
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
                Favourites favourites = new Favourites(this);
                favourites.addFavouritesMovies(1);
                favourites.addFavouritesMovies(12);
                favourites.addFavouritesMovies(3);
                favourites.addFavouritesMovies(0);
                favourites.addFavouritesMovies(-1);
                favourites.isOnList(1);
                favourites.LogFavouritesMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadMovies(final int page) {
        if (isInternetEnable()) {
            APIService apiService =
                    popularMovies.getClient().create(APIService.class);

            Call<MoviesResponse> call = apiService.getTopRatedMoviesPage(API_KEY, page);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movie> moviesResposnse = response.body().getResults();
                    movies.clear();
                    if (page!=1){
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

}