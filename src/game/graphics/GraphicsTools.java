package game.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Contains many helper methods to help draw fancy things
 * 
 * @author Gordon Guan
 * @version Dec 2014
 * 
 */
public class GraphicsTools {

    public static final Color PANEL_COLOR = new Color(247, 247, 247, 128);
    public static final Font MAIN_FONT = new Font("Optima", Font.BOLD, 40);
    public static final Font OPTIONS_FONT = new Font("Optima", Font.BOLD, 27);
    public static final Font LEVEL_SELECT_FONT = new Font("Consolas", Font.PLAIN, 20);
    public static final Color BG_COLOR = new Color(150, 150, 150, 150);

    /**
     * Draws shadowed text at the position
     * 
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

    /**
     * Makes a color brighter (for selected MenuItems)
     * 
     * @param color The color to brighten
     * @param fraction The percentage to brighten the color by
     * @return the brightened color
     */
    public static Color brighten(Color color, double fraction) {

	int red = (int) Math.round(Math.min(255, color.getRed() + 255 * fraction));
	int green = (int) Math.round(Math.min(255, color.getGreen() + 255 * fraction));
	int blue = (int) Math.round(Math.min(255, color.getBlue() + 255 * fraction));

	int alpha = color.getAlpha();

	return new Color(red, green, blue, alpha);

    }

    public static BufferedImage bufferImage(Image image) {
	BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

	Graphics g = bufferedImage.createGraphics();
	g.drawImage(image, 0, 0, null);
	g.dispose();

	return bufferedImage;
    }
}
