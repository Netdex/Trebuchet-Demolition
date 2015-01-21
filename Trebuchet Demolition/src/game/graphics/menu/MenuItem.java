package game.graphics.menu;

import game.graphics.GraphicsTools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Represents an item in a game menu
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */

public class MenuItem
{
	private String text;
	private Font font;
	private Color color;
	private Color bgcolor;
	private Rectangle dimensions;
	private boolean isHighlighted;
	private int shadowDist;
	private MenuItemAction action;

	/**
	 * Constructs a menu with Calibri font
	 * 
	 * @param text The text on the MenuItem
	 * @param color The color of the text
	 * @param bgcolor The background color
	 * @param dimensions The dimensions as a Rectangle
	 * @param action The MenuItemAction to do when clicked
	 * @param shadowDist The distance of the shadow for the text
	 */
	public MenuItem(String text, Color color, Color bgcolor,
			Rectangle dimensions, MenuItemAction action, int shadowDist)
	{
		this.text = text;
		this.font = Font.getFont("Calibri");
		this.color = color;
		this.bgcolor = bgcolor;
		this.dimensions = dimensions;
		this.action = action;
		isHighlighted = false;
		this.shadowDist = shadowDist;
	}

	/**
	 * Constructs a menu with given font
	 * 
	 * @param text The text on the MenuItem
	 * @param font The font of the text
	 * @param color The color of the text
	 * @param bgcolor The background color
	 * @param dimensions The dimenisons as a Rectangle
	 * @param action The MenuItemAction to do when clicked
	 * @param shadowDist The distance of the shadow for the text
	 */
	public MenuItem(String text, Font font, Color color, Color bgcolor,
			Rectangle dimensions, MenuItemAction action, int shadowDist)
	{
		this.text = text;
		this.font = font;
		this.color = color;
		this.bgcolor = bgcolor;
		this.dimensions = dimensions;
		this.action = action;
		this.shadowDist = shadowDist;
	}

	/**
	 * Draws the menu onto the given graphics
	 * 
	 * @param g The graphics to draw it on
	 */
	public void drawItem(Graphics2D g)
	{
		// Draw the background rectangle
		Rectangle rectangle = this.getDimensions();
		g.setColor(this.getBackgroundColor().darker().darker());
		g.fillRect(rectangle.x + 2, rectangle.y + 2, rectangle.width,
				rectangle.height);
		// Draw highlights if they exist
		if (this.isHighlighted())
			g.setColor(GraphicsTools.brighten(this.getBackgroundColor(), 0.3));
		else
			g.setColor(this.getBackgroundColor());
		g.fill(rectangle);
		g.setColor(this.getColor());
		g.setFont(this.getFont());
		// Draw the menu item text
		GraphicsTools.drawShadowedText(g, this.getText(), rectangle.x + 10,
				rectangle.y + g.getFontMetrics().getAscent() + 3, shadowDist);
	}

	/**
	 * Executes this MenuItem's MenuItemAction
	 * 
	 * @param item The menu item which was clicked
	 */
	public void doAction(MenuItem item)
	{
		action.doAction(item);
	}

	/**
	 * Gets the MenuItem's color
	 * 
	 * @return the MenuItem's color
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Sets the MenuItem's color
	 * 
	 * @param color The color to set it to
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * Get the background color
	 * 
	 * @return the background color
	 */
	public Color getBackgroundColor()
	{
		return bgcolor;
	}

	/**
	 * Set the background color
	 * 
	 * @param bgcolor The new background color
	 */
	public void setBackgroundColor(Color bgcolor)
	{
		this.bgcolor = bgcolor;
	}

	/**
	 * Gets the text on the MenuItem
	 * 
	 * @return the text on the MenuItem
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * Sets the text on the MenuItem
	 * 
	 * @param text The text to put on the MenuItem
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 * Gets the font on the MenuItem
	 * 
	 * @return the font on the MenuItem
	 */
	public Font getFont()
	{
		return font;
	}

	/**
	 * Sets the font on the MenuItem
	 * 
	 * @param font The font to set on the MenuItem
	 */
	public void setFont(Font font)
	{
		this.font = font;
	}

	/**
	 * Gets the rectangle dimensions
	 * 
	 * @return The rectangle dimensions
	 */
	public Rectangle getDimensions()
	{
		return dimensions;
	}

	/**
	 * Sets the rectangle dimensions
	 * 
	 * @param dimensions The new rectangle dimensions
	 */
	public void setDimensions(Rectangle dimensions)
	{
		this.dimensions = dimensions;
	}

	/**
	 * Gets whether this item is highlighted
	 * 
	 * @return whether this item is highlighted
	 */
	public boolean isHighlighted()
	{
		return isHighlighted;
	}

	/**
	 * Set whether this item was highlighted
	 * 
	 * @param isHighlighted Whether this item should be highlighted
	 */
	public void setHighlighted(boolean isHighlighted)
	{
		this.isHighlighted = isHighlighted;
	}
}
