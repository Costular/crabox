package com.costular.crabox.android;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.costular.crabox.util.NotificationRequest;

public class Notification implements NotificationRequest{

	Handler uiThread;
    Context appContext;
	
	public Notification(Context appContext) {
		this.appContext = appContext;
		uiThread = new Handler();
	}
	
	@Override
	public void showShortToast(final String message) {
		 uiThread.post(new Runnable() {
             public void run() {
                     Toast.makeText(appContext, message, Toast.LENGTH_SHORT)
                                     .show();
             }
     });
	}

	@Override
	public void showLongToast(final String message) {
		 uiThread.post(new Runnable() {
             public void run() {
                     Toast.makeText(appContext, message, Toast.LENGTH_LONG)
                                     .show();
             }
     });
	}

	
}
