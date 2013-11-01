package com.hyrt.readreport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import org.vudroid.pdfdroid.PdfViewerActivity;
import com.hyrt.cei.adapter.BookSelfAdapter;
import com.hyrt.cei.adapter.BookSelfListAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.util.ComparatorReport;
import com.hyrt.cei.util.EncryptDecryption;
import com.hyrt.cei.util.NewFileDownload;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.util.ZipUtils;
import com.hyrt.cei.vo.Report;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.cei.util.MyTools;
import com.hyrt.ceiphone.R;
import com.hyrt.readreport.view.ShelvesView;
import com.poqop.document.ViewerPreferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CeiShelfBookActivity extends Activity implements OnClickListener {
	private int number = 0, hengORshu = 0;
	private List<Report> data = new ArrayList<Report>();
	private ShelvesView shelvesview;
	private DataHelper dataHelper;
	private ImageView find, heng, shu, shezhi, backImg;
	private EditText findText;
	private ListView shelvesListView;
	private BookSelfAdapter selfAdapter;
	private BookSelfListAdapter selfListAdapter;
	private LinearLayout porLayout;
	private CeiApplication application;
	HashMap<String, String> loadThreads = new HashMap<String, String>();
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 404:
				if (msg.arg1 == 1) {
					MyTools.exitShow(CeiShelfBookActivity.this, shelvesview,
							"报告路径不正确！");
				} else if(msg.arg1 == 2){
					MyTools.exitShow(CeiShelfBookActivity.this, shelvesview,
							 "文件解压失败！");
				}else if(msg.arg1==3){
					//Toast.makeText(this, "同步错误", 1).show();
					MyTools.exitShow(CeiShelfBookActivity.this,CeiShelfBookActivity.this.
							getWindow().getDecorView(),  "同步错误！");
				}else {
					MyTools.exitShow(CeiShelfBookActivity.this, shelvesview,
							"后台加密错误！");
				}
				porLayout.setVisibility(View.GONE);
				break;
			case 10:
				selfAdapter = new BookSelfAdapter(CeiShelfBookActivity.this,
						data, shelvesview);
				shelvesview.setAdapter(selfAdapter);
				shelvesview.setSelector(R.drawable.list_selector);
				shelvesview
						.setOnItemLongClickListener(new OnItemLongClickListener() {

							@Override
							public boolean onItemLongClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								Report report = data.get(arg2);
								delViewShow(report, 10);
								return true;
							}
						});
				shelvesview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							final int arg2, long arg3) {
						final Report report = data.get(arg2);
						final TextView tv = (TextView) arg1
								.findViewById(R.id.yjbg_book_item_tv);
						final ProgressBar bar = (ProgressBar) arg1
								.findViewById(R.id.yjbg_book_item_pro);
						if (!report.getIsLoad().equals("yes")) {
							Handler handler = new Handler(getLooper()) {

								@Override
								public void handleMessage(Message msg) {
									if (msg.arg1 > 0) {
										bar.setProgress(msg.arg1);
										tv.setText(msg.arg1 + "%");
									}
									switch (msg.arg1) {
									case -1:
										// 路劲错误
										tv.setText("err");
										dataHelper.delReport(report.getId());
										loadThreads.remove(report.getId());
										initData();
										if(selfAdapter!=null)
										selfAdapter.notifyDataSetChanged();
										MyTools.exitShow(
												CeiShelfBookActivity.this,
												shelvesview, "下载文件为空！");
										break;
									case -2:
										tv.setText("err");
										dataHelper.delReport(report.getId());
										loadThreads.remove(report.getId());
										initData();
										if(selfAdapter!=null)
										selfAdapter.notifyDataSetChanged();
										MyTools.exitShow(
												CeiShelfBookActivity.this,
												shelvesview, "连接网络超时！");
										break;
									case -3:
										tv.setText("err");
										dataHelper.delReport(report.getId());
										loadThreads.remove(report.getId());
										initData();
										if(selfAdapter!=null)
										selfAdapter.notifyDataSetChanged();
										MyTools.exitShow(
												CeiShelfBookActivity.this,
												shelvesview, "文件不存在！");
										break;
									case -4:
										tv.setText("err");
										dataHelper.delReport(report.getId());
										loadThreads.remove(report.getId());
										initData();
										if(selfAdapter!=null)
										selfAdapter.notifyDataSetChanged();
										MyTools.exitShow(
												CeiShelfBookActivity.this,
												shelvesview, "URL地址错误！");
										break;
									case -5:
										tv.setText("err");
										dataHelper.delReport(report.getId());
										loadThreads.remove(report.getId());
										initData();
										if(selfAdapter!=null)
										selfAdapter.notifyDataSetChanged();
										MyTools.exitShow(
												CeiShelfBookActivity.this,
												shelvesview, "本地文件创建失败！");
										break;
									case -6:
										tv.setText("err");
										dataHelper.delReport(report.getId());
										loadThreads.remove(report.getId());
										initData();
										if(selfAdapter!=null)
										selfAdapter.notifyDataSetChanged();
										MyTools.exitShow(
												CeiShelfBookActivity.this,
												shelvesview, "数据读写错误！");
										break;
									case 100:
										tv.setText("100%");
										bar.setVisibility(View.GONE);
										report.setIsLoad("yes");
										dataHelper.UpdateReportZT(report);
										//统计
										Service.updatedownsum(report.getId(), "bg");
										
										// 下载完毕后减压文件
										porLayout.setVisibility(View.VISIBLE);
										setViewListener(data.get(arg2));
										loadThreads.remove(report.getId());
										break;
									}
								}

							};

							String path = report.getDownpath().toString();
							File savedir = new File(Report.SD_PATH
									+ report.getName());
							if (!savedir.exists()) {
								savedir.mkdirs();
							}

							try {
								if (loadThreads.get(report.getId()) != null) {
									MyTools.exitShow(CeiShelfBookActivity.this,CeiShelfBookActivity.this.
											getWindow().getDecorView(),  "正在下载！");
									return;
								} else {
									NewFileDownload.download(path, savedir,
											handler);
									loadThreads.put(report.getId(), "1");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							// 下载完毕后减压文件
							porLayout.setVisibility(View.VISIBLE);
							setViewListener(data.get(arg2));
						}
					}
				});
				break;
			case 20:
				selfListAdapter = new BookSelfListAdapter(
						CeiShelfBookActivity.this, data, shelvesListView);
				shelvesListView.setAdapter(selfListAdapter);
				shelvesListView.setSelector(R.drawable.list_selector);
				shelvesListView
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, final int arg2, long arg3) {
								// setViewListener(data.get(arg2), arg1);

								final Report report = data.get(arg2);
								final TextView tv = (TextView) arg1
										.findViewById(R.id.yjbg_book_list_item_tv);
								final ProgressBar bar = (ProgressBar) arg1
										.findViewById(R.id.yjbg_book_list_item_pro);
								if (!report.getIsLoad().equals("yes")) {
									Handler handler = new Handler(getLooper()) {

										@Override
										public void handleMessage(Message msg) {
											if (msg.arg1 > 0) {
												bar.setProgress(msg.arg1);
												tv.setText(msg.arg1 + "%");
											}
											switch (msg.arg1) {
											case -1:
												// 路劲错误
												tv.setText("err");
												dataHelper.delReport(report
														.getId());
												loadThreads.remove(report.getId());
												initData();
												if(selfListAdapter!=null)
												selfListAdapter.notifyDataSetChanged();
												MyTools.exitShow(
														CeiShelfBookActivity.this,
														shelvesview, "下载文件为空！");
												break;
											case -2:
												tv.setText("err");
												dataHelper.delReport(report
														.getId());
												loadThreads.remove(report.getId());
												initData();
												if(selfListAdapter!=null)
												selfListAdapter.notifyDataSetChanged();
												MyTools.exitShow(
														CeiShelfBookActivity.this,
														shelvesview, "连接网络超时！");
												break;
											case -3:
												tv.setText("err");
												dataHelper.delReport(report
														.getId());
												loadThreads.remove(report.getId());
												initData();
												if(selfListAdapter!=null)
												selfListAdapter.notifyDataSetChanged();
												MyTools.exitShow(
														CeiShelfBookActivity.this,
														shelvesview, "文件不存在！");
												break;
											case -4:
												tv.setText("err");
												dataHelper.delReport(report
														.getId());
												loadThreads.remove(report.getId());
												initData();
												if(selfListAdapter!=null)
												selfListAdapter.notifyDataSetChanged();
												MyTools.exitShow(
														CeiShelfBookActivity.this,
														shelvesview, "URL地址错误！");
												break;
											case -5:
												tv.setText("err");
												dataHelper.delReport(report
														.getId());
												loadThreads.remove(report.getId());
												initData();
												if(selfListAdapter!=null)
												selfListAdapter.notifyDataSetChanged();
												MyTools.exitShow(
														CeiShelfBookActivity.this,
														shelvesview,
														"本地文件创建失败！");
												break;
											case -6:
												tv.setText("err");
												dataHelper.delReport(report
														.getId());
												loadThreads.remove(report.getId());
												initData();
												if(selfListAdapter!=null)
												selfListAdapter.notifyDataSetChanged();
												MyTools.exitShow(
														CeiShelfBookActivity.this,
														shelvesview, "数据读写错误！");
												break;
											case 100:
												tv.setText("100%");
												bar.setVisibility(View.GONE);
												report.setIsLoad("yes");
												dataHelper
														.UpdateReportZT(report);
												//统计
												Service.updatedownsum(report.getId(), "bg");
												// 下载完毕后减压文件
												porLayout.setVisibility(View.VISIBLE);
												setViewListener(data.get(arg2));
												loadThreads.remove(report
														.getId());
												break;
											}
										}

									};

									String path = report.getDownpath()
											.toString();
									File savedir = new File(Report.SD_PATH
											+ report.getName());
									if (!savedir.exists()) {
										savedir.mkdirs();
									}

									try {
										if (loadThreads.get(report.getId()) != null) {
											MyTools.exitShow(CeiShelfBookActivity.this,CeiShelfBookActivity.this.
													getWindow().getDecorView(),  "正在下载！");
											return;
										} else {
											NewFileDownload.download(path,
													savedir, handler);
											loadThreads.put(report.getId(), "1");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									// 下载完毕后减压文件
									porLayout.setVisibility(View.VISIBLE);
									setViewListener(data.get(arg2));
								}

							}
						});
				shelvesListView
						.setOnItemLongClickListener(new OnItemLongClickListener() {

							@Override
							public boolean onItemLongClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								final Report report = data.get(arg2);
								delViewShow(report, 20);
								return true;
							}
						});
				break;
			case 30:
				porLayout.setVisibility(View.GONE);
				break;
			default:
				break;
			}

			find.setOnClickListener(CeiShelfBookActivity.this);
			heng.setOnClickListener(CeiShelfBookActivity.this);
			shu.setOnClickListener(CeiShelfBookActivity.this);
		}

	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_report_bookshelf);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		application = (CeiApplication) getApplication();
		SharedPreferences settings = getSharedPreferences("BookSelfColor",
				Activity.MODE_PRIVATE);
		int col = settings.getInt("color", 0);
		number = settings.getInt("number", 0);
		if (col != 0) {
			findViewById(R.id.mainlayout).setBackgroundResource(col);
		}
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		dataHelper = ((CeiApplication) this.getApplication()).dataHelper;
		initView();
		initData();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		porLayout = (LinearLayout) findViewById(R.id.yjbg_bookself_pro);
		findText = (EditText) findViewById(R.id.read_report_bookself_et);
		find = (ImageView) findViewById(R.id.read_report_bookself_find);
		heng = (ImageView) findViewById(R.id.read_report_bookself_heng);
		shu = (ImageView) findViewById(R.id.read_report_bookself_shu);
		shelvesListView = (ListView) findViewById(R.id.yjbg_book_shelves);
		shezhi = (ImageView) findViewById(R.id.read_report_bookself_sz);
		shezhi.setOnClickListener(this);
		shelvesview = (ShelvesView) findViewById(R.id.grid_shelves);
		backImg = (ImageView) findViewById(R.id.ib_findbg_back);
		backImg.setOnClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 查询数据库报告253224
		new Thread() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				data.clear();
				// 此处在加导入的记录
				List<Report> reports = new ArrayList<Report>();
				if(synDownloadReport()!=null){
					reports.addAll(synDownloadReport());
				}
				if(dataHelper.getReportList()!=null){
					data.addAll(dataHelper.getReportList());
				}
				// 按时间排序 sdfsfdsfsff
				ComparatorReport comparator = new ComparatorReport(
						"time");
				Collections.sort(data, comparator);
				Message msg = new Message();
				msg.what = 10;
				handler.sendMessage(msg);
			}

		}.start();

	}

	/**
	 * 换背景
	 * 
	 * @param color
	 * @param back
	 */
	private void loadBackGround(int color, int back, int number) {
		/*
		 * RelativeLayout layout = (RelativeLayout)
		 * findViewById(R.id.mainlayout); layout.setBackgroundResource(color);
		 */
		// 保存颜色到数据库
		SharedPreferences settings = getSharedPreferences("BookSelfColor",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("color", color);
		editor.putInt("back", back);
		editor.putInt("number", number);
		editor.commit();
		startActivity(new Intent(CeiShelfBookActivity.this,
				CeiShelfBookActivity.class));
		this.finish();
	}

	@Override
	public void onClick(View v) {
		if (shezhi == v) {
			// 加载popupWindow的布局文件
			View contentView = LayoutInflater.from(getApplicationContext())
					.inflate(R.layout.read_report_bookself_popu, null);
			// 设置popupWindow的背景颜色
			// contentView.setBackgroundColor(Color.RED);
			// 声明一个弹出框
			final PopupWindow mPopupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
			mPopupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			if (number == 1) {
				contentView
						.findViewById(R.id.report_item_bg1)
						.setBackgroundResource(R.drawable.read_report_book_sky1);
				contentView.findViewById(R.id.report_item_bg2)
						.setBackgroundResource(R.drawable.read_report_book_qmu);
				contentView.findViewById(R.id.report_item_bg3)
						.setBackgroundResource(R.drawable.read_report_book_smu);
			} else if (number == 2) {
				contentView
						.findViewById(R.id.report_item_bg2)
						.setBackgroundResource(R.drawable.read_report_book_qmu1);
				contentView.findViewById(R.id.report_item_bg1)
						.setBackgroundResource(R.drawable.read_report_book_sky);
				contentView.findViewById(R.id.report_item_bg3)
						.setBackgroundResource(R.drawable.read_report_book_smu);
			} else if (number == 3) {
				contentView
						.findViewById(R.id.report_item_bg3)
						.setBackgroundResource(R.drawable.read_report_book_smu1);
				contentView.findViewById(R.id.report_item_bg1)
						.setBackgroundResource(R.drawable.read_report_book_sky);
				contentView.findViewById(R.id.report_item_bg2)
						.setBackgroundResource(R.drawable.read_report_book_qmu);
			} else if (number == 0) {
				contentView
						.findViewById(R.id.report_item_bg1)
						.setBackgroundResource(R.drawable.read_report_book_sky1);
				contentView.findViewById(R.id.report_item_bg2)
						.setBackgroundResource(R.drawable.read_report_book_qmu);
				contentView.findViewById(R.id.report_item_bg3)
						.setBackgroundResource(R.drawable.read_report_book_smu);
			}

			contentView.findViewById(R.id.report_item_bg1).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							application.bgindex = 1;
							loadBackGround(R.drawable.bookself_bg,
									R.drawable.shelf_panel, 1);

						}
					});
			contentView.findViewById(R.id.report_item_bg2).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							application.bgindex = 2;
							loadBackGround(
									R.drawable.read_report_bookshelf_bg_qmu,
									R.drawable.shelf_panel, 2);
						}
					});
			contentView.findViewById(R.id.report_item_bg3).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							application.bgindex = 3;
							loadBackGround(
									R.drawable.read_report_bookshelf_bg_smu,
									R.drawable.shelf_panel, 3);
						}
					});
			contentView.findViewById(R.id.report_item_sort1)
					.setOnClickListener(new OnClickListener() {

						@SuppressWarnings("unchecked")
						@Override
						public void onClick(View v) {
							// 按名称排序
							ComparatorReport comparator = new ComparatorReport(
									"name");
							Collections.sort(data, comparator);
							// 横活树
							if (hengORshu == 1) {
								selfListAdapter = new BookSelfListAdapter(
										CeiShelfBookActivity.this, data,
										shelvesListView);
								shelvesListView.setAdapter(selfListAdapter);
							} else {
								selfAdapter = new BookSelfAdapter(
										CeiShelfBookActivity.this, data,
										shelvesview);
								shelvesview.setAdapter(selfAdapter);
							}
							mPopupWindow.dismiss();
						}
					});
			contentView.findViewById(R.id.report_item_sort2)
					.setOnClickListener(new OnClickListener() {

						@SuppressWarnings("unchecked")
						@Override
						public void onClick(View v) {
							// 按时间排序
							ComparatorReport comparator = new ComparatorReport(
									"time");
							Collections.sort(data, comparator);
							// 横活树
							if (hengORshu == 1) {
								selfListAdapter = new BookSelfListAdapter(
										CeiShelfBookActivity.this, data,
										shelvesListView);
								shelvesListView.setAdapter(selfListAdapter);
							} else {
								selfAdapter = new BookSelfAdapter(
										CeiShelfBookActivity.this, data,
										shelvesview);
								shelvesview.setAdapter(selfAdapter);
							}
							mPopupWindow.dismiss();
						}
					});

			mPopupWindow.showAtLocation(findViewById(R.id.mainlayout),
					Gravity.RIGHT | Gravity.TOP, 0, 30);

		} else if (find == v) {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(findText.getWindowToken(), 0);
			if (findText.getText().toString() != null
					&& !findText.getText().toString().equals("")) {
				data.clear();
				data.addAll(dataHelper.getReportListById(findText.getText()
						.toString()));
			} else {
				data.clear();
				data.addAll(dataHelper.getReportList());
			}
			if(data!=null&&data.size()==0)
				MyTools.exitShow(this, this.getWindow().getDecorView(), "没有查到您需要的信息!");
			// 横活树
			if (hengORshu == 1) {
				Message msg = new Message();
				msg.what = 20;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = 10;
				handler.sendMessage(msg);
			}
		} else if (heng == v) {
			if (loadThreads.size() > 0) {
				//Toast.makeText(this, "下载完毕在切换视图！", 2).show();
				MyTools.exitShow(CeiShelfBookActivity.this,CeiShelfBookActivity.this.
						getWindow().getDecorView(),  "下载完毕再切换视图！");
				return;
			}
			findViewById(R.id.mainlayout).setBackgroundColor(Color.WHITE);
			/*
			 * if (application.bgindex == 2) {
			 * findViewById(R.id.mainlayout).setBackgroundResource(
			 * R.drawable.read_report_bookshelf_bg_qmu); } else if
			 * (application.bgindex == 3) {
			 * findViewById(R.id.mainlayout).setBackgroundResource(
			 * R.drawable.read_report_bookshelf_bg_smu); }
			 */
			// data.clear();
			// data.addAll(dataHelper.getReportList());
			shelvesListView.setVisibility(View.VISIBLE);
			shelvesview.setVisibility(View.GONE);
			Message msg = new Message();
			msg.what = 20;
			handler.sendMessage(msg);
			final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(findText.getWindowToken(), 0);
			hengORshu = 1;
		} else if (shu == v) {
			if (loadThreads.size() > 0) {
				//Toast.makeText(this, "下载完毕在切换视图！", 2).show();
				MyTools.exitShow(CeiShelfBookActivity.this,CeiShelfBookActivity.this.
						getWindow().getDecorView(),  "下载完毕再切换视图！");
				return;
			}
			if (application.bgindex == 2) {
				findViewById(R.id.mainlayout).setBackgroundResource(
						R.drawable.read_report_bookshelf_bg_qmu);
			} else if (application.bgindex == 3) {
				findViewById(R.id.mainlayout).setBackgroundResource(
						R.drawable.read_report_bookshelf_bg_smu);
			} else if (application.bgindex == 0) {
				findViewById(R.id.mainlayout).setBackgroundResource(
						R.drawable.read_report_bookself_bg);
			}
			// data.clear();
			// data.addAll(dataHelper.getReportList());
			shelvesListView.setVisibility(View.GONE);
			shelvesview.setVisibility(View.VISIBLE);
			Message msg = new Message();
			msg.what = 10;
			handler.sendMessage(msg);
			final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(findText.getWindowToken(), 0);
			hengORshu = 0;
		} else if (v == backImg) {
			Intent intent = new Intent(this, ReadReportMainActivity.class);
			startActivity(intent);
			this.finish();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		loadThreads.clear();
		super.onDestroy();
	}

	private void delViewShow(final Report report, final int what) {
		// TODO Auto-generated method stub
		// 加载popupWindow的布局文件
		View contentView = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.read_report_bookself_popu_delect, null);
		// 设置popupWindow的背景颜色
		// contentView.setBackgroundColor(Color.RED);
		// 声明一个弹出框
		final PopupWindow mPopupWindow = new PopupWindow(contentView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
		mPopupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		ImageView close = (ImageView) contentView
				.findViewById(R.id.yjbg_bookself_quan);
		ImageView bookxinx = (ImageView) contentView
				.findViewById(R.id.yjbg_bookself_xinx);
		ImageView bookdel = (ImageView) contentView
				.findViewById(R.id.yjbg_bookself_del);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		bookxinx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View contentView = LayoutInflater.from(
						CeiShelfBookActivity.this).inflate(
						R.layout.read_report_pop_intro, null);
				// 声明一个弹出框
				final PopupWindow mPopupWindow = new PopupWindow(contentView,
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				mPopupWindow.setFocusable(true);
				mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
				mPopupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopupWindow.showAtLocation(findViewById(R.id.mainlayout),
						Gravity.CENTER, 0, 30);
				TextView title = (TextView) contentView
						.findViewById(R.id.yjbg_pop_intro_title);
				title.setText(report.getName());
				TextView zhuz = (TextView) contentView
						.findViewById(R.id.yjbg_pop_intro_zuoz);
				zhuz.setText(report.getAuthor());
				TextView price = (TextView) contentView
						.findViewById(R.id.yjbg_pop_intro_price);
				price.setText(report.getPrice());
				TextView content = (TextView) contentView
						.findViewById(R.id.yjbg_pop_intro_content);
				content.setText(report.getIntro());
				ImageButton close = (ImageButton) contentView
						.findViewById(R.id.yjbg_pop_intro_close);
				close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mPopupWindow.dismiss();
					}
				});
			}
		});
		bookdel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				data.remove(report);
				if (dataHelper.delReport(report.getId()) == 0) {
					MyTools.exitShow(CeiShelfBookActivity.this, shelvesview,
							"删除失败");
					mPopupWindow.dismiss();
					return;
				} else {
					// 删除成功后把本地文件也删除
					String pdfPath = report.getDatapath();
					File dir = new File(pdfPath);
					delAll(dir);
					Message msg = new Message();
					msg.what = what;
					handler.sendMessage(msg);
					mPopupWindow.dismiss();
					MyTools.exitShow(CeiShelfBookActivity.this, shelvesview,
							"删除成功！");

				}
			}
		});
		mPopupWindow.showAtLocation(findViewById(R.id.mainlayout), Gravity.TOP,
				0, 200);
	}

	private void setViewListener(final Report report) {
		new Thread() {

			@Override
			public void run() {
				String pdfUri = null;
				String pdfPath = report.getDatapath();
				File dir = new File(pdfPath);
				if (!dir.exists() || dir.listFiles().length <= 0) {
					try {
						ZipUtils.unzip(Report.SD_PATH + report.getName()
								+ "/bg.zip",
								Report.SD_PATH + report.getFileName());

						delAll(new File(Report.SD_PATH + report.getName()));
					} catch (IOException e) {
						
						Message meg = new Message();
						meg.what = 404;
						meg.arg1=2;
						handler.sendMessage(meg);
						e.printStackTrace();
						return;
					}
				}
				File file[] = dir.listFiles();
				if (file != null) {
					for (int i = 0; i < file.length; i++) {
						if (!file[i].isDirectory()) {
							file[i].getName().lastIndexOf(".pdf");
							pdfUri = pdfPath + "/" + file[i].getName();// data.get(position).getDatapath()现在不正确
						}

					}
				}
				readReport(pdfUri, report);
			}

		}.start();
	}

	private void readReport(String pdfUri, Report report) {
		if (pdfUri != null && !pdfUri.equals("")) {
			if (report.getKey().equals("")) {
				handler.sendEmptyMessage(404);
			} else {
				// 此处解密阅读
				try {
					EncryptDecryption.DecryptionReport(
							pdfUri,
							report.getKey().substring(0,
									report.getKey().toString().length() - 1));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Uri uri = Uri.parse("file://" + pdfUri);// data.get(position).getDatapath()现在不正确+pdfUri
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				intent.putExtra("pdfPath", pdfUri);
				intent.putExtra("name", report.getName());
				intent.putExtra("report", report);
				String uriString = uri.toString();
				intent.setClass(CeiShelfBookActivity.this,
						PdfViewerActivity.class);
				startActivity(intent);
				ViewerPreferences preferences = new ViewerPreferences(
						CeiShelfBookActivity.this);
				preferences.putYourReads(uriString);
				Message meg = new Message();
				meg.what = 30;
				handler.sendMessage(meg);
			}
		} else {
			Message meg = new Message();
			meg.what = 404;
			meg.arg1 = 1;
			handler.sendMessage(meg);
		}
	}

	public boolean delAll(File f) {
		if (!f.exists())// 文件夹不存在不存在
		{
			//MyTools.exitShow(this, porLayout, "本地文件不存在！");
			return true;
		}
		boolean rslt = true;// 保存中间结果
		if (!(rslt = f.delete())) {// 先尝试直接删除
			// 若文件夹非空。枚举、递归删除里面内容
			File subs[] = f.listFiles();
			if (subs.length > 0) {
				for (int i = 0; i <= subs.length - 1; i++) {
					if (subs[i].isDirectory())
						delAll(subs[i]);// 递归删除子文件夹内容
					rslt = subs[i].delete();// 删除子文件夹本身
				}
				rslt = f.delete();// 删除此文件夹本身
			}
		}
		if (!rslt) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 同步离线列表
	 */
	private List<Report> synDownloadReport() {
		List<Report> reports = new ArrayList<Report>();
		StringBuilder fileNames = new StringBuilder();
		File file = new File(MyTools.RESOURCE_PATH + "pdf");
		if (!file.exists()) {
			file.mkdirs();
		}
		if (file.list().length > 0) {
			for (int i = 0; i < file.list().length; i++) {
				String fileName = file.list()[i];
				fileNames.append(fileName + ",");
			}
			// 通过报告的部分路径去获取报告的信息集合
			String result = Service.queryPassKeyBuyReport(fileNames.toString()
					.substring(0, fileNames.length() - 1));
			try {
				if (result
						.equals("<?xml version='1.0' encoding='utf-8'?><ROOT>5</ROOT>")) {
					//Toast.makeText(this, "同步错误", 1).show();
					Message msg=new Message();
					msg.what=404;
					msg.arg1=3;
					handler.sendMessage(msg);
				} else {
					reports = XmlUtil.parseReport(result);
				}
				result = Service.queryBuyReport(((CeiApplication) (this
						.getApplication())).columnEntry.getUserId());
				for (int i = 0; i < reports.size(); i++) {
					// 判断是不是合法的报告，如果合法的话把本地的目录设置进去。
					if (result.contains(reports.get(i).getId())) {
						if (dataHelper.getReportListById(
								reports.get(i).getName()).size() < 1) {// 判断数据库中是否有记录
							Report report = reports.get(i);
							String fileTime = System.currentTimeMillis() + "";
							String fileName = report.getDownpath().replace(
									"/bg.zip", "");
							fileName = fileName.substring(fileName
									.lastIndexOf("/") + 1);
							// 保存数据库
							report.setReadtime(fileTime);
							report.setFileName(fileName);
							report.setDatapath(MyTools.RESOURCE_PATH + "pdf/"
									+ fileName);
							long retuCoad = dataHelper.saveReport(report);
							if (retuCoad != -1) {
								report.setIsLoad("yes");
								dataHelper.UpdateReportZT(report);
							} else {
								Message msg=new Message();
								msg.what=404;
								msg.arg1=3;
								handler.sendMessage(msg);
								//Toast.makeText(this, "同步错误", 1).show();
							}
						}
					} else {
						new File(reports.get(i).getDownpath()).delete();
						reports.remove(i);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reports;
	}
}