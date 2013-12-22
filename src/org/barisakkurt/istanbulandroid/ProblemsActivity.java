package org.barisakkurt.istanbulandroid;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

@SuppressLint("NewApi")
public class ProblemsActivity extends FragmentActivity implements
ActionBar.TabListener {


	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "Harita", "Liste" };
	private String userId;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_problem);

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
				// on changing the page
				// make respected tab selected
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
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		List<Fragment> fragments = fragmentManager.getFragments();
		for(Fragment fragment : fragments)
		{
			if(fragment instanceof MapFragment)
			{
				MapFragment mapFragment = (MapFragment)fragment;
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
		Intent myIntent = new Intent(ProblemsActivity.this, NewReportActivity.class);
		ProblemsActivity.this.startActivity(myIntent);
	}
	
	public void showNearestProblems(View v)
	{
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		List<Fragment> fragments = fragmentManager.getFragments();
		for(Fragment fragment : fragments)
		{
			if(fragment instanceof MapFragment)
			{
				MapFragment mapFragment = (MapFragment)fragment;
				mapFragment.reload();
				break;
			}
		}
	}
}

