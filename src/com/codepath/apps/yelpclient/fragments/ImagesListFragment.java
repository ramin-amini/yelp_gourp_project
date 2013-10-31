package com.codepath.apps.yelpclient.fragments;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.apps.yelpclient.ImageResult;
import com.codepath.apps.yelpclient.ImageResultArrayAdapter;
import com.codepath.apps.yelpclient.ImageViewActivity;
import com.codepath.apps.yelpclient.R;
import com.codepath.apps.yelpclient.SearchActivity;
import com.codepath.apps.yelpclient.YelpClient;
import com.codepath.apps.yelpclient.YelpClientApp;
import com.codepath.apps.yelpclient.controllers.DefaultImagesListManager;
import com.codepath.apps.yelpclient.listener.EndlessScrollListener;
import com.codepath.apps.yelpclient.models.Business;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ImagesListFragment extends Fragment {
	//final static String BUSINESS_PHOTOS_BASEURL = "http://www.yelp.com/biz_photos/";
	//final static String USER_PHOTOS_REGEX = "initial_photos\"\\s*:\\s*(\\[[^\\]]*\\])";

	private ImageResultArrayAdapter imageAdapter;
	//private HashMap<String,Business> allBusinesses; 	//we need all businesses for scrolling
	//private HashMap<String,Business> businesses;
	//int bizCount = 0;
	private ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	//private ArrayList<ImageResult> allBizPhotos = new ArrayList<ImageResult>();
	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState){
		return inf.inflate(R.layout.fragment_images_list, parent, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		GridView gvThumbs = (GridView) getActivity().findViewById(R.id.gvThumbs);
		//allBusinesses = new HashMap<String,Business>();

		imageAdapter = new ImageResultArrayAdapter(getActivity(), imageResults);
		gvThumbs.setAdapter(imageAdapter);
 
		
		gvThumbs.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) { 
		        DefaultImagesListManager.getBusinesses(getActivity(), totalItemsCount, getAdapter()); 
		    }
        });
		DefaultImagesListManager.getBusinesses(getActivity(), 0, getAdapter());
		                      	
		gvThumbs.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long rowId) {
				Intent i = new Intent(getActivity(),ImageViewActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				i.putExtra("businessInfo", DefaultImagesListManager.allBusinesses.get(imageResult.getBizId()));
				startActivity(i);	
			}	
		});
	}
	
	public ImageResultArrayAdapter getAdapter(){
		return imageAdapter;
	}
	
	 // Append more data into the adapter
    //public void customLoadMoreDataFromApi(int offset) {
   /* public void getBusinesses(int offset) {
    	
    	allBizPhotos.clear();
    	Log.d("test", Integer.toString(offset));
		String location  = getActivity().getIntent().getStringExtra("location");
		String category  = getActivity().getIntent().getStringExtra("category");	

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
				Toast.makeText(getActivity(), "FAIL", Toast.LENGTH_LONG).show();
			}
		});
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
    

	
	 helper method to parse html content and return initial photos using regex 
	 	
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
	}*/
}
