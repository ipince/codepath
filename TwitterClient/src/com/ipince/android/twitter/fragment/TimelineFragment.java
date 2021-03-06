package com.ipince.android.twitter.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ipince.android.twitter.R;
import com.ipince.android.twitter.model.Tweet;
import com.ipince.android.twitter.widget.EndlessScrollListener;
import com.ipince.android.twitter.widget.TweetArrayAdapter;
import com.ipince.android.twitter.widget.TweetArrayAdapter.ProfileImageListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TimelineFragment extends Fragment {

    protected PullToRefreshListView lvTweets;

    private ProfileImageListener listener;
    private TweetArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TweetArrayAdapter(getActivity(), new ArrayList<Tweet>());
        // HACK HACK. See note below.
        if (listener != null) {
            adapter.setProfileImageListener(listener);
        }
        // End HACK.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        lvTweets = (PullToRefreshListView) view.findViewById(R.id.lv_tweets);
        lvTweets.setAdapter(adapter);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Assume we just need to load more; ignore page.
                if (adapter.getCount() > 1) {
                    Tweet lastTweet = adapter.getItem(adapter.getCount() - 1);
                    long lastId = lastTweet.getRemoteIdAsLong();
                    long maxId = lastId - 1;
                    fetchTweets(maxId, getTweetHandler(false));
                }
            }
        });

        lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTweets(null, getTweetHandler(true));
            }
        });

        fetchTweets(null, getTweetHandler(true));
        return view;
    }

    public TweetArrayAdapter getAdapter() {
        return adapter;
    }

    // HACK!! HACK!!
    // NOTE(ipince): had to add this nasty method to have the parent activity
    // set itself as a listener on the adapter. Ideally, the parent activity
    // would just call getAdapter().setProfileImageListener() itself, but I
    // couldn't find a place in the Activity at which point the fragment's
    // onCreate() method had already been called (where the adapter is constructed).
    // Ideas for alternative solutions? I really don't want my adapter to have
    // to know about Activities; doesn't seem like modular design to me. In addition,
    // I don't want my adapter inside the ProfileActivity to launch a new ProfileActivity
    // when the user clicks on his own profile image, so I want to be able to have an adapter
    // with no listener.
    // Note that this solution could lead to a race condition, so we should probably
    // synchronize the method. My Android threading model is unclear though (can Fragment.onCreate()
    // and Activity.onCreate() be called in separate threads?), so I skipped that.
    public void setProfileImageListener(ProfileImageListener listener) {
        this.listener = listener;
        if (adapter != null) {
            adapter.setProfileImageListener(listener);
        }
    }
    // End HACK.

    protected abstract void fetchTweets(Long maxId, AsyncHttpResponseHandler handler);


    private AsyncHttpResponseHandler getTweetHandler(final boolean clear) {
        return new JsonHttpResponseHandler() {
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
        };
    }
}
