package com.ipince.android.imagesearch;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    private static final int IMAGE_RESULT_SIZE = 8;

    // UI-related fields.
    private EditText etSearch;
    private GridView gvResults;
    private final List<ImageResult> imageResults = Lists.newArrayList();
    private ArrayAdapter<ImageResult> imageAdapter;


    private final GoogleImagesClient client = new GoogleImagesClient();
    private AdvancedSettings settings = new AdvancedSettings();

    private String query; // current query. could remove this by using closures.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = (EditText) findViewById(R.id.et_query);
        gvResults = (GridView) findViewById(R.id.gv_results);

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

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page > 0) {
                    fetchImages(query, IMAGE_RESULT_SIZE, (page - 1) * IMAGE_RESULT_SIZE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_REQUEST_CODE_SETTINGS && resultCode == RESULT_OK) {
            settings = (AdvancedSettings) data.getSerializableExtra(INTENT_KEY_SETTINGS);
        }
    }

    public void onEditAdvancedSettings(MenuItem item) {
        Intent intent = new Intent(this, EditSettingsActivity.class);
        intent.putExtra(INTENT_KEY_SETTINGS, settings);
        startActivityForResult(intent, INTENT_REQUEST_CODE_SETTINGS);
    }

    public void onClickSearch(View view) {
        // TODO: disable button, add loading icon.
        String query = etSearch.getText().toString();
        if (Strings.isNullOrEmpty(query)) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_no_query), Toast.LENGTH_SHORT).show();
            return;
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        this.query = query;
        imageResults.clear();
        fetchImages(query, IMAGE_RESULT_SIZE, 0);
    }

    private void fetchImages(String query, int size, int offset) {
        client.search(query, size, offset, settings, new ImageSearchCallback() {
            @Override
            public void handle(List<ImageResult> results) {
                for (ImageResult result : results) {
                    imageAdapter.add(result);
                }
            }
        });
    }
}
