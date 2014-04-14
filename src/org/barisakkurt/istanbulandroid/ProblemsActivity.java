package org.barisakkurt.istanbulandroid;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

@SuppressLint("NewApi")
public class ProblemsActivity extends FragmentActivity implements
		ActionBar.TabListener {
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
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
		actionBar = getActionBar();
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

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
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

	public void showNearestProblems(View v) {
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
						"GPS'iniz kapalýdýr. Uygulamanýn çalýþmasý için açýk olmalýdýr.")
				.setCancelable(false)
				.setPositiveButton("GPS Ayarýna Git",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("Ýptal",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
}
