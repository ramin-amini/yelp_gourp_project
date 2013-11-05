package com.codepath.apps.yelpclient;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.codepath.apps.yelpclient.MyLocation.LocationResult;
import com.codepath.apps.yelpclient.MyLocation.LocationResult;

public class HomePageActivity extends FragmentActivity {


	EditText etQuery;
	Button btnSearch;
	Spinner spnrCategory;
	boolean ll;
	private LocationManager locationMangaer=null;  
	private LocationListener locationListener=null;

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
		locationMangaer = (LocationManager)   
		        getSystemService(Context.LOCATION_SERVICE);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_favorites:
			Intent f = new Intent(getApplicationContext(), SearchActivity.class);
		    f.putExtra("tab", "favorites");
		    startActivity(f); 
			return super.onOptionsItemSelected(item);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void setViews(){
		etQuery = (EditText) findViewById(R.id.etQuery);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		spnrCategory = (Spinner) findViewById(R.id.spnrCategory);
	}
	
	private Boolean displayGpsStatus() {  
	    ContentResolver contentResolver = getBaseContext()  
	    .getContentResolver();  
	    boolean gpsStatus = Settings.Secure  
	    .isLocationProviderEnabled(contentResolver,   
	    LocationManager.GPS_PROVIDER);  
	    if (gpsStatus) {  
	     return true;  
	    
	    } else {  
	     return false;  
	    }  
	   }  
	
	public void yelpImageSearch(View v) {
		String query = etQuery.getText().toString();
		ll= false;
		boolean flag = displayGpsStatus();
			
		if (query == null || query.isEmpty()) {
		    
		    LocationResult locationResult = new LocationResult(){
		        @Override
		        public void gotLocation(Location location) {
		           //Log.d("prajna", location.get);
		            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy); 
		            
                    String query1 = null;
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    if( Double.compare(longitude,Double.NaN)==0  && 
                    		Double.compare(latitude,Double.NaN)==0){
                    	query1 = "sunnyvale";
                   	
                    }
                    else{
                    	query1 = String.valueOf(latitude) + "," + String.valueOf(longitude); 
                    	ll =true;
                    }                    
                    goToRestaurant(query1);
		        } 
		    };
		
		    MyLocation myLocation = new MyLocation();
		    myLocation.getLocation(this, locationResult);
		} else {
		    goToRestaurant(query);
		}
		
	}

	public void goToRestaurant(final String query) {
		
	    HomePageActivity.this.runOnUiThread(new Runnable() {
	        public void run() {
	            Toast.makeText(HomePageActivity.this, "Searching for yelp photos in " + query,
	                                  Toast.LENGTH_SHORT).show();
	        }
	    });
        Intent j = new Intent(getApplicationContext(), SearchActivity.class);
        if(ll)
            j.putExtra("ll", query);
        else
        	j.putExtra("location", query);
        if (!getCategory().equalsIgnoreCase("food")) {
            j.putExtra("category", getCategory());      
        }
        j.putExtra("tab", "search");
        startActivity(j);
    }

	public String getCategory(){
		String category = spnrCategory.getSelectedItem().toString();
		return category;
	}

}
