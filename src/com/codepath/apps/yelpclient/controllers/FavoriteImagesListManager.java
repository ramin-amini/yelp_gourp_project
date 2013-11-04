package com.codepath.apps.yelpclient.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.yelpclient.ImageResult;
import com.codepath.apps.yelpclient.ImageResultArrayAdapter;
import com.codepath.apps.yelpclient.R;
import com.codepath.apps.yelpclient.models.Business;

public class FavoriteImagesListManager implements IImagesListManager {

	HashMap<String,Business> allBusinesses = new HashMap<String,Business>();; 	//we need all businesses for scrolling
	private  ArrayList<ImageResult> allFavePhotos;// = new ArrayList<ImageResult>();
	private static final String MY_FAVES_FILENAME = "yelp_my_faves_master.txt";

	
	private  ImageResultArrayAdapter imageAdapter;
	
	public FavoriteImagesListManager( ImageResultArrayAdapter pImageAdapter){
		imageAdapter = pImageAdapter;
	}
	
	
	@Override
	public void getBusinesses(FragmentActivity activity, int totalItemsCount) {
		try {
			readItems();
			Log.d("DEBUG", "allBusinesses after read items" + allBusinesses);
		} catch (Exception e) {
			e.printStackTrace();
		} 		
	}

	@Override
	public HashMap<String, Business> getAllBusinesses() {
		return allBusinesses;
	}
	
	private void readItems() throws IOException, JSONException {
		imageAdapter.clear();
		//File fileDir = getFilesDir();
		ArrayList<ImageResult> allFavePhotos = new ArrayList<ImageResult>(); 
	    BufferedReader br = new BufferedReader(new FileReader("/data/data/com.codepath.apps.yelpclient/files/" + MY_FAVES_FILENAME));
	  
	    try {
	        String line = br.readLine();
	        JSONArray faveBizAll = new JSONArray();
	        JSONArray favePhotosAll = new JSONArray();

	        while (line != null) {
	            JSONArray jsonLine = new JSONArray(line.toString());
	            faveBizAll.put(jsonLine.get(1));	 
	            favePhotosAll.put(jsonLine.get(0));
				line = br.readLine();
	        }
	        

	        allBusinesses = Business.fromJson(faveBizAll);				
			ArrayList<ImageResult> allPhotos = ImageResult.fromJsonArray(favePhotosAll);
			allFavePhotos.addAll(allPhotos);	

			//Toast.makeText(this, "Reading My Favorites from local storage",Toast.LENGTH_SHORT).show();	    
			imageAdapter.addAll(allFavePhotos);	
		

	    } finally {
	        br.close();
	    }
	}

}
