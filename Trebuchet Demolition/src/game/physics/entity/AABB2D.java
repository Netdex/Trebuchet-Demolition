package game.physics.entity;

import game.GamePanel;
import game.graphics.GraphicsTools;
import game.physics.util.CollisionType;
import game.physics.util.MathOperations;
import game.physics.util.Vector2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;

/**
 * Represents an Axis Aligned Bounding Box (AABB) which is an entity; a rectangle without rotation
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class AABB2D extends Entity2D {
    private static Image AABBTexture = GamePanel.metalTexture;

    public Vector2D p1;
    public Vector2D p2;

    /**
     * Constructs an AABB
     * @param p1 The first point
     * @param p2 The second point
     * @param vel The velocity
     */
    public AABB2D(Vector2D p1, Vector2D p2, Vector2D vel) {
	super(vel, AABBTexture);
	this.p1 = p1;
	this.p2 = p2;
    }

    /**
     * Constructs an AABB
     * @param p1 The first point
     * @param p2 The second point
     * @param vel The velocity
     * @param physics Whether this entity has physics
     */
    public AABB2D(Vector2D p1, Vector2D p2, Vector2D vel, boolean physics) {
	super(vel, physics, AABBTexture);
	this.p1 = p1;
	this.p2 = p2;
    }

    @Override
    public void translate(double x, double y){
	for(Vector2D vector : this.getPointArray()){
	    vector.x += x;
	    vector.y += y;
	}
    }
    
    @Override
    public boolean handleWallCollision(int width, int height, final double RESTITUTION) {
	boolean retType = false;
	if (this.p1.x < 0) {
	    double correction = Math.abs(this.p1.x);
	    this.p1.x += correction;
	    this.p2.x += correction;
	    this.vel.x = -this.vel.x / RESTITUTION;
	    retType = true;
	}
	if (this.p1.y < 0) {
	    double correction = Math.abs(this.p1.y);
	    this.p1.y += correction;
	    this.p2.y += correction;
	    this.vel.y = -this.vel.y / RESTITUTION;
	    retType = true;
	}
	if (this.p2.x > width) {
	    double correction = this.p2.x - width;
	    this.p1.x -= correction;
	    this.p2.x -= correction;
	    this.vel.x = -this.vel.x / RESTITUTION;
	    retType = true;
	}
	if (this.p2.y > height) {
	    double correction = this.p2.y - height;
	    this.p1.y -= correction;
	    this.p2.y -= correction;
	    this.vel.y = -this.vel.y / RESTITUTION;
	    retType = true;
	}
	return retType;
    }

    @Override
    public double getMass() {
	return 0;
    }

    /**
     * Gets the width of the AABB 
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

    @Override
    public CollisionType getCollisionState(Entity2D entity) {
	if (entity instanceof AABB2D) {
	    return MathOperations.hasAABBCollision(this, (AABB2D) entity);
	}
	return CollisionType.NO_COLLISION;
    }

    @Override
    public void drawEntity(Graphics2D g) {
	if (this.getTexture() == null) {
	    Shape shape = this.getShape();
	    g.fill(shape);
	}
	else{
	    Paint originalPaint = g.getPaint();
	    TexturePaint texturePaint = new TexturePaint(GraphicsTools.bufferImage(AABBTexture), new Rectangle(0,0,GamePanel.TEXTURE_SIZE,GamePanel.TEXTURE_SIZE));
	    g.setPaint(texturePaint);
	    Shape poly = this.getShape();
	    g.fill(poly);
	    g.setPaint(originalPaint);
	    g.setColor(Color.DARK_GRAY);
	    g.draw(poly);
	}
    }

    @Override
    public Shape getShape() {
	return new Rectangle2D.Double(p1.x, p1.y, getWidth(), getHeight());
    }

    @Override
    public Vector2D[] getPointArray() {
	return new Vector2D[] { p1, p2 };
    }

    @Override
    public Vector2D getCenter() {
	return new Vector2D(p1.x + getWidth() / 2, p1.y + getHeight() / 2);
    }

    @Override
    public Entity2D clone() {
	return new AABB2D(p1.copy(), p2.copy(), this.vel.copy(), this.hasPhysics());
    }
}
