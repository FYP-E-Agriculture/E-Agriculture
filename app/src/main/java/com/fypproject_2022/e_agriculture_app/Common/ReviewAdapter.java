package com.fypproject_2022.e_agriculture_app.Common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fypproject_2022.e_agriculture_app.Models.Review;
import com.fypproject_2022.e_agriculture_app.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>{

    List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override

    public ReviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_layout, parent, false);
        ReviewAdapter.MyViewHolder myViewHolder = new ReviewAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.MyViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.name.setText(review.getReviewerName());
        holder.description.setText(review.getDescription());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            description =itemView.findViewById(R.id.description);
        }
    }
}
