package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailBinding;
import com.codepath.apps.restclienttemplate.databinding.ActivityTweetDetailWithImgBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.parceler.Parcels;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {
    TwitterClient client;
    Tweet tweet;
    public static final int MAX_TWEET_LENGTH = 280;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        if (tweet.mediaUrl.isEmpty()) {
            ActivityTweetDetailBinding binder = ActivityTweetDetailBinding.inflate(getLayoutInflater());
            setContentView(binder.getRoot());
            binder.tvBody.setText(tweet.body);
            binder.tvScreenName.setText(tweet.user.screenName);
            binder.tvTimeAgo.setText(TweetsAdapter.getRelativeTimeAgo(tweet.timestamp));
            binder.tvRetweet.setText(Integer.toString(tweet.retweetCount));
            binder.tvLike.setText(Integer.toString(tweet.favoriteCount));
            Glide.with(TweetDetailActivity.this).load(tweet.user.profileImageUrl).transform(new CircleCrop()).into(binder.ivProfileImage);
            binder.etCompose.setHint("@" + tweet.user.screenName);
            client = TwitterApp.getRestClient(TweetDetailActivity.this);
            final TextView etCompose = binder.etCompose;
            final MaterialButton btnTweet = binder.btnTweet;

            binder.etComposeHolder.setCounterMaxLength(MAX_TWEET_LENGTH);

            //set a click listener and make an api call to publish tweet
            btnTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String replyContent = etCompose.getText().toString();
                    if (replyContent.isEmpty()) {
                        //add snack bars instead of Toast.
                        Snackbar.make(view, "Too Short", Snackbar.LENGTH_LONG).show();
                        return;
                    } else if (replyContent.length() > MAX_TWEET_LENGTH) {
                        Snackbar.make(view, "Too Long", Snackbar.LENGTH_LONG).show();
                    } else {
                        replyContent = "@"+tweet.user.screenName + " " + replyContent;
                        client.replyTweet(replyContent, Long.toString(tweet.id), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Snackbar.make(btnTweet, "Reply Posted!", Snackbar.LENGTH_LONG).show();
                            }


                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Snackbar.make(btnTweet, "Can not post! Try later." + throwable, Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });

        }else{
            ActivityTweetDetailWithImgBinding binder = ActivityTweetDetailWithImgBinding.inflate(getLayoutInflater());
            setContentView(binder.getRoot());
            setContentView(binder.getRoot());
            binder.tvBody.setText(tweet.body);
            binder.tvScreenName.setText(tweet.user.screenName);
            binder.tvTimeAgo.setText(TweetsAdapter.getRelativeTimeAgo(tweet.timestamp));
            binder.tvRetweet.setText(Integer.toString(tweet.retweetCount));
            binder.tvLike.setText(Integer.toString(tweet.favoriteCount));
            Glide.with(TweetDetailActivity.this).load(tweet.user.profileImageUrl).transform(new CircleCrop()).into(binder.ivProfileImage);
            binder.etCompose.setHint("@" + tweet.user.screenName);
            client = TwitterApp.getRestClient(TweetDetailActivity.this);
            final TextView etCompose = binder.etCompose;
            final MaterialButton btnTweet = binder.btnTweet;

            binder.etComposeHolder.setCounterMaxLength(MAX_TWEET_LENGTH);

            //set a click listener and make an api call to publish tweet
            btnTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String replyContent = etCompose.getText().toString();
                    if (replyContent.isEmpty()) {
                        //add snack bars instead of Toast.
                        Snackbar.make(view, "Too Short", Snackbar.LENGTH_LONG).show();
                        return;
                    } else if (replyContent.length() > MAX_TWEET_LENGTH) {
                        Snackbar.make(view, "Too Long", Snackbar.LENGTH_LONG).show();
                    } else {
                        replyContent = "@"+tweet.user.screenName + " " + replyContent;
                        client.replyTweet(replyContent, Long.toString(tweet.id), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Snackbar.make(btnTweet, "Reply Posted!", Snackbar.LENGTH_LONG).show();
                            }


                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Snackbar.make(btnTweet, "Can not post! Try later." + throwable, Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        }
    }
}