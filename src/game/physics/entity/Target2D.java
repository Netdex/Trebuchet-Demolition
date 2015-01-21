package game.physics.entity;

import game.GamePanel;
import game.graphics.GraphicsTools;
import game.physics.util.Vector2D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;

/**
 * Represents a target to shoot at
 * 
 * @author Gordon Guan
 * @version Jan 2015
 */
public class Target2D extends AABB2D {
    private static Image targetTexture = GamePanel.brickTexture;

    /**
     * Constructs a target
     * @param p1 The first point
     * @param p2 The second point
     */
    public Target2D(Vector2D p1, Vector2D p2) {
	super(p1, p2, Vector2D.ZERO, false);
    }

    @Override
    public void drawEntity(Graphics2D g) {
	if (this.getTexture() == null) {
	    Shape shape = this.getShape();
	    g.fill(shape);
	} else {
	    Paint originalPaint = g.getPaint();
	    TexturePaint texturePaint = new TexturePaint(GraphicsTools.bufferImage(targetTexture), new Rectangle(0, 0, GamePanel.TEXTURE_SIZE, GamePanel.TEXTURE_SIZE));
	    g.setPaint(texturePaint);
	    Shape poly = this.getShape();
	    g.fill(poly);
	    g.setPaint(originalPaint);
	    g.setColor(Color.DARK_GRAY);
	    g.draw(poly);
	}
    }

}
