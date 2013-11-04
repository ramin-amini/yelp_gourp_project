package com.codepath.apps.yelpclient.fragments;

import com.codepath.apps.yelpclient.controllers.FavoriteImagesListManager;
import com.codepath.apps.yelpclient.controllers.IImagesListManager;

import android.os.Bundle;

public class FavoriteImagesListFragment extends ImagesListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
	}

	@Override
	public IImagesListManager getImagesListManager() {
		return new FavoriteImagesListManager(getAdapter());
	}
}
