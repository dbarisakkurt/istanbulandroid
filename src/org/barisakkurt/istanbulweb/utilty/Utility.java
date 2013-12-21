package org.barisakkurt.istanbulweb.utilty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;

public class Utility extends Activity {

	private Utility() {
	} // private constructor

	// is text has min 6 max 18 chars
	public static boolean isTextInRange(String text) {
		if (text.length() < 6)
			return false;
		else if (text.length() > 18)
			return false;
		return true;
	}

	public static boolean validate(final String hex) {
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(hex);
		return matcher.matches();
	}

}
