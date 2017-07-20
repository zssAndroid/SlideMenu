package com.sen.slidemenu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private SlidingMenu slidingMenu;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		slidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
	}

	
	public void menuClick(View v){
		slidingMenu.toggle();
	}
	
	public void menuItemClick(View v){
		TextView textView = (TextView) v;
		Toast.makeText(this, textView.getText().toString(), 0).show();
	}

}
