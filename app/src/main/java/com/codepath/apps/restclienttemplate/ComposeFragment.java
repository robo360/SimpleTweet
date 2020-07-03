package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.codepath.apps.restclienttemplate.databinding.FragmentComposeBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends DialogFragment{
    public static final int MAX_TWEET_LENGTH = 280;
    public static final String TAG = "ComposeActivity";

    EditText etCompose;
    Button btnTweet;
    TwitterClient client;
    Context context;
    FragmentComposeBinding binder;

    //set up an interface
    public interface OnFinishListener{
        void onFinished(Tweet tweet);
    }

    public ComposeFragment() {
        // Required empty public constructor
    }


    public static ComposeFragment newInstance() {
        ComposeFragment composeFragment = new ComposeFragment();
        return composeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the client in ComposeActivity
        context = getContext();
        client = TwitterApp.getRestClient(context);

        binder =  FragmentComposeBinding.inflate(getLayoutInflater());

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
                            Log.i(TAG, "Published Tweet says" + tweet);
                            etCompose.setText("");
                            Snackbar.make(btnTweet, "Tweet Published", Snackbar.LENGTH_LONG).show();
                            //Go back to the parent TimelineActivity
                            OnFinishListener listener = (OnFinishListener) getActivity();
                            listener.onFinished(tweet);
                            dismiss();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return binder.getRoot();
    }

}