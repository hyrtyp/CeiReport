package com.hyrt.cei.ui.ebook;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.BookAdapter;
import com.hyrt.cei.ui.ebook.view.BookLayout;
import com.poqop.document.DocumentView;

import android.app.Activity;
import android.os.Bundle;

public class OpenBookActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BookLayout bk = new BookLayout(this);
		List<String> str = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			str.add("/sdcard/pdfimage/" + i + ".jpg");
		}
		BookAdapter ba = new BookAdapter(this);
		ba.addItem(str);
		bk.setPageAdapter(ba);
		setContentView(bk);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
	}
}
