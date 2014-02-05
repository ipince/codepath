package com.ipince.android.twitter.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.ipince.android.twitter.TwitterClientApp;
import com.ipince.android.twitter.client.TwitterClient;
import com.ipince.android.twitter.model.Tweet;
import com.ipince.android.twitter.widget.EndlessScrollListener;
import com.ipince.android.twitter.widget.TweetArrayAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {

    public static final int REQ_CODE_COMPOSE_TWEET = 1;

    private PullToRefreshListView lvTweets;

    private TweetArrayAdapter tweetAdapter;
    private final List<Tweet> tweets = new ArrayList<Tweet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (PullToRefreshListView) findViewById(R.id.lv_tweets);

        tweetAdapter = new TweetArrayAdapter(this, tweets);
        lvTweets.setAdapter(tweetAdapter);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Assume we just need to load more; ignore page.
                if (tweetAdapter.getCount() > 1) {
                    Tweet lastTweet = tweetAdapter.getItem(tweetAdapter.getCount() - 1);
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

        if (iCanHasInternets()) {
            fetchTweets(null, true);
        } else {
            // load tweets from db.
            loadLocalTweets();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_COMPOSE_TWEET && resultCode == RESULT_OK) {
            Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
            tweetAdapter.insert(tweet, 0);
            tweet.deepSave();
        }
    }

    // maxId can be null
    private void fetchTweets(Long maxId, final boolean clear) {
        TwitterClient client = TwitterClientApp.getRestClient();
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray json) {
                List<Tweet> tweets = Tweet.fromJson(json);
                Toast.makeText(TimelineActivity.this, "Got " + tweets.size() + " tweets", Toast.LENGTH_LONG).show();

                if (clear) {
                    tweetAdapter.clear();
                }
                tweetAdapter.addAll(tweets);
                // TODO(ipince): optimize?
                for (Tweet t : tweets) {
                    t.deepSave();
                }
                lvTweets.onRefreshComplete();
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject json) {
                Log.d("TimelineActivity", "Error fetching tweets: " + json.toString(), throwable);
                Toast.makeText(TimelineActivity.this, "Sorry, cannot fetch tweets", Toast.LENGTH_LONG).show();
                lvTweets.onRefreshComplete();
            }
        });
    }

    private void loadLocalTweets() {
        List<Tweet> tweets = Tweet.recentTweets();
        tweetAdapter.addAll(tweets);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    public void onMenuClickCompose(MenuItem item) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQ_CODE_COMPOSE_TWEET);
    }

    private boolean iCanHasInternets() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
