package mrodkiewicz.pl.popularmovies.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.helpers.Config;
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
        Timber.d("api poster load url: " + Config.API_IMAGE_URL + Config.API_IMAGE_SIZE_W185 + movieList.get(position).getPosterPath());

        if (movieList.size()-1  == position) {
            Timber.d("last element" + position);
            holder.textView.setText("NEXT PAGE PLESE");
            
            holder.imageView.setImageDrawable(new ColorDrawable(context.getResources().getColor(R.color.colorSecondary)));
        } else {
            Picasso.with(context)
                    .load(Config.API_IMAGE_URL + Config.API_IMAGE_SIZE_W185 + movieList.get(position).getPosterPath())
                    .into(holder.imageView);
            holder.textView.setText(movieList.get(position).getTitle());
        }


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            Timber.d("ViewHolder constructor");
            imageView = (ImageView) itemView.findViewById(R.id.item_list_movie_imageView);
            textView = (TextView) itemView.findViewById(R.id.item_list_movie_textView);
        }
    }
}
