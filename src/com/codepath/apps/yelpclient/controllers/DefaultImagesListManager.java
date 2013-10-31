package com.codepath.apps.yelpclient.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.yelpclient.ImageResult;
import com.codepath.apps.yelpclient.ImageResultArrayAdapter;
import com.codepath.apps.yelpclient.YelpClient;
import com.codepath.apps.yelpclient.YelpClientApp;
import com.codepath.apps.yelpclient.models.Business;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class DefaultImagesListManager {
	private static final String TAG = "DefaultImagesListManager";
	
	final static String BUSINESS_PHOTOS_BASEURL = "http://www.yelp.com/biz_photos/";
	final static String USER_PHOTOS_REGEX = "initial_photos\"\\s*:\\s*(\\[[^\\]]*\\])";

	public static HashMap<String,Business> allBusinesses = new HashMap<String,Business>();; 	//we need all businesses for scrolling
	private static HashMap<String,Business> businesses;
	private static int bizCount = 0;
	private static ArrayList<ImageResult> allBizPhotos = new ArrayList<ImageResult>();
	
	

	 // Append more data into the adapter
    //public void customLoadMoreDataFromApi(int offset) {
    public static void getBusinesses(final FragmentActivity pActivity, int offset, final ImageResultArrayAdapter imageAdapter) {
    	
    	allBizPhotos.clear();
    	Log.d("test", Integer.toString(offset));
		String location  = pActivity.getIntent().getStringExtra("location");
		String category  = pActivity.getIntent().getStringExtra("category");	

		YelpClient yelpClient = YelpClientApp.getRestClient();
		yelpClient.search("food", location, category, Integer.toString(6),Integer.toString(offset), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int code, JSONObject body) {
				try {
					JSONArray businessesJson = body.getJSONArray("businesses");
					businesses = Business.fromJson(businessesJson);		
					allBusinesses.putAll(businesses);
					bizCount = businesses.size();     
					for (String key : businesses.keySet()) {
						findBizPhotos(key, imageAdapter);
					}	
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}			
			@Override
			public void onFailure(Throwable t) {
				Toast.makeText(pActivity, "FAIL", Toast.LENGTH_LONG).show();
			}
		});
    }
    
    private static void findBizPhotos(final String bizId, final ImageResultArrayAdapter imageAdapter){
		AsyncHttpClient httpClient = new AsyncHttpClient();
 		httpClient.get(BUSINESS_PHOTOS_BASEURL + bizId, new AsyncHttpResponseHandler(){
  			@Override
			public void onSuccess(String response){
				try {
					bizCount --;
					if(response != null){
						JSONArray bizPhotos = getImages(response, bizId);
						if(bizPhotos != null){
							JSONArray bizMaxThree = new JSONArray();
							int loop = bizPhotos.length();
							if(loop> 0){
								if(loop > 3)  loop = 3;//get 3 photos only
								for (int l=0; l<loop; l++){
									JSONObject photo = (JSONObject) bizPhotos.get(l);
									photo.put("id", bizId);
									bizMaxThree.put(photo);
								}
								ArrayList<ImageResult> oneBizPhotos = ImageResult.fromJsonArray(bizMaxThree);
								allBizPhotos.addAll(oneBizPhotos);	
							}
						}					
					}						 							
					if(bizCount == 0){
						long seed = System.nanoTime();
						Collections.shuffle(allBizPhotos, new Random(seed));
						imageAdapter.addAll(allBizPhotos);						
					}	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 			
 			}
 		});
			
	}
    

	/*
	 * helper method to parse html content and return initial photos using regex 
	 */	
	private static JSONArray getImages(String respn, String bizId) {
		Pattern pattern = Pattern.compile(USER_PHOTOS_REGEX);
		Matcher matcher = pattern.matcher(respn);
        if(matcher.find()){
    	   try {
    		   return new JSONArray(matcher.group(1));
			} catch (JSONException e) {
				Log.d("DEBUG", "Cound not retrieve photos from " + BUSINESS_PHOTOS_BASEURL + bizId);
			}
        }
        return null;
	}


}
