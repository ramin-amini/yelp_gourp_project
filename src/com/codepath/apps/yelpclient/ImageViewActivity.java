package com.codepath.apps.yelpclient;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		
		ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_view, menu);
		return true;
	}
	
	public void addToMyFavorites(View v){
        Business biz = (Business) getIntent().getSerializableExtra("businessInfo");
        ImageResult pic = (ImageResult) getIntent().getSerializableExtra("result");

        String bizStr = "{\"id\":\"" + pic.getBizId() +"\",\"name\":\"" + biz.getName() + "\",\"display_phone\":\"" + biz.getPhone()
                        + "\",\"location\":{\"display_address\":[\"" + biz.getAddress() +"\"]}}";
        String picStr = "{\"id\":\"" + pic.getBizId() + "\",\"src\":\"" + pic.getThumbUrl().replace("http:", "") + "\",\"caption\":\"" + pic.getCaption() +"\"}";
        JSONObject bizJObj;
        JSONObject picJObj;
        JSONArray picBiz = new JSONArray();
		try {
			bizJObj = new JSONObject(bizStr);
	        picJObj = new JSONObject(picStr);
	        
	        picBiz.put(picJObj);
	        picBiz.put(bizJObj);

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
}
	public void callAction(View v) {
	    
	    Intent callIntent = new Intent(Intent.ACTION_CALL);
	    callIntent.setData(Uri.parse("tel:"+phone.getText().toString().trim()));
	    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	    startActivity(callIntent);
	}
}
