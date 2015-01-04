package physics.entity;

import java.awt.Color;

import physics.util.CollisionType;
import physics.util.Vector;

/**
 * Represents a rectangle which is an entity
 * 
 * @author Gordon Guan
 * @version Dec 2014
 * 
 * TODO There are major problems with this
 */
public class Rectangle extends Entity {
    public Vector p1;
    public Vector p2;

    public double angle;
    public double angularVel;
    public double angularAcc;

    public Rectangle(Vector p1, Vector p2, Vector vel, Vector acc, double angle, double angularVel, double angularAcc, Color c) {
	super(EntityType.RECTANGLE, c, vel, acc);
	this.p1 = p1;
	this.p2 = p2;

	this.angle = angle;
	this.angularVel = angularVel;
	this.angularAcc = angularAcc;
	// rotate(angle);
	Rectangle rect = this;
	System.out.println(rect.p1.x + " " + rect.p1.y + " " + rect.p2.x + " " + rect.p2.y);
    }

    /**
     * Gets the width of the Rectangle
     * 
     * @return The width of the Rectangle
     */
    public double getWidth() {
	return Math.abs(p1.x - p2.x);
    }

    /**
     * Gets the height of the Rectangle
     * 
     * @return The height of the Rectangle
     * 
     * TODO there are problems with this
     */
    public double getHeight() {
	return Math.abs(p1.y - p2.y);
    }

    /**
     * Rotates the rectangle around the center
     * @param angle the angle to rotate in degrees
     * TODO there are major problems with this
     */
    public void rotate(double angle) {
	this.angle += angle;
	Vector center = new Vector((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);

	p1 = rotatePoint(p1, center, angle);
	p2 = rotatePoint(p2, center, angle);
    }
    /**
     * Rotates a single vector around another a certain angle
     * @param vector The vector to rotate
     * @param center The center to rotate around
     * @param angle The angle to rotate the vector
     * @return the new rotated vector
     * TODO fix this too
     */

    private Vector rotatePoint(Vector vector, Vector center, double angle) {
	Vector translated = new Vector(vector.x - center.x, vector.y - center.y);
	double sin = Math.sin(Math.toRadians(angle));
	double cos = Math.cos(Math.toRadians(angle));

	return new Vector(translated.x * cos - translated.y * sin, translated.x * sin - translated.y * cos);
    }

    /**
     * TODO Add collision code
     */
    public CollisionType getCollisionState(Entity entity) {
	return CollisionType.NO_COLLISION;
    }
}
