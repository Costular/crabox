package com.costular.crabox;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

public class GameController {
	
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
		state = GameState.GAME_OVER;
		
		for(Controller c : controllers) {
			c.gameOver();
		}
	}
	
	public void start() {
		state = GameState.STARTED;
		
		for(Controller c : controllers) {
			c.start();
		}
	}
	
	public void notReady() {
		state = GameState.NOT_READY;
		
		for(Controller c : controllers) {
			c.notReady();
		}
	}
}
