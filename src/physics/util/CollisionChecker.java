package physics.util;

import physics.entity.AABB;
import physics.entity.Circle;
import physics.entity.Rectangle;

/**
 * Contains most of the math to check for collisions between objects
 * 
 * @author Gordon Guan
 * @version Dec 2014
 * 
 */
public class CollisionChecker {

    /**
     * Checks for a collision between two circles
     * 
     * @param a
     *            The first circle
     * @param b
     *            The second circle
     * @return The type of collision the circles are having
     */
    public static CollisionType circleToCircle(Circle a, Circle b) {
	int ar = a.getRadius();
	int br = b.getRadius();
	double ax = a.loc.x;
	double ay = a.loc.y;
	double bx = b.loc.x;
	double by = b.loc.y;
	int r = ar + br;
	r *= r;
	double dist = (ax - bx) * (ax - bx) + (ay - by) * (ay - by);
	if (r > dist) {
	    return CollisionType.CIRCLE_TO_CIRCLE;
	}
	return CollisionType.NO_COLLISION;
    }

    /**
     * Checks whether two Axis Aligned Bounding Boxes are colliding
     * 
     * @param a
     *            The first AABB
     * @param b
     *            The second AABB
     * @return The type of collision the two AABBs are having
     */
    public static CollisionType AABBtoAABB(AABB a, AABB b) {
	if (a.p2.x < b.p1.x || a.p1.x > b.p2.x)
	    return CollisionType.NO_COLLISION;
	if (a.p2.y < b.p1.y || a.p1.y > b.p2.y)
	    return CollisionType.NO_COLLISION;
	return CollisionType.AABB_TO_AABB;
    }

    /**
     * Resolves a circle to circle collision
     * 
     * @param a
     *            The first circle
     * @param b
     *            The second circle
     * @param RESTITUTION
     *            The minimum restitution between the circles
     */
    public static void resolveCircleCollision(Circle a, Circle b,
	    final double RESTITUTION) {
	double magA = a.vel.length();
	double magB = b.vel.length();
	double diffX = b.loc.x - a.loc.x;
	double diffY = b.loc.y - a.loc.y;

	Vector relative = new Vector(diffX, diffY);
	relative.normalize();

	a.vel.x = relative.x / RESTITUTION;
	a.vel.y = relative.y / RESTITUTION;
	b.vel.x = -relative.x / RESTITUTION;
	b.vel.y = -relative.y / RESTITUTION;
    }

    /**
     * Resolves a AABB to AABB collision
     * 
     * @param a
     *            The first AABB
     * @param b
     *            The second AABB
     * @param RESTITUTION
     *            The minimum restitution between the AABBs
     * @deprecated Broken code
     */
    public static void resolveAABBCollision(AABB a, AABB b,
	    final double RESTITUTION) {
	Vector relative = b.vel.subtract(a.vel);

	double widtha = a.getWidth();
	double widthb = b.getWidth();
	double widthOverlap = widtha + widthb - Math.abs(b.p1.x - a.p2.x);

	double heighta = a.getHeight();
	double heightb = b.getHeight();
	double heightOverlap = heighta + heightb - Math.abs(b.p1.y - a.p2.y);

	if (widthOverlap > 0) {
	    a.vel.x = -a.vel.x / RESTITUTION;
	    b.vel.x = -b.vel.x / RESTITUTION;
	    if (heightOverlap > 0) {
		a.vel.y = -a.vel.y;
		b.vel.y = -b.vel.y;
	    }
	}
    }

    /**
     * Resolves a Rectangle to Rectangle collision
     * 
     * @param a
     *            The first Rectangle
     * @param b
     *            The second Rectangle
     * @param RESTITUTION
     *            The minimum restitution between the Rectangles
     */
    public static void resolveRectangleCollision(Rectangle a, Rectangle b, final double RESTITUTION) {

    }
}
