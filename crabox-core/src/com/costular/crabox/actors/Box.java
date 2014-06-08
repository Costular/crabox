package com.costular.crabox.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.costular.crabox.actors.DefaultBox.Type;

public class Box extends DefaultBox{

		//booleans
		private boolean dynamic;
		
		public boolean isDynamic() {
			return dynamic;
		}
		
		// -------------------------CONSTRUCTORS--------------------------------
		public Box(float x, float y, float width, float height, boolean dynamic, float velocityX, float velocityY, World world) {
			super(x, y, width, height, velocityX, velocityY, BodyType.KinematicBody, world);
			setDynamic(dynamic);
			
			if(isDynamic()) {
				this.getBody().setType(BodyType.DynamicBody);
			}
			
			setType(Type.GROUND);
		}
		
		public Box(float x, float y, float width, float height, boolean dynamic, float velocityX, float velocityY, Sprite sprite, World world) {
			super(x, y, width, height, velocityX, velocityY, BodyType.KinematicBody, world);
			setDynamic(dynamic);
			
			if(isDynamic()) {
				this.getBody().setType(BodyType.DynamicBody);
			} 
			
			setType(Type.GROUND);
			setSprite(sprite);
		}
		
		public void setDynamic(boolean is) {
			dynamic = is;
		}

		
}
