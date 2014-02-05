package com.codepath.apps.activity;

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
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.RestClientApp;
import com.codepath.apps.restclienttemplate.models.Tweet;
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

        fetchTweets();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_COMPOSE_TWEET && resultCode == RESULT_OK) {
            Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
            tweetAdapter.insert(tweet, 0);
            // TODO(ipince): save tweet.
        }
    }

    private void fetchTweets() {
        RestClient client = RestClientApp.getRestClient();
        client.getHomeTimeline(1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray json) {
                List<Tweet> tweets = Tweet.fromJson(json);
                Toast.makeText(TimelineActivity.this, "Got " + tweets.size() + " tweets", Toast.LENGTH_LONG).show();

                tweetAdapter.clear();
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
