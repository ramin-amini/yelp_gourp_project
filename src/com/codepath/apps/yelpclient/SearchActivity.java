package com.codepath.apps.yelpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.apps.yelpclient.listener.EndlessScrollListener;
import com.codepath.apps.yelpclient.models.Business;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ArrayList<ImageResult> allBizPhotos = new ArrayList<ImageResult>();

	ImageResultArrayAdapter imageAdapter;
	GridView gvThumbs;
	boolean display = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		gvThumbs = (GridView) findViewById(R.id.gvThumbs);
		
		imageAdapter = new ImageResultArrayAdapter(this,imageResults);
		gvThumbs.setAdapter(imageAdapter);
		String location  = getIntent().getStringExtra("location");
		String category  = getIntent().getStringExtra("category");

		gvThumbs.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
	                // Triggered only when new data needs to be appended to the list
	                // Add whatever code is needed to append new items to your AdapterView
		        customLoadMoreDataFromApi(page); 
	                // or customLoadMoreDataFromApi(totalItemsCount); 
		    }
	        });
		
		YelpClient yelpClient = YelpClientApp.getRestClient();
		yelpClient.search("food", location, category, Integer.toString(6), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int code, JSONObject body) {
				try {
					JSONArray businessesJson = body.getJSONArray("businesses");
					ArrayList<Business> businesses = Business.fromJson(businessesJson);
					// for testing purposes I'm limiting the size to 6
					// for actual implementation use businesses.size();?
					// or any count that is needed for scrolling
					//int loopCount = 6 ; 
					//if(businesses.size() < 6) 
					//	loopCount = businesses.size();
					for (int b=0; b<businesses.size(); b++){ 
						if(b == businesses.size()-1) display = true;
						findBizPhotos(businesses.get(b).getId());
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	public void findBizPhotos(final String bizId){
		allBizPhotos.clear();

		AsyncHttpClient httpClient = new AsyncHttpClient();
 		httpClient.get("http://www.yelp.com/biz_photos/" + bizId, new AsyncHttpResponseHandler(){
  			@Override
			public void onSuccess(String response){
				try {
					ArrayList<ImageResult> allPhotosOneBiz = ImageResult.fromJsonArray(getImages(response));
					int oneBizPhotosSize = allPhotosOneBiz.size();
					//if no photos ignore, otherwise get only 3 photos for each business
					if(oneBizPhotosSize > 0 ) {
						if (oneBizPhotosSize > 3) 
							oneBizPhotosSize = 3;
						
						for (int i=0; i<oneBizPhotosSize; i++){
							allBizPhotos.add(allPhotosOneBiz.get(i));
						}	
						Log.d("DEBUG", "biz id  = " +bizId);
						//Log.d("DEBUG", allPhotosOneBiz.toString());
						//Log.d("DEBUG", allBizPhotos.toString());

						imageAdapter.clear();
						//Display the array when we have gone through the whole "for" loop
						//This way we can shuffle the photos 
						//otherwise we end up displaying 3 photos from the same business on each row
						if(display){
							long seed = System.nanoTime();
							Collections.shuffle(allBizPhotos, new Random(seed));
							imageAdapter.addAll(allBizPhotos);		
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 			
 			}
 		});
			
	}
	

	///helper method to parse html content and return initial photos using regex 
	
	private JSONArray getImages(String respn) throws JSONException{
		String IMAGE_PATTERN ="initial_photos\"\\s*:\\s*(\\[[^\\]]*\\])";
		Pattern pattern = Pattern.compile(IMAGE_PATTERN);
		Matcher matcher = pattern.matcher(respn);
        if(matcher.find()){
    	   return new JSONArray(matcher.group(1));
        }
       return null;
	}
	
	 // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
      // This method probably sends out a network request and appends new data items to your adapter. 
      // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
      // Deserialize API response and then construct new objects to append to the adapter
    	
    	String location  = getIntent().getStringExtra("location");
		String category  = getIntent().getStringExtra("category");
		YelpClient yelpClient = YelpClientApp.getRestClient();
		yelpClient.search("food", location, category, Integer.toString(6),Integer.toString(offset), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int code, JSONObject body) {
				try {
					JSONArray businessesJson = body.getJSONArray("businesses");
					ArrayList<Business> businesses = Business.fromJson(businessesJson);
					// for testing purposes I'm limiting the size to 6
					// for actual implementation use businesses.size();?
					// or any count that is needed for scrolling
					//int loopCount = 6 ; 
					//if(businesses.size() < 6) 
					//	loopCount = businesses.size();
					for (int b=0; b<businesses.size(); b++){ 
						if(b == businesses.size()-1) display = true;
						loadMoreBizPhotos(businesses.get(b).getId());
					}		
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});	
    }
    
    public void loadMoreBizPhotos(final String bizId){
		//allBizPhotos.clear();

		AsyncHttpClient httpClient = new AsyncHttpClient();
 		httpClient.get("http://www.yelp.com/biz_photos/" + bizId, new AsyncHttpResponseHandler(){
  			@Override
			public void onSuccess(String response){
				try {
					ArrayList<ImageResult> allPhotosOneBiz = ImageResult.fromJsonArray(getImages(response));
					int oneBizPhotosSize = allPhotosOneBiz.size();
					//if no photos ignore, otherwise get only 3 photos for each business
					if(oneBizPhotosSize > 0 ) {
						if (oneBizPhotosSize > 3) 
							oneBizPhotosSize = 3;
						
						for (int i=0; i<oneBizPhotosSize; i++){
							allBizPhotos.add(allPhotosOneBiz.get(i));
						}	
						Log.d("DEBUG", "biz id  = " +bizId);
						//Log.d("DEBUG", allPhotosOneBiz.toString());
						//Log.d("DEBUG", allBizPhotos.toString());

						//imageAdapter.clear();
						//Display the array when we have gone through the whole "for" loop
						//This way we can shuffle the photos 
						//otherwise we end up displaying 3 photos from the same business on each row
						if(display){
							long seed = System.nanoTime();
							Collections.shuffle(allBizPhotos, new Random(seed));
							imageAdapter.addAll(allBizPhotos);		
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 			
 			}
 		});
			
	}

}
