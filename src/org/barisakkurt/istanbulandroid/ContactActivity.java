package org.barisakkurt.istanbulandroid;

import org.barisakkurt.istanbulweb.utilty.Utility;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends BaseActivity {
	Button buttonSend;
	EditText textSubject;
	EditText textMessage;
	CheckBox checkboxDeviceInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		buttonSend = (Button) findViewById(R.id.buttonSend);
		textSubject = (EditText) findViewById(R.id.editTextSubject);
		textMessage = (EditText) findViewById(R.id.editTextMessage);
		checkboxDeviceInfo=(CheckBox) findViewById(R.id.checkBoxDeviceInfo);
 
		buttonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
 
			  String subject = textSubject.getText().toString();
			  String message = textMessage.getText().toString();
 
			  Intent email = new Intent(Intent.ACTION_SEND);
			  email.putExtra(Intent.EXTRA_EMAIL, new String[]{ Utility.emailAddress });
			  email.putExtra(Intent.EXTRA_SUBJECT, subject);
			  
			  
			  if(checkboxDeviceInfo.isChecked()) {
				  message+="\nOS Version:"+System.getProperty("os.version")+"\n";
				  message+="OS API Level:"+android.os.Build.VERSION.SDK_INT+"\n";
				  message+="Device:"+android.os.Build.DEVICE +"\n";
				  message+="Model:"+android.os.Build.MODEL +"\n";
				  message+="Product:"+android.os.Build.PRODUCT +"\n";          
			  }
			  email.putExtra(Intent.EXTRA_TEXT, message);
 
			  //need this to prompts email client only
			  email.setType("message/rfc822");
			  if(isOnline())
				  startActivity(Intent.createChooser(email, getString(R.string.select_email_client)));
			  else
				  Toast.makeText(getApplicationContext(), getString(R.string.internet_connection_required), Toast.LENGTH_SHORT).show();
 
			}
		});
	}


}
