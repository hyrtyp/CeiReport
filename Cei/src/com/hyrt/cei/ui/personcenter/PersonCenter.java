package com.hyrt.cei.ui.personcenter;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.hyrt.cei.ui.ebook.BaseActivity;
import com.hyrt.cei.ui.main.Announcement;
import com.hyrt.cei.ui.main.Disclaimer;
import com.hyrt.cei.ui.witsea.WitSeaActivity;

/**
 * 个人中心
 * 
 * @author Administrator
 * 
 */
public class PersonCenter extends BaseActivity implements OnClickListener {
	private Button person_info, qccount_info, change_password;
	private RelativeLayout re;
	private Intent i;
	private String loginName;

	// 视图
	private Fragment current;
	private Fragment mContent;
	private Fragment fragmentPersonInfo;
	private Fragment fragmentQccountInfo;
	private Fragment fragmentChangePassword;

	public FragmentManager fm;
	public FragmentTransaction ft;

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
		// findViewById(R.id.home).setOnClickListener(this);
		// findViewById(R.id.home_announcement).setOnClickListener(this);
		// findViewById(R.id.home_witsea).setOnClickListener(this);
		// findViewById(R.id.home_ceinet).setOnClickListener(this);
		// findViewById(R.id.home_disclaimer).setOnClickListener(this);
		findViewById(R.id.person_info).setOnClickListener(this);
		findViewById(R.id.qccount_info).setOnClickListener(this);
		findViewById(R.id.change_password).setOnClickListener(this);
		re = (RelativeLayout) findViewById(R.id.pc_re);
		person_info = (Button) findViewById(R.id.person_info);
		qccount_info = (Button) findViewById(R.id.qccount_info);
		change_password = (Button) findViewById(R.id.change_password);
		// SwitchActivity(0);

		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();

		mContent = fragmentPersonInfo = new PersonInfo();
		ft.add(R.id.pc_re, fragmentPersonInfo);
		ft.commit();
	}

	@Override
	protected void onPause() {
		super.onPause();
		PersonCenter.this.finish();
	}

	private void switchContent(Fragment from, Fragment to) {
		if (mContent != to) {
			mContent = to;
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			if (!to.isAdded()) { // 先判断是否被add过
				transaction.hide(from).add(R.id.pc_re, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
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
			if (fragmentPersonInfo == null) {
				fragmentPersonInfo = new PersonInfo();
			}
			switchContent(mContent, fragmentPersonInfo);
			break;
		case R.id.qccount_info:
			person_info.setBackgroundResource(R.drawable.grzx_1_0);
			qccount_info.setBackgroundResource(R.drawable.grzx_2_1);
			change_password.setBackgroundResource(R.drawable.grzx_3_0);
			if (fragmentQccountInfo == null) {
				fragmentQccountInfo = new QccountInfo();
			}
			switchContent(mContent, fragmentQccountInfo);
			break;
		case R.id.change_password:
			person_info.setBackgroundResource(R.drawable.grzx_1_0);
			qccount_info.setBackgroundResource(R.drawable.grzx_2_0);
			change_password.setBackgroundResource(R.drawable.grzx_3_1);
			if (fragmentChangePassword == null) {
				fragmentChangePassword = new ChangePassword();
			}
			switchContent(mContent, fragmentChangePassword);
			break;
		}

	}
}
