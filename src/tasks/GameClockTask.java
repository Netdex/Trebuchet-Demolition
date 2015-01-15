package tasks;
import game.GamePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameClockTask implements ActionListener {
    private GamePanel panel;

    public GameClockTask(GamePanel panel) {
	super();
	this.panel = panel;
    }

    public void actionPerformed(ActionEvent event) {
	panel.tick();
	panel.checkWin();
    }
}
