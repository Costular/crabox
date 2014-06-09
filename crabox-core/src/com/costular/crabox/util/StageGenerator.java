package com.costular.crabox.util;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.costular.crabox.actors.Box;
import com.costular.crabox.actors.Player;

public class StageGenerator implements Disposable{
	
	final List<Box> boxes;
	final World world;
	final Player player;
	final Camera camera;
		
	// CONSTANTS :)
	public float minDistance;  // Players' Width * 2.5
	public float maxDistance; // Players' Width * 5
	public float minWidth; // Player's Width * 2.5 - Lo que mide de ancho el jugador
	public float maxWidth; // Player's Width * 7 - Lo que mide de ancho el jugador
	public float maxY;
	public final float height; // Ni puta
	
	public float distance;
	public float width;

	private float lastX;
		
	public StageGenerator(final List<Box> boxes, final Camera camera, final World world, Player player) {
		this.boxes = boxes;
		this.world = world;
		this.camera = camera;
		this.player = player;
		
		//CONSTANTS
		minDistance = player.getWidth() * 6.25f;
		maxDistance = player.getWidth() * 6.40f; // 
		minWidth = player.getWidth() * 4.5f;
		maxWidth = player.getWidth() * 6f;
		maxY = 2; 
		height = 2.5f;
		
		firstGenerate();
	}
	
	public void incrementAll() {
		minDistance += 1.3f;
		maxDistance += 1.6f;
		minWidth += 0.2f;
		maxWidth += 0.45f;
		maxY += 0.2f;
	}
	
	public void incrementMinDistance(float ff) {
		minDistance += ff;
	}
	
	public void incrementMaxDistance(float ff) {
		maxDistance += ff;
	}

	private Box getLastBox() {
		return boxes.get(boxes.size()-1);
	}
	
	public boolean isBoxOnLeftSide(Box b) {
		return b.getX() < camera.position.x - camera.viewportWidth;
	}
	
	public boolean isBoxOnRightSide(Box b) {
		return b.getX() >= camera.position.x + camera.viewportWidth / 2;
	}
	
	private boolean isNotOnLeftSide(Box b) {
		if(!isBoxOnLeftSide(b)) return true;
		
		return false;
	}
	
	public void generateBoxes() {		
		
		if(boxes.isEmpty()) {
			return;
		}
		
		if(isBoxOnRightSide(getLastBox())) {
			return;
		}
		
		generate();
	}
	
	private void generate() {
		generate(MathUtils.random(minDistance, maxDistance), MathUtils.random(minWidth, maxWidth));
	}
	
	private void generate(float distance, float width) {
		Box box = new Box((float) (lastX+distance), MathUtils.random(0, maxY), width, height, false, 0, 0, world);
		boxes.add(box);
			
		lastX = box.getX()+box.getWidth();
	}
	
	public void firstGenerate() {
		Box box = new Box(0, 0, 150, height, false, 0, 0, world);
		boxes.add(box);
			
		lastX = box.getX()+box.getWidth()/1.8f;
	}
	
	public void restart() {
		boxes.clear();
		
		// Los valores por defecto
		minDistance = player.getWidth() * 7f;
		maxDistance = player.getWidth() * 7.5f; // 11 es quizás la ideal
		minWidth = player.getWidth() * 4.5f;
		maxWidth = player.getWidth() * 6f;
	}
	
	@Override
	public void dispose() {
		boxes.clear();
		// Player too
		player.dispose();
	}

}
