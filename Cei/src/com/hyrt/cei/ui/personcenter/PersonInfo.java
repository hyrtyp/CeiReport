package com.hyrt.cei.ui.personcenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.PersonCenterInf;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 用户信息
 * 
 * @author Administrator
 * 
 */
public class PersonInfo extends Fragment {
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	public static PersonCenterInf p;
	private TextView lgoinname, name, sex, certype, cardno, phone, email,
			unitname;
	private ColumnEntry columnEntry;
	private String userId;

	public static PersonCenterInf getPersonCenterInf() {
		return p;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.personinfo);
//		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		View view = inflater.inflate(R.layout.personinfo,container, false);
		columnEntry = ((CeiApplication) getActivity().getApplication()).columnEntry;
		userId = columnEntry.getUserId();
		init(view);
		return view;
	}

	private void init(View view) {
		lgoinname = (TextView) view.findViewById(R.id.personinfo_loginname);
		name = (TextView) view.findViewById(R.id.personinfo_name);
		sex = (TextView) view.findViewById(R.id.personinfo_sex);
		certype = (TextView) view.findViewById(R.id.personinfo_zhengjian);
		cardno = (TextView) view.findViewById(R.id.personinfo_zhengjiannum);
		phone = (TextView) view.findViewById(R.id.personinfo_phonenum);
		email = (TextView) view.findViewById(R.id.personinfo_email);
		unitname = (TextView) view.findViewById(R.id.personinfo_danwei);
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
				if (p.getLgoinname() != null) {
					lgoinname.setText(p.getLgoinname());
				} else {
					lgoinname.setText("");
				}

				if (p.getName() != null) {
					name.setText(p.getName());
				} else {
					name.setText("");
				}

				if (p.getSex() != null) {
					sex.setText(p.getSex());
				} else {
					sex.setText("");
				}

				if (p.getCertype() != null) {
					certype.setText(p.getCertype());
				} else {
					certype.setText("");
				}

				if (p.getCardno() != null) {
					cardno.setText(p.getCardno());
				} else {
					cardno.setText("");
				}

				if (p.getPhone() != null) {
					phone.setText(p.getPhone());
				} else {
					phone.setText("");
				}

				if (p.getEmail() != null) {
					email.setText(p.getEmail());
				} else {
					email.setText("");
				}

				if (p.getUnitname() != null) {
					unitname.setText(p.getUnitname());
				} else {
					unitname.setText("");
				}
			}
		}
	};
}
