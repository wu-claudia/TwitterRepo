package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DetailsActivity extends AppCompatActivity {

    User user;
    Tweet tweet;

    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvUserName) TextView tvUsername;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvRetweets) TextView tvRetweets;
    @BindView(R.id.tvLikes) TextView tvLikes;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.ivMedia) ImageView ivMedia;
    @BindView(R.id.tvRetweetsText) TextView tvRetweetsText;
    @BindView(R.id.tvLikesText) TextView tvLikesText;
    @BindView(R.id.tvTime) TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        user = tweet.getUser();

        populateDetailPage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(DetailsActivity.this, SearchActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateDetailPage() {
        tvName.setText(user.getFullName());
        tvUsername.setText("@" + user.getScreenName());
        tvRetweets.setText(String.valueOf(tweet.getRetweetCount()));
        tvRetweetsText.setText(" RETWEETS");
        tvLikes.setText(String.valueOf(tweet.getFavCount()));
        tvLikesText.setText(" LIKES");
        tvBody.setText(tweet.getBody());
        tvTime.setText(tweet.getCreatedAt());
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

        String mediaUrl = tweet.getMediaUrl();
        if (mediaUrl != null) {
            ivMedia.setVisibility(View.VISIBLE);
            Picasso.with(getApplicationContext()).load(mediaUrl).transform(new RoundedCornersTransformation(2,2)).into(ivMedia);
        } else {
            ivMedia.setVisibility(View.GONE);
        }
    }

}
