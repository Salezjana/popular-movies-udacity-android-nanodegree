package mrodkiewicz.pl.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.model.Movie;
import timber.log.Timber;

/**
 * Created by pc-mikolaj on 21.02.2018.
 */

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder> {
    private List<Movie> movieList;
    private Context context;
    private String data[];

    public MoviesRecyclerViewAdapter(Context context, List<Movie> movieList) {
        this.movieList = movieList;
        this.context = context;
    }

    public MoviesRecyclerViewAdapter(Context context, String data[]) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movie, parent, false);
        Timber.d("onCreateViewHolder");

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Timber.d("onBindViewHolder");
        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            Timber.d("ViewHolder constructor");
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
