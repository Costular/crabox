package com.costular.crabox.screens;

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

public class HUD extends Stage implements Controller{

	private Label score;
	private Table table;
	
	Label scoreGameOver;
	Label highScore;
	Label ready;
	
	public HUD(Viewport view, SpriteBatch batch, final GameScreen screen) {
		super(view, batch);
				
		LabelStyle lbl = new LabelStyle();
		lbl.font = Cbx.getResources().white;
		if(lbl.font.getScaleX() < 2.5) {
			lbl.font.setScale(2.5f);
		}
		lbl.fontColor = Color.WHITE;
		
		score = new Label("Score: ", lbl);
		score.setPosition(getCamera().viewportWidth / 2, getCamera().viewportHeight - getCamera().viewportHeight / 7);
		score.setVisible(false);
		
		ready = new Label("Ready? Tap to start!", lbl);
		ready.setPosition(getCamera().viewportWidth / 2 - getCamera().viewportWidth / 6.5f, getCamera().viewportHeight - getCamera().viewportHeight / 3);
		ready.setVisible(true);
								
		//Creamos la tabla
		table = new Table();
		table.setPosition(getCamera().viewportWidth / 4, getCamera().viewportHeight / 2 - getCamera().viewportHeight / 4);
		table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/background-gameover.png")), 0, 0)));
		table.setSize(490, 266);
		table.setVisible(false);
		
		scoreGameOver = new Label("Score: ", lbl);
		scoreGameOver.setColor(Color.BLACK);
		scoreGameOver.setPosition(table.getWidth() / 4, table.getHeight() / 2 + 100);
		
		highScore = new Label("High Score: ", lbl);
		highScore.setColor(Color.BLACK);
		highScore.setPosition(table.getWidth() / 4, table.getHeight() / 2);
		
		ImageButtonStyle bt = new ImageButtonStyle();
		bt.imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("img/restart.png")));
		
		ImageButton button = new ImageButton(bt);
		button.setBounds(64, 64, table.getWidth() - 100, 100);	
		button.setScale(2);
		button.addListener(new ClickListener() {             
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Borramos toda la pantalla
				screen.restart();
			};
		});
		
		table.addActor(scoreGameOver);
		table.addActor(highScore);
		table.addActor(button);
		
		addActor(score);
		addActor(table);
		addActor(ready);
		
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
		scoreGameOver.setText("Score: " + score);
		this.highScore.setText("Highscore: " + highScore);
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
	}

	@Override
	public void gameOver() {		
		//Mostramos la tabla
		table.setVisible(true);
				
		//Ocultamos el label
		score.setVisible(false);
	}
}

