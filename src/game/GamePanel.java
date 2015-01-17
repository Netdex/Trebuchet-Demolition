package game;

import game.graphics.GraphicsTools;
import game.graphics.ScreenType;
import game.graphics.menu.Menu;
import game.graphics.menu.MenuItem;
import game.graphics.menu.MenuItemAction;
import game.graphics.menu.MenuMouseEvent;
import game.graphics.menu.ToggleMenuItem;
import game.level.Level;
import game.level.LevelManager;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import physics.PhysicsEngine;
import physics.entity.Entity2D;
import physics.entity.Projectile2D;
import physics.entity.Rectangle2D;
import physics.util.Vector2D;
import tasks.GameClockTask;

/**
 * A panel which draws all the shapes and contains a physics engine
 * 
 * @author Gordon Guan
 * @version Dec 2014
 * 
 */

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public static Image titleImage, backgroundImage, trebuchetImage;

    /***********************************************************************/
    public PhysicsEngine engine;
    private Timer physicsTimer;
    private Level loadedLevel;

    // Approximately 60 frames per second
    private static final int TICK_RATE = 10;
    private int power = 75;
    private int angle = 50;

    private boolean paused = false;
    /***********************************************************************/
    private ScreenType displayScreen = ScreenType.MAIN_MENU;

    private final int width, height;
    /***********************************************************************/
    private Menu mainMenu;
    private Menu optionsMenu, levelSelectMenu, pauseMenu;

    private MusicManager musicManager;
    private ToggleMenuItem musicMenuItem;

    /***********************************************************************/

    public GamePanel(final int width, final int height) {
	super();
	this.setBackground(Color.WHITE);
	this.setFocusable(true);

	this.width = width;
	this.height = height;
	engine = new PhysicsEngine(width, height);

	// Load all the images
	loadResources();

	physicsTimer = new Timer(TICK_RATE, new GameClockTask(this));

	// Set up all the main menu handlers

	MenuItemAction playMenuItemAction = new MenuItemAction() {
	    public void doAction(MenuItem item) {
		LevelManager.loadLevels();
		levelSelectMenu.clearMenu();
		int y = 75;
		int mheight = 23;
		int mlength = 600;
		int sep = 10;
		for (int levelID = 0; levelID < LevelManager.getLevels().size(); levelID++) {
		    Level level = LevelManager.getLevels().get(levelID);
		    MenuItemAction levelSelectAction = new MenuItemAction() {
			public void doAction(MenuItem item) {
			    Level level = LevelManager.getLevels().get(levelSelectMenu.getMenuItems().indexOf(item));
			    loadLevel(level);

			    start();
			    repaint();
			}
		    };
		    String menuItemText = level.getName() + " - " + level.getFile().getName();
		    MenuItem menuItem = new MenuItem(menuItemText, GraphicsTools.LEVEL_SELECT_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(10, y, mlength, mheight),
			    levelSelectAction);
		    levelSelectMenu.addMenuItem(menuItem);
		    y += mheight + sep;
		}
		y += mheight + sep;
		levelSelectMenu.addMenuItem(new MenuItem("Return to Main Menu", GraphicsTools.LEVEL_SELECT_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(100, y, 200, y + 50), new MenuItemAction() {
		    public void doAction(MenuItem item) {
			displayScreen = ScreenType.MAIN_MENU;
			repaint();
		    }
		}));
		displayScreen = ScreenType.LEVEL_SELECT;
		repaint();
	    }
	};
	MenuItemAction optionsMenuItemAction = new MenuItemAction() {
	    public void doAction(MenuItem item) {
		displayScreen = ScreenType.OPTIONS_MENU;
		repaint();
	    }
	};
	MenuItemAction helpMenuItemAction = new MenuItemAction() {
	    public void doAction(MenuItem item) {
		JOptionPane.showMessageDialog(null, "Use the up and down arrow keys to traverse the menus.\n" + "Use the enter key to select a menu item.\n\n"
			+ "Use the scroll wheel to increase power.", "Trebuchet Demolition Help", JOptionPane.INFORMATION_MESSAGE);
	    }
	};
	MenuItemAction aboutMenuItemAction = new MenuItemAction() {
	    public void doAction(MenuItem item) {
		JOptionPane.showMessageDialog(null, "Created by Gordon Guan\n(c) 2015 \n", "About Trebuchet Demolition", JOptionPane.INFORMATION_MESSAGE);
	    }
	};
	MenuItemAction exitMenuItemAction = new MenuItemAction() {
	    public void doAction(MenuItem item) {
		System.exit(0);
	    }
	};
	// Setup all the items in the menus

	mainMenu = new Menu();
	int y = 150;
	int mheight = 55;
	int mlength = 200;
	int sep = 10;
	MenuItem playMenuItem = new MenuItem("PLAY", GraphicsTools.MAIN_FONT, Color.WHITE, GraphicsTools.BG_COLOR, 		new Rectangle(100, y, mlength, mheight), playMenuItemAction);
	MenuItem optionsMenuItem = new MenuItem("OPTIONS", GraphicsTools.MAIN_FONT, Color.WHITE, GraphicsTools.BG_COLOR, 	new Rectangle(100, y + mheight + sep, mlength, mheight), optionsMenuItemAction);
	MenuItem helpMenuItem = new MenuItem("HELP", GraphicsTools.MAIN_FONT, Color.WHITE, GraphicsTools.BG_COLOR, 		new Rectangle(100, y + mheight * 2 + sep * 2, mlength, mheight), helpMenuItemAction);
	MenuItem aboutMenuItem = new MenuItem("ABOUT", GraphicsTools.MAIN_FONT, Color.WHITE, GraphicsTools.BG_COLOR, 		new Rectangle(100, y + mheight * 3 + sep * 3, mlength, mheight), aboutMenuItemAction);
	MenuItem exitMenuItem = new MenuItem("EXIT", GraphicsTools.MAIN_FONT, Color.WHITE, GraphicsTools.BG_COLOR, 		new Rectangle(100, y + mheight * 4 + sep * 4, mlength, mheight), exitMenuItemAction);
	mainMenu.addMenuItem(playMenuItem);
	mainMenu.addMenuItem(optionsMenuItem);
	mainMenu.addMenuItem(helpMenuItem);
	mainMenu.addMenuItem(aboutMenuItem);
	mainMenu.addMenuItem(exitMenuItem);

	MenuItemAction musicMenuItemAction = new MenuItemAction() {
	    public void doAction(MenuItem item) {
		musicMenuItem.toggle();
		if (musicMenuItem.getEnabled()) {
		    musicManager.start();
		} else {
		    musicManager.stop();
		}
		repaint();
	    }
	};

	MenuItemAction returnMenuItemAction = new MenuItemAction() {
	    public void doAction(MenuItem item) {
		displayScreen = ScreenType.MAIN_MENU;
		repaint();
	    }
	};
	optionsMenu = new Menu();

	musicMenuItem = new ToggleMenuItem("Music", GraphicsTools.OPTIONS_FONT, Color.WHITE, Color.GREEN, Color.RED, new Rectangle(100, 100, 100, 100), musicMenuItemAction);
	MenuItem returnMenuItem = new MenuItem("Return to Main Menu", GraphicsTools.LEVEL_SELECT_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(100, 200, 100, 100), returnMenuItemAction);

	optionsMenu.addMenuItem(musicMenuItem);
	optionsMenu.addMenuItem(returnMenuItem);

	levelSelectMenu = new Menu();

	// pauseMenu = new AlignedMenu(pauseMenuEvent, 1);
	// pauseMenu.addMenuItem(new MenuItem("Resume", GraphicsTools.OPTIONS_FONT, Color.GRAY));
	// pauseMenu.addMenuItem(new MenuItem("Return to Main Menu", GraphicsTools.OPTIONS_FONT, Color.GRAY));

	// Start the music TODO Add configuration support
	musicManager = new MusicManager();
	if (ConfigurationManager.musicEnabled)
	    musicManager.start();

	this.addMouseListener(new MouseAdapter() {
	    public void mousePressed(MouseEvent event) {
		System.out.println(event.getX() + ", " + event.getY());
		if (displayScreen == ScreenType.MAIN_MENU) {
		    mainMenu.invokeAction(event);
		} else if (displayScreen == ScreenType.OPTIONS_MENU) {
		    optionsMenu.invokeAction(event);
		} else if (displayScreen == ScreenType.LEVEL_SELECT) {
		    levelSelectMenu.invokeAction(event);
		} else if (displayScreen == ScreenType.IN_GAME) {
		    if (paused) {
			pauseMenu.invokeAction(event);
		    }
		}
	    }
	});
	this.addMouseWheelListener(new MouseWheelListener() {
	    public void mouseWheelMoved(MouseWheelEvent event) {
		int notches = event.getWheelRotation();
		if (displayScreen == ScreenType.IN_GAME) {
		    changePower(notches * 5);
		}
	    }
	});
	this.addMouseMotionListener(new MouseMotionListener() {

	    @Override
	    public void mouseDragged(MouseEvent event) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void mouseMoved(MouseEvent event) {
		Point point = event.getPoint();
		if (displayScreen == ScreenType.MAIN_MENU) {

		}

	    }

	});
	this.addKeyListener(new KeyAdapter() {
	    public void keyPressed(KeyEvent event) {
		int keycode = event.getKeyCode();
		// Respond to ingame events
		if (displayScreen == ScreenType.IN_GAME) {

		    if (keycode == KeyEvent.VK_RIGHT) {
			changePower(1);
		    } else if (keycode == KeyEvent.VK_LEFT) {
			changePower(-1);
		    } else if (keycode == KeyEvent.VK_UP) {
			changeAngle(1);
		    } else if (keycode == KeyEvent.VK_DOWN) {
			changeAngle(-1);
		    } else if (keycode == KeyEvent.VK_SPACE) {
			engine.removeLastProjectile();
			double vecX = Math.cos(Math.toRadians(180 - angle)) * power / 9;
			double vecY = Math.sin(Math.toRadians(180 - angle)) * power / 9;
			Vector2D vel = new Vector2D(vecX, vecY);

			Projectile2D c = new Projectile2D(new Vector2D(50, height - 50), vel, 10, Color.BLACK);
			engine.addEntity(c);

		    } else if (keycode == KeyEvent.VK_0) {
			engine.removeLastProjectile();
		    } else if (keycode == KeyEvent.VK_ESCAPE) {
			pause();
			repaint();
		    }

		}

	    }
	});

	/* DEBUG CODE TODO REMOVE DEBUG CODE FOR HOT-INSERTING ENTITIES */
	final Rectangle2D rect = new Rectangle2D(new Vector2D(300, 10), new Vector2D(350, 310), Color.BLACK);
	rect.rotate(30);
	engine.addEntity(rect);
	/* END DEBUG CODE */
    }

    public void loadLevel(Level level) {
	try {
	    if (level.getMetadata().getProperty("bgcolor") != null) {
		String[] colorRGB = level.getMetadata().getProperty("bgcolor").replaceAll(" ", "").split(",");
		Color color = new Color(Integer.parseInt(colorRGB[0]), Integer.parseInt(colorRGB[1]), Integer.parseInt(colorRGB[2]));
		this.setBackground(color);
	    } else {
		this.setBackground(Color.WHITE);
	    }
	} catch (Exception e) {
	    JOptionPane.showMessageDialog(null, "An error occured while loading a level: " + e.getMessage(), "Level Loading Error", JOptionPane.ERROR_MESSAGE);
	    TrebuchetDemolition.LOGGER.warning("Failed to load the level selected: " + e.getMessage());
	    return;
	}
	loadedLevel = level;
	engine.loadLevel(level);
    }

    /**
     * Checks for a win, then takes suitable action
     */
    public void checkWin() {
	if (engine.hasWon()) {
	    JOptionPane.showMessageDialog(null, "You have won!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
	    stop();
	    engine.clearAll();
	    displayScreen = ScreenType.LEVEL_SELECT;
	    repaint();
	    LevelManager.loadLevels();
	}
    }

    /**
     * Draws the game
     */
    @Override
    public void paintComponent(Graphics graphics) {
	super.paintComponent(graphics);

	Graphics2D g = (Graphics2D) graphics;
	RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g.setRenderingHints(rh);

	// Playing game
	if (displayScreen == ScreenType.IN_GAME) {
	    // Draw debug info
	    g.setColor(Color.WHITE);
	    g.fillRect(0, 0, 110, 100);
	    g.setColor(Color.BLACK);
	    g.drawString("Entities: " + engine.getEntities().size(), 10, 15);
	    g.drawString("Collisions: " + engine.collisionsInTick, 10, 30);
	    g.drawString("[GAME VARIABLES]: ", 10, 65);
	    g.drawString("Power: " + power + "%", 10, 80);
	    g.drawString("Angle: " + angle + "�", 10, 95);

	    g.drawImage(trebuchetImage, 20, height - 60, 80, height, 0, 0, trebuchetImage.getWidth(null), trebuchetImage.getHeight(null), null);
	    double vecX = Math.cos(Math.toRadians(180 - angle)) * power / 9;
	    double vecY = Math.sin(Math.toRadians(180 - angle)) * power / 9;
	    Vector2D vel = new Vector2D(vecX * 10, vecY * 10);
	    Vector2D shootPoint = new Vector2D(50, height - 50);

	    g.setColor(Color.RED);
	    g.drawLine((int) shootPoint.x, (int) shootPoint.y, (int) (shootPoint.x - vel.x), (int) (shootPoint.y - vel.y));
	    g.setColor(Color.BLACK);

	    // Draw all the entities on the screen
	    for (Entity2D entity : engine.getEntities()) {
		g.setColor(entity.getColor());
		if (entity.isHandling())
		    g.setColor(Color.RED);

		entity.drawEntity(g);

		/* TODO REMOVE DEBUG CODE FOR VISUALIZING ENTITY VELOCITY */
		Vector2D velocity = entity.vel.copy().multiply(5);
		Vector2D center = entity.getCenter();
		g.setColor(Color.RED);
		g.drawLine((int) center.x, (int) center.y, (int) (center.x - velocity.x), (int) (center.y - velocity.y));
		g.setColor(Color.BLACK);
		/* TODO END DEBUG */

	    }
	    // Draw pause menu
	    if (paused) {
		g.setFont(GraphicsTools.MAIN_FONT);
		g.setColor(Color.GRAY);
		g.drawRect(100, 100, width - 200, height - 200);
		g.setColor(GraphicsTools.PANEL_COLOR.darker());
		g.fillRect(100, 100, width - 200, height - 200);
		g.setColor(Color.WHITE);
		int pauseY = 100 + g.getFontMetrics().getAscent();
		GraphicsTools.drawShadowedText(g, "PAUSED", 105, pauseY, 2);
		pauseMenu.drawMenu(g);
	    }
	}
	// Main Menu
	else if (displayScreen == ScreenType.MAIN_MENU) {
	    // Draw the background image
	    g.drawImage(backgroundImage, 0, 0, backgroundImage.getWidth(null), backgroundImage.getHeight(null), 0, 0, width, height, null);

	    // Draw the title, and a slightly transparent box behind it
	    int titleWidth = titleImage.getWidth(null);
	    int titleHeight = titleImage.getHeight(null);
	    g.setColor(GraphicsTools.PANEL_COLOR);
	    g.fillRect(0, 0, width, titleHeight + 20);
	    g.setColor(Color.BLACK);
	    g.drawLine(0, titleHeight + 20, width, titleHeight + 20);
	    g.drawImage(titleImage, width / 2 - titleWidth / 2, 10, null);

	    // Draw the menu
	    mainMenu.drawMenu(g);
	}
	// Options Menu
	else if (displayScreen == ScreenType.OPTIONS_MENU) {
	    // Draw the background image
	    g.drawImage(backgroundImage, 0, 0, backgroundImage.getWidth(null), backgroundImage.getHeight(null), 0, 0, width, height, null);

	    g.setColor(GraphicsTools.PANEL_COLOR);

	    final int topBarHeight = 50;
	    g.fillRect(0, 0, width, topBarHeight);
	    g.setColor(Color.BLACK);
	    g.drawLine(0, topBarHeight, width, topBarHeight);
	    g.setFont(GraphicsTools.MAIN_FONT);
	    g.setColor(Color.WHITE);
	    GraphicsTools.drawShadowedText(g, "Options", 10, 37, 3);

	    optionsMenu.drawMenu(g);
	} else if (displayScreen == ScreenType.LEVEL_SELECT) {
	    // Draw the background image
	    g.drawImage(backgroundImage, 0, 0, backgroundImage.getWidth(null), backgroundImage.getHeight(null), 0, 0, width, height, null);

	    g.setColor(GraphicsTools.PANEL_COLOR);

	    final int topBarHeight = 50;
	    g.fillRect(0, 0, width, topBarHeight);
	    g.setColor(Color.BLACK);
	    g.drawLine(0, topBarHeight, width, topBarHeight);
	    g.setFont(GraphicsTools.MAIN_FONT);
	    g.setColor(Color.WHITE);
	    GraphicsTools.drawShadowedText(g, "Level Select", 10, 37, 3);

	    levelSelectMenu.drawMenu(g);
	}
    }

    /**
     * Loads game resources and images
     */
    public static void loadResources() {
	try {
	    titleImage = ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/title.png"));
	    backgroundImage = ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/background.jpg"));
	    trebuchetImage = ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/trebuchet.png"));
	} catch (Exception e) {
	    TrebuchetDemolition.LOGGER.severe("Failed to load game resources: " + e.getMessage());
	}
    }

    /**
     * (Re)starts the game clock
     */
    public void start() {
	physicsTimer.start();
	displayScreen = ScreenType.IN_GAME;
	paused = false;
    }

    /**
     * Pauses the game clock
     */
    public void pause() {
	physicsTimer.stop();
	paused = true;
    }

    /**
     * Returns to the main menu
     */
    public void stop() {
	physicsTimer.stop();
	displayScreen = ScreenType.MAIN_MENU;
	paused = false;
    }

    /**
     * Causes the physics engine to do a tick, and repaints
     */
    public void tick() {
	engine.update();
	repaint(TICK_RATE);
    }

    /**
     * Changes the angle
     * 
     * @param difference The difference to change
     */
    public void changeAngle(int difference) {
	int wantedChange = angle + difference;
	if (wantedChange >= 0 && wantedChange <= 180) {
	    angle = wantedChange;
	}
    }

    /**
     * Changes the power
     * 
     * @param difference The difference to change
     */
    public void changePower(int difference) {
	int wantedChange = power + difference;
	if (wantedChange >= 0 && wantedChange <= 100) {
	    power = wantedChange;
	}
    }
}
