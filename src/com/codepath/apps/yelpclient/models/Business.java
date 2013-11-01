package com.codepath.apps.yelpclient.models;

import java.io.Serializable;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Business implements Serializable{
	private static final long serialVersionUID = 1371990832013475977L;
	private String name;
	private String phone;
	private String imageUrl;
	private String city;
	private String address;
	private String rating;
	
	public String getCity() {
		return this.city;
	}
	public String getAddress() {
		return this.address;
	}
	public String getRating() {
		return this.rating;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	public String getImageUrl() {
		return this.imageUrl;
	}
	
	
	// Decodes business json into business model object
	public static Business fromJson(JSONObject jsonObject) {
		Business b = new Business();
        // Deserialize json into object fields
		try {
        	b.name = jsonObject.has("name") ? jsonObject.getString("name") : "";
        	b.phone = jsonObject.has("display_phone") ? 
        			jsonObject.getString("display_phone").replaceAll("^\\+[^-]*-", "") : "";
        	b.imageUrl = jsonObject.has("image_url") ? jsonObject.getString("image_url") : "";
			String display_address = "";
    		if(jsonObject.has("location")){
    			JSONObject location = jsonObject.getJSONObject("location");
    			JSONArray address = location.getJSONArray("display_address");
    			for(int i=0; i<address.length(); i++){
    				display_address += address.getString(i) + "\n";
    			}
    	    	b.address = display_address;
    		} 
			b.rating = jsonObject.has("rating") ? jsonObject.getString("rating") : "";			
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
		// Return new object
		return b;
	}
	
 	
	// Decodes hashmap of business json results into business model objects
    public static HashMap<String,Business> fromJson(JSONArray jsonArray) {
    	HashMap<String, Business> businesses = new HashMap<String, Business>(jsonArray.length());
        String businessId = ""; 

        // Process each result in json hashmap, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject businessJson = null;
            try {
            	businessJson = jsonArray.getJSONObject(i);
                businessId =  businessJson.get("id").toString();

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Business business = Business.fromJson(businessJson);
            if (business != null) {
            	businesses.put(businessId, business);
            }
        }

        return businesses;
    }
    
    @Override
    public String toString() {
    	return  name + "\n" + rating + "\n" + address + "\n" + phone;
    }
}
