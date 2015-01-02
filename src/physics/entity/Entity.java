package physics.entity;

import java.awt.Color;

import physics.util.Vector;

/**
 * Represents an entity
 * @author Gordon Guan
 * @version Dec 2014
 */
public abstract class Entity
{
	private static int lastID = 0;
	
	private final EntityType type;
	private final int entityID;
	private boolean handlingCollision;

	public Vector vel;
	public Vector acc;
	
	private Color color;

	public Entity(EntityType type, Color c, Vector vel, Vector acc)
	{
		this.type = type;
		this.entityID = lastID;
		lastID++;
		this.color = c;
		this.handlingCollision = false;
		this.vel = vel;
		this.acc = acc;
	}

	/**
	 * Set the entity to not be handling any collisions
	 * @param handling Whether or not the entity is handing any collisions
	 */
	public void setHandling(boolean handling)
	{
		this.handlingCollision = handling;
	}

	/**
	 * Check if the entity is handling any collisions
	 * @return Whether or not the entity is handing any collisions
	 */
	public boolean isHandling()
	{
		return handlingCollision;
	}

	/**
	 * Gets the entity's color
	 * @return The entity's color
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Sets the entity's color
	 * @param color The entity's color to set
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * Gets the entity ID
	 * @return The entity ID
	 */
	public int getID()
	{
		return entityID;
	}

	/**
	 * Gets the entity type
	 * @return The entity type
	 */
	public EntityType getType()
	{
		return type;
	}
}
