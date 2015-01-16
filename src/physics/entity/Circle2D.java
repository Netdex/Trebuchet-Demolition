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
public class Circle2D extends Entity2D {
    private int radius;
    public Vector2D loc;

    public Circle2D(Vector2D loc, Vector2D vel, int radius, Color c) {
	super(c, vel);
	this.loc = loc;
	this.radius = radius;
    }

    public Entity2D clone() {
	return new Circle2D(loc.copy(), this.vel.copy(), radius, this.getColor());
    }

    /**
     * Gets the radius of the circle
     * 
     * @return The radius of the circle
     */
    public int getRadius() {
	return radius;
    }

    @Override
    public boolean handleWallCollision(int width, int height, final double RESTITUTION) {
	int radius = this.getRadius();
	double x = this.loc.x;
	double y = this.loc.y;

	// Make sure the circle collides with walls
	boolean retType = false;
	if (x - radius < 0) {
	    this.loc.x = radius;
	    this.vel.x = -this.vel.x / RESTITUTION;
	    retType = true;
	}
	if (y - radius < 0) {
	    this.loc.y = radius;
	    this.vel.y = -this.vel.y / RESTITUTION;
	    retType = true;
	}
	if (x + radius > width) {
	    this.loc.x = width - radius;
	    this.vel.x = -this.vel.x / RESTITUTION;
	    retType = true;
	}
	if (y + radius > height) {
	    this.loc.y = height - radius;
	    this.vel.y = -this.vel.y / RESTITUTION;
	    retType = true;
	}
	return retType;
    }

    @Override
    public CollisionType getCollisionState(Entity2D entity) {
	// Check for this circle to another
	if (entity instanceof Circle2D) {
	    Circle2D circle = (Circle2D) entity;
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
	else if (entity instanceof Rectangle2D) {

	} else if (entity instanceof Target2D) {
	    Target2D target = (Target2D) entity;
	    AABB2D bounds = this.getBoundingBox();

	    CollisionType doesCollide = MathOperations.hasAABBCollision(target, bounds);
	    if (doesCollide == CollisionType.AABB_TO_AABB) {
		if (this instanceof Projectile2D)
		    return CollisionType.WINNING_COLLISION;
		else
		    return CollisionType.CIRCLE_TO_AABB;
	    }
	    return CollisionType.NO_COLLISION;
	}
	// Check for this circle to an AABB
	else if (entity instanceof AABB2D) {
	    AABB2D aabb = (AABB2D) entity;
	    AABB2D bounds = this.getBoundingBox();

	    CollisionType doesCollide = MathOperations.hasAABBCollision(aabb, bounds);
	    if (doesCollide == CollisionType.AABB_TO_AABB) {
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
    public Shape getShape() {
	return new Ellipse2D.Double(loc.x - radius, loc.y - radius, radius * 2, radius * 2);
    }

    @Override
    public Vector2D[] getPointArray() {
	return new Vector2D[] { loc };
    }

    /**
     * Gets the bounds around this circle
     * 
     * @return the bounds around this circle
     */
    public AABB2D getBoundingBox() {
	return new AABB2D(new Vector2D(loc.x - radius, loc.y - radius), new Vector2D(loc.x + radius, loc.y + radius), Vector2D.ZERO, Color.BLACK);
    }
}
