package org.barisakkurt.istanbulandroid;

import org.barisakkurt.istanbulweb.utilty.Utility;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


//TODO resimleri haritada göster
//TODO cep telefonunda problems activity açýlmýyor.
//TODO farklý birkaç telefonda test et.
//TODO map fragmenti yükleyemiyor telefonda
//TODO resim çekince kameradan resim gelmiyor gibi biþe var.
//TODO yenile butonu tam çalýþmýyor.
//TODO yeni bir þey ekleyince liste güncellenmiyor.
//TODO kategori null geliyor.---
//TODO uygulama yavaþ çalýþýyor.

//TODO verilen oylarý göster
//TODO alýnan oya göre sýralama
//TODO son 1 ayda eklenen içeriðini gösterme.
//TODO dosyalarý servera kaydederken isimere kullanici adlarini da ekle.
//TODO user, citizenuser gibi kullanýcý ile ilgili sýnýflarý ekle
//TODO sadece kendi eklediðin sorunlarý göster
//TODO açýlýþta internete baðlý mý diyalogu açtýr.
//TODO google+ ile login yapma.
//TODO yardýmcý araçlar bölümü ->hangi sokaktayým vs. gibi.
//TODO resim kaydetmeye kullanici adýný da ekle.

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.d("DIZIN", "DIZIN:"+Utility.imageFolder);
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
		if(isOnline()) {
			Intent myIntent = new Intent(MainActivity.this, ProblemsActivity.class);
			myIntent.putExtra("showButons", false);
	    	MainActivity.this.startActivity(myIntent);
		}
		else {
			Toast.makeText(getApplicationContext(), getString(R.string.internet_connection_required), Toast.LENGTH_SHORT).show();
		}
	}
}
