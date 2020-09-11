package com.example.news.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.news.R;
import com.example.news.Utils;
import com.example.news.api.Api;
import com.example.news.api.NewsDetailApiInterface;

import com.example.news.data.History;
import com.example.news.data.HistoryViewModel;
import com.example.news.models.ArticleContent;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;


import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    //Variables View/Layout
    private ImageView mImageView;
    private TextView mAppBarTitle, mAppBar, mSubtitle, mDate, mTime, mTitle, mTextView;
    private FrameLayout mDateBehavior;
    private LinearLayout mTitleAppbar;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private WebView mWebView;
    //Variables
    private boolean isHideToolBarView = false;
    private String mContentPrimaryIdString, mCategoryString, mContentString, mDateString, mEntitiesString,
            mIdString, mInfluenceString, mLangString, mRelatedEventsString, mSegTextString, mSourceString,
            mTflagString, mTimeString, mTitleString, mTypeString, mUrlString, mUrlsString, mStatusString;
    private String TAG = "NewsDetailActivity";

    //History
    private HistoryViewModel historyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        //Find View
        mToolbar = findViewById(R.id.activity_news_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        mAppBarLayout = findViewById(R.id.appbar);
        mAppBarLayout.addOnOffsetChangedListener(this);
        mDateBehavior = findViewById(R.id.date_behavior);
        mTitleAppbar = findViewById(R.id.title_appbar);
        mImageView = findViewById(R.id.backdrop);
        mAppBarTitle = findViewById(R.id.title_on_appbar);
        mSubtitle = findViewById(R.id.subtitle_on_appbar);
        mDate = findViewById(R.id.date);
        mTime = findViewById(R.id.time);
        mTitle = findViewById(R.id.title);
        mTextView = findViewById(R.id.news_detail_textView);
        mWebView = findViewById(R.id.webView);
        mProgressBar = findViewById(R.id.progress_bar);

//        https://i.imgur.com/bIRGzVO.jpg

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .load("")
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mImageView);

//        Glide.with(this).load("https://i.imgur.com/bIRGzVO.jpg").transition(DrawableTransitionOptions.withCrossFade()).into(mImageView);

        //Receive Intent
        Intent intent = getIntent();
        mUrlString = intent.getStringExtra("url");
        mTitleString = intent.getStringExtra("title");
        mDateString = intent.getStringExtra("date");

        //Update View
        mTitle.setText(mTitleString);
        mDate.setText(mDateString);
        mAppBarTitle.setText(mTitleString);
        LoadJson(mUrlString);

        //Set to History Database When Read
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
    }

    //WebView --> maybe implement

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
    public void onOffsetChanged(final AppBarLayout appBarLayout, final int verticalOffset) {
        mAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

                if (percentage == 1f && isHideToolBarView) {
                    mDateBehavior.setVisibility(View.GONE);
                    mTitleAppbar.setVisibility(View.VISIBLE);
                    isHideToolBarView = !isHideToolBarView;
                } else if (percentage < 1f && !isHideToolBarView) {
                    mDateBehavior.setVisibility(View.VISIBLE);
                    mTitleAppbar.setVisibility(View.GONE);
                    isHideToolBarView = !isHideToolBarView;
                }

            }
        });

    }
    //load specific article
    public void LoadJson(final String id){
        NewsDetailApiInterface apiInterface = Api.getApiClient("https://covid-dashboard-api.aminer.cn/event/").create(NewsDetailApiInterface.class);
        Call<ArticleContent> articleContentCall = apiInterface.getArticleContent(id);
        articleContentCall.enqueue(new Callback<ArticleContent>() {
            @Override
            public void onResponse(Call<ArticleContent> call, Response<ArticleContent> response) {
                // Log.d(TAG, "On Response Started for " + "https://covid-dashboard-api.aminer.cn/event/" + id + " Response Body: " + response.body() + " " + response);
                if(response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Response is Successful");
                    //Get Information --> deal with lists later
                    ArticleContent body = response.body();
                    mContentPrimaryIdString   = body.getData().get_id();
                    mCategoryString           = body.getData().getCategory();
                    mContentString            = body.getData().getContent();
                    mDateString               = body.getData().getTime();
                    // mEntities           = body.getData().getEntitiesList()
                    // mGeoInfo            = body.getData().getGeoInfo()
                    mIdString                 = body.getData().getId();
                    mInfluenceString          = body.getData().getInfluence();
                    mLangString               = body.getData().getLanguage();
                    // mRelatedEvents   = body.getData().getRelatedEvents();
                    mSegTextString            = body.getData().getSeg_text();
                    mSourceString             = body.getData().getSource();
                    mTflagString        = body.getData().getTflag();
                    mTimeString         = body.getData().getDate();
                    mTypeString         = body.getData().getType();
                    // mUrls            = body.getDATA().getUrls();
                    mStatusString       = body.getStatus();

                    // Show Information
                    mTextView.setText(mContentString);

                    // show how long ago article was posted
                    mDateString = mDateString.substring(0, 10) + "T" + mTimeString.substring(17,25) + "Z";
                    mDateString = mDateString.replace('/', '-');
                    mTime.setText(Utils.DateToTimeFormat(mDateString));
                    mDate.setText(mTimeString.substring(0, 16));

                    //WebView if there is
                    //Url
                    if (body.getData().getUrlsList().size() != 0) {
                        mUrlString = body.getData().getUrlsList().get(0);
                        try {
                            initWebView((mUrlString));
                        }
                        catch (Exception e) {
                            Log.d(TAG, "cannot connect to webpage");
                            mWebView.setVisibility(View.GONE);
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                    else  {
                        mWebView.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.GONE);
                    }

                    //If Components are Empty
                    if (mSourceString != null && mSourceString.equalsIgnoreCase("")) {
                        mSubtitle.setText(mSourceString);
                    }
                    else {
                        mSubtitle.setText("No Source");
                    }
                    if (mContentString != null && mContentString.equalsIgnoreCase("")) {
                        mTextView.setText(mContentString);
                    }
                    else {
                        mTextView.setText("No Content to be Shown");
                    }

                    //Store Info
                    historyViewModel.insert(new History(mContentPrimaryIdString, mCategoryString, mContentString, mDateString, mIdString, mInfluenceString,
                            mLangString, mSegTextString, mSourceString, mTimeString, mTitleString, mTypeString, mStatusString));

                }
                else {
                    Log.d(TAG, "was not able to load news");
                }
            }
            @Override
            public void onFailure(Call<ArticleContent> call, Throwable t) {
                Log.d(TAG, "Connection Failure");
            }
        });
    }

    private void initWebView(String url){
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(url);
    }
    //share
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }
    //add options if necessary
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, mSourceString);
                String body = mTitle + "\n\n"  + "API URL: https://covid-dashboard-api.aminer.cn/event/" + mUrlString + "\n\n" + mDate + "\n\n" +  mContentString;
                i.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(i, "Share with : "));
            } catch (Exception e)  {
                Toast.makeText(this, "Can not be shared", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
