package com.ipince.android.twitter.activity;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipince.android.twitter.R;
import com.ipince.android.twitter.TwitterClientApp;
import com.ipince.android.twitter.fragment.UserTimelineFragment;
import com.ipince.android.twitter.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

    private ImageView ivProfileImage;
    private TextView tvName;
    private TextView tvTagline;
    private TextView tvFollowers;
    private TextView tvFollowing;

    private UserTimelineFragment frgUserTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivProfileImage = (ImageView) findViewById(R.id.iv_profile);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTagline = (TextView) findViewById(R.id.tv_tagline);
        tvFollowers = (TextView) findViewById(R.id.tv_followers);
        tvFollowing = (TextView) findViewById(R.id.tv_following);

        Intent i = getIntent();
        if (i.hasExtra("user")) {
            User user = (User) i.getSerializableExtra("user");
            displayUser(user);

            frgUserTimeline = UserTimelineFragment.newInstance(user.handle);
        } else {
            TwitterClientApp.getRestClient().getUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject json) {
                    User user = new User(json);
                    displayUser(user);
                }

                @Override
                public void onFailure(Throwable throwable, JSONObject json) {
                    Log.e("ProfileActivity", "Failed to get user details", throwable);
                }
            });

            frgUserTimeline = new UserTimelineFragment();
        }

        // Attach fragment.
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fts = manager.beginTransaction();
        fts.replace(R.id.frm_timeline_container, frgUserTimeline);
        fts.commit();
    }

    private void displayUser(User user) {
        // TODO(ipince): move away from here.
        getActionBar().setTitle("@" + user.handle);
        ImageLoader.getInstance().displayImage(user.profileImageUrl, ivProfileImage);
        tvName.setText(user.name);
        tvTagline.setText(user.tagline);
        // TODO(ipince): move to string resources.
        tvFollowers.setText(user.followers + " Followers");
        tvFollowing.setText(user.following + " Following");
    }
}
