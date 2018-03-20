package mrodkiewicz.pl.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mrodkiewicz.pl.popularmovies.R;
import mrodkiewicz.pl.popularmovies.model.Reviews;
import mrodkiewicz.pl.popularmovies.model.Trailer;
import timber.log.Timber;

/**
 * Created by pc-mikolaj on 18.03.2018.
 */

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {
    private List<Reviews> reviewsList;
    private Context context;

    public ReviewsRecyclerViewAdapter(Context context, List<Reviews> reviewsList) {
        this.reviewsList = reviewsList;
        this.context = context;
    }

    @Override
    public ReviewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_review, parent, false);
        Timber.d("onCreateViewHolder");
        return new ReviewsRecyclerViewAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ReviewsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textViewAuthor.setText(reviewsList.get(position).getAuthor());
        holder.textViewContent.setText(reviewsList.get(position).getContent());
    }

    @Override
    public int getItemCount() {return reviewsList.size();}


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAuthor;
        TextView textViewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = (TextView) itemView.findViewById(R.id.item_list_author_review_textView);
            textViewContent = (TextView) itemView.findViewById(R.id.item_list_content_review_textView);
        }
    }
}
