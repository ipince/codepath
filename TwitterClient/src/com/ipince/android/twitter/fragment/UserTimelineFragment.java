package com.ipince.android.twitter.fragment;

import android.os.Bundle;

import com.ipince.android.twitter.TwitterClientApp;
import com.ipince.android.twitter.client.TwitterClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class UserTimelineFragment extends TimelineFragment {

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
    protected void fetchTweets(Long maxId, AsyncHttpResponseHandler handler) {
        TwitterClient client = TwitterClientApp.getRestClient();
        client.getUserTimeline(handle, maxId, handler);
    }
}
