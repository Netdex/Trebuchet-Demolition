package physics.util;

import java.awt.geom.Line2D;

import physics.entity.AABB2D;

/**
 * Contains math operations to simplify physics calculations
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class MathOperations {
    /**
     * Squares a number
     * @param number The number to square
     * @return the squared number
     */
    public static double square(double number) {
	return number * number;
    }

    /**
     * Checks for an AABB collision between two AABBs
     * 
     * @param a An AABB
     * @param b An AABB
     * @return the collision type between the AABBs
     */
    public static CollisionType hasAABBCollision(AABB2D a, AABB2D b) {
	if (a.p2.x < b.p1.x || a.p1.x > b.p2.x)
	    return CollisionType.NO_COLLISION;
	if (a.p2.y < b.p1.y || a.p1.y > b.p2.y)
	    return CollisionType.NO_COLLISION;
	return CollisionType.AABB_TO_AABB;
    }

    /**
     * Gets the shortest distance between a point and a line
     * 
     * @param a Point A of line
     * @param b Point B of line
     * @param p The point
     * @return the shortest distance between a point and a line
     */
    public static double pointToLineDistance(Vector2D a, Vector2D b, Vector2D p) {
	double normalLength = Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y));
	return Math.abs((p.x - a.x) * (b.y - a.y) - (p.y - a.y) * (b.x - a.x)) / normalLength;
    }
    
    /**
     * Gets the shortest distance between a point and a line segment
     * @param a Point A of the line
     * @param b Point B of the line
     * @param p The point to measure the distance from
     * @return The shortest distance between the points
     */
    public static double pointToLineSegDistance(Vector2D a, Vector2D b, Vector2D p){
	return Line2D.ptSegDist(a.x, a.y, b.x, b.y, p.x, p.y);
    }
}
