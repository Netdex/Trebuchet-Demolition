package physics.util;

import physics.entity.AABB;

/**
 * Contains math operations to simplify physics calculations
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class MathOperations {
    public static double square(double number) {
	return number * number;
    }

    public static CollisionType hasAABBCollision(AABB a, AABB b) {
	if (a.p2.x < b.p1.x || a.p1.x > b.p2.x)
	    return CollisionType.NO_COLLISION;
	if (a.p2.y < b.p1.y || a.p1.y > b.p2.y)
	    return CollisionType.NO_COLLISION;
	return CollisionType.AABB_TO_AABB;
    }
}
