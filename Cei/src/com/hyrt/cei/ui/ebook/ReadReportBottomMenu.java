package com.hyrt.cei.ui.ebook;
import com.hyrt.cei.R;
import com.hyrt.cei.ui.main.Announcement;
import com.hyrt.cei.ui.main.Disclaimer;
import com.hyrt.cei.ui.personcenter.PersonCenter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ReadReportBottomMenu extends Fragment implements OnClickListener{

	public RelativeLayout layout_yjbg;
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
		
		layout_yjbg = (RelativeLayout) view.findViewById(R.id.iv_report_home);
		layout_yjbg.setOnClickListener(this);
		
		layout_jptj = (RelativeLayout) view.findViewById(R.id.iv_report_goods);
		layout_jptj.setOnClickListener(this);

		layout_ydph = (RelativeLayout) view.findViewById(R.id.iv_report_sort);
		layout_ydph.setOnClickListener(this);

		layout_flll = (RelativeLayout) view.findViewById(R.id.iv_report_partition);
		layout_flll.setOnClickListener(this);

		layout_mfbg = (RelativeLayout) view.findViewById(R.id.iv_report_miamfei);
		layout_mfbg.setOnClickListener(this);

		layout_ssbg = (RelativeLayout) view.findViewById(R.id.iv_report_find);
		layout_ssbg.setOnClickListener(this);

		layout_tzgg = (RelativeLayout) view.findViewById(R.id.iv_report_tzgg);
		layout_tzgg.setOnClickListener(this);

		layout_grzx = (RelativeLayout) view.findViewById(R.id.iv_report_grzx);
		layout_grzx.setOnClickListener(this);

		layout_gywm = (RelativeLayout) view.findViewById(R.id.iv_report_gywm);
		layout_gywm.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		
		switch(v.getId()){
		case R.id.iv_report_home:
			if (getActivity() instanceof ReadReportActivity) {

			} else {
				intent.setClass(getActivity(), ReadReportActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.iv_report_goods:
			if (getActivity() instanceof GoodsReportActivity) {

			} else {
				intent.setClass(getActivity(), GoodsReportActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.iv_report_sort:
			if (getActivity() instanceof SortReportActivity) {

			} else {
				intent.setClass(getActivity(), SortReportActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.iv_report_partition:
			if (getActivity() instanceof PartitionReportActivity) {

			} else {
				intent.setClass(getActivity(), PartitionReportActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.iv_report_miamfei:
			if (getActivity() instanceof MianfeiReportActivity) {

			} else {
				intent.setClass(getActivity(), MianfeiReportActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.iv_report_find:
			if (getActivity() instanceof FindReportActivity) {

			} else {
				intent.setClass(getActivity(), FindReportActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.iv_report_tzgg:
			if (getActivity() instanceof Announcement) {

			} else {
				if(checkIsLogin()){
					intent.setClass(getActivity(), Announcement.class);
					startActivity(intent);
				}else{
					Toast.makeText(getActivity(), "请登陆后查看！", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.iv_report_grzx:
			if (getActivity() instanceof PersonCenter) {

			} else {
				if(checkIsLogin()){
					intent.setClass(getActivity(), PersonCenter.class);
					startActivity(intent);
				}else{
					Toast.makeText(getActivity(), "请登陆后查看！", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.iv_report_gywm:
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
