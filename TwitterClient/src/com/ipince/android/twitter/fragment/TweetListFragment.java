package com.ipince.android.twitter.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ipince.android.twitter.R;
import com.ipince.android.twitter.model.Tweet;
import com.ipince.android.twitter.widget.EndlessScrollListener;
import com.ipince.android.twitter.widget.TweetArrayAdapter;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetListFragment extends Fragment {

    protected PullToRefreshListView lvTweets;

    private ArrayAdapter<Tweet> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweet_list, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new TweetArrayAdapter(getActivity(), new ArrayList<Tweet>());
        lvTweets = (PullToRefreshListView) getActivity().findViewById(R.id.lv_tweets);
        lvTweets.setAdapter(adapter);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Assume we just need to load more; ignore page.
                if (adapter.getCount() > 1) {
                    Tweet lastTweet = adapter.getItem(adapter.getCount() - 1);
                    long lastId = lastTweet.getRemoteIdAsLong();
                    long maxId = lastId - 1;
                    fetchTweets(maxId, false);
                }
            }
        });

        lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTweets(null, true);
            }
        });

        fetchTweets(null, true);
    }

    public ArrayAdapter<Tweet> getAdapter() {
        return adapter;
    }

    protected abstract void fetchTweets(Long maxId, boolean clear);
}
