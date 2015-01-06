package physics.util;

import physics.PhysicsEngine;
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

	a.vel.x = relative.x;
	a.vel.y = relative.y;
	b.vel.x = -relative.x;
	b.vel.y = -relative.y;
	// Calculate new velocities after the collision
	// double newAVelX = (a.vel.x * (a.getMass() - b.getMass()) + (2 * b.getMass() * b.vel.x)) / (a.getMass() + b.getMass());
	// double newAVelY = (a.vel.y * (a.getMass() - b.getMass()) + (2 * b.getMass() * b.vel.y)) / (a.getMass() + b.getMass());
	// double newBVelX = (b.vel.x * (b.getMass() - a.getMass()) + (2 * a.getMass() * a.vel.x)) / (a.getMass() + b.getMass());
	// double newBVelY = (b.vel.y * (b.getMass() - a.getMass()) + (2 * a.getMass() * a.vel.y)) / (a.getMass() + b.getMass());
	// a.vel.x = newAVelX;
	// a.vel.y = newAVelY;
	// b.vel.x = newBVelX;
	// b.vel.y = newBVelY;
	// a.loc.x += newAVelX;
	// a.loc.y += newAVelY;
	// b.loc.x += newBVelX;
	// b.loc.y += newBVelY;
	// double xDist = Math.abs(b.loc.x - a.loc.x);
	// double yDist = Math.abs(b.loc.y - a.loc.y);
	// double distSquared = xDist*xDist + yDist*yDist;
	//
	// double xVel = b.vel.x - a.vel.x;
	// double yVel = b.vel.y - a.vel.y;
	//
	// double dotProduct = xDist * xVel + yDist * yVel;
	// if(dotProduct > 0){
	// double collisionScale = dotProduct / distSquared;
	// double xCol = xDist * collisionScale;
	// double yCol = yDist * collisionScale;
	// double totalMass = a.getMass() + b.getMass();
	// double colWA = 2 * b.getMass() / totalMass;
	// double colWB = 2 * a.getMass() / totalMass;
	// a.vel.x += colWA * xCol;
	// a.vel.y += colWA * yCol;
	// b.vel.x -= colWB * xCol;
	// b.vel.y -= colWB * yCol;
	// a.loc.x += a.vel.x;
	// a.loc.y += a.vel.y;
	// b.loc.x += b.vel.x;
	// b.loc.y += b.vel.y;
	//
	// }
	//
	// double aVelX = a.vel.x;
	// double aVelY = a.vel.y;
	// double bVelX = b.vel.x;
	// double bVelY = b.vel.y;
	//
	// a.vel.x = bVelX;
	// a.vel.y = bVelY;
	// b.vel.x = aVelX;
	// b.vel.y = aVelY;
	// a.loc.x += a.vel.x;
	// a.loc.y += a.vel.y;
	// b.loc.x -= b.vel.x;
	// b.loc.y -= b.vel.y;

	// double angle = Math.atan2(a.loc.y - b.loc.y, a.loc.x - b.loc.x);
	//
	// double averageX = (Math.abs(a.vel.x) + Math.abs(b.vel.x)) / 2;
	// double averageY = (Math.abs(a.vel.y) + Math.abs(b.vel.y)) / 2;
	// a.vel.x = -(averageX * Math.cos(angle));
	// a.vel.y = -(averageY * Math.sin(angle));
	// b.vel.x = -(averageY * Math.cos(angle - Math.PI));
	// b.vel.y = -(averageY * Math.sin(angle - Math.PI));
	//
	// // Give the circles anti-gravity to prevent sticking
	// a.acc = PhysicsEngine.GRAVITY.multiply(-1.5);
	// b.acc = PhysicsEngine.GRAVITY.multiply(-1.5);
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
