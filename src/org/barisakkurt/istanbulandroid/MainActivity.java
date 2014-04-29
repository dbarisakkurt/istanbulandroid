package org.barisakkurt.istanbulandroid;

import org.barisakkurt.istanbulweb.utilty.Utility;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


//TODO resimleri haritada g�ster
//TODO cep telefonunda problems activity a��lm�yor.
//TODO farkl� birka� telefonda test et.
//TODO map fragmenti y�kleyemiyor telefonda
//TODO resim �ekince kameradan resim gelmiyor gibi bi�e var.
//TODO yenile butonu tam �al��m�yor.
//TODO yeni bir �ey ekleyince liste g�ncellenmiyor.
//TODO kategori null geliyor.---
//TODO uygulama yava� �al���yor.

//TODO verilen oylar� g�ster
//TODO al�nan oya g�re s�ralama
//TODO son 1 ayda eklenen i�eri�ini g�sterme.
//TODO dosyalar� servera kaydederken isimere kullanici adlarini da ekle.
//TODO user, citizenuser gibi kullan�c� ile ilgili s�n�flar� ekle
//TODO sadece kendi ekledi�in sorunlar� g�ster
//TODO a��l��ta internete ba�l� m� diyalogu a�t�r.
//TODO google+ ile login yapma.
//TODO yard�mc� ara�lar b�l�m� ->hangi sokaktay�m vs. gibi.
//TODO resim kaydetmeye kullanici ad�n� da ekle.

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
