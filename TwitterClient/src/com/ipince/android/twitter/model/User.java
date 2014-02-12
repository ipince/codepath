package com.ipince.android.twitter.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.util.Log;

@Table(name = "users")
public class User extends Model implements Serializable {

    @Column(name = "remoteId")
    public String remoteId;
    @Column(name = "name")
    public String name;
    @Column(name = "handle")
    public String handle;
    public String tagline;
    public int followers;
    public int following;
    @Column(name = "profile_image_url")
    public String profileImageUrl;

    public User() {
        super();
    }

    public User(JSONObject json) {
        super();

        try {
            remoteId = json.getString("id_str");
            name = json.getString("name");
            handle = json.getString("screen_name");
            tagline = json.getString("description");
            followers = json.getInt("followers_count");
            following = json.getInt("friends_count");
            profileImageUrl = json.getString("profile_image_url");
        } catch (JSONException e) {
            Log.d("User", "Invalid json: " + json.toString(), e);
        }
    }
}
