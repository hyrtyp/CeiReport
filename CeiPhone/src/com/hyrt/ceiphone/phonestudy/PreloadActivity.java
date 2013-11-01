package com.hyrt.ceiphone.phonestudy;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.predownload.DownloadProgressListener;
import com.hyrt.cei.predownload.DownloadThreadManager;
import com.hyrt.cei.predownload.FileDownloader;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.ThreadPoolWrap;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.util.ZipUtils;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.Preload;
import com.hyrt.cei.vo.PreloadContolGroup;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.common.WebViewUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 
 * 视频下载管理类
 * 
 */
public class PreloadActivity extends FoundationActivity {

	// 持有所有进度条目的集合
	private Map<String, PreloadContolGroup> groupMap = new HashMap<String, PreloadContolGroup>();
	// 持有所有数据的集合
	private Map<String, Preload> preloadMap = new HashMap<String, Preload>();
	// 自选课列表
	private List<Courseware> selfselCoursewares;
	private LayoutInflater layoutInflater;
	private List<Preload> preloadList;
	// 所有进度条目的父级view
	private LinearLayout linearLayList;
	private DecimalFormat myformat;
	private DataHelper dataHelper;
	public static final int UPDATE_PROGRESS = 1;
	public static final int UPDATE_DOWNLIST = 2;
	public static final String FLASH_GATE = "/apad.html";
	public static final String FLASH_POSTFIX = ".zip";
	public static final String FLASH_UNPOSTFIX = ".yepeng";
	private PopupWindow popWin;
	// 负责更新进度条
	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message message) {
			switch (message.arg1) {
			case UPDATE_DOWNLIST:
				// 进入页面初始化线程
				DownloadThreadManager.clearThread();
				// 获取下载列表，并下载视频
				ThreadPoolWrap.getThreadPool().executeTask(rGetPreloadList);
				break;
			case UPDATE_PROGRESS:
				final Preload preload = (Preload) message.getData()
						.getSerializable("preload");
				final PreloadContolGroup preloadcontolgroup = (PreloadContolGroup) message
						.getData().getSerializable("group");
				if (preload.getLoadSumByte() != -1) {
					preloadcontolgroup
							.getProgressBarDown()
							.setProgress(
									(int) ((1000L * (long) preload
											.getLoadCurrentByte()) / (long) preload
											.getLoadSumByte()));
					String s = myformat.format((100D * (double) preload
							.getLoadCurrentByte())
							/ (double) preload.getLoadSumByte());
					preloadcontolgroup.getLblPercent().setText(
							String.valueOf(s));
					preloadcontolgroup
							.getProgressBarDown()
							.setProgress(
									(int) ((1000L * (long) preload
											.getLoadCurrentByte()) / (long) preload
											.getLoadSumByte()));
					if (preload.getLoadCurrentByte() >= preload
							.getLoadSumByte() && preload.getLoadSumByte() != -1) {
						preload.setLoading(0);
						preload.setLoadFinish(1);
						dataHelper.updatePreload(preload);
						File file = new File(preload.getLoadLocalPath()
								+ FLASH_UNPOSTFIX);
						file.renameTo(new File(preload.getLoadLocalPath()));
						startDownNext();
						new Thread(new Runnable() {

							@Override
							public void run() {
								try {
									// 启动一个线程，去解压已完成下载的视频
									ZipUtils.unzip(
											preload.getLoadLocalPath(),
											preload.getLoadLocalPath().replace(
													FLASH_POSTFIX, ""));
									handler.post(new Runnable() {

										@Override
										public void run() {
											preloadcontolgroup
													.getLinearLayProcessStatus()
													.setVisibility(View.GONE);
											preloadcontolgroup
													.getLinearLayProcess()
													.setVisibility(View.GONE);
											preloadcontolgroup.getBtnControl()
													.setVisibility(View.GONE);
											preloadcontolgroup
													.getBtnPlay()
													.setVisibility(View.VISIBLE);
											preloadcontolgroup
													.getRootRelativeLayout()
													.setVisibility(View.GONE);
											// 一个视频加载完毕，下载下一个视频,并为这个视频设置播放事件
											preloadcontolgroup
													.getBtnPlay()
													.setOnClickListener(
															new OnClickListener() {

																@Override
																public void onClick(
																		View arg0) {
																	Intent intent = new Intent(
																			PreloadActivity.this,
																			WebViewUtil.class);
																	intent.putExtra(
																			"path",
																			"file:///"
																					+ preload
																							.getLoadLocalPath()
																							.replace(
																									FLASH_POSTFIX,
																									FLASH_GATE));
																	intent.putExtra(
																			"classId",
																			preload.getLoadPlayId());
																	intent.putExtra(
																			"bdclass",
																			preload.getLoadPlayTitleBelow()
																					+ "|"
																					+ preload
																							.getLoadPlayTitle());
																	intent.putExtra(
																			"classLength",
																			preload.getClassLength());
																	startActivity(intent);
																}
															});
										}
									});
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}).start();

					}
				}
			}
			super.dispatchMessage(message);
		}
	};

	private ColumnEntry columnEntry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_preload);
		this.CURRENT_KEY = FoundationActivity.PRELOAD_DATA_KEY;
		// 检查sd卡是否存在不存在的话，则退出
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			MyTools.exitShow(this,
					((Activity) this).getWindow().getDecorView(), "sd卡不存在！");
			this.finish();
			return;
		}
		ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info != null && ConnectivityManager.TYPE_WIFI != info.getType()) {
			MyTools.exitShow(this,
					((Activity) this).getWindow().getDecorView(),
					"非wifi模式下载，会产生额外流量资费请注意！");
		}
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		myformat = new DecimalFormat("#0.00");
		layoutInflater = getLayoutInflater();
		final ImageView alreLoadBtn = (ImageView) findViewById(R.id.phone_study_preload_alreLoadBtn);
		final ImageView loadBtn = (ImageView) findViewById(R.id.phone_study_preload_loadingBtn);
		alreLoadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alreLoadBtn
						.setImageResource(R.drawable.phone_study_preload_aldown_hover);
				loadBtn.setImageResource(R.drawable.phone_study_preload_down);
				for (int i = 0; i < linearLayList.getChildCount(); i++) {
					if (linearLayList
							.getChildAt(i)
							.findViewById(R.id.phone_study_preload_item_btnPlay)
							.getVisibility() == View.VISIBLE) {
						linearLayList.getChildAt(i).setVisibility(View.VISIBLE);
					} else {
						linearLayList.getChildAt(i).setVisibility(View.GONE);
					}
				}
			}
		});
		loadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alreLoadBtn
						.setImageResource(R.drawable.phone_study_preload_aldown);
				loadBtn.setImageResource(R.drawable.phone_study_preload_down_hover);
				for (int i = 0; i < linearLayList.getChildCount(); i++) {
					if (linearLayList
							.getChildAt(i)
							.findViewById(R.id.phone_study_preload_item_btnPlay)
							.getVisibility() != View.VISIBLE) {
						linearLayList.getChildAt(i).setVisibility(View.VISIBLE);
					} else {
						linearLayList.getChildAt(i).setVisibility(View.GONE);
					}
				}
			}
		});
		// 进入页面初始化线程
		DownloadThreadManager.clearThread();
		// 获取下载列表，并下载视频
		ThreadPoolWrap.getThreadPool().executeTask(rGetPreloadList);
		// 同步课件离线列表
		synDownloadClass();
	}

	private Runnable rGetPreloadList = new Runnable() {

		public void run() {
			dataHelper = ((CeiApplication) (PreloadActivity.this
					.getApplication())).dataHelper;
			preloadList = dataHelper.getPreloadList();
			if (preloadList != null)
				handler.post(rReInitData);
		}
	};

	private Runnable rReInitData = new Runnable() {

		public void run() {
			// 初始化加载条目
			initData();
			// 初始化每个条目的事件，并下载第一个视频
			bindListenerToControlGroup();
		}
	};

	private void bindListenerToControlGroup() {
		boolean isNextBegin = true;
		Iterator<Preload> iterator = preloadList.iterator();
		while (iterator.hasNext()) {
			final Preload preload = (Preload) iterator.next();
			String playId = preload.getLoadPlayId();
			final PreloadContolGroup preloadcontolgroup = (PreloadContolGroup) groupMap
					.get(playId);
			if (preload.getLoadFinish() == 1) {
				preloadcontolgroup.getLinearLayProcessStatus().setVisibility(
						View.GONE);
				preloadcontolgroup.getLinearLayProcess().setVisibility(
						View.GONE);
				preloadcontolgroup.getBtnControl().setVisibility(View.GONE);
				preloadcontolgroup.getBtnPlay().setVisibility(View.VISIBLE);
				preloadcontolgroup.getBtnPlay().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent intent = new Intent(
										PreloadActivity.this, WebViewUtil.class);
								intent.putExtra(
										"path",
										"file:///"
												+ preload.getLoadLocalPath()
														.replace(FLASH_POSTFIX,
																FLASH_GATE));
								intent.putExtra("classId",
										preload.getLoadPlayId());
								intent.putExtra("bdclass",
										preload.getLoadPlayTitleBelow() + "|"
												+ preload.getLoadPlayTitle());
								System.out.println(preload
										.getLoadPlayTitleBelow());
								intent.putExtra("classLength",
										preload.getClassLength());
								startActivity(intent);
							}
						});
				preloadcontolgroup.getRootRelativeLayout().setVisibility(
						View.GONE);
			} else if (preload.getLoading() == 1) {
				preloadcontolgroup.getProgressBarDown().setMax(1000);
				preloadcontolgroup.getProgressBarDown()
						.setProgress(
								(int) ((1000L * (long) preload
										.getLoadCurrentByte()) / (long) preload
										.getLoadSumByte()));
				String s = myformat.format((100D * (double) preload
						.getLoadCurrentByte())
						/ (double) preload.getLoadSumByte());
				preloadcontolgroup.getLblPercent().setText(String.valueOf(s));
				final Button controlBtn = preloadcontolgroup.getBtnControl();
				controlBtn.setTag("暂停");
				controlBtn
						.setBackgroundResource(R.drawable.phone_study_preload_item_pause_bg);
				controlBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						if (controlBtn.getTag().equals("暂停")) {
							controlBtn.setTag("开始");
							controlBtn
									.setBackgroundResource(R.drawable.phone_study_preload_item_start_bg);
							preload.setLoading(0);
							dataHelper.updatePreload(preload);
							startDownNext();
						} else {
							controlBtn.setTag("暂停");
							controlBtn
									.setBackgroundResource(R.drawable.phone_study_preload_item_pause_bg);
							preload.setLoading(1);
							dataHelper.updatePreload(preload);
							download(preload);
						}
					}
				});
				if (isNextBegin) {
					download(preload);
					isNextBegin = false;
				}

			} else if (preload.getLoading() != 1) {
				preloadcontolgroup.getProgressBarDown().setMax(1000);
				preloadcontolgroup.getProgressBarDown()
						.setProgress(
								(int) ((1000L * (long) preload
										.getLoadCurrentByte()) / (long) preload
										.getLoadSumByte()));
				String s = myformat.format((100D * (double) preload
						.getLoadCurrentByte())
						/ (double) preload.getLoadSumByte());
				preloadcontolgroup.getLblPercent().setText(String.valueOf(s));
				final Button controlBtn = preloadcontolgroup.getBtnControl();
				controlBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						if (controlBtn.getTag().equals("暂停")) {
							controlBtn.setTag("开始");
							controlBtn
									.setBackgroundResource(R.drawable.phone_study_preload_item_start_bg);
							preload.setLoading(0);
						} else {
							controlBtn.setTag("暂停");
							controlBtn
									.setBackgroundResource(R.drawable.phone_study_preload_item_pause_bg);
							preload.setLoading(1);
							download(preload);
						}
					}
				});
			}
			preloadcontolgroup.getBtnDelete().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							alertIsSurePop(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if (popWin != null)
										popWin.dismiss();
									linearLayList.removeView(preloadcontolgroup
											.getRootRelativeLayout());
									preload.setLoadFinish(1);
									int isSuccessDel = dataHelper
											.deletePreload(preload
													.getLoadPlayId());
									if (isSuccessDel != -1) {
										new Thread(new Runnable() {

											@Override
											public void run() {
												if (preload.getLoadLocalPath() != null) {
													File zipFile = new File(
															preload.getLoadLocalPath());
													File zipYepFile = new File(
															preload.getLoadLocalPath()
																	+ ".yepeng");
													zipFile.delete();
													zipYepFile.delete();
													delFolder(preload
															.getLoadLocalPath()
															.replace(
																	FLASH_POSTFIX,
																	""));
												}
											}
										}).start();
										if (preloadcontolgroup.getBtnPlay()
												.getVisibility() != View.VISIBLE)
											startDownNext();
									}
								}
							}, false);
						}
					});
			// 请求自选课的数据，来判断是否隐藏
			final Button addcourseBtn = preloadcontolgroup.getBtnAddCourse();
			addcourseBtn.setVisibility(View.GONE);
			final Handler handlerVG = new Handler() {
				@Override
				public void dispatchMessage(Message msg) {
					addcourseBtn.setVisibility(View.VISIBLE);
				}
			};
			new Thread(new Runnable() {

				@Override
				public void run() {
					if (selfselCoursewares == null) {
						String result = Service
								.queryCourse(((CeiApplication) (PreloadActivity.this
										.getApplication())).columnEntry
										.getUserId());
						selfselCoursewares = new ArrayList<Courseware>();
						XmlUtil.parseCoursewares(result, selfselCoursewares);
					}
					boolean isSelfCourse = false;
					for (int i = 0; i < selfselCoursewares.size(); i++) {
						if(selfselCoursewares != null && selfselCoursewares.get(i) != null && selfselCoursewares.get(i).getClassId() != null
								&& preload != null && selfselCoursewares.get(i).getClassId().equals(preload.getLoadPlayId())){
							isSelfCourse = true;
						}
					}
					if (!isSelfCourse) {
						handlerVG.sendMessage(handler.obtainMessage());
					}
				}
			}).start();
			addcourseBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					alertIsSurePop(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							new Thread(new Runnable() {

								@Override
								public void run() {
									Service.saveCourse(preload.getLoadPlayId(),
											columnEntry.getUserId());
									handler.post(new Runnable() {

										@Override
										public void run() {
											TextView titleTv = (TextView) popWin
													.getContentView()
													.findViewById(
															R.id.issure_title);
											titleTv.setText("加入自选课成功 ！");
											handler.postDelayed(new Runnable() {

												@Override
												public void run() {
													popWin.dismiss();
												}
											}, 300);
											addcourseBtn
													.setVisibility(View.GONE);
										}
									});
								}
							}).start();
						}
					}, true);
				}
			});
		}
	}

	public void initData() {
		linearLayList = (LinearLayout) findViewById(R.id.phone_study_preload_itemParent);
		linearLayList.removeAllViews();
		groupMap.clear();
		if (preloadList != null) {
			RelativeLayout relativelayout;
			for (Iterator<Preload> iterator = preloadList.iterator(); iterator
					.hasNext(); linearLayList.addView(relativelayout)) {
				Preload preload = (Preload) iterator.next();
				preloadMap.put(preload.getLoadPlayId(), preload);
				// 获取单个视频加载条目
				relativelayout = getView(preload);
			}
		}
	}

	public RelativeLayout getView(Preload preload) {
		RelativeLayout relativelayout = (RelativeLayout) layoutInflater
				.inflate(R.layout.phone_study_preload_item, null);
		PreloadContolGroup preloadcontolgroup = new PreloadContolGroup();
		groupMap.put(preload.getLoadPlayId(), preloadcontolgroup);
		((TextView) relativelayout
				.findViewById(R.id.phone_study_preload_item_title))
				.setText(preload.getLoadPlayTitle());
		((TextView) relativelayout
				.findViewById(R.id.phone_study_preload_item_content))
				.setText("第"
						+ (preload.getClassLevel().equals("null") ? "1"
								: preload.getClassLevel()) + "集");
		preloadcontolgroup.setRootRelativeLayout(relativelayout);
		preloadcontolgroup.setBtnDelete((Button) relativelayout
				.findViewById(R.id.phone_study_preload_item_btnDelete));
		preloadcontolgroup.setBtnControl((Button) relativelayout
				.findViewById(R.id.phone_study_preload_item_btnControl));
		preloadcontolgroup.setBtnPlay((Button) relativelayout
				.findViewById(R.id.phone_study_preload_item_btnPlay));
		preloadcontolgroup.setLblPercent((TextView) relativelayout
				.findViewById(R.id.phone_study_preload_item_lblPercent));
		preloadcontolgroup.setLblContent((TextView) relativelayout
				.findViewById(R.id.phone_study_preload_item_content));
		preloadcontolgroup.setLinearLayProcess((LinearLayout) relativelayout
				.findViewById(R.id.phone_study_preload_item_process));
		preloadcontolgroup
				.setLinearLayProcessStatus((LinearLayout) relativelayout
						.findViewById(R.id.phone_study_preload_item_processStatus));
		preloadcontolgroup.setPlayId(preload.getLoadPlayId());
		preloadcontolgroup.setProgressBarDown((ProgressBar) relativelayout
				.findViewById(R.id.phone_study_preload_item_progressBarDown));
		preloadcontolgroup.setBtnAddCourse((Button) relativelayout
				.findViewById(R.id.phone_study_preload_item_addcourse));
		return relativelayout;
	}

	public void startDownNext() {
		Preload preload = null;
		for (int i = 0; i < preloadList.size(); i++) {
			if (preloadList.get(i).getLoadFinish() != 1
					&& preloadList.get(i).getLoading() == 1
					&& preloadList.get(i).getLoadCurrentByte() != preloadList
							.get(i).getLoadSumByte()) {
				preload = preloadList.get(i);
				break;
			}
		}
		if (preload != null) {
			String playId = preload.getLoadPlayId();
			PreloadContolGroup preloadcontolgroup = (PreloadContolGroup) groupMap
					.get(playId);
			preloadcontolgroup.getProgressBarDown().setMax(1000);
			if (preload.getLoadSumByte() != -1)
				preloadcontolgroup.getProgressBarDown()
						.setProgress(
								(int) ((1000L * (long) preload
										.getLoadCurrentByte()) / (long) preload
										.getLoadSumByte()));
			download(preload);
		}

	}

	/**
	 * 下载此视频停止其他下载，并更新进度条
	 * 
	 * @param preload
	 */
	private void download(final Preload preload) {
		Runnable runnable = new Runnable() {

			public void run() {
				try {
					FileDownloader loader = new FileDownloader(
							((CeiApplication) (PreloadActivity.this
									.getApplication())).dataHelper,
							preload);
					final PreloadContolGroup group = (PreloadContolGroup) groupMap
							.get(preload.getLoadPlayId());
					loader.download(new DownloadProgressListener() {
						public void onDownloadSize() {
							Message message = Message.obtain();
							message.arg1 = UPDATE_PROGRESS;
							message.getData().putSerializable("preload",
									preload);
							message.getData().putSerializable("group", group);
							handler.sendMessage(message);
						}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		DownloadThreadManager.clearThread();
		ThreadPoolWrap.getThreadPool().shutdown();
		ThreadPoolWrap.getThreadPool().executeTask(runnable);
	}

	/**
	 * 同步离线列表
	 */
	private void synDownloadClass() {
		dataHelper = ((CeiApplication) (PreloadActivity.this.getApplication())).dataHelper;
		new Thread(new Runnable() {

			@Override
			public void run() {
				File file = new File(MyTools.RESOURCE_PATH
						+ MyTools.KJ_PARTPATH);
				StringBuilder fileNames = new StringBuilder();
				for (int i = 0; i < file.list().length; i++) {
					String fileName = "";
					if (file.list()[i].contains("."))
						fileName = file.list()[i].substring(0,
								file.list()[i].indexOf("."));
					else
						fileName = file.list()[i];
					if (fileNames.indexOf(fileName) == -1)
						fileNames.append(fileName + ",");
				}
				List<Preload> preloads = dataHelper.getPreloadList();
				for (int i = 0; i < preloads.size(); i++) {
					if (preloads.get(i).getLoadLocalPath() == null)
						continue;
					final String pathName = preloads
							.get(i)
							.getLoadLocalPath()
							.replace(FLASH_POSTFIX, "")
							.replace(
									MyTools.RESOURCE_PATH + MyTools.KJ_PARTPATH,
									"");
					// 删掉重复的文件
					if (fileNames.toString().contains(pathName)
							&& new File(pathName + FLASH_POSTFIX
									+ FLASH_UNPOSTFIX).exists()) {
						// && new File(pathName + FLASH_POSTFIX).exists()) {
						// 过滤掉本地正在下载，和已经导入的课件，则删除正在下载的课件，然后解压这个课件，并剔除需要验证的此课件，并更新数据库说明已经下载
						new File(pathName + FLASH_POSTFIX + FLASH_UNPOSTFIX)
								.delete();
						/*
						 * try { ZipUtils.unzip(MyTools.RESOURCE_PATH + pathName
						 * + FLASH_POSTFIX, MyTools.RESOURCE_PATH+ pathName); }
						 * catch (IOException e) { e.printStackTrace(); }
						 */
						Preload preload = preloads.get(i);
						preload.setLoadFinish(1);
						preload.setLoading(0);
						preload.setLoadCurrentByte(preload.getLoadSumByte());
						dataHelper.updatePreload(preload);
					} else if (fileNames.toString().contains(pathName)
							&& new File(pathName + FLASH_POSTFIX).exists()) {
						// 如果已经下完，则剔除需要验证的字段
						fileNames.delete(fileNames.indexOf(pathName),
								pathName.length() + 1);
					}
				}
				// 通过课件的资源根路径获取课件的key以及信息
				String result = Service.queryPassKey(fileNames.toString());
				List<Courseware> coursewares = new ArrayList<Courseware>();
				XmlUtil.parseCoursewares(result, coursewares);
				StringBuilder classIds = new StringBuilder();
				for (int i = 0; i < coursewares.size(); i++) {
					classIds.append(coursewares.get(i).getClassId() + ",");
				}
				if (classIds.length() > 0)
					classIds.deleteCharAt(classIds.length() - 1);
				// 验证课件合法性
				result = Service.queryBuyClass(columnEntry.getUserId(),
						classIds.toString());
				List<Courseware> legalCoursewares = new ArrayList<Courseware>();
				XmlUtil.parseCoursewares(result, legalCoursewares);
				if (legalCoursewares.size() != coursewares.size()) {
					for (int i = 0; i < coursewares.size(); i++) {
						boolean isLegal = false;
						for (int j = 0; j < legalCoursewares.size(); j++) {
							if (legalCoursewares.get(j).getClassId()
									.equals(coursewares.get(i).getClassId())) {
								isLegal = true;
							}
						}
						if (!isLegal) {
							coursewares.remove(i);
							i--;
						}
					}
				}
				for (int i = 0; i < coursewares.size(); i++) {
					final Preload preload = new Preload();
					preload.setLoadPlayId(coursewares.get(i).getClassId());
					if (dataHelper.hasPreload(preload.getLoadPlayId()))
						continue;
					preload.setLoadPlayTitle(coursewares.get(i).getName());
					preload.setLoadPlayTitleBelow("讲师姓名 ： "
							+ coursewares.get(i).getTeacherName()
							+ "    发布时间 ： " + coursewares.get(i).getProTime());
					preload.setClassLength(coursewares.get(i).getClassLength());
					if (coursewares.get(i).getDownPath().contains("/"))
						preload.setLoadLocalPath(MyTools.RESOURCE_PATH
								+ MyTools.KJ_PARTPATH
								+ coursewares
										.get(i)
										.getDownPath()
										.substring(
												coursewares.get(i)
														.getDownPath()
														.lastIndexOf("/") + 1)
								+ FLASH_POSTFIX);
					preload.setLoadFinish(1);
					preload.setLoading(0);
					preload.setLoadSumByte((int) new File(preload
							.getLoadLocalPath()).length());
					preload.setLoadCurrentByte(preload.getLoadSumByte());
					preload.setPassKey(coursewares.get(i).getKey());
					dataHelper.savePreload(preload);
				}
				Message msg = handler.obtainMessage();
				msg.arg1 = UPDATE_DOWNLIST;
				handler.sendMessage(msg);
			}
		}).start();
	}

	public static void delFolder(String folderPath) {
		try {
			File file = new File(folderPath);
			folderPath += "_del";
			file.renameTo(new File(folderPath));
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	private void alertIsSurePop(OnClickListener clickListener,
			boolean isCheckLogin) {
		View popView = this.getLayoutInflater().inflate(
				R.layout.phone_study_issure, null);
		if (isCheckLogin &&!((CeiApplication) this.getApplication()).isNet()) {
			((TextView) popView.findViewById(R.id.issure_title))
					.setText("请联网操作！");
			clickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					popWin.dismiss();
				}
			};
		} else if (isCheckLogin && columnEntry.getUserId() == null) {
			((TextView) popView.findViewById(R.id.issure_title))
					.setText("请登录操作！");
		}
		popView.findViewById(R.id.phone_study_issure_sure_btn)
				.setOnClickListener(clickListener);
		popView.findViewById(R.id.phone_study_issure_cancel_btn)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (popWin != null)
							popWin.dismiss();
					}
				});
		popWin = new PopupWindow(popView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popWin.setFocusable(true);
		popWin.showAtLocation(this.findViewById(R.id.full_view),
				Gravity.CENTER, 0, 0);
	}

}
