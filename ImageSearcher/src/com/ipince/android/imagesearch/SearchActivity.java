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

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page > 0) {
                    fetchImages(query, IMAGE_RESULT_SIZE, (page - 1) * IMAGE_RESULT_SIZE);
                }
                Toast.makeText(getApplicationContext(),
                        "page: " + page + ", totalItemsCount: " + totalItemsCount, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        // TODO: disable button, add loading icon? Get rid of keyboard.
        String query = etSearch.getText().toString();
        if (Strings.isNullOrEmpty(query)) {
            // TODO Toast w error
        }
        this.query = query;
        imageResults.clear();
        fetchImages(query, IMAGE_RESULT_SIZE, 0);
    }

    private void fetchImages(String query, int size, int offset) {
        client.search(query, size, offset, new ImageSearchCallback() {
            @Override
            public void handle(List<ImageResult> results) {
                for (ImageResult result : results) {
                    imageAdapter.add(result);
                }
            }
        });
    }
}
