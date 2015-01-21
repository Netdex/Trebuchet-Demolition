package game.physics.entity;

import game.GamePanel;
import game.graphics.GraphicsTools;
import game.physics.util.CollisionType;
import game.physics.util.MathOperations;
import game.physics.util.Vector2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Ellipse2D;

/**
 * Represents a Circle which is an entity
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class Circle2D extends Entity2D
{
	private static Image circleTexture = GamePanel.metalTexture;
	private int radius;
	public Vector2D loc;

	/**
	 * Creates a circle
	 * 
	 * @param loc The center of the circle
	 * @param vel The velocity of the circle
	 * @param radius The radius of the circle
	 */
	public Circle2D(Vector2D loc, Vector2D vel, int radius)
	{
		super(vel, circleTexture);
		this.loc = loc;
		this.radius = radius;
	}

	@Override
	public Entity2D clone()
	{
		return new Circle2D(loc.copy(), this.vel.copy(), radius);
	}

	@Override
	public void translate(double x, double y)
	{
		for (Vector2D vector : this.getPointArray())
		{
			vector.x += x;
			vector.y += y;
		}
	}

	/**
	 * Gets the radius of the circle
	 * 
	 * @return The radius of the circle
	 */
	public int getRadius()
	{
		return radius;
	}

	@Override
	public boolean handleWallCollision(int width, int height,
			final double RESTITUTION)
	{
		int radius = this.getRadius();
		double x = this.loc.x;
		double y = this.loc.y;

		// Make sure the circle collides with walls
		boolean hasCollided = false;
		if (x - radius < 0)
		{
			this.loc.x = radius;
			this.vel.x = -this.vel.x / RESTITUTION;
			hasCollided = true;
		}
		if (y - radius < 0)
		{
			this.loc.y = radius;
			this.vel.y = -this.vel.y / RESTITUTION;
			hasCollided = true;
		}
		if (x + radius > width)
		{
			this.loc.x = width - radius;
			this.vel.x = -this.vel.x / RESTITUTION;
			hasCollided = true;
		}
		if (y + radius > height)
		{
			this.loc.y = height - radius;
			this.vel.y = -this.vel.y / RESTITUTION;
			hasCollided = true;
		}
		return hasCollided;
	}

	@Override
	public CollisionType getCollisionState(Entity2D entity)
	{
		// Check for this circle to another
		if (entity instanceof Circle2D)
		{
			Circle2D circle = (Circle2D) entity;
			int otherRadius = circle.getRadius();
			int totalRadius = radius + otherRadius;
			double ax = loc.x;
			double ay = loc.y;
			double bx = circle.loc.x;
			double by = circle.loc.y;
			totalRadius *= totalRadius;
			double dist = (ax - bx) * (ax - bx) + (ay - by) * (ay - by);
			if (totalRadius > dist)
			{
				return CollisionType.CIRCLE_TO_CIRCLE;
			}
			return CollisionType.NO_COLLISION;
		}
		// Check for this circle to a rectangle
		else if (entity instanceof Rectangle2D)
		{
			Rectangle2D rect = (Rectangle2D) entity;
			Vector2D center = this.loc;
			double dist1 = MathOperations.pointToLineSegDistance(rect.p1,
					rect.p2, center);
			double dist2 = MathOperations.pointToLineSegDistance(rect.p2,
					rect.p4, center);
			double dist3 = MathOperations.pointToLineSegDistance(rect.p4,
					rect.p3, center);
			double dist4 = MathOperations.pointToLineSegDistance(rect.p3,
					rect.p1, center);
			double min = Math.min(dist1,
					Math.min(dist2, Math.min(dist3, dist4)));

			if (min < radius || rect.getShape().contains(center.x, center.y))
			{
				return CollisionType.CIRCLE_TO_RECT;
			}
			return CollisionType.NO_COLLISION;
		}
		// Check for this circle to an AABB, or a Target
		else if (entity instanceof AABB2D)
		{
			AABB2D aabb = (AABB2D) entity;
			AABB2D bounds = this.getBoundingBox();

			CollisionType doesCollide = MathOperations.hasAABBCollision(aabb,
					bounds);
			if (doesCollide == CollisionType.AABB_TO_AABB)
			{
				return CollisionType.CIRCLE_TO_AABB;
			}
			return CollisionType.NO_COLLISION;
		}
		return CollisionType.NO_COLLISION;
	}

	@Override
	public double getMass()
	{
		return radius;
	}

	@Override
	public void drawEntity(Graphics2D g)
	{
		if (this.getTexture() == null)
		{
			Shape shape = this.getShape();
			g.fill(shape);
		}
		else
		{
			// Draws the texture on the circle
			Paint originalPaint = g.getPaint();
			TexturePaint texturePaint = new TexturePaint(
					GraphicsTools.bufferImage(circleTexture), new Rectangle(0,
							0, GamePanel.TEXTURE_SIZE, GamePanel.TEXTURE_SIZE));
			g.setPaint(texturePaint);
			Shape poly = this.getShape();
			g.fill(poly);
			g.setPaint(originalPaint);
			g.setColor(Color.DARK_GRAY);
			g.draw(poly);
		}
	}

	@Override
	public Shape getShape()
	{
		return new Ellipse2D.Double(loc.x - radius, loc.y - radius, radius * 2,
				radius * 2);
	}

	@Override
	public Vector2D[] getPointArray()
	{
		return new Vector2D[] { loc };
	}

	@Override
	public Vector2D getCenter()
	{
		return loc.copy();
	}

	/**
	 * Gets the bounds around this circle
	 * 
	 * @return the bounds around this circle as an AABB
	 */
	public AABB2D getBoundingBox()
	{
		return new AABB2D(new Vector2D(loc.x - radius, loc.y - radius),
				new Vector2D(loc.x + radius, loc.y + radius), Vector2D.ZERO);
	}
}
