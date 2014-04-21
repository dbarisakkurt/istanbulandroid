package org.barisakkurt.istanbulandroid;

import org.barisakkurt.istanbulweb.utilty.Utility;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

public class SingleListActivity extends BaseActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.single_list_item_view);
         
        TextView txtCategory = (TextView) findViewById(R.id.category);
        TextView txtDescription = (TextView) findViewById(R.id.description);
        TextView txtReportDate = (TextView) findViewById(R.id.reportDate);
        TextView txtLatitude = (TextView) findViewById(R.id.latitude);
        TextView txtLongitude = (TextView) findViewById(R.id.longitude);
        ImageView imgView = (ImageView) findViewById(R.id.imageViewReport);
         
        Intent i = getIntent();
        String description= i.getStringExtra("description");
        String category= i.getStringExtra("category");
        String reportDate= i.getStringExtra("reportDate");
        String latitude= i.getStringExtra("latitude");
        String longitude=i.getStringExtra("longitude");
        String imagePath=i.getStringExtra("imagePath");
        
        AQuery aq = new AQuery(this);
        
        Log.d("RESIM-singlelist",Utility.webSiteAddress + imagePath);
        
        
        aq.id(R.id.imageViewReport).image(Utility.webSiteAddress + imagePath, true, true, 0, 0, null, AQuery.FADE_IN_NETWORK, 1.0f);

        txtCategory.setText(category);
        txtDescription.setText(description);
        txtLatitude.setText(latitude);
        txtLongitude.setText(longitude);
        txtReportDate.setText(reportDate);
        
    }
}
