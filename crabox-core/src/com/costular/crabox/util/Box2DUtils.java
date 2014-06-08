package com.costular.crabox.util;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Transform;

/**
 * Contains some methods to get the size, position, vertices, min/max x/y of Box2D's {@link Body Bodies}, {@link Fixture Fixtures} and {@link Shape Shapes}.
 * @author dermetfan
 */
public abstract class Box2DUtils {

	/**
	 * @param fixture The {@link Fixture} which vertices should be returned. If the given {@link Fixture} holds a {@link CircleShape}, the vertices of a bounding box will be returned.
	 * @return the vertices of the given {@link Fixture} 
	 */
	public static Vector2[] getVertices(Fixture fixture) {
		return getVertices(fixture.getShape());
	}

	/**
	 * @param shape The {@link Shape} which vertices should be returned. If a {@link CircleShape} is given, the vertices of a bounding box will be returned.
	 * @return the vertices of the given {@link Shape} 
	 */
	public static Vector2[] getVertices(Shape shape) {
		Vector2[] vertices = null;

		if(shape instanceof PolygonShape) {
			PolygonShape polygonShape = (PolygonShape) shape;

			// make a vertices array and fill it with new Vector2s
			vertices = new Vector2[polygonShape.getVertexCount()];
			for(int v = 0; v < vertices.length; v++)
				vertices[v] = new Vector2();

			// put the vertices in the just made vertices array
			for(int index = 0; index < polygonShape.getVertexCount(); index++)
				polygonShape.getVertex(index, vertices[index]);
		} else if(shape instanceof ChainShape) {
			ChainShape chainShape = (ChainShape) shape;

			// make a vertices array and fill it with new Vector2s
			vertices = new Vector2[chainShape.getVertexCount()];
			for(int v = 0; v < vertices.length; v++)
				vertices[v] = new Vector2();

			// put the vertices in the just made vertices array
			for(int index = 0; index < chainShape.getVertexCount(); index++)
				chainShape.getVertex(index, vertices[index]);
		} else if(shape instanceof EdgeShape) {
			EdgeShape edgeShape = (EdgeShape) shape;

			Vector2 vertex1 = new Vector2(), vertex2 = new Vector2();
			edgeShape.getVertex1(vertex1);
			edgeShape.getVertex2(vertex2);

			vertices = new Vector2[] {vertex1, vertex2};
		} else if(shape instanceof CircleShape) {
			CircleShape circleShape = (CircleShape) shape;

			vertices = new Vector2[] {
					new Vector2(getPositionRelative(circleShape).x - getWidth(circleShape) / 2, getPositionRelative(circleShape).y + getHeight(circleShape) / 2), // top left
					new Vector2(getPositionRelative(circleShape).x - getWidth(circleShape) / 2, getPositionRelative(circleShape).y - getHeight(circleShape) / 2), // bottom left
					new Vector2(getPositionRelative(circleShape).x + getWidth(circleShape) / 2, getPositionRelative(circleShape).y - getHeight(circleShape) / 2), // bottom right
					new Vector2(getPositionRelative(circleShape).x + getWidth(circleShape) / 2, getPositionRelative(circleShape).y + getHeight(circleShape) / 2) // top right
			};
		} else {
			assert false : "Shape type '" + shape.getType().name() + "' does not exist";
			throw new IllegalArgumentException("Shapes of the type '" + shape.getType().name() + "' are not supported");
		}

		return vertices;
	}

	/**
	 * @param body the {@link Body} to measure
	 * @return a {@link Vector2} representing the size of the given {@link Body}
	 */
	public static Vector2 getSize(Body body) {
		Vector2 min = new Vector2(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY), max = new Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

		for(Fixture fixture : body.getFixtureList()) {
			min.x = getMinX(fixture) < min.x ? getMinX(fixture) : min.x;
			min.y = getMinY(fixture) < min.y ? getMinY(fixture) : min.y;
			max.x = getMaxX(fixture) > max.x ? getMaxX(fixture) : max.x;
			max.y = getMaxY(fixture) > max.y ? getMaxY(fixture) : max.y;
		}

		return new Vector2(Math.abs(max.x - min.x), Math.abs(max.y - min.y));
	}

	/**
	 * @param fixture the {@link Fixture} to measure
	 * @return a {@link Vector2} representing the size of a rectangle that could contain the given {@link Fixture}
	 */
	public static Vector2 getSize(Fixture fixture) {
		return getSize(fixture.getShape());
	}

