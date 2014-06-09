package com.costular.crabox.actors;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.costular.crabox.Cbx;
import com.costular.crabox.util.Utils;

public class Player extends DefaultBox implements Disposable{

	public static enum State {
		STAYING, RUNNING, JUMPING, DYING;
	}
	
	// FINAL VARS
	public static int IMPULSE = 40; 
	public static final int JUMP_IMPULSE = 940;
	
	//State
	public State state;
		
	//Score
	private int score;
	private int highScore;
	
	public void incrementVelocity(int n) {
		IMPULSE += n;
	}
	
	//------------------------ GETTERS ------------------------------------
	
	// STATES
	public boolean isDying() {
		return state == State.DYING;
	}

	public boolean isStaying() {
		return state == State.STAYING;
	}

	public boolean isRunning() {
		return state == State.RUNNING;
	}

	public boolean isJumping() {
		return state == State.JUMPING;
	}

	public int getScore() {
		return score;
	}

	public int getHighScore() {
		return highScore;
	}

	// ------------------------ SETTTERS -----------------------------------
	public void setState(State state) {
		this.state = state;
	}
	
	
	public void setScore(int score) {
		this.score = score;
	}

	public void incrementScore() {
		score++;
	}

	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}

	public void setToDying() {
		state = State.DYING;
	}

	public void setToRunning() {
		state = State.RUNNING;
	}

	public void setToJumping() {
		state = State.JUMPING;
	}

	public void setToStaying() {
		state = State.STAYING;
	}

	private void setMass() {
		MassData masa = new MassData();
		masa.center.set(getWidth()/2, getHeight()/2);
		masa.mass = 25; // Pesa 5 kilográmos.
		
		getBody().setMassData(masa);
		getBody().resetMassData();
	}

	// -------------------------CONSTRUCTORS--------------------------------
	public Player(float x, float y, float width, float height, World world) {
		super(x, y, width, height, BodyType.DynamicBody, world);
		
		state = State.STAYING;
		
		//Agregar la masa al cuerpo
		setMass();
		setHighScore(Cbx.getPreferences().getHighScore());
	}
	
	public Player(float x, float y, float width, float height, float density, float friction, float restitution, Sprite sprite, World world) {
		super(x, y, width, height, density, friction, restitution, BodyType.DynamicBody, sprite, world);
		
		state = State.STAYING;
		
		body.setUserData(new String("player"));
			
		//Agregar la masa al cuerpo
		setMass();
		body.setGravityScale(15f);
		body.setFixedRotation(true);
		setHighScore(Cbx.getPreferences().getHighScore());
		setType(Type.PLAYER);
		
	}
	
	@Override
	public void update() {
		if(getY() < Utils.getBorderBottomOfCamera() && state != State.DYING) { // Posición de la caja es 0, mide 10 y le damos un poco más de margen, por tanto 15.
			setToDying();
		}
	
		velocity.x = body.getLinearVelocity().x;			
		position.set(body.getPosition());
		angle = body.getAngle() * MathUtils.radiansToDegrees;
		
		if(velocity.x < IMPULSE && isRunning() && Cbx.getController().isStarted()) {
			body.setLinearVelocity(IMPULSE, 0);
		}
	}
	
	public void beginRun() {
		getBody().setLinearVelocity(IMPULSE, 0);
	}
	
	public void jump() {
		if(isJumping() || getY() < 0) {
			return;
		}
		setToJumping();
		Cbx.getResources().soundJump();
		
		applyLinearImpulse(0, JUMP_IMPULSE);
		//applyAngularImpulse(-180);
	}
	
	@Override
	public void applyLinearImpulse() {
		/*
		 * Lo quitamos para que no salte hacia delante.
		 
		if(isJumping()) {
			velocity.x = 0;
		}
		*/
		
		getBody().applyLinearImpulse(velocity.x, velocity.y, getBody().getWorldCenter().x, getBody().getWorldCenter().y, true);		
	}
	
	public void restart() {
		IMPULSE = 40;
		score = 0;
		highScore = 0;
		state = State.STAYING;
		
		// ahora la posición
		position.set(1, 2.6f);
		body.setTransform(1, 2.6f, 0);
	}
	
	@Override
	public void dispose() {
		restart();
	}
	

}
