package com.ipince.android.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class EditSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_settings);

        loadOptionsFromIntent();

        // setResult()
        // finish()
    }

    private void loadOptionsFromIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            // TODO: Toast and return?
        }

        AdvancedSettings settings =
                (AdvancedSettings) intent.getSerializableExtra(SearchActivity.INTENT_KEY_SETTINGS);
        // TODO: check settings null

        setSettings(settings);
    }

    private void setSettings(AdvancedSettings settings) {
        Toast.makeText(this, "Seeting: " + settings.getSize(), Toast.LENGTH_LONG).show();
    }
}
