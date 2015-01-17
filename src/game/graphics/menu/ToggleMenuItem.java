package game.graphics.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

/**
 * A MenuItem which can be toggled
 * @author Gordon Guan
 * @version Jan 2015
 */
public class ToggleMenuItem extends MenuItem {
    private Color toggleColor;
    private boolean isEnabled;
    
    public ToggleMenuItem(String text, Font font, Color color, Color bgcolor, Color toggleColor, Rectangle dimensions, MenuItemAction action) {
	super(text, font, color, bgcolor, dimensions, action);
	this.toggleColor = toggleColor;
	isEnabled = false;
    }
    
    /**
     * Toggles the toggle color with this item's actual color
     */
    public void toggle(){
	Color tempColor = this.getBackgroundColor();
	this.setBackgroundColor(toggleColor);
	toggleColor = tempColor;
	isEnabled = !isEnabled;
    }
    
    /**
     * Gets the state of the toggle
     * @return the state of the toggle
     */
    public boolean getEnabled(){
	return isEnabled;
    }
    
    /**
     * Sets the state of the toggle
     * @param enabled the new state of the toggle
     */
    public void setEnabled(boolean enabled){
	isEnabled = enabled;
    }
}
