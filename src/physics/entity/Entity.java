package physics.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import physics.util.CollisionType;
import physics.util.Vector;

/**
 * Represents an entity
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public abstract class Entity {
    private static int lastID = 0;
    
    private final int entityID;
    private boolean handlingCollision;

    public Vector vel;

    private Color color;

    public Entity(Color c, Vector vel) {
	this.entityID = lastID;
	lastID++;
	this.color = c;
	this.handlingCollision = false;
	this.vel = vel;
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
     * Gets the entity's color
     * 
     * @return The entity's color
     */
    public Color getColor() {
	return color;
    }

    /**
     * Sets the entity's color
     * 
     * @param color The entity's color to set
     */
    public void setColor(Color color) {
	this.color = color;
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
    public abstract CollisionType getCollisionState(Entity entity);

    public String toString() {
	return String.format("%s r%dg%db%d id%d v%s a%s", this.getClass().getName(), color.getRed(), color.getGreen(), color.getBlue(), entityID, vel.toString());
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
}
