package com.ipince.android.twitter.client;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API.
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes:
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "Dx1LPLKCCcefeg2LJG4ZYg";
    public static final String REST_CONSUMER_SECRET = "bi7PdS2ScN1t16svmumbv2FHce8PgwcYOwznviW2Y";
    public static final String REST_CALLBACK_URL = "oauth://trivialtweetclient";

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getUser(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        getClient().get(apiUrl, handler);
    }

    public void getUser(String handle, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", handle);
        getClient().get(apiUrl, params, handler);
    }

    public void getHomeTimeline(Long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", "25");
        if (maxId != null) {
            params.put("max_id", String.valueOf(maxId));
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getMentions(Long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", "25");
        if (maxId != null) {
            params.put("max_id", String.valueOf(maxId));
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(String handle, Long maxId, AsyncHttpResponseHandler handler) {
        getTimeline("user", handle, maxId, handler);
    }

    public void getUserTimeline(Long maxId, AsyncHttpResponseHandler handler) {
        getTimeline("user", null /* handle */, maxId, handler);
    }

    private void getTimeline(String type, String handle, Long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/" + type + "_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", "25");
        if (handle != null) {
            params.put("screen_name", handle);
        }
        if (maxId != null) {
            params.put("max_id", String.valueOf(maxId));
        }
        getClient().get(apiUrl, params, handler);
    }

    public void postTweet(String body, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", body);
        getClient().post(apiUrl, params, handler);
    }
}