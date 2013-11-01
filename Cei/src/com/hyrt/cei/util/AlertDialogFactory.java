package com.hyrt.cei.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class AlertDialogFactory {
	public static AlertDialog getConfirmDialog(Context ctx, String title,
			String msg, final DialogCallback ok) {
		AlertDialog.Builder builder = new Builder(ctx);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ok.execute();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ok.cancle();
			}
		});
		return builder.create();
	}
}
