package game.graphics.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

/**
 * Represents an item in a game menu
 * @author Gordon Guan
 * @version Dec 2014
 */

public class MenuItem {
    private String text;
    private Font font;
    private Color color;
    private Color bgcolor;
    private Rectangle dimensions;
    
    private MenuItemAction action;

    public MenuItem(String text, Color color, Color bgcolor, Rectangle dimensions, MenuItemAction action) {
	this.text = text;
	this.font = Font.getFont("Calibri");
	this.color = color;
	this.bgcolor = bgcolor;
	this.dimensions = dimensions;
	this.action = action;
    }

    

    public MenuItem(String text, Font font, Color color, Color bgcolor, Rectangle dimensions, MenuItemAction action) {
	this.text = text;
	this.font = font;
	this.color = color;
	this.bgcolor = bgcolor;
	this.dimensions = dimensions;
	this.action = action;
    }
    public void doAction(MenuItem item){
	action.doAction(item);
    }
    /**
     * Gets the MenuItem's color
     * @return the MenuItem's color
     */
    public Color getColor(){
	return color;
    }
    
    /**
     * Sets the MenuItem's color
     * @param color The color to set it to
     */
    public void setColor(Color color){
	this.color = color;
    }
    
    public Color getBackgroundColor(){
	return bgcolor;
    }
    
    public void setBackgroundColor(Color bgcolor){
	this.bgcolor = bgcolor;
    }
    /**
     * Gets the text on the MenuItem
     * @return the text on the MenuItem
     */
    public String getText(){
	return text;
    }
    
    /**
     * Sets the text on the MenuItem
     * @param text The text to put on the MenuItem
     */
    public void setText(String text){
	this.text = text;
    }
    
    /**
     * Gets the font on the MenuItem
     * @return the font on the MenuItem
     */
    public Font getFont(){
	return font;
    }
    
    /**
     * Sets the font on the MenuItem
     * @param font The font to set on the MenuItem
     */
    public void setFont(Font font){
	this.font = font;
    }

    public Rectangle getDimensions() {
        return dimensions;
    }

    public void setDimensions(Rectangle dimensions) {
        this.dimensions = dimensions;
    }
}
