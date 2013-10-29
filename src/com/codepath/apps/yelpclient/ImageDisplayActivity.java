package com.codepath.apps.yelpclient;


import com.codepath.apps.yelpclient.models.Business;
import com.loopj.android.image.SmartImageView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ImageDisplayActivity extends Activity {
	TextView name; 
	TextView caption; 
	TextView address;
	//TextView phone;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		
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
		Business info;
		info = (Business) getIntent().getSerializableExtra("businessInfo");
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
}
