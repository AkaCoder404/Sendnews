package com.example.news.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.news.R;
import com.example.news.models.KnowledgeEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KnowledgeGraphAdapter extends RecyclerView.Adapter <KnowledgeGraphAdapter.MyViewHolder>
    implements Filterable {
    //Variables
    private int LIMIT = 10;
    private String TAG = "KnowledgeGrapherAdapter";
    private List<KnowledgeEntity> knowledgeEntityList;
    private List<KnowledgeEntity> knowledgeEntityListFull;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private SharedPreferences sharedPreferences;

    public KnowledgeGraphAdapter(List<KnowledgeEntity> knowledgeEntities, Context context) {
        this.knowledgeEntityList = knowledgeEntities;
        this.context = context;

        knowledgeEntityListFull = new ArrayList<>(knowledgeEntities);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(view, onItemClickListener);
    }

    //bind information to cards
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        KnowledgeEntity model = knowledgeEntityList.get(position);
        //set cards
        holder.mDateLayout.setVisibility(View.INVISIBLE);
        holder.mTitle.setText(model.getLabel());
        String enwiki = model.getAbstractInfo().getEnwiki();
        String baidu = model.getAbstractInfo().getBaidu();
        String zhwiki = model.getAbstractInfo().getZhwiki();
        String source = new String("NO SOURCE");
        if( enwiki != null && !enwiki.equalsIgnoreCase("")) {
            holder.mDescription.setText(enwiki);
            source = "enwiki";
        }
        else if (baidu != null && !baidu.equalsIgnoreCase("")) {
            holder.mDescription.setText(baidu);
            source = "baidu";
        }
        else if (zhwiki != null && !zhwiki.equalsIgnoreCase("")) {
            holder.mDescription.setText(zhwiki);
            source = "zhwiki";
        }

        holder.mSource.setText(source);
        if (model.getHot().length() >= 5) {
            holder.mTime.setText("Hot: " + model.getHot().substring(0, 6));
        }
        else {
            holder.mTime.setText("Hot: " + model.getHot());
        }


        //Images
        if(model.getImg() != null && !model.getImg().equalsIgnoreCase("")) {
            Glide.with(context).load(model.getImg()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.mProgressBar.setVisibility(View.GONE);
                    return false;
                }
                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.mProgressBar.setVisibility(View.GONE);
                    return false;
                }
            }).transition(DrawableTransitionOptions.withCrossFade()).into(holder.mImageView);
        }
        else {
            holder.mImageView.setVisibility(View.GONE);
            holder.mShadowImageView.setVisibility(View.GONE);
            holder.mProgressBar.setVisibility(View.GONE);
        }
    }

    //limit how many cards are shown
    @Override
    public int getItemCount() {
       return knowledgeEntityList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //search and filter functionality
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    public Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<KnowledgeEntity> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filterList.addAll(knowledgeEntityListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (KnowledgeEntity knowledgeEntity : knowledgeEntityListFull) {
                    if (knowledgeEntity.getLabel().toLowerCase().contains(filterPattern)) {
                        filterList.add(knowledgeEntity);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            knowledgeEntityList.clear();
            knowledgeEntityList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Views
        TextView mTitle, mDescription, mAuthor, mPublisher, mSource, mTime;
        ImageView mImageView, mShadowImageView;
        CardView mCardView;
        ProgressBar mProgressBar;
        FrameLayout mDateLayout;
        OnItemClickListener onItemClickListener;

        public MyViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);

            //Find Views
            mCardView = itemView.findViewById(R.id.article_cardView);
            mTitle = itemView.findViewById(R.id.card_title);
            mDescription = itemView.findViewById(R.id.desc);
            mAuthor = itemView.findViewById(R.id.card_author_textView);
            mSource = itemView.findViewById(R.id.card_source);
            mPublisher = itemView.findViewById(R.id.publishedAt);
            mTime = itemView.findViewById(R.id.card_time);
            mImageView = itemView.findViewById(R.id.img);
            mProgressBar = itemView.findViewById(R.id.card_progressBar);
            mShadowImageView = itemView.findViewById(R.id.card_shadow_bottom);
            mDateLayout = itemView.findViewById(R.id.layoutDate);
            this.onItemClickListener = onItemClickListener;
        }
        @Override
        public void onClick(View v) {
            this.onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    //sort array
    public class CustomComparator implements Comparator<KnowledgeEntity> {
        @Override
        public int compare(KnowledgeEntity o1, KnowledgeEntity o2) {
            return o1.getHot().compareTo(o2.getHot());
        }
    }
    //set limit
    public void setLimit(int limit) {
        this.LIMIT = limit;
    }

}
