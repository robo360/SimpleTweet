package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 280;
    public static final String TAG = "ComposeActivity";

    EditText etCompose;
    Button btnTweet;
    TwitterClient client;
    List<Tweet> tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the client in ComposeActivity
        client = TwitterApp.getRestClient(this);

        ActivityComposeBinding binder = ActivityComposeBinding.inflate(getLayoutInflater());
        setContentView(binder.getRoot());
        etCompose = binder.etCompose;
        btnTweet = binder.btnTweet;
        // for the counter on the composer TextLayoutFilter set the max of tweet length.
        binder.etComposeHolder.setCounterMaxLength(MAX_TWEET_LENGTH);

        //set a click listener and make an api call to publish tweet
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()){
                    //add snack bars instead of Toast.
                    Snackbar.make(view, "Too Short", Snackbar.LENGTH_LONG).show();
                    return;
                }else if( tweetContent.length() > MAX_TWEET_LENGTH){
                    Snackbar.make(view, "Too Long", Snackbar.LENGTH_LONG).show();
                }else {
                    client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Snackbar.make(btnTweet, "Tweet Posted!", Snackbar.LENGTH_LONG).show();
                            Tweet tweet = null;
                            tweet = new Tweet(json.jsonObject);
                            Log.i(TAG, "Published Tweet says"+tweet);
                            etCompose.setText("");
                            Snackbar.make(btnTweet, "Tweet Published", Snackbar.LENGTH_LONG).show();
                            //Go back to the parent TimelineActivity
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Snackbar.make(btnTweet, "Can not post! Try later."+throwable, Snackbar.LENGTH_LONG).show();

                        }
                    });
                }
            }
        });

    }
}