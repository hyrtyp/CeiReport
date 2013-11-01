package com.hyrt.readreport.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.AbsListView.LayoutParams;

public class GGridView extends GridView {
	
	public GGridView(Context context) {
		super(context);
	}

	public GGridView(Context context, AttributeSet attributeset) {
		super(context, attributeset);
	}

	public GGridView(Context context, AttributeSet attributeset, int i) {
		super(context, attributeset, i);
	}

	public void setAdapter(BaseAdapter baseAdapter) {
		super.setAdapter(baseAdapter);
		if (baseAdapter.getCount() > 0) {
			setNumColumns(baseAdapter.getCount());
			DisplayMetrics displaymetrics = new DisplayMetrics();
			((Activity) getContext()).getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);

			int itemWidth = displaymetrics.widthPixels/4;
			/*setLayoutParams(new android.widget.LinearLayout.LayoutParams(
					itemWidth * baseAdapter.getCount()>displaymetrics.widthPixels?itemWidth * baseAdapter.getCount():
						displaymetrics.widthPixels,LayoutParams.FILL_PARENT));*/
			setLayoutParams(new android.widget.LinearLayout.LayoutParams(itemWidth * baseAdapter.getCount(),LayoutParams.MATCH_PARENT));
		}
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
