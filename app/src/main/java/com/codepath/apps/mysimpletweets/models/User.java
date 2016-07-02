package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {
    //List the attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String profileBannerUrl;

    public String getMediaUrl() {
        return mediaUrl;
    }

    private String mediaUrl;

    private String tagline;
    private int followersCount;

    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public int getFriendsCount() {
        return followingCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String getTagline() {
        return tagline;
    }

    public String getBannerUrl() {
        return profileBannerUrl;
    }

    private int followingCount;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    //Deserialize the user json => User
    public static User fromJSON(JSONObject json) {
        User u = new User();
        //Extract and fill the values
        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
            u.profileBannerUrl = json.getString("profile_banner_url");
            u.tagline = json.getString("description");
            u.followersCount = json.getInt("followers_count");
            u.followingCount = json.getInt("friends_count");
            u.fullName = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Return a user
        return u;
    }

    public User() {

    }
}
