package game.physics.entity;

import game.physics.util.CollisionType;
import game.physics.util.MathOperations;
import game.physics.util.Vector2D;

public class Projectile2D extends Circle2D {

    /**
     * Constructs a projectile
     * @param loc The location of the center
     * @param vel The velocity
     * @param radius The radius
     */
    public Projectile2D(Vector2D loc, Vector2D vel, int radius) {
	super(loc, vel, radius);
    }
    
    @Override
    public CollisionType getCollisionState(Entity2D entity){
	if(entity instanceof Target2D){
	    Target2D target = (Target2D) entity;
	    AABB2D bounds = this.getBoundingBox();

	    CollisionType doesCollide = MathOperations.hasAABBCollision(target, bounds);
	    if (doesCollide == CollisionType.AABB_TO_AABB) {
		if (this instanceof Projectile2D)
		    return CollisionType.WINNING_COLLISION;
		else
		    return CollisionType.CIRCLE_TO_AABB;
	    }
	    return CollisionType.NO_COLLISION;
	}
	return super.getCollisionState(entity);
    }
}
