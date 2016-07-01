package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.NumberFormat;

import cz.msebera.android.httpclient.Header;

public class DetailsActivity extends AppCompatActivity {

    TwitterClient client;
    User user;
    TextView tvName;
    TextView tvTagline;
    ImageView ivProfileImage;
    TextView tvRetweets;
    TextView tvLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        client = TwitterApplication.getRestClient();

        // get the screenName that tweeted from intent and store it to get more information about the tweet
        // actually get the tweet ID and pass it in to do other things with it and get details

        client.getUserInfo(screenName,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                // My current user account's info
                getSupportActionBar().setTitle(user.getScreenName());
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));
                populateDetailPage(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        populateDetailPage(user);
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

    private void populateDetailPage(User user,Tweet tweet ???) {
        tvName = (TextView) findViewById(R.id.tvName);
        tvTagline = (TextView) findViewById(R.id.tvTagline);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvRetweets = (TextView) findViewById(R.id.tvRetweets);
        tvLikes = (TextView) findViewById(R.id.tvLikes);

        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvRetweets.setText(); //tweet.getRetweets()
        tvLikes.setText(); //tweets.getLikes()
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

}
