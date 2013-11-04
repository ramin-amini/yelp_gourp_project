package com.codepath.apps.yelpclient.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.codepath.apps.yelpclient.ImageResult;
import com.codepath.apps.yelpclient.ImageResultArrayAdapter;
import com.codepath.apps.yelpclient.YelpConfig;
import com.codepath.apps.yelpclient.models.Business;

public class FavoriteImagesListManager implements IImagesListManager {

	HashMap<String,Business> allBusinesses = new HashMap<String,Business>();; 	//we need all businesses for scrolling
	ArrayList<ImageResult> allBizPhotos = new ArrayList<ImageResult>();
	private  HashMap<String,Business> businesses;

	private  ImageResultArrayAdapter imageAdapter;
	int count = 0;
	
	public FavoriteImagesListManager(ImageResultArrayAdapter pImageAdapter ){
		imageAdapter = pImageAdapter;
	}
	
	@Override
	public void getBusinesses(FragmentActivity activity, int totalItemsCount) {
		allBizPhotos.clear();
		if(count == 0){
			getMyFavorites(activity);
			count++;	
		}
	
	}

	@Override
	public HashMap<String, Business> getAllBusinesses() {
		return allBusinesses;
	}

	
	private void getMyFavorites(final FragmentActivity pActivity){
		 readItems(pActivity);
	}

	private void readItems(final FragmentActivity pActivity) {
		File fileDir = pActivity.getFilesDir();
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileDir.getPath() + "/"
					+ YelpConfig.MY_FAVES_FILENAME));

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
			ArrayList<ImageResult> allPhotos = ImageResult
					.fromJsonArray(jsonPhotosAll);
			allBizPhotos.addAll(allPhotos);
			allBusinesses.putAll(businesses);
			
			Toast.makeText(pActivity,
					"Reading My Favorites from local storage",
					Toast.LENGTH_SHORT).show();
			imageAdapter.addAll(allBizPhotos);

			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
