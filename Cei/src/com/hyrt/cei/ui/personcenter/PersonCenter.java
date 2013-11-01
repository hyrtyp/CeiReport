package com.hyrt.cei.ui.personcenter;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hyrt.cei.R;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.ui.common.WebViewUtil;
import com.hyrt.cei.ui.main.Announcement;
import com.hyrt.cei.ui.main.Disclaimer;
import com.hyrt.cei.ui.witsea.WitSeaActivity;

/**
 * 个人中心
 * 
 * @author Administrator
 * 
 */
public class PersonCenter extends ActivityGroup implements OnClickListener {
	private Button person_info, qccount_info, change_password;
	private RelativeLayout re;
	private Intent i;
	private String loginName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personcentered);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		init();
	}

	private void init() {
		findViewById(R.id.home).setOnClickListener(this);
		findViewById(R.id.home_announcement).setOnClickListener(this);
		findViewById(R.id.home_witsea).setOnClickListener(this);
		findViewById(R.id.home_ceinet).setOnClickListener(this);
		findViewById(R.id.home_disclaimer).setOnClickListener(this);
		findViewById(R.id.person_info).setOnClickListener(this);
		findViewById(R.id.qccount_info).setOnClickListener(this);
		findViewById(R.id.change_password).setOnClickListener(this);
		re = (RelativeLayout) findViewById(R.id.pc_re);
		person_info = (Button) findViewById(R.id.person_info);
		qccount_info = (Button) findViewById(R.id.qccount_info);
		change_password = (Button) findViewById(R.id.change_password);
		SwitchActivity(0);
	}
	@Override
	protected void onPause() {
		super.onPause();
		PersonCenter.this.finish();
	}

	void SwitchActivity(int id) {
		re.removeAllViews();
		Intent intent = null;
		if (id == 0) {
			intent = new Intent(PersonCenter.this, PersonInfo.class);
		} else if (id == 1) {
			intent = new Intent(PersonCenter.this, QccountInfo.class);
		} else if (id == 2) {
			intent = new Intent(PersonCenter.this, ChangePassword.class);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window subActivity = getLocalActivityManager().startActivity(
				"subActivity", intent);
		re.addView(subActivity.getDecorView(), LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home:
			PersonCenter.this.finish();
			break;
		case R.id.home_announcement:
			i = new Intent(PersonCenter.this, Announcement.class);
			if (!loginName.equals(""))
				startActivity(i);
			break;
		case R.id.home_witsea:
			i = new Intent(PersonCenter.this, WitSeaActivity.class);
			if (!loginName.equals(""))
				startActivity(i);
			break;
		case R.id.home_ceinet:
			i = new Intent(PersonCenter.this, WebViewUtil.class);
			i.putExtra("path", "http://mob.cei.gov.cn/");
			startActivity(i);
			break;
		case R.id.home_disclaimer:
			i = new Intent(PersonCenter.this, Disclaimer.class);
			startActivity(i);
			break;
		case R.id.person_info:
			person_info.setBackgroundResource(R.drawable.grzx_1_1);
			qccount_info.setBackgroundResource(R.drawable.grzx_2_0);
			change_password.setBackgroundResource(R.drawable.grzx_3_0);
			SwitchActivity(0);
			break;
		case R.id.qccount_info:
			person_info.setBackgroundResource(R.drawable.grzx_1_0);
			qccount_info.setBackgroundResource(R.drawable.grzx_2_1);
			change_password.setBackgroundResource(R.drawable.grzx_3_0);
			SwitchActivity(1);
			break;
		case R.id.change_password:
			person_info.setBackgroundResource(R.drawable.grzx_1_0);
			qccount_info.setBackgroundResource(R.drawable.grzx_2_0);
			change_password.setBackgroundResource(R.drawable.grzx_3_1);
			SwitchActivity(2);
			break;
		}

	}
}
