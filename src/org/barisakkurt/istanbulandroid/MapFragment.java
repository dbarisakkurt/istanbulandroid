package org.barisakkurt.istanbulandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class MapFragment extends Fragment {
	JSONArray problems = null;
	ArrayList<HashMap<String, String>> problemsList;
	JSONParser jParser = new JSONParser();
	public List<Problem> probList;
	public Set<Problem> problemSet = new HashSet<Problem>();
	Button left, right, middle;
	private ProgressDialog pDialog;
	private String serverUrl = Utility.webSiteAddress;
	private String urlAllProblems = Utility.webSiteAddress+"loadBasicProblems.php";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_PHOTO = "photo";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_REPORTDATE = "reportdate";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_PROBLEM_ID = "problemid";
	public GoogleMap googleMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		boolean showBut=((ProblemsActivity)this.getActivity()).showButtons;

		View rootView = inflater.inflate(R.layout.map_tab, container, false);
		
		left=(Button) rootView.findViewById(R.id.buttonSendNewProblem);
		middle=(Button) rootView.findViewById(R.id.buttonShowNearestProblems);
		right=(Button) rootView.findViewById(R.id.closeButton);

		if(showBut==false) {
			left.setVisibility(View.INVISIBLE);
			middle.setVisibility(View.INVISIBLE);
			right.setVisibility(View.INVISIBLE);
		}
		
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
			googleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getActivity(), "Üzgünüm! Ne yazýk ki haritayý oluþturamadým.",
						Toast.LENGTH_SHORT).show();
			} else {
				new LoadAllProducts(this).execute();
			}
		}
	}

	public void reload() {
		new LoadAllProducts(this).execute();
	}

	/*
	 * @Override protected void onResume() { super.onResume(); initilizeMap(); }
	 */

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	// Background Async Task to Load all product by making HTTP Request
	class LoadAllProducts extends AsyncTask<String, String, String> {
		public List<Problem> pList = new ArrayList<Problem>();
		MapFragment activity;

		public LoadAllProducts(MapFragment activity) {
			this.activity = activity;
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
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.makeHttpRequest(urlAllProblems, "GET",
					params);

			try {
				if (!json.isNull("problems")) {
					// products found
					// Getting Array of Products
					problems = json.getJSONArray("problems");

					// looping through All Products
					for (int i = 0; i < problems.length(); i++) {
						JSONObject c = problems.getJSONObject(i);

						String latitude = c.getString(TAG_LATITUDE);
						String longitude = c.getString(TAG_LONGITUDE);
						String photo = c.getString(TAG_PHOTO);
						String description = c.getString(TAG_DESCRIPTION);
						String reportDate = c.getString(TAG_REPORTDATE);
						String category = c.getString(TAG_CATEGORY);
						String problemId = c.getString(TAG_PROBLEM_ID);

						Problem tempProblem = new Problem(latitude, longitude,
								reportDate);
						tempProblem.setCategory(category);
						tempProblem.setDescription(description);
						tempProblem.setImagePath(photo);
						tempProblem.setLatitude(latitude);
						tempProblem.setLongitude(longitude);
						tempProblem.setProblemId(problemId);

						// tempProblem.setImagePath(photo);
						pList.add(tempProblem);
					}
				} else {
					// no products found Launch Add New product Activity
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		// After completing background task Dismiss the progress dialog
		protected void onPostExecute(String file_url) {
			activity.probList = pList;
			addMarkersToMap();
			// dismiss the dialog after getting all products
			pDialog.dismiss();
		}

	}

	public void showNearestProblems(View v) {
		getActivity().finish();
		startActivity(getActivity().getIntent());
	}

	private void addMarkersToMap() {
		for (int i = 0; i < probList.size(); i++) {
			if (!this.problemSet.contains(probList.get(i))) {
				this.problemSet.add(probList.get(i));

				double d1 = Double.parseDouble(probList.get(i).getLatitude());
				double d2 = Double.parseDouble(probList.get(i).getLongitude());
				String title = probList.get(i).getCategory();
				
				String body=probList.get(i).getProblemId()+";;";
				body += probList.get(i).getDescription()+";;";
				body+=probList.get(i).getImagePath();

				googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
					private final View contents = getActivity().getLayoutInflater().inflate(
							R.layout.map_marker, null);

					@Override
					public View getInfoWindow(Marker marker) {
						// Only changing the content for this tutorial
						// if you return null, it will just use the default
						// window
						return null;
					}

					@Override
					public View getInfoContents(Marker marker) {
						//EventInfo eventInfo = eventMarkerMap.get(marker);
						String title = marker.getTitle();
						String body=marker.getSnippet();
						//String imagePath=marker.get
						//String markerId=marker.get
						
						String[] bodyTextArray=body.split("(;;)");
						String bodyText="";
						String idText="";
						String imagePath="";
						if(bodyTextArray.length==3) {
							idText=bodyTextArray[0];
							bodyText=bodyTextArray[1];
							imagePath=bodyTextArray[2];
						}
						
						
						//ImageView imgView=(ImageView) contents.findViewById(R.id.ivInfoWindowMain);
						AQuery aquery = new AQuery(getActivity(), contents);
						Log.d("RESIM-map",Utility.webSiteAddress + imagePath);
			            aquery.id(R.id.ivInfoWindowMain).image(Utility.webSiteAddress +imagePath, true, true, 0, 0, null, AQuery.FADE_IN_NETWORK, 1.0f).visible();
						
						
						TextView txtTitle = ((TextView) contents
								.findViewById(R.id.txtInfoWindowTitle));
						if (title != null) {
							// Spannable string allows us to edit the formatting
							// of the text.
							SpannableString titleText = new SpannableString(
									title);
							titleText.setSpan(
									new ForegroundColorSpan(Color.RED), 0,
									titleText.length(), 0);
							txtTitle.setText(titleText);
						} else {
							txtTitle.setText("");
						}
						TextView txtType = ((TextView) contents
								.findViewById(R.id.txtInfoWindowEventType));
						txtType.setText(bodyText);
						//txtType.setText(eventInfo.getType());
						return contents;
					}

				});

				 googleMap.addMarker(new MarkerOptions().position(new
				 LatLng(d1, d2)).title(title).snippet(body));
				 
				 
				 googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			            @Override
			            public void onInfoWindowClick(Marker marker) {
			               //Intent intent = new Intent(MapActivity.this,OtherActivity.class);
			               //startActivity(intent);
			            	String allText=marker.getSnippet();
			            	String[] bodyTextArray=allText.split("(;;)");
							String bodyText="";
							String idText="";
							String imagePath="";
							if(bodyTextArray.length==3) {
								idText=bodyTextArray[0];
								bodyText=bodyTextArray[1];
								imagePath=bodyTextArray[2];
								
							}
							
							Problem problemPass=null;
							
							for(Problem p: probList) {
								if(p.getProblemId().equals(idText))
									problemPass=p;
							}
			            	
							if(problemPass!=null) {
								// Launching new Activity on selecting single List Item
								Intent i = new Intent(getActivity(),
										SingleListActivity.class);
								// sending data to new activity
								i.putExtra("description", bodyText);
								i.putExtra("reportDate", problemPass.getReportDate());
								i.putExtra("category", problemPass.getCategory());
								i.putExtra("latitude", problemPass.getLatitude());
								i.putExtra("longitude", problemPass.getLongitude());
								i.putExtra("imagePath", problemPass.getImagePath());
								startActivity(i);

							}
							else {
								Toast.makeText(getActivity().getApplicationContext(), "Bir hata oluþtu.",
										Toast.LENGTH_LONG).show();
							}
			            }
			        });
			}
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
				Log.d("RESPONSE", ""
						+ httpResponse.getStatusLine().getStatusCode());
				HttpEntity httpEntity = httpResponse.getEntity();
				json = EntityUtils.toString(httpEntity);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
