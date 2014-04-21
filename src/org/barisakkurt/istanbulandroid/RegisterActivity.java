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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity {
	EditText editTextNameSurname;
	EditText editTextEmail;
	EditText editTextPassword;
	EditText editTextPasswordAgain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		editTextNameSurname = (EditText) findViewById(R.id.editTextNameSurname);
		editTextEmail = (EditText) findViewById(R.id.editTextEmail);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		editTextPasswordAgain = (EditText) findViewById(R.id.editTextPasswordAgain);
	}

	public void registerUser(View v) {
		String strNameSurname="";
		String strEmail="";
		String strPassword="";
		String strPasswordAgain="";

		strNameSurname = editTextNameSurname.getText().toString();
		strEmail = editTextEmail.getText().toString();
		strPassword = editTextPassword.getText().toString();
		strPasswordAgain = editTextPasswordAgain.getText().toString();

		if (strPassword.equals(strPasswordAgain)
				&& Utility.isTextInRange(strPassword)
				&& Utility.validateEmail(strEmail)) {
			String params[] = { strEmail, strPassword, strNameSurname };
			new RegisterTask().execute(params);
		} else {
			Toast.makeText(getApplicationContext(),
					"E-posta ve þifreniz kriterleri karþýlamýyor.",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	public void clear(View v) {
		editTextNameSurname.setText("");
		editTextEmail.setText("");
		editTextPassword.setText("");
		editTextPasswordAgain.setText("");
	}

	class RegisterTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("usermail", params[0]));
			nameValuePairs.add(new BasicNameValuePair("userpassword", params[1]));
			nameValuePairs.add(new BasicNameValuePair("fullname", params[2]));
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Utility.webSiteAddress+"mobile_signup.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity httpEntity = response.getEntity();
				String status = EntityUtils.toString(httpEntity);
				return status;
			} catch (Exception e) {
				System.out.println("Error in http connection " + e.toString());
			}

			return null;
		}

		protected void onPostExecute(String result) {
			boolean validation = false;
			JSONObject resultJson;
			try {
				JSONObject jsonObject = new JSONObject(result);
				resultJson = jsonObject.getJSONObject("result");
				String loginResult = resultJson.getString("status");

				validation = (loginResult.equals("success"));

				if (validation) { // open activity
					String userId = resultJson.getString("userid");
					((GlobalApplication) RegisterActivity.this.getApplication())
							.setUserId(userId);

					Intent myIntent = new Intent(RegisterActivity.this,
							ProblemsActivity.class);
					myIntent.putExtra("userid", userId);
					RegisterActivity.this.startActivity(myIntent);
				} else {
					Toast.makeText(getApplicationContext(), result,
							Toast.LENGTH_LONG).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}
}