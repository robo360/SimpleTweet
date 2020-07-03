package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TweetDetailActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Tweet> tweets;
    public final int REQUEST_CODE = 10;
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.item_timeline_with_img, parent, false);
                return new ViewHolderWithImage(view);
            default:
                view = LayoutInflater.from(context).inflate(R.layout.item_timeline, parent, false);
                return new ViewHolderWithoutImage(view);
        }
    }
    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        Tweet tweet = tweets.get(position);
        if(tweet.mediaUrl.isEmpty()){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        switch (holder.getItemViewType()){
            case 1:
                ViewHolderWithImage holderWithImage = (ViewHolderWithImage) holder;
                holderWithImage.bindWithImage(tweet);
                break;
            default:
                ViewHolderWithoutImage holderWithoutImage = (ViewHolderWithoutImage) holder;
                holderWithoutImage.bindWithoutImage(tweet);
        }
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolderWithoutImage extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvTimeAgo;
        ImageButton btnDetail;
        ImageButton btnLike;
        ImageButton btnRetweet;
        ImageButton btnReply;
        TextView tvLike;
        TextView tvRetweet;

        public ViewHolderWithoutImage(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnReply = itemView.findViewById(R.id.btnReply);
            btnRetweet= itemView.findViewById(R.id.btnRetweet);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvRetweet = itemView.findViewById(R.id.tvRetweet);
//            //send to the detailView
            View.OnClickListener ListenerToDetailView = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, TweetDetailActivity.class);
                    i.putExtra("tweet", Parcels.wrap(tweets.get(getAdapterPosition())));
                    context.startActivity(i);
                }
            };
          btnDetail.setOnClickListener(ListenerToDetailView);
          btnReply.setOnClickListener(ListenerToDetailView);

        }

        public void bindWithoutImage(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText("@"+tweet.user.screenName);
            tvRetweet.setText(Integer.toString(tweet.retweetCount));
            tvLike.setText(Integer.toString(tweet.favoriteCount));
            Log.i("TweetsAdapter", tweet.mediaUrl);
            tvTimeAgo.setText(getRelativeTimeAgo(tweet.timestamp));
            Glide.with(context).load(tweet.user.profileImageUrl).transform(new CircleCrop()).into(ivProfileImage);

        }
    }

    public class ViewHolderWithImage extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        ImageView ivMedia;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvTimeAgo;
        ImageButton btnDetail;
        ImageButton btnLike;
        ImageButton btnRetweet;
        ImageButton btnReply;
        TextView tvLike;
        TextView tvRetweet;


        public ViewHolderWithImage(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimeAgo = itemView.findViewById(R.id.tvTimeAgo);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnReply = itemView.findViewById(R.id.btnReply);
            btnRetweet= itemView.findViewById(R.id.btnRetweet);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvRetweet = itemView.findViewById(R.id.tvRetweet);

            //send to the detailView
            View.OnClickListener ListenerToDetailView = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, TweetDetailActivity.class);
                    i.putExtra("tweet", Parcels.wrap(tweets.get(getAdapterPosition())));
                    context.startActivity(i);
                }
            };
            btnDetail.setOnClickListener(ListenerToDetailView);
            btnReply.setOnClickListener(ListenerToDetailView);


        }

        public void bindWithImage(final Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);
            tvTimeAgo.setText(getRelativeTimeAgo(tweet.timestamp));
            tvRetweet.setText(Integer.toString(tweet.retweetCount));
            tvLike.setText(Integer.toString(tweet.favoriteCount));
            //set Onclick listeners to ImageButtons
            Glide.with(context).load(tweet.user.profileImageUrl).transform(new CircleCrop()).into(ivProfileImage);
            Log.i("TweetsAdapter", tweet.mediaUrl);
            Glide.with(context).load(tweet.mediaUrl).into(ivMedia);

        }
    }
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}