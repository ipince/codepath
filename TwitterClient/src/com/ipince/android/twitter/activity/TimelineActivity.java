package com.ipince.android.twitter.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.ipince.android.twitter.TwitterClientApp;
import com.ipince.android.twitter.client.TwitterClient;
import com.ipince.android.twitter.model.Tweet;
import com.ipince.android.twitter.widget.EndlessScrollListener;
import com.ipince.android.twitter.widget.TweetArrayAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {

    public static final int REQ_CODE_COMPOSE_TWEET = 1;

    private ListView lvTweets;

    private TweetArrayAdapter tweetAdapter;
    private final List<Tweet> tweets = new ArrayList<Tweet>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (ListView) findViewById(R.id.lv_tweets);

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

        fetchTweets(null, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_COMPOSE_TWEET && resultCode == RESULT_OK) {
            Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
            tweetAdapter.insert(tweet, 0);
            // TODO(ipince): save tweet.
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
            }
        });
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
}