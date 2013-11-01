/*
 * Copyright (C) 2008 Romain Guy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hyrt.cei.ui.econdata.view;

import android.widget.GridView;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.hyrt.cei.R;

public class EconDataView extends GridView {
	private Bitmap mShelfBackground;
	private int mShelfWidth;
	private int mShelfHeight;
	Bitmap shelfBackground ;


	public EconDataView(Context context) {
		super(context);
	}

	public EconDataView(Context context, AttributeSet attrs) {
		super(context, attrs);
		load(context, attrs, 0);
	}

	public EconDataView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		load(context, attrs, defStyle);
	}

	private void load(Context context, AttributeSet attrs, int defStyle) {
		Resources resources = getResources();
		shelfBackground = BitmapFactory.decodeResource(resources,
				R.drawable.econ_data_gri_bg1);
		if (shelfBackground != null) {
			mShelfWidth = shelfBackground.getWidth();
			mShelfHeight = shelfBackground.getHeight();
			mShelfBackground = shelfBackground;
		}

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
			for (int y = top; y < height; y += shelfHeight) {
				canvas.drawBitmap(background, x, y, null);
			}
		}
		super.dispatchDraw(canvas);
	}
   public void gc(){
	   shelfBackground.recycle();
	   shelfBackground=null;
	   System.gc();
   }
}
