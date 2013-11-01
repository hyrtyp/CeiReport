package com.hyrt.ceiphone.phonestudy;

import com.hyrt.ceiphone.R;

import android.os.Bundle;

/**
 * 移动学习模块主界面
 * @author 叶朋
 *
 */
public class FreeActivity extends FoundationActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_free);
		CURRENT_KEY = FREE_DATA_KEY;
	}
}
