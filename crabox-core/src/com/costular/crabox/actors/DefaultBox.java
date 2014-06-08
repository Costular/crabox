package com.costular.crabox.actors;



import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.costular.crabox.util.Box2DUtils;
import com.costular.crabox.util.Utils;

public abstract class DefaultBox implements Disposable{
	
	public static enum Type {
		GROUND, PLAYER, FLYER;
	}
	private Color color = Color.GRAY;
	
	public final Vector2 position = new Vector2();
	public final Vector2 velocity = new Vector2();
	public float angle = 0;
		
	protected boolean toDestroy = false;
	
	//Type and userData
	protected Sprite sprite;
	
	//Body
	protected final Body body;
	private final Fixture fixture;

	private Type type;
	
	public float getWidth() {
		return Box2DUtils.getWidth(fixture);
	}

	public float getHeight() {
		return Box2DUtils.getHeight(fixture);
	}

	public float getX() {
		return position.x;
		//return Box2DUtils.position(fixture).x;
	}

	public float getY() {
		return position.y;
		//return Box2DUtils.position(fixture).y;
	}
	
	public void setX(float x) {
		position.x = x;
	}
	
	public void setY(float y) {
		position.y = y;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	

	public Body getBody() {
		return body;
	}
	
	public boolean toDestroy() {
		return toDestroy;
	}
	
	
	public void setTodestroy() {
		toDestroy = true;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public Sprite getSprite() {
		return sprite;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public DefaultBox(float x, float y, float width, float height, BodyType type, World world) {
		position.x = x;
		position.y = y;
				
		body = Utils.createRectangleBody(x, y, width, height, type, 0f, 0f, 0f, world);
		fixture = body.getFixtureList().get(0);
	
		//El userdata es esta propia clase, padre de todo actor.
		body.setUserData(this);
	}
	
	public DefaultBox(float x, float y, float width, float height, float velocityX, float velocityY, BodyType type, World world) {
		position.x = x;
		position.y = y;
		
		velocity.x = velocityX;
		velocity.y = velocityY;
		
		body = Utils.createRectangleBody(x, y, width, height, type, 0f, 0f, 0f, world);
		fixture = body.getFixtureList().get(0);
		
		//El userdata es esta propia clase, padre de todo actor.
		body.setUserData(this);
	}
	
	public DefaultBox(float x, float y, float width, float height, float density, float friction, float restitution, BodyType type, Sprite sprite, World world) {
		position.x = x;
		position.y = y;
		
		this.sprite = sprite;
		
		body = Utils.createRectangleBody(x, y, width, height, type, density, friction, restitution, world);
		fixture = body.getFixtureList().get(0);
		
		//El userdata es esta propia clase, padre de todo actor.
		body.setUserData(this);
	}
	
	public DefaultBox(float x, float y, float width, float height, float velocityX, float velocityY, float density, float friction, float restitution, BodyType type, World world) {
		position.x = x;
		position.y = y;
		
		velocity.x = velocityX;
		velocity.y = velocityY;
		
		body = Utils.createRectangleBody(x, y, width, height, type, density, friction, restitution, world);
		fixture = body.getFixtureList().get(0);
		
		//El userdata es esta propia clase, padre de todo actor.
		body.setUserData(this);
	}
	
	public void destroy() {
		body.getWorld().destroyBody(body);
	}
	
	public void update() {
		// Si es dinámico y por tanto se mueve, es una caja "voladora" y la destruímos.		
		position.set(body.getPosition());
	}
	
	public void render(ShapeRenderer shape) {
		shape.setColor(color);
		shape.rect(getX() - getWidth() / 2 , getY() - Box2DUtils.getHeight(fixture) / 2, Box2DUtils.getWidth(fixture), Box2DUtils.getHeight(fixture), getWidth(), getHeight(), angle);
	}
	
	public void render(SpriteBatch batch) {
		if(sprite != null) {
			sprite.setBounds(getX() - getWidth() / 2, getY() - getHeight() / 2, getWidth(), getHeight());
			sprite.setPosition(getX() - getWidth() / 2, getY() - getHeight() / 2);
			sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
			sprite.draw(batch);
		}
	}
	
	public void applyLinearImpulse(float velocityX, float velocityY) {
		velocity.x = velocityX;
		velocity.y = velocityY;
		
		applyLinearImpulse();
	}
	
	
	public void applyLinearImpulse() {
		body.applyLinearImpulse(velocity.x, velocity.y, body.getWorldCenter().x, body.getWorldCenter().y, true);
	}
	
	public void applyForceToCenter(float x, float y) {
		velocity.x = x;
		velocity.y = y;
		
		applyForceToCenter();
	}
	
	public void applyForceToCenter() {
		body.applyForceToCenter(velocity.x, velocity.y, true);
	}
	
	public void applyAngularImpulse(float force) {
		body.applyAngularImpulse(force, true);
	}
	
	@Override
	public void dispose() {
		sprite = null;
		
	}
}
