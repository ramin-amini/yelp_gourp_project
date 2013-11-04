package com.codepath.apps.yelpclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.apps.yelpclient.models.Business;

public class MyFavesActivity extends Activity {

	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ArrayList<ImageResult> allBizPhotos = new ArrayList<ImageResult>();
	private static final String MY_FAVES_FILENAME = "yelp_my_faves.txt";


	HashMap<String,Business> businesses;
	ImageResultArrayAdapter imageAdapter;
	GridView gvFaves;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_faves);
		gvFaves = (GridView) findViewById(R.id.gvFaves);
		
		imageAdapter = new ImageResultArrayAdapter(this,imageResults);
		gvFaves.setAdapter(imageAdapter);
 
		getMyFavorites();
		
		gvFaves.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long rowId) {
				Intent i = new Intent(getApplicationContext(),ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				i.putExtra("businessInfo", businesses.get(imageResult.getBizId()));
				startActivity(i);	
			}	
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_faves, menu);
		return true;
	}
	
	public void getMyFavorites(){
		try {
			readItems();
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
 
	
	private void readItems() throws IOException, JSONException {
		File fileDir = getFilesDir();
		ArrayList<ImageResult> allBizPhotos = new ArrayList<ImageResult>(); 
	    BufferedReader br = new BufferedReader(new FileReader(fileDir.getPath() + "/" + MY_FAVES_FILENAME));
	  
	    try {
	        String line = br.readLine();
	        JSONArray jsonBizAll = new JSONArray();
	        JSONArray jsonPhotosAll = new JSONArray();

	        while (line != null) {
	            JSONArray jsonLine = new JSONArray(line.toString());
	            jsonBizAll.put(jsonLine.get(1));	 
	            jsonPhotosAll.put(jsonLine.get(0));
				line = br.readLine();
	        }
	        

			businesses = Business.fromJson(jsonBizAll);				
			ArrayList<ImageResult> allPhotos = ImageResult.fromJsonArray(jsonPhotosAll);
			allBizPhotos.addAll(allPhotos);	

			Toast.makeText(this, "Reading My Favorites from local storage",Toast.LENGTH_SHORT).show();	    
			imageAdapter.addAll(allBizPhotos);	
		

	    } finally {
	        br.close();
	    }
	}
	

}
