package com.codepath.apps.yelpclient.controllers;

import java.util.HashMap;

import android.support.v4.app.FragmentActivity;

import com.codepath.apps.yelpclient.models.Business;

public class FavoriteImagesListManager implements IImagesListManager {

	HashMap<String,Business> allBusinesses = new HashMap<String,Business>();; 	//we need all businesses for scrolling

	@Override
	public void getBusinesses(FragmentActivity activity, int totalItemsCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashMap<String, Business> getAllBusinesses() {
		return allBusinesses;
	}

}
