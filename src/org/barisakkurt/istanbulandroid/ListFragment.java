package org.barisakkurt.istanbulandroid;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListFragment extends Fragment {
	JSONArray problems = null;
	private String urlAllProblems = "http://web.itu.edu.tr/ilbay/istanbulweb/loadBasicProblems.php";
	JSONParser jParser = new JSONParser();
	ListFragment activity;
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_PHOTO = "photo";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_REPORTDATE = "reportdate";
	public List<Problem> probList = new ArrayList<Problem>();
	private boolean mIsCreated=false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.list_tab, container, false);
		

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
    }

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	// //////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////
	// Background Async Task to Load all product by making HTTP Request
	class LoadAllProducts2 extends AsyncTask<String, String, String> {
		public List<Problem> pList = new ArrayList<Problem>();
		ListFragment activity;

		public LoadAllProducts2(ListFragment activity) {
			this.activity = activity;
		}

		// Before starting background thread Show Progress Dialog
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*
			 * pDialog = new ProgressDialog(getActivity());
			 * pDialog.setMessage("Problemler yükleniyor, lüten bekleyiniz...");
			 * pDialog.setIndeterminate(false); pDialog.setCancelable(false);
			 * pDialog.show();
			 */
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
						Log.d("LATTTTTTTTTTTTTT=", "" + latitude);
						String longitude = c.getString(TAG_LONGITUDE);
						String photo = c.getString(TAG_PHOTO);
						String description = c.getString(TAG_DESCRIPTION);
						String reportDate = c.getString(TAG_REPORTDATE);
						String category = "Sorun";

						Problem tempProblem = new Problem(latitude, longitude,
								reportDate);
						tempProblem.setCategory(category);
						tempProblem.setDescription(description);

						// tempProblem.setImagePath(photo);
						pList.add(tempProblem);
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
			// dismiss the dialog after getting all products
			// pDialog.dismiss();
			
			String[] categoryArray=new String[probList.size()];
			Problem[] problemArray=new Problem[probList.size()];
			
			for(int i=0; i<probList.size(); i++) {
				categoryArray[i]=probList.get(i).getCategory();
			}

			
			//ArrayAdapter adapter=new ArrayAdapter<Problem>(getActivity(), android.R.layout.simple_list_item_1, problemArray);
			
			//ProblemAdapter problemAdapter=new ProblemAdapter(getActivity(), android.R.layout.simple_list_item_1, problemArray);
			
			//ArrayAdapter adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categoryArray);
			
			ProblemAdapter probAdapter=new ProblemAdapter(getActivity(),android.R.layout.list_content, probList);
			
			ListView lv = (ListView) getActivity().findViewById(R.id.list);
			lv.setAdapter(probAdapter);
			lv.setOnItemClickListener(new OnItemClickListener()
			{
			     @Override
			     public void onItemClick(AdapterView<?> a, View v,int position, long id) 
			     {
			          //Toast.makeText(getActivity(), "Click", Toast.LENGTH_LONG).show();
			    	// selected item
		              //String product = ((TextView) v).getText().toString();
			    	 
			    	 Problem p1=probList.get(position);
			    	 
			    	 /*Toast toast = Toast.makeText(getActivity(),
			    	            "Item " + (position + 1) + ": " + probList.get(position),
			    	            Toast.LENGTH_SHORT);
			    	        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			    	        toast.show();*/
			    	 
		               
		              // Launching new Activity on selecting single List Item
		              Intent i = new Intent(getActivity(), SingleListActivity.class);
		              // sending data to new activity
		              i.putExtra("description", p1.getDescription());
		              i.putExtra("reportDate", p1.getReportDate());
		              i.putExtra("category", p1.getCategory());
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
 
    /*private view holder class*/
    private class ViewHolder {
        TextView category;
        TextView description;
        TextView reportDate;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Problem rowItem = getItem(position);
 
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.category = (TextView) convertView.findViewById(R.id.category);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            //holder.reportDate = (TextView) convertView.findViewById(R.id.reportDate);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
        holder.category.setText(rowItem.getCategory());
        holder.description.setText(rowItem.getDescription());
        //holder.reportDate.setText(rowItem.getReportDate());
 
        return convertView;
    }
}