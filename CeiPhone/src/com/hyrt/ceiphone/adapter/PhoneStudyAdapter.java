package com.hyrt.ceiphone.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.ui.phonestudy.CourseDetailActivity;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Preload;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.common.WebViewUtil;
import com.hyrt.ceiphone.phonestudy.FoundationActivity;
import com.hyrt.ceiphone.phonestudy.PreloadActivity;
import com.hyrt.ceiphone.phonestudy.SayGroupActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PhoneStudyAdapter extends BaseAdapter {

	private int itemLayout;
	private LayoutInflater inflater;
	private List<Courseware> coursewares;
	private List<Courseware> allCoursewares;
	private AsyncImageLoader asyncImageLoader;
	private ColumnEntry columnEntry;
	private ListView lv;
	private HashMap<String, Drawable> drawables = new HashMap<String, Drawable>();
	private Activity activity;
	private static final int NO_NET = 1;
	private static final int ADD_SUCCESS = 2;
	private static final int CANCEL_SUCCESS = 3;
	private boolean isRecord;

	public PhoneStudyAdapter(Activity activity, int itemLayout,
			List<Courseware> coursewares, ListView lv,boolean isRecord) {
		this.activity = activity;
		this.itemLayout = itemLayout;
		this.coursewares = coursewares;
		this.isRecord = isRecord;
		this.lv = lv;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		asyncImageLoader = ((CeiApplication) (activity.getApplication())).asyncImageLoader;
		columnEntry = ((CeiApplication) (activity.getApplication())).columnEntry;
	}

	public PhoneStudyAdapter(Activity activity, int itemLayout,
			List<Courseware> coursewares, ListView lv,
			List<Courseware> allCoursewares,boolean isRecord) {
		this.activity = activity;
		this.itemLayout = itemLayout;
		this.coursewares = coursewares;
		this.isRecord = isRecord;
		this.lv = lv;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.allCoursewares = allCoursewares;
		asyncImageLoader = ((CeiApplication) (activity.getApplication())).asyncImageLoader;
		columnEntry = ((CeiApplication) (activity.getApplication())).columnEntry;
	}

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			AlertDialog.Builder builder = new Builder(activity);
			switch (msg.arg1) {
			case NO_NET:
				popWin.dismiss();
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
			case ADD_SUCCESS:
				popWin.dismiss();
				Courseware courseware = coursewares.get(msg.arg2);
				courseware.setSelfCourse(true);
				PhoneStudyAdapter.this.notifyDataSetChanged();
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
				break;
			case CANCEL_SUCCESS:
				popWin.dismiss();
				Courseware cancelCourseware = coursewares.get(msg.arg2);
				if (cancelCourseware.isSelfPage()) {
					if (allCoursewares != null)
						allCoursewares.remove(cancelCourseware);
					coursewares.remove(cancelCourseware);
				} else {
					cancelCourseware.setSelfCourse(false);
				}
				PhoneStudyAdapter.this.notifyDataSetChanged();
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
				break;
			}
		}
	};

	public int getCount() {
		return coursewares.size();
	}

	public Object getItem(int position) {
		return Integer.valueOf(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		holder = new ViewHolder();
		convertView = inflater.inflate(itemLayout, null);
		holder.courseIcon = (ImageView) convertView
				.findViewById(R.id.phone_study_listviewitem_icon);
		holder.coursePlayBtn = (ImageView) convertView
				.findViewById(R.id.phone_study_listviewitem_playbtn);
		holder.downloadBtn = (ImageView) convertView
				.findViewById(R.id.phone_study_listviewitem_downbtn);
		holder.controCourse = (ImageView) convertView
				.findViewById(R.id.phone_study_controllcourse);
		holder.sayBtn = (ImageView) convertView
				.findViewById(R.id.phone_study_listviewitem_say);
		holder.uploadStudyBtn = (ImageView) convertView
				.findViewById(R.id.phone_study_listviewitem_upload);
		holder.tv1 = (TextView) convertView
				.findViewById(R.id.phone_study_listviewitem_title);
		holder.tv2 = (TextView) convertView
				.findViewById(R.id.phone_study_listviewitem_teachername);
		holder.tv3 = (TextView) convertView
				.findViewById(R.id.phone_study_listviewitem_protime);
		holder.studystatus = (TextView) convertView
				.findViewById(R.id.phone_study_listviewitem_status);
		convertView.setTag(holder);
		if (holder.uploadStudyBtn != null) {
			if (coursewares.get(position).getUploadTime() != 0 && !"1".equals(coursewares.get(position).getIscompleted()))
				holder.uploadStudyBtn
						.setImageResource(R.drawable.phone_study_uploadcourse_btn);
			else
				holder.uploadStudyBtn
						.setImageResource(R.drawable.phone_study_uploadcourse_btn_hover);
		}
		holder.tv1.setText(coursewares.get(position).getName());
		if(coursewares.get(position).getClassLevel() != null)
		holder.tv2.setText("讲师姓名 ： "
				+ coursewares.get(position).getTeacherName());
		else
			holder.tv2.setText("讲师姓名 ： "
					+ coursewares.get(position).getTeacherName());
		holder.tv3.setText("发布时间 ： " + coursewares.get(position).getProTime());
		holder.courseIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, CourseDetailActivity.class);
				intent.putExtra("coursewareInfo", coursewares.get(position));
				intent.putExtra("isRecord", isRecord);
				activity.startActivity(intent);
			}
		});
		if (holder.sayBtn != null) {
			holder.sayBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(activity, SayGroupActivity.class);
					intent.putExtra("coursewareinfo", coursewares.get(position));
					activity.startActivity(intent);
				}
			});
		}
		if (holder.uploadStudyBtn != null) {
			try {
				if("1".equals(coursewares.get(position).getIscompleted()))
						holder.studystatus.setText("状态：已学完");
			} catch (Exception e) {
				e.printStackTrace();
			}
			coursewares.get(position).setFree(true);
			holder.uploadStudyBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 上传学习记录，并同步数据库的信息，更新列表
					final Handler handler = new Handler();
					if (coursewares.get(position).getUploadTime() != 0 && !"1".equals(coursewares.get(position).getIscompleted())) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								if (!XmlUtil
										.parseReturnCode(
												Service.saveUserClassTime(
														((CeiApplication) (activity
																.getApplication())).columnEntry
																.getUserId(),
														coursewares.get(
																position)
																.getClassId(),
														coursewares
																.get(position)
																.getUploadTime()
																+ "")).equals(
												"-1")) {
									handler.post(new Runnable() {

										@Override
										public void run() {
											coursewares.get(position)
													.setUploadTime(0);
											((CeiApplication) (activity
													.getApplication())).dataHelper
													.updatePlayRecord(coursewares
															.get(position));
											coursewares.add(0,coursewares.remove(position));
											((FoundationActivity)activity).courses.add(0,((FoundationActivity)activity).courses.remove(position));
											((FoundationActivity)activity).allCoursewares.add(0,((FoundationActivity)activity).allCoursewares.remove(position));
											PhoneStudyAdapter.this.notifyDataSetChanged();
											AlertDialog.Builder builder = new Builder(
													activity);
											builder.setMessage("上传学习记录成功 ！");
											builder.setPositiveButton(
													"确认",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															dialog.dismiss();
														}
													});

											builder.create().show();
										}
									});
								}
							}
						}).start();
					}
				}
			});
		}

		if (holder.coursePlayBtn != null) {
			holder.coursePlayBtn.setOnClickListener(new OnClickListener() {

				// 没有购买的
				private static final int NO_BUY = 0;
				// 已经购买的
				private static final int AL_BUY = 1;

				/**
				 * 检查是否有买了这个课件
				 */
				private void checkBuy() {
					if (coursewares.get(position).isFree()) {
						Message message = handler.obtainMessage();
						message.arg1 = AL_BUY;
						handler.sendMessage(message);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								String result = Service.queryBuyClass(
										columnEntry.getUserId(), coursewares
												.get(position).getClassId());
								List<Courseware> coursewares = new ArrayList<Courseware>();
								XmlUtil.parseCoursewares(result, coursewares);
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
							if(((CeiApplication)activity.getApplication()).isNet())
								MyTools.exitShow(activity, activity.getWindow().getDecorView(), "未购买该课件！");
							else
								MyTools.exitShow(activity, activity.getWindow().getDecorView(), "请联网查看！");
							break;
						case AL_BUY:
							Intent intent = new Intent(activity,
									WebViewUtil.class);
							intent.putExtra("isRecord", isRecord);
							intent.putExtra("path", coursewares.get(position)
									.getLookPath().replace("/i2/", "/an2/")
									.replace("an1", "an2"));
							intent.putExtra("class", coursewares.get(position));
							activity.startActivity(intent);
							break;
						}
					}
				};

				@Override
				public void onClick(View v) {
					checkBuy();
				}
			});
		}
		if (holder.downloadBtn != null) {
			holder.downloadBtn.setOnClickListener(new OnClickListener() {

				// 没有购买的
				private static final int NO_BUY = 0;
				// 已经购买的
				private static final int AL_BUY = 1;

				/**
				 * 检查是否有买了这个课件
				 */
				private void checkBuy() {
					if (coursewares.get(position).isFree()) {
						Message message = handler.obtainMessage();
						message.arg1 = AL_BUY;
						handler.sendMessage(message);
					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {
								String result = Service.queryBuyClass(
										columnEntry.getUserId(), coursewares
												.get(position).getClassId());
								List<Courseware> coursewares = new ArrayList<Courseware>();
								XmlUtil.parseCoursewares(result, coursewares);
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
							if(((CeiApplication)activity.getApplication()).isNet())
								MyTools.exitShow(activity, activity.getWindow().getDecorView(), "未购买该课件！");
							else
								MyTools.exitShow(activity, activity.getWindow().getDecorView(), "请联网查看！");
							break;
						case AL_BUY:
							downloadThisCourse(coursewares.get(position));
							break;
						}
					}
				};

				@Override
				public void onClick(View arg0) {
					checkBuy();
				}
			});
		}
		if (holder.controCourse != null) {
			if (!coursewares.get(position).isSelfCourse()) {
				holder.controCourse
						.setImageResource(R.drawable.phone_study_addcourse_btn);
				holder.controCourse.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertIsSurePop(new OnClickListener() {

							@Override
							public void onClick(View v) {
								new Thread(new Runnable() {

									@Override
									public void run() {
										String rs = Service.saveCourse(
												coursewares.get(position)
														.getClassId(),
												columnEntry.getUserId());
										if (XmlUtil.parseReturnCode(rs)
												.length() > 5) {
											Message message = handler
													.obtainMessage();
											message.arg1 = ADD_SUCCESS;
											message.arg2 = position;
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
				holder.controCourse
						.setImageResource(R.drawable.phone_study_canclecourse_btn);
				holder.controCourse.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertIsSurePop(new OnClickListener() {

							@Override
							public void onClick(View v) {
								new Thread(new Runnable() {

									@Override
									public void run() {
										String rs = Service.cancelCourse(
												coursewares.get(position)
														.getClassId(),
												columnEntry.getUserId());
										if (XmlUtil.parseReturnCode(rs).equals(
												"1")) {
											Message message = handler
													.obtainMessage();
											message.arg1 = CANCEL_SUCCESS;
											message.arg2 = position;
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
		if (coursewares.size() != 0) {
			String imageUrl = coursewares.get(position).getSmallPath();
			holder.courseIcon.setTag(imageUrl);
			if (drawables.containsKey(coursewares.get(position).getClassId())
					&& drawables.get(coursewares.get(position).getClassId()) != null) {
				holder.courseIcon.setImageDrawable(drawables.get(coursewares
						.get(position).getClassId()));
			} else {
				ImageResourse imageResource = new ImageResourse();
				imageResource.setIconUrl(imageUrl);
				imageResource.setIconId(coursewares.get(position).getClassId());
				asyncImageLoader.loadDrawable(imageResource,
						new AsyncImageLoader.ImageCallback() {

							@Override
							public void imageLoaded(Drawable drawable,
									String path) {
								ImageView imageView = (ImageView) lv
										.findViewWithTag(path);
								if (drawable != null && imageView != null) {
									imageView.setImageDrawable(drawable);
									try {
										drawables.put(coursewares.get(position)
												.getClassId(), drawable);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						});
			}
		}

		return convertView;
	}

	class ViewHolder {
		ImageView courseIcon;
		ImageView downloadBtn;
		ImageView coursePlayBtn;
		ImageView controCourse;
		ImageView sayBtn;
		ImageView uploadStudyBtn;
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView studystatus;
	}

	private void downloadThisCourse(final Courseware courseware) {
		alertIsSurePop(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popWin.dismiss();

				DataHelper dataHelper = ((CeiApplication) (activity
						.getApplication())).dataHelper;
				Preload preload = new Preload();
				preload.setLoadPlayId(courseware.getClassId());
				preload.setLoadCurrentByte(0);
				preload.setLoading(1);
				preload.setLoadFinish(0);
				preload.setLoadUrl(courseware.getDownPath());
				preload.setLoadLocalPath(MyTools.RESOURCE_PATH
						+ MyTools.KJ_PARTPATH
						+ courseware.getDownPath().substring(
								courseware.getDownPath()
										.lastIndexOf(
												"/",
												(courseware.getDownPath()
														.length() - 10)) + 1,
								courseware.getDownPath().lastIndexOf("/"))
						+ ".zip");
				preload.setLoadPlayTitle(courseware.getName());
				preload.setLoadPlayTitleBelow("讲师姓名 ： "
						+ courseware.getTeacherName() + "    发布时间 ： "
						+ courseware.getProTime());
				preload.setPassKey(courseware.getKey());
				preload.setClassLength(courseware.getClassLength());
				if (!dataHelper.hasPreload(preload.getLoadPlayId())) {
					dataHelper.savePreload(preload);
					AlertDialog.Builder builder = new Builder(activity);
					builder.setMessage("成功加入下载队列 ！");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent(activity,
											PreloadActivity.class);
									activity.startActivity(intent);
								}
							});
					builder.create().show();
				} else {
					AlertDialog.Builder builder = new Builder(activity);
					builder.setMessage("下载队列已存在该剧集 ！");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent(activity,
											PreloadActivity.class);
									activity.startActivity(intent);
								}
							});

					builder.create().show();
				}
			}
		},true);

	}

	private PopupWindow popWin;

	private void alertIsSurePop(OnClickListener clickListener,boolean isCheckLogin) {
		View popView = activity.getLayoutInflater().inflate(
				R.layout.phone_study_issure, null);
		if(isCheckLogin &&!((CeiApplication)activity.getApplication()).isNet()){
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
		popWin = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popWin.setFocusable(true);
		popWin.showAtLocation(activity.findViewById(R.id.full_view),
				Gravity.CENTER, 0, 0);
	}

}
