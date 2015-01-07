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
 * TODO Fix rectangle collision and rotation code
 */
public class Rectangle extends Entity {
    public Vector p1;
    public Vector p2;
    public Vector p3;
    public Vector p4;
    
    public double angle;
    public double angularVel;
    public double angularAcc;

    public Rectangle(Vector p1, Vector p2, Vector p3, Vector p4, Vector vel, Vector acc, double angle, double angularVel, double angularAcc, Color c) {
	super(EntityType.RECTANGLE, c, vel, acc);
	this.p1 = p1;
	this.p2 = p2;

	this.angle = angle;
	this.angularVel = angularVel;
	this.angularAcc = angularAcc;
    }
    
    /**
     * Creates a rectangle with the least possible arguments
     * @param p1 The top left point
     * @param p2 The bottom right point
     * @param c The color
     */
    public Rectangle(Vector p1, Vector p2, Color c){
	super(EntityType.RECTANGLE, c, Vector.ZERO, Vector.ZERO);
	this.p1 = p1;
	this.p4 = p2;
	this.p2 = new Vector(p2.x, p1.y);
	this.p3 = new Vector(p1.x, p2.y);
	
	this.angle = 0;
	this.angularVel = 0;
	this.angularVel = 0;
    }

    public double getMass(){
	// TODO Add mass code for rectangle
	return 0;
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
     * TODO Fix math on this
     */
    public double getHeight() {
	return Math.abs(p1.y - p2.y);
    }

    /**
     * Rotates the rectangle around the center
     * @param angle the angle to rotate in degrees
     * TODO Fix rotation code
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
     * TODO Fix vector rotation around point
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
    
    /**
     * Translates the entire rectangle
     * @param x How much x to translate
     * @param y How much y to translate
     */
    public void translate(double x, double y){
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
     * Gets a bounding box around the rectangle
     */
    public AABB getBoundingBox(){
	return new AABB(new Vector(Math.min(this.p1.x, Math.min(this.p2.x, Math.min(this.p3.x, this.p4.x))), Math.min(this.p1.y, Math.min(this.p2.y, Math.min(this.p3.y, this.p4.y)))), new Vector(Math.max(this.p1.x, Math.max(this.p2.x, Math.max(this.p3.x, this.p4.x))), Math.max(this.p1.y, Math.max(this.p2.y, Math.max(this.p3.y, this.p4.y)))), Vector.ZERO, Vector.ZERO, Color.BLACK);
    }
}
