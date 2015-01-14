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
     * Resolves a circle to circle collision<br>
     * <a href=http://en.wikipedia.org/wiki/Elastic_collision>http://en.wikipedia.org/wiki/Elastic_collision</a>
     * 
     * @param a The first circle
     * @param b The second circle
     * @param RESTITUTION The minimum restitution between the circles
     */
    public static void resolveCircleCollision(Circle a, Circle b, final double RESTITUTION) {
	// double diffX = b.loc.x - a.loc.x;
	// double diffY = b.loc.y - a.loc.y;
	//
	// Vector relativeA = new Vector(diffX, diffY);
	// relativeA.normalize();
	// relativeA = relativeA.multiply(a.vel.length());
	// Vector relativeB = new Vector(diffX, diffY);
	// relativeB.normalize();
	// relativeB = relativeB.multiply(b.vel.length());
	// double dist = a.loc.distance(b.loc);
	// a.loc.y -= ((a.getRadius() + b.getRadius()) - dist) / 2;
	// b.loc.y += ((a.getRadius() + b.getRadius()) - dist) / 2;
	// a.loc.x -= ((a.getRadius() + b.getRadius()) - dist) / 2;
	// b.loc.x += ((a.getRadius() + b.getRadius()) - dist) / 2;
	//
	// a.vel.x = relativeA.x / RESTITUTION;
	// a.vel.y = relativeA.y / RESTITUTION;
	// b.vel.x = -relativeB.x / RESTITUTION;
	// b.vel.y = -relativeB.y / RESTITUTION;

	// Calculate variables in physics collision
	double totalMass = a.getMass() + b.getMass();

	// Beta is the angle of the vector from the x-axis
	double betaA = computeBeta(a.vel.x, a.vel.y);
	double betaB = computeBeta(a.vel.x, a.vel.y);

	// Phi is the angle of the line connecting the centers of both circles to the x-axis
	double phi = computeBeta(b.loc.x - a.loc.x, b.loc.y - a.loc.y);

	// Store the length of the two vectors immediately, since length() uses the costly Math.sqrt() function
	double lengthA = a.vel.length();
	double lengthB = b.vel.length();

	double newXA = ((lengthA * Math.cos(betaA - phi) * (a.getMass() - b.getMass()) + 2 * b.getMass() * lengthB * Math.cos(betaB - phi)) / totalMass) * Math.cos(phi) + lengthA
		* Math.sin(betaA - phi) * Math.cos(phi + Math.PI / 2);
	double newYA = ((lengthA * Math.cos(betaA - phi) * (a.getMass() - b.getMass()) + 2 * b.getMass() * lengthB * Math.cos(betaB - phi)) / totalMass) * Math.sin(phi) + lengthA
		* Math.sin(betaA - phi) * Math.sin(phi + Math.PI / 2);
	double newXB = ((lengthB * Math.cos(betaB - phi) * (b.getMass() - a.getMass()) + 2 * a.getMass() * lengthA * Math.cos(betaA - phi)) / totalMass) * Math.cos(phi) + lengthB
		* Math.sin(betaB - phi) * Math.cos(phi + Math.PI / 2);
	double newYB = ((lengthB * Math.cos(betaB - phi) * (b.getMass() - a.getMass()) + 2 * a.getMass() * lengthA * Math.cos(betaA - phi)) / totalMass) * Math.sin(phi) + lengthB
		* Math.sin(betaB - phi) * Math.sin(phi + Math.PI / 2);

	double penetrationDepth = a.loc.distance(b.loc);
	a.loc = a.loc.subtract(new Vector(penetrationDepth / 2, penetrationDepth / 2));
	b.loc = b.loc.add(new Vector(penetrationDepth / 2, penetrationDepth / 2));
	a.vel = new Vector(newXA, newYA);
	b.vel = new Vector(newXB, newYB);
    }

    /**
     * Computes the angle relative to the positive x-axis
     * 
     * @param velX The x-coordinate of the point
     * @param velY The y-coordinate of the point
     * @return the angle relative to the positive x-axis
     */
    public static double computeBeta(double velX, double velY) {
	if (velX < 0) {
	    return Math.PI + Math.atan(velY / velX);

	} else {
	    return Math.atan(velY / velX);
	}
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
