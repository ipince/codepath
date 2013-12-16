package codepath.apps.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoActivity extends Activity {

    private final static int EDIT_ITEM_REQUEST_CODE = 1;

    private List<String> items = new ArrayList<String>();
    private ArrayAdapter<String> itemsAdapter;

    // UI elements
    private EditText etAddItem;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        etAddItem = (EditText) findViewById(R.id.etAddItem);
        lvItems = (ListView) findViewById(R.id.lvItems);

        loadItems();
        itemsAdapter = new ArrayAdapter<String>(
                getBaseContext(), android.R.layout.simple_list_item_1, items);

        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.to_do, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            String item = data.getStringExtra("item");
            int pos = data.getIntExtra("pos", -1);
            if (pos != -1) {
                if (item.isEmpty()) {
                    // delete item.
                    items.remove(pos);
                    itemsAdapter.notifyDataSetChanged();
                } else {
                    // update item.
                    items.remove(pos);
                    items.add(pos, item);
                    itemsAdapter.notifyDataSetChanged();
                }
                saveItems();
            }
        }
    }

    public void addItem(View v) {
        itemsAdapter.add(etAddItem.getText().toString());
        etAddItem.setText("");
        saveItems();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int pos, long id) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                saveItems();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                Intent intent = new Intent(ToDoActivity.this, EditItemActivity.class);
                intent.putExtra("item", itemsAdapter.getItem(pos));
                intent.putExtra("pos", pos);
                startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
            }
        });
    }

    private void loadItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            // no file (well, not really, but OK for now)
            items = new ArrayList<String>();
        }
    }

    private void saveItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            // TODO(ipince): serialize this better to avoid multiline deserialization breakage.
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
