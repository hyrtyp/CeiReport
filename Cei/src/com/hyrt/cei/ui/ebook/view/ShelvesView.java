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

package com.hyrt.cei.ui.ebook.view;

import android.widget.GridView;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.hyrt.cei.R;

public class ShelvesView extends GridView {
	private Bitmap mShelfBackground;
	private int mShelfWidth;
	private int mShelfHeight;
	Bitmap shelfBackground1;

	public ShelvesView(Context context) {
		super(context);
	}

	public ShelvesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		load(context, attrs, 0);
	}

	/*public ShelvesView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		load(context, attrs, defStyle);
	}*/

	private void load(Context context, AttributeSet attrs, int defStyle) {
		// 设置书架的背景图片
		SharedPreferences settings = context.getSharedPreferences(
				"BookSelfColor", Activity.MODE_PRIVATE);
		int col = settings.getInt("back", 0);
		Resources resources = context.getResources();
		Bitmap shelfBackground=null;
		if (col != 0) {
			shelfBackground = BitmapFactory.decodeResource(
					resources, col);
		} else {
			
			shelfBackground = BitmapFactory.decodeResource(
					resources, R.drawable.shelf_panel);
		}
		

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

}
