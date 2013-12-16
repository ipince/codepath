package codepath.apps.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {

    private String item;
    private int pos;

    // UI
    private EditText etEditItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (EditText) findViewById(R.id.etEditItem);

        item = getIntent().getStringExtra("item");
        pos = getIntent().getIntExtra("pos", -1);

        etEditItem.setText(item);
        etEditItem.setSelection(item.length());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_item, menu);
        return true;
    }

    public void saveItem(View v) {
        Intent data = new Intent();
        data.putExtra("item", etEditItem.getText().toString());
        data.putExtra("pos", pos);
        etEditItem.setText("");
        setResult(RESULT_OK, data);
        finish();
    }
}
