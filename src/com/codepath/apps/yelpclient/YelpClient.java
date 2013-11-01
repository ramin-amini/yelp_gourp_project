package com.codepath.apps.yelpclient;

import org.scribe.builder.api.Api;
import org.scribe.model.Token;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 *
 * This is the object responsible for communicating with a REST API.
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes:
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 *
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 *
 */
public class YelpClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = YelpApi2.class;  
    public static final String REST_URL = "http://api.yelp.com/v2";  
    public static final String REST_CONSUMER_KEY = "mHgbr0emfoaHxq589hQD2g";
    public static final String REST_CONSUMER_SECRET = "EmAKdvX0s-s8AMbgKG7uwgbwsqg";
    public static final String TOKEN = "mqVeYwgUdcJdkC0p8VD9BX9o0pypp6uT";
    public static final String TOKEN_SECRET = "7F1pT7mm2G2p7rGEsto312LU97w";
    public static final String REST_CALLBACK_URL = "oauth://cpyelp";  

    public YelpClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
        this.client.setAccessToken(new Token(TOKEN, TOKEN_SECRET));

    }
    
    public void search(String term, String location,  boolean latlong, String category, String limit, String offset, AsyncHttpResponseHandler handler) {
    	// http://api.yelp.com/v2/search?term=food&location=San+Francisco    	 
    	String apiUrl = getApiUrl("search");
        RequestParams params = new RequestParams();
        params.put("term", term);
        if(latlong)
            params.put("ll", location); 
        else
        	params.put("location", location); 
        params.put("category_filter", category);
        params.put("limit", limit);
        params.put("offset", offset);
        client.get(apiUrl, params, handler);        
    }


 
}