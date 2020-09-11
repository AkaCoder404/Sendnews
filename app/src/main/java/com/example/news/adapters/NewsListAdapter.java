package com.example.news.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.news.R;
import com.example.news.Utils;
import com.example.news.models.Article;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.MyViewHolder> implements Filterable {
    // Variables
    private int limit = 10;
    private String TAG = "NewsListAdapter";
    private List<Article> articles;
    private List<Article> articlesFull;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private SharedPreferences sharedPreferences;


    public NewsListAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;



        //Sort By Date --> Make this into function later
        Collections.sort(this.articles, new CustomComparator());
        Collections.reverse(this.articles);

        articlesFull = new ArrayList<>(articles);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }
    // Binds Information to Cards
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        Article model = articles.get(position);
        //Request Options
//        requestOptions = new RequestOptions();
//        requestOptions.centerCrop().placeholder(R.mipmap.ic_send_news).error(R.mipmap.ic_send_news);
//        requestOptions.placeholder(Utils.getRandomDrawbleColor());
//        requestOptions.error(Utils.getRandomDrawbleColor());
//        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
//        requestOptions.centerCrop();
        //Set Cards
        holder.title.setText("Title: " + model.getTitle());
        holder.desc.setText("ID: " + model.getId());
        holder.time.setText(model.getTime());
        holder.source.setText(model.getType());
        holder.publisher.setText("");
        holder.author.setText("");
        //Glide.with(context).load("https://i.imgur.com/bIRGzVO.jpg").into(holder.imageView);

        // Since There are No Images
        holder.imageView.setVisibility(View.GONE);
        holder.shadowImageView.setVisibility(View.GONE);
        holder.progressBar.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        //Limit Number of Articles
        if (articles.size() > limit) {
            return limit;
        } else {
            return articles.size();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Article> filterList = new ArrayList<>();
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String category = sharedPreferences.getString("CATEGORYFILTER", context.getString(R.string.settings_all_label));
            System.out.println(category);

            if (charSequence == null) {
                filterList.addAll(articlesFull);
            } else if (category.equalsIgnoreCase("ALL CATEGORIES")) {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Article article : articlesFull) {
                    if (article.getTitle().toLowerCase().contains(filterPattern)) {
                        filterList.add(article);
                    }
                }
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Article article : articlesFull) {
                    if (article.getTitle().toLowerCase().contains(filterPattern) && article.getType().equalsIgnoreCase(category)) {
                        filterList.add(article);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            articles.clear();
            articles.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

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

            this.onItemClickListener = onItemClickListener;
        }
        @Override
        public void onClick(View v) {
            this.onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public class CustomComparator implements Comparator<Article> {
        @Override
        public int compare(Article o1, Article o2) {
            return o1.getTime().compareTo(o2.getTime());
        }

    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


}
