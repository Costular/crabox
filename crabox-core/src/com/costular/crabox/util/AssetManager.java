package com.costular.crabox.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.costular.crabox.Cbx;

public class AssetManager implements Disposable{

	public final Sprite player;
	public final TextureRegion splash;
	
	public final TextureAtlas atlas;
	public final Skin skin;
	public final BitmapFont white;
	public final BitmapFont black;
	
	//Sound
	public final Sound jump;
	public final Music music;
	
	public AssetManager() {

		white = new BitmapFont(Gdx.files.internal("fnt/white-bold.fnt"));
		black = new BitmapFont(Gdx.files.internal("fnt/black.fnt"));
		white.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		black.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		jump = Gdx.audio.newSound(Gdx.files.internal("sound/jump.ogg"));
		music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.mp3"));
		
		atlas = new TextureAtlas(Gdx.files.internal("img/crabox.atlas"));
		skin = new Skin();
		skin.addRegions(atlas);
		//Splash and buttons
		splash = new TextureRegion(new Texture(Gdx.files.internal("img/splash.png")), 800, 480);
		player = new Sprite(atlas.findRegion("cubo"));
	}
	
	@Override
	public void dispose() {
		player.getTexture().dispose(); 
		atlas.dispose();
		jump.dispose();
	}
	
	public void startMusic() {
		if(music.isPlaying()) {
			music.stop();
		}
		
		music.setVolume(0.5f);
		music.setLooping(true);
		music.play();
	}
	
	public void downMusicToStop() {
		for(int i = 0; i < 5; i++) {
			music.setVolume(music.getVolume()-0.15f);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		music.stop();
	}
	
	public void stopMusic() {
		if(music.isPlaying()) {
			music.stop();
		}
	}
	
	public void playSound(Sound sound) {
		if(Cbx.getPreferences().canPlaySound()) {
			jump.play(0.3f);
		}
	}
	
	public void soundJump() {
		playSound(jump);
	}
	
}
