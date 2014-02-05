package com.codepath.apps.restclienttemplate.models;

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
    @Column(name = "profile_image_url")
    public String profileImageUrl;

    public User() {
        super();
    }

    public User(JSONObject json) {
        super();

        try {
            this.remoteId = json.getString("id_str");
            this.name = json.getString("name");
            this.profileImageUrl = json.getString("profile_image_url");
        } catch (JSONException e) {
            Log.d("User", "Invalid json: " + json.toString(), e);
        }
    }
}
