package org.barisakkurt.istanbulandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.barisakkurt.istanbulweb.utilty.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {
	JSONArray problems = null;
	ArrayList<HashMap<String, String>> problemsList;
	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	public List<Problem> probList;
	
	// Progress Dialog
	private ProgressDialog pDialog;
	private String serverUrl = "http://web.itu.edu.tr/ilbay/istanbulweb/";
	private String urlAllProblems = "http://web.itu.edu.tr/ilbay/istanbulweb/loadBasicProblems.php";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_PHOTO = "photo";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_REPORTDATE = "reportdate";


	public GoogleMap googleMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.map_tab, container, false);

		try {
			// Loading map
			initilizeMap();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rootView;
	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getActivity(), "Sorry! unable to create maps",
						Toast.LENGTH_SHORT).show();
			} else {
				new LoadAllProducts(this).execute();
			}
		}
	}

	/*
	 * @Override protected void onResume() { super.onResume(); initilizeMap(); }
	 */

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Background Async Task to Load all product by making HTTP Request
	class LoadAllProducts extends AsyncTask<String, String, String> {
		public List<Problem> pList=new ArrayList<Problem>();
		MapFragment activity;
		
		public LoadAllProducts (MapFragment activity){
		    this.activity=activity;
		}

		// Before starting background thread Show Progress Dialog
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Problemler yükleniyor, lüten bekleyiniz...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		// getting All products from url
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(urlAllProblems, "GET",
					params);

			// Check your log cat for JSON reponse
			Log.d("All Products: ", json.toString());

			try {
				// Log.d("BARIS", Integer.toString(success));
				if (!json.isNull("problems")) {
					// products found
					// Getting Array of Products
					problems = json.getJSONArray("problems");
					Log.d("BARIS", Integer.toString(problems.length()));

					// looping through All Products
					for (int i = 0; i < problems.length(); i++) {
						JSONObject c = problems.getJSONObject(i);

						Log.d("iiiiiiiiiiiiiii", "i=" + Integer.toString(i));
						// Storing each json item in variable
						
						
						String latitude = c.getString(TAG_LATITUDE);
						Log.d("LATTTTTTTTTTTTTT=", ""+latitude);
						String longitude = c.getString(TAG_LONGITUDE);
						String photo = c.getString(TAG_PHOTO);
						String description = c.getString(TAG_DESCRIPTION);
						String reportDate = c.getString(TAG_REPORTDATE);
						String category= "Sorun";
						
						Problem tempProblem=new Problem(latitude, longitude, reportDate);
						tempProblem.setCategory(category);
						tempProblem.setDescription(description);
						
						//tempProblem.setImagePath(photo);
						pList.add(tempProblem);
					}
				} else {
					// no products found Launch Add New product Activity
				}
			} catch (JSONException e) {
				Log.e("RESPONSE","RESPONSIVE RESPONSE");
				e.printStackTrace();
			}
			return null;
		}

		// After completing background task Dismiss the progress dialog
		protected void onPostExecute(String file_url) {
			activity.probList=pList;
			addMarkersToMap();
			// dismiss the dialog after getting all products
			pDialog.dismiss();
		}

	}
	
	private void addMarkersToMap() {

		for(int i=0; i<probList.size(); i++) {
			double d1=Double.parseDouble(probList.get(i).getLatitude());
			double d2=Double.parseDouble(probList.get(i).getLongitude());
			String title="Sorun";
			String body=probList.get(i).getDescription();
			//String url = serverUrl;
			//url+=probList.get(i).getImagePath();
					
	        /*BitmapFactory.Options bmOptions;
	        bmOptions = new BitmapFactory.Options();
	        bmOptions.inSampleSize = 1;*/
	        //Bitmap bm = Utility.loadBitmap(url, bmOptions);
			
			googleMap.addMarker(new MarkerOptions().position(new LatLng(d1, d2)).title(title).snippet(body));
					//.icon(BitmapDescriptorFactory.fromBitmap(bm)));
		}
		
	}
}

class JSONParser {
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {
	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		// Making HTTP request
		try {

			// check for request method
			if (method == "POST") {
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			} else if (method == "GET") {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				Log.d("RESPONSE",""+httpResponse.getStatusLine().getStatusCode());
				HttpEntity httpEntity = httpResponse.getEntity();
				json = EntityUtils.toString(httpEntity);
			}

		} catch (UnsupportedEncodingException e) {
			Log.e("RESPONSE","999999999999999");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e("RESPONSE","88888888888888");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("RESPONSE","8-777777777777777");
			e.printStackTrace();
		} catch (Exception e) {
			Log.e("RESPONSE","776569801298012");
			e.printStackTrace();
		}

		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return jObj;
	}
}
