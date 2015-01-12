package game.graphics.menu;

import game.graphics.GraphicsTools;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Represents a main menu for the game
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class CenteredMenu extends Menu{
    
    /**
     * Constructs a main menu
     */
    public CenteredMenu(MenuKeyEvent event, int shadowDist) {
	super(event, shadowDist);	
    }

    /**
     * Draws the menu
     */
    @Override
    public void drawMenu(Graphics g, int width, int height, int startHeight, int separation) {
	int verticalOffset = startHeight;
	// Make a backup of the graphics' font and color
	Font originalFont = g.getFont();
	Color originalColor = g.getColor();

	for (MenuItem menuItem : this.getMenuItems()) {
	    if(isSelected(menuItem)){
		g.setColor(Menu.brighten(menuItem.getColor(), 0.75));
	    }
	    else{
		g.setColor(menuItem.getColor());
	    }
	    
	    String text = menuItem.getText();
	    Font font = menuItem.getFont();
	    g.setFont(font);
	    FontMetrics fm = g.getFontMetrics();
	    int x = width / 2 - fm.stringWidth(text) / 2;
	    GraphicsTools.drawShadowedText(g, text, x, verticalOffset, this.getShadowDist());
	    
	    
	    verticalOffset += separation;
	}
	
	// Restore the original font and color
	g.setColor(originalColor);
	g.setFont(originalFont);

    } 
}
