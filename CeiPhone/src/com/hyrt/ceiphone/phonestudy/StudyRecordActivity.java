package com.hyrt.ceiphone.phonestudy;

import com.hyrt.ceiphone.R;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 移动学习模块主界面
 * 
 * @author 叶朋
 * 
 */
public class StudyRecordActivity extends FoundationActivity {

	// 标签集合的父级元素
	private LinearLayout labelParent;
	// 4个操作标志位
	private final static int ALL_INDEX = 0;
	private final static int NOSTUDY_INDEX = 1;
	private final static int NOUPLOAD_INDEX = 2;
	private final static int UPLOAD_INDEX = 3;
	private final static int STUDY_INDEX = 4;
	// 当先显示的列表种类
	private int currentLookKind = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.CURRENT_KEY = FoundationActivity.RECORD_DATA_KEY;
		setContentView(R.layout.phone_study_record);
		labelParent = (LinearLayout) findViewById(R.id.phone_study_labelParent);
		for (int i = 0; i < labelParent.getChildCount(); i++) {
			final int x = i;
			Button iv = (Button) labelParent.getChildAt(i);
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try{
					if(currentLookKind == x)
						return;
					currentLookKind = x;
					courses.clear();
					coursewares.clear();
					switch (x) {
					case ALL_INDEX:
						courses.addAll(allCoursewares);
						break;
					case NOUPLOAD_INDEX:
						for (int j = 0; j < allCoursewares.size(); j++) {
							try{
							if (allCoursewares.get(j).getUploadTime()!= 0 && !"1".equals(courses.get(j).getIscompleted())){
								courses.add(allCoursewares.get(j));
							}
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						break;
					case NOSTUDY_INDEX:
						for (int j = 0; j < allCoursewares.size(); j++) {
							if (!"1".equals(allCoursewares.get(j).getIscompleted())) {
								courses.add(allCoursewares.get(j));
							}
						}
						break;
					case UPLOAD_INDEX:
						for (int j = 0; j < allCoursewares.size(); j++) {
							if ("1".equals(allCoursewares.get(j).getIscompleted()) || allCoursewares.get(j).getUploadTime()== 0){
								courses.add(allCoursewares.get(j));
							}
						}
						break;
					case STUDY_INDEX:
						for (int j = 0; j < allCoursewares.size(); j++) {
							if ("1".equals(allCoursewares.get(j).getIscompleted())) {
								courses.add(allCoursewares.get(j));
							}
						}
						break;
					}
					Message msg = dataHandler.obtainMessage();
					msg.arg1 = FoundationActivity.LVDATA_KEY;
					dataHandler.sendMessage(msg);
					for (int i = 0; i < labelParent.getChildCount(); i++) {
						Button iv = (Button) labelParent.getChildAt(i);
						if (i == x) {
							iv.setBackgroundResource(R.drawable.phone_study_label_select);
							iv.setTextColor(Color.BLUE);
						} else {
							iv.setBackgroundResource(R.drawable.phone_study_labelnormal);
							iv.setTextColor(Color.WHITE);
						}
					}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
		}
	}
}
