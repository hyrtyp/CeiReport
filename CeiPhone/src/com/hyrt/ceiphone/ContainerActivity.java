package com.hyrt.ceiphone;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ContainerActivity extends FragmentActivity {
	
	// 维护activity集合
	public static final List<Activity> activities = new ArrayList<Activity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activities.add(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		activities.remove(this);
		super.onDestroy();
	}
	
	public static void destroyActivities(){
		for(int i=0;i<activities.size();i++){
			activities.get(i).finish();
		}
	}
}
