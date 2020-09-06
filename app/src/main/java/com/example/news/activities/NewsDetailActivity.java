package com.example.news.activities;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.news.R;

public class NewsDetailActivity extends AppCompatActivity {
    //Variables
    private ImageView imageView;
    private TextView appbar_title, appbar_subtitle, date, time, title;
    private boolean isHideToolBarView = false;
    private FrameLayout date_behavior;
    private LinearLayout titleAppbar;
    private Toolbar mtoolbar;
    private String mUrl, mImg, mTitle, mDate, mSource, mAuthor;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        //Change Toolbar
        mtoolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(mtoolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
