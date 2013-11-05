package com.hyrt.cei.ui.personcenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.PersonCenterInf;
import com.hyrt.ceiphone.R;

public class QccountInfo extends Fragment {
	public static PersonCenterInf p;
	private TextView userid, integral;

	public static PersonCenterInf getPersonCenterInf() {
		return p;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//		setContentView(R.layout.qccountinfo);
//		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		View view = inflater.inflate(R.layout.qccountinfo,container, false);
		init(view);
		return view;
	}

	private void init(View view) {
		userid = (TextView) view.findViewById(R.id.personinfo_userid);
		integral = (TextView) view.findViewById(R.id.personinfo_integral);
		String rss = WriteOrRead.read(
				MyTools.nativeData, "PersonCenter.xml");
		try {
			p = XmlUtil.personCenter(rss);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
}
