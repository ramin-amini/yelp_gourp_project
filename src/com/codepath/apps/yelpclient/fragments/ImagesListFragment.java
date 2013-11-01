package com.codepath.apps.yelpclient.fragments;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.codepath.apps.yelpclient.ImageResult;
import com.codepath.apps.yelpclient.ImageResultArrayAdapter;
import com.codepath.apps.yelpclient.ImageViewActivity;
import com.codepath.apps.yelpclient.R;
import com.codepath.apps.yelpclient.controllers.IImagesListManager;
import com.codepath.apps.yelpclient.listener.EndlessScrollListener;
import com.codepath.apps.yelpclient.models.Business;

public abstract class ImagesListFragment extends Fragment {

	private ImageResultArrayAdapter imageAdapter;
	private HashMap<String,Business> allBusinesses; 	//we need all businesses for scrolling
	private ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	private IImagesListManager manager;
	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState){
		return inf.inflate(R.layout.fragment_images_list, parent, false);
	}
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		GridView gvThumbs = (GridView) getActivity().findViewById(R.id.gvThumbs);
		imageAdapter = new ImageResultArrayAdapter(getActivity(), imageResults);
		gvThumbs.setAdapter(imageAdapter);
		manager = getImagesListManager();
		allBusinesses = manager.getAllBusinesses();
		gvThumbs.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) { 
		    	manager.getBusinesses(getActivity(), totalItemsCount); 
		    }
        });
		manager.getBusinesses(getActivity(), 0);
		                      	
		gvThumbs.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long rowId) {
				Intent i = new Intent(getActivity(),ImageViewActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				i.putExtra("businessInfo", allBusinesses.get(imageResult.getBizId()));
				startActivity(i);	
			}	
		});
	}
	
	public ImageResultArrayAdapter getAdapter(){
		return imageAdapter;
	}
	
	public ArrayList<ImageResult> getImageResults(){
		return imageResults;
	}
	
	public abstract IImagesListManager getImagesListManager();

}
