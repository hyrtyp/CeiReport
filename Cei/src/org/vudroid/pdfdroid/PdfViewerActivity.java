package org.vudroid.pdfdroid;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.poqop.document.BaseViewerActivity;
import com.poqop.document.DecodeService;
import com.poqop.document.DecodeServiceBase;
import org.vudroid.pdfdroid.codec.PdfContext;

public class PdfViewerActivity extends BaseViewerActivity{
	
    @Override
    protected DecodeService createDecodeService() { 
    	
    	Log.v("menu", "4�ˡ�������");
        return new DecodeServiceBase(new PdfContext());
    }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
