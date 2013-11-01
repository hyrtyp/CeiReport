package com.hyrt.cei.ui.personcenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.PersonCenterInf;
import com.hyrt.cei.webservice.service.Service;

public class QccountInfo extends Activity {
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	public static PersonCenterInf p;
	private TextView userid, integral;
	private ColumnEntry columnEntry;
	private String userId;

	public static PersonCenterInf getPersonCenterInf() {
		return p;
	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qccountinfo);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		userId = columnEntry.getUserId();
		init();
	}

	private void init() {
		userid = (TextView) findViewById(R.id.personinfo_userid);
		integral = (TextView) findViewById(R.id.personinfo_integral);
		refreshListData();
	}

	/**
	 * 获取数据
	 */
	private void refreshListData() {
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String rs = "";
				rs = Service.queryUserInfo(userId);
				WriteOrRead.write(rs, MyTools.nativeData, "PersonCenter.xml");
				WriteOrRead.write(rs, MyTools.nativeData, "new01.xml");
				p = null;
				try {
					p = XmlUtil.personCenter(rs);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = newsHandler.obtainMessage();
				newsHandler.sendMessage(msg);
			}
		});
	}

	Handler newsHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(p!=null){
				if (p.getName()!= null) {
					userid.setText(p.getLgoinname());
				} else {
					userid.setText("");
				}
				if (p.getIntegral() != null) {
					integral.setText(p.getIntegral());
				} else {
					integral.setText("");
				}
			}
		}
	};
}
