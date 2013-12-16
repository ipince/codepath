package codepath.apps.simpletodo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ToDoActivity extends Activity {

    private final List<String> items = new ArrayList<String>();
    private ListAdapter itemsAdapter;

    // UI elements
    private EditText etAddItem;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        etAddItem = (EditText) findViewById(R.id.etAddItem);
        lvItems = (ListView) findViewById(R.id.lvItems);

        itemsAdapter = new ArrayAdapter<String>(
                getBaseContext(), android.R.layout.simple_list_item_1, items);

        lvItems.setAdapter(itemsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
        return true;
    }

    public void addItem(View v) {
        items.add(etAddItem.getText().toString());
        etAddItem.setText("");
    }
}
