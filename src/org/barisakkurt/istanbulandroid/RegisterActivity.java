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
import org.barisakkurt.istanbulandroid.LoginActivity.LoginTask;
import org.barisakkurt.istanbulweb.utilty.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	public void openProblemsActivity(View v) {
		EditText editTextNameSurname = (EditText) findViewById(R.id.editTextNameSurname);
		EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
		EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		EditText editTextPasswordAgain = (EditText) findViewById(R.id.editTextPasswordAgain);

		String strNameSurname = editTextNameSurname.getText().toString();
		String strEmail = editTextEmail.getText().toString();
		String strPassword = editTextPassword.getText().toString();
		String strPasswordAgain = editTextPasswordAgain.getText().toString();

		/*
		 * if (strPassword.equals(strPasswordAgain) &&
		 * Utility.isTextInRange(strPassword) &&
		 * Utility.validateEmail(strEmail)) { String params[] = { strEmail,
		 * strPassword }; new RegisterTask().execute(params); } else {
		 * Toast.makeText(getApplicationContext(),
		 * "E-posta ve þifreniz kriterleri karþýlamýyor.",
		 * Toast.LENGTH_SHORT).show(); }
		 */
	}

	/*
	 * class RegisterTask extends AsyncTask<String, String, String> {
	 * 
	 * @Override protected String doInBackground(String... params) {
	 * ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	 * 
	 * nameValuePairs.add(new BasicNameValuePair("usermail", params[0]));
	 * nameValuePairs.add(new BasicNameValuePair("password", params[1]));
	 * 
	 * try { HttpClient httpclient = new DefaultHttpClient(); HttpPost httppost
	 * = new HttpPost( "http://193.140.196.117/istanbulweb/signin.php");
	 * httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	 * HttpResponse response = httpclient.execute(httppost); HttpEntity
	 * httpEntity = response.getEntity(); String status =
	 * EntityUtils.toString(httpEntity); Log.d("SONUC", status); return status;
	 * } catch (Exception e) { System.out.println("Error in http connection " +
	 * e.toString()); }
	 * 
	 * return null; }
	 * 
	 * // After completing background task Dismiss the progress dialog protected
	 * void onPostExecute(String result) { boolean validation = false;
	 * JSONObject resultJson; // Log.d("SONUC", result); try {
	 * 
	 * JSONObject jsonObject = new JSONObject(result);
	 * 
	 * resultJson = jsonObject.getJSONObject("result"); String loginResult =
	 * resultJson.getString("status");
	 * 
	 * validation = (loginResult.equals("success"));
	 * 
	 * if (validation) { // open activity
	 * 
	 * String userId = resultJson.getString("id"); String name =
	 * resultJson.getString("name"); String mail = resultJson.getString("mail");
	 * 
	 * ((GlobalApplication) LoginActivity.this.getApplication())
	 * .setUserId(userId);
	 * 
	 * Intent myIntent = new Intent(LoginActivity.this, ProblemsActivity.class);
	 * myIntent.putExtra("id", userId);
	 * LoginActivity.this.startActivity(myIntent);
	 * 
	 * } else { Toast.makeText(getApplicationContext(), result,
	 * Toast.LENGTH_LONG).show(); }
	 * 
	 * } catch (JSONException e) { e.printStackTrace(); }
	 */

}
