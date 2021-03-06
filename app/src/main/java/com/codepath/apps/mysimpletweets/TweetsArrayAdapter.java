package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.activities.ComposeActivity;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

// Taking the Tweet objects and turning them into Views displayed in the list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    TextView tvUserName;
    TextView tvBody;
    TextView tvName;
    TextView tvTime;
    ImageView ivProfileImage;
    ImageView ivReply;
    TextView tvRetweets;
    TextView tvFav;
    ImageView ivMedia;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.fragment_tweets_list, tweets);
    }

    // Override and setup custom template
    // Viewholder pattern


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Get the tweet
        final Tweet tweet = getItem(position);
        // 2. Find or inflate the template
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }
        // 3. Find the subviews to fill with data in the template
        ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        ivProfileImage.setScaleType(ImageView.ScaleType.FIT_XY);
        tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        ivReply = (ImageView) convertView.findViewById(R.id.ivReply);
        tvRetweets = (TextView) convertView.findViewById(R.id.tvRetweets);
        tvFav = (TextView) convertView.findViewById(R.id.tvFavorites);
        ivMedia = (ImageView) convertView.findViewById(R.id.ivMedia);


        // 4. Populate data into the subviews
        tvUserName.setText(tweet.getUser().getFullName());
        tvBody.setText(tweet.getBody());
        tvName.setText("@" + tweet.getUser().getScreenName());
        tvTime.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        ivProfileImage.setImageResource(android.R.color.transparent); //clear out the old image for a recycled view

        int retweet = tweet.getRetweetCount();
        int fav = tweet.getFavCount();
        if (retweet != 0) {
            tvRetweets.setText(String.valueOf(retweet));
        }
        if (fav != 0) {
            tvFav.setText(String.valueOf(fav));
        }
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).transform(new RoundedCornersTransformation(2,2)).into(ivProfileImage);

        String mediaUrl = tweet.getMediaUrl();
        if (mediaUrl != null) {
            ivMedia.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(mediaUrl).transform(new RoundedCornersTransformation(2,2)).into(ivMedia);
        } else {
            ivMedia.setVisibility(View.GONE);
        }

        //ivProfileImage.setTag(tweet.getUser().getScreenName());
        ivProfileImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("username", tweet.getUser().getScreenName());
                //Toast.makeText(getContext(), tweet.getUser().getScreenName(), Toast.LENGTH_SHORT).show();
                getContext().startActivity(i);
            }
        });

        ivReply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ComposeActivity.class);
                long replyTo = tweet.getUid();
                String usernameReply = ("@" + tweet.getUser().getScreenName());
                i.putExtra("name", usernameReply);
                i.putExtra("reply",replyTo);
                getContext().startActivity(i);
            }
        });
        // 5. Return the view to be inserted into the list
        return convertView;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        String newDate = "";
        int space = relativeDate.indexOf(" ");
        if (relativeDate.contains("second")) {
            newDate = relativeDate.substring(0,space);
            newDate += "s";
        } else if (relativeDate.contains("minute")) {
            newDate = relativeDate.substring(0,space);
            newDate += "m";
        } else if (relativeDate.contains("hour")) {
            newDate = relativeDate.substring(0,space);
            newDate += "h";
        }

        return newDate;
    }
}
