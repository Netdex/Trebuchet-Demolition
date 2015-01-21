package game.graphics.menu;

/**
 * An action which occurs when a MenuItem is clicked
 * 
 * @author Gordon Guan
 * @version Jan 2015
 */
public interface MenuItemAction
{
	/**
	 * An action to be done on click
	 * 
	 * @param item The MenuItem who this action belongs to
	 */
	public void doAction(MenuItem item);
}
