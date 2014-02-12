package com.ipince.android.twitter.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipince.android.twitter.R;
import com.ipince.android.twitter.TwitterClientApp;
import com.ipince.android.twitter.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

    private ImageView ivProfileImage;
    private TextView tvName;
    private TextView tvTagline;
    private TextView tvFollowers;
    private TextView tvFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivProfileImage = (ImageView) findViewById(R.id.iv_profile);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTagline = (TextView) findViewById(R.id.tv_tagline);
        tvFollowers = (TextView) findViewById(R.id.tv_followers);
        tvFollowing = (TextView) findViewById(R.id.tv_following);

        TwitterClientApp.getRestClient().getUser("reipince", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
                User user = new User(json);
                displayUser(user);
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject json) {
                // TODO(ipince): implement.
                Log.e("ProfileActivity", "Failed to get user details", throwable);
            }
        });
    }

    private void displayUser(User user) {
        ImageLoader.getInstance().displayImage(user.profileImageUrl, ivProfileImage);
        tvName.setText(user.name);
        tvTagline.setText(user.tagline);
        // TODO(ipince): move to string resources.
        tvFollowers.setText(user.followers + " Followers");
        tvFollowing.setText(user.following + " Following");
    }
}
