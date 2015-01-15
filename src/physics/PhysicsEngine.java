package physics;

import game.level.Level;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Properties;
import java.util.Vector;

import physics.entity.AABB;
import physics.entity.Circle;
import physics.entity.Entity;
import physics.util.CollisionResolver;
import physics.util.CollisionType;
import physics.util.Vector2D;

/**
 * Contains the main code to emulate physics
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class PhysicsEngine {
    // These constants are not related to real world constants
    public static final Vector2D GRAVITY_CONSTANT = new Vector2D(0, 0.1);
    private static final double FRICTION = 1.05;
    private static final double RESTITUTION = 1.5;

    public boolean gravity = true;

    private int width;
    private int height;

    public int collisionsInTick = 0;

    private Vector<Entity> entities;

    private boolean won = false;

    public PhysicsEngine(int width, int height) {
	this.width = width;
	this.height = height;
	this.entities = new Vector<Entity>();
    }

    /**
     * Loads a level into the simulator
     * 
     * @param level The level to load
     */
    @SuppressWarnings("unchecked")
    public void loadLevel(Level level) {
	entities.clear();
	won = false;
	Properties metadata = level.getMetadata();
	Vector<Entity> levelEntities = level.getEntities();

	try {
	    if (metadata.getProperty("gravity") != null) {
		try {
		    this.gravity = Boolean.valueOf(metadata.getProperty("gravity"));
		} catch (Exception e) {

		}
	    } else {
		this.gravity = true;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}

	entities = (Vector) levelEntities.clone();
    }

    /**
     * Makes the physics engine do a physics update
     */
    public void update() {
	collisionsInTick = 0;
	for (Entity e : entities) {
	    e.setHandling(false);
	}
	for (Entity e : entities) {
	    boolean entityCollided = handleEntityCollisions(e);
	    boolean wallCollided = handleWallCollisions(e);
	    if (entityCollided || wallCollided)
		collisionsInTick++;
	    if (gravity && e.hasPhysics()) {
		e.vel = e.vel.subtract(GRAVITY_CONSTANT);
	    }

	    // Subtract the acceleration from velocities
	    for (Vector2D vector : e.getPointArray()) {
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
    public synchronized boolean handleEntityCollisions(Entity entity) {
	boolean hasCollided = false;
	if (!entity.isHandling()) {
	    for (Entity e : entities) {
		if (e != entity) {
		    CollisionType colType = entity.getCollisionState(e);
		    if (colType == CollisionType.CIRCLE_TO_CIRCLE) {
			CollisionResolver.resolveCircleCollision((Circle) entity, (Circle) e, RESTITUTION);
			entity.setHandling(true);
			e.setHandling(true);
			hasCollided = true;
		    } else if (colType == CollisionType.AABB_TO_AABB) {
			entity.setHandling(true);
			e.setHandling(true);
			hasCollided = true;
		    } else if (colType == CollisionType.CIRCLE_TO_AABB) {
			CollisionResolver.resolveAABBCircleCollision((Circle) entity, (AABB) e, RESTITUTION);
			entity.setHandling(true);
			e.setHandling(true);
			hasCollided = true;
		    } else if (colType == CollisionType.WINNING_COLLISION) {
			won = true;
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
    public synchronized boolean handleWallCollisions(Entity entity) {
	// Check for circle based collisions
	if (entity instanceof Circle) {
	    Circle circle = (Circle) entity;
	    int radius = circle.getRadius();
	    double x = circle.loc.x;
	    double y = circle.loc.y;

	    // Make sure the circle collides with walls
	    boolean retType = false;
	    if (x - radius < 0) {
		circle.loc.x = radius;
		circle.vel.x = -circle.vel.x / RESTITUTION;
		retType = true;
	    }
	    if (y - radius < 0) {
		circle.loc.y = radius;
		circle.vel.y = -circle.vel.y / RESTITUTION;
		retType = true;
	    }
	    if (x + radius > width) {
		circle.loc.x = width - radius;
		circle.vel.x = -circle.vel.x / RESTITUTION;
		retType = true;
	    }
	    if (y + radius > height) {
		circle.loc.y = height - radius;
		circle.vel.y = -circle.vel.y / RESTITUTION;
		circle.vel.x = circle.vel.x / FRICTION;
		retType = true;
	    }
	    if (retType)
		return true;
	}
	// Check for Axis Aligned Bounding Box collisions
	else if (entity instanceof AABB) {
	    AABB aabb = (AABB) entity;

	    boolean retType = false;
	    if (aabb.p1.x < 0) {
		double correction = Math.abs(aabb.p1.x);
		aabb.p1.x += correction;
		aabb.p2.x += correction;
		aabb.vel.x = -aabb.vel.x / RESTITUTION;
		retType = true;
	    }
	    if (aabb.p1.y < 0) {
		double correction = Math.abs(aabb.p1.y);
		aabb.p1.y += correction;
		aabb.p2.y += correction;
		aabb.vel.y = -aabb.vel.y / RESTITUTION;
		retType = true;
	    }
	    if (aabb.p2.x > width) {
		double correction = aabb.p2.x - width;
		aabb.p1.x -= correction;
		aabb.p2.x -= correction;
		aabb.vel.x = -aabb.vel.x / RESTITUTION;
		retType = true;
	    }
	    if (aabb.p2.y > height) {
		double correction = aabb.p2.y - height;
		aabb.p1.y -= correction;
		aabb.p2.y -= correction;
		aabb.vel.y = -aabb.vel.y / RESTITUTION;
		aabb.vel.x = aabb.vel.x / FRICTION;
		retType = true;
	    }
	    if (retType)
		return true;

	}
	return false;

    }

    /**
     * Adds the entity to the physics engine
     * 
     * @param entity The entity to add
     */
    public void addEntity(Entity entity) {
	entities.add(entity);
    }

    /**
     * Removes the entity from the physics engine
     * 
     * @param entity The entity to remove
     */
    public void removeEntity(Entity entity) {
	entities.remove(entity);
    }

    public void removeProjectiles() {

	for (Entity entity : entities) {
	    try {
		if (entity instanceof Circle) {
		    removeEntity(entity);
		}
	    } catch (Throwable e) {

	    }
	}

    }

    /**
     * Gets all the entities in the engine
     * 
     * @return All the entities in the engine
     */
    public Vector<Entity> getEntities() {
	return entities;
    }

    /**
     * Destroys all entities in the engine
     */
    public void clearAll() {
	entities.clear();
    }

    public boolean hasWon() {
	return won;
    }

    public void setWon(boolean b) {
	won = b;
    }
}
