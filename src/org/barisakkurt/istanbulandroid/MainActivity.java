package org.barisakkurt.istanbulandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


//TODO düzelttiklerim
//TODO MainActivity, LoginActivity, RegisterActivity, AboutActivity, ContactActivity, BaseActivty
//TODO SetingsActivity düzelmez.
//TODO resimleri haritada göster
//TODO cep telefonunda problems activity açýlmýyor.
//TODO farklý birkaç telefonda test et.
//TODO register a isonline mi kontrolü ekle.
//TODO sorun detail activity sadece portait olsun ve scrollview ekle, resmi ortala.
//TODO map fragmenti yükleyemiyor.
//TODO resim çekince kameradan resim gelmiyor gibi biþe var.
//TODO sorun istanbul baþlýðýný ortala
//TODO show all problemse is online mi kontrolü ekle

//TODO verilen oylarý göster
//TODO alýnan oya göre sýralama
//TODO son 1 ayda eklenen içeriðini gösterme.
//TODO dosyalarý servera kaydederken isimere kullanici adlarini da ekle.
//TODO user, citizenuser gibi kullanýcý ile ilgili sýnýflarý ekle
//TODO sadece kendi eklediðin sorunlarý göster

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void openLoginActivity(View v) {
	    if(isOnline()==true) {
	    	Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
	    	MainActivity.this.startActivity(myIntent);
	    }
	    else {
	    	Toast.makeText(getApplicationContext(), getString(R.string.internet_connection_required), Toast.LENGTH_SHORT).show();
	    }
	}
	
	public void openRegisterPage(View v) {
		Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
		startActivity(myIntent);
	}
	
	public void openAllProblems(View v) {
		Intent myIntent = new Intent(MainActivity.this, ProblemsActivity.class);
		myIntent.putExtra("showButons", false);
    	MainActivity.this.startActivity(myIntent);
	}
}
