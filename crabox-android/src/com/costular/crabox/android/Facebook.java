package com.costular.crabox.android;


import android.app.Activity;
import android.os.Bundle;

import com.costular.crabox.util.FacebookRequest;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.ShareDialogBuilder;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class Facebook implements FacebookRequest{

	Activity activity;
	UiLifecycleHelper uiHelper;

	public Facebook(Activity activity, UiLifecycleHelper uiHelper) {
		this.activity = activity;
		this.uiHelper = uiHelper;
	}
	
	@Override
	public void post(String description, String link, String urlImage) {
		
		if (FacebookDialog.canPresentShareDialog(activity.getApplicationContext(), 
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(activity)
			.setLink(link == "" ? "http://www.facebook.com/craboxgame" : link)
			.setDescription(description)
			.setPicture(urlImage)
			.build();
		
			uiHelper.trackPendingDialogCall(shareDialog.present());
		} else {
			
			Bundle params = new Bundle();
		    params.putString("name", "Crabox");
		    params.putString("caption", "pene");
		    params.putString("description", description);
		    params.putString("link", link == "" ? "http://www.facebook.com/craboxgame" : link);
		    params.putString("picture", urlImage);

		    WebDialog feedDialog = (
		        new WebDialog.FeedDialogBuilder(activity,
		            Session.getActiveSession(),
		            params))
		        .build();
		    feedDialog.show();
		}

	}
	
}
