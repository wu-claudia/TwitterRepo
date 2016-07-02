package com.codepath.apps.mysimpletweets.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;
    String screenName;

    ViewPager vpPager;
    PagerSlidingTabStrip tabStrip;
    //TweetsPagerAdapter adapter;

    HomeTimelineFragment homeTimeline;

    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTagline) TextView tvTagline;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.ivBanner) ImageView ivBannerImage;
    @BindView(R.id.tvUserName) TextView tvUser;
    @BindView(R.id.tvFollowersText) TextView tvFollowersText;
    @BindView(R.id.tvFollowingText) TextView tvFollowingText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        client = TwitterApplication.getRestClient();

//        // Get the view pager
//        vpPager = (ViewPager) findViewById(R.id.viewpager);
//        // Set the viewpager adapter for the pager
//        adapter = new TweetsPagerAdapter(getSupportFragmentManager());
//        vpPager.setAdapter(adapter);
//        // Find the pager tabstrip
//        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
//        // Attach the tabstrip to the viewpager
//        tabStrip.setViewPager(vpPager);

        // Get the account info
        if (getIntent().getStringExtra("username") != null) {
            screenName = getIntent().getStringExtra("username");
        } else {
            screenName = getIntent().getStringExtra("screen_name");
        }
        client.getUserInfo(screenName,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                // My current user account's info
                getSupportActionBar().setTitle("@" + user.getScreenName());
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));
                populateProfileHeader(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        if (savedInstanceState == null) {
            // Create the user timeline fragment
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            // Display user fragment within this activity (dynamically)
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // make a change
            ft.replace(R.id.flContainer,fragmentUserTimeline);
            //commit the transaction
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {

        tvName.setText(user.getName());
        tvUser.setText("@" + user.getScreenName());
        tvTagline.setText(user.getTagline());
        tvFollowersText.setText(" Followers");
        tvFollowingText.setText(" Following");
        tvFollowers.setText(NumberFormat.getIntegerInstance().format(user.getFollowersCount()));
        tvFollowing.setText(NumberFormat.getIntegerInstance().format(user.getFriendsCount()));
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
        Picasso.with(this).load(user.getBannerUrl()).into(ivBannerImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    // Return the order of the fragments in the view pager
//    public class TweetsPagerAdapter extends FragmentPagerAdapter {
//        final int PAGE_COUNT = 2;
//        private String tabTitles[] = {"TWEETS","PHOTOS","FAVORITES"};
//
//        public TweetsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        // The order and creation of fragments within the pager
//        @Override
//        public Fragment getItem(int position) {
//            if (position == 0) {
//                return homeTimeline;
//            } else {
//                return null;
//            }
//        }
//
//        // Return the tab title
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return tabTitles[position];
//        }
//
//        // Returns the number of tab titles
//        @Override
//        public int getCount() {
//            return tabTitles.length;
//        }
//    }
}
