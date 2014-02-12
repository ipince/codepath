package com.ipince.android.twitter.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipince.android.twitter.R;
import com.ipince.android.twitter.model.Tweet;
import com.ipince.android.twitter.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

    public interface ProfileImageListener {
        void onProfileImageClick(User user);
    }

    private ProfileImageListener listener;

    public TweetArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.tweet_item, tweets);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.tweet_item, null);
        }

        Tweet tweet = getItem(pos);

        // Set info.
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_tweet_pic);
        ImageLoader.getInstance().displayImage(tweet.getUser().profileImageUrl, imageView);

        TextView nameView = (TextView) view.findViewById(R.id.tv_tweet_name);
        // TODO(ipince): format nicely.

        String time = "";
        try {
            Date creationDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH).parse(tweet.getCreatedAt());
            time = DateUtils.getRelativeTimeSpanString(creationDate.getTime(), new Date().getTime(),
                    DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            time = tweet.getCreatedAt();
        }

        String formattedHeadline = "<b>" + tweet.getUser().name + "</b> <small><font color=#777777>@"
                + tweet.getUser().handle + " - " + time + "</font></small>";
        nameView.setText(Html.fromHtml(formattedHeadline));

        TextView bodyView = (TextView) view.findViewById(R.id.tv_tweet_body);
        bodyView.setText(tweet.getBody());

        // Setup listeners.
        imageView.setTag(pos);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = (Integer) view.getTag();
                    Tweet tweet = getItem(position);
                    listener.onProfileImageClick(tweet.getUser());
                }
            }
        });

        return view;
    }

    public void setProfileImageListener(ProfileImageListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }
}
