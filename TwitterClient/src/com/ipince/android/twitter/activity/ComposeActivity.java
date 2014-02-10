package com.ipince.android.twitter.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ipince.android.twitter.R;
import com.ipince.android.twitter.TwitterClientApp;
import com.ipince.android.twitter.client.TwitterClient;
import com.ipince.android.twitter.model.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends Activity {

    private static final int LENGTH_BOUND = 140;

    private EditText etCompose;
    private TextView tvCharCount;
    private Button btnCompose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = (EditText) findViewById(R.id.et_compose);
        tvCharCount = (TextView) findViewById(R.id.tv_char_count);
        btnCompose = (Button) findViewById(R.id.btn_compose);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable text) {
                // Do nothing.
            }

            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                // Do nothing.
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                int length = text.length();
                tvCharCount.setText(String.valueOf(length));
                if (length > LENGTH_BOUND) {
                    tvCharCount.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    btnCompose.setEnabled(false);
                } else {
                    tvCharCount.setTextColor(getResources().getColor(android.R.color.black));
                    btnCompose.setEnabled(true);
                }
            }
        });
    }

    public void onClickCompose(View view) {
        String body = etCompose.getText().toString();

        // TODO(ipince): Do some client-side checks.

        TwitterClient client = TwitterClientApp.getRestClient();
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
