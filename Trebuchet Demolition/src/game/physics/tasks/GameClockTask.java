package game.physics.tasks;

import game.GamePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A clock which constantly update()s the physics engine
 * 
 * @author Gordon Guan
 * @version Dec 2014
 */
public class GameClockTask implements ActionListener
{
	private GamePanel panel;

	public GameClockTask(GamePanel panel)
	{
		super();
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		panel.tick();
		panel.checkWin();
		panel.doScore();
	}
}
