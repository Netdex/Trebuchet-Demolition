package game.graphics.menu;

import game.graphics.GraphicsTools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Represents a menu
 * 
 * @author Gordon Guan
 * @version Jan 2015
 */
public class Menu {
    
    private ArrayList<MenuItem> menuItems;

    public Menu() {
	menuItems = new ArrayList<MenuItem>();
    }

    /**
     * Makes the menu respond to the given key code to do menu actions, based on the actions defined in MenuKeyEvent
     * 
     * @param keycode The keycode to input
     */
    public void invokeAction(MouseEvent event) {
	Point p = event.getPoint();
	for(MenuItem item : this.getMenuItems()){
	    if(item.getDimensions().contains(p)){
		item.doAction(item);
	    }
	}
    }



    /**
     * Draws the menu onto the specified graphics
     * 
     * @param g The graphics to draw the menu on
     */
    public void drawMenu(Graphics2D g){
	for(MenuItem menuItem : this.getMenuItems()){
	    Rectangle rectangle = menuItem.getDimensions();
	    g.setColor(menuItem.getBackgroundColor().darker().darker());
	    g.fillRect(rectangle.x + 2, rectangle.y + 2, rectangle.width, rectangle.height);
	    g.setColor(menuItem.getBackgroundColor());
	    g.fill(rectangle);
	    g.setColor(menuItem.getColor());
	    g.setFont(menuItem.getFont());
	    GraphicsTools.drawShadowedText(g, menuItem.getText(), rectangle.x + 10, rectangle.y + g.getFontMetrics().getAscent(), 2);
	}
    }

    /**
     * Adds a MenuItem to the menu
     * 
     * @param item The MenuItem to add
     */
    public void addMenuItem(MenuItem item) {
	menuItems.add(item);
    }

    /**
     * Removes a MenuItem from the menu
     * 
     * @param item The MenuItem to remove
     */
    public void removeMenuItem(MenuItem item) {
	menuItems.remove(item);
    }

    /**
     * Gets a list of all the MenuItems in this menu
     * 
     * @return a list of the MenuItems
     */
    public ArrayList<MenuItem> getMenuItems() {
	return menuItems;
    }

    /**
     * Clears the menu
     */
    public void clearMenu() {
	menuItems.clear();
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
}
