package mrodkiewicz.pl.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.model.Trailer;
import timber.log.Timber;

/**
 * Created by pc-mikolaj on 18.03.2018.
 */

public class TrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.ViewHolder> {
    private List<Trailer> trailersList;
    private Context context;

    public TrailersRecyclerViewAdapter(Context context, List<Trailer> trailersList) {
        this.trailersList = trailersList;
        this.context = context;
    }

    @Override
    public TrailersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_trailer, parent, false);
        Timber.d("onCreateViewHolder");
        return new TrailersRecyclerViewAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(TrailersRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textViewTitle.setText(trailersList.get(position).getName());
    }

    @Override
    public int getItemCount() {return trailersList.size();}


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.item_list_title_trailer_textView);

        }
    }
}
