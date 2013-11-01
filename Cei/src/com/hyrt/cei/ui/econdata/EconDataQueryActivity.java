package com.hyrt.cei.ui.econdata;

import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EconDataQueryActivity extends Activity implements OnClickListener{
	ImageView zxImg, szImg, zjImg, fxImg, zbImg, sjImg, homeImg, search,backImg;
	TextView text1, text2, text3, text4, text5;
	List<String> data1, data2, data3, data4, data5;;
	ListView listV1, listV2, listV3, listV4, listV5;
	private CeiApplication application;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_shujuchaxun);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		application=(CeiApplication) getApplication();
		//initData();
		initView();
	}

	private void initData() {
		
	}

	private void initView() {
		search = (ImageView) findViewById(R.id.econ_search_btn);
		//search.setOnClickListener(this);
		zxImg= (ImageView) findViewById(R.id.econ_data_dataquary_zx);
		zxImg.setOnClickListener(this);
		szImg = (ImageView) findViewById(R.id.econ_data_dataquary_sz);
		szImg.setOnClickListener(this);
		zjImg = (ImageView) findViewById(R.id.econ_data_dataquary_zz);
		zjImg.setOnClickListener(this);

		fxImg = (ImageView) findViewById(R.id.econ_data_dataquary_fx);
		fxImg.setOnClickListener(this);
		zbImg = (ImageView) findViewById(R.id.econ_data_dataquary_zb);
		zbImg.setOnClickListener(this);

		homeImg = (ImageView) findViewById(R.id.econ_data_dataquary_home);
		homeImg.setOnClickListener(this);
		RelativeLayout title = (RelativeLayout) findViewById(R.id.econ_title);
		backImg = (ImageView) title.findViewById(R.id.econ_data_dataquary_back);
		backImg.setOnClickListener(this);
/*
		text1 = (TextView) findViewById(R.id.shujuchaxun_data1);
		text1.setOnClickListener(this);
		text2 = (TextView) findViewById(R.id.shujuchaxun_data2);
		text2.setOnClickListener(this);
		text3 = (TextView) findViewById(R.id.shujuchaxun_data3);
		text3.setOnClickListener(this);
		text4 = (TextView) findViewById(R.id.shujuchaxun_data4);
		text4.setOnClickListener(this);
		text5 = (TextView) findViewById(R.id.shujuchaxun_data5);
		text5.setOnClickListener(this);
*/
	}

	@Override
	public void onClick(View v) {
		if (v == zxImg) {
			// 最新页面
			Intent intent = new Intent(this, EconDataMain.class);
			startActivity(intent);
		} else if (v == szImg) {
			Intent intent = new Intent(this, EconDateNumberActivity.class);
			startActivity(intent);
			// 数字快讯
		} else if (v == zjImg) {
			// 中经指数
			Intent intent = new Intent(this, EconZZDataActivity.class);
			startActivity(intent);
		} else if (v == fxImg) {
			// 分析预测
			Intent intent = new Intent(this, EconFXDataActivity.class);
			startActivity(intent);
		} else if (v == zbImg) {
			// 指标查询
			Intent intent = new Intent(this, EconZBQueryActivity.class);
			startActivity(intent);
		} else if (v == sjImg) {
			return;
		} else if (v == homeImg) {
			// 首页
			Intent intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
		} else if (v == backImg) {
			this.finish();
		} else if (v == search) {
		}
	}
	 public void saveActivity(Class<?> clas) {
			if (application.activitys.contains(clas)) {
				application.activitys.remove(clas);
				application.activitys.add(clas);
			} else {
				application.activitys.add(clas);
			}
		}
}
