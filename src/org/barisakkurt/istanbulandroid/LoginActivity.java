package org.barisakkurt.istanbulandroid;

import org.barisakkurt.istanbulweb.utilty.Utility;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	public void openProblemsActivity(View v) {
		EditText editTextUsername = (EditText)findViewById(R.id.editTextUsername);
		EditText editTextPassword = (EditText)findViewById(R.id.editTextPassword);
		
	    //if(Utility.isTextInRange(password) && Utility.validateEmail(username)) {
			String username=editTextUsername.getText().toString();
			String password=editTextPassword.getText().toString();
			
	    	Intent myIntent = new Intent(LoginActivity.this, ProblemsActivity.class);
	    	//myIntent.putExtra("key", value); //uncomment if you want to add parameter
	    	LoginActivity.this.startActivity(myIntent);
	    //}
	    //else {
	    //	Toast.makeText(getApplicationContext(), "E-posta ve þifreniz kriterleri karþýlamýyor.", Toast.LENGTH_SHORT).show();
	    //}
	}

}
