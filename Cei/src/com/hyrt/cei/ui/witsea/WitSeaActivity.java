package com.hyrt.cei.ui.witsea;

import java.util.List;
import com.hyrt.cei.R;
import com.hyrt.cei.adapter.WitSeaAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.ui.common.WebViewUtil;
import com.hyrt.cei.ui.main.Announcement;
import com.hyrt.cei.ui.main.Disclaimer;
import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.cei.ui.phonestudy.HomePageActivity;
import com.hyrt.cei.ui.witsea.view.FullGridView;
import com.hyrt.cei.util.AsyncImageLoader.ImageCallback;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class WitSeaActivity extends Activity implements OnClickListener {

	//全局application
	private CeiApplication application;
	//用户选择的业务
	private List<ColumnEntry> selectListColumn;
	//智慧海内的业务
	private List<ColumnEntry> witSeaData;
	//智慧海本地缓存文件
	private static final String WITSEA_DATA_FILENAME = "witSea.xml";
	//登陆名
	private String loginName;
	//适配器
	private WitSeaAdapter adapter;
	//gridView视图
	private FullGridView fullGridView;
	Handler operationHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// 数据发送到服务端 是次业务在服务端的id
			int index = msg.what;
			String returnCode = (String) msg.obj;
			if (returnCode != null && !returnCode.equals("")) {
				try {
					returnCode = XmlUtil.updateWitSea(returnCode);
					if (!returnCode.equals("") && !returnCode.equals("0") && !returnCode.equals("-1")) {
						// 下载成功
						// 把点击的业务加载到本地保存。
						String locStr = WriteOrRead.read(MyTools.nativeData, WITSEA_DATA_FILENAME);
						locStr = locStr
								.replace(
										"<operationimage>"
												+ witSeaData.get(index).getOperationImage()
												+ "</operationimage><isorder>1</isorder>",
										"<operationimage>"
												+ witSeaData.get(index).getOperationImage()
												+ "</operationimage><isorder>0</isorder>");
						WriteOrRead.write(locStr, MyTools.nativeData, WITSEA_DATA_FILENAME);
						//将自选业务同步的全局
						witSeaData.get(index).setSelected(true);
						application.columnEntry.getSelectColumnEntryChilds().add(witSeaData.get(index));
						adapter.notifyDataSetChanged();
					} else {
						MyTools.exitShow(WitSeaActivity.this, ((Activity)WitSeaActivity.this).getWindow().getDecorView(), "应用失败,请检查网络！");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				// 删除
				ColumnEntry item = witSeaData.get(index);
				List<ColumnEntry> selectColumns = application.columnEntry.getSelectColumnEntryChilds();
				for (ColumnEntry columnEntry : selectColumns) {
					if (columnEntry.getId() != null && columnEntry.getId().equals(item.getId())) {
						item.setUserId( application.columnEntry.getUserId());
						String deleteCode = Service.deleteWitSea(item);
						// 通知服务端删除
						if (deleteCode.equals("<?xml version='1.0' encoding='utf-8'?><ROOT><RETURNCODE>1</RETURNCODE></ROOT>")) {
							//更新视图
							witSeaData.get(index).setSelected(false);
							adapter.notifyDataSetChanged();
							// 修改本地xml数据或数据库文件
							String locStr = WriteOrRead.read(MyTools.nativeData, WITSEA_DATA_FILENAME);
							locStr = locStr
									.replace(
											"<operationimage>"
													+ witSeaData.get(index).getOperationImage() + "</operationimage><isorder>0</isorder>",
											"<operationimage>"
													+ witSeaData.get(index).getOperationImage() + "</operationimage><isorder>1</isorder>");
							WriteOrRead.write(locStr, MyTools.nativeData, WITSEA_DATA_FILENAME);
							selectColumns.remove(columnEntry);
						} else {
							MyTools.exitShow(WitSeaActivity.this, ((Activity)WitSeaActivity.this).getWindow().getDecorView(), "删除失败,请检查网络！");
						}
					}
					break;
				}
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.toolbox);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		SharedPreferences settings = getSharedPreferences("loginInfo",Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		application = (CeiApplication) this.getApplication();
		initView();
		dataInit();
	}

	private void initView() {
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.home_announcement).setOnClickListener(this);
		findViewById(R.id.home_ceinet).setOnClickListener(this);
		findViewById(R.id.home_personcenter).setOnClickListener(this);
		findViewById(R.id.home_disclaimer).setOnClickListener(this);
	}

	public void dataInit() {
		fullGridView = (FullGridView) findViewById(R.id.grid_view);
		selectListColumn = application.columnEntry.getSelectColumnEntryChilds();
		witSeaData = application.columnEntry.getWitSeaColumns();
		if (selectListColumn != null && selectListColumn.size() > 0) {
			for (ColumnEntry columnEntry : selectListColumn) {
				for (ColumnEntry witSea : witSeaData) {
					if (columnEntry.getId() != null && columnEntry.getId().equals(witSea.getId())) {
						witSea.setSelected(true);
					}
				}
			}
		}
		WriteOrRead.write(dataToXml(witSeaData), MyTools.nativeData,WITSEA_DATA_FILENAME);
		loadImage();
	}

	public String dataToXml(List<ColumnEntry> data) {
		StringBuffer xml = new StringBuffer(
				"<?xml version='1.0' encoding='UTF-8'?><ROOT><brightness>");
		for (ColumnEntry witSea : data) {
			xml.append("<bright><funid>" + witSea.getId() + "</funid>"
					+ "<name>" + witSea.getName() + "</name>" + "<issuetime>"
					+ witSea.getIssueTime() + "</issuetime>"
					+ "<operationimage>" + witSea.getOperationImage()  
					+ "</operationimage>" + "<isorder>" + (witSea.isSelected()==true?"0":"1")
					+ "</isorder></bright>");
		}
		xml.append("</brightness></ROOT>");
		return xml.toString();
	}

	private void loadImage() {
		adapter = new WitSeaAdapter(WitSeaActivity.this,
				application,fullGridView, witSeaData, operationHandler);
		fullGridView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn:
			WitSeaActivity.this.finish();
			intent = new Intent(WitSeaActivity.this,HomePageDZB.class);
			startActivity(intent);
			break;
		case R.id.home_announcement:
			intent = new Intent(WitSeaActivity.this, Announcement.class);
			if (!loginName.equals(""))
				startActivity(intent);
			break;
		case R.id.home_ceinet:
			intent = new Intent(WitSeaActivity.this, WebViewUtil.class);
			intent.putExtra("path", "http://mob.cei.gov.cn/");
			startActivity(intent);
			break;
		case R.id.home_personcenter:
			intent = new Intent(WitSeaActivity.this, PersonCenter.class);
			if (!loginName.equals(""))
				startActivity(intent);
			break;
		case R.id.home_disclaimer:
			intent = new Intent(WitSeaActivity.this, Disclaimer.class);
			startActivity(intent);
			break;
		}
	}
	
	//弹出popWindow
	private PopupWindow popWindow;
	/**
	 * 弹出业务简介
	 */
	public void alertBusinessIntro(ColumnEntry columnEntry){
		final View popView = this.getLayoutInflater().inflate(
				R.layout.witsea_pop, null);
		popView.findViewById(R.id.close_icon).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popWindow.dismiss();
			}
		});
		popWindow = new PopupWindow(popView, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		popWindow.setFocusable(true);
		popWindow.showAtLocation(findViewById(R.id.full_view), Gravity.CENTER,0, 0);
		TextView introTv = (TextView) popView.findViewById(R.id.business_intro);
		TextView titleTv = (TextView) popView.findViewById(R.id.business_name);
		introTv.setText("介绍 ："+ "\n"+ columnEntry.getDescription()==null?"暂无介绍。": columnEntry.getDescription());
		titleTv.setText(columnEntry.getName());
		ImageResourse imageResource = new ImageResourse();
		imageResource.setIconUrl(columnEntry.getOperationImage());
		imageResource.setIconId(columnEntry.getId());
		imageResource.setIconTime(columnEntry.getIssueTime());
		((CeiApplication) WitSeaActivity.this.getApplication()).asyncImageLoader
				.loadDrawable(imageResource, new ImageCallback() {
					@Override
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						ImageView imageView = (ImageView) popView.findViewById(R.id.business_icon);
						if (imageView != null && imageDrawable != null) {
							imageView.setImageDrawable(imageDrawable);
						}
					}
				});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			Intent intent = new Intent(this,HomePageDZB.class);
			startActivity(intent);
		}
		return true;
	}

}
