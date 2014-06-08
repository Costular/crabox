package com.costular.crabox;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.costular.crabox.screens.GameScreen;
import com.costular.crabox.screens.MenuScreen;

public class MainClass extends Game {

	private AddInterface ads;
	
	public MainClass(AddInterface ads) {
		this.ads = ads;
	}
	
	@Override
	public void create () {
		Cbx.initialize(this); // Initialize cbx class.
		
		setScreen(new MenuScreen());
	}

}
