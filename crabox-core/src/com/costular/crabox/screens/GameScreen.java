package com.costular.crabox.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.costular.crabox.Cbx;
import com.costular.crabox.Controller;
import com.costular.crabox.MainClass;
import com.costular.crabox.actors.Box;
import com.costular.crabox.actors.ContactBodies;
import com.costular.crabox.actors.Player;
import com.costular.crabox.util.StageGenerator;
import com.costular.crabox.util.Utils;

public class GameScreen extends InputAdapter implements Screen, Controller{
	
	private static OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shape;
	private World world;
	
	// Actors
	public Player player;
	private List<Box> boxes = new ArrayList<Box>();
	private List<Box> boxesToRemove = new ArrayList<Box>();
	
	//Generator
	private StageGenerator generator;
	
	//booleans
	public static boolean debug;
	
	//Lights
	private RayHandler handler;
	private PointLight backgroundLight;
	
	// STAGE UI
	private HUD hud;
	
	
	//SCHEDULED
	private ScheduledThreadPoolExecutor service;
	
	public static OrthographicCamera getCamera() {
		return camera;
	}
	
	@Override
	public void render(float delta) {
		// Limpiamos la pantalla.
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Actualizamos todo el juego.
		update();
		
		// Renderizamos
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		renderPlayer();
		batch.end();

		shape.begin(ShapeType.Filled);
		shape.setProjectionMatrix(camera.combined);
		renderBoxes();
		shape.end();
		
		handler.updateAndRender();
		/*
		 *  Dibuja y todas esas mierdas, y si no est� jugando porque no ha empezado o perdi�, simplemente retorna y por tanto no calcula las nuevas
		 *  posiciones de los objetos con step, esto hace que los objetos no se muevan y parezca que el juego est� en pausa.
		 */
		
		hud.act(delta);
		hud.draw();		
		
		// Si perdi� para nada queremos que genere m�s cajas ni calcule la f�sica.
		if(Cbx.getController().isGameOver()) {
			return;
		}
		
		// La f�sica del juego.
		world.step(1/60f, 6, 3); // Aqu� se calcula la f�sica. CAMBIAR POR UNO DE M�VIL xd		
		
		// Generamos cajas.
		generator.generateBoxes();
	}
	
	@Override
	public void resize(int width, int height) {
		// Camera
		camera.viewportWidth = 800 / 2.8f;
		camera.viewportHeight = 480 / 2.8f;
		camera.zoom = 0.3f;
		camera.update();
	}

	@Override
	public void show() {
		
		//----------------------------------------------DEBUG TRUE----------------------------------------------------------------------------------||
	/*	||*/  debug = false;                                                                                                                       //|| 
		//------------------------------------------------------------------------------------------------------------------------------------------||
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);	
			
		camera.position.set(1, 6, 0);
		camera.update();
		
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		world = new World(new Vector2(0, -16f), true);
		
		// El jugador
		player = new Player(1, 2.6f, 3, 3, 1.65f, 0f, 0f, Cbx.getResources().player, world); // Mide 3x3 METROS.
		
		// Creamos la luz
		createLight();
		
		// Iniciamos el listener de choque de cuerpos.
		initContactListener(); // Para las colisiones de objetos.
		
		// InputListener y log.
		setLogLevel();
		
		//Generate map
		beginGenerate();
		
		/*
		 * ---------------------------- STAGE UI ------------------------------------------------
		 */
		
		// Creamos el stage
		hud = new HUD(new FillViewport(1440, 1024), batch, this);
		
		//InputProcessor
		InputMultiplexer plexer = new InputMultiplexer();
		plexer.addProcessor(this);
		plexer.addProcessor(hud);
		Gdx.input.setInputProcessor(plexer);
		
