package mrodkiewicz.pl.popularmovies.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.view.base.BaseAppCompatActivity;

public class DetailActivity extends BaseAppCompatActivity {
    public static final String EXTRAS_MOVIE_ID = "EXTRAS_MOVIE_ID";

    public static Intent getConfigureIntent(Context context, Integer movieId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRAS_MOVIE_ID, movieId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Test");

    }

}
