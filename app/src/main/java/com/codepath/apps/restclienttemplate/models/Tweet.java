package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
@Entity
public class Tweet {
    // Define database columns and associated fields
    @PrimaryKey
    @ColumnInfo
    public
    long id;
    @ColumnInfo
    public
    String timestamp;
    @ColumnInfo
    public
    String body;

    @ColumnInfo
    public
    String mediaUrl;


    // Use @Embedded to keep the column entries as part of the same table while still
    // keeping the logical separation between the two objects.
    @Embedded
    public
    User user;


    //empty constructor needed by the Parceler library
    public Tweet(){

    }

    public Tweet(JSONObject object){
        try {
            this.user = User.parseJSON(object.getJSONObject("user"));
            this.timestamp = object.getString("created_at");
            this.body = object.getString("text");
            this.id = object.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject media = object.getJSONObject("entities").getJSONArray("media").getJSONObject(0);
            this.mediaUrl = media.getString("media_url_https");
            Log.i("TweetsAdapter", mediaUrl);
        } catch (JSONException e) {
            this.mediaUrl = "";
            Log.i("TweetsAdapter", mediaUrl);
        }

    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) throws JSONException {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweets.add(tweet);
        }

        return tweets;
    }
}
