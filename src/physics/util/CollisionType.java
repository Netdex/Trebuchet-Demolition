package physics.util;

/**
 * Stores all the possible types of collisions
 * @author Gordon Guan
 * @version Dec 2014
 */
public enum CollisionType {
	SIDE_WALL,
	FLOOR_CEILING,
	CORNER,
	NO_COLLISION,
	CIRCLE_TO_CIRCLE,
	AABB_TO_AABB,
	RECT_TO_RECT;
}
