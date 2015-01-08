package physics.util;

/**
 * Represents a mathematical vector
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class Vector {
    public final static Vector ZERO = new Vector(0, 0);
    public double x;
    public double y;

    /**
     * Construct the vector with all components as 0.
     */
    public Vector() {
	this.x = 0;
	this.y = 0;
    }

    /**
     * Construct the vector with provided double components.
     * 
     * @param x X component
     * @param y Y component
     */
    public Vector(double x, double y) {
	this.x = x;
	this.y = y;
    }

    /**
     * Adds a vector to this one
     * 
     * @param vec The other vector
     * @return the same vector
     */
    public Vector add(Vector vec) {
	return new Vector(x + vec.x, y + vec.y);
    }

    /**
     * Subtracts a vector from this one.
     * 
     * @param vec The other vector
     * @return the same vector
     */
    public Vector subtract(Vector vec) {
	return new Vector(x - vec.x, y - vec.y);
    }

    /**
     * Multiplies the vector by another.
     * 
     * @param vec The other vector
     * @return the same vector
     */
    public Vector multiply(Vector vec) {
	return new Vector(x * vec.x, y * vec.y);
    }

    /**
     * Divides the vector by another.
     * 
     * @param vec The other vector
     * @return the same vector
     */
    public Vector divide(Vector vec) {
	return new Vector(x / vec.x, y / vec.y);
    }

    /**
     * Gets the magnitude of the vector, defined as sqrt(x^2+y^2). NaN will be returned if the inner result of the sqrt() function overflows, which will be caused if the length is too long.
     * 
     * @return the magnitude
     */
    public double length() {
	return Math.sqrt(x * x + y * y);
    }

    /**
     * Get the distance between this vector and another. NaN will be returned if the inner result of the sqrt() function overflows, which will be caused if the distance is too long.
     * 
     * @param o The other vector
     * @return the distance
     */
    public double distance(Vector o) {
	return Math.sqrt(MathOperations.square(x - o.x) + MathOperations.square(y - o.y));
    }

    /**
     * Gets the angle between this vector and another in radians.
     * 
     * @param other The other vector
     * @return angle in radians
     */
    public double angle(Vector other) {
	double dot = dotProduct(other) / (length() * other.length());
	return (double) Math.acos(dot);
    }

    /**
     * Sets this vector to the midpoint between this vector and another.
     * 
     * @param other The other vector
     * @return this same vector (now a midpoint)
     */
    public Vector midpoint(Vector other) {
	return new Vector((x + other.x) / 2, (y + other.y) / 2);
    }

    /**
     * Gets a new midpoint vector between this vector and another.
     * 
     * @param other The other vector
     * @return a new midpoint vector
     */
    public Vector getMidpoint(Vector other) {
	double x = (this.x + other.x) / 2;
	double y = (this.y + other.y) / 2;
	return new Vector(x, y);
    }

    /**
     * Performs scalar multiplication, multiplying all components with a scalar.
     * 
     * @param m The factor
     * @return the same vector
     */
    public Vector multiply(double m) {
	return new Vector(x * m, y * m);
    }

    /**
     * Calculates the dot product of this vector with another. The dot product is defined as x1*x2+y1*y2. The returned value is a scalar.
     * 
     * @param other The other vector
     * @return dot product
     */
    public double dotProduct(Vector other) {
	return x * other.x + y * other.y;
    }

    /**
     * Calculates the cross product of this vector with another.
     * 
     * @param o The other vector
     * @return the same vector
     */
    public Vector crossProduct(Vector o) {
	return new Vector(y * o.x - o.y * x, y * o.x - o.y * x);
    }

    /**
     * Converts this vector to a unit vector (a vector with length of 1).
     * 
     * @return the same vector
     */
    public void normalize() {
	double length = length();

	x /= length;
	y /= length;
    }

    /**
     * Zero this vector's components.
     * 
     * @return the same vector
     */
    public void zero() {
	x = 0;
	y = 0;
    }

    /**
     * Returns whether this vector is in an axis-aligned bounding box.
     * The minimum and maximum vectors given must be truly the minimum and maximum X and Y components.
     * 
     * @param min Minimum vector
     * @param max Maximum vector
     * @return whether this vector is in the AABB
     */
    public boolean isInAABB(Vector min, Vector max) {
	return x >= min.x && x <= max.x && y >= min.y && y <= max.y;
    }

    /**
     * Gets the minimum components of two vectors.
     * 
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return minimum
     */
    public static Vector getMinimum(Vector v1, Vector v2) {
	return new Vector(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y));
    }

    /**
     * Gets the maximum components of two vectors.
     * 
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return maximum
     */
    public static Vector getMaximum(Vector v1, Vector v2) {
	return new Vector(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y));
    }

    public String toString() {
	return x + " " + y;
    }
}