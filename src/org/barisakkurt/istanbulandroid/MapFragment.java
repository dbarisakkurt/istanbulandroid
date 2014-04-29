package org.barisakkurt.istanbulandroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.http.NameValuePair;
import org.barisakkurt.istanbulweb.utilty.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {
	JSONArray problems = null;
	ArrayList<HashMap<String, String>> problemsList;
	JSONParser jParser = new JSONParser();
	private Set<Problem> problemSet = new HashSet<Problem>();
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_PHOTO = "photo";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_REPORTDATE = "reportdate";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_PROBLEM_ID = "problemid";
	public GoogleMap googleMap;
	Button left, right, middle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		boolean showBut = ((ProblemsActivity) this.getActivity()).showButtons;
		View rootView = inflater.inflate(R.layout.map_tab, container, false);

		left = (Button) rootView.findViewById(R.id.buttonSendNewProblem);
		middle = (Button) rootView.findViewById(R.id.buttonShowNearestProblems);
		right = (Button) rootView.findViewById(R.id.closeButton);

		if (showBut == false) {
			left.setVisibility(View.INVISIBLE);
			middle.setVisibility(View.INVISIBLE);
			right.setVisibility(View.INVISIBLE);
		}

		try {
			initilizeMap();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return rootView;
	}

	// assistant method1
	public float distanceBetween(LatLng latLng1, LatLng latLng2) {
		float[] results = new float[3];
		Location.distanceBetween(latLng1.latitude, latLng1.longitude,
				latLng2.latitude, latLng2.longitude, results);
		return results[0];
	}

	// assistant method2
	public LatLngBounds getBoundsForPoints(List<LatLng> coordinates) {
		LatLngBounds.Builder builder = LatLngBounds.builder();
		for (LatLng coordinate : coordinates) {
			builder.include(coordinate);
		}
		return builder.build();
	}

	// assistant method3
	public boolean isWithinBound(LatLng latLng, LatLngBounds bounds) {
		return bounds.contains(latLng);
	}

	// assistant method4
	public String getStreetNameForAddress(Address address) {
		String streetName = address.getAddressLine(0);
		if (streetName == null) {
			streetName = address.getThoroughfare();
		}
		return streetName;
	}

	// assistant method5
	public static Address getAddressForLocation(Context context,
			Location location) {
		try {
			Geocoder geocoder = new Geocoder(context);
			List<Address> addresses = geocoder.getFromLocation(
					location.getLatitude(), location.getLongitude(), 1);
			if (addresses != null && addresses.size() > 0) {
				return addresses.get(0);
			} else {
				return null;
			}
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		/*
		 * float uzak=distanceBetween(new LatLng(41.088821, 28.97337), new
		 * LatLng(41.082085, 28.99103)); Log.d("ASSISTANT", "Uzaklik"+uzak);
		 * 
		 * Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
		 * List<Address> addresses=null; try { addresses =
		 * geocoder.getFromLocation(41.09043, 29.043086, 1); Log.d("ASSISTANT",
		 * "Adres1="+addresses.get(0)); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
				private final View contents = getActivity().getLayoutInflater()
						.inflate(R.layout.map_marker, null);

				@Override
				public View getInfoWindow(Marker marker) {
					return null;
				}

				@Override
				public View getInfoContents(Marker marker) {
					String title = marker.getTitle();
					String body = marker.getSnippet();

					String[] bodyTextArray = body.split("(;;)");
					String bodyText = "";
					String idText = "";
					String imagePath = "";
					if (bodyTextArray.length == 3) {
						idText = bodyTextArray[0];
						bodyText = bodyTextArray[1];
						imagePath = bodyTextArray[2];
					}

					// ImageView imgView=(ImageView)
					// contents.findViewById(R.id.ivInfoWindowMain);
					/*
					 * AQuery aquery = new AQuery(getActivity(), contents);
					 * Log.d("RESIM-map", Utility.webSiteAddress + imagePath);
					 * aquery.id(R.id.ivInfoWindowMain)
					 * .image(Utility.webSiteAddress + imagePath, true, true, 0,
					 * 0, null, AQuery.FADE_IN_NETWORK, 1.0f).visible();
					 */

					TextView txtTitle = ((TextView) contents
							.findViewById(R.id.txtInfoWindowTitle));
					if (title != null) {
						// Spannable string allows us to edit the formatting
						// of the text.
						SpannableString titleText = new SpannableString(title);
						titleText.setSpan(new ForegroundColorSpan(Color.RED),
								0, titleText.length(), 0);
						txtTitle.setText(titleText);
					} else {
						txtTitle.setText("");
					}
					TextView txtType = ((TextView) contents
							.findViewById(R.id.txtInfoWindowEventType));
					txtType.setText(bodyText);

					return contents;
				}

			});

			googleMap
					.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
						@Override
						public void onInfoWindowClick(Marker marker) {
							String allText = marker.getSnippet();
							String[] bodyTextArray = allText.split("(;;)");
							String bodyText = "";
							String idText = "";
							String imagePath = "";
							if (bodyTextArray.length == 3) {
								idText = bodyTextArray[0];
								bodyText = bodyTextArray[1];
								imagePath = bodyTextArray[2];

							}
							final View content2 = getActivity()
									.getLayoutInflater().inflate(
											R.layout.map_marker, null);

							AQuery aquery = new AQuery(getActivity(), content2);
							aquery.id(R.id.ivInfoWindowMain)
									.image(Utility.webSiteAddress + imagePath,
											true, true, 0, 0, null,
											AQuery.FADE_IN_NETWORK, 1.0f)
									.visible();

							Problem problemPass = null;
							Object[] problemArray = problemSet.toArray();

							for (int i = 0; i < problemArray.length; i++) {
								if (((Problem) problemArray[i]).getProblemId()
										.equals(idText)) {
									problemPass = (Problem) problemArray[i];
									if (problemPass != null) {
										Intent intent = new Intent(
												getActivity(),
												SingleListActivity.class);
										intent.putExtra("description", bodyText);
										intent.putExtra("reportDate",
												problemPass.getReportDate());
										intent.putExtra("category",
												problemPass.getCategory());
										intent.putExtra("latitude",
												problemPass.getLatitude());
										intent.putExtra("longitude",
												problemPass.getLongitude());
										intent.putExtra("imagePath",
												problemPass.getImagePath());
										startActivity(intent);

									}
									break;
								} else {
									Toast.makeText(
											getActivity()
													.getApplicationContext(),
											"Bir hata oluþtu.",
											Toast.LENGTH_LONG).show();
								}
							}
						}
					});

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getActivity(),
						"Üzgünüm! Ne yazýk ki haritayý oluþturamadým.",
						Toast.LENGTH_SHORT).show();
			} else {
				new LoadAllProblemsWithoutImages(this).execute();
			}
		}
	}

	public void reload() {
		new LoadAllProblemsWithoutImages(this).execute();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	// Background Async Task to Load all product by making HTTP Request
	class LoadAllProblemsWithoutImages extends
			AsyncTask<String, String, String> {
		private ProgressDialog pDialog;
		private List<Problem> problemList = new ArrayList<Problem>();
		MapFragment mapFragment;

		public LoadAllProblemsWithoutImages(MapFragment mapFragment) {
			this.mapFragment = mapFragment;
		}

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
			JSONObject json = jParser.makeHttpRequest(Utility.webSiteAddress
					+ "loadBasicProblems.php", "GET", params);

			try {
				if (!json.isNull("problems")) {
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
						problemList.add(tempProblem);
					}
				} else {
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		// After completing background task Dismiss the progress dialog
		protected void onPostExecute(String file_url) {
			// mapFragment.probList = problemList;
			addMarkersToMap(problemList);
			pDialog.dismiss();
		}

	}

	public void showNearestProblems(View v) {
		getActivity().finish();
		startActivity(getActivity().getIntent());
	}

	private void addMarkersToMap(final List<Problem> problemList) {
		for (int i = 0; i < problemList.size(); i++) {
			if (!this.problemSet.contains(problemList.get(i))) {
				this.problemSet.add(problemList.get(i));

				Log.d("LATLANG", problemList.get(i).getLatitude() + "");

				double d1 = Double
						.parseDouble(problemList.get(i).getLatitude());
				double d2 = Double.parseDouble(problemList.get(i)
						.getLongitude());
				String title = problemList.get(i).getCategory();

				String body = problemList.get(i).getProblemId() + ";;";
				body += problemList.get(i).getDescription() + ";;";
				body += problemList.get(i).getImagePath();

				googleMap.addMarker(new MarkerOptions()
						.position(new LatLng(d1, d2)).title(title)
						.snippet(body));

			}
		}

	}
}
