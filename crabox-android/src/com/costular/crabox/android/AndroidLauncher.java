package com.costular.crabox.android;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.costular.crabox.MainClass;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AndroidLauncher extends AndroidApplication {
	
	public static final String SMALL_CODE = "ca-app-pub-8514943763666378/1317534045";
	
	
	//ANALYTICS
	  /**
	   * Enum used to identify the tracker that needs to be used for tracking.
	   *
	   * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
	   * storing them all in Application object helps ensure that they are created only once per
	   * application instance.
	   */
	  public enum TrackerName {
	    APP_TRACKER, // Tracker used only in this app.
	    GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
	    ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
	  }

	  HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	
	
	RelativeLayout layout;
	
	protected AdView adView; //small ad
	protected View gameView;
	
	private UiLifecycleHelper uiHelper;	
		
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		//Facebook helper
		uiHelper = new UiLifecycleHelper(this, null);
	    uiHelper.onCreate(savedInstanceState);
	    
	    
        // -------------------------------------------------------ANALYTICS------------------------------------------------------------------------
        // Get tracker.
        Tracker t = getTracker(TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("Gameview");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
	    
	    
		// ----------------------------------------------------ADS----------------------------------------------------------------------------
        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        //Create layout
        layout = new RelativeLayout(this);
     
        //Ad view
        adView = createAdView();
        
        //Game view
        View gameView = createGameView(config);

        // Load views
        layout.addView(gameView);
        layout.addView(adView);
             
        // Set content
        setContentView(layout);
        startAdvertising(adView);
  	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	        @Override
	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	        }

	        @Override
	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	        }
	    });
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	private void startAdvertising(AdView adView) {
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);
	  }
	
	private AdView createAdView() {
		adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(SMALL_CODE);
        adView.setId(12345); // this is an arbitrary id, allows for relative positioning in createGameView()
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.setLayoutParams(params);
        adView.setLayoutParams(params);
        adView.setBackgroundColor(Color.TRANSPARENT);
        return adView;
	}
	
	private View createGameView(AndroidApplicationConfiguration cfg) {
		    gameView = initializeForView(new MainClass(new Facebook(this, uiHelper), new Notification(this), new RequestAd(adView)), cfg);
		    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		    gameView.setLayoutParams(params);
		    return gameView;
	  }
	
	synchronized Tracker getTracker(TrackerName trackerId) {

		if (!mTrackers.containsKey(trackerId)) {
	      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
	      Tracker t = analytics.newTracker("UA-51930062-2");
	      mTrackers.put(trackerId, t);
	    }
	    return mTrackers.get(trackerId);
	  }
}

