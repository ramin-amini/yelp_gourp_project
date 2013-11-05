package com.codepath.apps.yelpclient;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.yelpclient.models.Business;
import com.loopj.android.image.SmartImageView;

public class ImageViewActivity extends Activity {
	TextView name; 
	TextView caption; 
	TextView address;
	//TextView phone;
	
	TextView phone;

	Button btnFavorites;
    Business biz;
    ImageResult result;
    private  HashMap<String,Business> businesses;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		
		result = (ImageResult) getIntent().getSerializableExtra("result");
		SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		String smImage = result.getThumbUrl();
		ivImage.setImageUrl(smImage.substring(0,smImage.lastIndexOf("/")+1) + "l.jpg");
		name = (TextView) findViewById(R.id.nameText);
		address = (TextView) findViewById(R.id.addressView);
		phone = (TextView) findViewById(R.id.phoneNumber);
		caption = (TextView) findViewById(R.id.captionView);
		String resultCaption = result.getCaption();
		if(resultCaption.equalsIgnoreCase("null"))
			caption.setText("");	//we can put some text here		
		else 
			caption.setText(resultCaption);
		Business info = (Business) getIntent().getSerializableExtra("businessInfo");
		name.setText(info.getName() + "\nRating: " + info.getRating());
		address.setText(info.getAddress());
	    phone.setText(info.getPhone());
	    
	    //check if we should enable and display the "Add" button
        btnFavorites = (Button)findViewById(R.id.btnAddFaves);
        readIntoMemory();
        if( YelpConfig.favBusinesses.get(result.getBizId()) == null){
                btnFavorites.setText(R.string.add_to_my_favorites);
                btnFavorites.setTag("enable");
        }else{
                btnFavorites.setText(R.string.already_my_favorites);
                btnFavorites.setTag("disable");
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_view, menu);
		return true;
	}
	
	public void addToMyFavorites(View v){
        biz = (Business) getIntent().getSerializableExtra("businessInfo");
        if(v.getTag().equals("enable")){
                
        //result = (ImageResult) getIntent().getSerializableExtra("result");

        String bizStr = "{\"id\":\"" + result.getBizId() +"\",\"name\":\"" + biz.getName() + "\",\"display_phone\":\"" + biz.getPhone()
                        + "\",\"location\":{\"display_address\":[\"" + biz.getAddress() +"\"]}}";
        String picStr = "{\"id\":\"" + result.getBizId() + "\",\"src\":\"" + result.getThumbUrl().replace("http:", "") + "\",\"caption\":\"" + result.getCaption() +"\"}";
        JSONObject bizJObj;
        JSONObject picJObj;
        JSONArray picBiz = new JSONArray();
                try {
                picJObj = new JSONObject(picStr);
                bizJObj = new JSONObject(bizStr);
                picBiz.put(picJObj);
                picBiz.put(bizJObj);

                } catch (JSONException e) {
                        e.printStackTrace();
                }

        

        PrintWriter writer;
        File fileDir = getFilesDir();                        
        try {
                writer = new PrintWriter(new BufferedWriter(new FileWriter(fileDir.getPath() 
                                + "/" + YelpConfig.MY_FAVES_FILENAME, true)));
                writer.println(picBiz.toString());
                writer.close();
                Toast.makeText(this, "Added " + biz.getName() + " to My Favorites",
                        Toast.LENGTH_SHORT).show();         
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        } 
        finish();
        startActivity(getIntent());
        }else{
                Toast.makeText(this, "The " + biz.getName() + "has already been My Favorites",
            Toast.LENGTH_SHORT).show();   
        }    
        
	}
	
	
	 private void readIntoMemory(){

         File fileDir = getFilesDir();
         
         BufferedReader br;
         try {
                 br = new BufferedReader(new FileReader(fileDir.getPath() + "/"
                                 + YelpConfig.MY_FAVES_FILENAME));

                 String line = br.readLine();
                 JSONArray jsonBizAll = new JSONArray();
                 JSONArray jsonPhotosAll = new JSONArray();

                 while (line != null) {
                         JSONArray jsonLine = new JSONArray(line.toString());
                         jsonBizAll.put(jsonLine.get(1));
                         jsonPhotosAll.put(jsonLine.get(0));
                         line = br.readLine();
                 }

                 businesses = Business.fromJson(jsonBizAll);
                 ArrayList<ImageResult> allPhotos = ImageResult
                                 .fromJsonArray(jsonPhotosAll);
                 YelpConfig.favBizPhotos.addAll(allPhotos);
                 YelpConfig.removeDuplicate(YelpConfig.favBizPhotos);
                 YelpConfig.favBusinesses.putAll(businesses);
                 br.close();
         } catch (FileNotFoundException e) {
                 e.printStackTrace();
         } catch (IOException e) {
                 e.printStackTrace();
         } catch (JSONException e) {
                 e.printStackTrace();
         }
         
	 }
	 
	public void callAction(View v) {
	    
	    Intent callIntent = new Intent(Intent.ACTION_CALL);
	    callIntent.setData(Uri.parse("tel:"+phone.getText().toString().trim()));
	    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	    startActivity(callIntent);
	}
}
