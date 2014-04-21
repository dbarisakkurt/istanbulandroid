package org.barisakkurt.istanbulandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


//TODO d�zelttiklerim
//TODO MainActivity, LoginActivity, RegisterActivity, AboutActivity, ContactActivity, BaseActivty
//TODO SetingsActivity d�zelmez.
//TODO resimleri haritada g�ster
//TODO cep telefonunda problems activity a��lm�yor.
//TODO farkl� birka� telefonda test et.
//TODO register a isonline mi kontrol� ekle.
//TODO sorun detail activity sadece portait olsun ve scrollview ekle, resmi ortala.
//TODO map fragmenti y�kleyemiyor.
//TODO resim �ekince kameradan resim gelmiyor gibi bi�e var.
//TODO sorun istanbul ba�l���n� ortala
//TODO show all problemse is online mi kontrol� ekle

//TODO verilen oylar� g�ster
//TODO al�nan oya g�re s�ralama
//TODO son 1 ayda eklenen i�eri�ini g�sterme.
//TODO dosyalar� servera kaydederken isimere kullanici adlarini da ekle.
//TODO user, citizenuser gibi kullan�c� ile ilgili s�n�flar� ekle
//TODO sadece kendi ekledi�in sorunlar� g�ster

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
