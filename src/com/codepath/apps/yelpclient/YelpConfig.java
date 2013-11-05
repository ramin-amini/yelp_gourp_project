package com.codepath.apps.yelpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.codepath.apps.yelpclient.models.Business;

public class YelpConfig {

	public static final String MY_FAVES_FILENAME = "yelp_my_faves.txt";
    public static HashMap<String,Business> favBusinesses = new HashMap<String,Business>();
    public static ArrayList<ImageResult> favBizPhotos = new ArrayList<ImageResult>();
    
    public static void removeDuplicate(ArrayList<ImageResult> list)   {
            HashMap<String, ImageResult> map = new HashMap<String, ImageResult>();
            Iterator<ImageResult> i = list.iterator();
            while(i.hasNext()){
                    ImageResult result = (ImageResult)i.next();
                    map.put(result.getThumbUrl(), result);
            }
            list.clear();
            Set<String> set = map.keySet();
            Iterator<String> keyIterator = set.iterator();
            while(keyIterator.hasNext()){
                    String key = (String)keyIterator.next();
                    list.add(map.get(key));
            }
    } 
}
