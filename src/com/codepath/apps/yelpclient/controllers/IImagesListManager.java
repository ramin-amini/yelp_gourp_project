package com.codepath.apps.yelpclient.controllers;

import java.util.HashMap;

import android.support.v4.app.FragmentActivity;

import com.codepath.apps.yelpclient.models.Business;

public interface IImagesListManager {

	public void getBusinesses(FragmentActivity activity, int totalItemsCount);

	public HashMap<String, Business> getAllBusinesses();

}
