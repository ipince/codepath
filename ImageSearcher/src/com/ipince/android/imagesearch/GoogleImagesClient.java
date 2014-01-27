package com.ipince.android.imagesearch;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class GoogleImagesClient {

    public interface ImageSearchCallback {
        void handle(List<ImageResult> results);
    }

    private static final String ENDPOINT =
            "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&q=";

    private final AsyncHttpClient client = new AsyncHttpClient();

    public void search(String query, final ImageSearchCallback callback) {
        client.get(ENDPOINT + Uri.encode(query), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int responseCode, JSONObject response) {
                try {
                    List<ImageResult> results = ImageResult.fromJson(
                            response.getJSONObject("responseData").getJSONArray("results"));
                    callback.handle(results);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
}
