package org.barisakkurt.istanbulandroid;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.barisakkurt.istanbulweb.utilty.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	public static final String PREFS_NAME = "APP_PREFS";
	EditText editTextUsername;
	EditText editTextPassword;
	CheckBox checkBoxRemember;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Log.d("RESIM", "Dizin:"+MediaStore.Images.Media.DATA);
		Log.d("RESIM", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString());

		
		editTextUsername = (EditText) findViewById(R.id.editTextUsername);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		checkBoxRemember = (CheckBox) findViewById(R.id.checkBoxRememberMe);
		
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);  
        String usrName=sharedPrefs.getString("username_text", "");
        String passwd=sharedPrefs.getString("password_text", "");
        boolean rememberCheck=sharedPrefs.getBoolean("remember_check", false);
        
        editTextUsername.setText(usrName);
        editTextPassword.setText(passwd);
        checkBoxRemember.setChecked(rememberCheck);
	}

	public void openProblemsActivity(View v) {

		String username=editTextUsername.getText().toString();
		String password=editTextPassword.getText().toString();
		
		if(!this.isOnline()) {
			Toast.makeText(getApplicationContext(),
					"Lütfen internet baðlantýnýzý kontrol edin.",
					Toast.LENGTH_SHORT).show();
		}
		else if (Utility.isTextInRange(password) && Utility.validateEmail(username)) {
			String params[] = { username, password };
			
			if(checkBoxRemember.isChecked()) {
				PreferenceManager.getDefaultSharedPreferences(this).edit().putString("username_text", editTextUsername.getText().toString())
				.putString("password_text", editTextPassword.getText().toString()).putBoolean("remember_check",  checkBoxRemember.isChecked()).commit();
			}
			
			
			new LoginTask().execute(params);
		} else {
			Toast.makeText(getApplicationContext(),
					"E-posta ve þifreniz kriterleri karþýlamýyor.",
					Toast.LENGTH_SHORT).show();
		}
	}

	class LoginTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			Log.d("SONUC", "baris");
			nameValuePairs.add(new BasicNameValuePair("usermail", params[0]));
			nameValuePairs.add(new BasicNameValuePair("password", params[1]));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Utility.webSiteAddress+"signin.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity httpEntity = response.getEntity();
				String status = EntityUtils.toString(httpEntity);
				Log.d("SONUC", status);
				return status;
			} catch (Exception e) {
				System.out.println("Error in http connection " + e.toString());
			}

			return null;
		}

		// After completing background task Dismiss the progress dialog
		protected void onPostExecute(String result) {
			boolean validation = false;
			JSONObject resultJson;
			// Log.d("SONUC", result);
			try {

				JSONObject jsonObject = new JSONObject(result);

				resultJson = jsonObject.getJSONObject("result");
				String loginResult = resultJson.getString("status");

				validation = (loginResult.equals("success"));

				if (validation) { // open activity

					String userId = resultJson.getString("id");
					String name = resultJson.getString("name");
					String mail = resultJson.getString("mail");

					((GlobalApplication) LoginActivity.this.getApplication())
							.setUserId(userId);

					Intent myIntent = new Intent(LoginActivity.this,
							ProblemsActivity.class);
					myIntent.putExtra("id", userId);
					LoginActivity.this.startActivity(myIntent);

				} else {
					Toast.makeText(getApplicationContext(), "Kullanýcý adý veya þifreniz yanlýþ",
							Toast.LENGTH_LONG).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

}