	/**
	 * @param shape the {@link Shape} to measure
	 * @return a {@link Vector2} representing the size of a rectangle that could contain the given {@link Shape}
	 */
	public static Vector2 getSize(Shape shape) {
		Vector2 size = new Vector2();

		if(shape instanceof CircleShape)
			size.set(shape.getRadius() * 2, shape.getRadius() * 2);
		else
			size.set(GeometryUtils.getSize(getVertices(shape)));

		return size;
	}

	/**
	 * @param body the {@link Body} to measure
	 * @return the width of the given {@link Body} in meters
	 */
	public static float getWidth(Body body) {
		return getSize(body).x;
	}

	/**
	 * @param body the {@link Body} to measure
	 * @return the height of the given {@link Body} in meters
	 */
	public static float getHeight(Body body) {
		return getSize(body).y;
	}

	/**
	 * @param fixture the {@link Fixture} to measure
	 * @return the width of the given {@link Fixture} in meters
	 */
	public static float getWidth(Fixture fixture) {
		return getSize(fixture).x;
	}

	/**
	 * @param fixture the {@link Fixture} to measure
	 * @return the height of the given {@link Fixture} in meters
	 */
	public static float getHeight(Fixture fixture) {
		return getSize(fixture).y;
	}

	/**
	 * @param shape the {@link Shape} to measure
	 * @return the width of the given {@link Shape} in meters
	 */
	public static float getWidth(Shape shape) {
		return getSize(shape).x;
	}

	/**
	 * @param shape the {@link Shape} to measure
	 * @return the height of the given {@link Shape} in meters
	 */
	public static float getHeight(Shape shape) {
		return getSize(shape).y;
	}

	/**
	 * @param body the {@link Body} out of which {@link Fixture Fixture's} {@link Vector2 vertices} the minimal x value should be returned
	 * @return the minimal x value in the given {@link Body Body's} fixtures' {@link Vector2 vertices}
	 */
	public static float getMinX(Body body) {
		float minX = Float.POSITIVE_INFINITY;
		for(Fixture fixture : body.getFixtureList())
			minX = getMinX(fixture) < minX ? getMinX(fixture) : minX;
		return minX;
	}

	/**
	 * @param body the {@link Body} out of which {@link Fixture Fixture's} {@link Vector2 vertices} the maximal x value should be returned
	 * @return the maximal x value in the given {@link Body Body's} fixtures' {@link Vector2 vertices}
	 */
	public static float getMaxX(Body body) {
		float maxX = Float.NEGATIVE_INFINITY;
		for(Fixture fixture : body.getFixtureList())
			maxX = getMaxX(fixture) > maxX ? getMaxX(fixture) : maxX;
		return maxX;
	}

	/**
	 * @param body the {@link Body} out of which {@link Fixture Fixture's} {@link Vector2 vertices} the minimal y value should be returned
	 * @return the minimal y value in the given {@link Body Body's} fixtures' {@link Vector2 vertices}
	 */
	public static float getMinY(Body body) {
		float minY = Float.POSITIVE_INFINITY;
		for(Fixture fixture : body.getFixtureList())
			minY = getMinY(fixture) < minY ? getMinY(fixture) : minY;
		return minY;
	}

	/**
	 * @param body the {@link Body} out of which {@link Fixture Fixture's} {@link Vector2 vertices} the maximal y value should be returned
	 * @return the maximal y value in the given {@link Body Body's} fixtures' {@link Vector2 vertices}
	 */
	public static float getMaxY(Body body) {
		float maxY = Float.NEGATIVE_INFINITY;
		for(Fixture fixture : body.getFixtureList())
			maxY = getMaxX(fixture) > maxY ? getMaxX(fixture) : maxY;
		return maxY;
	}

	/**
	 * @param fixture the {@link Fixture} out of which {@link Vector2 vertices} the minimal x value should be returned
	 * @return the minimal x value in the given {@link Fixture Fixture's} {@link Vector2 vertices}
	 */
	public static float getMinX(Fixture fixture) {
		return getMinX(fixture.getShape());
	}

	/**
	 * @param fixture the {@link Fixture} out of which {@link Vector2 vertices} the maximal x value should be returned
	 * @return the maximal x value in the given {@link Fixture Fixture's} {@link Vector2 vertices}
	 */
	public static float getMaxX(Fixture fixture) {
		return getMaxX(fixture.getShape());
	}

