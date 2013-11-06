package com.hyrt.readreport;

import android.content.Intent;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockListActivity;
import com.hyrt.ceiphone.R;

public class SampleList extends SherlockListActivity{
	
	public static int THEME = R.style.Theme_Cei_Sherlock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(
				R.drawable.phone_study_top_item_bg));
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
        String path = intent.getStringExtra("com.example.android.apis.Path");

        if (path == null) {
            path = "";
        }
	}
}
