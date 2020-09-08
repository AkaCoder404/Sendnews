package com.example.news.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.data.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    //Variables
    private int limit = 10;
    private String TAG = "HistoryAdapter";
    private List<History> histories = new ArrayList<>();
    private Context context;
    private OnItemClickListener onItemClickListener;

    public HistoryAdapter(List<History> histories, Context context) {
        this.histories = histories;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }
    // Binds Information to Cards
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        Log.d(TAG, "Creating Adapter " + position);
        History model = histories.get(position);

        holder.title.setText("Title: " + model.getTitle());
        holder.desc.setText("ID: " + model.getContentPrimaryId());
        holder.time.setText(model.getDate().substring(0, 10).replace("-","/"));
        holder.source.setText("Type: " + model.getType());
        holder.publisher.setText("Temp");
        holder.author.setText("Temp");

        // Since There are No Images
        holder.imageView.setVisibility(View.GONE);
        holder.shadowImageView.setVisibility(View.GONE);
        holder.progressBar.setVisibility(View.GONE);

    }
    @Override
    public int getItemCount(){
        return histories.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Views
        TextView title, desc, author, publisher, source, time;
        ImageView imageView, shadowImageView;
        CardView cardView;
        ProgressBar progressBar;
        OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            // Find Views
            cardView = itemView.findViewById(R.id.article_cardView);
            title = itemView.findViewById(R.id.card_title);
            desc = itemView.findViewById(R.id.desc);
            author = itemView.findViewById(R.id.card_author_textView);
            source = itemView.findViewById(R.id.card_source);
            publisher = itemView.findViewById(R.id.publishedAt);
            time = itemView.findViewById(R.id.card_time);
            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.card_progressBar);
            shadowImageView = itemView.findViewById(R.id.card_shadow_bottom);
            this.onItemClickListener = onItemClickListener;
        }
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Item Clicked " + getAdapterPosition());
            this.onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
        notifyDataSetChanged();
    }
}
