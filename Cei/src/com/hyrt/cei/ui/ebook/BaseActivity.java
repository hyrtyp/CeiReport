package com.hyrt.cei.ui.ebook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.hyrt.cei.R;
import com.hyrt.cei.ui.common.LoginActivity;

public class BaseActivity extends SherlockFragmentActivity{

	public static String loginName;

	// 菜单
	public Fragment mFragmentmenu;
	public FragmentManager fm;
	public FragmentTransaction ft;

	public static Intent intent_CeiShelfBookActivity = new Intent();
	public static Intent intent_LoginActivity = new Intent();

	// 维护activity集合
	public static final List<Activity> activities = new ArrayList<Activity>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activities.add(this);

		super.onCreate(savedInstanceState);

		showLoginBtnByUserName();

		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		mFragmentmenu = fm.findFragmentByTag("menu");
		if (mFragmentmenu == null) {
			if (loginName.equals("")) {
				mFragmentmenu = new MenuFragmentNoLogin();
			} else {
				mFragmentmenu = new MenuFragmentIsLogin();
			}
		}
		intent_CeiShelfBookActivity.setClass(this, CeiShelfBookActivity.class);
		intent_LoginActivity.setClass(this, LoginActivity.class);
		// 添加菜单
		ft.add(mFragmentmenu, "menu");
		ft.commit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			for (int i = activities.size()-1; i >0 ; i--) {
				activities.get(i).finish();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		activities.remove(this);
		super.onDestroy();
	}

	public static void destroyActivities() {
		for (int i = 0; i < activities.size(); i++) {
			activities.get(i).finish();
		}
	}

	/**
	 * A fragment that displays a menu. This fragment happens to not have a UI
	 * (it does not implement onCreateView), but it could also have one if it
	 * wanted.
	 */
	public static class MenuFragmentNoLogin extends SherlockFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			menu.add("登录")
					.setIcon(R.drawable.phone_study_saygroup_item_icon)
					.setIntent(intent_LoginActivity)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM);
			menu.add("书架")
					.setIcon(R.drawable.bookshelf)
					.setIntent(intent_CeiShelfBookActivity)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		
	}

	public static class MenuFragmentIsLogin extends SherlockFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			menu.add(loginName).setShowAsAction(
					MenuItem.SHOW_AS_ACTION_IF_ROOM
							| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
			menu.add("书架")
					.setIcon(R.drawable.bookshelf)
					.setIntent(intent_CeiShelfBookActivity)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}

	}

	// 根据登陆与否判断是否显示登陆按钮
	private void showLoginBtnByUserName() {
		// 获取登陆名
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
	}
	
}
