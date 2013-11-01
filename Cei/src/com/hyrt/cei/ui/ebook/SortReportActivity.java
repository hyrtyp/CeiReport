package com.hyrt.cei.ui.ebook;

import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.GoodReportAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Report;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SortReportActivity extends Activity implements OnClickListener{
	private ImageView homePage, homeReport, goodsView, sortView, partView,
	mianfeiReport, findReport;
	private ImageButton back,bookSelf;
	private ListView sortList;
	private ColumnEntry columnEntry;
	private List<Report> reportData;
	private GoodReportAdapter adapter;
	private StringBuilder colIDs;
	private int pageSize=1;
	private LinearLayout loadProgressBar;
	private TextView moreTextView;
	private LinearLayout prolayout;
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==2){
				if(msg.arg1<20){
					moreTextView.setVisibility(View.GONE);
				}else{
					moreTextView.setVisibility(View.VISIBLE);
				}
				loadProgressBar.setVisibility(View.GONE);
				if(adapter!=null)
				adapter.notifyDataSetChanged();
			}else if(msg.what==3){
				loadProgressBar.setVisibility(View.GONE);
				moreTextView.setVisibility(View.GONE);
			}else{
			super.handleMessage(msg);
		    adapter=new GoodReportAdapter(SortReportActivity.this, reportData, sortList);
		    if(sortList!=null){
		    	addPageMore();
		    	sortList.setAdapter(adapter);
		    }
			}
			prolayout.setVisibility(View.GONE);
		}
		
	};
	private void addPageMore(){
        View view=LayoutInflater.from(this).inflate(R.layout.list_page_load, null);
        moreTextView=(TextView)view.findViewById(R.id.more_id);
        loadProgressBar=(LinearLayout)view.findViewById(R.id.load_id);
        moreTextView.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏"加载更多"
                moreTextView.setVisibility(View.GONE);
                //显示进度条
                loadProgressBar.setVisibility(View.VISIBLE);
                
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //加载模拟数据：下一页数据， 在正常情况下，上面的休眠是不需要，直接使用下面这句代码加载相关数据
                    try {
						pageSize++;
						String addBg=dataChange();
						Log.i("sys",addBg.length()+"" );
						List<Report> addReport= XmlUtil.parseReport(addBg);
						if(addReport.size()==0){
							handler.sendEmptyMessage(3);
							return;
						}
						reportData.addAll(addReport);
						Message msg=new Message();
						msg.what=2;
						msg.arg1=addReport.size();
						handler.sendMessage(msg);
						
					} catch (Exception e) {
						MyTools.showPushXml(getApplicationContext());
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    }
                }).start();
            }
        });
        sortList.addFooterView(view);
    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yjbg_paihang);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		initView();
		prolayout.setVisibility(View.VISIBLE);
		initData();
	}
	private void initData(){
		if(((CeiApplication) getApplication()).isNet()){
			new Thread(){

				@Override
				public void run() {
						
						if(!dataChange().equals("")){
							//String sortBg=Service.queryReportByName(colIDs.toString().substring(0, colIDs.toString().length()-1),"30","");
							String sortBg = dataChange();
							try {
								reportData= XmlUtil.parseReport(sortBg);
								WriteOrRead.write(sortBg, MyTools.nativeData,"sortReport.xml");
								if(handler!=null){
									handler.sendEmptyMessage(1);
								}
							} catch (Exception e) {
								MyTools.showPushXml(getApplicationContext());
								e.printStackTrace();
							}
						}
				}
				
			}.start();
		}else{
			try {
				reportData= XmlUtil.parseReport(WriteOrRead.read(MyTools.nativeData, "sortReport.xml"));
				if(handler!=null){
					handler.sendEmptyMessage(1);
				}
			} catch (Exception e) {
				MyTools.showPushXml(getApplicationContext());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	private String dataChange() {
		ColumnEntry allColBg = columnEntry.getColByName(((CeiApplication) getApplication()).nowStart);
		if (allColBg != null && allColBg.getId() != null
				&& !allColBg.getId().equals("")) {
			String allBgId = allColBg.getId();
			colIDs = new StringBuilder();
			List<ColumnEntry> allCol = columnEntry
					.getEntryChildsForParent(allBgId);
			for (ColumnEntry columnEntry : allCol) {
				colIDs.append(columnEntry.getId() + ",");
			}
			if (colIDs != null && !colIDs.equals("")) {
				String sortBg = Service.queryReportByName(colIDs.toString()
						.substring(0, colIDs.toString().length() - 1), pageSize+"", "");
				return sortBg;
			}
		}
		return "";
	}
	private void initView(){
		TextView title=(TextView)(findViewById(R.id.top).findViewById(R.id.title));
		title.setText("阅读排行");
		prolayout=(LinearLayout)findViewById(R.id.report_sort_pro);
		sortList=(ListView)findViewById(R.id.lv_sortbg);
		
		homePage = (ImageView) findViewById(R.id.iv_sort_report_page_home);
		homePage.setOnClickListener(this);
		homeReport = (ImageView) findViewById(R.id.iv_sort_report_home);
		homeReport.setOnClickListener(this);
		goodsView = (ImageView) findViewById(R.id.iv_sort_report_goods);
		goodsView.setOnClickListener(this);
		sortView = (ImageView) findViewById(R.id.iv_sort_report_sort);
		sortView.setOnClickListener(this);
		partView = (ImageView) findViewById(R.id.iv_sort_report_partition);
		partView.setOnClickListener(this);
		mianfeiReport = (ImageView) findViewById(R.id.iv_sort_report_miamfei);
		mianfeiReport.setOnClickListener(this);
		findReport = (ImageView) findViewById(R.id.iv_sort_report_find);
		findReport.setOnClickListener(this);
		bookSelf = (ImageButton) (findViewById(R.id.top).findViewById(R.id.ib_bg_bookshelf));
		bookSelf.setOnClickListener(this);
		back = (ImageButton)(findViewById(R.id.top). findViewById(R.id.ib_bg_back));
		back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		//if(ReadReportActivity.activitys!=null){
		if (v == homeReport) {
			// 阅读报告页
			Intent intent = new Intent(this, ReadReportActivity.class);
			startActivity(intent);
			//saveActivity(ReadReportActivity.class);
			
		} else if (v == goodsView) {
			Intent intent = new Intent(this, GoodsReportActivity.class);
			startActivity(intent);
			//saveActivity(GoodsReportActivity.class);
			// 精彩报告页
		} else if (v == sortView) {
			// 排序报告页
			/*Intent intent = new Intent(this, SortReportActivity.class);
			startActivity(intent);
			saveActivity(SortReportActivity.class);*/
			return;
		} else if (v == partView) {
			// 分类报告页
			Intent intent = new Intent(this, PartitionReportActivity.class);
			startActivity(intent);
			//saveActivity(PartitionReportActivity.class);
		} else if (v == mianfeiReport) {
			// 免费报告
			Intent intent = new Intent(this, MianfeiReportActivity.class);
			startActivity(intent);
			//saveActivity(MianfeiReportActivity.class);
		} else if (v == findReport) {
			// 报告查询查询
			Intent intent = new Intent(this, FindReportActivity.class);
			startActivity(intent);
			//saveActivity(FindReportActivity.class);
		} else if (v == homePage) {
			// 首页
			//ReadReportActivity.activitys.clear();
			Intent intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
		} else if (v == back) {
			// 返回键
			/*if(ReadReportActivity.activitys.size()>0){
				ReadReportActivity.activitys.remove(ReadReportActivity.activitys.get(ReadReportActivity.activitys.size()-1));
				Intent intent = new Intent(this, ReadReportActivity.activitys.get(ReadReportActivity.activitys.size()-1));
				
				startActivity(intent);
			}*/
		} else if (v == bookSelf) {
			// 右侧数据图片
			Intent intent = new Intent(this, CeiShelfBookActivity.class);
			startActivity(intent);
			//saveActivity(CeiShelfBookActivity.class);
		}
		//this.finish();
	}
	/*private void saveActivity(Class<?> clas){
		if(ReadReportActivity.activitys.contains(clas)){
			ReadReportActivity.activitys.remove(clas);
			ReadReportActivity.activitys.add(clas);
		}else{
			ReadReportActivity.activitys.add(clas);
		}
	}*/
	@Override
	protected void onRestart() {
		if(adapter!=null)
		adapter.notifyDataSetChanged();
		super.onRestart();
	}
}
