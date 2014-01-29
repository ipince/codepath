package com.ipince.android.imagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.common.base.Preconditions;
import com.ipince.android.imagesearch.AdvancedSettings.Color;
import com.ipince.android.imagesearch.AdvancedSettings.Size;
import com.ipince.android.imagesearch.AdvancedSettings.Type;

public class EditSettingsActivity extends Activity {

    private Spinner spinSize;
    private Spinner spinColor;
    private Spinner spinType;
    private EditText etSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_settings);

        spinSize = (Spinner) findViewById(R.id.spin_size);
        spinSize.setAdapter(getEnumAdapter(Size.values()));
        spinColor = (Spinner) findViewById(R.id.spin_color);
        spinColor.setAdapter(getEnumAdapter(Color.values()));
        spinType = (Spinner) findViewById(R.id.spin_type);
        spinType.setAdapter(getEnumAdapter(Type.values()));
        etSite = (EditText) findViewById(R.id.et_site);

        loadOptionsFromIntent();
    }

    private <T extends Enum<T>> ArrayAdapter<T> getEnumAdapter(T[] e) {
        return new ArrayAdapter<T>(this, android.R.layout.simple_spinner_item, e);
    }

    private void loadOptionsFromIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            // TODO: Toast?
            return;
        }

        AdvancedSettings settings =
                (AdvancedSettings) intent.getSerializableExtra(SearchActivity.INTENT_KEY_SETTINGS);

        setSettings(settings);
    }

    private void setSettings(AdvancedSettings settings) {
        Preconditions.checkNotNull(settings);
        spinSize.setSelection(settings.getSize().ordinal());
        spinColor.setSelection(settings.getColor().ordinal());
        spinType.setSelection(settings.getType().ordinal());
        etSite.setText(settings.getSite());
    }

    private AdvancedSettings getSettings() {
        AdvancedSettings settings = new AdvancedSettings();
        settings.setSize(Size.values()[spinSize.getSelectedItemPosition()]);
        settings.setColor(Color.values()[spinColor.getSelectedItemPosition()]);
        settings.setType(Type.values()[spinType.getSelectedItemPosition()]);
        settings.setSite(etSite.getText().toString());
        return settings;
    }

    public void onSaveSettings(View view) {
        AdvancedSettings settings = getSettings();

        Intent intent = new Intent();
        intent.putExtra(SearchActivity.INTENT_KEY_SETTINGS, settings);
        setResult(RESULT_OK, intent);
        finish();
    }

    // TODO: make back button save settings too? or no need for Save button.
}
