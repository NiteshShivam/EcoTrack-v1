package com.example.ecotrack_v1.leaderBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecotrack_v1.R;

import java.util.List;

public class ScoreAdapter  extends RecyclerView.Adapter<ScoreAdapter.ScoreViewAdapter> {
    List<ScoreData> list;
    Context context;

    public ScoreAdapter(List<ScoreData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ScoreViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.score_list_item,parent,false);
        return new ScoreViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewAdapter holder, int position) {
            ScoreData currentItem = list.get(position);
            holder.name.setText(currentItem.getFullname());
            holder.green_point.setText(String.valueOf(currentItem.getGreenPoints()));
            holder.rank.setText(String.valueOf(position));
          //  Glide.with(context).load(currentItem.getImage()).into(holder.imageView);
          //  i++;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ScoreViewAdapter extends RecyclerView.ViewHolder {
      //  ImageView imageView;
        TextView name,green_point,rank;

        public ScoreViewAdapter(@NonNull View itemView) {
            super(itemView);
         //   imageView = itemView.findViewById(R.id.score_user_img);
            name = itemView.findViewById(R.id.score_user_name);
            green_point = itemView.findViewById(R.id.score_user_green_point);
            rank = itemView.findViewById(R.id.score_user_rank);
        }
    }
}
