package physics.entity;

import java.awt.Color;

import physics.util.Vector;

/**
 * Represents an Axis Aligned Bounding Box (AABB) which is an entity;
 * a rectangle without rotation
 * @author Gordon Guan
 * @version Dec 2014
 */
public class AABB extends Entity
{
	public Vector p1;
	public Vector p2;
	
	public AABB(Vector p1, Vector p2, Vector vel, Vector acc, Color c)
	{
		super(EntityType.AABB, c, vel, acc);
		this.p1 = p1;
		this.p2 = p2;
	}

	/**
	 * Gets the width of the AABB
	 * @return The width of the AABB
	 */
	public double getWidth()
	{
		return Math.abs(p1.x - p2.x);
	}

	/**
	 * Gets the height of the AABB
	 * @return The height of the AABB
	 */
	public double getHeight()
	{
		return Math.abs(p1.y - p2.y);
	}
}
