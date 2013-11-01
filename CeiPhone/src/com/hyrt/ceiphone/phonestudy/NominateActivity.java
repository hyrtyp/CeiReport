package com.hyrt.ceiphone.phonestudy;

import java.util.List;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.phonestudy.view.MenuGridView;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.adapter.HorGridViewAdapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 推荐课件
 * 
 */
public class NominateActivity extends FoundationActivity{

	// 业务集合
	private List<ColumnEntry> columnEntries;
	private MenuGridView gridView;
	// 当前查询的业务课件id
	private String oldFunctionId = "";
	// 当前查询的业务课件id
	private String currentFunctionId;
	
	public static final String SERVICE_DATA = "SERVICE_DATA";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_nominate);
		CURRENT_KEY = SERVICE_DATA_KEY;
		loadData();
	}

	private void loadData() {
		ColumnEntry columnEntry = ((CeiApplication) (getApplication())).columnEntry;
		if(columnEntry.getColByName(FoundationActivity.PHONE_NAME,columnEntry.getColByName(FoundationActivity.MODEL_NAME).getId()) == null)
			return;
		columnEntries = columnEntry.getEntryChildsForParent(columnEntry.getColByName(FoundationActivity.PHONE_NAME,columnEntry.getColByName(FoundationActivity.MODEL_NAME).getId()).getId());
		HorGridViewAdapter gridViewAdapter = new HorGridViewAdapter(this,columnEntries);
		gridView = (MenuGridView) findViewById(R.id.phone_study_gridview);
		gridView.setAdapter(gridViewAdapter, 4);
		gridView.setOnItemClickListener(this);
		getServiceDataByServiceId(columnEntries.get(0).getId());
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String functionId = columnEntries.get(position).getId();
		currentFunctionId = functionId;
		if (oldFunctionId.equals(currentFunctionId))
			return;
		oldFunctionId = currentFunctionId;
		for (int i = 0; i < parent.getChildCount(); i++) {
			RelativeLayout rl = (RelativeLayout) parent.getChildAt(i);
			if (i == position) {
				((ImageView) rl.getChildAt(0)).setImageResource(R.drawable.phone_study_menu_select);
				((TextView) rl.getChildAt(1)).setTextColor(Color.WHITE);
			} else {
				((ImageView) rl.getChildAt(0)).setImageDrawable(null);
				((TextView) rl.getChildAt(1)).setTextColor(Color.BLUE);
			}
		}
		getServiceDataByServiceId(functionId);
	}

}
