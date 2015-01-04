package game.graphics.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Represents a menu
 * @author Gordon Guan
 * @version Jan 2015
 */
public abstract class Menu {
    private int selectedIndex;
    private ArrayList<MenuItem> menuItems;
    
    public Menu() {
	this.selectedIndex = 0;
	menuItems = new ArrayList<MenuItem>();
    }
    
    /**
     * Draws the menu onto the specified graphics
     * @param g The graphics to draw the menu on
     * @param width The width of the area
     * @param height The height of the area
     * @param startHeight The start height to begin drawing
     * @param separation The separation of the menu items
     */
    public abstract void drawMenu(Graphics g, int width, int height, int startHeight, int separation);

    /**
     * Adds a MenuItem to the menu
     * 
     * @param item
     *            The MenuItem to add
     */
    public void addMenuItem(MenuItem item) {
	menuItems.add(item);
    }

    /**
     * Removes a MenuItem from the menu
     * 
     * @param item
     *            The MenuItem to remove
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
    public void clearMenu(){
	menuItems.clear();
    }
    
    /**
     * Gets the selected item's index
     * 
     * @return the selected item's index
     */
    public int getSelectedItem() {
	return selectedIndex;
    }

    /**
     * Sets the selected index
     * 
     * @param selectedIndex
     *            The index to set
     */
    public void setSelectedItem(int selectedIndex) {
	this.selectedIndex = selectedIndex;
    }

    /**
     * Check if the current MenuItem is selected
     * 
     * @return if the current MenuItem is selected or not
     */
    public boolean isSelected(MenuItem item) {
	return selectedIndex == menuItems.indexOf(item);
    }

    /**
     * Moves the selected item one up
     */
    public void shiftUp() {
	if (selectedIndex > 0)
	    selectedIndex--;
    }

    /**
     * Moves the selected item one down
     */
    public void shiftDown() {
	if (selectedIndex < menuItems.size() - 1)
	    selectedIndex++;
    }
    
    /**
     * Makes a color brighter (for selected MenuItems)
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
