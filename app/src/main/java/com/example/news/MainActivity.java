package com.example.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Variables
    private static int SPLASH_SCREEN = 2000;
    Toolbar toolbar;
    ProgressBar progressBar;
    TextView textView;
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView slogan;
    SQLiteDatabase allNewsDataBase;
    SQLiteDatabase newsHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Animations
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        image = findViewById(R.id.loading_image);
        slogan = findViewById(R.id.textView2);
        progressBar = findViewById(R.id.progressBar);

        // progressBar.setMax(100);
        image.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                //progressAnimation();
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
    public void progressAnimation() {
        ProgressBarAnimation anim = new ProgressBarAnimation(this,progressBar,textView,0,100f);
        anim.setDuration(500);
        progressBar.setAnimation(anim);
    }

}