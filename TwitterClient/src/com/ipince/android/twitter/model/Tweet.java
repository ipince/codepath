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
import com.activeandroid.annotation.Table;

@Table(name = "tweets")
public class Tweet extends Model implements Serializable {
    // Define database columns and associated fields
    @Column(name = "remote_id")
    String remoteId;
    @Column(name = "user")
    User user;
    @Column(name = "body")
    String body;

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
}
