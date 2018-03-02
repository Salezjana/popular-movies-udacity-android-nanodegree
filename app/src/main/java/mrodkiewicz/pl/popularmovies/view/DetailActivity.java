package mrodkiewicz.pl.popularmovies.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import mrodkiewicz.pl.popularmovies.PopularMovies;
import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.api.APIService;
import mrodkiewicz.pl.popularmovies.helpers.Config;
import mrodkiewicz.pl.popularmovies.helpers.Favourites;
import mrodkiewicz.pl.popularmovies.model.Movie;
import mrodkiewicz.pl.popularmovies.view.base.BaseAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static mrodkiewicz.pl.popularmovies.helpers.Config.API_IMAGE_URL;


/**
 * Created by Mikolaj Rodkiewicz on 19.02.2018.
 */


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
    @BindView(R.id.activity_detail_description_textView)
    TextView activityDetailDescriptionTextView;
    @BindView(R.id.activity_detail)
    LinearLayout activityDetail;
    @BindView(R.id.activity_detail_top_layout)
    LinearLayout activityDetailTopLayout;

    private Integer movieId;
    private PopularMovies popularMovies;
    private Movie movie;
    private boolean isFavoutire;
    private MenuItem menuItem;
    private Menu menu;
    private Favourites favourites;

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

        showProgressDialog(null, getString(R.string.download_details));

        popularMovies = new PopularMovies();

        if (getIntent().getExtras() != null) {
            movieId = getIntent().getIntExtra(EXTRAS_MOVIE_ID, -1);
            loadMovie();
        } else {
            finish();
        }

//        favourites = new Favourites(this);
//        favourites.saveFavourites(10);
//        favourites.log();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_detail, menu);
        this.menu = menu;
        menuItem = menu.getItem(0);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favourite:
                if (isFavoutire) {
                    isFavoutire = false;
                    item.setIcon(R.drawable.ic_favorite_border_24dp);
                    item.setTitle(getString(R.string.action_favourite_false));
                } else {
                    isFavoutire = true;
//                    favourites.addFavouritesMovies(movieId);
                    item.setIcon(R.drawable.ic_favorite_24dp);
                    item.setTitle(getString(R.string.action_favourite_true));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadMovie() {
        if (isInternetEnable()) {
            APIService apiService =
                    popularMovies.getClient().create(APIService.class);
            Call<Movie> call = apiService.getMovieDetails(movieId, Config.getApiKey(this));
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
                    activityDetailTimelongTextView.setText(movie.getRuntime().toString() + getString(R.string.duration_activity_detail));
                    activityDetailMarkTextView.setText(movie.getVoteAverage().toString() + getString(R.string.rating_activity_detail));
                    Timber.d("MoviesResponse movie" + movie.toString());
                    hideProgressDialog();

                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    Timber.d("MoviesResponse onFailure");
                    hideProgressDialog();
                }
            });
        }  else {
            hideProgressDialog();
            Snackbar.make(
                    findViewById(R.id.activity_detail),
                    getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.no_internet_button), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadMovie();
                        }
                    }).show();
            Timber.d("MoviesResponse internet off");
        }
    }


    private void setAsFavoutire() {
        menuItem.setIcon(R.drawable.ic_favorite_border_24dp);
        menuItem.setTitle(getString(R.string.action_favourite_false));
    }

    private void setAsNotFavourite() {
        menuItem.setIcon(R.drawable.ic_favorite_24dp);
        menuItem.setTitle(getString(R.string.action_favourite_true));
    }
}
