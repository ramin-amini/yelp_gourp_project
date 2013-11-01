package com.codepath.apps.yelpclient.fragments;

import android.os.Bundle;

import com.codepath.apps.yelpclient.controllers.IImagesListManager;
import com.codepath.apps.yelpclient.controllers.SearchImagesListManager;

public class SearchImagesListFragment extends ImagesListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
	}
	
	@Override
	public IImagesListManager getImagesListManager(){
		return new SearchImagesListManager(getAdapter());
	}
}
