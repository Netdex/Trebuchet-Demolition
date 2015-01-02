package game.graphics.menu;

import game.graphics.GraphicsTools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class AlignedMenu extends Menu {
    @Override
    public void drawMenu(Graphics g, int x, int height, int startHeight, int separation) {
	int verticalOffset = startHeight;
	// Make a backup of the graphics' font and color
	Font originalFont = g.getFont();
	Color originalColor = g.getColor();

	for (MenuItem menuItem : this.getMenuItems()) {
	    if (isSelected(menuItem)) {
		g.setColor(Color.WHITE);
	    } else {
		g.setColor(menuItem.getColor());
	    }

	    String text = menuItem.getText();
	    Font font = menuItem.getFont();
	    g.setFont(font);
	    GraphicsTools.drawShadowedText(g, text, x, verticalOffset);

	    verticalOffset += separation;
	}

	// Restore the original font and color
	g.setColor(originalColor);
	g.setFont(originalFont);
    }
}
