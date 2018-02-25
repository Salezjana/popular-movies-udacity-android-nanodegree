package mrodkiewicz.pl.popularmovies.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
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
import mrodkiewicz.pl.popularmovies.view.MainActivity;
import timber.log.Timber;

/**
 * Created by pc-mikolaj on 21.02.2018.
 */

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder> {
    private List<Movie> movieList;
    private Context context;
    private String data[];
    private int page;

    public MoviesRecyclerViewAdapter(Context context, List<Movie> movieList,int page) {
        this.movieList = movieList;
        this.context = context;
        this.page = page;
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

        if (movieList.get(position).getTitle() == null) {
            if (position == 0){
                Timber.d("last element" + position);
                holder.textView.setText("NEXT PAGE PLESE");
                holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.colorSecondary));
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_white_24dp));
            }else {
                Timber.d("last element" + position);
                holder.textView.setText("NEXT PAGE PLESE");
                holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.colorTextBlack));
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_white_24dp));
            }

        }else {
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
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            Timber.d("ViewHolder constructor");
            imageView = (ImageView) itemView.findViewById(R.id.item_list_movie_imageView);
            textView = (TextView) itemView.findViewById(R.id.item_list_movie_textView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

        }
    }

    public void setPage(int page) {
        this.page = page;
    }
}
