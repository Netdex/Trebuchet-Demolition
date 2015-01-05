package physics;

import game.level.Level;

import java.util.ArrayList;
import java.util.Properties;

import physics.entity.AABB;
import physics.entity.Circle;
import physics.entity.Entity;
import physics.entity.Rectangle;
import physics.util.CollisionChecker;
import physics.util.CollisionType;
import physics.util.Vector;

/**
 * Contains the main code to emulate physics
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class PhysicsEngine {
    // These constants are not related to real world constants
    private static final Vector GRAVITY = new Vector(0, 0.1);
    private static final double FRICTION = 1.05;
    private static final double RESTITUTION = 1.5;

    private boolean gravity = true;

    private int width;
    private int height;

    public int collisionsInTick = 0;

    private ArrayList<Entity> entities;

    public PhysicsEngine(int width, int height) {
	this.width = width;
	this.height = height;
	this.entities = new ArrayList<Entity>();
    }

    /**
     * Loads a level into the simulator
     * 
     * @param level The level to load
     */
    @SuppressWarnings("unchecked")
    public void loadLevel(Level level) {
	entities.clear();
	Properties metadata = level.getMetadata();
	ArrayList<Entity> levelEntities = level.getEntities();

	// TODO Finish level loader into physics engine
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
	
	entities = (ArrayList<Entity>) levelEntities.clone();
    }

    /**
     * Makes the physics engine do a physics 'tick'
     */
    public void tick() {
	collisionsInTick = 0;
	for (Entity e : entities) {
	    if (gravity)
		e.acc = GRAVITY;

	    boolean collided = handleCollisions(e);
	    if (collided)
		collisionsInTick++;

	    e.vel = e.vel.subtract(e.acc);

	    // Subtract the acceleration from velocities
	    if (e instanceof Circle) {
		Circle c = (Circle) e;
		c.loc = c.loc.subtract(e.vel);
	    } else if (e instanceof AABB) {
		AABB a = (AABB) e;
		a.p1 = a.p1.subtract(a.vel);
		a.p2 = a.p2.subtract(a.vel);
	    } else if (e instanceof Rectangle) {
		Rectangle r = (Rectangle) e;
		r.p1 = r.p1.subtract(r.vel);
		r.p2 = r.p2.subtract(r.vel);
	    }

	}

    }

    /**
     * Checks for collisions with walls or other entities of an entity
     * 
     * @param entity The entity to check for collisions for
     * @return Whether or not the entity is colliding
     */
    public synchronized boolean handleCollisions(Entity entity) {
	// Check for circle based collisions
	if (entity instanceof Circle) {
	    Circle circle = (Circle) entity;
	    int radius = circle.getRadius();
	    double x = circle.loc.x;
	    double y = circle.loc.y;

	    // Check every circle onto this one to see if they are colliding
	    for (Entity e : entities) {
		if (e instanceof Circle) {
		    if (e != circle) {
			if (circle.getCollisionState(e) == CollisionType.CIRCLE_TO_CIRCLE) {
			    CollisionChecker.resolveCircleCollision(circle, (Circle) e, RESTITUTION);
			}
		    }
		}
	    }

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

	    for (Entity e : entities) {
		if (e instanceof AABB) {
		    if (e != entity) {
			if (aabb.getCollisionState(e) == CollisionType.CIRCLE_TO_CIRCLE) {
			    CollisionChecker.resolveAABBCollision(aabb, (AABB) e, RESTITUTION);
			    return true;
			}
		    }
		}
	    }

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

	} else if (entity instanceof Rectangle) {
	    Rectangle rect = (Rectangle) entity;

	    boolean retType = false;
	    if (rect.p1.x < 0) {
		double correction = Math.abs(rect.p1.x);
		rect.p1.x += correction;
		rect.p2.x += correction;
		rect.vel.x = -rect.vel.x / RESTITUTION;
		retType = true;
	    }
	    if (rect.p1.y < 0) {
		double correction = Math.abs(rect.p1.y);
		rect.p1.y += correction;
		rect.p2.y += correction;
		rect.vel.y = -rect.vel.y / RESTITUTION;
		retType = true;
	    }
	    if (rect.p2.x > width) {
		double correction = rect.p2.x - width;
		rect.p1.x -= correction;
		rect.p2.x -= correction;
		rect.vel.x = -rect.vel.x / RESTITUTION;
		retType = true;
	    }
	    if (rect.p2.y > height) {
		double correction = rect.p2.y - height;
		rect.p1.y -= correction;
		rect.p2.y -= correction;
		rect.vel.y = -rect.vel.y / RESTITUTION;
		rect.vel.x = rect.vel.x / FRICTION;
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
     * Removes the entity to the physics engine
     * 
     * @param entity The entity to remove
     */
    public void removeEntity(Entity entity) {
	entities.remove(entity);
    }

    /**
     * Gets all the entities in the engine
     * 
     * @return All the entities in the engine
     */
    public ArrayList<Entity> getEntities() {
	return entities;
    }

    /**
     * Destroys all entities in the engine
     */
    public void clearAll() {
	entities.clear();
    }
}
