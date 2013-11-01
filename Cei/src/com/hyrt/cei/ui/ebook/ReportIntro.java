package com.hyrt.cei.ui.ebook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.vudroid.pdfdroid.PdfViewerActivity;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.ui.information.funId;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.EncryptDecryption;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.ZipUtils;
import com.hyrt.cei.util.AsyncImageLoader.ImageCallback;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Report;
import com.poqop.document.ViewerPreferences;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ReportIntro extends Activity {
	private TextView textJianj, textMul;
	private TextView zhuz, price, zhuzbq, pricebq,time, title;
	private ImageView reportImg;
	private ImageButton read;
	private Report report;
	private DataHelper dataHelper;
	private AsyncImageLoader asyncImageLoader;
	private boolean flage;
	private CeiApplication application;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == -1) {
				//Toast.makeText(ReportIntro.this, "文件保存错误", 1).show();
				MyTools.exitShow(ReportIntro.this,ReportIntro.this.
						getWindow().getDecorView(),  "文件保存错误!");
			} else {
				startActivity(new Intent(ReportIntro.this,
						CeiShelfBookActivity.class));
				finish();
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_intro);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		report = (Report) bundle.get("report");
		asyncImageLoader = ((CeiApplication) this.getApplication()).asyncImageLoader;
		dataHelper = ((CeiApplication) this.getApplication()).dataHelper;
		application=(CeiApplication) getApplication();
		initView();

	}

	private void initView() {
		findViewById(R.id.ib_bg_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findViewById(R.id.report_intro_bookshelf).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ReportIntro.this,
								CeiShelfBookActivity.class);
						startActivity(intent);
						finish();
					}
				});
		textJianj = (TextView) findViewById(R.id.report_intro_jiej);
		textJianj.setText(report.getIntro().replace("\n", "").trim());
		textMul = (TextView) findViewById(R.id.report_intro_mul);
		textMul.setText(report.getMulu());
		zhuz = (TextView) findViewById(R.id.report_intro_zuoz);
		zhuz.setText(report.getAuthor());
		price = (TextView) findViewById(R.id.report_intro_price);
		zhuzbq=(TextView) findViewById(R.id.report_intro_zuoz_bq);
		zhuzbq.setText(application.ReportColumns.get(0).getAuthro()==null?"作者：":application.ReportColumns.get(0).getAuthro()+":");
		pricebq=(TextView) findViewById(R.id.report_intro_price_bq);
		pricebq.setText(application.ReportColumns.get(0).getPrice()==null?"价格：":application.ReportColumns.get(0).getPrice()+":");
		if(ReadReportActivity.bbStart){
			price.setText(report.getPrice());
		}else{
			price.setVisibility(View.GONE);
			pricebq.setVisibility(View.GONE);
		}
		time = (TextView) findViewById(R.id.report_intro_time);
		time.setText(report.getProtime());
		title = (TextView) findViewById(R.id.report_intro_content);
		title.setText(report.getName());
		reportImg = (ImageView) findViewById(R.id.report_intro_book);
		// 加载图片
		ImageResourse imageResource = new ImageResourse();
		imageResource.setIconUrl(report.getSmallPpath());
		imageResource.setIconId(report.getId());
		imageResource.setIconTime(report.getProtime());
		asyncImageLoader.loadDrawable(imageResource, new ImageCallback() {

			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {

				if (reportImg != null && imageDrawable != null) {
					// img.setLayoutParams(new Gallery.LayoutParams(360, 160));
					reportImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
					reportImg.setImageDrawable(imageDrawable);
				}

			}
		});
		read = (ImageButton) findViewById(R.id.report_intro_read);
		// 判断数据库中是否有记录
		if (dataHelper.getReportListById(report.getName()).size() < 1) {
			read.setImageResource(R.drawable.bg_bookdownload_but);
			read.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 下载加入书架
					if (!((CeiApplication) ReportIntro.this.getApplication())
							.isNet()) {
						MyTools.exitShow(ReportIntro.this,
								findViewById(R.id.report_intro_main),
								"网络连接错误！请检查网络");
						return;
					}
					if (report.getIsFree() != null
							&& report.getIsFree().equals("1")) {

						new Thread() {

							@Override
							public void run() {
								String fileTime = System.currentTimeMillis()
										+ "";
								String fileName = report.getDownpath().replace(
										"/bg.zip", "");
								fileName = fileName.substring(fileName
										.lastIndexOf("/") + 1);
								// 保存数据库
								report.setReadtime(fileTime);
								report.setFileName(fileName);
								report.setDatapath(Report.SD_PATH + fileName);
								long retuCoad = dataHelper.saveReport(report);
								report.setIsLoad("start");
								dataHelper.UpdateReportZT(report);
								Message msg = new Message();
								msg.arg1 = (int) retuCoad;
								handler.sendMessage(msg);
							}

						}.start();
					} else {// 收费报告，需要判断是否购买？
						List<funId> buyReportData = ((CeiApplication) getApplication()).buyReportData;
						Set<funId> set = new HashSet<funId>();
						set.addAll(buyReportData);
						List<funId> newlist = new ArrayList<funId>();
						newlist.addAll(set);
						if (newlist != null && newlist.size() > 0) {
							for (funId funId : newlist) {
								if (funId.getFunid().equals(report.getId())) {
									flage=true;//表示此报告已经购买！
									new Thread() {

										@Override
										public void run() {
											String fileTime = System
													.currentTimeMillis() + "";
											String fileName = report
													.getDownpath().replace(
															"/bg.zip", "");
											fileName = fileName.substring(fileName
													.lastIndexOf("/") + 1);
											// 保存数据库
											report.setReadtime(fileTime);
											report.setFileName(fileName);
											report.setDatapath(Report.SD_PATH
													+ fileName);
											long retuCoad = dataHelper
													.saveReport(report);
											report.setIsLoad("start");
											dataHelper.UpdateReportZT(report);
											Message msg = new Message();
											msg.arg1 = (int) retuCoad;
											handler.sendMessage(msg);
										}

									}.start();
								} 
							}
							if(!flage){
								MyTools.exitShow(ReportIntro.this,
										findViewById(R.id.report_intro_main),
										"请您去网站购买后下载！");
							}
						} else {
							MyTools.exitShow(ReportIntro.this,
									findViewById(R.id.report_intro_main),
									"请您去网站购买后下载！");
						}

					}

				}
			});

		} else {
			read.setImageResource(R.drawable.yjbg_read);
			read.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String pdfUri = null;
					Report nowReport = dataHelper.getReportListById(
							report.getName()).get(0);
					String pdfPath = nowReport.getDatapath();
					File dir = new File(pdfPath);
					File file[] = dir.listFiles();
					if (file == null) {
						MyTools.exitShow(ReportIntro.this,
								findViewById(R.id.report_intro_main),
								"文件还没有下载完成，请到书架下载！");
						return;
					}
					for (int i = 0; i < file.length; i++) {
						if (!file[i].isDirectory()) {
							file[i].getName().lastIndexOf(".pdf");
							pdfUri = pdfPath+"/" + file[i].getName();// data.get(position).getDatapath()现在不正确
						}

					}
					if (report.getKey().equals("")) {
						MyTools.exitShow(ReportIntro.this,
								findViewById(R.id.report_intro_main), "后台加密错误！");
					} else {
						// 此处解密阅读
						try {
							EncryptDecryption.DecryptionReport(
									pdfUri,
									report.getKey()
											.substring(
													0,
													report.getKey().toString()
															.length() - 1));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Uri uri = Uri.parse("file://" + pdfUri);// data.get(position).getDatapath()现在不正确
																// +pdfUri
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						intent.putExtra("name", nowReport.getName());
						intent.putExtra("pdfPath", pdfUri);
						intent.putExtra("report", report);
						String uriString = uri.toString();
						intent.setClass(ReportIntro.this,
								PdfViewerActivity.class);
						startActivity(intent);
						ViewerPreferences preferences = new ViewerPreferences(
								ReportIntro.this);
						preferences.putYourReads(uriString);
					}
				}
			});

		}

	}

}