	/**
	 * @param fixture the {@link Fixture} out of which {@link Vector2 vertices} the minimal y value should be returned
	 * @return the minimal y value in the given {@link Fixture Fixture's} {@link Vector2 vertices}
	 */
	public static float getMinY(Fixture fixture) {
		return getMinY(fixture.getShape());
	}

	/**
	 * @param fixture the {@link Fixture} out of which {@link Vector2 vertices} the maximal y value should be returned
	 * @return the maximal y value in the given {@link Fixture Fixture's} {@link Vector2 vertices}
	 */
	public static float getMaxY(Fixture fixture) {
		return getMaxY(fixture.getShape());
	}

	/**
	 * @param shape the {@link Shape} out of which vertices the minimal x value should be returned
	 * @return the minimal x value in the given {@link Shape Shape's} {@link Vector2 vertices}
	 */
	public static float getMinX(Shape shape) {
		return GeometryUtils.getMinX(getVertices(shape));
	}

	/**
	 * @param shape the {@link Shape} out of which vertices the maximal x value should be returned
	 * @return the maximal x value in the given given {@link Shape Shape's} {@link Vector2 vertices}
	 */
	public static float getMaxX(Shape shape) {
		return GeometryUtils.getMaxX(getVertices(shape));
	}

	/**
	 * @param shape the {@link Shape} out of which vertices the minimal y value should be returned
	 * @return the minimal y value in the given given {@link Shape Shape's} {@link Vector2 vertices}
	 */
	public static float getMinY(Shape shape) {
		return GeometryUtils.getMinY(getVertices(shape));
	}

	/**
	 * @param shape the {@link Shape} out of which vertices the maximal y value should be returned
	 * @return the maximal y value in the given given {@link Shape Shape's} {@link Vector2 vertices}
	 */
	public static float getMaxY(Shape shape) {
		return GeometryUtils.getMaxY(getVertices(shape));
	}

	/**
	 * @param fixture the {@link Fixture} which world position should be returned
	 * @return the world position of the given {@link Fixture}
	 */
	public static Vector2 getPosition(Fixture fixture) {
		return fixture.getBody().getPosition().add(getPositionRelative(fixture.getShape(), fixture.getBody().getTransform().getRotation()));
	}

	/**
	 * @param body the {@link Body} of the given {@link Shape} to get the world position
	 * @param shape the {@link Shape} which world position should be returned
	 * @return the world position of the given {@link Shape}
	 */
	public static Vector2 getPosition(Shape shape, Body body) {
		return body.getPosition().add(getPositionRelative(shape, body.getTransform().getRotation()));
	}

	/**
	 * @param fixture the {@link Fixture} which position should be returned 
	 * @return the position of the given {@link Fixture} relative to its {@link Body}
	 */
	public static Vector2 getPositionRelative(Fixture fixture) {
		return getPositionRelative(fixture.getShape(), fixture.getBody().getTransform().getRotation());
	}

	/**
	 * @param shape the {@link CircleShape} which position should be returned
	 * @return the position of the given {@link CircleShape} relative to its {@link Body}
	 */
	public static Vector2 getPositionRelative(CircleShape shape) {
		return shape.getPosition();
	}

	/**
	 * @param shape the {@link Shape} which position should be returned
	 * @param rotation the rotation of the {@link Body Body's} {@link Transform} this shape is attached to
	 * @return the position of the given {@link Shape} relative to its {@link Body}
	 */
	public static Vector2 getPositionRelative(Shape shape, float rotation) {
		Vector2 position = new Vector2();

		// get the position without rotation
		Vector2[] shapeVertices = getVertices(shape);
		position.set(GeometryUtils.getMaxX(shapeVertices) - GeometryUtils.getWidth(shapeVertices) / 2, GeometryUtils.getMaxY(shapeVertices) - GeometryUtils.getHeight(shapeVertices) / 2);

		// transform position according to rotation
		// http://stackoverflow.com/questions/1469149/calculating-vertices-of-a-rotated-rectangle
		float xx = position.x, xy = position.y, yx = position.x, yy = position.y;

		xx = xx * (float) Math.cos(rotation) - xy * (float) Math.sin(rotation);
		yy = yx * (float) Math.sin(rotation) + yy * (float) Math.cos(rotation);

		position.set(xx, yy);

		return position;
	}

}