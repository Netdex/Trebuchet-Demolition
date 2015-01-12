package game.graphics.menu;

/**
 * Represents an event where a user selects a menu item and an action is performed
 * @author Gordon Guan
 * @version Jan 2015
 *
 */
public interface MenuKeyEvent {
    /**
     * This method is called when a menu action occurs
     * @param keycode The keycode pressed to do the menu action
     */
    public void selectionAction(int keycode);
}
