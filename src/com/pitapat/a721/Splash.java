package com.pitapat.a721;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;

public class Splash extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash);
	    // TODO Auto-generated method stub
	    initialize();
	}

	private void initialize()
	{
		Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				startMainActivity();
				finish();
				overridePendingTransition(0, android.R.anim.fade_out);
			}
		};
		handler.sendEmptyMessageDelayed(0, 1000);
	}
	
	private void startMainActivity()
	{
		startActivity(new Intent(this, MainActivity.class));
	}
}
