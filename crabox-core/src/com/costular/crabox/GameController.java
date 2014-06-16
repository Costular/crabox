package com.costular.crabox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

public class GameController implements Controller{
	
	private List<Controller> controllers;
	
	private GameState state;
	

	public boolean isNotReady() {
		return state == GameState.NOT_READY;
	}
	
	public boolean isStarted() {
		return state == GameState.STARTED;
	}
	
	public boolean isGameOver() {
		return state == GameState.GAME_OVER;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
		Gdx.app.debug(getClass().getSimpleName(), "State changed: " + state.toString());
	}

	public GameController() {
		controllers = new ArrayList<Controller>();
	}
	
	public void addController(Controller c) {
		controllers.add(c);
	}
	
	public void removeController(Controller c) {
		controllers.remove(c);
	}
	
	public void gameOver() {
		setState(GameState.GAME_OVER);
		
		for(Controller c : controllers) {
			c.gameOver();
		}
	}
	
	public void start() {
		setState(GameState.STARTED);
		
		for(Controller c : controllers) {
			c.start();
		}
	}
	
	public void notReady() {
		setState(GameState.NOT_READY);
		
		for(Controller c : controllers) {
			c.notReady();
		}
	}
	
	@Override
	public void screenChanged() {
		state = GameState.SCREEN_CHANGED;
		
		for(Controller c : controllers) {
			c.screenChanged();
		}
		
		notReady();
	}
}
