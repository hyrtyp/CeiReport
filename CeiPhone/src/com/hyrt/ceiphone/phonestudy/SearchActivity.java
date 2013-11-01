package com.hyrt.ceiphone.phonestudy;

import com.hyrt.ceiphone.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 移动学习模块主界面
 * @author 叶朋
 *
 */
public class SearchActivity extends FoundationActivity {
	
	private EditText editText;
	private String oldStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.CURRENT_KEY = FoundationActivity.SEARCH_DATA_KEY;
		setContentView(R.layout.phone_study_search);
		final SharedPreferences settings = getSharedPreferences("search_result",Activity.MODE_PRIVATE);
		String historyStr = settings.getString("history", "");
		editText = (EditText)findViewById(R.id.search_edit);
		editText.setText(historyStr);
		findViewById(R.id.search_btn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);   
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
				String className = editText.getText().toString().trim();
				if(className.equals(oldStr))
					return;
				courses.clear();
				coursewares.clear();
				oldStr = className;
				if(!className.trim().equals("")){
					getServiceDataByClassName(className);
					Editor editor = settings.edit();
					editor.putString("history", className);
					editor.commit();
				}
			}
		});
	}
}
