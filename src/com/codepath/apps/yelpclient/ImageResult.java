package com.codepath.apps.yelpclient;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable{
	private static final long serialVersionUID = -1864781680065683848L;
	private String fullUrl;
	private String thumbUrl;
	private String caption;
	private String bizId;


	
	public ImageResult(JSONObject json){
		try{
			this.fullUrl = json.has("biz_photos_url")  ? json.getString("biz_photos_url") : "";
			this.thumbUrl = json.has("src") ? json.getString("src") : "";
			this.caption = json.has("caption") ? json.getString("caption") : "";
			this.bizId = json.has("id") ? json.getString("id") : "";

		} catch(JSONException e){
			this.fullUrl = null;
			this.thumbUrl = null;
			this.caption = null;
			this.bizId = null;
		}
	}
	public String getFullUrl(){
		return fullUrl;
	}
	public String getCaption(){
		return caption;
	}
	public String getBizId(){
		return bizId;
	}
	
	public String getThumbUrl(){
		return "http:" + thumbUrl;
	}

	public String toString(){
		return this.thumbUrl;
	}
	
	public static ArrayList<ImageResult> fromJsonArray(
			JSONArray array) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int x = 0; x< array.length(); x++){
			try{
				results.add(new ImageResult(array.getJSONObject(x)));
			} catch (JSONException e){
				e.printStackTrace();
			}
		}
		
		return results;
	}
	
	

}
