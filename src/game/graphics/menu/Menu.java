package game.graphics.menu;

import java.awt.Graphics2D;
import java.awt.Point;
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
     * Makes mouseovers on menu items highlight the menu item
     * @param event The mouse event given from the mouse moved event
     */
    public void handleHighlights(MouseEvent event){
	Point p = event.getPoint();
	for(MenuItem item : this.getMenuItems()){
	    if(item.getDimensions().contains(p)){
		item.setHighlighted(true);
	    }
	    else
		item.setHighlighted(false);
	}
    }

    /**
     * Draws the menu onto the specified graphics
     * 
     * @param g The graphics to draw the menu on
     */
    public void drawMenu(Graphics2D g){
	for(MenuItem menuItem : this.getMenuItems()){
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
