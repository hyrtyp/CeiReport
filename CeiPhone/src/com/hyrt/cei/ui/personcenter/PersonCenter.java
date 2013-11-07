package com.hyrt.cei.ui.personcenter;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.hyrt.cei.ui.witsea.WitSeaActivity;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.ContainerActivity.MenuFragmentIsLogin;
import com.hyrt.ceiphone.common.Announcement;
import com.hyrt.ceiphone.common.HomePageDZB;
import com.hyrt.readreport.CeiShelfBookActivity;

/**
 * 个人中心
 * 
 * @author Administrator
 * 
 */
public class PersonCenter extends ContainerActivity implements OnClickListener {
	private Button person_info, qccount_info, change_password;
	private RelativeLayout re;
	private Intent intent;
	private static String loginName;

	// 视图
	private Fragment current;
	private Fragment mContent;
	private Fragment fragmentPersonInfo;
	private Fragment fragmentQccountInfo;
	private Fragment fragmentChangePassword;
	// 菜单
	public Fragment mFragmentmenu;
	public FragmentManager fm;
	public FragmentTransaction ft;
	private static Intent intent_CeiShelfBookActivity = new Intent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ContainerActivity.activities.add(this);
		setContentView(R.layout.personcentered);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		init();
	}

	private void init() {
		// LinearLayout bottomsLl = (LinearLayout)
		// findViewById(R.id.bottoms_Ll);
		// for (int i = 0; i < bottomsLl.getChildCount(); i++) {
		// ((RelativeLayout) (bottomsLl.getChildAt(i))).getChildAt(0)
		// .setOnClickListener(this);
		// }
		findViewById(R.id.person_info).setOnClickListener(this);
		findViewById(R.id.qccount_info).setOnClickListener(this);
		findViewById(R.id.change_password).setOnClickListener(this);
		re = (RelativeLayout) findViewById(R.id.pc_re);
		person_info = (Button) findViewById(R.id.person_info);
		qccount_info = (Button) findViewById(R.id.qccount_info);
		change_password = (Button) findViewById(R.id.change_password);
		
		intent_CeiShelfBookActivity.setClass(this, CeiShelfBookActivity.class);
		
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		// 菜单
		mFragmentmenu = fm.findFragmentByTag("menu");
		mFragmentmenu = new MenuFragmentIsLogin();
		ft.add(mFragmentmenu, "menu");
		//视图
		mContent = fragmentPersonInfo = new PersonInfo();
		ft.add(R.id.pc_re, fragmentPersonInfo);
		ft.commit();

		// SwitchActivity(0);
	}

	@Override
	protected void onPause() {
		super.onPause();
		PersonCenter.this.finish();
	}

	// void SwitchActivity(int id) {
	// re.removeAllViews();
	// Intent intent = null;
	// if (id == 0) {
	// intent = new Intent(PersonCenter.this, PersonInfo.class);
	// } else if (id == 1) {
	// intent = new Intent(PersonCenter.this, QccountInfo.class);
	// } else if (id == 2) {
	// intent = new Intent(PersonCenter.this, ChangePassword.class);
	// }
	// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// @SuppressWarnings("deprecation")
	// Window subActivity = getLocalActivityManager().startActivity(
	// "subActivity", intent);
	// re.addView(subActivity.getDecorView(), LayoutParams.FILL_PARENT,
	// LayoutParams.FILL_PARENT);
	// }

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
		case R.id.main_rl:
			intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
			break;
		case R.id.notice_rl:
			intent = new Intent(this, Announcement.class);
			startActivity(intent);
			break;
		case R.id.collect_rl:
			intent = new Intent(this, WitSeaActivity.class);
			startActivity(intent);
			break;
		case R.id.psc_rl:
			intent = new Intent(this, PersonCenter.class);
			startActivity(intent);
			break;
		case R.id.person_info:
			findViewById(R.id.person_info).setBackgroundResource(
					R.drawable.grzx_1_0);
			findViewById(R.id.qccount_info).setBackgroundResource(
					R.drawable.grzx_2_1);
			findViewById(R.id.change_password).setBackgroundResource(
					R.drawable.grzx_3_1);
			// SwitchActivity(0);
			if (fragmentPersonInfo == null) {
				fragmentPersonInfo = new PersonInfo();
			}
			switchContent(mContent, fragmentPersonInfo);
			break;
		case R.id.qccount_info:
			findViewById(R.id.person_info).setBackgroundResource(
					R.drawable.grzx_1_1);
			findViewById(R.id.qccount_info).setBackgroundResource(
					R.drawable.grzx_2_0);
			findViewById(R.id.change_password).setBackgroundResource(
					R.drawable.grzx_3_1);
			// SwitchActivity(1);
			if (fragmentQccountInfo == null) {
				fragmentQccountInfo = new QccountInfo();
			}
			switchContent(mContent, fragmentQccountInfo);
			break;
		case R.id.change_password:
			findViewById(R.id.person_info).setBackgroundResource(
					R.drawable.grzx_1_1);
			findViewById(R.id.qccount_info).setBackgroundResource(
					R.drawable.grzx_2_1);
			findViewById(R.id.change_password).setBackgroundResource(
					R.drawable.grzx_3_0);
			// SwitchActivity(2);
			if (fragmentChangePassword == null) {
				fragmentChangePassword = new ChangePassword();
			}
			switchContent(mContent, fragmentChangePassword);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		ContainerActivity.activities.remove(this);
		super.onDestroy();
	}

	public static class MenuFragmentIsLogin extends SherlockFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			menu.add(loginName)
			.setShowAsAction(
					MenuItem.SHOW_AS_ACTION_IF_ROOM
							| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			menu.add("书架")
					.setIcon(R.drawable.read_report_bookshelf)
					.setIntent(intent_CeiShelfBookActivity)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}

	}
}
