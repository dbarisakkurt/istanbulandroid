package org.barisakkurt.istanbulandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.base, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_about:
			openAboutActivity();
			return true;

		case R.id.menu_contact:
			openContactActivity();
			return true;

		case R.id.menu_rate_app:
			openRateApp();
			return true;
			
		case R.id.menu_settings:
			openSettingsActivity();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	public void openAboutActivity() {
		Intent myIntent = new Intent(this, AboutActivity.class);
    	startActivity(myIntent);
	}
	
	public void openSettingsActivity() {
		Intent myIntent = new Intent(this, SettingsActivity.class);
    	startActivity(myIntent);
	}
	
	public void openContactActivity() {
		Intent myIntent = new Intent(this, ContactActivity.class);
    	startActivity(myIntent);
	}
	
	public void openRateApp() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
		startActivity(browserIntent);
	}
	
	//returns true if device is online
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	


}
