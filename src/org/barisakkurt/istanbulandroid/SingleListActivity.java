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
         
        Intent i = getIntent();
        String description= i.getStringExtra("description");
        String category= i.getStringExtra("category");
        String reportDate= i.getStringExtra("reportDate");

        txtCategory.setText(category);
        txtDescription.setText(description);
        txtReportDate.setText(reportDate);
         
    }
}
