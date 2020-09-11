package com.example.news.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.news.R;
import com.example.news.models.Article;
import com.example.news.models.Specialist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ExpertsAdapter extends RecyclerView.Adapter<ExpertsAdapter.MyViewHolder> implements Filterable {
    //Variables
    private int LIMIT = 10;
    private String TAG = "ExpertsAdapter";
    private List<Specialist> specialistList;
    private List<Specialist> specialistsListFull;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ExpertsAdapter(List<Specialist> specialistList, Context context) {
        this.specialistList = specialistList;
        this.context = context;

        //sorting by popularity
        Collections.sort(this.specialistList, new CustomComparator());
        Collections.reverse(this.specialistList);

        specialistsListFull = new ArrayList<>(specialistList);

    }
    //Create Cards
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }
    //Bind Information to Cards
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        Specialist model = specialistList.get(position);

        //Change Text color/size to make it More Readable
        holder.author.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        holder.author.setText("bold");
        holder.mDateLayot.setVisibility(View.INVISIBLE);

        //Set Cards
        holder.title.setText(model.getProfile().getAffiliation_zh());
        holder.author.setText(model.getName_zh() + ", " + model.getName());
        holder.desc.setText(model.getProfile().getEdu());
        try {
            model.getProfile().getEmail();
            holder.source.setText(model.getProfile().getEmail());
        } catch (NullPointerException e) {
            holder.source.setText("No Email");
        }
        holder.time.setText("Views: " + model.getNum_viewed());

        //Images
        Glide.with(context).load(model.getAvatar()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.GONE);
                return false;
            }
        })
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(holder.imageView);
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Specialist> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filterList.addAll(specialistsListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Specialist specialist : specialistsListFull) {
                    if (specialist.getName().toLowerCase().contains(filterPattern) ||
                            specialist.getName_zh().toLowerCase().contains(filterPattern))
                            filterList.add(specialist);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            specialistList.clear();
            specialistList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    //Return Number of Events that Want to Be Passed
    @Override
    public int getItemCount(){
        if(specialistList.size() > LIMIT) { return LIMIT; }
        else { return specialistList.size(); }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Views
        TextView title, desc, author, publisher, source, time;
        ImageView imageView, shadowImageView, dateIconImageView;
        CardView cardView;
        FrameLayout mDateLayot;
        ProgressBar progressBar;
        OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);

            //Find Views
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
            mDateLayot = itemView.findViewById(R.id.layoutDate);
            dateIconImageView = itemView.findViewById(R.id.cardViewDateIcon);
            this.onItemClickListener = onItemClickListener;
        }
        @Override
        public void onClick(View v) {
            this.onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    //increasing limit
    public void setLimit(int limit) { this.LIMIT = limit; }

    //custom comparator
    public class CustomComparator implements Comparator<Specialist> {
        @Override
        public int compare(Specialist o1, Specialist o2) {
            int o1Int = Integer.parseInt(o1.getNum_viewed());
            int o2Int = Integer.parseInt(o2.getNum_viewed());

            if (o1Int == o2Int) return 0;
            else if (o1Int > o2Int) return 1;
            else return -1;
        }
    }
}
