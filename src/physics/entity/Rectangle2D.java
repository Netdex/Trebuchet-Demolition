package physics.entity;

import game.GamePanel;
import game.graphics.GraphicsTools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.TexturePaint;

import physics.util.CollisionType;
import physics.util.Vector2D;

/**
 * Represents a rectangle which is an entity, it doesn't actually have physics, but is collidable
 * 
 * @author Gordon Guan
 * @version Dec 2014
 * 
 */
public class Rectangle2D extends Entity2D {

    private static Image rectTexture = GamePanel.metalTexture;

    public Vector2D p1;
    public Vector2D p2;
    public Vector2D p3;
    public Vector2D p4;

    public double angle;
    public double angularVel;

    public Rectangle2D(Vector2D p1, Vector2D p2, Vector2D p3, Vector2D p4, Vector2D vel, double angle, double angularVel, Color c) {
	super(c, vel, false, rectTexture);
	this.p1 = p1;
	this.p2 = p2;
	this.p3 = p3;
	this.p4 = p4;
	this.vel = vel;

	this.rotate(angle);
	this.angularVel = angularVel;
    }

    @Override
    public boolean handleWallCollision(int width, int height, final double RESTITUTION) {
	return false;
    }

    @Override
    public Entity2D clone() {
	return new Rectangle2D(p1.copy(), p2.copy(), p3.copy(), p4.copy(), this.vel.copy(), this.angle, angularVel, this.getColor());
    }

    /**
     * Creates a rectangle with the least possible arguments
     * 
     * @param p1 The top left point
     * @param p2 The bottom right point
     * @param c The color
     */
    public Rectangle2D(Vector2D p1, Vector2D p2, Color c) {
	super(c, Vector2D.ZERO, false, rectTexture);
	this.p1 = p1;
	this.p4 = p2;
	this.p2 = new Vector2D(p2.x, p1.y);
	this.p3 = new Vector2D(p1.x, p2.y);

	this.angle = 0;
	this.angularVel = 0;
	this.angularVel = 0;
    }

    @Override
    public void drawEntity(Graphics2D g) {
	if (this.getTexture() == null) {
	    Shape poly = this.getShape();
	    g.fill(poly);
	} else {
	    Paint originalPaint = g.getPaint();
	    TexturePaint texturePaint = new TexturePaint(GraphicsTools.bufferImage(rectTexture), this.getShape().getBounds2D());
	    g.setPaint(texturePaint);
	    Shape poly = this.getShape();
	    g.fill(poly);
	    g.setPaint(originalPaint);
	    g.setColor(this.getColor());
	    g.draw(poly);
	    
	}
    }

    @Override
    public Shape getShape() {
	int xPoly[] = { (int) p1.x, (int) p2.x, (int) p4.x, (int) p3.x };
	int yPoly[] = { (int) p1.y, (int) p2.y, (int) p4.y, (int) p3.y };

	Polygon poly = new Polygon(xPoly, yPoly, xPoly.length);
	return poly;
    }

    /**
     * Gets the width of the Rectangle
     * 
     * @return The width of the Rectangle
     */
    public double getWidth() {
	return p1.distance(p2);
    }

    /**
     * Gets the height of the Rectangle
     * 
     * @return The height of the Rectangle
     * 
     */
    public double getHeight() {
	return p1.distance(p3);
    }

    /**
     * TODO Add collision code
     */
    public CollisionType getCollisionState(Entity2D entity) {
	return CollisionType.NO_COLLISION;
    }

    @Override
    public Vector2D[] getPointArray() {
	return new Vector2D[] { p1, p2, p3, p4 };
    }

    @Override
    public double getMass() {
	return 0;
    }

    /**
     * Gets a bounding box around the rectangle
     */
    public AABB2D getBoundingBox() {
	return new AABB2D(new Vector2D(Math.min(this.p1.x, Math.min(this.p2.x, Math.min(this.p3.x, this.p4.x))), Math.min(this.p1.y, Math.min(this.p2.y, Math.min(this.p3.y, this.p4.y)))),
		new Vector2D(Math.max(this.p1.x, Math.max(this.p2.x, Math.max(this.p3.x, this.p4.x))), Math.max(this.p1.y, Math.max(this.p2.y, Math.max(this.p3.y, this.p4.y)))), Vector2D.ZERO,
		Color.BLACK);
    }

