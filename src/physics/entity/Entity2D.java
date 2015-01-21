package physics.entity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;

import physics.util.CollisionType;
import physics.util.Vector2D;

/**
 * Represents an entity
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public abstract class Entity2D implements Cloneable {
    private Image texture;
    private static int lastID = 0;

    private final int entityID;
    private boolean handlingCollision;

    public Vector2D vel;

    private boolean hasPhysics;

    /**
     * Constructs an entity
     * @param vel The velocity
     * @param texture The texture image
     */
    public Entity2D(Vector2D vel, Image texture) {
	this.entityID = lastID;
	lastID++;
	this.handlingCollision = false;
	this.vel = vel;
	hasPhysics = true;
	this.texture = texture;
    }

    /**
     * Constructs an entity
     * @param vel The velocity
     * @param hasPhysics Whether it has physics
     * @param texture The texture image
     */
    public Entity2D(Vector2D vel, boolean hasPhysics, Image texture) {
	this.entityID = lastID;
	lastID++;
	this.handlingCollision = false;
	this.vel = vel;
	this.hasPhysics = hasPhysics;
	this.texture = texture;
    }

    /**
     * Gets the center point of the entity
     * @return the center point of the entity as a Vector2D
     */
    public abstract Vector2D getCenter();
    /**
     * Gets an array of all the points in the shape
     * 
     * @return an array of all the points in the shape
     */
    public abstract Vector2D[] getPointArray();

    /**
     * Returns whether the entity has physics or not
     * 
     * @return whether the entity has physics or not
     */
    public boolean hasPhysics() {
	return hasPhysics;
    }

    /**
     * Set the entities physics
     * 
     * @param hasPhysics The new value for the entity's physics
     */
    public void setPhysics(boolean hasPhysics) {
	this.hasPhysics = hasPhysics;
    }

    /**
     * Set the entity to not be handling any collisions
     * 
     * @param handling Whether or not the entity is handing any collisions
     */
    public void setHandling(boolean handling) {
	this.handlingCollision = handling;
    }

    /**
     * Gets the "mass" of the entity
     * @return the mass of the entity
     */
    public abstract double getMass();

    /**
     * Check if the entity is handling any collisions
     * 
     * @return Whether or not the entity is handing any collisions
     */
    public boolean isHandling() {
	return handlingCollision;
    }

    /**
     * Gets the entity ID
     * 
     * @return The entity ID
     */
    public int getID() {
	return entityID;
    }

    /**
     * Checks whether this entity has collided with another one
     * 
     * @param entity The entity to check if this entity has collided with
     * @return Whether this entity has collided with the given one
     */
    public abstract CollisionType getCollisionState(Entity2D entity);
    
    public String toString() {
	return String.format("%s r%dg%db%d id%d v%s a%s", this.getClass().getName(), entityID, vel.toString());
    }

    /**
     * Draws the entity onto the given graphics
     * @param g The graphics to draw on
     */
    public abstract void drawEntity(Graphics2D g);

    /**
     * Gets a Graphics2D shape representing the entity
     * @return a Shape object representing the entity
     */
    public abstract Shape getShape();
    
    /**
     * Clones the entity, to make a completely new copy
     */
    public abstract Entity2D clone();
    
    /**
     * Checks for a wall collision, and recovers from it
     * @param width The width in pixels
     * @param height The height in pixels
     * @param RESTITUTION The bounciness of the objects
     * @return whether the collision happened
     */
    public abstract boolean handleWallCollision(int width, int height, final double RESTITUTION);

    /**
     * Gets this entity's textures
     * @return this entity's textures
     */
    public Image getTexture() {
        return texture;
    }

    /**
     * Sets this entity's textures
     * @param texture The new texture of this entity
     */
    public void setTexture(Image texture) {
        this.texture = texture;
    }
    
    /**
     * Moves the entity over by x and y
     * @param x The x to move it
     * @param y The y to move it
     */
    public abstract void translate(double x, double y);
    
}
