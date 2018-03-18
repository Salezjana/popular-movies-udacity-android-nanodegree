package mrodkiewicz.pl.popularmovies.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mrodkiewicz.pl.popularmovies.PopularMovies;
import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.adapter.ReviewsRecyclerViewAdapter;
import mrodkiewicz.pl.popularmovies.adapter.TrailersRecyclerViewAdapter;
import mrodkiewicz.pl.popularmovies.api.APIService;
import mrodkiewicz.pl.popularmovies.helpers.Config;
import mrodkiewicz.pl.popularmovies.listeners.RecyclerViewItemClickListener;
import mrodkiewicz.pl.popularmovies.model.Movie;
import mrodkiewicz.pl.popularmovies.model.Reviews;
import mrodkiewicz.pl.popularmovies.model.ReviewsResponse;
import mrodkiewicz.pl.popularmovies.model.Trailer;
import mrodkiewicz.pl.popularmovies.model.TrailersResponse;
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
    @BindView(R.id.activity_detail_mark_textView)
    TextView activityDetailMarkTextView;
    @BindView(R.id.activity_detail_description_textView)
    TextView activityDetailDescriptionTextView;
    @BindView(R.id.activity_detail)
    LinearLayout activityDetail;
    @BindView(R.id.activity_detail_top_layout)
    LinearLayout activityDetailTopLayout;
    @BindView(R.id.trailers_recycler_view)
    RecyclerView trailersRecyclerView;
    @BindView(R.id.reviews_recycler_view)
    RecyclerView reviewsRecyclerView;
    @BindView(R.id.activity_detail_trailers_textView)
    TextView activityDetailTrailersTextView;
    @BindView(R.id.activity_detail_reviews_textView)
    TextView activityDetailReviewsTextView;

    private ArrayList<Trailer> trailers = new ArrayList<Trailer>();
    private ArrayList<Reviews> reviews = new ArrayList<Reviews>();
    private PopularMovies popularMovies;
    private Movie movie;
    private boolean isFavoutire;
    private MenuItem menuItem;
    private TrailersRecyclerViewAdapter trailersRecyclerViewAdapter;
    private ReviewsRecyclerViewAdapter reviewsRecyclerViewAdapter;


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
            setupView();
            initListener();
        } else {
            finish();
        }
    }

    private void initListener() {
        trailersRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, trailersRecyclerView, new RecyclerViewItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(position).getKey())));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void setupView() {
        trailersRecyclerViewAdapter = new TrailersRecyclerViewAdapter(this, trailers);
        trailersRecyclerView.setAdapter(trailersRecyclerViewAdapter);
        trailersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trailersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trailersRecyclerView.setNestedScrollingEnabled(false);

        reviewsRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(this, reviews);
        reviewsRecyclerView.setAdapter(reviewsRecyclerViewAdapter);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewsRecyclerView.setNestedScrollingEnabled(false);
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

    private void loadData(final Movie movie) {
        setTitle(movie.getTitle());
        Picasso.with(getApplicationContext()).load(API_IMAGE_URL + Config.API_IMAGE_SIZE_W185 + movie.getPosterPath()).into(activityDetailImageView);
        activityDetailTitleTextview.setText(movie.getTitle());
        activityDetailDescriptionTextView.setText(movie.getOverview());
        activityDetailYearTextView.setText(movie.getReleaseDate());
        activityDetailMarkTextView.setText(movie.getVoteAverage().toString() + getString(R.string.rating_activity_detail));
        if (isInternetEnable()) {
            popularMovies = new PopularMovies();
            APIService apiService =
                    popularMovies.getClient().create(APIService.class);
            Call<TrailersResponse> callTrialers = apiService.getTrailers(movie.getId(), Config.API_KEY);
            callTrialers.enqueue(new Callback<TrailersResponse>() {
                @Override
                public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                    List<Trailer> trailersResponse = response.body().getResults();
                    if (trailersResponse.isEmpty()) {
                        activityDetailTrailersTextView.setVisibility(View.INVISIBLE);
                    } else {
                        trailers.clear();
                        trailers.addAll(trailersResponse);
                        trailersRecyclerViewAdapter.notifyDataSetChanged();
                        trailersRecyclerView.scrollToPosition(0);
                    }

                }

                @Override
                public void onFailure(Call<TrailersResponse> call, Throwable t) {
                    Timber.d("MoviesResponse onFailure");

                }
            });
            Call<ReviewsResponse> callReviews = apiService.getReviews(movie.getId(), Config.API_KEY);
            callReviews.enqueue(new Callback<ReviewsResponse>() {
                @Override
                public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                    List<Reviews> reviewsResponse = response.body().getResults();
                    if (reviewsResponse.isEmpty()) {
                        activityDetailReviewsTextView.setVisibility(View.INVISIBLE);
                    } else {
                        reviews.clear();
                        reviews.addAll(reviewsResponse);
                        reviewsRecyclerViewAdapter.notifyDataSetChanged();
                        reviewsRecyclerView.scrollToPosition(0);
                    }


                }

                @Override
                public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                    Timber.d("MoviesResponse onFailure");

                }
            });
        } else {
            Snackbar.make(
                    findViewById(R.id.activity_detail),
                    getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.no_internet_button), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadData(movie);
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
