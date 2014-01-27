package com.ipince.android.imagesearch;

import android.app.Activity;
import android.os.Bundle;

import com.loopj.android.image.SmartImageView;

public class ImageDisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);

        ImageResult imageResult =
                (ImageResult) getIntent().getSerializableExtra(SearchActivity.INTENT_KEY_IMAGE_RESULT);
        ivImage.setImageUrl(imageResult.getImgUrl());
    }
}
