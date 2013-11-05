package com.hyrt.readreport;

import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.common.Announcement;
import com.hyrt.ceiphone.common.Disclaimer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CeiReportBottomMenu extends android.support.v4.app.Fragment implements OnClickListener {

	public RelativeLayout layout_jptj;
	public RelativeLayout layout_ydph;
	public RelativeLayout layout_flll;
	public RelativeLayout layout_mfbg;
	public RelativeLayout layout_ssbg;
	public RelativeLayout layout_tzgg;
	public RelativeLayout layout_grzx;
	public RelativeLayout layout_gywm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.read_report_bottom_menu,
				container, false);
		layout_jptj = (RelativeLayout) view.findViewById(R.id.read_report_jp);
		layout_jptj.setOnClickListener(this);

		layout_ydph = (RelativeLayout) view.findViewById(R.id.read_report_ph);
		layout_ydph.setOnClickListener(this);

		layout_flll = (RelativeLayout) view.findViewById(R.id.read_report_fl);
		layout_flll.setOnClickListener(this);

		layout_mfbg = (RelativeLayout) view.findViewById(R.id.read_report_mf);
		layout_mfbg.setOnClickListener(this);

		layout_ssbg = (RelativeLayout) view.findViewById(R.id.read_report_find);
		layout_ssbg.setOnClickListener(this);

		layout_tzgg = (RelativeLayout) view.findViewById(R.id.read_report_tzgg);
		layout_tzgg.setOnClickListener(this);

		layout_grzx = (RelativeLayout) view.findViewById(R.id.read_report_grzx);
		layout_grzx.setOnClickListener(this);

		layout_gywm = (RelativeLayout) view.findViewById(R.id.read_report_gywm);
		layout_gywm.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Intent intent = new Intent();

		switch (v.getId()) {
		case R.id.read_report_jp:
			if (getActivity() instanceof ReadReportGoodActivity) {

			} else {
				intent.setClass(getActivity(), ReadReportGoodActivity.class);
				startActivity(intent);
			}

			break;
		case R.id.read_report_ph:
			if (getActivity() instanceof ReadReportPH) {

			} else {
//				Toast.makeText(getActivity(), "successfully",
//						Toast.LENGTH_SHORT).show();
				intent.setClass(getActivity(), ReadReportPH.class);
				startActivity(intent);
			}

			break;
		case R.id.read_report_fl:
			if (getActivity() instanceof ReadReportFL) {

			} else {
				intent.setClass(getActivity(), ReadReportFL.class);
				startActivity(intent);
			}
			break;
		case R.id.read_report_mf:
			if (getActivity() instanceof ReadReportMF) {

			} else {
				intent.setClass(getActivity(), ReadReportMF.class);
				startActivity(intent);
			}

			break;
		case R.id.read_report_find:
			if (getActivity() instanceof ReadReportFind) {

			} else {
				intent.setClass(getActivity(), ReadReportFind.class);
				startActivity(intent);
			}

			break;
		case R.id.read_report_tzgg:
			if (getActivity() instanceof Announcement) {

			} else {
				intent.setClass(getActivity(), Announcement.class);
				startActivity(intent);
			}

			break;
		case R.id.read_report_grzx:
			if (getActivity() instanceof PersonCenter) {

			} else {
				if (checkIsLogin()) {
					intent.setClass(getActivity(), PersonCenter.class);
					startActivity(intent);
				} else {
					Toast.makeText(getActivity(), "请登录后查看", Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
		case R.id.read_report_gywm:
			if (getActivity() instanceof Disclaimer) {

			} else {
				intent.setClass(getActivity(), Disclaimer.class);
				startActivity(intent);
			}
			break;
		}
	}

	private boolean checkIsLogin() {
		SharedPreferences settings = getActivity().getSharedPreferences(
				"loginInfo", Activity.MODE_PRIVATE);
		String loginName = settings.getString("LOGINNAME", "");
		if (loginName.equals("")) {
			return false;
		} else {
			return true;
		}

	}

}
