package com.hyrt.cei.ui.main;

import com.hyrt.cei.R;

import android.app.Activity;
import android.os.Bundle;
/**
 * 我的智库
 * @author Administrator
 *
 */
public class MyThinkTank extends Activity{
    @Override
 protected void onCreate(Bundle savedInstanceState) {
 	// TODO Auto-generated method stub
 	super.onCreate(savedInstanceState);
 	setContentView(R.layout.my_think_tank);
 	overridePendingTransition(R.anim.push_in, R.anim.push_out);
 }
}
