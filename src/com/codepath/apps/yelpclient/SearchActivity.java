package com.codepath.apps.yelpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.apps.yelpclient.listener.EndlessScrollListener;
import com.codepath.apps.yelpclient.models.Business;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	final static String BUSINESS_PHOTOS_BASEURL = "http://www.yelp.com/biz_photos/";
	final static String USER_PHOTOS_REGEX = "initial_photos\"\\s*:\\s*(\\[[^\\]]*\\])";;

	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ArrayList<ImageResult> allBizPhotos = new ArrayList<ImageResult>();

	HashMap<String,Business> businesses;
	//we need all businesses for scrolling
	HashMap<String,Business> allBusinesses;

	
	ImageResultArrayAdapter imageAdapter;
	GridView gvThumbs;
	int bizCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		gvThumbs = (GridView) findViewById(R.id.gvThumbs);
		allBusinesses = new HashMap<String,Business>();
		
		imageAdapter = new ImageResultArrayAdapter(this,imageResults);
		gvThumbs.setAdapter(imageAdapter);
 
		
		gvThumbs.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) { 
		        getBusinesses(totalItemsCount); 
		    }
        });
		getBusinesses(0);
		
		gvThumbs.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long rowId) {
				Intent i = new Intent(getApplicationContext(),ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				i.putExtra("businessInfo", allBusinesses.get(imageResult.getBizId()));
				startActivity(i);	
			}	
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public void findBizPhotos(final String bizId){
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
	 // Append more data into the adapter
    //public void customLoadMoreDataFromApi(int offset) {
    public void getBusinesses(int offset) {
    	
    	allBizPhotos.clear();
    	Log.d("test", Integer.toString(offset));
		String location  = getIntent().getStringExtra("location");
		String category  = getIntent().getStringExtra("category");	

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
						findBizPhotos(key);
					}	
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}			
			@Override
			public void onFailure(Throwable t) {
				Toast.makeText(SearchActivity.this, "FAIL", Toast.LENGTH_LONG).show();
			}
		});
    }
 

	///helper method to parse html content and return initial photos using regex 
	
	private JSONArray getImages(String respn, String bizId) {
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
