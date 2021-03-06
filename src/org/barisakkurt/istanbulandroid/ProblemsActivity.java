package org.barisakkurt.istanbulandroid;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//@SuppressLint("NewApi")
public class ProblemsActivity extends ActionBarActivity  implements
		ActionBar.TabListener {
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private android.support.v7.app.ActionBar actionBar;
	private String[] tabs = { "Harita", "Liste" };
	private String userId;
	Button left, middle, right;
	public boolean showButtons=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_problem);

		Intent i = getIntent();
		showButtons = i.getBooleanExtra("showButons", true);

		if (i.getStringExtra("userid") != null) {
			String a = i.getStringExtra("userid");
			Log.d("REGISTER_SONRA", a);
		}

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		//actionBar = getActionBar();
		actionBar=getSupportActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		// on swiping the viewpager make respective tab selected
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// on changing the page make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	protected void onRestart() {
		super.onRestart();
		android.support.v4.app.FragmentManager fragmentManager = this
				.getSupportFragmentManager();
		List<android.support.v4.app.Fragment> fragments = fragmentManager
				.getFragments();
		for (android.support.v4.app.Fragment fragment : fragments) {
			if (fragment instanceof MapFragment) {
				MapFragment mapFragment = (MapFragment) fragment;
				mapFragment.reload();
				break;
			}
		}
	}

	public void closeApp(View v) {
		finish();
	}

	public void sendNewProblem(View v) {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Intent myIntent = new Intent(ProblemsActivity.this,
					NewReportActivity.class);
			ProblemsActivity.this.startActivity(myIntent);
		} else {
			showGPSDisabledAlertToUser();
		}

	}

	public void refreshPage(View v) {
		android.support.v4.app.FragmentManager fragmentManager = this
				.getSupportFragmentManager();
		List<android.support.v4.app.Fragment> fragments = fragmentManager
				.getFragments();
		for (android.support.v4.app.Fragment fragment : fragments) {
			if (fragment instanceof MapFragment) {
				MapFragment mapFragment = (MapFragment) fragment;
				mapFragment.reload();
				break;
			}
		}
	}

	private void showGPSDisabledAlertToUser() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(
						"GPS'iniz kapal�d�r. Uygulaman�n �al��mas� i�in a��k olmal�d�r.")
				.setCancelable(false)
				.setPositiveButton("GPS Ayar�na Git",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("�ptal",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	@Override
	public void onTabReselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab,
			android.support.v4.app.FragmentTransaction arg1) {
		viewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
}