		// A�adimos el controlador al administrador. �ste se encarga de cuando el juego cambie de estado, ahorramos en c�digo.
		Cbx.getController().addController(this);
		Cbx.getController().notReady();
	}

	@Override
	public void hide() {
		save();
		stopService();
		Gdx.app.exit();
	}

	@Override
	public void pause() {
		
		if(!Cbx.getController().isGameOver()) {
			save();
			stopService();
			Gdx.app.exit();
		}
	}
	
	public void stopService() {
		service.shutdown();
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		Cbx.getController().notReady();
		
		service.shutdown();
		
		world.dispose();
		shape.dispose();
		batch.dispose();
		player.dispose();
		generator.dispose();
		handler.dispose();
	}
	
	public void restart() {
		// Lo paramos.
		stopService();
		
		//Volvemos al principio el Generator. (Ya se encarga de vaciar el List de las cajas).
		generator.restart();
		
		//Tambi�n tenemos que borrar todas las cajas.
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		for(Body b : bodies) {
			
			if(b.getUserData() instanceof String && b.getUserData().equals("player"))  {
				continue;
			}
			
			world.destroyBody(b);
		}
		bodies.clear();
		
		// El jugador
		player.restart();
		
		// Situamos la c�mara al principio
		camera.position.set(1, 6, 0);
		camera.update();
		
		// Generamos la primera caja.
		generator.firstGenerate();
		
		// Y pasamos el estado a notReady.
		Cbx.getController().notReady();
	}

	private void update() {
		//Update before
		player.update();
		// After render
		hud.updateText(String.valueOf(player.getScore()));
		
		updateLight();
		updateLogic();
		
		camera.position.x = player.getX();
		camera.update();
		
		if(debug) {
			hud.fps.setText("FPS: " + Gdx.app.getGraphics().getFramesPerSecond());
		}
	}
	
	private void updateLight() {
		backgroundLight.setPosition(camera.position.x + camera.viewportWidth / 3, camera.position.y + camera.viewportHeight / 2);
		handler.setCombinedMatrix(camera.combined, camera.position.x, camera.position.y, camera.viewportWidth, camera.viewportHeight);
	}

	private void updateLogic() {
		if(player.isDying() && !Cbx.getController().isGameOver()) {
			Cbx.getController().gameOver();
		}
	}
	
	private void renderPlayer() {
		//shape.begin(ShapeType.Filled);
		//shape.setColor(Color.CYAN);
		//shape.end();
		
		player.render(batch);
	}
	
	private void renderBoxes() {
		if(boxes.isEmpty()) {
			return;
		}
		
		shape.setColor(Color.GRAY);
		for(Box box : boxes) {
			box.update();
			
			if(generator.isBoxOnLeftSide(box)) {
				boxesToRemove.add(box);
				continue;
			}
			
			box.render(shape);
		}
		// Aqu� eliminamos todo :)
		boxes.remove(boxesToRemove);
		boxesToRemove.clear();
	}

	private void createLight() {
		handler = new RayHandler(world);
		handler.setCulling(true);
		handler.setCombinedMatrix(camera.combined);

		backgroundLight = new PointLight(handler, 10, Color.WHITE, 500, camera.position.x + camera.viewportWidth / 3, camera.position.y + camera.viewportHeight / 2);
		backgroundLight.setColor(Utils.getRandomColor());
		backgroundLight.setXray(true);
		backgroundLight.setSoft(true);
	}
	
	private void beginGenerate() {
		generator = new StageGenerator(boxes, camera, world, player);
	}
	
	private void setLogLevel() {
		//Set log level
		if(debug) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		}
	}
	
	private void initContactListener() {
		ContactBodies contact = new ContactBodies(player);
		world.setContactListener(contact);
	}
	
	private void save() {
		Cbx.getPreferences().save(player.getScore());
	}
	
	public void gameOver() {		
		service.shutdownNow();
		
		// Guardamos la score
		Cbx.getPreferences().saveScore(player.getScore());
		save();
		//Actualizamos la score
		hud.updateScores(String.valueOf(player.getScore()), String.valueOf(Cbx.getPreferences().getHighScore()));
		
		Cbx.getAudio().stopMusic();
	}
	
	@Override
	public void start() {
		player.beginRun();
		
		// SCHEDULER CADA 5 SEGUNDOS.
		int count = 0;
		
		service = new ScheduledThreadPoolExecutor(1);
		service.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);
		service.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				
				
				if(Cbx.getController().isGameOver()) {
					return;
				}
				
				backgroundLight.setColor(Utils.getRandomColor());
				
				player.incrementVelocity(7);	
				generator.incrementAll();
								
				Gdx.app.debug(getClass().getSimpleName(), "Color changed, velocity: " + player.getBody().getLinearVelocity().x);
			}
		}, 3L, 3L, TimeUnit.SECONDS);
	
		Cbx.getAudio().startMusic();
		
		Gdx.app.debug(getClass().getSimpleName(), "------------------------STARTED-------------------------");
	}

	@Override
	public void notReady() {		
		player.restart();
		
		player.getBody().setLinearVelocity(new Vector2(0, 0));
	}

	@Override
	public void screenChanged() {
		if(Cbx.getAudio().isPlayingMusic()) {
			Cbx.getAudio().stopMusic();
		}
	}

	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Input.Keys.BACK:
			 if(Cbx.currentScreen.equals(this)) {
	        	  Cbx.goToMenu();
	          }
			break;
		}
		return true;
	}

	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		
		Gdx.app.debug(getClass().getSimpleName(), "Velocity.x: " + player.velocity.x + ", Impulse: " + Player.IMPULSE + ", body Velocity: " + player.getBody().getLinearVelocity().x);
		
		if(Cbx.getController().isStarted()) {
			player.jump();
			return true;
		}
		
		if(Cbx.getController().isNotReady()) {
			Cbx.getController().start();
			return true;
		}
		
		return false;
	}

	
}
