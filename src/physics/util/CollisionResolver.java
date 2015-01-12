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

	Vector relativeA = new Vector(diffX, diffY);
	relativeA.normalize();
	relativeA = relativeA.multiply(a.vel.length());
	Vector relativeB = new Vector(diffX, diffY);
	relativeB.normalize();
	relativeB = relativeB.multiply(b.vel.length());
//	double dist = a.loc.distance(b.loc);
//	a.loc.y -= ((a.getRadius() + b.getRadius()) - dist) / 2;
//	b.loc.y += ((a.getRadius() + b.getRadius()) - dist) / 2;
//	a.loc.x -= ((a.getRadius() + b.getRadius()) - dist) / 2;
//	b.loc.x += ((a.getRadius() + b.getRadius()) - dist) / 2;
	
	a.vel.x = relativeA.x / RESTITUTION;
	a.vel.y = relativeA.y / RESTITUTION;
	b.vel.x = -relativeB.x / RESTITUTION;
	b.vel.y = -relativeB.y / RESTITUTION;
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
