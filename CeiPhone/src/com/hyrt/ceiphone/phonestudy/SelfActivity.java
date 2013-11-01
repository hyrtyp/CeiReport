package com.hyrt.ceiphone.phonestudy;

import java.util.List;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.vo.Preload;
import com.hyrt.ceiphone.R;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 移动学习模块主界面
 * @author 叶朋
 *
 */
public class SelfActivity extends FoundationActivity {
	
	// 当先显示的列表种类0代表全部，1代表下载的
	private int currentLookKind = 0;
	// 数据库查询类
	private DataHelper dataHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.CURRENT_KEY = FoundationActivity.SELF_DATA_KEY;
		setContentView(R.layout.phone_study_self);
		dataHelper = ((CeiApplication) getApplication()).dataHelper;
		final LinearLayout labelParent = (LinearLayout) findViewById(R.id.phone_study_labelParent);
		for (int i = 0; i < labelParent.getChildCount(); i++) {
			final int j = i;
			labelParent.getChildAt(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (currentLookKind == j)
						return;
					index = 0;
					courses.clear();
					coursewares.clear();
					switch (j) {
					case 0:
						currentLookKind = 0;
						courses.addAll(allCoursewares);
						break;
					case 1:
						currentLookKind = 1;
						List<Preload> preloadCourseware = dataHelper.getPreloadList();
						//已下载自选课集合
						for(int x=0;x<preloadCourseware.size();x++){
							if(preloadCourseware.get(x).getLoadFinish() == 1){
								for(int y=0;y<allCoursewares.size();y++){
									if(allCoursewares.get(y).getClassId().equals(preloadCourseware.get(x).getLoadPlayId())){
										courses.add(allCoursewares.get(y));
									}
								}
							}
						}
						break;
					}
					Message msg = dataHandler.obtainMessage();
					msg.arg1 = FoundationActivity.LVDATA_KEY;
					dataHandler.sendMessage(msg);
					for (int i = 0; i < labelParent.getChildCount(); i++) {
						Button labelIv = (Button) labelParent.getChildAt(i);
						if (i == j) {
							labelIv.setBackgroundResource(R.drawable.phone_study_label_select);
							labelIv.setTextColor(Color.BLUE);
						} else {
							labelIv.setBackgroundResource(R.drawable.phone_study_labelnormal);
							labelIv.setTextColor(Color.WHITE);
						}
					}
				}
			});
		}
	}
}
