package com.hyrt.cei.ui.personcenter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.PersonCenterInf;
import com.hyrt.ceiphone.R;

public class QccountInfo extends Activity {
	public static PersonCenterInf p;
	private TextView userid, integral;

	public static PersonCenterInf getPersonCenterInf() {
		return p;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qccountinfo);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		init();
	}

	private void init() {
		userid = (TextView) findViewById(R.id.personinfo_userid);
		integral = (TextView) findViewById(R.id.personinfo_integral);
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
