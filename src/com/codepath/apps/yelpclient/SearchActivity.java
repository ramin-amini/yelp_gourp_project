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

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.apps.yelpclient.fragments.FavoriteImagesListFragment;
import com.codepath.apps.yelpclient.fragments.SearchImagesListFragment;
import com.codepath.apps.yelpclient.listener.EndlessScrollListener;
import com.codepath.apps.yelpclient.models.Business;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends FragmentActivity implements TabListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		String tab = getIntent().getStringExtra("tab");
		setupNavigationTabs(tab);
	}

	private void setupNavigationTabs(String tab) {
		ActionBar actionBar = getActionBar();
    	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	actionBar.setDisplayShowTitleEnabled(true);
    	Tab tabSearch = actionBar.newTab().setText("Search")
    			.setTag("SearchImagesListFragment").setIcon(R.drawable.ic_home)
    			.setTabListener(this);
    	
    	Tab tabFavorite = actionBar.newTab().setText("Favorite")
    			.setTag("FavoriteImagesListFragment").setIcon(R.drawable.ic_favorite)
    			.setTabListener(this);
    	
    	actionBar.addTab(tabSearch);
    	actionBar.addTab(tabFavorite);
    	if(tab.equalsIgnoreCase("favorites")){
    		actionBar.selectTab(tabFavorite);
    	}else{
        	actionBar.selectTab(tabSearch);
    	}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager() ;
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		if(tab.getTag()=="SearchImagesListFragment"){
			fts.replace(R.id.frame_container, new SearchImagesListFragment());
		}
		else{
			fts.replace(R.id.frame_container, new FavoriteImagesListFragment());
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	


 

	
	

}
