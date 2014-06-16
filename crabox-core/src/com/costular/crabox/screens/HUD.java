package com.costular.crabox.screens;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.costular.crabox.Cbx;
import com.costular.crabox.Controller;
import com.costular.crabox.GameState;
import com.costular.crabox.actors.Player;

public class HUD extends Stage implements Controller{

	GameScreen screen;
	
	private Label score;
	private Table table;
	
	Image gameOver;
	Label scoreGameOver;
	Label scoreGameOverNum;
	Label highScoreNum;
	Label highScore;
	Label ready;
	
	Button facebook;
	Button twitter;
	
	public Label fps;
	
	//boolean
	public boolean stop = false;
	
	public HUD(Viewport view, SpriteBatch batch, final GameScreen screen) {
		super(view, batch);
			
		this.screen = screen;
		
		LabelStyle lbl = new LabelStyle();
		lbl.font = Cbx.getResources().white;
		lbl.fontColor = Color.WHITE;
		
		LabelStyle gameOverLabel = new LabelStyle();
		gameOverLabel.font = Cbx.getResources().black;
		
		LabelStyle gameOverLabelNum = new LabelStyle();
		gameOverLabelNum.font = Cbx.getResources().blue;
				
		score = new Label("Score: ", lbl);
		score.setPosition(getCamera().viewportWidth / 2, getCamera().viewportHeight - getCamera().viewportHeight / 7);
		score.setVisible(false);
		
		ready = new Label("Ready? Tap to start!", lbl);
		ready.setPosition(getCamera().viewportWidth / 2 - getCamera().viewportWidth / 6.5f, getCamera().viewportHeight - getCamera().viewportHeight / 3);
		ready.setVisible(true);
								
		//Creamos la tabla
		//Texture texture = new Texture(Cbx.getResources().);
		//texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		table = new Table();
		table.setPosition(getCamera().viewportWidth / 4.5f, getCamera().viewportHeight / 2 - getCamera().viewportHeight / 5);
		table.setBackground(new NinePatchDrawable(Cbx.getResources().getNine("window")));
		table.setSize(805, 510);
		table.setVisible(false);
		
		scoreGameOver = new Label("Score", gameOverLabel);
		scoreGameOver.setPosition(20, table.getHeight() - 100);
		
		highScore = new Label("Best", gameOverLabel);
		highScore.setPosition(20, table.getHeight() / 2.1f);
		
		scoreGameOverNum = new Label("0", gameOverLabelNum);
		scoreGameOverNum.setPosition(30, table.getHeight() - 165);
		
		highScoreNum = new Label("0", gameOverLabelNum);
		highScoreNum.setPosition(30, 175);
		
		gameOver = new Image(new Texture(Gdx.files.internal("img/game-over.png")));
		gameOver.setPosition(getCamera().viewportWidth / 2 - (378/2), getCamera().viewportHeight / 1.2f);
		gameOver.setVisible(false);
		
		Button button = new Button(Cbx.getResources().getDrawable("return"));
		button.setBounds(550, 180, 192, 260);	
		button.setScale(2);
		button.addListener(new ClickListener() {             
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Borramos toda la pantalla
				screen.restart();
			};
		});
		
		twitter = new Button(Cbx.getResources().getDrawable("share-twitter"));
		twitter.setBounds(590, 50, 192, 69);
		twitter.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Cbx.showShortToast("¡Esta opción estará próximamente! | Coming soon...");
			}
		});
		
		facebook = new Button(Cbx.getResources().getDrawable("share-facebook"));
		facebook.setBounds(380, 50, 192, 69);
		facebook.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Cbx.showShortToast("¡Esta opción estará próximamente! | Coming soon...");
				//Cbx.postOnFacebook("Esto es una prueba xd.", "", "http://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/512px-Android_robot.svg.png");
			}
		});
		
		
		table.addActor(scoreGameOver);
		table.addActor(highScore);
		table.addActor(button);
		table.addActor(twitter);
		table.addActor(facebook);
		table.addActor(scoreGameOverNum);
		table.addActor(highScoreNum);
		
		if(GameScreen.debug) {
			fps = new Label("FPS: ", lbl);
			fps.setPosition(5, getCamera().viewportHeight - fps.getHeight());
			
			addActor(fps);
		}
		
		addActor(score);
		addActor(table);
		addActor(ready);
		addActor(gameOver);
		
		// Añadimos el controlador al administrador. Éste se encarga de cuando el juego cambie de estado, ahorramos en código.
		Cbx.getController().addController(this);
	}
	
	public void resize(int width, int height) {
		getCamera().viewportWidth = width;
		getCamera().viewportHeight = height;
		getCamera().update();
	}

	public void hideReady() {
		ready.setVisible(false);
	}
	
	public void showScore() {
		score.setVisible(true);
	}
	
	public void updateScores(String score, String highScore) {
		scoreGameOverNum.setText(String.valueOf(score));
		highScoreNum.setText(String.valueOf(highScore));
	}
	
	public void updateText(String txt) {
		score.setText(txt);
	}

	@Override
	public void start() {
		hideReady(); // Hacemos que desaparezca el mensaje de que toque para empezar
		showScore(); // Empezamos a mostrar el score.
	}

	@Override
	public void notReady() {
		table.setVisible(false);
		ready.setVisible(true);
		gameOver.setVisible(false);
	}

	@Override
	public void gameOver() {		
		//Mostramos la tabla
		table.setVisible(true);
				
		//Ocultamos el label
		score.setVisible(false);
		gameOver.setVisible(true);		
	}
	

	@Override
	public void screenChanged() {
		// nothing here.
	}
}

