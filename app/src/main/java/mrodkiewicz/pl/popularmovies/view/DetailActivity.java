package mrodkiewicz.pl.popularmovies.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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


/**
 * Created by Mikolaj Rodkiewicz on 19.02.2018.
 */


public class DetailActivity extends AppCompatActivity {
    public static final String EXTRAS_MOVIE_ID = "EXTRAS_MOVIE_ID";
    @BindView(R.id.activity_detail_title_textview)
    TextView activityDetailTitleTextview;
    @BindView(R.id.activity_detail_imageView)
    ImageView activityDetailImageView;
    @BindView(R.id.activity_detail_year_textView)
    TextView activityDetailYearTextView;
    @BindView(R.id.activity_detail_mark_textView)
    TextView activityDetailMarkTextView;
    @BindView(R.id.activity_detail_description_textView)
    TextView activityDetailDescriptionTextView;
    @BindView(R.id.activity_detail)
    LinearLayout activityDetail;
    @BindView(R.id.activity_detail_top_layout)
    LinearLayout activityDetailTopLayout;

    private PopularMovies popularMovies;
    private Movie IntentMovie,movie;
    private boolean isFavoutire;
    private MenuItem menuItem;

    public static Intent getConfigureIntent(Context context, Movie IntentMovie) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRAS_MOVIE_ID, IntentMovie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);


        if (getIntent().getExtras() != null) {
            Bundle data = getIntent().getExtras();
            movie = (Movie) data.getParcelable(EXTRAS_MOVIE_ID);
            loadData(movie);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_detail, menu);
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
                    item.setIcon(R.drawable.ic_favorite_24dp);
                    item.setTitle(getString(R.string.action_favourite_true));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void loadData(Movie movie){
        setTitle(movie.getTitle());
        Picasso.with(getApplicationContext()).load(API_IMAGE_URL + Config.API_IMAGE_SIZE_W185 + movie.getPosterPath()).into(activityDetailImageView);
        activityDetailTitleTextview.setText(movie.getTitle());
        activityDetailDescriptionTextView.setText(movie.getOverview());
        activityDetailYearTextView.setText(movie.getReleaseDate());
        activityDetailMarkTextView.setText(movie.getVoteAverage().toString() + getString(R.string.rating_activity_detail));
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
