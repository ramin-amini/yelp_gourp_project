package com.codepath.apps.yelpclient;

 

 
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;

import com.codepath.apps.yelpclient.fragments.FavoriteImagesListFragment;
import com.codepath.apps.yelpclient.fragments.SearchImagesListFragment;


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
