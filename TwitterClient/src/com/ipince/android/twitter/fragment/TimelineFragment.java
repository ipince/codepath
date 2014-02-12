package com.ipince.android.twitter.fragment;

import com.ipince.android.twitter.TwitterClientApp;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class TimelineFragment extends TweetListFragment {

    // maxId can be null.
    @Override
    protected void fetchTweets(Long maxId, AsyncHttpResponseHandler handler) {
        TwitterClientApp.getRestClient().getHomeTimeline(maxId, handler);
    }
}
