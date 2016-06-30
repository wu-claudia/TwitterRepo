package com.codepath.apps.mysimpletweets;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "eHgxN6AIsC1pzFT8Df9Msv3FL";       // Change this
	public static final String REST_CONSUMER_SECRET = "dkQgXhOzUsdi9k6Ybe38YFnTXlZDc0VjpJly6hNaSzjI6jINQN"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// HomeTimeline --> Gets us the home timeline
	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");

		//Specify the params
		RequestParams params = new RequestParams();
		params.put("count",25);
		params.put("since_id",1);
		//Execute the request
		getClient().get(apiUrl,params,handler);
	}

	public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");

		//Specify the params
		RequestParams params = new RequestParams();
		params.put("count",25);
		//Execute the request
		getClient().get(apiUrl,params,handler);
	}

	public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",25);
		params.put("screen_name",screenName);
		//Execute the request
		getClient().get(apiUrl,params,handler);
	}

	public void getUserInfo(String username,AsyncHttpResponseHandler handler) {
		if (username == null) {
			String apiUrl = getApiUrl("account/verify_credentials.json");
			RequestParams params = new RequestParams();
			params.put("count",25);
			//Execute the request
			getClient().get(apiUrl,params,handler);
		} else {
			String apiUrl = getApiUrl("users/show.json");
			RequestParams params = new RequestParams();
			params.put("screen_name",username);
			//Execute the request
			getClient().get(apiUrl,params,handler);
		}

	}

	public void postTweet(String status, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status",status);
		getClient().post(apiUrl,params,handler);
	}

	public void searchTweets(String query, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("search/tweets.json");
		RequestParams params = new RequestParams();
		params.put("q",query);
		getClient().get(apiUrl,params,handler);
	}
}