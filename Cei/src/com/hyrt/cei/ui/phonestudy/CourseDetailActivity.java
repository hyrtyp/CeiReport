package com.hyrt.cei.ui.phonestudy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.ui.common.WebViewUtil;
import com.hyrt.cei.ui.phonestudy.adapter.CourseDetailAboutAdapter;
import com.hyrt.cei.ui.phonestudy.view.GGridView;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.ImageUtil;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.UIHelper;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.Forum;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Preload;
import com.hyrt.cei.webservice.service.Service;
//import com.weibo.sdk.android.demo.MainActivity;

public class CourseDetailActivity extends Activity implements OnClickListener {

	private Courseware courseware;
	private AsyncImageLoader asyncImageLoader;
	private ColumnEntry columnEntry;
	private static final int NO_NET = 1;
	private static final int SAY_SUCCESS = 2;
	private static final int ADD_SUCCESS = 3;
	private static final int CANCEL_SUCCESS = 4;
	private static final int ABOUT_CLASS = 5;
	private ImageView controSelClass;
	public static final String FLASH_GATE = "/apad.html";
	public static final String FLASH_POSTFIX = ".zip";
	private DataHelper dataHelper;
	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			AlertDialog.Builder builder = new Builder(CourseDetailActivity.this);
			switch (msg.arg1) {
			case NO_NET:
				builder.setMessage("请登录后再进行此操作！");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				//builder.create().show();
				break;
			case SAY_SUCCESS:
				findViewById(R.id.phone_study_submit).setVisibility(
						View.VISIBLE);
				findViewById(R.id.phone_study_submit_progress).setVisibility(
						View.GONE);
				builder.setMessage("评论成功 ！");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				((EditText) findViewById(R.id.phone_study_detail_say_inputEdit)).setText("");
				builder.create().show();
				break;
			case ADD_SUCCESS:
				controSelClass
						.setImageResource(R.drawable.phone_study_canclecourse_btn);
				builder.setMessage("加入自选课成功 ！");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				//builder.create().show();
				controSelClass.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertIsSurePop(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								new Thread(new Runnable() {

									@Override
									public void run() {
										String rs = Service.cancelCourse(
												courseware.getClassId(),
												columnEntry.getUserId());
										if (XmlUtil.parseReturnCode(rs).equals(
												"1")) {
											Message message = handler
													.obtainMessage();
											message.arg1 = CANCEL_SUCCESS;
											handler.sendMessage(message);
										} else {
											Message message = handler
													.obtainMessage();
											message.arg1 = NO_NET;
											handler.sendMessage(message);
										}
									}
								}).start();
							}
						},true);
					}

				});
				break;
			case CANCEL_SUCCESS:
				controSelClass
						.setImageResource(R.drawable.phone_study_addcourse_btn);
				builder.setMessage("取消自选课成功 ！");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				//builder.create().show();
				controSelClass.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						alertIsSurePop(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								new Thread(new Runnable() {

									@Override
									public void run() {
										String rs = Service.saveCourse(
												courseware.getClassId(),
												columnEntry.getUserId());
										if (XmlUtil.parseReturnCode(rs)
												.length() > 5) {
											Message message = handler
													.obtainMessage();
											message.arg1 = ADD_SUCCESS;
											handler.sendMessage(message);
										} else {
											Message message = handler
													.obtainMessage();
											message.arg1 = NO_NET;
											handler.sendMessage(message);
										}
									}
								}).start();
							}
						},true);
					}
				});
				break;
			case ABOUT_CLASS:
				for(int i=0;i<aboutCourseware.size();i++){
					if(aboutCourseware.get(i).getClassLevel().equals(courseware.getClassLevel())){
						aboutCourseware.remove(i);
					}else{
						int isrepeat = 0;
						for(int j=0;j<aboutCourseware.size();j++){
							if(aboutCourseware.get(j).getClassLevel().equals(aboutCourseware.get(i).getClassLevel())){
								isrepeat++;
								if(isrepeat == 2)
									aboutCourseware.remove(j);
							}
						}
					}
				}
				CourseDetailAboutAdapter aboutAdapter = new CourseDetailAboutAdapter(
						CourseDetailActivity.this, aboutCourseware, gridView);
				gridView.setAdapter(aboutAdapter, 5);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_detail);
		asyncImageLoader = ((CeiApplication) (getApplication())).asyncImageLoader;
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		dataHelper = ((CeiApplication) getApplication()).dataHelper;
		loadDataForView();
		loadAboutClass();
		registEvent();
	}

	// 相关课程列表
	private List<Courseware> aboutCourseware = new ArrayList<Courseware>();
	// 相关课程横向列表控件
	private GGridView gridView;

	/**
	 * 加载课件相关课程列表
	 */
	private void loadAboutClass() {
		gridView = (GGridView) findViewById(R.id.phone_study_gridview);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 点击相关课程进入详细信息
				CourseDetailActivity.this.finish();
				Intent intent = new Intent(CourseDetailActivity.this,
						CourseDetailActivity.class);
				intent.putExtra("coursewareInfo", aboutCourseware.get(position));
				startActivity(intent);
			}
		});
		new Thread(new Runnable() {

			private StringBuilder functionIds;

			// 初始化请求数据
			private void initSendData() {
				ColumnEntry columnEntry = ((CeiApplication) (CourseDetailActivity.this
						.getApplication())).columnEntry;
				ColumnEntry phoneStudyCol = columnEntry
						.getColByName(HomePageActivity.MODEL_NAME);
				functionIds = new StringBuilder(phoneStudyCol.getId());
				for (int i = 0; i < columnEntry.getColumnEntryChilds().size(); i++) {
					ColumnEntry entryChild = columnEntry.getColumnEntryChilds()
							.get(i);
					if (entryChild.getPath() != null
							&& entryChild.getPath().contains(
									phoneStudyCol.getId())) {
						functionIds.append("," + entryChild.getId());
					}
				}
			}

			@Override
			public void run() {
				if(courseware.getName() != null && !courseware.getName().contains("["))
					return;
				String name = courseware.getName().substring(0, courseware.getName().lastIndexOf("["));
				// 获取该课程相关课程
				initSendData();
				if (((CeiApplication) getApplication()).isNet()) {
					String result = Service.queryClassName(name, functionIds.toString());
					XmlUtil.parseCoursewares(result, aboutCourseware);
					for (int i = 0; i < aboutCourseware.size(); i++) {
						if (aboutCourseware.get(i).getClassId()
								.equals(courseware.getClassId())) {
							aboutCourseware.remove(i);
						} else {
							((CeiApplication) getApplication()).dataHelper
									.saveCourseware(aboutCourseware.get(i));
						}
					}
				} else {
					Courseware courseware = new Courseware();
					courseware.setName(name);
					aboutCourseware = ((CeiApplication) getApplication()).dataHelper
							.getCoursewares(courseware);
					for (int i = 0; i < aboutCourseware.size(); i++) {
						if (aboutCourseware.get(i).getClassId()
								.equals(courseware.getClassId()))
							aboutCourseware.remove(i);
					}
				}
				Message message = handler.obtainMessage();
				message.arg1 = ABOUT_CLASS;
				handler.sendMessage(message);
			}
		}).start();
	}

	private void loadDataForView() {
		// 检查sd卡是否存在不存在的话，则退出
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			MyTools.exitShow(this, ((Activity)this).getWindow().getDecorView(), "sd卡不存在！");
			this.finish();
			return;
		}
		courseware = (Courseware) getIntent().getSerializableExtra(
				"coursewareInfo");
		TextView courseDetailTv = (TextView) findViewById(R.id.phone_study_detail_content);
		((TextView) findViewById(R.id.phone_study_detail_title))
				.setText(courseware.getFullName());
		((TextView) findViewById(R.id.phone_study_detail_author))
				.setText("讲师姓名 ： " + courseware.getTeacherName());
		((TextView) findViewById(R.id.phone_study_detail_protime))
				.setText("发布时间 ： " + courseware.getProTime());
		((TextView) findViewById(R.id.phone_study_detail_timelength))
		.setText("课件时长 ： " + courseware.getClassLength());
		if(courseware.getIntro() != null)
			courseDetailTv.setText("        " + courseware.getIntro().replace("\n", ""));
		ImageResourse imageResource = new ImageResourse();
		imageResource.setIconUrl(courseware.getSmallPath());
		imageResource.setIconId(courseware.getClassId());
		asyncImageLoader.loadDrawable(imageResource,
				new AsyncImageLoader.ImageCallback() {

					@Override
					public void imageLoaded(Drawable drawable, String path) {
						ImageView imageView = (ImageView) CourseDetailActivity.this
								.findViewById(R.id.phone_study_detail_icon);
						if (drawable != null) {
							imageView.setImageDrawable(drawable);
							File file = Environment
									.getExternalStorageDirectory();
							String sdPath = file.getAbsolutePath();
							// 请保证SD卡根目录下有这张图片文件
							String imagePath = sdPath + "/" + "abc.png";
							try {
								ImageUtil.saveBytesByFile(ImageUtil
										.Bitmap2Bytes(ImageUtil
												.drawableToBitmap(drawable)),
										imagePath);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	private void registEvent() {
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.phone_study_refresh).setOnClickListener(this);
		findViewById(R.id.phone_study_downmanager).setOnClickListener(this);
		findViewById(R.id.phone_study_search_btn).setOnClickListener(this);
		findViewById(R.id.phone_study_give).setOnClickListener(this);
		findViewById(R.id.phone_study_submit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						final Forum forum = new Forum();
						forum.setClassId(courseware.getClassId());
						forum.setFunctionid(columnEntry.getColumnEntryChilds()
								.get(0).getId());
						forum.setUserid(columnEntry.getUserId());
						forum.setContent(((EditText) findViewById(R.id.phone_study_detail_say_inputEdit))
								.getText().toString());
						if(forum.getContent() != null && forum.getContent().trim().length()==0){
							MyTools.exitShow(CourseDetailActivity.this, CourseDetailActivity.this.getWindow().getDecorView(), "请输入评论内容!");
							return;
						}
						findViewById(R.id.phone_study_submit).setVisibility(View.GONE);
						findViewById(R.id.phone_study_submit_progress).setVisibility(View.VISIBLE);
						forum.setSerial("1");
						// 加到自己评论的课件列表中
						new Thread(new Runnable() {

							@Override
							public void run() {
								String ret = Service.saveBBS(forum);
								if (XmlUtil.parseReturnCode(ret).equals("-1")) {
									Message message = handler.obtainMessage();
									message.arg1 = NO_NET;
									handler.sendMessage(message);
								} else {
									Message message = handler.obtainMessage();
									message.arg1 = SAY_SUCCESS;
									handler.sendMessage(message);
								}
							}
						}).start();
					}
				});
		loadSelfCourseData();
		findViewById(R.id.phone_study_detail_play).setOnClickListener(
				new OnClickListener() {

					// 没有购买的
					private static final int NO_BUY = 0;
					// 已经购买的
					private static final int AL_BUY = 1;

					/**
					 * 检查是否有买了这个课件
					 */
					private void checkBuy() {
						if (courseware.isFree()) {
							Message message = handler.obtainMessage();
							message.arg1 = AL_BUY;
							handler.sendMessage(message);
						} else {
							new Thread(new Runnable() {

								@Override
								public void run() {
									String result = Service.queryBuyClass(
											columnEntry.getUserId(),
											courseware.getClassId());
									List<Courseware> coursewares = new ArrayList<Courseware>();
									XmlUtil.parseCoursewares(result,
											coursewares);
									Message message = handler.obtainMessage();
									if (coursewares.size() > 0) {
										message.arg1 = AL_BUY;
									} else {
										message.arg1 = NO_BUY;
									}
									handler.sendMessage(message);
								}
							}).start();
						}
					}

					// 提示是否有权限
					private Handler handler = new Handler() {
						@Override
						public void dispatchMessage(Message msg) {
							switch (msg.arg1) {
							case NO_BUY:
								if(((CeiApplication)CourseDetailActivity.this.getApplication()).isNet())
									MyTools.exitShow(CourseDetailActivity.this, CourseDetailActivity.this.getWindow().getDecorView(), "未购买该课件！");
								else
									MyTools.exitShow(CourseDetailActivity.this, CourseDetailActivity.this.getWindow().getDecorView(), "请联网查看！");
								break;
							case AL_BUY:
								Intent intent = new Intent(
										CourseDetailActivity.this,
										WebViewUtil.class);
								if (courseware.getLookPath() == null)
									courseware.setLookPath("file:///" + dataHelper.getPreload(courseware.getClassId()).getLoadLocalPath()
											.replace(
													FLASH_POSTFIX,
													FLASH_GATE));
								intent.putExtra("path",
										courseware.getLookPath());
								intent.putExtra("isRecord",getIntent().getBooleanExtra("isRecord",false));
								intent.putExtra("class", courseware);
								startActivity(intent);
								break;
							}
						}
					};

					@Override
					public void onClick(View arg0) {
						checkBuy();
					}
				});
		findViewById(R.id.phone_study_detail_preload).setOnClickListener(
				new OnClickListener() {

					// 没有购买的
					private static final int NO_BUY = 0;
					// 已经购买的
					private static final int AL_BUY = 1;

					/**
					 * 检查是否有买了这个课件
					 */
					private void checkBuy() {
						if (courseware.isFree()) {
							Message message = handler.obtainMessage();
							message.arg1 = AL_BUY;
							handler.sendMessage(message);
						} else {
							new Thread(new Runnable() {

								@Override
								public void run() {
									String result = Service.queryBuyClass(
											columnEntry.getUserId(),
											courseware.getClassId());
									List<Courseware> coursewares = new ArrayList<Courseware>();
									XmlUtil.parseCoursewares(result,
											coursewares);
									Message message = handler.obtainMessage();
									if (coursewares.size() > 0) {
										message.arg1 = AL_BUY;
									} else {
										message.arg1 = NO_BUY;
									}
									handler.sendMessage(message);
								}
							}).start();
						}
					}

					// 提示是否有权限
					private Handler handler = new Handler() {
						@Override
						public void dispatchMessage(Message msg) {
							switch (msg.arg1) {
							case NO_BUY:
								if(((CeiApplication)CourseDetailActivity.this.getApplication()).isNet())
									MyTools.exitShow(CourseDetailActivity.this, CourseDetailActivity.this.getWindow().getDecorView(), "未购买该课件！");
								else
									MyTools.exitShow(CourseDetailActivity.this, CourseDetailActivity.this.getWindow().getDecorView(), "请联网查看！");
								break;
							case AL_BUY:
								downloadThisCourse(courseware);
								break;
							}
						}
					};

					@Override
					public void onClick(View arg0) {
						checkBuy();
					}

				});
		changeDownBtn(findViewById(R.id.phone_study_detail_preload),courseware.getClassId());
	}

	private void loadSelfCourseData() {
		final Handler selfCoursehandler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				controSelClass = (ImageView) findViewById(R.id.phone_study_detail_addcourse);
				controSelClass.setVisibility(View.VISIBLE);
				if (courseware.isSelfCourse()) {
					controSelClass
							.setImageResource(R.drawable.phone_study_canclecourse_btn);
					controSelClass.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							alertIsSurePop(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									popWin.dismiss();
									new Thread(new Runnable() {

										@Override
										public void run() {
											String rs = Service.cancelCourse(
													courseware.getClassId(),
													columnEntry.getUserId());
											if (XmlUtil.parseReturnCode(rs)
													.equals("1")) {
												Message message = handler
														.obtainMessage();
												message.arg1 = CANCEL_SUCCESS;
												handler.sendMessage(message);
											} else {
												Message message = handler
														.obtainMessage();
												message.arg1 = NO_NET;
												handler.sendMessage(message);
											}
										}
									}).start();
								}
							},true);
						}
					});
				} else {
					controSelClass.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							alertIsSurePop(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									popWin.dismiss();
									new Thread(new Runnable() {

										@Override
										public void run() {
											String rs = Service.saveCourse(
													courseware.getClassId(),
													columnEntry.getUserId());
											if (XmlUtil.parseReturnCode(rs)
													.length() > 5) {
												Message message = handler
														.obtainMessage();
												message.arg1 = ADD_SUCCESS;
												handler.sendMessage(message);
											} else {
												Message message = handler
														.obtainMessage();
												message.arg1 = NO_NET;
												handler.sendMessage(message);
											}
										}
									}).start();
								}
							},true);
						}
					});
				}
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<Courseware> selfselCourseware = new ArrayList<Courseware>();
				String result = Service
						.queryCourse(((CeiApplication) (getApplication())).columnEntry
								.getUserId());
				XmlUtil.parseCoursewares(result, selfselCourseware);
				for (int i = 0; i < selfselCourseware.size(); i++) {
					if (selfselCourseware.get(i).getClassId()
							.equals(courseware.getClassId())) {
						courseware.setSelfCourse(true);
					}
				}
				selfCoursehandler.sendMessage(selfCoursehandler.obtainMessage());
			}
		}).start();
	}

	private PopupWindow popWin;

	private void alertIsSurePop(OnClickListener clickListener,boolean isCheckLogin) {
		View popView = this.getLayoutInflater().inflate(
				R.layout.phone_study_issure, null);
		if(isCheckLogin &&!((CeiApplication)this.getApplication()).isNet()){
			((TextView)popView.findViewById(R.id.issure_title)).setText("请联网操作！");
			clickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					popWin.dismiss();
				}
			};
		}else if(isCheckLogin && columnEntry.getUserId() == null){
			((TextView)popView.findViewById(R.id.issure_title)).setText("请登录操作！");
		}
		popView.findViewById(R.id.phone_study_issure_sure_btn)
				.setOnClickListener(clickListener);
		popView.findViewById(R.id.phone_study_issure_cancel_btn)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						popWin.dismiss();
					}
				});
		popWin = new PopupWindow(popView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popWin.setFocusable(true);
		popWin.showAtLocation(findViewById(R.id.full_view), Gravity.CENTER, 0,
				0);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn:
			CourseDetailActivity.this.finish();
			break;
		case R.id.phone_study_downmanager:
			intent = new Intent(this, PreloadActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_search_btn:
			intent = new Intent(this, SearchCourseActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_refresh:
			intent = new Intent(this, CourseDetailActivity.class);
			intent.putExtra("coursewareInfo", courseware);
			startActivity(intent);
			CourseDetailActivity.this.finish();
			break;
		case R.id.phone_study_give:
			//intent = new Intent(this,MainActivity.class);
			//startActivity(intent);
			break;
		}
	}

	/**
	 * 下载课件
	 * 
	 * @param courseware
	 */
	private void downloadThisCourse(final Courseware courseware) {
		alertIsSurePop(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popWin.dismiss();
				DataHelper dataHelper = ((CeiApplication) (CourseDetailActivity.this
						.getApplication())).dataHelper;
				Preload preload = new Preload();
				preload.setLoadPlayId(courseware.getClassId());
				preload.setLoadCurrentByte(0);
				preload.setLoading(1);
				preload.setLoadFinish(0);
				preload.setLoadUrl(courseware.getDownPath());
				try {
					preload.setLoadLocalPath(MyTools.RESOURCE_PATH
							+ MyTools.KJ_PARTPATH
							+ courseware
									.getDownPath()
									.substring(
											courseware
													.getDownPath()
													.lastIndexOf(
															"/",
															(courseware
																	.getDownPath()
																	.length() - 10)) + 1,
											courseware.getDownPath()
													.lastIndexOf("/")) + ".zip");
				} catch (Exception e) {
					e.printStackTrace();
				}
				preload.setLoadPlayTitle(courseware.getFullName());
				preload.setLoadPlayTitleBelow("讲师姓名 ： "
						+ courseware.getTeacherName() + "    发布时间 ： "
						+ courseware.getProTime());
				preload.setPassKey(courseware.getKey());
				if (!dataHelper.hasPreload(preload.getLoadPlayId())) {
					dataHelper.savePreload(preload);
					AlertDialog.Builder builder = new Builder(
							CourseDetailActivity.this);
					builder.setMessage("成功加入下载队列 ！");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent(
											CourseDetailActivity.this,
											PreloadActivity.class);
									CourseDetailActivity.this
											.startActivity(intent);
								}
							});
					builder.create().show();
				} else {
					AlertDialog.Builder builder = new Builder(
							CourseDetailActivity.this);
					builder.setMessage("下载队列已存在该剧集 ！");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent(
											CourseDetailActivity.this,
											PreloadActivity.class);
									CourseDetailActivity.this
											.startActivity(intent);
								}
							});
					builder.create().show();
				}
			}
		},false);

	}
	
	private void changeDownBtn(View view,String classId){
		DataHelper dataHelper = ((CeiApplication) (this.getApplication())).dataHelper;
		ImageView downBtn = (ImageView)view;
		Preload preload = dataHelper.getPreload(classId);
		if(preload != null && preload.getLoadFinish() == 1){
			downBtn.setOnClickListener(null);
			downBtn.setImageResource(R.drawable.phone_study_nodown_btn);
		}
	}

}
