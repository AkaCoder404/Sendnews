package com.example.news.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.news.R;
import com.example.news.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class SpecialistActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private String TAG = "SpecialistActivity";
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    // private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mImageView;
    private TextView mTitle, mContent, mAppBarLayoutTitle, mAppBarLayoutSubtitle, mDate, mSubtitle;
    private String mAvatarString, mBindString, mIdString, mActivityString, mCitationsString, mDiversityString, mGindexString,
    mHindexString, mNewStarString, mPubsString, mRisingStarString, mSociabilityString, mName, mName_zh, mNumFollowed, mNumViewed,
    mProfileAddressString, mProfileAffiliationString, mProfileAffiliationZhString,
    mProfileBioString, mEduString, mEmailString, mEmailCrString, mHomePageString, mFaxString,
    mNoteString, mPhoneString, mPositionString, mWorkString, mIsPassedAway;
    private boolean isHideToolBarView = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialists);

        mToolbar = findViewById(R.id.specialistsToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.specialistsCollapsingToolbarLayout);
        mCollapsingToolbarLayout.setTitle("");

        mAppBarLayout = findViewById(R.id.specialistsAppBarLayout);
        mAppBarLayout.addOnOffsetChangedListener(this);
        mAppBarLayoutTitle = findViewById(R.id.specialistsAppBarTitle);
        mAppBarLayoutSubtitle = findViewById(R.id.specialistsAppBarSubtitle);
        mImageView = findViewById(R.id.specialistsBackdrop);
        mTitle = findViewById(R.id.specialistsTitle);
        mSubtitle = findViewById(R.id.specialistsTime);
        mContent = findViewById(R.id.specialistsContent);

        Intent intent = getIntent();
        mAvatarString = intent.getStringExtra("avatar");
        mBindString = intent.getStringExtra("bind");
        mIdString = intent.getStringExtra("id");
        mActivityString = intent.getStringExtra("activity");
        mCitationsString = intent.getStringExtra("citations");
        mDiversityString = intent.getStringExtra("diversity");
        mGindexString = intent.getStringExtra("gindex");
        mHindexString = intent.getStringExtra("hindex");
        mNewStarString = intent.getStringExtra("newStar");
        mPubsString = intent.getStringExtra("risingStar");
        mRisingStarString = intent.getStringExtra("sociability");
        mName = intent.getStringExtra("name");
        mName_zh = intent.getStringExtra("name_zh");
        mNumFollowed = intent.getStringExtra("num_followed");
        mNumViewed = intent.getStringExtra("num_viewed");
        mProfileAddressString = intent.getStringExtra("address");
        mProfileAffiliationString = intent.getStringExtra("affiliation");
        mProfileAffiliationZhString = intent.getStringExtra("affiliation_zh");
        mProfileBioString = intent.getStringExtra("bio");
        mEduString = intent.getStringExtra("edu");
        mEmailString = intent.getStringExtra("email");
        mEmailCrString = intent.getStringExtra("emails_cr");
        mFaxString = intent.getStringExtra("fax");
        mHomePageString = intent.getStringExtra("homepage");
        mNoteString = intent.getStringExtra("note");
        mPositionString = intent.getStringExtra("position");
        mWorkString = intent.getStringExtra("work");
        mIsPassedAway = intent.getStringExtra("is_passedaway");

        //set views with intents
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .load(mAvatarString)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mImageView);

        //update view
        mAppBarLayoutTitle.setText(mName_zh);
        mAppBarLayoutSubtitle.setText(mName);
        String names = mName_zh + ", " + mName;
        mTitle.setText(names);
        mSubtitle.setText(mProfileAffiliationString);

        String content = "Profile. . .\n" +
                "Address: " + mProfileAddressString + "\n" +
                "Affiliation: " + mProfileAffiliationString + "\n" +
                "Affiliation_zh: " + mProfileAffiliationZhString + "\n" +
                "Bio: " + mProfileBioString + "\n" +
                "Edu: " + mEduString + "\n" +
                "Position: " + mPositionString + "\n" +
                "Work: " + mWorkString + "\n\n" +
                "Contact Information. . .\n" +
                "Email: " + mEmailString + "\n" +
                "Email_cr: " + mEmailCrString + "\n" +
                "Fax: " + mFaxString + "\n" +
                "Note: " + mNoteString + "\n" +
                "Is Passed Away: " + mIsPassedAway;

        mContent.setText(content);
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
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, mName_zh);
                String body =   "Bio: " + mProfileBioString + "\n" +
                        "Edu: " + mEduString + "\n" +   "Affiliation: " + mProfileAffiliationString + "\n" +
                        "Affiliation_zh: " + mProfileAffiliationZhString + "\n";
                i.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(i, "Share with: "));
            } catch (Exception e) {
                Toast.makeText(this, "Can't Share", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
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
