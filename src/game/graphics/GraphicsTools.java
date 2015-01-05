package game.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Contains many helper methods to help draw fancy things
 * 
 * @author Gordon Guan
 * @version Dec 2014
 * 
 */
public class GraphicsTools {
    
    public final static Color PANEL_COLOR = new Color(247, 247, 247, 128);
    public static final Font MAIN_FONT = new Font("Optima", Font.BOLD, 40);
    public static final Font OPTIONS_FONT = new Font("Optima", Font.BOLD, 27);
    public static final Font LEVEL_SELECT_FONT = new Font("Consolas", Font.PLAIN, 20);
    /**
     * Draws shadowed text at the position
     * @param g The graphics to draw the text with
     * @param text The text to draw
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public static void drawShadowedText(Graphics g, String text, int x, int y, int dist) {
	Color originalColor = g.getColor();
	g.setColor(Color.BLACK);
	g.drawString(text, x + dist, y + dist);
	g.setColor(originalColor);
	g.drawString(text, x, y);
    }
}
