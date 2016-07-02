package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ComposeActivity extends AppCompatActivity{
    TwitterClient client;
    User user;
    Tweet tweet;
    int length;
    String usernameReply;
    long replyTo;

    @BindView(R.id.etStatus) EditText etStatus;
    @BindView(R.id.tvCount) TextView tvCount;
    @BindView(R.id.btTweet) Button btnSubmit;
    @BindView(R.id.tvName)TextView tvName;
    @BindView(R.id.tvScreenName) TextView tvScreenName;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient();
        ButterKnife.bind(this);

        usernameReply = getIntent().getStringExtra("name");
        replyTo = getIntent().getLongExtra("reply",0);

        client.getUserInfo(null,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                populateComposeHeader(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });

        tvCount.setText(String.valueOf(140));
        etStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
                length = 140-s.toString().length();
                if (length < 0) {
                    tvCount.setTextColor(Color.parseColor("#ff0000"));
                    tvCount.setText(String.valueOf(length));
                    btnSubmit.setEnabled(false);
                }
                tvCount.setText(String.valueOf(length));
            }
        });
    }

    private void populateComposeHeader(User user) {

        tvName.setText(user.getFullName());
        tvScreenName.setText("@" + user.getScreenName());
        if (usernameReply != "") {
            etStatus.setText(usernameReply);
        }
        Picasso.with(this).load(user.getProfileImageUrl()).transform(new RoundedCornersTransformation(10,10)).into(ivProfileImage);
    }

    public void onTweet(View view) {
        client.postTweet(etStatus.getText().toString(), replyTo, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    tweet = Tweet.fromJSON(response);
                    Intent intent = new Intent();
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK,intent);
                    finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }
        });
    }

    public void onXReturn(View view) {
        finish();
    }
}
