package physics.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;

import physics.util.CollisionType;
import physics.util.Vector;

/**
 * Represents a rectangle which is an entity, it doesn't actually have physics, but is collidable
 * 
 * @author Gordon Guan
 * @version Dec 2014
 * 
 *          TODO Fix rectangle collision code
 */
public class Rectangle extends Entity {
    public Vector p1;
    public Vector p2;
    public Vector p3;
    public Vector p4;

    public double angle;
    public double angularVel;
    public double angularAcc;

    public Rectangle(Vector p1, Vector p2, Vector p3, Vector p4, Vector vel, double angle, double angularVel, double angularAcc, Color c) {
	super(c, vel, false);
	this.p1 = p1;
	this.p2 = p2;
	this.p3 = p3;
	this.p4 = p4;
	this.vel = vel;

	this.angle = angle;
	this.angularVel = angularVel;
	this.angularAcc = angularAcc;
    }
    
    /**
     * Creates a rectangle with the least possible arguments
     * 
     * @param p1 The top left point
     * @param p2 The bottom right point
     * @param c The color
     */
    public Rectangle(Vector p1, Vector p2, Color c) {
	super(c, Vector.ZERO, false);
	this.p1 = p1;
	this.p4 = p2;
	this.p2 = new Vector(p2.x, p1.y);
	this.p3 = new Vector(p1.x, p2.y);

	this.angle = 0;
	this.angularVel = 0;
	this.angularVel = 0;
    }

    @Override
    public void drawEntity(Graphics2D g) {
	Shape poly = this.getShape();
	g.fill(poly);
    }

    @Override
    public Shape getShape() {
	int xPoly[] = { (int) p1.x, (int) p2.x, (int) p4.x, (int) p3.x };
	int yPoly[] = { (int) p1.y, (int) p2.y, (int) p4.y, (int) p3.y };

	Polygon poly = new Polygon(xPoly, yPoly, xPoly.length);
	return poly;
    }

    
    // public double getMass() {
    // // TODO Add mass code for rectangle
    // return 0;
    // }

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
    public CollisionType getCollisionState(Entity entity) {
	// if (entity instanceof Rectangle) {
	// Rectangle rect = (Rectangle) entity;
	// AABB thisBounds = this.getBoundingBox();
	// AABB otherBounds = rect.getBoundingBox();
	// // Do a preliminary bounding box overlap check to see if they collide, since this check is less CPU intensive
	// if (thisBounds.p2.x < otherBounds.p1.x || thisBounds.p1.x > otherBounds.p2.x || thisBounds.p2.y < otherBounds.p1.y || thisBounds.p1.y > otherBounds.p2.y)
	// return CollisionType.NO_COLLISION;
	//
	// // Do a more detailed check by seeing if any of the points in the other shape are in this one
	// for(Vector vector : rect.getPointArray()){
	// if(this.getShape().contains(vector.x, vector.y)){
	// System.out.println("YES");
	// return CollisionType.RECT_TO_RECT;
	// }
	// }
	// System.out.println("NO");
	// return CollisionType.NO_COLLISION;
	// }
	return CollisionType.NO_COLLISION;
    }

    @Override
    public Vector[] getPointArray() {
	return new Vector[] { p1, p2, p3, p4 };
    }

    @Override
    public double getMass() {
	return 0;
    }

    // /**
    // * Rotates the rectangle around the center
    // *
    // * @param angle The angle to rotate in degrees
    // */
    // public void rotate(double angle) {
    // this.angle += angle;
    // this.angle %= 360;
    // Vector center = this.getCenter();
    //
    // p1 = rotatePoint(p1, center, angle);
    // p2 = rotatePoint(p2, center, angle);
    // p3 = rotatePoint(p3, center, angle);
    // p4 = rotatePoint(p4, center, angle);
    // }
    //
    // /**
    // * Rotates the rectangle around a given center
    // *
    // * @param angle The angle to rotate in degrees
    // * @param center The center to rotate the rectangle around
    // */
    // public void rotate(double angle, Vector center) {
    // this.angle += angle;
    // this.angle %= 360;
    //
    // p1 = rotatePoint(p1, center, angle);
    // p2 = rotatePoint(p2, center, angle);
    // p3 = rotatePoint(p3, center, angle);
    // p4 = rotatePoint(p4, center, angle);
    // }
    //
    // /**
    // * Rotates a single vector around another a certain angle
    // *
    // * @param vector The vector to rotate
    // * @param center The center to rotate around
    // * @param angle The angle to rotate the vector
    // * @return the new rotated vector
    // */
    // private Vector rotatePoint(Vector vector, Vector center, double angle) {
    // double newX = vector.x - center.x;
    // double newY = vector.y - center.y;
    //
    // double radians = Math.toRadians(angle);
    // double sin = Math.sin(radians);
    // double cos = Math.cos(radians);
    //
    // double tempX = newX * cos - newY * sin;
    // double tempY = newX * sin + newY * cos;
    //
    // return new Vector(tempX + center.x, tempY + center.y);
    // }

