package com.poqop.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.hyrt.cei.util.EncryptDecryption;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.vo.Report;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.cei.adapter.ReportMuluAdapter;
import com.hyrt.readreport.ReadReportMainActivity;
import com.poqop.document.Dao.MyReadDao;
import com.poqop.document.events.CurrentPageListener;
import com.poqop.document.events.DecodingProgressListener;
import com.poqop.document.models.CurrentPageModel;
import com.poqop.document.models.DecodingProgressModel;
import com.poqop.document.models.ZoomModel;

public abstract class BaseViewerActivity extends ContainerActivity implements
		DecodingProgressListener, CurrentPageListener, OnItemSelectedListener {
	private static final String DOCUMENT_VIEW_STATE_PREFERENCES = "DjvuDocumentViewState";
	private DecodeService decodeService;
	private DocumentView documentView;
	private ViewerPreferences viewerPreferences;
	private Toast pageNumberToast;
	private CurrentPageModel currentPageModel;
	private LayoutInflater inflater;
	private ImageView home, mulu, fontsize, light, addshuqian, shuqian;
	FrameLayout frameLayout;
	ZoomModel zoomModel;
	private MyReadDao dao;
	// private Gallery gallery;
	private Bitmap[] mImageIds;
	// private ImageAdapter adapter;
	GestureDetector gestureScanner;
	private List<HashMap<String, String>> xml;
	private float oldFontSize;
	private Report report;
	private List<PopupWindow> poplist;
	private String pdfPath;
	private int startLight;
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			HorizontalScrollView ScrollView = (HorizontalScrollView) findViewById(R.id.ScrollView);
			ScrollView.setFocusable(true);
			ScrollView.smoothScrollTo(240, 0);
		}

	};

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Toast.makeText(this, "双击屏幕放大，长按屏幕缩小", 0).show();
		initDecodeService();
		poplist = new ArrayList<PopupWindow>();
		inflater = LayoutInflater.from(this);
		zoomModel = new ZoomModel();
		final DecodingProgressModel progressModel = new DecodingProgressModel();
		progressModel.addEventListener(this);
		currentPageModel = new CurrentPageModel();
		currentPageModel.addEventListener(this);
		documentView = new DocumentView(this, zoomModel, progressModel,
				currentPageModel);
		zoomModel.addEventListener(documentView);
		documentView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));
		decodeService.setContentResolver(getContentResolver());
		decodeService.setContainerView(documentView);
		documentView.setDecodeService(decodeService);

		try {
			decodeService.open(getIntent().getData());
			MyTools.exitShow(this, documentView, "pdf解析错误");
		} catch (Exception e) {

			e.printStackTrace();
		}

		viewerPreferences = new ViewerPreferences(this);

		dao = new MyReadDao(this);
		View contextView = inflater.inflate(R.layout.read_report, null);
		frameLayout = (FrameLayout) contextView
				.findViewById(R.id.yjbg_read_report_pdfview);
		frameLayout.addView(documentView);
		// frameLayout.addView(createZoomControls(zoomModel));
		// frameLayout.addView(gallery);
		// 初始化首选项，加以读，独到多少页等
		setFullScreen();
		setContentView(contextView);
		initView();
		// 低栏的沉浮
		ImageView imageView = (ImageView) contextView
				.findViewById(R.id.yjbg_read_report_menu_is);
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				findViewById(R.id.yjbg_read_report_menu).setVisibility(
						View.VISIBLE);
				mHandler.sendEmptyMessage(1);
			}
		});
		ImageView imageBottom = (ImageView) contextView
				.findViewById(R.id.yjbg_read_report_over);
		ImageView imageBottom1 = (ImageView) contextView
				.findViewById(R.id.yjbg_read_report_over1);
		imageBottom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				findViewById(R.id.yjbg_read_report_menu).setVisibility(
						View.GONE);
			}
		});
		imageBottom1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				findViewById(R.id.yjbg_read_report_menu).setVisibility(
						View.GONE);
			}
		});
		TextView title = (TextView) findViewById(R.id.yjbg_read_report_title);
		title.setText(getIntent().getStringExtra("name"));
		report = (Report) getIntent().getExtras().get("report");
		if (getIntent() != null) {
			pdfPath = getIntent().getData().toString().replace("file://", "");// getIntent().getExtras().get("pdfPath").toString();
		}
		final SharedPreferences sharedPreferences = getSharedPreferences(
				DOCUMENT_VIEW_STATE_PREFERENCES, 0);
		documentView.goToPage(sharedPreferences.getInt(getIntent().getData()
				.toString(), 0));
		documentView.showDocument();

		viewerPreferences.addRecent(getIntent().getData());
		/*
		 * HorizontalScrollView
		 * ScrollView=(HorizontalScrollView)findViewById(R.id.ScrollView);
		 * ScrollView.setFocusable(true);
		 * ScrollView.smoothScrollTo(ScrollView.getWidth()/2, 0);
		 */

	}

	/*
	 * @Override public boolean onContextItemSelected(MenuItem item) { // TODO
	 * Auto-generated method stub int i = item.getItemId(); switch (i) { case 0:
	 * gallery.setVisibility(View.GONE); break; } return
	 * super.onContextItemSelected(item); }
	 */

	public void decodingProgressChanged(final int currentlyDecoding) {
		runOnUiThread(new Runnable() {
			public void run() {
				getWindow().setFeatureInt(
						Window.FEATURE_INDETERMINATE_PROGRESS,
						currentlyDecoding == 0 ? 10000 : currentlyDecoding);
			}
		});
	}

	// 显示当前页面
	public void currentPageChanged(int pageIndex) {
		final String pageText = (pageIndex + 1) + "/"
				+ decodeService.getPageCount();
		if (pageNumberToast != null) {

			pageNumberToast.setText(pageText);
		} else {

			pageNumberToast = Toast.makeText(this, pageText, 300);
		}
		pageNumberToast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
		// pageNumberToast.show();
		saveCurrentPage();
	}

	private void setWindowTitle() {
		final String name = getIntent().getData().getLastPathSegment();
		getWindow().setTitle(name);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setWindowTitle();
	}

	private void setFullScreen() {
		if (viewerPreferences.isFullScreen()) {
			getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		}
	}

	/*
	 * private PageViewZoomControls createZoomControls(ZoomModel zoomModel) {
	 * 
	 * final PageViewZoomControls controls = new PageViewZoomControls(this,
	 * zoomModel); controls.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
	 * zoomModel.addEventListener(controls); return controls; }
	 */
	private void initDecodeService() {
		if (decodeService == null) {
			decodeService = createDecodeService();
		}
	}

	protected abstract DecodeService createDecodeService();

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// 文件加密
		try {
			EncryptDecryption.EncryptReport(pdfPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		decodeService.recycle();
		decodeService = null;
		super.onDestroy();
	}

	private void saveCurrentPage() {
		final SharedPreferences sharedPreferences = getSharedPreferences(
				DOCUMENT_VIEW_STATE_PREFERENCES, 0);
		final SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(getIntent().getData().toString(),
				documentView.getCurrentPage());
		editor.commit();
	}

	OnClickListener onselect = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			HashMap<String, String> map = xml.get(which);
			String page = map.get("page");
			documentView.goToPage(Integer.parseInt(page));
		}
	};

	

	private void initView() {
		home = (ImageView) findViewById(R.id.yjbg_read_report_home);
		home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BaseViewerActivity.this,
						ReadReportMainActivity.class);
				startActivity(intent);
				BaseViewerActivity.this.finish();
			}
		});
		mulu = (ImageView) findViewById(R.id.yjbg_read_report_mulu);
		mulu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 显示目录
				// pop实现字体大小调节
				View contentView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.read_report_pop_mulu, null);
				final PopupWindow mPopupWindow = new PopupWindow(contentView,
						350, 500);
				ListView view = (ListView) contentView
						.findViewById(R.id.yjbg_mulu_lv);
				List<String> muluData = new ArrayList<String>();
				// muluData.add("aaa");
				/*
				 * if (report != null && report.getMulu() != null &&
				 * !report.getMulu().equals("")) { String muluStr =
				 * report.getMulu(); String[] strs = muluStr.split("页"); for
				 * (String string : strs) { muluData.add(string+"页"); } }
				 * if(muluData.size()>0){ view.setAdapter(new
				 * ReportMuluAdapter(BaseViewerActivity.this, muluData));
				 * view.setOnItemClickListener(new OnItemClickListener() {
				 * 
				 * @Override public void onItemClick(AdapterView<?> arg0, View
				 * arg1, int arg2, long arg3) { int page=0; try { String item =
				 * arg0.getAdapter().getItem(arg2) .toString(); String[] strs =
				 * item.split(","); if(strs.length!=2){ strs = item.split("，");
				 * } if(strs.length==2){ page =
				 * Integer.parseInt(strs[1].replace("页",""));
				 * if(page>documentView.pages.size()){
				 * Toast.makeText(BaseViewerActivity.this, "页数不对！", 2).show();
				 * return; } documentView.goToPage(page-1); for(PopupWindow
				 * window:poplist){ if(window.isShowing()){ window.dismiss(); }
				 * } } } catch (NumberFormatException e) { // TODO
				 * Auto-generated catch block
				 * Toast.makeText(BaseViewerActivity.this,"目录格式不对，联系管理员",
				 * 2).show(); mPopupWindow.dismiss(); e.printStackTrace(); } }
				 * }); }
				 */
				if (report != null && report.getMulu() != null
						&& !report.getMulu().equals("")) {
					String muluStr = report.getMulu();
					String[] strs = muluStr.split("\n");// "页"
					for (String string : strs) {
						muluData.add(string);// +"页"
					}

				}
				if (muluData.size() > 0) {
					view.setAdapter(new ReportMuluAdapter(
							BaseViewerActivity.this, muluData));
					view.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							int page = 0;
							try {
								String item = arg0.getAdapter().getItem(arg2)
										.toString();
								/*
								 * String[] strs = item.split(",");
								 * if(strs.length!=2){ strs = item.split("，"); }
								 */

								// if(strs.length==2){
								String regex = "\\d{1,}$";

								Pattern p = Pattern.compile(regex);
								Matcher matcher = p.matcher(item);
								if (matcher.find()) {
									String count = matcher.group();
									page = Integer.parseInt(count);// strs[1].replace("页","")
								}
								if (page > documentView.pages.size()
										|| page == 0) {
									Toast.makeText(BaseViewerActivity.this,
											"页数不对！", 2).show();
									return;
								}
								documentView.goToPage(page - 1);
								for (PopupWindow window : poplist) {
									if (window.isShowing()) {
										window.dismiss();
									}
								}
								// }
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								Toast.makeText(BaseViewerActivity.this,
										"目录格式不对，联系管理员", 2).show();
								mPopupWindow.dismiss();
								e.printStackTrace();
							}
						}
					});
				}

				// 声明一个弹出框
				mPopupWindow.setFocusable(true);
				mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
				mPopupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopupWindow.showAtLocation(documentView, Gravity.BOTTOM
						| Gravity.LEFT, 0, 80);
				poplist.add(mPopupWindow);
				// 获取到页数
				/*
				 * int pageCount=decodeService.getPageCount();
				 * documentView.goToPage(pageCount);
				 * gallery.setVisibility(View.VISIBLE);
				 * gallery.setSelection(documentView.getCurrentPage());
				 */
			}
		});
		fontsize = (ImageView) findViewById(R.id.yjbg_read_report_fontsize);
		fontsize.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				oldFontSize = zoomModel.getZoom(); // 原始值是1
				// frameLayout.addView(createZoomControls(zoomModel));
				// pop实现字体大小调节
				View contentView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.read_report_light, null);
				// 声明一个弹出框
				final PopupWindow mPopupWindow = new PopupWindow(contentView,
						450, 300);
				mPopupWindow.setFocusable(true);
				mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
				mPopupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopupWindow
						.showAtLocation(documentView, Gravity.CENTER, 0, 30);
				TextView title = (TextView) contentView
						.findViewById(R.id.yjbg_light_title);
				ImageButton jiebut = (ImageButton) contentView
						.findViewById(R.id.yjbg_light_jie);
				ImageButton jiabut = (ImageButton) contentView
						.findViewById(R.id.yjbg_light_jia);
				ImageButton exitbut = (ImageButton) contentView
						.findViewById(R.id.yjbg_light_exit);
				ImageButton yesbut = (ImageButton) contentView
						.findViewById(R.id.yjbg_light_yes);
				final SeekBar bar = (SeekBar) contentView
						.findViewById(R.id.yjbg_light_seekbar);
				bar.setProgress((int) ((zoomModel.getZoom() - 1) * 10));
				title.setText("字体大小调节");

				bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// float size = zoomModel.getZoom();
						zoomModel.setZoom((float) progress / 10f + 1);
						zoomModel.commit();
					}
				});
				jiabut.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						float size = zoomModel.getZoom();
						bar.setProgress(bar.getProgress() + 10);
						zoomModel.setZoom(size + bar.getProgress() / 10f);
						zoomModel.commit();
					}
				});
				jiebut.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						float size = zoomModel.getZoom();
						bar.setProgress(bar.getProgress() - 10);
						zoomModel.setZoom(size - bar.getProgress() / 10f);
						zoomModel.commit();
					}
				});
				exitbut.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						zoomModel.setZoom(oldFontSize);
						zoomModel.commit();
						for (PopupWindow window : poplist) {
							if (window.isShowing()) {
								window.dismiss();
							}
						}
					}
				});
				yesbut.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						for (PopupWindow window : poplist) {
							if (window.isShowing()) {
								window.dismiss();
							}
						}
					}
				});
				poplist.add(mPopupWindow);
			}

		});
		light = (ImageView) findViewById(R.id.yjbg_read_report_light);
		light.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// pop实现屏幕的亮度调节
				View contentView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.read_report_light, null);
				// 声明一个弹出框
				final PopupWindow mPopupWindow = new PopupWindow(contentView,
						450, 300);
				mPopupWindow.setFocusable(true);
				mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
				mPopupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopupWindow
						.showAtLocation(documentView, Gravity.CENTER, 0, 30);
				ImageButton jiebut = (ImageButton) contentView
						.findViewById(R.id.yjbg_light_jie);
				ImageButton jiabut = (ImageButton) contentView
						.findViewById(R.id.yjbg_light_jia);
				ImageButton exitbut = (ImageButton) contentView
						.findViewById(R.id.yjbg_light_exit);
				ImageButton yesbut = (ImageButton) contentView
						.findViewById(R.id.yjbg_light_yes);
				final SeekBar bar = (SeekBar) contentView
						.findViewById(R.id.yjbg_light_seekbar);
				bar.setMax(255);
				startLight = getScreenBrightness(BaseViewerActivity.this);
				bar.setProgress(startLight);
				bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						if (progress > 20) {
							changeLight(progress);
						}
					}
				});
				// 事件
				jiebut.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						int now = bar.getProgress();
						if (now <= 20) {
							for (PopupWindow window : poplist) {
								if (window.isShowing()) {
									window.dismiss();
								}
							}
							return;
						}
						bar.setProgress(now - 20);
						changeLight(now - 20);
					}
				});
				jiabut.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						int now = bar.getProgress();
						bar.setProgress(now + 20);
						changeLight(now + 20);
					}
				});
				exitbut.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						bar.setProgress(startLight);
						changeLight(startLight);
						for (PopupWindow window : poplist) {
							if (window.isShowing()) {
								window.dismiss();
							}
						}
						return;
					}
				});
				yesbut.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						saveBrightness(
								BaseViewerActivity.this.getContentResolver(),
								bar.getProgress());
						for (PopupWindow window : poplist) {
							if (window.isShowing()) {
								window.dismiss();
							}
						}
					}
				});
				poplist.add(mPopupWindow);
			}
		});
		addshuqian = (ImageView) findViewById(R.id.yjbg_read_report_addshuqian);
		addshuqian.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// pop实现字体大小调节
				View contentView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.read_report_pop_add_shuqian, null);
				final PopupWindow mPopupWindow = new PopupWindow(contentView,
						450, 300);
				mPopupWindow.setFocusable(true);
				mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
				mPopupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopupWindow
						.showAtLocation(documentView, Gravity.CENTER, 0, 30);
				ImageButton yesBut = (ImageButton) contentView
						.findViewById(R.id.yjbg_add_shuqian_yes);
				ImageButton exitBut = (ImageButton) contentView
						.findViewById(R.id.yjbg_add_shuqian_exit);
				yesBut.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (dao.getMyRead(documentView.getCurrentPage() + "",
								viewerPreferences.getYourRead())) {
							/*
							 * Toast.makeText(BaseViewerActivity.this, "书签已存在!",
							 * 2).show();
							 */
							MyTools.exitShow(BaseViewerActivity.this,
									BaseViewerActivity.this.getWindow()
											.getDecorView(), "书签已存在!");
							if (mPopupWindow.isShowing()) {
								mPopupWindow.dismiss();
							}
							return;
						}
						long id = dao.addMyRead(
								viewerPreferences.getYourRead(),
								documentView.getCurrentPage() + "",
								report.getKey());
						if (id != -1) {
							/*
							 * Toast.makeText(BaseViewerActivity.this,
							 * "添加书签成功!", 2).show();
							 */
							MyTools.exitShow(BaseViewerActivity.this,
									BaseViewerActivity.this.getWindow()
											.getDecorView(), "添加书签成功!");
							if (mPopupWindow.isShowing()) {
								mPopupWindow.dismiss();
							}
						} else {
							/*
							 * Toast.makeText(BaseViewerActivity.this,
							 * "添加书签失败!", 2).show();
							 */
							MyTools.exitShow(BaseViewerActivity.this,
									BaseViewerActivity.this.getWindow()
											.getDecorView(), "添加书签失败!");
							if (mPopupWindow.isShowing()) {
								mPopupWindow.dismiss();
							}
						}
						for (PopupWindow window : poplist) {
							if (window.isShowing()) {
								window.dismiss();
							}
						}
					}
				});
				exitBut.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mPopupWindow.dismiss();
					}
				});
				poplist.add(mPopupWindow);
			}
		});
		shuqian = (ImageView) findViewById(R.id.yjbg_read_report_shuqian);
		shuqian.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// pop实现字体大小调节
				View contentView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.read_report_pop_mulu, null);
				final PopupWindow mPopupWindow = new PopupWindow(contentView,
						350, 250);
				ListView view = (ListView) contentView
						.findViewById(R.id.yjbg_mulu_lv);

				view.setAdapter(new YourReadAdapter(BaseViewerActivity.this,
						viewerPreferences.getYourRead(), getIntent()
								.getStringExtra("name") != null ? getIntent()
								.getStringExtra("name") : ""));
				view.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						@SuppressWarnings("unchecked")
						Map<String, String> map = (Map<String, String>) arg0
								.getAdapter().getItem(arg2);
						// 解密文件
						if (map.get("key") == null || map.get("key").equals("")) {
							Toast.makeText(BaseViewerActivity.this,
									"后台加密错误，联系谭杰、吴明杰!", 2).show();
						} else {
							// 此处解密阅读
							try {
								EncryptDecryption.Decryption(
										map.get("book_path"),
										map.get("key").substring(
												0,
												map.get("key").toString()
														.length() - 1));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							documentView.goToPage(Integer.parseInt(map
									.get("pageNo")));
							for (PopupWindow window : poplist) {
								if (window.isShowing()) {
									window.dismiss();
								}
							}
						}
					}
				});
				// 声明一个弹出框
				mPopupWindow.setFocusable(true);
				mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
				mPopupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
				mPopupWindow.showAtLocation(documentView, Gravity.BOTTOM
						| Gravity.RIGHT, 0, 80);

				/*
				 * Intent intent=new Intent(BaseViewerActivity.this,
				 * BookQianActivity.class); startActivity(intent);
				 */
				poplist.add(mPopupWindow);
			}

		});
	}

	/** * 获取屏幕的亮度 */

	public static int getScreenBrightness(Activity activity) {

		int nowBrightnessValue = 0;

		ContentResolver resolver = activity.getContentResolver();

		try {

			nowBrightnessValue = android.provider.Settings.System.getInt(
					resolver, Settings.System.SCREEN_BRIGHTNESS);

		}

		catch (Exception e) {

			e.printStackTrace();

		}

		return nowBrightnessValue;
	}

	// 那如何修改屏幕的亮度呢？

	/** * 设置亮度 */

	public static void setBrightness(Activity activity, int brightness) {

		// Settings.System.putInt(activity.getContentResolver(),

		// Settings.System.SCREEN_BRIGHTNESS_MODE,

		// Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

		lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
		Log.d("lxy", "set  lp.screenBrightness == " + lp.screenBrightness);

		activity.getWindow().setAttributes(lp);
	}

	// 那么，能设置了，但是为什么还是会出现，设置了，没反映呢？

	// 嘿嘿，那是因为，开启了自动调节功能了，那如何关闭呢？这才是最重要的：

	/** * 停止自动亮度调节 */

	public static void stopAutoBrightness(Activity activity) {

		Settings.System.putInt(activity.getContentResolver(),

		Settings.System.SCREEN_BRIGHTNESS_MODE,

		Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}

	// 能开启，那自然应该能关闭了哟哟，那怎么关闭呢？很简单的：

	/**
	 * * 开启亮度自动调节 *
	 * 
	 * @param activity
	 */

	public static void startAutoBrightness(Activity activity) {

		Settings.System.putInt(activity.getContentResolver(),

		Settings.System.SCREEN_BRIGHTNESS_MODE,

		Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

	}

	// 至此，应该说操作亮度的差不多都有了，结束！
	// 哎，本来认为是应该结束了，但是悲剧得是，既然像刚才那样设置的话，只能在当前的activity中有作用，一段退出的时候，会发现毫无作用，悲剧，原来是忘记了保存了。汗！

	/** * 保存亮度设置状态 */

	public static void saveBrightness(ContentResolver resolver, int brightness) {

		Uri uri = android.provider.Settings.System
				.getUriFor("screen_brightness");

		android.provider.Settings.System.putInt(resolver, "screen_brightness",
				brightness);

		// resolver.registerContentObserver(uri, true, myContentObserver);

		resolver.notifyChange(uri, null);
	}

	/** * 判断是否开启了自动亮度调节 */

	public static boolean isAutoBrightness(ContentResolver aContentResolver) {

		boolean automicBrightness = false;

		try {

			automicBrightness = Settings.System.getInt(aContentResolver,

			Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;

		}

		catch (SettingNotFoundException e)

		{

			e.printStackTrace();

		}

		return automicBrightness;
	}

	private void changeLight(int size) {
		if (isAutoBrightness(this.getContentResolver())) {
			stopAutoBrightness(this);
			setBrightness(this, size);
		} else {
			setBrightness(this, size);
		}
	}

	/*
	 * @Override public void onItemSelected(AdapterView<?> arg0, View arg1, int
	 * arg2, long arg3) { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onNothingSelected(AdapterView<?> arg0) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// 两点触屏后之间的长度
	private float beforeLenght;
	private float afterLenght;

	/*
	 * 通过多点触屏放大或缩小图像 beforeLenght用来保存前一时间两点之间的距离 afterLenght用来保存当前时间两点之间的距离
	 */
	public void scaleWithFinger(MotionEvent event) {
		float moveX = event.getX(1) - event.getX(0);
		float moveY = event.getY(1) - event.getY(0);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			beforeLenght = (float) Math.sqrt((moveX * moveX) + (moveY * moveY));
			break;
		case MotionEvent.ACTION_MOVE:
			// 得到两个点之间的长度
			afterLenght = (float) Math.sqrt((moveX * moveX) + (moveY * moveY));

			float gapLenght = afterLenght - beforeLenght;

			if (gapLenght == 0) {
				break;
			}

			// 如果当前时间两点距离大于前一时间两点距离，则传0，否则传1
			if (gapLenght > 3) {
				float size=zoomModel.getZoom();
				zoomModel.setZoom(size + 0.2f);
				zoomModel.commit();
			} else if (gapLenght < 3){
				float size=zoomModel.getZoom();
				if(size>1){
				zoomModel.setZoom(size - 0.2f);
				zoomModel.commit();
				}
			}

			beforeLenght = afterLenght;
			break;
		}
	}

	/*// 这里来监听屏幕触控时间
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		
		 * 判定用户是否触摸到了图片 如果是单点触摸则调用控制图片移动的方法 如果是2点触控则调用控制图片大小的方法
		 

		if (event.getPointerCount() == 2) {
			scaleWithFinger(event);
		}
		return true;
	}*/
}
