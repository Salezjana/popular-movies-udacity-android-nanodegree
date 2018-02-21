package mrodkiewicz.pl.popularmovies.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.transition.BuildConfig;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mrodkiewicz.pl.popularmovies.PopularMovies;
import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.adapter.MoviesRecyclerViewAdapter;
import mrodkiewicz.pl.popularmovies.api.APIService;
import mrodkiewicz.pl.popularmovies.model.Movie;
import mrodkiewicz.pl.popularmovies.model.MoviesResponse;
import mrodkiewicz.pl.popularmovies.view.base.BaseAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static mrodkiewicz.pl.popularmovies.helpers.Config.API_KEY;

public class MainActivity extends BaseAppCompatActivity implements MoviesRecyclerViewAdapter.ItemClickListener {

    @BindView(R.id.movies_recycler_view)
    RecyclerView moviesRecyclerView;


    private APIService service;
    private List<Movie> movies;
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

        loadMovies();

        String[] data = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48"};

        int numberOfColumns = 6;
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        moviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(this, data);
        moviesRecyclerViewAdapter.setClickListener(this);
        moviesRecyclerView.setAdapter(moviesRecyclerViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this,DetailActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadMovies() {
        if (isInternetEnable()) {
            APIService apiService =
                    popularMovies.getClient(this).create(APIService.class);

            Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movie> movies = response.body().getResults();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
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

        }

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + moviesRecyclerViewAdapter.getItem(position) + ", which is at cell position " + position);
    }
}