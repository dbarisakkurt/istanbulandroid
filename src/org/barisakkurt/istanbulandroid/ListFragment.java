package org.barisakkurt.istanbulandroid;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.barisakkurt.istanbulweb.utilty.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;

public class ListFragment extends Fragment {
	JSONArray problems = null;
	private String urlAllProblems = Utility.webSiteAddress
			+ "loadBasicProblems.php";
	JSONParser jParser = new JSONParser();
	ListFragment activity;
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_PHOTO = "photo";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_REPORTDATE = "reportdate";
	private static final String TAG_CATEGORY = "category";
	public List<Problem> probList = new ArrayList<Problem>();
	Button left, right, middle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.list_tab, container, false);
		
		boolean showBut = ((ProblemsActivity) this.getActivity()).showButtons;
		
		left = (Button) rootView.findViewById(R.id.buttonSendNewProblem);
		middle = (Button) rootView.findViewById(R.id.buttonShowNearestProblems);
		right = (Button) rootView.findViewById(R.id.closeButton);

		if (showBut == false) {
			left.setVisibility(View.INVISIBLE);
			middle.setVisibility(View.INVISIBLE);
			right.setVisibility(View.INVISIBLE);
		}
		
		new LoadAllProducts2(this).execute();

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
		// refrest listview
		ListView lv = (ListView) getActivity().findViewById(R.id.list);
		lv.invalidateViews();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	// Background Async Task to Load all product by making HTTP Request
	class LoadAllProducts2 extends AsyncTask<String, String, String> {
		public List<Problem> pList = new ArrayList<Problem>();
		ListFragment activity;

		public LoadAllProducts2(ListFragment activity) {
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
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

						String latitude = c.getString(TAG_LATITUDE);
						String longitude = c.getString(TAG_LONGITUDE);
						String photo = c.getString(TAG_PHOTO);
						String description = c.getString(TAG_DESCRIPTION);
						String reportDate = c.getString(TAG_REPORTDATE);
						String category = c.getString(TAG_CATEGORY);

						Problem tempProblem=new Problem();
						tempProblem.setLatitude(latitude);
						tempProblem.setLongitude(longitude);
						tempProblem.setReportDate(reportDate);
						tempProblem.setCategory(category);
						tempProblem.setDescription(description);
						tempProblem.setImagePath(photo);

						// tempProblem.setImagePath(photo);
						pList.add(tempProblem);
						tempProblem=null;
					}
				} else {
					// no products found Launch Add New product Activity
				}
			} catch (JSONException e) {
				Log.e("RESPONSE", "RESPONSIVE RESPONSE");
				e.printStackTrace();
			}
			return null;
		}

		// After completing background task Dismiss the progress dialog
		protected void onPostExecute(String file_url) {
			activity.probList = pList;


			ProblemAdapter probAdapter = new ProblemAdapter(getActivity(),
					android.R.layout.list_content, probList);

			ListView lv = (ListView) getActivity().findViewById(R.id.list);
			lv.setAdapter(probAdapter);
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {
					Problem p1 = probList.get(position);

					// Launching new Activity on selecting single List Item
					Intent i = new Intent(getActivity(),
							SingleListActivity.class);
					// sending data to new activity
					i.putExtra("description", p1.getDescription());
					i.putExtra("reportDate", p1.getReportDate());
					i.putExtra("category", p1.getCategory());
					i.putExtra("latitude", p1.getLatitude());
					i.putExtra("longitude", p1.getLongitude());
					i.putExtra("imagePath", p1.getImagePath());
					startActivity(i);
				}
			});
		}
	}
}

class ProblemAdapter extends ArrayAdapter<Problem> {
	Context context;

	public ProblemAdapter(Context context, int resourceId, List<Problem> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView category;
		TextView description;
		ImageView image;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Problem rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.category = (TextView) convertView
					.findViewById(R.id.category);
			holder.description = (TextView) convertView
					.findViewById(R.id.description);

			holder.image = (ImageView) convertView.findViewById(R.id.list_image);
			Log.d("RESIM", Utility.webSiteAddress + rowItem.getImagePath());

			holder.image
					.setTag(Utility.webSiteAddress + rowItem.getImagePath());
			

			AQuery aq = new AQuery(convertView);
            aq.id(R.id.list_image).image( Utility.webSiteAddress + rowItem.getImagePath(), true, true, 0, 0, null, AQuery.FADE_IN_NETWORK, 1.0f);
		

			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.category.setText(rowItem.getCategory());
		if (rowItem.getDescription().length() > 40)
			holder.description.setText(rowItem.getDescription()
					.substring(0, 40) + "...");
		else
			holder.description.setText(rowItem.getDescription());

		return convertView;
	}
}