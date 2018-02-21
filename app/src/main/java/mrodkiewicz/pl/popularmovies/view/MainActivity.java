package mrodkiewicz.pl.popularmovies.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mrodkiewicz.pl.popularmovies.PopularMovies;
import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.api.APIService;
import mrodkiewicz.pl.popularmovies.model.Movie;
import mrodkiewicz.pl.popularmovies.model.MoviesResponse;
import mrodkiewicz.pl.popularmovies.view.base.BaseAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseAppCompatActivity {
    public String API_KEY;

    private APIService service;
    private List<Movie> movies;
    private PopularMovies popularMovies;
    private Movie movie;
    private Boolean progressDialogShowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        API_KEY = getString(R.string.tmdb_api_key);

        showProgressDialog(null, getString(R.string.download_movies));

        movies = new ArrayList<Movie>();
        popularMovies = new PopularMovies();


        loadMovies();
    }

    private void loadMovies() {
        if (isInternetEnable()) {
            APIService apiService =
                    popularMovies.getClient(this).create(APIService.class);

            Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {
                    List<Movie> movies = response.body().getResults();
                }

                @Override
                public void onFailure(Call<MoviesResponse>call, Throwable t) {
                }
            });
        } else {
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .show();


        }

    }
}
