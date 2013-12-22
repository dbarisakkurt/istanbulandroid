package org.barisakkurt.istanbulandroid;

import android.app.Application;

public class GlobalApplication extends Application {
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}
