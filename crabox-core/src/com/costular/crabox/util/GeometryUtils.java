package com.costular.crabox.util;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/** contains some useful methods for geometric calculations */
public abstract class GeometryUtils {

	/**
	 * @param vertices the {@link Vector2 vertices} to get the size of
	 * @return a {@link Vector2} representing the size of a rectangle that could contain the {@link Vector2 vertices}
	 */
	public static Vector2 getSize(Vector2[] vertices) {
		return new Vector2(Math.abs(getMaxX(vertices) - getMinX(vertices)), Math.abs(getMaxY(vertices) - getMinY(vertices)));
	}

	/**
	 * @param vertices the {@link Vector2 vertices} to measure
	 * @return the width of the given {@link Vector2 vertices}
	 */
	public static float getWidth(Vector2[] vertices) {
		return getSize(vertices).x;
	}

	/**
	 * @param vertices the {@link Vector2 vertices} to measure
	 * @return the height of the given {@link Vector2 vertices}
	 */
	public static float getHeight(Vector2[] vertices) {
		return getSize(vertices).y;
	}

	/**
	 * @param vertices the {@link Vector2 vertices} out of which the minimal x value should be returned
	 * @return the minimal x value in the given {@link Vector2 vertices}
	 */
	public static float getMinX(Vector2[] vertices) {
		float minX = Float.POSITIVE_INFINITY;
		for(Vector2 vertice : vertices)
			minX = vertice.x < minX ? vertice.x : minX;
		return minX;
	}

	/**
	 * @param vertices the {@link Vector2 vertices} out of which the maximal x value should be returned
	 * @return the maximal x value in the given {@link Vector2 vertices}
	 */
	public static float getMaxX(Vector2[] vertices) {
		float maxX = Float.NEGATIVE_INFINITY;
		for(Vector2 vertice : vertices)
			maxX = vertice.x > maxX ? vertice.x : maxX;
		return maxX;
	}

	/**
	 * @param vertices the {@link Vector2 vertices} out of which the minimal y value should be returned
	 * @return the minimal y value in the given {@link Vector2 vertices}
	 */
	public static float getMinY(Vector2[] vertices) {
		float minY = Float.POSITIVE_INFINITY;
		for(Vector2 vertice : vertices)
			minY = vertice.y < minY ? vertice.y : minY;
		return minY;
	}

	/**
	 * @param vertices the {@link Vector2 vertices} out of which the maximal y value should be returned
	 * @return the maximal y value in the given {@link Vector2 vertices}
	 */
	public static float getMaxY(Vector2[] vertices) {
		float maxY = Float.NEGATIVE_INFINITY;
		for(Vector2 vertice : vertices)
			maxY = vertice.y > maxY ? vertice.y : maxY;
		return maxY;
	}

	/**
	 * @param vector2s the Vector2[] to convert to a float[]
	 * @return the float[] converted from the given Vector2[]
	 */
	public static float[] toFloatArray(Vector2[] vector2s) {
		float[] floats = new float[vector2s.length * 2];

		int vi = -1;
		for(int i = 0; i < floats.length; i++)
			if(i % 2 == 0)
				floats[i] = vector2s[++vi].x;
			else
				floats[i] = vector2s[vi].y;

		return floats;
	}

	/**
	 * @param floats the float[] to convert to a Vector2[]
	 * @return the Vector2[] converted from the given float[]
	 */
	public static Vector2[] toVector2Array(float[] floats) {
		if(floats.length % 2 != 0)
			throw new IllegalArgumentException("the float array's length is not dividable by two, so it won't make up a Vector2 array: " + floats.length);

		Vector2[] vector2s = new Vector2[floats.length / 2];

		int fi = -1;
		for(int i = 0; i < vector2s.length; i++)
			vector2s[i] = new Vector2(floats[++fi], floats[++fi]);

		return vector2s;
	}

	/**
	 * @param vertexCount the number of vertices for each {@link Polygon}
	 * @see #toPolygonArray(Vector2[], int[])
	 */
	public static Polygon[] toPolygonArray(Vector2[] vertices, int vertexCount) {
		int[] vertexCounts = new int[vertices.length / vertexCount];
		for(int i = 0; i < vertexCounts.length; i++)
			vertexCounts[i] = vertexCount;
		return toPolygonArray(vertices, vertexCounts);
	}

	/**
	 * @param vertices the vertices which should be split into a {@link Polygon} array
	 * @param vertexCounts the number of vertices of each {@link Polygon}
	 * @return the {@link Polygon} array extracted from the vertices
	 */
	public static Polygon[] toPolygonArray(Vector2[] vertices, int[] vertexCounts) {
		Polygon[] polygons = new Polygon[vertexCounts.length];
		int vertice = -1;
		for(int i = 0; i < polygons.length; i++) {
			Vector2[] verts = new Vector2[vertexCounts[i]];
			for(int v = 0; v < verts.length; v++)
				verts[v] = vertices[++vertice];
			polygons[i] = new Polygon(toFloatArray(verts));
		}
		return polygons;
	}

