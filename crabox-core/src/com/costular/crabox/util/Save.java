package com.costular.crabox.util;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.costular.crabox.actors.Score;


public class Save {
	
	private FileHandle file;
	private Preferences preferences;
	private Score score;
	private Json json;
	
	private boolean canPlay;
	
	public Save() {
		preferences = Gdx.app.getPreferences("crabox");
		/*file = Gdx.files.internal("bin/crabox.json");
		json = new Json();
		
		if(file.exists()) {
			score = new Score(getFileHighScore());
		} else {
			try {
				file.file().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			score = new Score(0);
		}
		*/
		score = new Score(getInt("score"));
		canPlay = getSound();
	}
	
	public boolean canPlaySound() {
		return canPlay;
	}
	
	public void setPlaySound(boolean can) {
		canPlay = can;
	}
	
	public boolean getSound() {
		return getBoolean("play_sound");
	}
	
	public String getString(String key) {
		return preferences.getString(key);
	}
	
	public boolean getBoolean(String key) {
		return preferences.getBoolean(key);
	}
	
	public float getFloat(String key) {
		return preferences.getFloat(key);
	}
	
	public int getInt(String key) {
		return preferences.getInteger(key);
	}
	
	public void setString(String key, String value) {
		preferences.putString(key, value);
	}
	
	public void setBoolean(String key, boolean value) {
		preferences.putBoolean(key, value);
	}
	
	public void setFloat(String key, float value) {
		preferences.putFloat(key, value);
	}
	
	public void setInt(String key, int value) {
		preferences.putInteger(key, value);
	}
	
	public void saveScore(int score) {
		//json.toJson(score, file);
		if(score > this.score.highScore) {
			setInt("score", score);
			this.score.highScore = score;
		}
	}
	
	public int getHighScore() {
		if(score != null) {
		return score.highScore;
		} else {
			return 0;
		}
	}
	
	/*
	 * Obtenemos el HighScore guardado en el fichero, intentaremos leerlo y, si no lo conseguimos, cambiaremos el highScore a 0.
	 */
	public int getFileHighScore() {
		/*int score;
		try {
			score = Integer.parseInt(Base64Coder.decodeString(file.readString()));
		} catch(NumberFormatException n) {
			score = 0;
		}
		
		return score;
		*/
		return getInt("score");
	}
	
	/*
	 * Tomará un segundo o así guardarlo y por lo tanto hay que hacerlo al final de cada partida, más o menos.
	 */
	public void save(int score) {
		/*if(score > this.score.highScore || score > getFileHighScore()) {
			this.score.highScore = score; // NEW HIGH_RECORD
			file.writeString((Base64Coder.encodeString(json.prettyPrint(score))), false);
		}
		*/
		if(score > this.score.highScore) {
			this.score.highScore = score;
			setInt("score", score);
		}
		setBoolean("play_sound", canPlay);
		
		// :D
		preferences.flush();
	}

	
}
