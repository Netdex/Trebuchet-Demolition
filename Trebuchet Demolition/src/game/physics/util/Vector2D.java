package game.physics.util;

/**
 * Represents a mathematical vector
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class Vector2D {
    public final static Vector2D ZERO = new Vector2D(0, 0);
    public double x;
    public double y;

    /**
     * Construct the vector with all components as 0
     */
    public Vector2D() {
	this.x = 0;
	this.y = 0;
    }

    /**
     * Construct the vector with provided double components
     * 
     * @param x X component
     * @param y Y component
     */
    public Vector2D(double x, double y) {
	this.x = x;
	this.y = y;
    }

    /**
     * Adds a vector to this one
     * 
     * @param vector The other vector
     * @return the sum of the vectors
     */
    public Vector2D add(Vector2D vector) {
	return new Vector2D(x + vector.x, y + vector.y);
    }

    /**
     * Subtracts a vector from this one
     * 
     * @param vector The other vector
     * @return the difference of the vectors
     */
    public Vector2D subtract(Vector2D vector) {
	return new Vector2D(x - vector.x, y - vector.y);
    }

    /**
     * Multiplies the vector by another
     * 
     * @param vector The other vector
     * @return the product of the vectors
     */
    public Vector2D multiply(Vector2D vector) {
	return new Vector2D(x * vector.x, y * vector.y);
    }

    /**
     * Divides the vector by another.
     * 
     * @param vector The other vector
     * @return the dividend of the vectors
     */
    public Vector2D divide(Vector2D vector) {
	return new Vector2D(x / vector.x, y / vector.y);
    }

    /**
     * Gets the magnitude of the vector, defined as sqrt(x^2+y^2)
     * 
     * @return the magnitude of the vector
     */
    public double length() {
	return Math.sqrt(x * x + y * y);
    }

    /**
     * Get the distance between this vector and another
     * 
     * @param otherVector The other vector
     * @return the distance between the vectors
     */
    public double distance(Vector2D otherVector) {
	return Math.sqrt(MathOperations.square(x - otherVector.x) + MathOperations.square(y - otherVector.y));
    }

    /**
     * Gets the angle between this vector and another in radians
     * 
     * @param otherVector The other vector
     * @return angle in radians
     */
    public double angle(Vector2D otherVector) {
	double dot = dotProduct(otherVector) / (length() * otherVector.length());
	return (double) Math.acos(dot);
    }

    /**
     * Gets a new midpoint vector between this vector and another
     * 
     * @param otherVector The other vector
     * @return a new midpoint vector
     */
    public Vector2D getMidpoint(Vector2D otherVector) {
	double x = (this.x + otherVector.x) / 2;
	double y = (this.y + otherVector.y) / 2;
	return new Vector2D(x, y);
    }

    /**
     * Performs scalar multiplication, multiplying all components with a scalar
     * 
     * @param scalar The factor
     * @return the new multiplied vetor
     */
    public Vector2D multiply(double scalar) {
	return new Vector2D(x * scalar, y * scalar);
    }

    /**
     * Calculates the dot product of this vector with another
     * The dot product is defined as x1*x2+y1*y2
     * The returned value is a scalar
     * 
     * @param otherVector The other vector
     * @return dot product
     */
    public double dotProduct(Vector2D otherVector) {
	return x * otherVector.x + y * otherVector.y;
    }

    /**
     * Calculates the cross product of this vector with another.
     * 
     * @param otherVector The other vector
     * @return the cross product of the vectors
     */
    public Vector2D crossProduct(Vector2D otherVector) {
	return new Vector2D(y * otherVector.x - otherVector.y * x, y * otherVector.x - otherVector.y * x);
    }

    /**
     * Converts this vector to a unit vector (a vector with length of 1)
     */
    public void normalize() {
	double length = length();

	x /= length;
	y /= length;
    }
    
    /**
     * Gets the unit vector of this vector (a vector with length of 1)
     * 
     * @return the normalized vector
     */
    public Vector2D getNormalized() {
	double length = length();

	return new Vector2D(x / length, y / length);
    }

    /**
     * Zero this vector's components
     * 
     */
    public void zero() {
	x = 0;
	y = 0;
    }

    /**
     * Returns whether this vector is in an axis-aligned bounding box
     * The minimum and maximum vectors given must be truly the minimum and maximum X and Y components
     * 
     * @param min Minimum vector
     * @param max Maximum vector
     * @return whether this vector is in the AABB
     */
    public boolean isInAABB(Vector2D min, Vector2D max) {
	return x >= min.x && x <= max.x && y >= min.y && y <= max.y;
    }

    /**
     * Gets the minimum components of two vectors.
     * 
     * @param v1 The first vector
     * @param v2 The second vector
     * @return minimum
     */
    public static Vector2D getMinimum(Vector2D v1, Vector2D v2) {
	return new Vector2D(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y));
    }

    /**
     * Gets the maximum components of two vectors.
     * 
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return maximum
     */
    public static Vector2D getMaximum(Vector2D v1, Vector2D v2) {
	return new Vector2D(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y));
    }

    public String toString() {
	return String.format("vec{x=%.2f,y=%.2f}", x, y);
    }

    /**
     * Divides a vector by a scalar
     * 
     * @param scalar The scalar to divide by
     * @return a new vector divided by the scalar
     */
    public Vector2D divide(double scalar) {
	return new Vector2D(x / scalar, y / scalar);
    }

    public Vector2D copy() {
	return new Vector2D(x, y);
    }

    /**
     * Finds the vector of the reflected ray
     * 
     * @param normal the normal vector
     * @return the incident vector reflected over the normal
     */
    public Vector2D reflect(Vector2D normal) {
	return this.subtract(normal.getNormalized().multiply(2 * this.dotProduct(normal.getNormalized())));
    }
}