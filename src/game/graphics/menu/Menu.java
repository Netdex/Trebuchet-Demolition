package game.graphics.menu;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Represents a game menu
 * 
 * @author Gordon Guan
 * @version Jan 2015
 */
public class Menu {

    private ArrayList<MenuItem> menuItems;
    
    /**
     * Constructs a menu
     */
    public Menu() {
	menuItems = new ArrayList<MenuItem>();
    }

    /**
     * Makes the menu respond to the given MouseEvent to do actions defined in the given MenuItemAciton
     * @param event The MouseEvent to trigger events
     */
    public void invokeAction(MouseEvent event) {
	Point p = event.getPoint();
	for (MenuItem item : this.getMenuItems()) {
	    if (item.getDimensions().contains(p)) {
		item.doAction(item);
		event.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	    }
	}
    }

    /**
     * Makes mouseovers on menu items highlight the menu item
     * 
     * @param event The mouse event given from the mouse moved event
     */
    public void handleHighlights(MouseEvent event) {
	Point p = event.getPoint();
	boolean highlightFound = false;
	for (MenuItem item : this.getMenuItems()) {
	    if (item.getDimensions().contains(p)) {
		item.setHighlighted(true);
		highlightFound = true;
	    } else {
		item.setHighlighted(false);	
	    }
	}
	if(highlightFound){
	    event.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	else{
	    event.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
    }

    /**
     * Draws the menu onto the specified graphics
     * 
     * @param g The graphics to draw the menu on
     */
    public void drawMenu(Graphics2D g) {
	for (MenuItem menuItem : this.getMenuItems()) {
	    menuItem.drawItem(g);
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
}
