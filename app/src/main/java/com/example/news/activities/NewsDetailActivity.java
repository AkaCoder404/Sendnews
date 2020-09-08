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
    private ImageView imageView;
    private TextView appbar_title, appbar_subtitle, date, time, title;
    private TextView textView;
    private FrameLayout date_behavior;
    private LinearLayout titleAppbar;
    private AppBarLayout appBarLayout;
    private Toolbar mtoolbar;
    private ProgressBar progressBar;
    private WebView webView;
    //Variables
    private boolean isHideToolBarView = false;
    private String mContentPrimaryId, mCategory, mContent, mDate, mEntities, mId, mInfluence, mLang, mRelatedEvents, mSegText, mSource,
            mTflag, mTime, mTitle, mType, mUrl, mUrls, mStatus;
    private String TAG = "NewsDetailActivity";

    //History
    private HistoryViewModel historyViewModel;

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
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progress_bar);

        Glide.with(this).load("https://i.imgur.com/bIRGzVO.jpg").transition(DrawableTransitionOptions.withCrossFade()).into(imageView);

        //Receive Intent
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mTitle = intent.getStringExtra("title");
        mDate = intent.getStringExtra("date");

        //Update View
        title.setText(mTitle);
        date.setText(mDate);
        appbar_title.setText(mTitle);
        LoadJson(mUrl);

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
                Log.d(TAG, "On Response Started for " + "https://covid-dashboard-api.aminer.cn/event/" + id + " Response Body: " + response.body() + " " + response);
                if(response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Response is Successful");
                    //Get Information --> deal with lists later
                    ArticleContent body = response.body();
                    mContentPrimaryId   = body.getData().get_id();
                    mCategory           = body.getData().getCategory();
                    mContent            = body.getData().getContent();
                    mDate               = body.getData().getTime();
                    // mEntities           = body.getData().getEntitiesList()
                    // mGeoInfo            = body.getData().getGeoInfo()
                    mId                 = body.getData().getId();
                    mInfluence          = body.getData().getInfluence();
                    mLang               = body.getData().getLanguage();
                    // mRelatedEvents   = body.getData().getRelatedEvents();
                    mSegText            = body.getData().getSeg_text();
                    mSource             = body.getData().getSource();
                    mTflag              = body.getData().getTflag();
                    mTime               = body.getData().getDate();
                    mType               = body.getData().getType();
                    // mUrls            = body.getDATA().getUrls();
                    mStatus             = body.getStatus();

                    // Show Information
                    textView.setText(mContent);

                    // show how long ago article was posted
                    mDate = mDate.substring(0, 10) + "T" + mTime.substring(17,25) + "Z";
                    mDate = mDate.replace('/', '-');
                    time.setText(Utils.DateToTimeFormat(mDate));
                    date.setText(mTime.substring(0, 16));

                    //WebView if there is
                    //Url
                    if (body.getData().getUrlsList().size() != 0) {
                        mUrl = body.getData().getUrlsList().get(0);
                        try {
                            initWebView((mUrl));
                        }
                        catch (Exception e) {
                            Log.d(TAG, "cannot connect to webpage");
                            webView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    else  {
                        webView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }

                    //Test Output
                    try {
                        System.out.println("Article\n" +
                                "_id        :" + body.getData().get_id() + "\n" +
                                "category   :" + body.getData().getCategory() + "\n" +
                                "content    :" + body.getData().getContent() + "\n" +
                                //"date       :" + body.getData().getEntitiesList().get(0).getLabel() + "," + body.getData().getEntitiesList().get(0).getUrl() + "\n" +
                                "geoInfo    :" + body.getData().getGeoInfoList().get(0).getGeoName() + "\n" +
                                "id         :" + body.getData().getInfluence() + "\n" +
                                //"related eve:" + body.getData().getRelatedEventsList().get(0).getId() + "," + body.getData().getRelatedEventsList().get(0).getScore() + "\n" +
                                "seg_text   :" + body.getData().getSeg_text() + "\n" +
                                "source     :" + body.getData().getSource() + "\n" +
                                "tflag      :" + body.getData().getTflag() + "\n" +
                                "time       :" + body.getData().getTime() + "\n" +
                                "title      :" + body.getData().getTitle() + "\n" +
                                "type       :" + body.getData().getType() + "\n" +
                                "urls       :" + body.getData().getUrlsList().get(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //If Components are Empty
                    if(mSource.equals("")) { appbar_subtitle.setText("No Source"); }
                    else { appbar_subtitle.setText(mSource); }

                    if(mContent.equals("")) { textView.setText("No Content to be Shown"); }
                    else {textView.setText(mContent); }

                    //Store Info
                    historyViewModel.insert(new History(mContentPrimaryId, mCategory, mContent, mDate, mId, mInfluence, mLang, mSegText, mSource, mTime, mTitle, mType, mStatus));

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
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
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
