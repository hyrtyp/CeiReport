package com.hyrt.ceiphone.phonestudy;

import java.util.ArrayList;
import java.util.List;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.phonestudy.view.MenuGridView;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ClassType;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.adapter.HorGridViewAdapter;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class KindsActivity extends FoundationActivity{

	private List<ColumnEntry> columnEntries;
	private List<ColumnEntry> firstColumnEntries;
	private List<ColumnEntry> secondColumnEntries;
	private MenuGridView gridView;
	private MenuGridView gridView1;
	private MenuGridView gridView2;
	private List<ClassType> classTypes = new ArrayList<ClassType>();
	private String oldFunctionId = "";
	public static final String KIND_DATA = "KIND_DATA";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_kinds);
		CURRENT_KEY = KIND_DATA_KEY;
		initMenuData();
	}

	private void initLvData(final String functionId) {
		currentFunctionId = functionId;
		if (oldFunctionId.equals(currentFunctionId))
			return;
		oldFunctionId = currentFunctionId;
		getServiceDataByKindId(currentFunctionId);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		switch(adapter.getId()){
		case R.id.phone_study_gridview:
			for (int i = 0; i < adapter.getChildCount(); i++) {
				RelativeLayout rl = (RelativeLayout) adapter.getChildAt(i);
				if (i == position) {
					((ImageView) rl.getChildAt(0)).setImageResource(R.drawable.phone_study_menu_select);
					((TextView) rl.getChildAt(1)).setTextColor(Color.WHITE);
				} else {
					((ImageView) rl.getChildAt(0)).setImageDrawable(null);
					((TextView) rl.getChildAt(1)).setTextColor(Color.BLUE);
				}
			}
			firstColumnEntries = new ArrayList<ColumnEntry>();
			for(int i=0;i<classTypes.size();i++){
				if(classTypes.get(i).getParentId().equals(columnEntries.get(position).getId())){
					ColumnEntry menuNodeChilds = new ColumnEntry();
					menuNodeChilds.setId(classTypes.get(i).getClassId());
					menuNodeChilds.setName(classTypes.get(i).getContent());
					firstColumnEntries.add(menuNodeChilds);
				}
			}
			HorGridViewAdapter gridViewAdapter = new HorGridViewAdapter(KindsActivity.this,firstColumnEntries,false);
			gridView1.setAdapter(gridViewAdapter, 4);
			findViewById(R.id.phone_study_gridviewparent2).setVisibility(View.GONE);
			phoneStudyListView.setVisibility(View.GONE);
			break;
		case R.id.phone_study_gridview1:
			for (int i = 0; i < adapter.getChildCount(); i++) {
				RelativeLayout rl = (RelativeLayout) adapter.getChildAt(i);
				if (i == position) {
					((ImageView) rl.getChildAt(0)).setImageResource(R.drawable.phone_study_menu_select);
					((TextView) rl.getChildAt(1)).setTextColor(Color.WHITE);
				} else {
					((ImageView) rl.getChildAt(0)).setImageDrawable(null);
					((TextView) rl.getChildAt(1)).setTextColor(Color.BLUE);
				}
			}
			secondColumnEntries = new ArrayList<ColumnEntry>();
			for(int i=0;i<classTypes.size();i++){
				if(classTypes.get(i).getParentId().equals(firstColumnEntries.get(position).getId())){
					ColumnEntry menuNodeChilds = new ColumnEntry();
					menuNodeChilds.setId(classTypes.get(i).getClassId());
					menuNodeChilds.setName(classTypes.get(i).getContent());
					secondColumnEntries.add(menuNodeChilds);
				}
			}
			if(secondColumnEntries.size() == 0 && firstColumnEntries.size() > 0){
				phoneStudyListView.setVisibility(View.VISIBLE);
				initLvData(firstColumnEntries.get(position).getId());
				findViewById(R.id.phone_study_gridviewparent2).setVisibility(View.GONE);
			}else{
				HorGridViewAdapter gridViewAdapter2 = new HorGridViewAdapter(KindsActivity.this,secondColumnEntries,false);
				gridView2.setAdapter(gridViewAdapter2, 4);
				findViewById(R.id.phone_study_gridviewparent2).setVisibility(View.VISIBLE);
			}
			break;
		case R.id.phone_study_gridview2:
			for (int i = 0; i < adapter.getChildCount(); i++) {
				RelativeLayout rl = (RelativeLayout) adapter.getChildAt(i);
				if (i == position) {
					((ImageView) rl.getChildAt(0)).setImageResource(R.drawable.phone_study_menu_select);
					((TextView) rl.getChildAt(1)).setTextColor(Color.WHITE);
				} else {
					((ImageView) rl.getChildAt(0)).setImageDrawable(null);
					((TextView) rl.getChildAt(1)).setTextColor(Color.BLUE);
				}
			}
			phoneStudyListView.setVisibility(View.VISIBLE);
			initLvData(secondColumnEntries.get(position).getId());
			break;
		}
	}
	
	private void initMenuData() {
		final Handler handler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				if(classTypes.size() <= 0)
					return;
				columnEntries = new ArrayList<ColumnEntry>();
				String rootId = classTypes.get(0).getClassId();
				classTypes.remove(0);
				for (int i = 0; i < classTypes.size(); i++) {
					if (!classTypes.get(i).getParentId().equals(rootId))
						continue;
					ColumnEntry menuNodeChilds = new ColumnEntry();
					menuNodeChilds.setId(classTypes.get(i).getClassId());
					menuNodeChilds.setName(classTypes.get(i).getContent());
					columnEntries.add(menuNodeChilds);
				}
				HorGridViewAdapter gridViewAdapter = new HorGridViewAdapter(KindsActivity.this,columnEntries,true);
				gridView = (MenuGridView) findViewById(R.id.phone_study_gridview);
				gridView.setAdapter(gridViewAdapter, 4);
				gridView.setOnItemClickListener(KindsActivity.this);
				//加载刚进页面显示的数据
				loadFirstData();
			}
		};
		//获取种类列表
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (((CeiApplication) getApplication()).isNet()) {
					ColumnEntry columnEntry = ((CeiApplication) (KindsActivity.this.getApplication())).columnEntry;
					ColumnEntry phoneStudyCol = columnEntry.getColByName(FoundationActivity.MODEL_NAME);
					StringBuilder functionIds = new StringBuilder(phoneStudyCol.getId());
					for (int i = 0; i < columnEntry.getColumnEntryChilds().size(); i++) {
						ColumnEntry entryChild = columnEntry.getColumnEntryChilds().get(i);
						if (entryChild.getPath() != null && entryChild.getPath().contains(phoneStudyCol.getId())) {
							functionIds.append("," + entryChild.getId());
						}
					}
					String result = Service.queryClassByType(functionIds.toString());
					XmlUtil.parseClassType(result, classTypes);
					((CeiApplication) (KindsActivity.this.getApplication())).dataHelper.saveClassType(classTypes);
				} else {
					classTypes = ((CeiApplication) (KindsActivity.this.getApplication())).dataHelper.getClassTypes();
				}
				handler.sendMessage(handler.obtainMessage());
			}
		}).start();
		gridView1 = (MenuGridView) findViewById(R.id.phone_study_gridview1);
		gridView1.setOnItemClickListener(KindsActivity.this);
		gridView2 = (MenuGridView) findViewById(R.id.phone_study_gridview2);
		gridView2.setOnItemClickListener(KindsActivity.this);
	}
	
	/**
	 * 加载第一级的数据
	 */
	private void loadFirstData(){
		findViewById(R.id.phone_study_gridviewparent1).setVisibility(View.GONE);
		findViewById(R.id.phone_study_gridviewparent2).setVisibility(View.GONE);
		if(columnEntries.size() == 0){
			findViewById(R.id.phone_study_gridviewparent).setVisibility(View.GONE);
			return;
		}
		firstColumnEntries = new ArrayList<ColumnEntry>();
		for(int i=0;i<classTypes.size();i++){
			if(classTypes.get(i).getParentId().equals(columnEntries.get(0).getId())){
				ColumnEntry menuNodeChilds = new ColumnEntry();
				menuNodeChilds.setId(classTypes.get(i).getClassId());
				menuNodeChilds.setName(classTypes.get(i).getContent());
				firstColumnEntries.add(menuNodeChilds);
			}
		}
		HorGridViewAdapter gridViewAdapter = new HorGridViewAdapter(KindsActivity.this,firstColumnEntries,true);
		gridView1.setAdapter(gridViewAdapter, 4);
		if(firstColumnEntries.size() != 0)
			findViewById(R.id.phone_study_gridviewparent1).setVisibility(View.VISIBLE);
		
		secondColumnEntries = new ArrayList<ColumnEntry>();
		if(firstColumnEntries.size() == 0){
			initLvData(columnEntries.get(0).getId());
			return;
		}
		for(int i=0;i<classTypes.size();i++){
			if(classTypes.get(i).getParentId().equals(firstColumnEntries.get(0).getId())){
				ColumnEntry menuNodeChilds = new ColumnEntry();
				menuNodeChilds.setId(classTypes.get(i).getClassId());
				menuNodeChilds.setName(classTypes.get(i).getContent());
				secondColumnEntries.add(menuNodeChilds);
			}
		}
		if(secondColumnEntries.size() == 0){
			phoneStudyListView.setVisibility(View.VISIBLE);
			initLvData(firstColumnEntries.get(0).getId());
			return;
		}
		HorGridViewAdapter gridViewAdapter2 = new HorGridViewAdapter(KindsActivity.this,secondColumnEntries,true);
		gridView2.setAdapter(gridViewAdapter2, 4);
		if(secondColumnEntries.size() != 0)
			findViewById(R.id.phone_study_gridviewparent2).setVisibility(View.VISIBLE);
		phoneStudyListView.setVisibility(View.VISIBLE);
		initLvData(secondColumnEntries.get(0).getId());
	}

}