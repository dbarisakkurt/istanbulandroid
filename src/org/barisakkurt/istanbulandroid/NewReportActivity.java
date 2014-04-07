package org.barisakkurt.istanbulandroid;

//193.140.196.117

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class NewReportActivity extends BaseActivity {
	private static final int CAMERA_REQUEST = 1888;
	private ImageView imageView;
	String filePath = "/storage/emulated/0/DCIM/Camera/";
	String userId;
	EditText edtAddress, edtDescription;

	String timeStamp;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	Button buttonSubmit;
	Spinner spinner;
	EditText editTextLat, editTextLong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_report);

		spinner = (Spinner) findViewById(R.id.categorySpinner);
		editTextLat = (EditText) findViewById(R.id.editTextLatitude1);
		editTextLong = (EditText) findViewById(R.id.editTextLongitude1);
		
		edtAddress = (EditText) findViewById(R.id.editTextAddress);
		edtDescription = (EditText) findViewById(R.id.editTextDescription);

		userId = ((GlobalApplication) getApplication()).getUserId();

		buttonSubmit = (Button) findViewById(R.id.btnSubmit);
		buttonSubmit.setEnabled(false);

		//Spinner spinner = (Spinner) findViewById(R.id.categorySpinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.categoryArray,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		// camera code
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
				.format(new Date());
		cameraIntent.putExtra(
				android.provider.MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File("/storage/emulated/0/DCIM/Camera/"
						+ "IMG_" + timeStamp + ".jpg")));

		startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK
				&& Activity.RESULT_OK == resultCode) {

			try {
				ImageView imgPreview = (ImageView) findViewById(R.id.problemImage);
				imgPreview.setVisibility(View.VISIBLE);

				// bimatp factory
				BitmapFactory.Options options = new BitmapFactory.Options();

				// downsizing image as it throws OutOfMemory Exception for
				// larger
				// images
				options.inSampleSize = 8;
				Uri fileUri = getOutputMediaFileUri(1);

				final Bitmap bitmap = BitmapFactory.decodeFile(
						fileUri.getPath(), options);

				imgPreview.setImageBitmap(bitmap);

				buttonSubmit.setEnabled(true);
				
				
				
				
				LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Location location = lm
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				double longitude = location.getLongitude();
				double latitude = location.getLatitude();
				String strLongitude = Double.toString(longitude);
				String strLatitude = Double.toString(latitude);
				
				editTextLat.setText(strLatitude);
				editTextLong.setText(strLongitude);

			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private File getOutputMediaFile(int type) {
		// mediaStorageDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File mediaStorageDir = new File("/storage/emulated/0/DCIM/Camera/");

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("DIZIN", "Dizin yaratýlamadý.");
				return null;
			}
		}

		File mediaFile;
		if (type == 1) {
			// timeStamp="20131220_220518";
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
			filePath = mediaFile.toString();
		} else {
			return null;
		}

		return mediaFile;
	}

	public void closeActivity(View v) {
		finish();
	}

	public void sendReport(View v) {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		
		
		
		finish();
		
		try {
			new UploadReport().execute(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void upload(String filepath) throws IOException {
		String url = "http://193.140.196.117/istanbulweb/addProblem.php";
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httppost = new HttpPost(url);
		File file = new File(filepath);
		MultipartEntity mpEntity = new MultipartEntity();
		ContentBody cbFile = new FileBody(file, "image/jpeg");
		mpEntity.addPart("userfile", cbFile);
		httppost.setEntity(mpEntity);
		System.out.println("executing request " + httppost.getRequestLine());
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();
		// check the response and do what is required
	}

	class UploadReport extends AsyncTask<String, String, String> {
		InputStream inputStream;

		public String convertResponseToString(HttpResponse response)
				throws IllegalStateException, IOException {

			String res = "";
			StringBuffer buffer = new StringBuffer();
			inputStream = response.getEntity().getContent();
			int contentLength = (int) response.getEntity().getContentLength(); // getting
																				// content
																				// length…..
			Toast.makeText(NewReportActivity.this,
					"contentLength : " + contentLength, Toast.LENGTH_LONG)
					.show();
			if (contentLength < 0) {
			} else {
				byte[] data = new byte[512];
				int len = 0;
				try {
					while (-1 != (len = inputStream.read(data))) {
						buffer.append(new String(data, 0, len)); // converting
																	// to string
																	// and
																	// appending
																	// to
																	// stringbuffer…..
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					inputStream.close(); // closing the stream…..
				} catch (IOException e) {
					e.printStackTrace();
				}
				res = buffer.toString(); // converting stringbuffer to string…..

				Toast.makeText(NewReportActivity.this, "Result : " + res,
						Toast.LENGTH_LONG).show();
				// System.out.println("Response => " +
				// EntityUtils.toString(response.getEntity()));
			}
			return res;
		}

		@SuppressLint("NewApi")
		@Override
		protected String doInBackground(String... params) {

			Bitmap bitmap = BitmapFactory.decodeFile(params[0]);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream); // compress
																		// to
																		// which
																		// format
																		// you
																		// want.
			byte[] byte_arr = stream.toByteArray();
			String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			Date now = new Date();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

			// Get the date today using Calendar object.
			Date today = Calendar.getInstance().getTime();
			// Using DateFormat format method we can create a string
			// representation of a date with the defined format.
			String reportDate = df.format(today);

			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = lm
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			String strLongitude = Double.toString(longitude);
			String strLatitude = Double.toString(latitude);


			String address = edtAddress.getText().toString();
			String description = edtDescription.getText().toString();
			
			String category = String.valueOf(spinner.getSelectedItem());
			

			nameValuePairs.add(new BasicNameValuePair("image", image_str));
			nameValuePairs.add(new BasicNameValuePair("reportdate", reportDate));
			nameValuePairs.add(new BasicNameValuePair("id", userId));
			nameValuePairs
					.add(new BasicNameValuePair("latitude", strLatitude));
			nameValuePairs
					.add(new BasicNameValuePair("longitude", strLongitude));
			nameValuePairs.add(new BasicNameValuePair("address", address));
			nameValuePairs.add(new BasicNameValuePair("description",
					description));
			
			
			nameValuePairs.add(new BasicNameValuePair("category", category));

			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://193.140.196.117/istanbulweb/addProblem.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				String the_string_response = convertResponseToString(response);
			} catch (Exception e) {
				System.out.println("Error in http connection " + e.toString());
			}

			return null;
		}
	}
}
