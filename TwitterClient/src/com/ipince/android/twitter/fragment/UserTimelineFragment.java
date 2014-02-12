package com.ipince.android.twitter.fragment;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ipince.android.twitter.TwitterClientApp;
import com.ipince.android.twitter.client.TwitterClient;
import com.ipince.android.twitter.model.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetListFragment {

    public static UserTimelineFragment newInstance(String handle) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("handle", handle);
        fragment.setArguments(args);
        return fragment;
    }

    private String handle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            handle = getArguments().getString("handle");
        }
    }

    @Override
    protected void fetchTweets(Long maxId, final boolean clear) {
        TwitterClient client = TwitterClientApp.getRestClient();
        client.getUserTimeline(handle, maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray json) {
                List<Tweet> tweets = Tweet.fromJson(json);
                Toast.makeText(getActivity(), "Got " + tweets.size() + " tweets", Toast.LENGTH_LONG).show();

                if (clear) {
                    getAdapter().clear();
                }
                getAdapter().addAll(tweets);
                lvTweets.onRefreshComplete();
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject json) {
                Log.d("TimelineActivity", "Error fetching tweets: " + json.toString(), throwable);
                lvTweets.onRefreshComplete();
            }
        });
    }
}
