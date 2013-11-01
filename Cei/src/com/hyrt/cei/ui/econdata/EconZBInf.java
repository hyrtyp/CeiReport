package com.hyrt.cei.ui.econdata;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.vo.ChartMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class EconZBInf extends Activity implements OnClickListener {
	List<ChartMap> chartData;
	ImageView view, webView;
	ImageView szImg, zzImg, fxImg, zbImg, sjImg, homeImg, imageView, back;
	AsyncImageLoader asyncImageLoader;
	public static String zb1 = "国民经济景气动向";
	public static String zb2 = "国内生产总值";
	public static String zb3 = "企业景气";
	public static String zb4 = "工业生产";
	public static String zb5 = "消费者信心";
	public static String zb6 = "固定资产投资";
	public static String zb7 = "房地产景气";
	public static String zb8 = "国内市场";
	public static String zb9 = "物价";
	public static String zb10 = "财政";
	public static String zb11 = "货币供应量";
	public static String zb12 = "对外贸易 ";
	public static String zb13 = "利率";
	public static String zb14 = "外商直接投资";
	public static String zb15 = "股票市场";
	public static String zb16 = "汇率与外汇储备";
	public static String zb17 = "国内生产总值";
	public static String zb18 = "对外贸易";
	public static String zb19 = "国内市场";
	public static String zb20 = "农村人均现金收入";
	public static String zb21 = "固定资产投资";
	public static String zb22 = "城镇人均可支配收入";
	public static String zb23 = "房地产开发投资";
	public static String zb24 = "城乡收入对比";
	public static String zb25 = "国内生产总值";
	public static String zb26 = "工业生产";
	public static String zb27 = "国内生产总值";
	public static String zb28 = "国内市场";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_zhibiao_info);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		initView();
		Intent intent = getIntent();
		String returnID = intent.getStringExtra("zbNumber");
		view = (ImageView) findViewById(R.id.wv_zb_map);
		webView = (ImageView) findViewById(R.id.wv_zb_map1);
		view.setBackgroundResource(R.drawable.econ_chart);
		initData();
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.econ_title);
		back = (ImageView) layout.findViewById(R.id.imageView2);
		back.setOnClickListener(this);
		if (returnID == null || returnID.equals("")) {
			loadView(0);
		} else {
			switch (Integer.parseInt(returnID)) {
			case 1:
				loadView(1);
				break;
			case 2:
				loadView(2);
				break;
			case 3:
				loadView(3);
				break;
			case 4:
				loadView(4);
				break;
			case 5:
				loadView(5);
				break;
			case 6:
				loadView(6);
				break;
			case 7:
				loadView(7);
				break;
			case 8:
				loadView(8);
				break;
			case 9:
				loadView(9);
				break;
			case 10:
				loadView(10);
				break;
			case 11:
				loadView(11);
				break;
			case 12:
				loadView(12);
				break;
			case 13:
				loadView(13);
				break;
			case 14:
				loadView(14);
				break;
			case 15:
				loadView(15);
				break;
			case 16:
				loadView(16);
				break;
			case 17:
				loadView(17);
				break;
			case 18:
				loadView(18);
				break;
			case 19:
				loadView(19);
				break;
			case 20:
				loadView(20);
				break;
			case 21:
				loadView(21);
				break;
			case 22:
				loadView(22);
				break;
			case 23:
				loadView(23);
				break;
			case 24:
				loadView(24);
				break;
			case 25:
				loadView(25);
				break;
			case 26:
				loadView(26);
				break;
			case 27:
				loadView(27);
				break;
			case 28:
				loadView(28);
				break;

			default:
				break;
			}
		}

	}

	private void loadView(int number) {
		/*
		 * HashMap<String, String> uris=new HashMap<String, String>();
		 * uris.put(zb1,
		 * "http://chart.apis.google.com/chart?cht=lxy&chs=800x300&chd=t:25,50,75,100|30,60,60,25&chxt=x,y&chxl=0:||2011.I|2011.II|2011.III|2011.IV|1:|1.8|2.0|2.2|2.4|2.6&chg=0,25,0,0&chm=d,FF0000,0,0.0,20.0|d,FF0000,0,1.0,20.0|d,FF0000,0,2.0,20.0|d,FF0000,0,3.0,20.0"
		 * ); uris.put(zb2,
		 * "http://chart.apis.google.com/chart?cht=lxy&chs=800x300&chd=t:5,15,25,35,45,55,65,75,85,95,100|40,60,50,45,80,40,39,60,40,36,81&chxt=x,y&chxl=0:|2011.2|2011.4|2011.6|2011.8|2011.10|2011.12|1:|0.6|0.8|1.0|1.2|1.4&chg=0,25,0,0&chm=d,FF0000,0,0.0,20.0|d,FF0000,0,1.0,20.0|d,FF0000,0,2.0,20.0|d,FF0000,0,3.0,20.0|d,FF0000,0,4.0,20.0|d,FF0000,0,5.0,20.0|d,FF0000,0,6.0,20.0|d,FF0000,0,7.0,20.0|d,FF0000,0,8.0,20.0|d,FF0000,0,9.0,20.0|d,FF0000,0,10.0,20.0"
		 * ); uris.put(zb3,
		 * "http://chart.apis.google.com/chart?cht=lxy&chs=800x300&chd=t:5,15,25,35,45,55,65,75,85,95,100|40,60,50,45,80,40,39,60,40,36,81&chxt=x,y&chxl=0:|2011.2|2011.4|2011.6|2011.8|2011.10|2011.12|1:|1.2|1.3|1.4|1.5|1.6&chg=0,25,0,0&chm=d,FF0000,0,0.0,20.0|d,FF0000,0,1.0,20.0|d,FF0000,0,2.0,20.0|d,FF0000,0,3.0,20.0|d,FF0000,0,4.0,20.0|d,FF0000,0,5.0,20.0|d,FF0000,0,6.0,20.0|d,FF0000,0,7.0,20.0|d,FF0000,0,8.0,20.0|d,FF0000,0,9.0,20.0|d,FF0000,0,10.0,20.0"
		 * ); uris.put(zb4,
		 * "http://chart.apis.google.com/chart?cht=lxy&chs=800x300&chd=t:5,15,25,35,45,55,65,75,85,95,100|40,60,50,45,80,40,39,60,40,36,81&chxt=x,y&chxl=0:|2011.2|2011.4|2011.6|2011.8|2011.10|2011.12|1:|-0.8|-0.3|0.2|0.7|1.2|1.7|2.2|2.7&chg=0,25,0,0&chm=d,FF0000,0,0.0,20.0|d,FF0000,0,1.0,20.0|d,FF0000,0,2.0,20.0|d,FF0000,0,3.0,20.0|d,FF0000,0,4.0,20.0|d,FF0000,0,5.0,20.0|d,FF0000,0,6.0,20.0|d,FF0000,0,7.0,20.0|d,FF0000,0,8.0,20.0|d,FF0000,0,9.0,20.0|d,FF0000,0,10.0,20.0"
		 * );
		 */
		if (chartData.get(number - 1).getData().length > 1) {
			try {
				URL url = new URL(getChartMap(chartData.get(number - 1))[0]);// "http://chart.apis.google.com/chart?cht=lc&chs=870x300&chd=t:10,30,60,50,70,40,30,50,100&chm=d,FF0000,0,0.0,20.0|chm=d,FF0000,0,1.0,20.0|chm=d,FF0000,0,2.0,20.0|chm=d,FF0000,0,3.0,20.0|chm=d,FF0000,0,4.0,20.0|chm=d,FF0000,0,5.0,20.0|chm=d,FF0000,0,6.0,20.0|chm=c,FF0000,0,7.0,20.0&chxt=x,y,r&chxl=0:||1:||2:|"
				InputStream in = url.openStream();
				Bitmap bp1 = BitmapFactory.decodeStream(in);
				URL url1 = new URL(getChartMap(chartData.get(number - 1))[1]);// "http://chart.apis.google.com/chart?cht=bvg&chs=900x300&chd=t:10,30,60,50,70,40,30,50,100&chco=cc0000&chxt=x,y,r&chxl=0:|091-lv|101|101-II|101-III|101-IV|III|III-II|III-III|III-IV|1:|0.0|5000.0|10000.0|15000.0|20000.0|25000.0|2:|0.0|2.0|4.0|6.0|8.0|10.0|12.0&chg=11.1,0,0,0&chbh=90"
				InputStream in1 = url1.openStream();
				Bitmap bp2 = BitmapFactory.decodeStream(in1);

				view.setImageBitmap(bp1);
				webView.setImageBitmap(bp2);
				webView.setAlpha(100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Bitmap bp2 = null;
			;
			try {
				URL url1 = new URL(getChartMap(chartData.get(number - 1))[0]);
				InputStream in1 = url1.openStream();
				bp2 = BitmapFactory.decodeStream(in1);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			webView.setImageBitmap(bp2);
		}

	}

	/**
	 * 
	 * 图片透明度处理
	 * 
	 * 
	 * @param sourceImg
	 * 
	 *            原始图片
	 * 
	 * @param number
	 * 
	 *            透明度
	 * 
	 * @return
	 */

	public static Bitmap setAlpha(Bitmap sourceImg, int number) {

		int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];

		sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,
				sourceImg.getWidth(), sourceImg.getHeight());// 获得图片的ARGB值

		number = number * 255 / 100;

		for (int i = 0; i < argb.length; i++) {

			// argb = (number << 24)|(argb&0x00FFFFFF);// 修改最高2位的值

		}

		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(),
				sourceImg.getHeight(), Config.ARGB_8888);

		return sourceImg;

	}

	/**
	 * 服务端请求数据
	 */
	public void initData() {
		// 返回结果解析后为List<ChartMap>
		chartData = new ArrayList<ChartMap>();
		for (int i = 0; i < 8; i++) {
			ChartMap chartMap = new ChartMap();
			chartMap.setKey("zb" + i);
			chartMap.setbType("zx");
			chartMap.setsType("lxy");
			chartMap.setData(new String[] { "" + i + 25 + "," + 50 + i
					+ ",75,100|30,60,60,25" });
			chartMap.setZhuoBiao("x,y&chxl=0:||2011.I|2011.II|2011.III|2011.IV|1:|1.8|2.0|2.2|2.4|2.6");
			chartMap.setWangGe("0,25,0,0");
			chartMap.setJieDianType("d,FF0000,0,0.0,20.0|d,FF0000,0,1.0,20.0|d,FF0000,0,2.0,20.0|d,FF0000,0,3.0,20.0");
			chartData.add(chartMap);
		}
		for (int i = 8; i < 18; i++) {
			ChartMap chartMap = new ChartMap();
			chartMap.setKey("zb" + i);
			chartMap.setbType("zz");
			chartMap.setsType("bvs");
			chartMap.setData(new String[] { "" + 25 + i + "," + 50 + i
					+ ",75,100,30,60,60,25" });
			chartMap.setZhuoBiao("x,y&chxl=0:||2011.I|2011.II|2011.III|2011.IV|1:|1.8|2.0|2.2|2.4|2.6");
			chartMap.setWangGe("0,25,0,0");
			chartMap.setColor("ff0000");
			chartMap.setBackGround("c,s,76A4FB");
			chartMap.setJieDianType("d,FF0000,0,0.0,20.0|d,FF0000,0,1.0,20.0|d,FF0000,0,2.0,20.0|d,FF0000,0,3.0,20.0");
			chartData.add(chartMap);
		}
		for (int i = 18; i < 25; i++) {
			ChartMap chartMap = new ChartMap();
			chartMap.setKey("zb" + i);
			chartMap.setbType("bz");
			chartMap.setsType("p3");
			chartMap.setData(new String[] { "25,50,75,100,30,60,60,25" });
			chartMap.setZhuoBiao("Jan|Feb|Mar|Apr|May|June");
			chartMap.setWangGe("0,25,0,0");
			chartMap.setColor("ff0000");
			chartMap.setBackGround("c,s,76A4FB");
			chartMap.setJieDianType("d,FF0000,0,0.0,20.0|d,FF0000,0,1.0,20.0|d,FF0000,0,2.0,20.0|d,FF0000,0,3.0,20.0");
			chartData.add(chartMap);
		}
		for (int i = 25; i < 28; i++) {
			ChartMap chartMap = new ChartMap();
			chartMap.setKey("zb" + i);
			chartMap.setbType("hh");
			chartMap.setData(new String[] { "", "" });
			chartData.add(chartMap);
		}
	}

	public String[] getChartMap(ChartMap map) {
		String[] chartUrl = null;
		if (map.getbType().equals("zx")) {
			String str = "http://chart.apis.google.com/chart?" + "cht="
					+ map.getsType() + "&" + "chs=800x300&" + "chd=t:"
					+ map.getData()[0] + "&" + // 25,50,75,100|30,60,60,25
					"chxt=" + map.getZhuoBiao() + "&" + // x,y&chxl=0:||2011.I|2011.II|2011.III|2011.IV|1:|1.8|2.0|2.2|2.4|2.6&
					"chg=" + map.getWangGe() + "&" + // 0,25,0,0
					"chm=" + map.getJieDianType() + "";// d,FF0000,0,0.0,20.0|d,FF0000,0,1.0,20.0|d,FF0000,0,2.0,20.0|d,FF0000,0,3.0,20.0
			chartUrl = new String[] { str };
		} else if (map.getbType().equals("zz")) {
			String str = "http://chart.apis.google.com/chart?" + "chs=800x300&"
					+ "chd=t:" + map.getData()[0] + "&" + "cht="
					+ map.getsType() + "&" + "chco=" + map.getColor() + "&"
					+ // ff0000
					"chf=" + map.getBackGround() + "&"
					+ // c,s,76A4FB|bg,s,FFF2CC
					"chg=" + map.getWangGe() + "&" + "chxt="
					+ map.getZhuoBiao() + "";
			chartUrl = new String[] { str };
		} else if (map.getbType().equals("bz")) {
			String str = "http://chart.apis.google.com/chart?" + "chs=800x300&"
					+ "chd=t:" + map.getData()[0] + "&" + "cht="
					+ map.getsType() + "&" + "chco=" + map.getColor() + "&"
					+ "chl=" + map.getZhuoBiao() + "";
			chartUrl = new String[] { str };
		} else if (map.getbType().equals("hh")) {
			String str = "http://chart.apis.google.com/chart?"
					+ "cht=lxy&"
					+ "chs=800x300&"
					+ "chd=t:10,30,60,50,20,20,20,20,20|70,40,30,50,20,20,20,20,20&"
					+ "chm=d,FF0000,0,0.0,20.0|chm=d,FF0000,0,1.0,20.0|chm=d,FF0000,0,2.0,20.0|chm=d,FF0000,0,3.0,20.0|chm=d,FF0000,0,4.0,20.0|chm=d,FF0000,0,5.0,20.0|chm=d,FF0000,0,6.0,20.0|chm=c,FF0000,0,7.0,20.0&"
					+ "chxt=x,y,r&chxl=0:||1:||2:|";
			String str1 = "http://chart.apis.google.com/chart?"
					+ "cht=bvg&chs=800x300&"
					+ "chd=t:10,30,60,50,70,40,30,50,100&"
					+ "chco=cc0000&"
					+ "chxt=x,y,r&chxl=0:|091-lv|101|101-II|101-III|101-IV|III|III-II|III-III|III-IV|1:|0.0|5000.0|10000.0|15000.0|20000.0|25000.0|2:|0.0|2.0|4.0|6.0|8.0|10.0|12.0&"
					+ "chg=11.1,0,0,0&" + "chbh=75";
			chartUrl = new String[] { str, str1 };
		}
		return chartUrl;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Class<?> cla = null;
		if (v == back) {
			this.finish();
			return;
		} else if (v == szImg) {
			cla = EconDateNumberActivity.class;
		} else if (v == fxImg) {
			cla = EconFXDataActivity.class;
		} else if (v == zzImg) {
			cla = EconZZDataActivity.class;
			Intent intent = new Intent(this, cla);
			startActivity(intent);
			return;
		} else if (v == zbImg) {
			cla = EconZBQueryActivity.class;
			Intent intent = new Intent(this, cla);
			startActivity(intent);
			return;
		} else if (v == homeImg) {
			cla = HomePageDZB.class;
		}
		Intent intent = new Intent(this, cla);
		startActivity(intent);
		this.finish();
	}

	private void initView() {
		szImg = (ImageView) findViewById(R.id.iv_econ_zbinfo_sz);
		szImg.setOnClickListener(this);
		zzImg = (ImageView) findViewById(R.id.iv_econ_zbinfo_zz);
		zzImg.setOnClickListener(this);

		fxImg = (ImageView) findViewById(R.id.iv_econ_zbinfo_fx);
		fxImg.setOnClickListener(this);
		zbImg = (ImageView) findViewById(R.id.iv_econ_zbinfo_zb);
		zbImg.setOnClickListener(this);

		homeImg = (ImageView) findViewById(R.id.iv_econ_zbinfo_home);
		homeImg.setOnClickListener(this);
		RelativeLayout title = (RelativeLayout) findViewById(R.id.econ_title);
		back = (ImageView) title.findViewById(R.id.imageView2);
		back.setOnClickListener(this);

	}
}
