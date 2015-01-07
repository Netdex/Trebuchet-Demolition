package physics.util;

import physics.entity.AABB;
import physics.entity.Circle;
import physics.entity.Rectangle;

/**
 * Contains most of the math to resolve collisions
 * 
 * @author Gordon Guan
 * @version Dec 2014
 * 
 */
public class CollisionResolver {

    /**
     * Resolves a circle to circle collision
     * 
     * @param a The first circle
     * @param b The second circle
     * @param RESTITUTION The minimum restitution between the circles
     */
    public static void resolveCircleCollision(Circle a, Circle b, final double RESTITUTION) {
	double diffX = b.loc.x - a.loc.x;
	double diffY = b.loc.y - a.loc.y;

	double magnitudeA = a.vel.length();
	double magnitudeB = b.vel.length();

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
     * @param a The first AABB
     * @param b The second AABB
     * @param RESTITUTION The minimum restitution between the AABBs
     */
    public static void resolveAABBCollision(AABB a, AABB b, final double RESTITUTION) {

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
     * @param a The first Rectangle
     * @param b The second Rectangle
     * @param RESTITUTION The minimum restitution between the Rectangles
     */
    public static void resolveRectangleCollision(Rectangle a, Rectangle b, final double RESTITUTION) {

    }
}
