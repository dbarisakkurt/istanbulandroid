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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	EditText editTextUsername;
	EditText editTextPassword;
	CheckBox checkBoxRemember;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Log.d("RESIM", Utility.imageFolder);

		editTextUsername = (EditText) findViewById(R.id.editTextUsername);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		checkBoxRemember = (CheckBox) findViewById(R.id.checkBoxRememberMe);

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String usrName = sharedPrefs.getString("username_text", "");
		String passwd = sharedPrefs.getString("password_text", "");
		boolean rememberCheck = sharedPrefs.getBoolean("remember_check", false);

		editTextUsername.setText(usrName);
		editTextPassword.setText(passwd);
		checkBoxRemember.setChecked(rememberCheck);
	}

	public void openProblemsActivity(View v) {

		String username = editTextUsername.getText().toString();
		String password = editTextPassword.getText().toString();

		if (!isOnline()) {
			Toast.makeText(getApplicationContext(),
					"Please ensure that you are online.", Toast.LENGTH_SHORT)
					.show();
		} else if (Utility.isTextInRange(password)
				&& Utility.validateEmail(username)) {
			String params[] = { username, password };

			if (checkBoxRemember.isChecked()) {
				PreferenceManager
						.getDefaultSharedPreferences(this)
						.edit()
						.putString("username_text",
								editTextUsername.getText().toString())
						.putString("password_text",
								editTextPassword.getText().toString())
						.putBoolean("remember_check",
								checkBoxRemember.isChecked()).commit();
			}

			new LoginTask().execute(params);
		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.internet_connection_required),
					Toast.LENGTH_SHORT).show();
		}
	}

	class LoginTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			nameValuePairs.add(new BasicNameValuePair("usermail", params[0]));
			nameValuePairs.add(new BasicNameValuePair("password", params[1]));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Utility.webSiteAddress
						+ "signin.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity httpEntity = response.getEntity();
				String status = EntityUtils.toString(httpEntity);
				Log.d("ISTANBUL_AND_LOGIN_RESULT", status);
				return status;
			} catch (Exception e) {
				Log.e("ISTANBUL_AND_ERROR", "Error in http connection " + e.toString());
			}

			return null;
		}

		// After completing background task Dismiss the progress dialog
		protected void onPostExecute(String result) {
			boolean validation = false;
			JSONObject resultJson;
			try {
				JSONObject jsonObject = new JSONObject(result);

				resultJson = jsonObject.getJSONObject("result");
				String loginResult = resultJson.getString("status");

				validation = (loginResult.equals("success"));

				if (validation) { // open activity
					String userId = resultJson.getString("id");

					((GlobalApplication) LoginActivity.this.getApplication())
							.setUserId(userId);

					Intent myIntent = new Intent(LoginActivity.this,
							ProblemsActivity.class);
					myIntent.putExtra("id", userId);
					LoginActivity.this.startActivity(myIntent);

				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.wrong_username_password), Toast.LENGTH_LONG)
							.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}

}
