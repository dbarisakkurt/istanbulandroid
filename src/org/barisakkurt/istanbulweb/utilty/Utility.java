package org.barisakkurt.istanbulweb.utilty;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

@SuppressLint("NewApi")
public class Utility extends Activity {
	public static final String webSiteAddress ="http://192.168.1.3/istanbulweb/"; //"http://188.226.204.205/istanbulweb/";
	public static final String emailAddress = "dbarisakkurt@gmail.com";
	@SuppressLint("NewApi")
	public static final String imageFolder = Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
			.toString()
			+ "/Camera/";

	private Utility() {
	} // private constructor

	// is text has minimum 6 max 18 chars
	public static boolean isTextInRange(String text) {
		if (text.length() < 6)
			return false;
		else if (text.length() > 18)
			return false;
		return true;
	}

	// returns true if it is a valid email address
	public static boolean validateEmail(final String hex) {
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(hex);
		return matcher.matches();
	}

	public static Bitmap loadBitmap(String URL, BitmapFactory.Options options) {
		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (IOException e1) {
		}
		return bitmap;
	}

	private static InputStream OpenHttpConnection(String strURL)
			throws IOException {
		InputStream inputStream = null;
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}
		} catch (Exception ex) {
		}
		return inputStream;
	}
}