    // /**
    // * Translates the entire rectangle
    // *
    // * @param x How much x to translate
    // * @param y How much y to translate
    // */
    // public void translate(double x, double y) {
    // this.p1.x += x;
    // this.p2.x += x;
    // this.p3.x += x;
    // this.p4.x += x;
    // this.p1.y += y;
    // this.p2.y += y;
    // this.p3.y += y;
    // this.p4.y += y;
    // }

    // /**
    // * Gets a bounding box around the rectangle
    // */
    // public AABB getBoundingBox() {
    // return new AABB(new Vector(Math.min(this.p1.x, Math.min(this.p2.x, Math.min(this.p3.x, this.p4.x))), Math.min(this.p1.y, Math.min(this.p2.y, Math.min(this.p3.y, this.p4.y)))), new Vector(
    // Math.max(this.p1.x, Math.max(this.p2.x, Math.max(this.p3.x, this.p4.x))), Math.max(this.p1.y, Math.max(this.p2.y, Math.max(this.p3.y, this.p4.y)))), Vector.ZERO, Color.BLACK);
    // }
    //
    // /**
    // * Gets the center of the rectangle
    // */
    // public Vector getCenter() {
    // return this.p1.midpoint(this.p4);
    // }
    //

    //
    // /**
    // * Gets the lowest point
    // *
    // * @return the lowest point
    // */
    // public Vector getLowestPoint() {
    // Vector lowestPoint = p1;
    // Vector[] points = getPointArray();
    // for (int vector = 1; vector < 4; vector++) {
    // Vector vec = points[vector];
    // if (vec.y > lowestPoint.y) {
    // lowestPoint = vec;
    // }
    // }
    // return lowestPoint;
    // }
    //
    // /**
    // * Gets the highest point
    // *
    // * @return the highest point
    // */
    // public Vector getHighestPoint() {
    // Vector lowestPoint = p1;
    // Vector[] points = getPointArray();
    // for (int vector = 1; vector < 4; vector++) {
    // Vector vec = points[vector];
    // if (vec.y > lowestPoint.y) {
    // lowestPoint = vec;
    // }
    // }
    // return lowestPoint;
    // }
    //
    // /**
    // * Gets the left-most point
    // *
    // * @return the left-most point
    // */
    // public Vector getLeftmostPoint() {
    // Vector lowestPoint = p1;
    // Vector[] points = getPointArray();
    // for (int vector = 1; vector < 4; vector++) {
    // Vector vec = points[vector];
    // if (vec.y < lowestPoint.x) {
    // lowestPoint = vec;
    // }
    // }
    // return lowestPoint;
    // }
    //
    // /**
    // * Gets the right-most point
    // *
    // * @return the right-most point
    // */
    // public Vector getRightmostPoint() {
    // Vector lowestPoint = p1;
    // Vector[] points = getPointArray();
    // for (int vector = 1; vector < 4; vector++) {
    // Vector vec = points[vector];
    // if (vec.y < lowestPoint.x) {
    // lowestPoint = vec;
    // }
    // }
    // return lowestPoint;
    // }
    //
    // /**
    // * Gets the ratio of the distance between a point on the rectangle and the center of mass
    // *
    // * @param vector The point on the rectangle
    // * @return the ratio of the distance between a point on the rectangle and the center of mass
    // */
    // public Vector getCenterDifferenceRatio(Vector vector) {
    // Vector difference = vector.subtract(this.getCenter());
    // return difference.divide(new Vector(this.getBoundingBox().getWidth(), this.getBoundingBox().getHeight()));
    //
    // }
}
