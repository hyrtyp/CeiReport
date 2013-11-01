package com.hyrt.ceiphone.phonestudy;

import com.hyrt.ceiphone.R;

import android.os.Bundle;

/**
 * 移动学习模块主界面
 * @author 叶朋
 *
 */
public class SayActivity extends FoundationActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.CURRENT_KEY = FoundationActivity.SAY_DATA_KEY;
		setContentView(R.layout.phone_study_say);
	}
}
