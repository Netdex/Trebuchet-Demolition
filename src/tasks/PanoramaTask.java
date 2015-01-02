package tasks;

import game.GamePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @deprecated
 * @author Netdex
 *
 */
public class PanoramaTask implements ActionListener{
    private GamePanel panel;

    public PanoramaTask(GamePanel panel) {
	super();
	this.panel = panel;
    }

    public void actionPerformed(ActionEvent event) {
	panel.repaint();
    }
}
