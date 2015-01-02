package game.graphics.menu;

import java.awt.Graphics;
import java.util.ArrayList;

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
}
