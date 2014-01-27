package com.ipince.android.imagesearch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

    public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageResult = getItem(position);
        SmartImageView ivImage;
        if (convertView == null) {
            // TODO(question): Why pass root but not attach it?
            ivImage = (SmartImageView) LayoutInflater.from(getContext())
                    .inflate(R.layout.item_image_result, parent, false);
        } else {
            ivImage = (SmartImageView) convertView;
            ivImage.setImageResource(android.R.color.transparent);
        }
        ivImage.setImageUrl(imageResult.getThumbUrl());
        return ivImage;
    }
}
