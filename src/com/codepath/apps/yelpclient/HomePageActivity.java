package com.codepath.apps.yelpclient;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class HomePageActivity extends Activity {
	
	EditText etQuery;
	Button btnSearch;
	Spinner spinner1;
	String category = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		setViews();
		setSpinner();

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
	}
	
	public void yelpImageSearch(View v){		
		String query = etQuery.getText().toString();

		Toast.makeText(this, "Searching for yelp photos in " + query , Toast.LENGTH_SHORT).show();

		Intent j = new Intent(getApplicationContext(),SearchActivity.class);
		j.putExtra("location", query );
		j.putExtra("category", category );
		startActivity(j);			
		}

	public void setSpinner(){
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.spinner1, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(adapter.getPosition("Show all results"));
		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
			@SuppressLint("DefaultLocale")
			public void onItemSelected(AdapterView<?> parent,
	            View view, int pos, long id) {
	            //Get item from spinner and store in string category........
	        	category = parent.getItemAtPosition(pos).toString().toLowerCase();	        	
	            if (category.equalsIgnoreCase("Indian")){
	            	category = "indpak"; 
	            }	
	            else if (category.equalsIgnoreCase("Middle Eastern")){
	            	category = "mideastern"; 
	            }
	            else if (category.equalsIgnoreCase("Show all results")){
	            	category = ""; 
	            } 
			}

	        public void onNothingSelected(AdapterView<?> parent) {
	          // Do nothing.
	        }
	        
	    });	
	}

}
