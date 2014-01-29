package com.ipince.android.imagesearch;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

import com.google.common.collect.Maps;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class GoogleImagesClient {

    private static final String TAG = "GoogleImagesClient";

    public interface ImageSearchCallback {
        void handle(List<ImageResult> results);
    }

    private static final String ENDPOINT =
            "https://ajax.googleapis.com/ajax/services/search/images";

    private final AsyncHttpClient client = new AsyncHttpClient();

    public void search(String query, int size, int offset,
            final ImageSearchCallback callback) {
        String req = buildRequest(query, size, offset);
        client.get(req, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int responseCode, JSONObject response) {
                try {
                    List<ImageResult> results = ImageResult.fromJson(
                            response.getJSONObject("responseData").getJSONArray("results"));
                    callback.handle(results);
                } catch (JSONException e) {
                    Log.w(TAG, "Unable to parse json response: " + response.toString(), e);
                }
            }
        });
    }

    private String buildRequest(String query, int size, int offset) {
        Map<String, String> args = Maps.newHashMap();
        args.put("v", "1.0");
        args.put("q", Uri.encode(query));
        args.put("rsz", String.valueOf(size));
        args.put("start", String.valueOf(offset));

        String argString = "";
        for (String key : args.keySet()) {
            argString += key + "=" + args.get(key) + "&";
        }

        return ENDPOINT + "?" + argString;
    }
}
