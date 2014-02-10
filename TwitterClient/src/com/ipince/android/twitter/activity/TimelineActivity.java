package com.ipince.android.twitter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ipince.android.twitter.R;
import com.ipince.android.twitter.fragment.TweetListFragment;
import com.ipince.android.twitter.model.Tweet;

public class TimelineActivity extends FragmentActivity {

    public static final int REQ_CODE_COMPOSE_TWEET = 1;

    private TweetListFragment frgTweetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        frgTweetList = (TweetListFragment) getSupportFragmentManager().findFragmentById(
                R.id.frg_tweet_list);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_COMPOSE_TWEET && resultCode == RESULT_OK) {
            Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
            frgTweetList.getAdapter().insert(tweet, 0);
            // TODO(ipince): save tweet.
        }
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
