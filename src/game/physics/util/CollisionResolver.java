package game.physics.util;

import game.physics.entity.AABB2D;
import game.physics.entity.Circle2D;
import game.physics.entity.Rectangle2D;

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
    public static void resolveCircleCollision(Circle2D a, Circle2D b, final double RESTITUTION) {
	// // Calculate variables in physics collision
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

	a.loc = a.loc.subtract(a.vel);
	a.vel = new Vector2D(newXA, newYA);
	b.vel = new Vector2D(newXB, newYB);
    }

    /**
     * Computes the angle relative to the positive x-axis
     * 
     * @param velX The x-coordinate of the point
     * @param velY The y-coordinate of the point
     * @return the angle relative to the positive x-axis
     */
    public static double computeBeta(double velX, double velY) {
	// Return a different angle depending on the quadrant of the point
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
    public static void resolveAABBCollision(AABB2D a, AABB2D b, final double RESTITUTION) {

	double widtha = a.getWidth();
	double widthb = b.getWidth();
	double widthOverlap = widtha + widthb - Math.abs(b.p1.x - a.p2.x);

	double heighta = a.getHeight();
	double heightb = b.getHeight();
	double heightOverlap = heighta + heightb - Math.abs(b.p1.y - a.p2.y);

	// If the width overlaps, then reverse both their x velocities, and if the height overlaps, then reverse both their y velocities
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
     * Resolves a Circle to AABB collision
     * 
     * @param a The circle
     * @param b The AABB
     * @param RESTITUTION The restitution to use in the calculation
     */
    public static void resolveAABBCircleCollision(Circle2D a, AABB2D b, final double RESTITUTION) {
	double vertDist = MathOperations.pointToLineSegDistance(b.p1, new Vector2D(b.p1.x, b.p2.y), a.loc);
	double vertDist2 = MathOperations.pointToLineSegDistance(new Vector2D(b.p2.x, b.p1.y), b.p2, a.loc);
	double horizDist = MathOperations.pointToLineSegDistance(b.p1, new Vector2D(b.p2.x, b.p1.y), a.loc);
	double horizDist2 = MathOperations.pointToLineSegDistance(new Vector2D(b.p1.x, b.p2.y), b.p2, a.loc);

	// Teleport the circle outside of the AABB first
	a.loc.subtract(a.vel);
	double radius = a.getRadius();
	
	// Check if any distances of the circle to any of the AABB's edges are less than the radius
	if (vertDist <= radius || vertDist2 <= radius) {
	    a.vel.x = -a.vel.x / RESTITUTION;
	    b.vel.x = -b.vel.x / RESTITUTION;
	}
	if (horizDist <= radius || horizDist2 <= radius) {
	    a.vel.y = -a.vel.y / RESTITUTION;
	    b.vel.y = -b.vel.y / RESTITUTION;
	}
    }

    /**
     * Resolves a Circle to Rectangle collision
     * 
     * @param a The first Circle
     * @param b The second Rectangle
     * @param RESTITUTION The minimum restitution between the Circle and the Rectangle
     */
    public static void resolveRectCircleCollision(Circle2D a, Rectangle2D b, final double RESTITUTION) {
	Vector2D center = a.getCenter();
	// Get the distance between the center of the circle and every edge of the rectangle
	double dist1 = MathOperations.pointToLineDistance(b.p1, b.p2, center);
	double dist2 = MathOperations.pointToLineDistance(b.p2, b.p4, center);
	double dist3 = MathOperations.pointToLineDistance(b.p4, b.p3, center);
	double dist4 = MathOperations.pointToLineDistance(b.p3, b.p1, center);
	double min = Math.min(dist1, Math.min(dist2, Math.min(dist3, dist4)));

	b.translate(-b.vel.x, -b.vel.y);
	
	Vector2D plane;
	if (dist1 == min) {
	    plane = b.p1.subtract(b.p2);
	} else if (dist2 == min) {
	    plane = b.p2.subtract(b.p4);
	} else if (dist3 == min) {
	    plane = b.p4.subtract(b.p3);
	} else {
	    plane = b.p3.subtract(b.p1);
	}

	// Get the normal, which is the line perpendicular to the plane, by flipping y and x, however make y negative since swing uses inverse y coordinates
	a.vel = a.vel.reflect(new Vector2D(-plane.y,plane.x));
//	a.vel.y /= RESTITUTION;

    }
}
