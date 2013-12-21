package org.barisakkurt.istanbulandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void openLoginActivity(View v) {
	    if(isOnline()==true) {
	    	Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
	    	//myIntent.putExtra("key", value); //uncomment if you want to add parameter
	    	MainActivity.this.startActivity(myIntent);
	    	
	    }
	    else {
	    	Toast.makeText(getApplicationContext(), "Bu uygulamayý kullanabilmek için internet baðlantýsý gerekiyor.", Toast.LENGTH_SHORT).show();
	    }
	}
}
