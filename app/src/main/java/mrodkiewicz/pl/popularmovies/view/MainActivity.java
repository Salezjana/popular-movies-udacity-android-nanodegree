package mrodkiewicz.pl.popularmovies.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;
import java.util.List;

import mrodkiewicz.pl.popularmovies.PopularMovies;
import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.api.APIService;
import mrodkiewicz.pl.popularmovies.model.Movie;
import mrodkiewicz.pl.popularmovies.view.base.BaseAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseAppCompatActivity {
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected static final int REQUEST_PERMISSIONS_REQUEST_CODE = 14;

    private APIService service;
    private List<Movie> movies;
    private PopularMovies popularMovies;
    private Movie movie;
    private Boolean progressDialogShowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showProgressDialog(null, getString(R.string.download_movies));

        movies = new ArrayList<Movie>();
        popularMovies = new PopularMovies();
        popularMovies.init(this);

        loadMovies();
    }

    private void loadMovies() {
//        if (isInternetEnable()) {
//            service = PopularMovies.retrofit.create(APIService.class);
//
////            if (!progressDialogShowed) {
////                showProgressDialog(null, getString(R.string.download_details));
////                progressDialogShowed = true;
////            }
//            Call<Movie> movie = service.getMovie(551);
//            movie.enqueue(new Callback<Movie>() {
//                @Override
//                public void onResponse(Call<Movie> call, Response<Movie> response) {
//                    if (progressDialog.isShowing())
//                        hideProgressDialog();
//                }
//
//                @Override
//                public void onFailure(Call<Movie> call, Throwable t) {
//                    if (progressDialog.isShowing())
//                        hideProgressDialog();
//                }
//            });
//
//        } else {
//            Snackbar.make(
//                    findViewById(R.id.activity_main),
//                    getString(R.string.no_internet),
//                    Snackbar.LENGTH_INDEFINITE)
//                    .show();
//
//
//        }
//
    }
}