    @Override
    public Vector2D getCenter() {
	return this.p1.midpoint(this.p4);
    }

    /**
     * Rotates the rectangle around the center
     * 
     * @param angle The angle to rotate in degrees
     */
    public void rotate(double angle) {
	this.angle += angle;
	this.angle %= 360;
	Vector2D center = this.getCenter();

	p1 = rotatePoint(p1, center, angle);
	p2 = rotatePoint(p2, center, angle);
	p3 = rotatePoint(p3, center, angle);
	p4 = rotatePoint(p4, center, angle);
    }

    /**
     * Rotates the rectangle around a given center
     * 
     * @param angle The angle to rotate in degrees
     * @param center The center to rotate the rectangle around
     */
    public void rotate(double angle, Vector2D center) {
	this.angle += angle;
	this.angle %= 360;

	p1 = rotatePoint(p1, center, angle);
	p2 = rotatePoint(p2, center, angle);
	p3 = rotatePoint(p3, center, angle);
	p4 = rotatePoint(p4, center, angle);
    }

    /**
     * Rotates a single vector around another a certain angle
     * 
     * @param vector The vector to rotate
     * @param center The center to rotate around
     * @param angle The angle to rotate the vector
     * @return the new rotated vector
     */
    private Vector2D rotatePoint(Vector2D vector, Vector2D center, double angle) {
	double newX = vector.x - center.x;
	double newY = vector.y - center.y;

	double radians = Math.toRadians(angle);
	double sin = Math.sin(radians);
	double cos = Math.cos(radians);

	double tempX = newX * cos - newY * sin;
	double tempY = newX * sin + newY * cos;

	return new Vector2D(tempX + center.x, tempY + center.y);
    }

    /**
     * Translates the entire rectangle
     * 
     * @param x How much x to translate
     * @param y How much y to translate
     */
    public void translate(double x, double y) {
	this.p1.x += x;
	this.p2.x += x;
	this.p3.x += x;
	this.p4.x += x;
	this.p1.y += y;
	this.p2.y += y;
	this.p3.y += y;
	this.p4.y += y;
    }

    /**
     * Gets the lowest point
     * 
     * @return the lowest point
     */
    public Vector2D getLowestPoint() {
	Vector2D lowestPoint = p1;
	Vector2D[] points = getPointArray();
	for (int vector = 1; vector < 4; vector++) {
	    Vector2D vec = points[vector];
	    if (vec.y > lowestPoint.y) {
		lowestPoint = vec;
	    }
	}
	return lowestPoint;
    }

    /**
     * Gets the highest point
     * 
     * @return the highest point
     */
    public Vector2D getHighestPoint() {
	Vector2D lowestPoint = p1;
	Vector2D[] points = getPointArray();
	for (int vector = 1; vector < 4; vector++) {
	    Vector2D vec = points[vector];
	    if (vec.y > lowestPoint.y) {
		lowestPoint = vec;
	    }
	}
	return lowestPoint;
    }

    /**
     * Gets the left-most point
     * 
     * @return the left-most point
     */
    public Vector2D getLeftmostPoint() {
	Vector2D lowestPoint = p1;
	Vector2D[] points = getPointArray();
	for (int vector = 1; vector < 4; vector++) {
	    Vector2D vec = points[vector];
	    if (vec.y < lowestPoint.x) {
		lowestPoint = vec;
	    }
	}
	return lowestPoint;
    }

    /**
     * Gets the right-most point
     * 
     * @return the right-most point
     */
    public Vector2D getRightmostPoint() {
	Vector2D lowestPoint = p1;
	Vector2D[] points = getPointArray();
	for (int vector = 1; vector < 4; vector++) {
	    Vector2D vec = points[vector];
	    if (vec.y < lowestPoint.x) {
		lowestPoint = vec;
	    }
	}
	return lowestPoint;
    }

}
