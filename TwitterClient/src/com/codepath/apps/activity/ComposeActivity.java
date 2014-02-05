package com.codepath.apps.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.RestClient;
import com.codepath.apps.restclienttemplate.RestClientApp;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends Activity {

    private EditText etCompose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = (EditText) findViewById(R.id.et_compose);

        // TODO(ipince): add listener to update char count.
    }

    public void onClickCompose(View view) {
        String body = etCompose.getText().toString();

        // TODO(ipince): Do some client-side checks.

        RestClient client = RestClientApp.getRestClient();
        client.postTweet(body, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
                Tweet tweet = new Tweet(json);

                Intent data = new Intent();
                data.putExtra("tweet", tweet);
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject json) {
                Toast.makeText(ComposeActivity.this, "Unable to send tweet. Try again", Toast.LENGTH_LONG).show();
            }
        });
    }
}
