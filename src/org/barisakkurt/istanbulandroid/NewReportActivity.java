package org.barisakkurt.istanbulandroid;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class NewReportActivity extends BaseActivity {
	private static final int CAMERA_REQUEST = 1888; 
    private ImageView imageView;

    String timeStamp;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    Button buttonSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_report);
		buttonSubmit = (Button)findViewById(R.id.btnSubmit);
		
		buttonSubmit.setEnabled(false);
		
		Spinner spinner = (Spinner) findViewById(R.id.categorySpinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.categoryArray, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		//camera code
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
	            Locale.getDefault()).format(new Date());
		cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
		           Uri.fromFile(new File("/storage/emulated/0/DCIM/Camera/" + "IMG_" + timeStamp + ".jpg")));
		
        startActivityForResult(cameraIntent, CAMERA_REQUEST); 
	}

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && Activity.RESULT_OK==resultCode) {  
            
			
			try {
		        ImageView imgPreview = (ImageView) findViewById(R.id.problemImage);
		        imgPreview.setVisibility(View.VISIBLE);
	 
	            // bimatp factory
	            BitmapFactory.Options options = new BitmapFactory.Options();
	 
	            // downsizing image as it throws OutOfMemory Exception for larger
	            // images
	            options.inSampleSize = 8;
	            Uri fileUri = getOutputMediaFileUri(1);
	 
	            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
	                    options);
	 
	            imgPreview.setImageBitmap(bitmap);
	            
	            
	            
	            buttonSubmit.setEnabled(true);
	            
	        } catch (NullPointerException e) {
	            e.printStackTrace();
	        }
        } 
	}
	public Uri getOutputMediaFileUri(int type) {
	    return Uri.fromFile(getOutputMediaFile(type));
	}
	
	private File getOutputMediaFile(int type) {
		//File mediaStorageDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File mediaStorageDir=new File("/storage/emulated/0/DCIM/Camera/");
	 
	    // Create the storage directory if it does not exist
	    if (!mediaStorageDir.exists()) {
	        if (!mediaStorageDir.mkdirs()) {
	            Log.d("DIZIN", "Dizin yaratýlamadý.");
	            return null;
	        }
	    }
	 
	    File mediaFile;
	    if (type == 1) {
	    	//timeStamp="20131220_220518";
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                + "IMG_" + timeStamp + ".jpg");
	    } else {
	        return null;
	    }
	 
	    return mediaFile;
	}
	
	public void closeActivity(View v) {
		finish();
	}
	
	public void sendReport(View v) {
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		
		
		Date d = new Date();
		String result="Tarih="+d.toString();
		result+="Longitude"+longitude;
		result+="Latitude"+latitude;
		
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
	}
	
}
