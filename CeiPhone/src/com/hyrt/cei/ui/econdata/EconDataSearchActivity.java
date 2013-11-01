package com.hyrt.cei.ui.econdata;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.adapter.InfAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.information.InfoSearchActivity;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.vo.funId;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 搜索页 实现系统的onCLick方法，需要绑定监听器，setOnclickListener(this),
 * 
 * @author tmy
 * 
 */
public class EconDataSearchActivity extends ContainerActivity implements
		OnClickListener {
	private ImageView img_econ_main, img_szks, img_zjzs, img_fxyc, img_zbsc,
			img_data_search;
	public static String MODEL_NAME;
	private TextView tv_left,tv_center;
	Context context = EconDataSearchActivity.this;
	private ListView list;
	private List<InfoNew> infoNews;
	private EditText searchEdittext;
	private String strId;
	private Intent intent;
	private Boolean canRead;
	private List<funId> funIds;
	private List<ColumnEntry> columnEntries;
	private ColumnEntry columnEntry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_data_search);
		MODEL_NAME = ((CeiApplication) getApplication()).nowStart;// 获取当前业务名称。
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();

	}

	private void initView() {
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		columnEntries = columnEntry.getEntryChildsForParent(columnEntry
				.getColByName(EconDataMain.MODEL_NAME).getId());
		System.out.println("initview经济数据信息" + columnEntries.get(0).getId());
		canRead = false;
		// 搜索键
		tv_left = (TextView) findViewById(R.id.econ_search_top_text1);
		tv_left.setText(MODEL_NAME);
		tv_center=(TextView)findViewById(R.id.econ_main_top_img);
		tv_center.setText("数据查询");
		img_szks = (ImageView) findViewById(R.id.econ_data_search_botton_1);
		img_zjzs = (ImageView) findViewById(R.id.econ_data_search_botton_2);
		img_fxyc = (ImageView) findViewById(R.id.econ_data_search_botton_3);
		img_zbsc = (ImageView) findViewById(R.id.econ_data_search_botton_4);
		// img_data_search=(ImageView)
		// findViewById(R.id.econ_data_search_botton_);
		tv_left.setOnClickListener(this);
		img_szks.setOnClickListener(this);
		img_zjzs.setOnClickListener(this);
		img_fxyc.setOnClickListener(this);
		img_zbsc.setOnClickListener(this);
		// img_data_search.setOnClickListener(this);

	}

	public void refreshListData() {
		EconDataSearchActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				infoNews = new ArrayList<InfoNew>();
				String rs = "";
				ColumnEntry columnEntry = ((CeiApplication) getApplication()).columnEntry;
				List<ColumnEntry> list = columnEntry
						.getEntryChildsForParent(columnEntry.getColByName(
								"经济数据").getId());
				System.out.println(list.size());
				strId = "";
				for (int i = 0; i < list.size(); i++) {
					if (i + 1 == list.size()) {
						strId += list.get(i).getId();
					} else {
						strId += list.get(i).getId() + ",";
					}
				}
				String srarchStr = searchEdittext.getText().toString();
				String result = Service.queryBuyNews(columnEntry.getUserId());
				System.out.println("search" + result);
				rs = Service.queryNewsByName(strId, srarchStr);
				System.out.println("search" + rs);
				System.out.println("search" + infoNews);

				try {
					XmlUtil.getNewsList(rs, infoNews);
					System.out.println("search" + funIds);
					funIds = XmlUtil.queryBuyNews(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = newsHandler.obtainMessage();
				newsHandler.sendMessage(msg);
			}
		});
	}

	Handler newsHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (infoNews.size() == 0) {
				Toast.makeText(EconDataSearchActivity.this, "未找到符合条件的资讯！",
						Toast.LENGTH_SHORT).show();
			} else {
				System.out.println(infoNews.get(0).getTitle());
				InfAdapter adapter = new InfAdapter(
						EconDataSearchActivity.this, R.layout.inf_list_item,
						infoNews);
				list.setAdapter(adapter);
				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						InfoNew new2 = infoNews.get(arg2);
						String intentId = new2.getId();
						intent = new Intent();
						for (int i = 0; i < columnEntries.size(); i++) {
							if (columnEntries.get(i).getId()
									.equals(new2.getFunctionId())) {
								intent.putExtra("topNum", i + "");
							}
						}
						intent.putExtra("extra", intentId);
						intent.putExtra("functionId", new2.getFunctionId());
						if (new2.getIsfree().endsWith("1")) {
							EconDataSearchActivity.this.startActivity(intent);
						} else {
							for (int i = 0; i < funIds.size(); i++) {
								if (funIds.get(i).getFunid()
										.endsWith(new2.getFunctionId())) {
									canRead = true;
									break;
								} else {
									canRead = false;
								}
							}
							if (canRead) {
								EconDataSearchActivity.this
										.startActivity(intent);
							} else {
								MyTools.exitShow(EconDataSearchActivity.this,
										getWindow().getDecorView(), "未购买该栏目！");
							}
						}
					}
				});
			}

		}
	};

	@Override
	public void onClick(View v) {
		if (v == tv_left) {
			Intent intent = new Intent(context, EconDataMain.class);
			startActivity(intent);

		} else if (v == img_szks) {
			Intent intent = new Intent(context, EconDateNumberActivity.class);
			startActivity(intent);

		} else if (v == img_zjzs) {
			Intent intent = new Intent(context, EconZZDataActivity.class);
			startActivity(intent);

		} else if (v == img_fxyc) {
			Intent intent = new Intent(context, EconFXDataActivity.class);
			startActivity(intent);

		} else if (v == img_zbsc) {
			Intent intent = new Intent(context, EconZBQueryActivity.class);
			startActivity(intent);

		} else if (v == img_data_search) {
			// 搜索功能
			if (searchEdittext.getText().toString().equals("")) {
				Toast.makeText(EconDataSearchActivity.this, "搜索内容不能为空",
						Toast.LENGTH_LONG).show();
			} else {
				refreshListData();
			}

		}

	}
}
