package com.hyrt.cei.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.ui.common.WebViewUtil;
import com.hyrt.cei.ui.phonestudy.CourseDetailActivity;
import com.hyrt.cei.ui.phonestudy.PreloadActivity;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.BitmapManager;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Preload;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

public class PhoneStudyKindsAdapter extends BaseAdapter {

	private int itemLayout;
	private LayoutInflater inflater;
	private List<Courseware> coursewares;
	private AsyncImageLoader asyncImageLoader;
	private ColumnEntry columnEntry;
	private ListView lv;
	private HashMap<String, Drawable> drawables = new HashMap<String, Drawable>();
	private Activity activity;
	private static final int NO_NET = 1;
	private static final int ADD_SUCCESS = 2;
	private static final int CANCEL_SUCCESS = 3;
	private BitmapManager bmpManager;

	public PhoneStudyKindsAdapter(Activity activity, int itemLayout,
			List<Courseware> coursewares, ListView lv) {
		this.activity = activity;
		this.itemLayout = itemLayout;
		this.coursewares = coursewares;
		this.lv = lv;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		asyncImageLoader = ((CeiApplication) (activity.getApplication())).asyncImageLoader;
		columnEntry = ((CeiApplication) (activity.getApplication())).columnEntry;
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(activity.getResources(), R.drawable.courseware_default_icon));
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
				PhoneStudyKindsAdapter.this.notifyDataSetChanged();
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
				cancelCourseware.setSelfCourse(false);
				PhoneStudyKindsAdapter.this.notifyDataSetChanged();
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(itemLayout, null);
			holder.courseIcon = (ImageView) convertView
					.findViewById(R.id.phone_study_kinds_course_icon);
			holder.coursePlayBtn = (ImageView) convertView
					.findViewById(R.id.phone_study_kinds_play_btn);
			holder.downloadBtn = (ImageView) convertView
					.findViewById(R.id.phone_study_kinds_download_btn);
			holder.addCourse = (ImageView) convertView
					.findViewById(R.id.phone_study_kinds_addcourse);
			holder.tv1 = (TextView) convertView
					.findViewById(R.id.phone_study_gird_item_tv1);
			holder.tv2 = (TextView) convertView
					.findViewById(R.id.phone_study_gird_item_tv2);
			holder.tv3 = (TextView) convertView
					.findViewById(R.id.phone_study_gird_item_tv3);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv1.setText(coursewares.get(position).getName().length()>28?coursewares.get(position).getName().substring(0
				,27)+"...":coursewares.get(position).getName());
		holder.tv2.setText("讲师姓名 ： " +coursewares.get(position).getTeacherName());
		holder.tv3.setText("发布时间 ： " +coursewares.get(position).getProTime());
		holder.courseIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, CourseDetailActivity.class);
				intent.putExtra("coursewareInfo", coursewares.get(position));
				activity.startActivity(intent);
			}
		});
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
									columnEntry.getUserId(),
									coursewares.get(position).getClassId());
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
						Intent intent = new Intent(activity, WebViewUtil.class);
						intent.putExtra("path", coursewares.get(position)
								.getLookPath());
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
									columnEntry.getUserId(),
									coursewares.get(position).getClassId());
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
		changeDownBtn(holder.downloadBtn,coursewares.get(position).getClassId());
		if (!coursewares.get(position).isSelfCourse()) {
			holder.addCourse
					.setImageResource(R.drawable.phone_study_addcourse_btn);
			holder.addCourse.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					alertIsSurePop(new OnClickListener() {

						@Override
						public void onClick(View v) {
							new Thread(new Runnable() {

								@Override
								public void run() {
									String rs = Service.saveCourse(coursewares
											.get(position).getClassId(),
											columnEntry.getUserId());
									if (XmlUtil.parseReturnCode(rs).length() > 5) {
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
			holder.addCourse
					.setImageResource(R.drawable.phone_study_canclecourse_btn);
			holder.addCourse.setOnClickListener(new OnClickListener() {

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
													.getClassId(), columnEntry
													.getUserId());
									if (XmlUtil.parseReturnCode(rs).equals("1")) {
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
		String imageUrl = coursewares.get(position).getSmallPath();
		if(com.hyrt.cei.util.StringUtils.isEmpty(imageUrl)){
			holder.courseIcon.setImageResource(R.drawable.courseware_default_icon);
		}else{
			bmpManager.loadBitmap(imageUrl,holder.courseIcon,coursewares.get(position).getClassId());
		}
		/*if (coursewares.size() != 0) {
			String imageUrl = coursewares.get(position).getSmallPath();
			holder.courseIcon.setTag(imageUrl);
			if (drawables.containsKey(imageUrl)
					&& drawables.get(imageUrl) != null) {
				holder.courseIcon.setImageDrawable(drawables.get(imageUrl));
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
									drawables.put(path, drawable);
								}
							}
						});
			}
		}*/
		return convertView;
	}

	class ViewHolder {
		ImageView courseIcon;
		ImageView downloadBtn;
		ImageView coursePlayBtn;
		ImageView addCourse;
		TextView tv1;
		TextView tv2;
		TextView tv3;
	}

	public void clearDrawable() {
		drawables.clear();
	}

	private void downloadThisCourse(final Courseware courseware) {
		alertIsSurePop(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popWin.dismiss();
				DataHelper dataHelper = ((CeiApplication) (activity.getApplication())).dataHelper;
				Preload preload = new Preload();
				preload.setLoadPlayId(courseware.getClassId());
				preload.setLoadCurrentByte(0);
				preload.setLoading(1);
				preload.setLoadFinish(0);
				preload.setLoadUrl(courseware.getDownPath());
				preload.setLoadLocalPath(MyTools.RESOURCE_PATH + MyTools.KJ_PARTPATH 
						+ courseware.getDownPath().substring(
								courseware.getDownPath().lastIndexOf("/",
										(courseware.getDownPath().length() - 10)) + 1,
								courseware.getDownPath().lastIndexOf("/")) + ".zip");
				preload.setLoadPlayTitle(courseware.getFullName());
				preload.setLoadPlayTitleBelow("讲师姓名 ： " + courseware.getTeacherName()
						+ "    发布时间 ： " + courseware.getProTime());
				preload.setPassKey(courseware.getKey());
				preload.setClassLength(courseware.getClassLength());
				if (!dataHelper.hasPreload(preload.getLoadPlayId())) {
					dataHelper.savePreload(preload);
					AlertDialog.Builder builder = new Builder(activity);
					builder.setMessage("成功加入下载队列 ！");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
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
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									Intent intent = new Intent(activity,
											PreloadActivity.class);
									activity.startActivity(intent);
								}
							});

					builder.create().show();
				}
			}
		},false);

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
		popWin = new PopupWindow(popView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popWin.setFocusable(true);
		popWin.showAtLocation(activity.findViewById(R.id.full_view),
				Gravity.CENTER, 0, 0);
	}

	private void changeDownBtn(View view,String classId){
		DataHelper dataHelper = ((CeiApplication) (activity.getApplication())).dataHelper;
		ImageView downBtn = (ImageView)view;
		Preload preload = dataHelper.getPreload(classId);
		if(preload != null && preload.getLoadFinish() == 1){
			downBtn.setOnClickListener(null);
			downBtn.setImageResource(R.drawable.phone_study_nodown_btn);
		}
	}
}
