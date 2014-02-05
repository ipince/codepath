package com.ipince.android.twitter.widget;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.ipince.android.twitter.model.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

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

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_tweet_pic);
        ImageLoader.getInstance().displayImage(tweet.getUser().profileImageUrl, imageView);

        TextView nameView = (TextView) view.findViewById(R.id.tv_tweet_name);
        // TODO(ipince): format nicely.
        String formattedHeadline = "<b>" + tweet.getUser().name + "</b> <small><font color=#777777>@"
                + tweet.getUser().handle + "</font></small>";
        nameView.setText(Html.fromHtml(formattedHeadline));

        TextView bodyView = (TextView) view.findViewById(R.id.tv_tweet_body);
        bodyView.setText(tweet.getBody());

        return view;
    }
}
