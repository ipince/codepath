package com.ipince.android.twitter.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ConflictAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "tweets")
public class Tweet extends Model implements Serializable {
    // Define database columns and associated fields
    @Column(name = "remote_id", unique = true, onUniqueConflict = ConflictAction.REPLACE)
    String remoteId;
    @Column(name = "user")
    User user;
    @Column(name = "body")
    String body;
    @Column(name = "created_at")
    String createdAt; // TODO(ipince): use seconds_since_epoch

    public Tweet() {
        super();
    }

    // TODO: can we do it without a constructor? ActiveAndroid seems to try to insert
    // stuff at construction-time... :(
    public Tweet(JSONObject object){
        super();

        try {
            this.remoteId = object.getString("id_str");
            this.user = new User(object.getJSONObject("user"));
            this.body = object.getString("text");
            this.createdAt = object.getString("created_at");
        } catch (JSONException e) {
            Log.d("Tweet", "Unable to parse json: " + object.toString(), e);
        }
    }

    public static List<Tweet> fromJson(JSONArray jsonArray) {
        List<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweet.save();
            tweets.add(tweet);
        }
        return tweets;
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getRemoteIdAsLong() {
        return Long.valueOf(remoteId);
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Long deepSave() {
        user.save();
        return save();
    }

    public static List<Tweet> recentTweets() {
        return new Select().from(Tweet.class).orderBy("id DESC").limit("300").execute();
    }
}
