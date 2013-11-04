package com.codepath.apps.yelpclient;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.apps.yelpclient.models.Business;
import com.loopj.android.image.SmartImageView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ImageDisplayActivity extends Activity {
	TextView name; 
	TextView caption; 
	TextView address;
	//TextView phone;
	Button btnAddMyFave;
	private static final String MY_FAVES_FILENAME = "yelp_my_faves.txt";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		btnAddMyFave = (Button) findViewById(R.id.btnAddFaves);

		
		ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
		SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		String smImage = result.getThumbUrl();
		ivImage.setImageUrl(smImage.substring(0,smImage.lastIndexOf("/")+1) + "l.jpg");
		name = (TextView) findViewById(R.id.nameText);
		address = (TextView) findViewById(R.id.addressView);
		//phone = (TextView) findViewById(R.id.phone);
		caption = (TextView) findViewById(R.id.captionView);
		String resultCaption = result.getCaption();
		if(resultCaption.equalsIgnoreCase("null" ))
			caption.setText("");	//we can put some text here		
		else 
			caption.setText(resultCaption);
		Business info = (Business) getIntent().getSerializableExtra("businessInfo");
		name.setText(info.getName() + "\nRating: " + info.getRating());
		address.setText(info.getAddress() + info.getPhone());
		//phone.setText(info.getString("phone"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);
		return true;
	}
	
	public void addToMyFavorites(View v) throws IOException, JSONException{
		Business biz = (Business) getIntent().getSerializableExtra("businessInfo");
		ImageResult pic = (ImageResult) getIntent().getSerializableExtra("result");

		String bizStr = "{\"id\":\"" + pic.getBizId() +"\",\"name\":\"" + biz.getName() + "\",\"display_phone\":\"" + biz.getPhone()
				+ "\",\"location\":{\"display_address\":[\"" + biz.getAddress() +"\"]}}";
		String picStr = "{\"id\":\"" + pic.getBizId() + "\",\"src\":\"" + pic.getThumbUrl().replace("http:", "") + "\",\"caption\":\"" + pic.getCaption() +"\"}";
		JSONObject bizJObj = new JSONObject(bizStr);
		JSONObject picJObj = new JSONObject(picStr);

		JSONArray picBiz = new JSONArray();
		picBiz.put(picJObj);
		picBiz.put(bizJObj);

		PrintWriter writer;
		File fileDir = getFilesDir();			
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(fileDir.getPath() 
					+ "/" + MY_FAVES_FILENAME, true)));
			writer.println(picBiz.toString());
			writer.close();
			Toast.makeText(this, "Added " + biz.getName() + " to My Favorites",
				Toast.LENGTH_SHORT).show();	 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
