package com.ipince.android.twitter.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.ipince.android.twitter.R;
import com.ipince.android.twitter.fragment.MentionsFragment;
import com.ipince.android.twitter.fragment.TimelineFragment;
import com.ipince.android.twitter.fragment.TweetListFragment;
import com.ipince.android.twitter.model.Tweet;
import com.ipince.android.twitter.model.User;
import com.ipince.android.twitter.widget.TweetArrayAdapter.ProfileImageListener;

public class TimelineActivity extends FragmentActivity implements ProfileImageListener {

    public static final int REQ_CODE_COMPOSE_TWEET = 1;

    private final TweetListFragment frgTweetList = new TimelineFragment();
    private final MentionsFragment frgMentions = new MentionsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
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
                .setTabListener(getTabListenerFor(frgTweetList)));

        actionBar.addTab(actionBar.newTab()
                .setText(R.string.tab_mentions)
                .setIcon(R.drawable.ic_at)
                .setTabListener(getTabListenerFor(frgMentions)));
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

    private TabListener getTabListenerFor(final Fragment fragment) {
        return new TabListener() {
            @Override
            public void onTabReselected(Tab tab, FragmentTransaction ft) {}

            @Override
            public void onTabSelected(Tab tab, FragmentTransaction ft) {
                FragmentManager manager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
                fts.replace(R.id.frm_container, fragment);
                fts.commit();
            }

            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
        };
    }
}
