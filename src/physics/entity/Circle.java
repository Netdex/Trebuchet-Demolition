package physics.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

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
	super(c, vel);
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

    @Override
    public void drawEntity(Graphics2D g) {
	Shape shape = this.getShape();
	g.fill(shape);
    }
    
    @Override
    public Shape getShape(){
	return new Ellipse2D.Double(loc.x - radius, loc.y - radius, radius * 2, radius * 2);
    }
}
