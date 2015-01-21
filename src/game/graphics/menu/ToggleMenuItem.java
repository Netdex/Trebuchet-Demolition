package game.graphics.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

/**
 * A MenuItem which can be toggled
 * 
 * @author Gordon Guan
 * @version Jan 2015
 */
public class ToggleMenuItem extends MenuItem {
    private Color toggleColor;
    private boolean isEnabled;

    /**
     * Creates a toggleable MenuItem
     * @param text The text on the MenuItem
     * @param font The font of the text
     * @param color The color of the text
     * @param bgcolor The background color of the MenuItem
     * @param toggleColor The background color when toggled
     * @param dimensions The dimensions as a Rectangle
     * @param action The MenuItemAction to take on click
     * @param shadowDist The distance of the text's shadow
     */
    public ToggleMenuItem(String text, Font font, Color color, Color bgcolor, Color toggleColor, Rectangle dimensions, MenuItemAction action, int shadowDist) {
	super(text, font, color, bgcolor, dimensions, action, shadowDist);
	this.toggleColor = toggleColor;
	isEnabled = false;
    }

    /**
     * Toggles the toggle color with this item's actual color
     */
    public void toggle() {
	isEnabled = !isEnabled;
    }

    /**
     * Gets the state of the toggle
     * 
     * @return the state of the toggle
     */
    public boolean getEnabled() {
	return isEnabled;
    }

    /**
     * Sets the state of the toggle
     * 
     * @param enabled the new state of the toggle
     */
    public void setEnabled(boolean enabled) {
	if (enabled != isEnabled)
	    toggle();
    }

    
    @Override
    public Color getBackgroundColor() {
	// Return the background color based on whether its toggled or not
	if (isEnabled)
	    return super.getBackgroundColor();
	return toggleColor;
    }
}
