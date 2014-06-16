package com.costular.crabox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.costular.crabox.Cbx;
import com.costular.crabox.actors.ToggleButton;

public class MenuScreen implements Screen{

	private Stage stage;
	
	private ImageButton play;
	private ToggleButton sound;
	private Skin skin;
	
	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		
		
		//Creamos el escenario que mide 800x480
		stage = new Stage(new FitViewport(800, 480)) {
			@Override
	        public boolean keyDown(int keyCode) {
	            if (keyCode == Keys.BACK) {
	                Gdx.app.exit();
	            }
	            return super.keyDown(keyCode);
	        }
		};
		
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		
		//Añadimos el fondo
		Image back = new Image(new TextureRegionDrawable(Cbx.getResources().splash));
		back.toBack();
		stage.addActor(back);
		
		skin = new Skin(Cbx.getResources().atlas);
		
		//play
		ImageButtonStyle playStyle = new ImageButtonStyle();
		playStyle.up = skin.getDrawable("play");
		playStyle.down = skin.getDrawable("play-down");
		
		play = new ImageButton(playStyle);
		play.setBounds(480, 480-170-40, 256, 64);
		play.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Cbx.setScreen(new GameScreen());
			}
		});
		
		// sonido
		ImageButtonStyle styleS = new ImageButtonStyle();
		styleS.up = skin.getDrawable("sonido-on");
		styleS.down = skin.getDrawable("sonido-off");
		styleS.checked = skin.getDrawable("sonido-off");
		
		sound = new ToggleButton(styleS);
		sound.setChecked(!Cbx.getAudio().canPlay());
		sound.setBounds(20, 480-15-64, 64, 64);
		
		//twitter
		ImageButton twitter = new ImageButton(skin.getDrawable("twitter"));
		twitter.setBounds(582, 480-259-60, 64, 64);
		twitter.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI("http://twitter.com/CraboxGame");
			}
		});
		
		ImageButton facebook = new ImageButton(skin.getDrawable("facebook"));
		facebook.setBounds(670, 480-259-60, 64, 64);
		facebook.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.net.openURI("https://www.facebook.com/craboxgame");
			}
		});
		
		
		stage.addActor(play);
		stage.addActor(sound);
		stage.addActor(twitter);
		stage.addActor(facebook);
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		skin.dispose();
		stage.dispose();	
	}
	
}


