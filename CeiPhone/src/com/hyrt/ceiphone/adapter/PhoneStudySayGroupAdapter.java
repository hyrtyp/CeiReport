package com.hyrt.ceiphone.adapter;

import java.util.List;

import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.Forum;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PhoneStudySayGroupAdapter extends BaseAdapter implements OnClickListener{

	private List<Forum> listData;
	private Context context;
	private LayoutInflater inflater;
	private static final int NO_NET = 1;
	private static final int SAVE_SUCCESS = 2;
	SharedPreferences settings;

	public PhoneStudySayGroupAdapter(Context context, List<Forum> listData) {
		this.context = context;
		this.listData = listData;
		inflater = LayoutInflater.from(context);
		settings = context.getSharedPreferences("loginInfo",Activity.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		final Forum item = listData.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.phone_study_saygroup_item,
					null);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.phone_study_saygroup_item_name);
			viewHolder.coursewareTime = (TextView) convertView
					.findViewById(R.id.phone_study_saygroup_item_time);
			viewHolder.saygroupContent = (TextView) convertView
					.findViewById(R.id.phone_study_saygroup_item_content);
			viewHolder.replyBtn = (ImageView) convertView
					.findViewById(R.id.phone_study_saygroup_item_reply);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final Forum newItem = new Forum();
		LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.phone_study_saygroup_childitem_parent);
		linearLayout.removeAllViews();
		for(int i=0;i<item.getBelowForums().size();i++){
			View view = inflater.inflate(R.layout.phone_study_saygroupchilds_item, null);
			((TextView) view
					.findViewById(R.id.phone_study_saygroup_childitem_tv1))
					.setText(item.getBelowForums().get(i).getName());
			((TextView) view
					.findViewById(R.id.phone_study_saygroup_childitem_tv2))
					.setText(item.getBelowForums().get(i).getContent());
			linearLayout.addView(view);
		}
		final Handler handler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				switch (msg.arg1) {
				case NO_NET:
					MyTools.exitShow(context, ((Activity)context).getWindow().getDecorView(), "网络有问题！");
					break;
				case SAVE_SUCCESS:
					newItem.setStrTime(XmlUtil.parseReturnCode(msg.obj.toString()));
					newItem.setContent(XmlUtil.parseContent(msg.obj.toString()));
					newItem.setName(settings.getString("LOGINNAME", ""));
					item.getBelowForums().add(newItem);
					popWin.dismiss();
					PhoneStudySayGroupAdapter.this.notifyDataSetChanged();
					break;
				}
			}
		};
		viewHolder.name.setText(item.getName());
		viewHolder.saygroupContent.setText(item.getContent());
		viewHolder.replyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final View view = alertSayPop();
				Button submitBtn = (Button) view.findViewById(R.id.weibosdk_btnSend);
				submitBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						EditText edt = (EditText) view.findViewById(R.id.weibosdk_etEdit);
						if(edt.getText() == null || edt.getText().toString().trim().length() == 0)
							return;
						String Serial = "1";
						for (Forum forme : listData) {
							if (item.getStrTime().equals(forme.getStrTime())) {
								Serial = forme.getSerial();
							}
						}
						int code = Integer.parseInt(Serial);
						code++;
						newItem.setSerial(code + "");
						newItem.setClassId(item.getClassId());
						newItem.setUserid(item.getUserid());
						newItem.setTime(item.getTime());
						newItem.setFunctionid(item.getFunctionid());
						newItem.setContent(edt.getText().toString());
						new Thread(new Runnable() {

							@Override
							public void run() {
								String result = Service.saveBBSInfo(newItem);
								Message message = handler.obtainMessage();
								if (XmlUtil.parseReturnCode(result).equals("-1")) {
									message.arg1 = NO_NET;
								} else {
									message.arg1 = SAVE_SUCCESS;
									message.obj =result;
								}
								handler.sendMessage(message);
							}
						}).start();
					}
				});
			}
		});
		viewHolder.coursewareTime.setText("发表于" + item.getTime() + " : ");
		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView coursewareTime;
		TextView saygroupContent;
		ImageView replyBtn;
	}
	
	private PopupWindow popWin;

	private View alertSayPop(){
		final View popView = ((Activity)context).getLayoutInflater().inflate(
				R.layout.say_view, null);
		popWin = new PopupWindow(popView, 350,300);
		popWin.setFocusable(true);
		popWin.showAtLocation(((Activity)context).findViewById(R.id.full_view), Gravity.CENTER, 0,
				0);
		new Handler().post(new Runnable() {
			
			public static final int WEIBO_MAX_LENGTH = 140;
			private TextView mTextNum;
			private Button mSend;
			private EditText mEdit;
			private String mContent = "";
			
			@Override
			public void run() {
				Button close = (Button) popView.findViewById(R.id.weibosdk_btnClose);
				close.setOnClickListener(PhoneStudySayGroupAdapter.this);
				mSend = (Button) popView.findViewById(R.id.weibosdk_btnSend);
				LinearLayout total = (LinearLayout) popView.findViewById(R.id.weibosdk_ll_text_limit_unit);
				total.setOnClickListener(PhoneStudySayGroupAdapter.this);
				mTextNum = (TextView) popView.findViewById(R.id.weibosdk_tv_text_limit);
				mEdit = (EditText) popView.findViewById(R.id.weibosdk_etEdit);
				mEdit.addTextChangedListener(new TextWatcher() {
					public void afterTextChanged(Editable s) {
					}

					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					public void onTextChanged(CharSequence s, int start, int before, int count) {
						String mText = mEdit.getText().toString();
						int len = mText.length();
						if (len <= WEIBO_MAX_LENGTH) {
							len = WEIBO_MAX_LENGTH - len;
							//mTextNum.setTextColor(R.color.weibosdk_text_num_gray);
							if (!mSend.isEnabled())
								mSend.setEnabled(true);
						} else {
							len = len - WEIBO_MAX_LENGTH;
							mTextNum.setTextColor(Color.RED);
							if (mSend.isEnabled())
								mSend.setEnabled(false);
						}
						mTextNum.setText(String.valueOf(len));
					}
				});
				mEdit.setText(mContent);
			}
		});
		return popView;
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		if (viewId == R.id.weibosdk_btnClose) {
			popWin.dismiss();
		}
		
	}

}
