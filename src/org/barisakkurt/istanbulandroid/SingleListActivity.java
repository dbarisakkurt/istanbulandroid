package org.barisakkurt.istanbulandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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
         
        Intent i = getIntent();
        String description= i.getStringExtra("description");
        String category= i.getStringExtra("category");
        String reportDate= i.getStringExtra("reportDate");
        String latitude= i.getStringExtra("latitude");
        String longitude=i.getStringExtra("longitude");
        String imagePath=i.getStringExtra("imagePath");

        txtCategory.setText(category);
        txtDescription.setText(description);
        txtLatitude.setText(latitude);
        txtLongitude.setText(longitude);
        txtReportDate.setText(reportDate);
        
         
    }
}
