package com.hyrt.cei.ui.ebook.view;



import com.hyrt.cei.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
	private Bitmap mShelfBackground;
    private int mShelfWidth;
    private int mShelfHeight;
	public MyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		load(context, attrs, 0);
	}
	private void load(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ReportView, defStyle, 0);

        final Resources resources = getResources();
        //final int background = a.getResourceId(R.styleable.ReportView_reportBackground, 0);
        final Bitmap shelfBackground = BitmapFactory.decodeResource(resources, R.drawable.background);
        if (shelfBackground != null) {
            mShelfWidth = shelfBackground.getWidth();
            mShelfHeight = shelfBackground.getHeight();
            mShelfBackground = shelfBackground;
        }
        a.recycle();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        final int count = getChildCount();
        final int top = count > 0 ? getChildAt(0).getTop() : 0;
        final int shelfWidth = mShelfWidth;
        final int shelfHeight = mShelfHeight;
        final int width = getWidth();
        final int height = getHeight();
        final Bitmap background = mShelfBackground;

        for (int x = 0; x < width; x += shelfWidth) {
            for (int y = top; y < height*2; y += shelfHeight) {
                canvas.drawBitmap(background, x, y, null);
            }
        }

        super.dispatchDraw(canvas);
    }

}
