package com.hyrt.cei.ui.phonestudy;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.PhoneStudyKindsAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ClassType;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.MenuNode;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class KindsActivity extends Activity implements OnClickListener {

	private LinearLayout secondMenu;
	private LinearLayout thirdMenu;
	private List<MenuNode> menuNodes;
	private LinearLayout root;
	private LayoutInflater inflater;
	private ListView lv;
	private List<Courseware> coursewares = new ArrayList<Courseware>();
	private static final int LVDATA_KEY = 1;
	private static final int NO_NET = 2;
	private static final int MORE_DATA = 3;
	private static final int HIDE_MORE = 4;
	private static final int NO_KINDS = 5;
	private List<ClassType> classTypes = new ArrayList<ClassType>();
	private int pageSize = 20;
	private String freeClassId;
	// 用户名
	private String loginName;

	// 根据数据添加一级栏目
	private void initData() {
		final Handler handler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				if(msg.arg1 == NO_KINDS || classTypes.size() == 0){
					MyTools.exitShow(KindsActivity.this,KindsActivity.this.getWindow().getDecorView(),"暂无分类！");
					findViewById(R.id.phone_study_progressLl).setVisibility(View.GONE);
					return;
				}
				menuNodes = new ArrayList<MenuNode>();
				String rootId = classTypes.get(0).getClassId();
				classTypes.remove(0);
				for(int i=0;i<classTypes.size();i++){
					if(classTypes.get(i).getContent().equals("免费课件")){
						freeClassId = classTypes.get(i).getClassId();
					}
				}
				for (int i = 0; i < classTypes.size(); i++) {
					if (!classTypes.get(i).getParentId().equals(rootId))
						continue;
					MenuNode menuNodeChilds = new MenuNode();
					menuNodeChilds.setId(classTypes.get(i).getClassId());
					menuNodeChilds.setContent(classTypes.get(i).getContent());
					for (int j = 0; j < classTypes.size(); j++) {
						if (classTypes.get(j).getParentId()
								.equals(menuNodeChilds.getId())) {
							MenuNode menuNodeChildChilds = new MenuNode();
							menuNodeChildChilds.setId(classTypes.get(j)
									.getClassId());
							menuNodeChildChilds.setContent(classTypes.get(j)
									.getContent());
							menuNodeChilds.getMenuNodeChilds().add(
									menuNodeChildChilds);
							for (int x = 0; x < classTypes.size(); x++) {
								if (classTypes.get(x).getParentId()
										.equals(menuNodeChildChilds.getId())) {
									MenuNode menuNodeChildChildCild = new MenuNode();
									menuNodeChildChildCild.setId(classTypes.get(x).getClassId());
									menuNodeChildChildCild
											.setContent(classTypes.get(x)
													.getContent());
									menuNodeChildChilds.getMenuNodeChilds()
											.add(menuNodeChildChildCild);
								}
							}
						}
					}
					menuNodes.add(menuNodeChilds);
				}
				
				if(menuNodes.get(0).getMenuNodeChilds().size() != 0 && menuNodes.get(0).getMenuNodeChilds().get(0).getMenuNodeChilds().size() != 0 &&
						menuNodes.get(0).getMenuNodeChilds().get(0).getMenuNodeChilds().get(0) != null)
					initLvData(menuNodes.get(0).getMenuNodeChilds().get(0).getMenuNodeChilds().get(0).getId());
				else if(menuNodes.get(0).getMenuNodeChilds().size() != 0 && menuNodes.get(0).getMenuNodeChilds().get(0) != null)
					initLvData(menuNodes.get(0).getMenuNodeChilds().get(0).getId());
				else if(menuNodes.get(0) != null)
					initLvData(menuNodes.get(0).getId());
				initTreeView();
				try{
					loadFirstMenu();
				}catch(Exception e){
					e.printStackTrace();
				}
				root.getChildAt(0).setBackgroundColor(Color.WHITE);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (((CeiApplication) getApplication()).isNet()) {
					ColumnEntry columnEntry = ((CeiApplication) (KindsActivity.this
							.getApplication())).columnEntry;
					ColumnEntry phoneStudyCol = columnEntry.getColByName(HomePageActivity.MODEL_NAME);
					StringBuilder functionIds = new StringBuilder(phoneStudyCol.getId());
					for (int i = 0; i < columnEntry.getColumnEntryChilds()
							.size(); i++) {
						ColumnEntry entryChild = columnEntry
								.getColumnEntryChilds().get(i);
						if (entryChild.getPath() != null
								&& entryChild.getPath().contains(
										phoneStudyCol.getId())) {
							functionIds.append("," + entryChild.getId());
						}
					}
					String result = Service.queryClassByType(functionIds
							.toString());
					if(XmlUtil.parseReturnCode(result).equals("5")){
					    Message message = handler.obtainMessage();
					    message.arg1 = NO_KINDS;
						handler.sendMessage(message);
						return;
					}	
					XmlUtil.parseClassType(result, classTypes);
					((CeiApplication) (KindsActivity.this.getApplication())).dataHelper.saveClassType(classTypes);
				} else {
					classTypes = ((CeiApplication) (KindsActivity.this
							.getApplication())).dataHelper.getClassTypes();
				}
				handler.sendMessage(handler.obtainMessage());
			}
		}).start();
	}

	private void initTreeView() {
		addFirstMenu();
		// 获取根节点的子元素
		for (int i = 0; i < root.getChildCount(); i++) {
			final int currentIndex = i;
			LinearLayout child = (LinearLayout) root.getChildAt(i);
			// 获取一级元素并设置事件
			child.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// 移除二级元素和三级元素
					root.removeView(secondMenu);
					if (thirdMenu.getParent() != null)
						((LinearLayout) thirdMenu.getParent())
								.removeView(thirdMenu);
					// 初始化二级栏目
					secondMenu.removeAllViews();
					MenuNode selectedFirstMenuNode = menuNodes
							.get(currentIndex);
					for (int i = 0; i < selectedFirstMenuNode
							.getMenuNodeChilds().size(); i++) {
						String selectedMenuContent = selectedFirstMenuNode
								.getMenuNodeChilds().get(i).getContent();
						LinearLayout secondMenuItem = (LinearLayout) inflater
								.inflate(
										R.layout.phone_study_kinds_second_menu_item,
										null);
						((TextView) secondMenuItem.getChildAt(1))
								.setText(selectedMenuContent);
						secondMenu.addView(secondMenuItem, i);
					}
					// 初始化一级栏目
					for (int i = 0; i < root.getChildCount(); i++) {
						LinearLayout child = (LinearLayout) root.getChildAt(i);
						if (i == currentIndex) {
							child.getChildAt(1)
									.setBackgroundResource(
											R.drawable.phone_study_kind_first_menu_icon_select);
							child.setBackgroundResource(R.drawable.phone_study_kinds_menu_bg);
						} else {
							child.getChildAt(1)
									.setBackgroundResource(
											R.drawable.phone_study_kind_first_menu_icon);
							child.setBackgroundDrawable(null);
						}
					}
					// 获取二级元素并设置事件
					for (int i = 0; i < secondMenu.getChildCount(); i++) {
						final int currentIndexSecon = i;
						final LinearLayout child = (LinearLayout) secondMenu.getChildAt(i);
						child.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// 移除三级元素并将三字元素加到相应的位置上
								if (thirdMenu.getParent() != null)
									((LinearLayout) thirdMenu.getParent())
											.removeView(thirdMenu);
								thirdMenu.setVisibility(View.VISIBLE);
								// 初始化二级栏目
								for (int i = 0; i < secondMenu.getChildCount(); i++) {
									LinearLayout child = (LinearLayout) secondMenu
											.getChildAt(i);
									if (i == currentIndexSecon) {
										child.getChildAt(0)
												.setBackgroundResource(
														R.drawable.phone_study_kind_second_menu_icon_select);
										((TextView) child.getChildAt(1))
												.setTextColor(Color.BLACK);
									} else {
										child.getChildAt(0)
												.setBackgroundResource(
														R.drawable.phone_study_kind_second_menu_icon);
										((TextView) child.getChildAt(1))
												.setTextColor(Color.WHITE);
									}
								}
								// 清空当前三级栏目然后根据数据增加条目
								final List<MenuNode> thirdMenuNodes = menuNodes
										.get(currentIndex).getMenuNodeChilds()
										.get(currentIndexSecon)
										.getMenuNodeChilds();
								if (thirdMenuNodes.size() != 0) {
									thirdMenu.removeAllViews();
								} else {
									String name = ((TextView)child.getChildAt(1)).getText().toString();
									for(int z=0;z<classTypes.size();z++){
										if(classTypes.get(z).getContent().equals(name))
											initLvData(classTypes.get(z).getClassId());
									}
								}
								for (int i = 0; i < thirdMenuNodes.size(); i++) {
									final int j = i;
									String thirdMenuItemContent = thirdMenuNodes
											.get(i).getContent();
									final LinearLayout thirdMenuItemLl = (LinearLayout) inflater
											.inflate(
													R.layout.phone_study_kinds_third_menu_item,
													null);
									((TextView) thirdMenuItemLl.getChildAt(1))
											.setText(thirdMenuItemContent);
									thirdMenuItemLl
											.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													for (int i = 0; i < thirdMenu
															.getChildCount(); i++) {
														LinearLayout thirdMenuItem = (LinearLayout) thirdMenu
																.getChildAt(i);
														if (j == i)
															((TextView) thirdMenuItem
																	.getChildAt(1))
																	.setTextColor(Color.YELLOW);
														else
															((TextView) thirdMenuItem
																	.getChildAt(1))
																	.setTextColor(Color.WHITE);
														String name = ((TextView)thirdMenuItemLl.getChildAt(1)).getText().toString();
														for(int z=0;z<classTypes.size();z++){
															if(classTypes.get(z).getContent().equals(name))
																initLvData(classTypes.get(z).getClassId());
														}
													}

												}
											});
									thirdMenu.addView(thirdMenuItemLl, i);
								}
								if (thirdMenuNodes.size() != 0 && thirdMenu.getParent() == null)
									secondMenu.addView(thirdMenu,currentIndexSecon + 1);
							}
						});
					}
					// 将二级元素添加到相应的位置
					secondMenu.setVisibility(View.VISIBLE);
					root.addView(secondMenu, currentIndex + 1);
					Animation animation = AnimationUtils.loadAnimation(
							KindsActivity.this, R.anim.scale);
					secondMenu.startAnimation(animation);
				}
			});
		}
		findViewById(R.id.phone_study_progressLl).setVisibility(View.GONE);
	}

	// 根据数据添加一级元素
	private void addFirstMenu() {
		secondMenu = (LinearLayout) findViewById(R.id.phone_study_kinds_second_menu);
		thirdMenu = (LinearLayout) findViewById(R.id.phone_study_kinds_third_menu);
		root = (LinearLayout) findViewById(R.id.phone_study_kinds_first_menu);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < menuNodes.size(); i++) {
			String firstMenuContent = menuNodes.get(i).getContent();
			LinearLayout firstMenuItemLl = (LinearLayout) inflater.inflate(
					R.layout.phone_study_kinds_first_menu_item, null);
			((TextView) firstMenuItemLl.getChildAt(0))
					.setText(firstMenuContent);
			root.addView(firstMenuItemLl, i);
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_kinds);
		SharedPreferences settings = getSharedPreferences("loginInfo",Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		initBottom();
		for (int i = 0; i < HomePageActivity.phoneStudyContainer.size(); i++) {
			try {
				KindsActivity isKindsActivity = (KindsActivity) (HomePageActivity.phoneStudyContainer
						.get(i));
				isKindsActivity.finish();
			} catch (Exception e) {
			}
		}
		HomePageActivity.phoneStudyContainer.add(this);
		findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				KindsActivity.this.finish();
			}
		});
		findViewById(R.id.phone_study_search_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(KindsActivity.this,
								SearchCourseActivity.class);
						startActivity(intent);
					}
				});
		findViewById(R.id.phone_study_refresh).setOnClickListener(this);
		findViewById(R.id.phone_study_downmanager).setOnClickListener(this);
		findViewById(R.id.phone_study_progressLl).setVisibility(View.VISIBLE);
		lv = (ListView) findViewById(R.id.phone_study_kinds_listview);
		footer = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.phone_study_listview_bottom, null);
		footer.findViewById(R.id.phone_study_morebtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						index++;
						new Thread(new Runnable(){

						@Override
						public void run() {
							List<Courseware> selfselCourseware = new ArrayList<Courseware>();
							List<Courseware> moreCoursewares = new ArrayList<Courseware>();
							String result = Service.queryClassTypeByClass(currentFunctionId,index);
							if (XmlUtil.parseReturnCode(result).equals("")) {
								XmlUtil.parseCoursewares(result, moreCoursewares);
								result = Service
										.queryCourse(((CeiApplication) (getApplication())).columnEntry
												.getUserId());
								XmlUtil.parseCoursewares(result, selfselCourseware);
								for (int i = 0; i < moreCoursewares.size(); i++) {
									for (int j = 0; j < selfselCourseware.size(); j++) {
										if (moreCoursewares
												.get(i)
												.getClassId()
												.equals(selfselCourseware.get(j)
														.getClassId())) {
											moreCoursewares.get(i).setSelfCourse(true);
										}
									}
									moreCoursewares.get(i).setParentId(currentFunctionId);
									((CeiApplication) getApplication()).dataHelper.saveCourseware(courses.get(i));
								}
								courses.addAll(moreCoursewares);
								if(moreCoursewares.size() < 20){
									Message msg = handler.obtainMessage();
									msg.arg1 = HIDE_MORE;
									handler.sendMessage(msg);
								}
								Message msg = handler.obtainMessage();
								msg.arg1 = MORE_DATA;
								handler.sendMessage(msg);
							} else {
								Message msg = handler.obtainMessage();
								msg.arg1 = NO_NET;
								handler.sendMessage(msg);
							}
						}}).start();
					}
				});
		lv.addFooterView(footer);
		initData();
	}

	//课件列表的适配器
	private PhoneStudyKindsAdapter phoneStudyKindsAdapter;
	//每类下的课件
	private List<Courseware> courses = new ArrayList<Courseware>();
	// 当前查询的业务课件id
	private String oldFunctionId = "";
	// 当前查询的业务课件id
	private String currentFunctionId;
	// 当前页码
	private int index = 0;
	//底部更多分页
	private LinearLayout footer;
	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.arg1) {
			case LVDATA_KEY:
				for (int i = (index-1) * pageSize; i < index * pageSize
						&& i < courses.size(); i++) {
					coursewares.add(courses.get(i));
				}
				if(courses.size() < 10)
					footer.setVisibility(View.GONE);
				else
					footer.setVisibility(View.VISIBLE);
				phoneStudyKindsAdapter = new PhoneStudyKindsAdapter(KindsActivity.this,
						R.layout.phone_study_kinds_listview_item, coursewares,lv);
				lv.setAdapter(phoneStudyKindsAdapter);
				break;
			case NO_NET:
				MyTools.exitShow(KindsActivity.this, ((Activity)KindsActivity.this).getWindow().getDecorView(),  "网络有问题!");
				break;
			case MORE_DATA:
				for (int i = (index-1) * pageSize; i < index * pageSize
						&& i < courses.size(); i++) {
					System.out.println(i);
					coursewares.add(courses.get(i));
				}
				phoneStudyKindsAdapter.notifyDataSetChanged();
				break;
			case HIDE_MORE:
				footer.setVisibility(View.GONE);
				break;
				
			}

		}
	};

	private void initLvData(final String functionId) {
		currentFunctionId = functionId;
		if (oldFunctionId.equals(currentFunctionId))
			return;
		index = 1;
		coursewares.clear();
		courses.clear();
		oldFunctionId = currentFunctionId;
		new Thread(new Runnable() {
			public void run() {
				List<Courseware> selfselCourseware = new ArrayList<Courseware>();
				if (((CeiApplication) getApplication()).isNet()) {
					boolean isFree = false;
					String result = Service.queryClassTypeByClass(functionId,index);
					for(int i=0;i<classTypes.size();i++){
						if(classTypes.get(i).getClassId().equals(functionId)){
							if(classTypes.get(i).getParentId().equals(freeClassId)){
								isFree = true;
							}
						}
					}
					if (XmlUtil.parseReturnCode(result).equals("")) {
						XmlUtil.parseCoursewares(result, courses);
						result = Service.queryCourse(((CeiApplication) (getApplication())).columnEntry.getUserId());
						XmlUtil.parseCoursewares(result, selfselCourseware);
						for (int i = 0; i < courses.size(); i++) {
							try{
							for (int j = 0; j < selfselCourseware.size(); j++) {
								if (courses
										.get(i)
										.getClassId()
										.equals(selfselCourseware.get(j)
												.getClassId())) {
									courses.get(i).setSelfCourse(true);
								}
							}
							}catch(Exception e){
								e.printStackTrace();
							}
							courses.get(i).setParentId(functionId);
							if(isFree)
								courses.get(i).setFree(true);
							((CeiApplication) getApplication()).dataHelper.saveCourseware(courses.get(i));
						}
						Message msg = handler.obtainMessage();
						msg.arg1 = LVDATA_KEY;
						handler.sendMessage(msg);
					} else {
						Message msg = handler.obtainMessage();
						msg.arg1 = NO_NET;
						handler.sendMessage(msg);
					}
				} else {
					Courseware courseware = new Courseware();
					courseware.setParentId(functionId);
					courses = ((CeiApplication) getApplication()).dataHelper.getCoursewares(courseware);
					Message msg = handler.obtainMessage();
					msg.arg1 = LVDATA_KEY;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		HomePageActivity.phoneStudyContainer.remove(this);
		super.onDestroy();
	}
	
	private void initBottom(){
		ImageView headIv = (ImageView)findViewById(R.id.phone_study);
		ImageView newIv = (ImageView)findViewById(R.id.phone_study_new);
		ImageView nominateIv = (ImageView)findViewById(R.id.phone_study_nominate);
		ImageView freeIv = (ImageView)findViewById(R.id.phone_study_free);
		ImageView kindIv = (ImageView)findViewById(R.id.phone_study_kind);
		ImageView selfIv = (ImageView)findViewById(R.id.phone_study_self);
		ImageView studyIv = (ImageView)findViewById(R.id.phone_study_study);
		ImageView sayIv = (ImageView)findViewById(R.id.phone_study_say);
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
		case R.id.phone_study :
			intent = new Intent(this,HomePageDZB.class);
			startActivity(intent);
			break;
		case R.id.phone_study_downmanager:
			intent = new Intent(this,
					PreloadActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_new :
			intent = new Intent(this,HomePageActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_nominate :
			intent = new Intent(this,NominateActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_free :
			intent = new Intent(this,FreeActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_refresh :
			intent = new Intent(this,KindsActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_self :
			intent = new Intent(this,SelfSelectCourseActivity.class);
			if (!loginName.equals(""))
				startActivity(intent);
			else
				MyTools.exitShow(this, ((Activity)this).getWindow().getDecorView(), "请登陆后查看！");
			break;
		case R.id.phone_study_study :
			intent = new Intent(this,PlayRecordCourseActivity.class);
			if (!loginName.equals(""))
				startActivity(intent);
			else
				MyTools.exitShow(this, ((Activity)this).getWindow().getDecorView(), "请登陆后查看！");
			break;
		case R.id.phone_study_say :
			intent = new Intent(this,SayGroupListActivity.class);
			if (!loginName.equals(""))
				startActivity(intent);
			else
				MyTools.exitShow(this, ((Activity)this).getWindow().getDecorView(), "请登陆后查看！");
			break;
		}
	}
	
	private void loadFirstMenu(){
				// 移除二级元素和三级元素
				root.removeView(secondMenu);
				if (thirdMenu.getParent() != null)
					((LinearLayout) thirdMenu.getParent()).removeView(thirdMenu);
				// 初始化二级栏目
				secondMenu.removeAllViews();
				MenuNode selectedFirstMenuNode = menuNodes.get(0);
				for (int i = 0; i < selectedFirstMenuNode.getMenuNodeChilds().size(); i++) {
					String selectedMenuContent = selectedFirstMenuNode.getMenuNodeChilds().get(i).getContent();
					LinearLayout secondMenuItem = (LinearLayout) inflater.inflate(R.layout.phone_study_kinds_second_menu_item,null);
					((TextView) secondMenuItem.getChildAt(1)).setText(selectedMenuContent);
					secondMenu.addView(secondMenuItem, i);
				}
				// 初始化一级栏目
				for (int i = 0; i < root.getChildCount(); i++) {
					LinearLayout child = (LinearLayout) root.getChildAt(i);
					if (i == 0) {
						child.getChildAt(1)
								.setBackgroundResource(
										R.drawable.phone_study_kind_first_menu_icon_select);
						child.setBackgroundResource(R.drawable.phone_study_kinds_menu_bg);
					} else {
						child.getChildAt(1)
								.setBackgroundResource(
										R.drawable.phone_study_kind_first_menu_icon);
						child.setBackgroundDrawable(null);
					}
				}
				// 获取二级元素并设置事件
				for (int i = 0; i < secondMenu.getChildCount(); i++) {
					final int currentIndexSecon = i;
					final LinearLayout child = (LinearLayout) secondMenu.getChildAt(i);
					child.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 移除三级元素并将三字元素加到相应的位置上
							if (thirdMenu.getParent() != null)
								((LinearLayout) thirdMenu.getParent())
										.removeView(thirdMenu);
							thirdMenu.setVisibility(View.VISIBLE);
							// 初始化二级栏目
							for (int i = 0; i < secondMenu.getChildCount(); i++) {
								LinearLayout child = (LinearLayout) secondMenu
										.getChildAt(i);
								if (i == currentIndexSecon) {
									child.getChildAt(0)
											.setBackgroundResource(
													R.drawable.phone_study_kind_second_menu_icon_select);
									((TextView) child.getChildAt(1))
											.setTextColor(Color.BLACK);
								} else {
									child.getChildAt(0)
											.setBackgroundResource(
													R.drawable.phone_study_kind_second_menu_icon);
									((TextView) child.getChildAt(1))
											.setTextColor(Color.WHITE);
								}
							}
							// 清空当前三级栏目然后根据数据增加条目
							final List<MenuNode> thirdMenuNodes = menuNodes.get(0).getMenuNodeChilds().get(currentIndexSecon).getMenuNodeChilds();
							if (thirdMenuNodes.size() != 0) {
								thirdMenu.removeAllViews();
							} else {
								String name = ((TextView)child.getChildAt(1)).getText().toString();
								for(int z=0;z<classTypes.size();z++){
									if(classTypes.get(z).getContent().equals(name))
										initLvData(classTypes.get(z).getClassId());
								}
							}
							for (int i = 0; i < thirdMenuNodes.size(); i++) {
								final int j = i;
								String thirdMenuItemContent = thirdMenuNodes.get(i).getContent();
								final LinearLayout thirdMenuItemLl = (LinearLayout) inflater.inflate(R.layout.phone_study_kinds_third_menu_item,null);
								((TextView) thirdMenuItemLl.getChildAt(1)).setText(thirdMenuItemContent);
								thirdMenuItemLl.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												for (int i = 0; i < thirdMenu
														.getChildCount(); i++) {
													LinearLayout thirdMenuItem = (LinearLayout) thirdMenu
															.getChildAt(i);
													if (j == i)
														((TextView) thirdMenuItem
																.getChildAt(1))
																.setTextColor(Color.YELLOW);
													else
														((TextView) thirdMenuItem
																.getChildAt(1))
																.setTextColor(Color.WHITE);
													String name = ((TextView)thirdMenuItemLl.getChildAt(1)).getText().toString();
													for(int z=0;z<classTypes.size();z++){
														if(classTypes.get(z).getContent().equals(name))
															initLvData(classTypes.get(z).getClassId());
													}
												}

											}
										});
								thirdMenu.addView(thirdMenuItemLl, i);
							}
							if (thirdMenuNodes.size() != 0 && thirdMenu.getParent() == null)
								secondMenu.addView(thirdMenu,currentIndexSecon + 1);
						}
					});
				}
				// 将二级元素添加到相应的位置
				secondMenu.setVisibility(View.VISIBLE);
				root.addView(secondMenu, 0 + 1);
				
				// 移除三级元素并将三字元素加到相应的位置上
				if (thirdMenu.getParent() != null)
					((LinearLayout) thirdMenu.getParent()).removeView(thirdMenu);
				thirdMenu.setVisibility(View.VISIBLE);
				// 初始化二级栏目
				for (int i = 0; i < secondMenu.getChildCount(); i++) {
					LinearLayout child = (LinearLayout) secondMenu
							.getChildAt(i);
					if (i == 0) {
						child.getChildAt(0)
								.setBackgroundResource(
										R.drawable.phone_study_kind_second_menu_icon_select);
						((TextView) child.getChildAt(1))
								.setTextColor(Color.BLACK);
					} else {
						child.getChildAt(0)
								.setBackgroundResource(
										R.drawable.phone_study_kind_second_menu_icon);
						((TextView) child.getChildAt(1))
								.setTextColor(Color.WHITE);
					}
				}
				// 清空当前三级栏目然后根据数据增加条目
				final List<MenuNode> thirdMenuNodes = menuNodes.get(0).getMenuNodeChilds().get(0).getMenuNodeChilds();
				if (thirdMenuNodes.size() != 0) {
					thirdMenu.removeAllViews();
				} else {
					String name = ((TextView) ((LinearLayout)(secondMenu.getChildAt(0))).getChildAt(1)).getText().toString();
					for(int z=0;z<classTypes.size();z++){
						if(classTypes.get(z).getContent().equals(name))
							initLvData(classTypes.get(z).getClassId());
					}
				}
				for (int i = 0; i < thirdMenuNodes.size(); i++) {
					final int j = i;
					String thirdMenuItemContent = thirdMenuNodes.get(i).getContent();
					final LinearLayout thirdMenuItemLl = (LinearLayout) inflater.inflate(R.layout.phone_study_kinds_third_menu_item,null);
					((TextView) thirdMenuItemLl.getChildAt(1)).setText(thirdMenuItemContent);
					thirdMenuItemLl.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									for (int i = 0; i < thirdMenu
											.getChildCount(); i++) {
										LinearLayout thirdMenuItem = (LinearLayout) thirdMenu
												.getChildAt(i);
										if (j == i)
											((TextView) thirdMenuItem
													.getChildAt(1))
													.setTextColor(Color.YELLOW);
										else
											((TextView) thirdMenuItem
													.getChildAt(1))
													.setTextColor(Color.WHITE);
										String name = ((TextView)thirdMenuItemLl.getChildAt(1)).getText().toString();
										for(int z=0;z<classTypes.size();z++){
											if(classTypes.get(z).getContent().equals(name))
												initLvData(classTypes.get(z).getClassId());
										}
									}

								}
							});
					thirdMenu.addView(thirdMenuItemLl, i);
				}
				if (thirdMenuNodes.size() != 0 && thirdMenu.getParent() == null)
					secondMenu.addView(thirdMenu,0 + 1);
				LinearLayout thirdMenuItem = (LinearLayout) thirdMenu.getChildAt(0);
				((TextView) thirdMenuItem.getChildAt(1)).setTextColor(Color.YELLOW);
	}
	
	
}