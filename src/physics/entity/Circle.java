package physics.entity;

import java.awt.Color;

import physics.util.Vector;

/**
 * Represents a Circle which is an entity
 * @author Gordon Guan
 * @version Dec 2014
 */
public class Circle extends Entity
{
	private int radius;
	public Vector loc;
	
	public Circle(Vector loc, Vector vel, Vector acc, int radius, Color c)
	{
		super(EntityType.CIRCLE, c, vel, acc);
		this.loc = loc;
		this.radius = radius;
	}
	
	/**
	 * Gets the radius of the circle
	 * @return The radius of the circle
	 */
	public int getRadius(){
		return radius;
	}

}
