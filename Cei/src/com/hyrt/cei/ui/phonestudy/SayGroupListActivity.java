package com.hyrt.cei.ui.phonestudy;

import java.util.ArrayList;
import java.util.List;
import com.hyrt.cei.R;
import com.hyrt.cei.adapter.PhoneStudyForumAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.webservice.service.Service;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SayGroupListActivity extends Activity implements OnClickListener {

	private ListView lv;
	List<Courseware> saygroupCoursewares = new ArrayList<Courseware>();
	private static final int NO_NET = 1;
	private static final int DATA_READY = 2;
	// 当前页索引
	private int index = 0;
	// 讨论组下所有的课件列表
	private List<Courseware> courses = new ArrayList<Courseware>();
	//讨论组列表适配器
	private PhoneStudyForumAdapter adapter;
	//更多按钮
	private LinearLayout footer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_saygrouplistcourse);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		initBottom();
		initLvData();
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(SayGroupListActivity.this,
						SayGroupActivity.class);
				intent.putExtra("coursewareinfo",
						saygroupCoursewares.get(position));
				startActivity(intent);
			}
		});
		footer = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.phone_study_listview_bottom, null);
		footer.findViewById(R.id.phone_study_morebtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						index++;
						for (int i = index * 20; i < (index + 1) * 20
								&& i < courses.size(); i++) {
							if(i == courses.size()-1)
								footer.setVisibility(View.GONE);
							saygroupCoursewares.add(courses.get(i));
						}
						adapter.notifyDataSetChanged();
					}
				});
		lv.addFooterView(footer);
		for (int i = 0; i < HomePageActivity.phoneStudyContainer.size(); i++) {
			try {
				SayGroupListActivity isSayGroupListActivity = (SayGroupListActivity) (HomePageActivity.phoneStudyContainer
						.get(i));
				isSayGroupListActivity.finish();
			} catch (Exception e) {
			}
		}
		HomePageActivity.phoneStudyContainer.add(this);
		findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SayGroupListActivity.this.finish();
			}
		});
		findViewById(R.id.phone_study_search_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(SayGroupListActivity.this,
								SearchCourseActivity.class);
						startActivity(intent);
					}
				});
		findViewById(R.id.phone_study_refresh).setOnClickListener(this);
		findViewById(R.id.phone_study_downmanager).setOnClickListener(this);
	}

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.arg1) {
			case NO_NET:
				findViewById(R.id.phone_study_progressLl).setVisibility(
						View.GONE);
				MyTools.exitShow(SayGroupListActivity.this, ((Activity)SayGroupListActivity.this).getWindow().getDecorView(),  "网络有问题!");
				break;
			case DATA_READY:
				for (int i = index * 20; i < (index + 1) * 20
						&& i < courses.size(); i++) {
					if(i == courses.size()-1){
						footer.setVisibility(View.GONE);
					}else{
						if(footer.getVisibility() == View.GONE)
							footer.setVisibility(View.VISIBLE);
					}
					saygroupCoursewares.add(courses.get(i));
				}
				findViewById(R.id.phone_study_progressLl).setVisibility(
						View.GONE);
				adapter = new PhoneStudyForumAdapter(
						SayGroupListActivity.this, saygroupCoursewares, lv);
				lv.setAdapter(adapter);
				break;
			}

		}
	};

	private void initLvData() {
		lv = (ListView) findViewById(R.id.phone_study_selfcourse_listview);
		findViewById(R.id.phone_study_progressLl).setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String result = Service
						.querySchoolForumInfo(((CeiApplication) getApplication()).columnEntry
								.getUserId());
				Message message = handler.obtainMessage();
				if (((CeiApplication) getApplication()).isNet()) {
					if (XmlUtil.parseReturnCode(result).equals("-1")) {
						message.arg1 = NO_NET;
					} else {
						message.arg1 = DATA_READY;
						XmlUtil.parseErrorCoursewares(result, courses);
						for (int i = 0; i < courses.size(); i++) {
							Courseware courseware = courses.get(i);
							courseware.setSay(true);
							((CeiApplication) (getApplication())).dataHelper.saveCourseware(courseware);
						}
					}
				} else {
					Courseware courseware = new Courseware();
					courseware.setSay(true);
					courses = ((CeiApplication) (getApplication())).dataHelper
							.getCoursewares(courseware);
					message.arg1 = DATA_READY;
				}
				handler.sendMessage(message);
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		HomePageActivity.phoneStudyContainer.remove(this);
		super.onDestroy();
	}

	private void initBottom() {
		ImageView headIv = (ImageView) findViewById(R.id.phone_study);
		ImageView newIv = (ImageView) findViewById(R.id.phone_study_new);
		ImageView nominateIv = (ImageView) findViewById(R.id.phone_study_nominate);
		ImageView freeIv = (ImageView) findViewById(R.id.phone_study_free);
		ImageView kindIv = (ImageView) findViewById(R.id.phone_study_kind);
		ImageView selfIv = (ImageView) findViewById(R.id.phone_study_self);
		ImageView studyIv = (ImageView) findViewById(R.id.phone_study_study);
		ImageView sayIv = (ImageView) findViewById(R.id.phone_study_say);
		headIv.setOnClickListener(this);
		newIv.setOnClickListener(this);
		nominateIv.setOnClickListener(this);
		freeIv.setOnClickListener(this);
		kindIv.setOnClickListener(this);
		selfIv.setOnClickListener(this);
		studyIv.setOnClickListener(this);
		sayIv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.phone_study:
			intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
			break;
		case R.id.phone_study_downmanager:
			intent = new Intent(this, PreloadActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_new:
			intent = new Intent(this, HomePageActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_nominate:
			intent = new Intent(this, NominateActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_free:
			intent = new Intent(this, FreeActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_kind:
			intent = new Intent(this, KindsActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_self:
			intent = new Intent(this, SelfSelectCourseActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_study:
			intent = new Intent(this, PlayRecordCourseActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_refresh:
			intent = new Intent(this, SayGroupListActivity.class);
			startActivity(intent);
			break;
		}
	}
}