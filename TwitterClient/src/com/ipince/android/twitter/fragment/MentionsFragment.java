package com.ipince.android.twitter.fragment;

import com.ipince.android.twitter.TwitterClientApp;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MentionsFragment extends TweetListFragment {

    @Override
    protected void fetchTweets(Long maxId, AsyncHttpResponseHandler handler) {
        TwitterClientApp.getRestClient().getMentions(maxId, handler);
    }
}
