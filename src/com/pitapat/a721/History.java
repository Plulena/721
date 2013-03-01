package com.pitapat.a721;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class History extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.history);
	    // TODO Auto-generated method stub
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
			startActivity(new Intent(this, MainActivity.class));
			finish();
			return true;
		case R.id.history:
			return true;
		case R.id.settings:
			startActivity(new Intent(this, Settings.class));
			finish();
			return true;
		}
		return false;
	}
}
