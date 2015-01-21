package game.physics;

import game.TrebuchetDemolition;
import game.level.Level;
import game.physics.entity.AABB2D;
import game.physics.entity.Circle2D;
import game.physics.entity.Entity2D;
import game.physics.entity.Projectile2D;
import game.physics.entity.Rectangle2D;
import game.physics.util.CollisionResolver;
import game.physics.util.CollisionType;
import game.physics.util.Vector2D;

import java.util.Properties;
import java.util.Vector;

/**
 * Contains the main code to emulate physics
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class PhysicsEngine
{
	// These constants are not related to real world constants
	public static final Vector2D GRAVITY_CONSTANT = new Vector2D(0, 0.1);
	/**
	 * Bounciness of an object
	 */
	private static final double RESTITUTION = 1.3;

	public boolean gravity = true;

	private int width;
	private int height;

	public int collisionsInTick = 0;

	private Vector<Entity2D> entities;

	private boolean won = false;

	/**
	 * Constructs a PhysicsEngine
	 * 
	 * @param width the width in pixels
	 * @param height the length in pixels
	 */
	public PhysicsEngine(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.entities = new Vector<Entity2D>();
	}

	/**
	 * Loads a level into the simulator
	 * 
	 * @param level The level to load
	 */
	public void loadLevel(Level level)
	{
		entities.clear();
		won = false;
		Properties metadata = level.getMetadata();

		try
		{
			if (metadata.getProperty("gravity") != null)
			{
				try
				{
					this.gravity = Boolean.valueOf(metadata
							.getProperty("gravity"));
				}
				catch (Exception e)
				{
					this.gravity = true;
				}
			}
			else
			{
				this.gravity = true;
			}
		}
		catch (Exception e)
		{
			TrebuchetDemolition.LOGGER
					.warning("Error while loading level properties: "
							+ e.getMessage());
		}

		// Clone it, since it will be cleared on load
		entities = (Vector<Entity2D>) level.getEntities().clone();
		TrebuchetDemolition.LOGGER.info("Loaded level \"" + level.getName()
				+ "\" into Physics Engine");
	}

	/**
	 * Makes the physics engine do a physics update
	 */
	public void update()
	{
		collisionsInTick = 0;
		this.dehandleAll();
		// Loop through every entities and do physics on them
		for (Entity2D e : entities)
		{
			boolean entityCollided = handleEntityCollisions(e);
			boolean wallCollided = false;
			if (e.hasPhysics())
				wallCollided = handleWallCollisions(e);

			// Keep track of how many collisions happened in this update()
			if (entityCollided || wallCollided)
				collisionsInTick++;
			if (gravity && e.hasPhysics())
			{
				e.vel = e.vel.subtract(GRAVITY_CONSTANT);
			}

			// Subtract the acceleration from velocities
			for (Vector2D vector : e.getPointArray())
			{
				vector.x -= e.vel.x;
				vector.y -= e.vel.y;
			}
		}

	}

	/**
	 * Checks if this entity is colliding with another one
	 * 
	 * @param entity The entity to check
	 * @return whether this entity is colliding with another one
	 */
	public synchronized boolean handleEntityCollisions(Entity2D entity)
	{
		boolean hasCollided = false;
		// Loop through every entity other than this one if this entity is not
		// hitting any other ones so far in this update()
		if (!entity.isHandling())
		{
			for (Entity2D e : entities)
			{
				if (e != entity)
				{
					// Check if the collision state isn't NO_COLLISION, then
					// resolve the collisions based on the type of collision
					// that has occured
					CollisionType colType = entity.getCollisionState(e);
					if (colType != CollisionType.NO_COLLISION)
					{
						entity.setHandling(true);
						e.setHandling(true);
						hasCollided = true;
						if (colType == CollisionType.CIRCLE_TO_CIRCLE)
						{
							CollisionResolver.resolveCircleCollision(
									(Circle2D) entity, (Circle2D) e,
									RESTITUTION);
						}
						else if (colType == CollisionType.AABB_TO_AABB)
						{

						}
						else if (colType == CollisionType.CIRCLE_TO_AABB)
						{
							CollisionResolver.resolveAABBCircleCollision(
									(Circle2D) entity, (AABB2D) e, RESTITUTION);

						}
						else if (colType == CollisionType.CIRCLE_TO_RECT)
						{
							CollisionResolver.resolveRectCircleCollision(
									(Circle2D) entity, (Rectangle2D) e,
									RESTITUTION);

						}
						else if (colType == CollisionType.WINNING_COLLISION)
						{
							won = true;
						}
					}
				}
			}
		}
		return hasCollided;
	}

	/**
	 * Checks for collisions with walls
	 * 
	 * @param entity The entity to check for wall collisions
	 * @return Whether or not the entity is colliding with a wall
	 */
	public synchronized boolean handleWallCollisions(Entity2D entity)
	{
		return entity.handleWallCollision(width, height, RESTITUTION);
	}

	/**
	 * Sets all entities as not handling
	 */
	public void dehandleAll()
	{
		for (Entity2D e : entities)
		{
			e.setHandling(false);
		}
	}

	/**
	 * Adds the entity to the physics engine
	 * 
	 * @param entity The entity to add
	 */
	public void addEntity(Entity2D entity)
	{
		entities.add(entity);
	}

	/**
	 * Removes the entity from the physics engine
	 * 
	 * @param entity The entity to remove
	 */
	public void removeEntity(Entity2D entity)
	{
		entities.remove(entity);
	}

	/**
	 * Removes the last fired projectile
	 */
	public void removeLastProjectile()
	{
		try
		{
			for (Entity2D entity : entities)
			{

				if (entity instanceof Projectile2D)
				{
					removeEntity(entity);
				}

			}
		}
		catch (Throwable e)
		{

		}

	}

	/**
	 * Gets all the entities in the engine
	 * 
	 * @return All the entities in the engine
	 */
	public Vector<Entity2D> getEntities()
	{
		return entities;
	}

	/**
	 * Destroys all entities in the engine
	 */
	public void clearAll()
	{
		entities.clear();
	}

	/**
	 * Check if the player has won
	 * 
	 * @return whether the player has won
	 */
	public boolean hasWon()
	{
		return won;
	}

	/**
	 * Sets if the player has won
	 * 
	 * @param b Whether the player has won
	 */
	public void setWon(boolean b)
	{
		won = b;
	}

	/**
	 * Fires a projectile
	 * 
	 * @param power The power to fire at
	 * @param angle The angle
	 */
	public void fireProjectile(int power, int angle)
	{
		double vecX = Math.cos(Math.toRadians(180 - angle)) * power / 9;
		double vecY = Math.sin(Math.toRadians(180 - angle)) * power / 9;
		Vector2D vel = new Vector2D(vecX, vecY);

		Projectile2D c = new Projectile2D(new Vector2D(50, height - 50), vel,
				10);
		this.addEntity(c);
	}
}
