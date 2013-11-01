package com.codepath.apps.yelpclient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.apps.yelpclient.MyLocation.LocationResult;

public class HomePageActivity extends Activity {
	
	EditText etQuery;
	Button btnSearch;
	Spinner spnrCategory;
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
		boolean flag = displayGpsStatus();
		
	
		
		if (query == null || query.isEmpty()) {
		    
		    LocationResult locationResult = new LocationResult(){
		        @Override
		        public void gotLocation(Location location) {
		           //Log.d("prajna", location.get);
		            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy); 

		            
                    String cityName = null;
                  
                    List<Address> addresses;
                    try {
                        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.getLatitude() + ","
                                + location.getLongitude() + "&sensor=true";
                        
                        JSONObject jsonObj = parser_Json.getJSONfromURL(url);
                        
                        String Status = jsonObj.getString("status");
                        
                        if (Status.equalsIgnoreCase("OK")) {
                            JSONArray Results = jsonObj.getJSONArray("results");
                            JSONObject zero = Results.getJSONObject(0);
                            JSONArray address_components = zero.getJSONArray("address_components");

                            for (int i = 0; i < address_components.length(); i++) {
                                JSONObject zero2 = address_components.getJSONObject(i);
                                String long_name = zero2.getString("long_name");
                                JSONArray mtypes = zero2.getJSONArray("types");
                                String Type = mtypes.getString(0);

                                if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                                    if (Type.equalsIgnoreCase("locality")) {
                                        String City = long_name;
                                        cityName = City;
                                    } 
                                }

                            }
                        }
                     } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String query1 = cityName;
                    
                    goToRestaurant(query1);
		        } 
		    };
		
		    MyLocation myLocation = new MyLocation();
		    myLocation.getLocation(this, locationResult);
		} else {
		    goToRestaurant(query);
		}
		
	}

	public void goToRestaurant(String query) {
        // TODO Auto-generated method stub
	    Toast.makeText(this, "Searching for yelp photos in " + query,
                Toast.LENGTH_SHORT).show();

        Intent j = new Intent(getApplicationContext(), SearchActivity.class);
        j.putExtra("location", query);
        if (!getCategory().equalsIgnoreCase("food")) {
            j.putExtra("category", getCategory());      
        }
        startActivity(j);

        
    }
	public String getCategory(){
		String category = spnrCategory.getSelectedItem().toString();
		return category;
	}
	 
}
