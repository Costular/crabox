package com.costular.crabox.android;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.costular.crabox.AddInterface;
import com.google.android.gms.ads.AdView;

public class RequestAd implements AddInterface{
	
	protected AdView adView;

    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;

    public RequestAd(AdView view) {
    	adView = view;
    }
    
    protected Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_ADS:
                {
                    adView.setVisibility(View.VISIBLE);
                    break;
                }
                case HIDE_ADS:
                {
                    adView.setVisibility(View.GONE);
                    break;
                }
            }
        }
    };

	@Override
	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
        
}
