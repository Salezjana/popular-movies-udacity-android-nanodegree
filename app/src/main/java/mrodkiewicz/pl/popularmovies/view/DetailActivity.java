package mrodkiewicz.pl.popularmovies.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import mrodkiewicz.pl.popularmovies.PopularMovies;
import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.api.APIService;
import mrodkiewicz.pl.popularmovies.helpers.Config;
import mrodkiewicz.pl.popularmovies.model.Movie;
import mrodkiewicz.pl.popularmovies.view.base.BaseAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static mrodkiewicz.pl.popularmovies.helpers.Config.API_IMAGE_URL;
import static mrodkiewicz.pl.popularmovies.helpers.Config.API_KEY;

public class DetailActivity extends BaseAppCompatActivity {
    public static final String EXTRAS_MOVIE_ID = "EXTRAS_MOVIE_ID";
    @BindView(R.id.activity_detail_title_textview)
    TextView activityDetailTitleTextview;
    @BindView(R.id.activity_detail_imageView)
    ImageView activityDetailImageView;
    @BindView(R.id.activity_detail_year_textView)
    TextView activityDetailYearTextView;
    @BindView(R.id.activity_detail_timelong_textView)
    TextView activityDetailTimelongTextView;
    @BindView(R.id.activity_detail_mark_textView)
    TextView activityDetailMarkTextView;
    @BindView(R.id.activity_detail_add_favoutire_button)
    Button activityDetailAddFavoutireButton;
    @BindView(R.id.activity_detail_description_textView)
    TextView activityDetailDescriptionTextView;
    @BindView(R.id.activity_detail)
    LinearLayout activityDetail;
    private APIService service;
    private Integer movieId;
    private PopularMovies popularMovies;
    private Movie movie;

    public static Intent getConfigureIntent(Context context, Integer movieId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRAS_MOVIE_ID, movieId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        popularMovies = new PopularMovies();

        if (getIntent().getExtras() != null) {
            movieId = getIntent().getIntExtra(EXTRAS_MOVIE_ID, -1);
            loadMovies();
        } else {
            finish();
        }


    }

    private void loadMovies() {
        if (isInternetEnable()) {
            APIService apiService =
                    popularMovies.getClient().create(APIService.class);

            Call<Movie> call = apiService.getMovieDetails(movieId, API_KEY);
            call.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    Timber.d("MoviesResponse getResults" + response.toString());
                    Timber.d("MoviesResponse getPosterPath " + response.body().getPosterPath());
                    movie = response.body();

                    setTitle(movie.getTitle());
                    Picasso.with(getApplicationContext()).load(API_IMAGE_URL + Config.API_IMAGE_SIZE_W185 + movie.getPosterPath()).into(activityDetailImageView);
                    activityDetailTitleTextview.setText(movie.getTitle());
                    activityDetailDescriptionTextView.setText(movie.getOverview());
                    activityDetailYearTextView.setText(movie.getReleaseDate());
                    activityDetailTimelongTextView.setText(movie.getRuntime().toString());
                    activityDetailMarkTextView.setText(movie.getVoteAverage().toString());
                    Timber.d("MoviesResponse movie" + movie.toString());

                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
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
