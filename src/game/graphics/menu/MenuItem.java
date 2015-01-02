package game.graphics.menu;

import java.awt.Color;
import java.awt.Font;

/**
 * Represents an item in a game menu
 * @author Gordon Guan
 * @version Dec 2014
 */
public class MenuItem {
    private String text;
    private Font font;
    private Color color;
    /**
     * Constructs a MenuItem with the default font of Calibri
     * @param text The text for the MenuItem
     * @param color The color for the MenuItem
     */
    public MenuItem(String text, Color color) {
	this.text = text;
	this.font = Font.getFont("Calibri");
	this.color = color;
    }

    /**
     * Constructs a MenuItem with a given font
     * @param text The text for the MenuItem
     * @param font The font for the MenuItem
     * @param color The color for the MenuItem
     */
    public MenuItem(String text, Font font, Color color) {
	this.text = text;
	this.font = font;
	this.color = color;
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
}