	/**
	 * @param polygon the polygon, assumed to be simple
	 * @return if the vertices are in clockwise order 
	 */
	public static boolean areVerticesClockwise(Polygon polygon) {
		return polygon.area() < 0;
	}

	/** @see #areVerticesClockwise(Polygon) */
	public static boolean areVerticesClockwise(float[] vertices) {
		return areVerticesClockwise(new Polygon(vertices));
	}

	/**
	 * @param vertices the vertices of the polygon to examine for convexity, in xy order
	 * @return if the polygon is convex
	 */
	public static boolean isConvex(float[] vertices) {
		// http://www.sunshine2k.de/coding/java/Polygon/Convex/polygon.htm
		Vector2[] polygon = toVector2Array(vertices);

		Vector2 p, v, u, tmp;
		int res = 0;
		for(int i = 0; i < polygon.length; i++) {
			p = polygon[i];
			tmp = polygon[(i + 1) % polygon.length];
			v = new Vector2();
			v.x = tmp.x - p.x;
			v.y = tmp.y - p.y;
			u = polygon[(i + 2) % polygon.length];

			if(i == 0) // in first loop direction is unknown, so save it in res
				res = (int) (u.x * v.y - u.y * v.x + v.x * p.y - v.y * p.x);
			else {
				int newres = (int) (u.x * v.y - u.y * v.x + v.x * p.y - v.y * p.x);
				if(newres > 0 && res < 0 || newres < 0 && res > 0)
					return false;
			}
		}

		return true;
	}

	/** @see #isConvex(float[]) */
	public static boolean isConvex(Polygon polygon) {
		return isConvex(polygon.getVertices());
	}

	/** prints a test to the console */
	@SuppressWarnings("unused")
	private static void test() {
		System.out.println("running GeometryUtils test...");

		float[] floats = new float[] {0, 0, 0, 2, 2, 2, 2, 0};
		System.out.println("\ncreate new float array:");
		for(float f : floats)
			System.out.println(f);

		System.out.println("\nconvert to vector2 array:");
		Vector2[] vecs = toVector2Array(floats);
		for(Vector2 vec : vecs)
			System.out.println(vec);

		System.out.println("\nconvert back to float array:");
		for(float f : toFloatArray(vecs))
			System.out.println(f);

		System.out.println("\ncreating polygon from float array, is it convex? " + isConvex(new Polygon(floats))); // true
		System.out.println("is another one convex? " + isConvex(new Polygon(new float[] {0, 0, 0, 0, 2, 2, 0, 2, 1, 1}))); // false
		System.out.println("is another one with 8 vertices convex? " + isConvex(new Polygon(new float[] {0, 0, 1, 0, 2, 1, 2, 2, 1, 3, 0, 3, -1, 2, -1, 1}))); // true
		System.out.println("is another one with 8 clockwise vertices convex? " + isConvex(new Polygon(new float[] {0, 0, -1, 1, -1, 2, 0, 3, 1, 3, 2, 2, 2, 1, 1, 0}))); // true
		System.out.println("is another one 8 cc v convex? not working in Box2DMapObjectParserTest: " + isConvex(new Polygon(new float[] {0, 0, 16, 16, 32, 16, 48, 0, 48, -16, 32, -32, 16, -32, 0, -16}))); // true? please
		System.out.println("is another one 8 c  v convex? not working in Box2DMapObjectParserTest: " + isConvex(new Polygon(new float[] {0, 0, 0, -16, 16, -32, 32, -32, 48, -16, 48, 0, 32, 16, 16, 16}))); // true? please
		System.out.println("is conclusioned quad convex? " + isConvex(new Polygon(new float[] {0, 0, 2, 0, 2, -2, 0, -2}))); // true
		System.out.println("is another one with clockwise vertices convex? " + isConvex(new Polygon(new float[] {0, 0, 2, 0, 2, 2, 0, 2}))); // true

		System.out.println("\ncreating float array containing three polygons' vertices...");
		float[] mixedVertices = new float[] {0, 0, 1, 0, .5f, 1, // 3
				5, 5, 6, 5, 6.5f, 6, 5.5f, 7, 4, 6, // 5
				2, 2, 4, 2, 4, 4, 3.8f, 2.2f}; // 4
		System.out.println("creating three polygons from these vertices...");
		Polygon[] polygons = toPolygonArray(toVector2Array(mixedVertices), new int[] {3, 5, 4});
		System.out.println(polygons.length + " polygons created:");
		for(short i = 0; i < polygons.length; i++) {
			System.out.println("polygon #" + (i + 1) + " (" + polygons[i].getVertices().length / 2 + " vertices, convex: " + isConvex(polygons[i]) + "):");
			for(Vector2 vertice : toVector2Array(polygons[i].getVertices()))
				System.out.println(vertice);
		}

		System.out.println("\nGeometryUtils test done");
	}

}