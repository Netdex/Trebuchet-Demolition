package physics.entity;

import java.awt.Color;

import physics.util.Vector2D;

/**
 * Represents a target to shoot at
 * @author Gordon Guan
 * @version Jan 2015
 */
public class Target extends AABB{

    public Target(Vector2D p1, Vector2D p2, Color c) {
	super(p1, p2, Vector2D.ZERO, c);
	// TODO Auto-generated constructor stub
    }

}
