package game.graphics;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Contains many helper methods to help draw certain things
 * 
 * @author Gordon Guan
 * @version Dec 2014
 * 
 */
public class GraphicsTools {
    
    /**
     * Drawsw shadowed text at the position
     * @param g The graphics to draw the text with
     * @param text The text to draw
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public static void drawShadowedText(Graphics g, String text, int x, int y) {
	Color originalColor = g.getColor();
	g.setColor(Color.BLACK);
	g.drawString(text, x + 5, y + 2);
	g.setColor(originalColor);
	g.drawString(text, x, y);
    }
}
