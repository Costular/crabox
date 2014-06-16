package com.costular.crabox;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.costular.crabox.screens.GameScreen;
import com.costular.crabox.screens.MenuScreen;
import com.costular.crabox.util.AssetManager;
import com.costular.crabox.util.AudioManager;
import com.costular.crabox.util.FacebookRequest;
import com.costular.crabox.util.NotificationRequest;
import com.costular.crabox.util.Preferences;

public class Cbx {

	private static Preferences prefs;
	private static AssetManager assets;
	private static AudioManager audio;
	private static MainClass main;
	private static GameController controller;
	
	public static Screen currentScreen;
	
	public static MenuScreen menu;
	public static GameScreen game;
	
	//Notification controller
	private static NotificationRequest notification;
	
	//APIS controllers
	private static FacebookRequest facebook;
	
	public static GameController getController() {
		return controller;
	}
	
	public static Preferences getPreferences() {
		return prefs;
	}
	
	public static AssetManager getResources() {
		return assets;
	}
	
	public static AudioManager getAudio() {
		return audio;
	}

	public static void setAudio(AudioManager audio) {
		Cbx.audio = audio;
	}

	public static void goToMenu() {
		setScreen(menu);
	}
	
	public static void goToGame() {
		setScreen(game);
	}
	
	public static void showAds() {
		MainClass.RequestHandler.showAds(true);
	}
	
	public static void hideAds() {
		MainClass.RequestHandler.showAds(false);
	}
	
	public static void setScreen(Screen scr) {
		main.setScreen(scr);
		currentScreen = scr;
		
		controller.screenChanged();
	}
	
	public static void postOnFacebook(String desc, String link, String imageUrl) {
		facebook.post(desc, link, imageUrl);
	}
	
	public static void showShortToast(String message) {
		notification.showShortToast(message);
	}
	
	public static void showLongToast(String message) {
		notification.showLongToast(message);
	}
	
	public static void initialize(MainClass clase) {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		prefs = new Preferences();
		assets = new AssetManager();
		audio = new AudioManager();
		
		main = clase;
		
		controller = new GameController();
		
		//load screens
		menu = new MenuScreen();
		game = new GameScreen();
		
		setScreen(menu); // Cargamose el menú.
	}
	
	public static void setFacebook(FacebookRequest face) {
		facebook = face;
	}
	
	public static void setNotification(NotificationRequest noti) {
		notification = noti;
	}
}
