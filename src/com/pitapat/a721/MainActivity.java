package com.pitapat.a721;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
