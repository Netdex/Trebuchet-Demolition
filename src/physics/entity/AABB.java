package physics.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import physics.util.CollisionType;
import physics.util.MathOperations;
import physics.util.Vector2D;

/**
 * Represents an Axis Aligned Bounding Box (AABB) which is an entity; a rectangle without rotation
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class AABB extends Entity {
    public Vector2D p1;
    public Vector2D p2;

    public AABB(Vector2D p1, Vector2D p2, Vector2D vel, Color c) {
	super(c, vel);
	this.p1 = p1;
	this.p2 = p2;
    }
    
    public AABB(Vector2D p1, Vector2D p2, Vector2D vel, Color c, boolean physics) {
	super(c, vel, physics);
	this.p1 = p1;
	this.p2 = p2;
    }

    public double getMass() {
	// TODO Make getMass() work for AABBs
	return 0;
    }

    /**
     * Gets the width of the AABB
     * 
     * @return The width of the AABB
     */
    public double getWidth() {
	return Math.abs(p1.x - p2.x);
    }

    /**
     * Gets the height of the AABB
     * 
     * @return The height of the AABB
     */
    public double getHeight() {
	return Math.abs(p1.y - p2.y);
    }

    public CollisionType getCollisionState(Entity entity) {
	if (entity instanceof AABB) {
	    return MathOperations.hasAABBCollision(this, (AABB) entity);
	}
	return CollisionType.NO_COLLISION;
    }

    @Override
    public void drawEntity(Graphics2D g) {
	Shape shape = this.getShape();
	g.fill(shape);
    }
    
    @Override
    public Shape getShape(){
	return new Rectangle2D.Double(p1.x, p1.y, getWidth(), getHeight());
    }

    @Override
    public Vector2D[] getPointArray() {
	return new Vector2D[]{p1, p2};
    }
    
    /**
     * Gets the center of the AABB
     * @return the center of the AABB
     */
    public Vector2D getCenter(){
	return new Vector2D(p1.x + getWidth() / 2, p1.y + getHeight() / 2);
    }
}
