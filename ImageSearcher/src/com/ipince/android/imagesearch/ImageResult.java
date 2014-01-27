package com.ipince.android.imagesearch;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.common.collect.Lists;

public class ImageResult implements Serializable {

    private static final long serialVersionUID = -4973754188772775683L;

    private static final String TAG = "ImageResult";

    private final String imgUrl;
    private final String thumbUrl;

    public ImageResult(String imgUrl, String thumUrl) {
        this.imgUrl = imgUrl;
        this.thumbUrl = thumUrl;
    }

    public static ImageResult fromJson(JSONObject json) {
        try {
            return new ImageResult(
                    json.getString("url"),
                    json.getString("tbUrl"));
        } catch (JSONException e) {
            Log.w("Unable to parse ImageResult from " + json.toString(), e);
        }
        return null; // failed.
    }

    public static List<ImageResult> fromJson(JSONArray json) {
        List<ImageResult> results = Lists.newArrayList();
        for (int i = 0; i < json.length(); i++) {
            try {
                ImageResult result = fromJson(json.getJSONObject(i));
                if (result != null) {
                    results.add(result);
                }
            } catch (JSONException e) {
                Log.w(TAG, "Unable to get entry " + i + " from json array: " + json.toString(),
                        e);
            }
        }
        return results;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }
}
