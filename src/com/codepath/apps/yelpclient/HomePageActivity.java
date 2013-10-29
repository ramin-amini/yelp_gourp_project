package com.codepath.apps.yelpclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class HomePageActivity extends Activity {
	
	EditText etQuery;
	Button btnSearch;
	Spinner spnrCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		setViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}
	
	
	public void setViews(){
		etQuery = (EditText) findViewById(R.id.etQuery);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		spnrCategory = (Spinner) findViewById(R.id.spnrCategory);
	}
	
	public void yelpImageSearch(View v) {
		String query = etQuery.getText().toString();

		Toast.makeText(this, "Searching for yelp photos in " + query,
				Toast.LENGTH_SHORT).show();

		Intent j = new Intent(getApplicationContext(), SearchActivity.class);
		j.putExtra("location", query);
		if (!getCategory().equalsIgnoreCase("Show all results")) {
			j.putExtra("category", getCategory());		
		}
		startActivity(j);
	}

	public String getCategory(){
		String category = spnrCategory.getSelectedItem().toString();
		return category;
	}

}
