package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SearchListFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        searchTweets();
    }

    public static SearchListFragment newInstance(String query) {
        SearchListFragment searchFragment = new SearchListFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        searchFragment.setArguments(args);
        return searchFragment;
    }

    // Send an API request to get the timeline json
    // fil the listview by creating the tweet objects from the json
    private void searchTweets() {
        String query = getArguments().getString("query");
        client.searchTweets(query, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                JSONArray tweets = null;
                try {
                    tweets = json.getJSONArray("statuses");
                    addAll(Tweet.fromJSONArray(tweets)); //Returns all the items
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
