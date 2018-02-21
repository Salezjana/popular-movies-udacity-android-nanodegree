package mrodkiewicz.pl.popularmovies.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.view.base.BaseAppCompatActivity;

public class DetailActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Test");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(
                        view,
                        getString(R.string.no_internet),
                        Snackbar.LENGTH_INDEFINITE)
                        .show();
            }
        });
    }

}
