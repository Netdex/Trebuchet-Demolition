package physics.entity;

import java.awt.Color;

import physics.util.CollisionType;
import physics.util.Vector;

/**
 * Represents a Circle which is an entity
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class Circle extends Entity {
    private int radius;
    public Vector loc;

    public Circle(Vector loc, Vector vel, Vector acc, int radius, Color c) {
	super(EntityType.CIRCLE, c, vel);
	this.loc = loc;
	this.radius = radius;
    }

    /**
     * Gets the radius of the circle
     * 
     * @return The radius of the circle
     */
    public int getRadius() {
	return radius;
    }

    public CollisionType getCollisionState(Entity entity) {
	if (entity instanceof Circle) {
	    Circle circle = (Circle) entity;
	    int otherRadius = circle.getRadius();
	    int totalRadius = radius + otherRadius;
	    double ax = loc.x;
	    double ay = loc.y;
	    double bx = circle.loc.x;
	    double by = circle.loc.y;
	    totalRadius *= totalRadius;
	    double dist = (ax - bx) * (ax - bx) + (ay - by) * (ay - by);
	    if (totalRadius > dist) {
		return CollisionType.CIRCLE_TO_CIRCLE;
	    }
	    return CollisionType.NO_COLLISION;
	}
	return CollisionType.NO_COLLISION;
    }

    public double getMass() {
	return radius;
    }
}
