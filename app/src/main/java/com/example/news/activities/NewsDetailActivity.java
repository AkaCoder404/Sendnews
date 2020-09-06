package com.example.news.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.news.R;
import com.example.news.Utils;
import com.example.news.api.Api;
import com.example.news.api.NewsDetailApiInterface;

import com.example.news.models.ArticleContent;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    //Variables
    private ImageView imageView;
    private TextView appbar_title, appbar_subtitle, date, time, title;
    private TextView textView;
    private FrameLayout date_behavior;
    private LinearLayout titleAppbar;
    private AppBarLayout appBarLayout;
    private Toolbar mtoolbar;

    private boolean isHideToolBarView = false;
    private String mUrl, mImg, mTitle, mDate, mSource, mAuthor, mContent, mTime;
    private String TAG_NewsDetailActivity = " News Detail Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        //Find View
        mtoolbar = findViewById(R.id.activity_news_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);
        date_behavior = findViewById(R.id.date_behavior);
        titleAppbar = findViewById(R.id.title_appbar);
        imageView = findViewById(R.id.backdrop);
        appbar_title = findViewById(R.id.title_on_appbar);
        appbar_subtitle = findViewById(R.id.subtitle_on_appbar);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);
        textView = findViewById(R.id.news_detail_textView);

        Glide.with(this).load("https://i.imgur.com/bIRGzVO.jpg").transition(DrawableTransitionOptions.withCrossFade()).into(imageView);


        //Receive Intent
        Intent intent = getIntent();
        Log.d(TAG_NewsDetailActivity, "Received Intent");

        mUrl = intent.getStringExtra("url");
        mTitle = intent.getStringExtra("title");
        mDate = intent.getStringExtra("date");
        // textView.setText(mUrl + mTitle);

        //Update View
        title.setText(mTitle);
        date.setText(mDate);
        appbar_title.setText(mTitle);
        LoadJson(mUrl);


    }

    //WebView --> maybe implement
    private void initWebView(String url) { }


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
    //Offset --> bugs
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        if (percentage == 1f && isHideToolBarView) {
            date_behavior.setVisibility(View.GONE);
            titleAppbar.setVisibility(View.VISIBLE);
            isHideToolBarView = !isHideToolBarView;
        } else if (percentage < 1f && !isHideToolBarView) {
            date_behavior.setVisibility(View.VISIBLE);
            titleAppbar.setVisibility(View.GONE);
            isHideToolBarView = !isHideToolBarView;
        }
    }
    //Load Article Page Specific
    public void LoadJson(final String id){
        NewsDetailApiInterface apiInterface = Api.getApiClient("https://covid-dashboard-api.aminer.cn/event/").create(NewsDetailApiInterface.class);
        Call<ArticleContent> articleContentCall = apiInterface.getArticleContent(id);

        articleContentCall.enqueue(new Callback<ArticleContent>() {
            @Override
            public void onResponse(Call<ArticleContent> call, Response<ArticleContent> response) {
                Log.d(TAG_NewsDetailActivity, "On Response Started for " + "https://covid" +
                        "-dashboard-api.aminer.cn/event/" + id + " Response Body: " + response.body() + " " + response);
                if(response.isSuccessful() && response.body() != null) {
                    Log.d(TAG_NewsDetailActivity, "Response is Successful");
                    // Get Info
                    ArticleContent body = response.body();
                    mTitle = response.body().getData().getTitle();
                    mSource = response.body().getData().getSource();
                    mContent = response.body().getData().getContent();
                    mDate = response.body().getData().getTime();
                    mTime = response.body().getData().getDate();

                    textView.setText(mContent);

                    mDate = mDate.substring(0, 10) + "T" + mTime.substring(17,25) + "Z";
                    time.setText(Utils.DateToTimeFormat(mDate));
                    date.setText(mTime.substring(0, 16));


                    if(mSource.equals("")) {
                        appbar_subtitle.setText("No Source");
                    }
                    else {
                        appbar_subtitle.setText(mSource);
                    }

                }
                else {
                    Log.d(TAG_NewsDetailActivity, "Was Not Able to Load");
                }
            }
            @Override
            public void onFailure(Call<ArticleContent> call, Throwable t) {
                Log.d(TAG_NewsDetailActivity, "Connection Failure");
            }
        });
    }

    //Share
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, mSource);
                String body = mTitle + "\n"  + "https://covid-dashboard-api.aminer.cn/event/" + mUrl + "\n" + mDate + "\n \n" +  mContent;
                i.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(i, "Share with : "));

            } catch (Exception e)  {
                Toast.makeText(this, "Can not be shared", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);

    }



}
