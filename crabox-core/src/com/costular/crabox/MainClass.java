package com.costular.crabox;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.costular.crabox.screens.GameScreen;
import com.costular.crabox.screens.MenuScreen;
import com.costular.crabox.util.FacebookRequest;
import com.costular.crabox.util.NotificationRequest;

public class MainClass extends Game {

	public static AddInterface RequestHandler;
	private FacebookRequest facebook;
	private NotificationRequest notification;
	
	public MainClass(FacebookRequest facebook, NotificationRequest notification, AddInterface ad) {
		MainClass.RequestHandler = ad;
		this.facebook = facebook;
		this.notification = notification;
	}
	
	@Override
	public void create () {
		Cbx.initialize(this); // Initialize cbx class.
		Cbx.setFacebook(facebook);
		Cbx.setNotification(notification);
	}

}
