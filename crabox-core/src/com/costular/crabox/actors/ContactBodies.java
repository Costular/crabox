package com.costular.crabox.actors;

import javax.management.StringValueExp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.costular.crabox.actors.DefaultBox.Type;
import com.costular.crabox.util.Box2DUtils;

public class ContactBodies implements ContactListener{

	public Player player;
	
	public ContactBodies(final Player player) {
		this.player = player;
	}
	
	private DefaultBox getUserData(Body body) {
		return ((DefaultBox) body.getUserData());
	}
	
	private boolean isPlayer(Body body) {
		return getUserData(body).getType().equals(Type.PLAYER);
	}
	
	private boolean isGround(Body body) {
		return getUserData(body).getType().equals(Type.GROUND);
	}
	
	private DefaultBox getGround(Body a, Body b) {
		if(isGround(b)) {
			return getUserData(b);
		}
		return getUserData(a);
	}
	
	@Override
	public void beginContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();
		
		DefaultBox ground = getGround(a, b);
		
		// No tengo ni idea de por qué, pero no va del todo bien.		
		if(ground.getY() + ground.getHeight() < player.getY()){
			player.setToRunning();
			
			// Cambiamos el color de la plataforma
			getGround(a, b).setColor(Color.WHITE);
			
			// Aumentamos el score
			if(player.getY() + player.getHeight() > getGround(a, b).getY() && player.getX() > 75) {
				player.incrementScore();
			}
			
		}
		
		a = null;
		b = null;
	}

	@Override
	public void endContact(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();
		
		if(isGround(b)) {
			getUserData(b).setColor(Color.GRAY);
		} else {
			getUserData(a).setColor(Color.GRAY);
		}
		
		if(player.isRunning()) {
			player.setToJumping();
		}
		
		a = null;
		b = null;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	
}
