package com.costular.crabox;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.costular.crabox.util.AssetManager;
import com.costular.crabox.util.Save;

public class Cbx {

	private static Save save;
	private static AssetManager assets;
	private static MainClass main;
	private static GameController controller;
	
	public static GameController getController() {
		return controller;
	}
	
	public static Save getPreferences() {
		return save;
	}
	
	public static AssetManager getResources() {
		return assets;
	}
	
	public static void setScreen(Screen scr) {
		main.setScreen(scr);
	}
	
	public static void initialize(MainClass clase) {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		save = new Save();
		assets = new AssetManager();
		
		main = clase;
		
		controller = new GameController();
	}
	
}
