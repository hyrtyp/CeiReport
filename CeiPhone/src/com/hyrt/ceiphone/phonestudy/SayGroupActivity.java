package com.hyrt.ceiphone.phonestudy;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.Forum;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.adapter.PhoneStudySayGroupAdapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SayGroupActivity extends FoundationActivity {

	List<Forum> says;
	private Courseware courseware;
	private AsyncImageLoader asyncImageLoader;
	private ColumnEntry columnEntry;
	private static final int NO_NET = 1;
	private static final int DATA_READY = 2;
	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.arg1) {
			case NO_NET:
				MyTools.exitShow(SayGroupActivity.this, ((Activity)SayGroupActivity.this).getWindow().getDecorView(), "网络有问题！");
				break;
			case DATA_READY:
				ListView listView = (ListView) findViewById(R.id.phone_study_saygroup_listview);
				List<Forum> forums = new ArrayList<Forum>();
				Forum firstForum = null;
				for (int i = 0; i < says.size(); i++) {
					if (says.get(i).getSerial().equals("1")) {
						firstForum = says.get(i);
						forums.add(firstForum);
					} else if (firstForum != null) {
						firstForum.getBelowForums().add(says.get(i));
					}
				}
				((TextView)findViewById(R.id.phone_study_saygroup_num)).setText("全部评论（"+says.size()+"）条") ;
				listView.setAdapter(new PhoneStudySayGroupAdapter(SayGroupActivity.this, forums));
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_say_group);
		this.CURRENT_KEY = SAYGROUP_DATA_KEY;
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);    
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
		initData();
	}

	private void initData() {
		courseware = (Courseware) getIntent().getSerializableExtra("coursewareinfo");
		asyncImageLoader = ((CeiApplication) (getApplication())).asyncImageLoader;
		columnEntry = ((CeiApplication) (getApplication())).columnEntry;
		final ImageView courseIcon = (ImageView) findViewById(R.id.phone_study_detail_icon);
		ImageResourse imageResource = new ImageResourse();
		imageResource.setIconUrl(courseware.getSmallPath());
		imageResource.setIconId(courseware.getClassId());
		asyncImageLoader.loadDrawable(imageResource,
				new AsyncImageLoader.ImageCallback() {

					@Override
					public void imageLoaded(Drawable drawable, String path) {
						if (drawable != null) {
							courseIcon.setImageDrawable(drawable);
						}
					}
				});
		TextView courseTitle = (TextView) findViewById(R.id.phone_study_detail_title);
		courseTitle.setText(courseware.getName());
		TextView courseAuthor = (TextView) findViewById(R.id.phone_study_detail_author);
		courseAuthor.setText(courseware.getTeacherName());
		TextView courseProTime = (TextView) findViewById(R.id.phone_study_detail_protime);
		courseProTime.setText(courseware.getProTime());
		says = new ArrayList<Forum>();
		new Thread(new Runnable() {

			@Override
			public void run() {
				String retData = Service.queryBBSFollow(new String[] {
						courseware.getClassId(),columnEntry.getColumnEntryChilds().get(0).getId()});
				Message message = handler.obtainMessage();
				if (XmlUtil.parseReturnCode(retData).equals("-1")) {
					message.arg1 = NO_NET;
				} else {
					message.arg1 = DATA_READY;
					try {
						says = XmlUtil.parseForumInfo(retData,
								courseware.getClassId(),
								columnEntry.getUserId(), columnEntry
										.getColumnEntryChilds().get(0).getId());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				handler.sendMessage(message);
			}
		}).start();

	}
}
