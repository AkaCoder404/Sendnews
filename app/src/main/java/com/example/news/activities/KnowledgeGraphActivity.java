package com.example.news.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.news.R;
import com.example.news.Utils;
import com.example.news.models.Relations;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class KnowledgeGraphActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    //Variables
    private String mHotString, mLabelString, mUrlString, mImgString;
    private boolean isHideToolBarView = false;
    //View
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mImageView;
    private TextView mTitle, mContent, mAppBarLayoutTitle, mAppBarLayoutSubtitle, mDate;
    private String mEnwikiString, mBaiduString, mZhwikiString,  mDefinition,
            mCharacteristics, mIncludes, mConditions, mTranfer, mUses;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_graph);

        mToolbar = findViewById(R.id.knowledgeGraphToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbarLayout = findViewById(R.id.knowledgeGraphCollapsingToolbarLayout);
        mCollapsingToolbarLayout.setTitle("");

        mAppBarLayout = findViewById(R.id.knowledgeGraphAppBarLayout);
        mAppBarLayout.addOnOffsetChangedListener(this);
        mAppBarLayoutTitle = findViewById(R.id.knowledgeGraphAppBarTitle);
        mAppBarLayoutSubtitle = findViewById(R.id.knowledgeGraphAppBarSubtitle);
        mImageView = findViewById(R.id.knowledgeGraphBackdrop);
        mTitle = findViewById(R.id.knowledgeGraphTitle);
        mContent = findViewById(R.id.knowledgeGraphContent);
        mDate = findViewById(R.id.knowledgeGraphTime);


        //retrieve intent
        Intent intent = getIntent();
        mHotString = intent.getStringExtra("hot");
        mLabelString = intent.getStringExtra("label");
        mUrlString = intent.getStringExtra("url");
        mImgString = intent.getStringExtra("img");

        mEnwikiString = intent.getStringExtra("enwiki");
        mBaiduString  = intent.getStringExtra("baidu");
        mZhwikiString = intent.getStringExtra("zhwiki");
        mDefinition = intent.getStringExtra("定义");
        mCharacteristics = intent.getStringExtra("特征");
        mIncludes = intent.getStringExtra("包括");
        mConditions = intent.getStringExtra("生存条件");
        mTranfer = intent.getStringExtra("传播方式");
        mUses = intent.getStringExtra("应用");



        //Set Intents

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .load(mImgString)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mImageView);

        //update view
        mAppBarLayoutTitle.setText(mLabelString);
        mAppBarLayoutSubtitle.setText(mUrlString);
        mTitle.setText(mLabelString);
        mDate.setText(mUrlString);

        mContent.setText("Abstract Information: \n" +
                "enwiki: " + mEnwikiString + "\n" +
                "baidu: " + mBaiduString + "\n" +
                "zhwiki: " + mZhwikiString + "\n\n" +
                mLabelString + "的 Properties: \n"+
                "定义: " + mDefinition + "\n" +
                "特征: " + mCharacteristics  + "\n" +
                "包括: " + mIncludes  + "\n" +
                "生存条件: " + mConditions  + "\n" +
                "传播方式: " + mTranfer + "\n" +
                "应用: " + mUses
        );

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Shared
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
            } catch (Exception e) {
                Toast.makeText(this, "Can't Share", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //collapsing toolbar
    @Override
    public void onOffsetChanged(final AppBarLayout appBarLayout, final int verticalOffset) {
        mAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

                if (percentage == 1f && isHideToolBarView) {
                    mAppBarLayoutTitle.setVisibility(View.VISIBLE);
                    mAppBarLayoutSubtitle.setVisibility(View.VISIBLE);
                    isHideToolBarView = !isHideToolBarView;
                } else if (percentage < 1f && !isHideToolBarView) {
                    mAppBarLayoutTitle.setVisibility(View.GONE);
                    mAppBarLayoutSubtitle.setVisibility(View.GONE);
                    isHideToolBarView = !isHideToolBarView;
                }
            }
        });
    }
}

