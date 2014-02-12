package com.ipince.android.twitter.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ipince.android.twitter.R;
import com.ipince.android.twitter.fragment.HomeTimelineFragment;
import com.ipince.android.twitter.fragment.MentionsTimelineFragment;
import com.ipince.android.twitter.fragment.TimelineFragment;
import com.ipince.android.twitter.model.Tweet;
import com.ipince.android.twitter.model.User;
import com.ipince.android.twitter.widget.FragmentTabListener;
import com.ipince.android.twitter.widget.TweetArrayAdapter.ProfileImageListener;

public class TweetsActivity extends FragmentActivity implements ProfileImageListener {

    public static final int REQ_CODE_COMPOSE_TWEET = 1;

    private final TimelineFragment frgTweetList = new HomeTimelineFragment();
    private final MentionsTimelineFragment frgMentions = new MentionsTimelineFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        setupTabs();

        frgTweetList.setProfileImageListener(this);
        frgMentions.setProfileImageListener(this);
    }

    private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        actionBar.addTab(actionBar.newTab()
                .setText(R.string.tab_home)
                .setIcon(R.drawable.ic_home)
                .setTabListener(new FragmentTabListener(
                        this, R.id.frm_container, frgTweetList)));

        actionBar.addTab(actionBar.newTab()
                .setText(R.string.tab_mentions)
                .setIcon(R.drawable.ic_at)
                .setTabListener(new FragmentTabListener(
                        this, R.id.frm_container, frgMentions)));
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
        getMenuInflater().inflate(R.menu.tweets, menu);
        return true;
    }

    public void onMenuClickCompose(MenuItem item) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQ_CODE_COMPOSE_TWEET);
    }

    public void onMenuClickProfile(MenuItem tiem) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    @Override
    public void onProfileImageClick(User user) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("user", user);
        startActivity(i);
    }
}
