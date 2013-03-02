package com.pitapat.a721;

import com.pitapat.a721.a721.TaskListColumns;

import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity {
	
	/**
     * The columns we are interested in from the database
     */
    private static final String[] PROJECTION = new String[] {
        TaskListColumns._ID, // 0
        TaskListColumns.TASK, // 1
    };

    /** The index of the title column */
    private static final int COLUMN_INDEX_TITLE = 1;
    
    private static final String[] myString = {"마음 먹은 일"};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		
        // If no data was given in the intent (because we were started
        // as a MAIN activity), then use our default content provider.
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(TaskListColumns.CONTENT_URI);
        }

        // Inform the list we provide context menus for items
        //getListView().setOnCreateContextMenuListener(this);
        
        // Perform a managed query. The Activity will handle closing and requerying the cursor
        // when needed.
		
		//System.out.println(getIntent().getData().toString());
        Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null, TaskListColumns.DEFAULT_SORT_ORDER);
        
        
        // Used to map notes entries from the database to views
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.tasklist_item, cursor,
                new String[] { TaskListColumns.TASK }, new int[] { android.R.id.text1 });
        
		ArrayAdapter<String> addAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myString);
		//setListAdapter(addAdapter);
        
        //ListView lv = getListView();
        //lv.setAdapter(addAdapter);
        //setListAdapter(adapter);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.main:
			return true;
		case R.id.history:
			startActivity(new Intent(this, History.class));
			finish();
			return true;
		case R.id.settings:
			startActivity(new Intent(this, Settings.class));
			finish();
			return true;
		}
		return false;
	}

}
