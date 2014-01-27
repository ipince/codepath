package com.ipince.android.imagesearch;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ipince.android.imagesearch.GoogleImagesClient.ImageSearchCallback;

public class SearchActivity extends Activity {

    public static final String INTENT_KEY_SETTINGS = "settings";
    public static final String INTENT_KEY_IMAGE_RESULT = "imageResult";

    private static final int INTENT_REQUEST_CODE_SETTINGS = 1;

    private EditText etSearch;
    private GridView gvResults;
    private final List<ImageResult> imageResults = Lists.newArrayList();
    private ArrayAdapter<ImageResult> imageAdapter;

    private AdvancedSettings settings;
    private final GoogleImagesClient client = new GoogleImagesClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = (EditText) findViewById(R.id.editText1);
        gvResults = (GridView) findViewById(R.id.gridView1);

        imageAdapter = new ImageResultArrayAdapter(this, imageResults);
        gvResults.setAdapter(imageAdapter);

        gvResults.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                Intent intent = new Intent(getApplicationContext(), ImageDisplayActivity.class);
                intent.putExtra(INTENT_KEY_IMAGE_RESULT, imageResults.get(pos));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    public void onEditAdvancedSettings(MenuItem item) {
        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, EditSettingsActivity.class);
        intent.putExtra(INTENT_KEY_SETTINGS, settings);
        startActivityForResult(intent, INTENT_REQUEST_CODE_SETTINGS);
    }

    public void onClickSearch(View view) {
        // TODO: disable button, add loading icon?
        String query = etSearch.getText().toString();
        if (Strings.isNullOrEmpty(query)) {
            // TODO Toast w error
        }
        client.search(query, new ImageSearchCallback() {

            @Override
            public void handle(List<ImageResult> results) {
                Toast.makeText(SearchActivity.this, "Got " + results.size() + " results", Toast.LENGTH_LONG).show();
                imageResults.clear();
                for (ImageResult result : results) {
                    imageAdapter.add(result);
                }
            }
        });
    }
}
