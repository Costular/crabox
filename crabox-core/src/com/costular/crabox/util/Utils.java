package com.costular.crabox.util;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.costular.crabox.screens.GameScreen;

public class Utils {
	
	public static final float WORLD_TO_BOX = 0.01f;
	public static final float BOX_TO_WORLD = 100f;
											// Azul claro            ROJO						  Naranja
	private static final Color[] colors = { Color.valueOf("2E9AFE"), Color.valueOf("FE2E2E"), Color.valueOf("FF8000"), Color.valueOf("9FF781")};
	private static int last = 0;
	
	public static Color getRandomColor() {
		int random = MathUtils.random(0, colors.length-1);
		if(random == last) {
			return getRandomColor();
		} else {
			last = random;
			return colors[MathUtils.random(0, colors.length-1)];
		}
	}
	
	public static Body createRectangleBody(float x, float y, float width, float height, BodyType type, float density, float friction, float restitution, World world) {
		
		final BodyDef def = new BodyDef();
		def.position.set(x, y);
		def.type = type;
		
		PolygonShape shape =  new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		
		FixtureDef fixt = new FixtureDef();
		fixt.shape = shape;
		fixt.density = density; 
		fixt.friction = friction; 
		fixt.restitution = restitution; 	
		
		Body bb = world.createBody(def);
		bb.createFixture(fixt);
		
		shape.dispose();
		
		return bb;
	}
	
	public static Body createBallBody(float x, float y, float radius, BodyType type, float density, float friction, float restitution, World world) {
		final BodyDef def = new BodyDef();
		def.position.set(x, y);
		def.type = type;
		
		CircleShape shape = new CircleShape();
		shape.setRadius(radius);
		
		FixtureDef fixt = new FixtureDef();
		fixt.shape = shape;
		fixt.density = density;
		fixt.friction = friction;
		fixt.restitution = restitution;
		
		Body bb = world.createBody(def);
		bb.createFixture(fixt);
		
		shape.dispose();
		
		return bb;
	}
	
	public static float getBorderBottomOfCamera() {
		if(GameScreen.getCamera() == null) {
			return -1f;
		}
		
		return GameScreen.getCamera().position.y - GameScreen.getCamera().viewportHeight / 2;
	}

	
	public static int randomInt(int min, int max) {
	    return new Random().nextInt((max - min) + 1) + min;
	}
	
	public static float randomFloat(float min, float max) {
		return new Random().nextFloat() * (max - min) + min;
	}
	
	public static float toMeter(float pixels) {
        return (float)pixels * WORLD_TO_BOX;
    }

    public static Vector2 toMeter(Vector2 vecPixel) {
        return new Vector2(toMeter(vecPixel.x), toMeter(vecPixel.y));
    }
	
	
    public static float toPixel(float meter) {
        return (float) meter * BOX_TO_WORLD;
    }
	
	
	public static void saveScreen() {
		int count = 0;
		try {
			FileHandle f;
			do {
				f = new FileHandle("screenshoot"+ count++ +".png");
			}while(f.exists());
			
			Pixmap px = takeScreenShoot();
			PixmapIO.writePNG(f, px);
			px.dispose();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Pixmap takeScreenShoot() {
		return ScreenUtils.getFrameBufferPixmap(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
}
