package physics.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import physics.util.CollisionType;
import physics.util.MathOperations;
import physics.util.Vector2D;

/**
 * Represents a Circle which is an entity
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class Circle extends Entity {
    private int radius;
    public Vector2D loc;

    public Circle(Vector2D loc, Vector2D vel, Vector2D acc, int radius, Color c) {
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
	// Check for this circle to another 
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
	// Check for this circle to a rectangle
	else if(entity instanceof Rectangle){
	    
	    
	}
	else if(entity instanceof Target){
	    Target target = (Target) entity;
	    AABB bounds = this.getBoundingBox();
	    
	    CollisionType doesCollide = MathOperations.hasAABBCollision(target, bounds);
	    if(doesCollide == CollisionType.AABB_TO_AABB){
		return CollisionType.WINNING_COLLISION;
	    }
	    return CollisionType.NO_COLLISION;
	}
	// Check for this circle to an AABB
	else if(entity instanceof AABB){
	    AABB aabb = (AABB) entity;
	    AABB bounds = this.getBoundingBox();
	    
	    CollisionType doesCollide = MathOperations.hasAABBCollision(aabb, bounds);
	    if(doesCollide == CollisionType.AABB_TO_AABB){
		return CollisionType.CIRCLE_TO_AABB;
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

    @Override
    public Vector2D[] getPointArray() {
	return new Vector2D[]{loc};
    }
    
    /**
     * Gets the bounds around this circle
     * @return the bounds around this circle
     */
    public AABB getBoundingBox(){
	return new AABB(new Vector2D(loc.x - radius, loc.y - radius), new Vector2D(loc.x + radius, loc.y + radius), Vector2D.ZERO, Color.BLACK);
    }
}
