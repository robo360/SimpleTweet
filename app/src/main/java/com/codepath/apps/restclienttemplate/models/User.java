package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    @ColumnInfo
    String name;

    @ColumnInfo
    public
    String screenName;

    @ColumnInfo
    public
    String profileImageUrl;

    // Parse model from JSON
    public static User parseJSON(JSONObject tweetJson) throws JSONException {

        User user = new User();
        user.screenName = tweetJson.getString("screen_name");
        user.name = tweetJson.getString("name");
        user.profileImageUrl= tweetJson.getString("profile_image_url_https");
        return user;
    }
}
