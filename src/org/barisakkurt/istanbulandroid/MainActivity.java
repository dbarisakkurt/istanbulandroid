package org.barisakkurt.istanbulandroid;

import android.content.Intent;
import android.net.Uri;
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
	
	public void openRegisterPage(View v) {
		Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
		startActivity(myIntent);
		/*String url="http://web.itu.edu.tr/ilbay/istanbulweb/signup.php";
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);*/
	}
	
	/*public void openAboutActivity(View v) {
		Intent myIntent = new Intent(MainActivity.this, AboutActivity.class);
    	MainActivity.this.startActivity(myIntent);
	}*/
}
