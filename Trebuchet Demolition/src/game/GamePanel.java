package game;

import game.graphics.GraphicsTools;
import game.graphics.ScreenType;
import game.graphics.menu.Menu;
import game.graphics.menu.MenuItem;
import game.graphics.menu.MenuItemAction;
import game.graphics.menu.ToggleMenuItem;
import game.level.Level;
import game.level.LevelEditor;
import game.level.LevelManager;
import game.physics.PhysicsEngine;
import game.physics.entity.Entity2D;
import game.physics.tasks.GameClockTask;
import game.physics.util.Vector2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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

/**
 * A panel which draws all the shapes and contains a physics engine, and basically contains the entire game
 * 
 * @author Gordon Guan
 * @version Dec 2014
 * 
 */

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    // Define the resources and textures
    public static Image titleImage, backgroundImage, trebuchetImage;
    public static Image metalTexture, brickTexture, rockTexture, debugTexture, ingameTexture;

    public static final int TEXTURE_SIZE = 128;
    /***********************************************************************/
    public PhysicsEngine engine;
    private Timer physicsTimer;
    private Level loadedLevel;

    // Approximately 60 frames per second
    private static final int TICK_RATE = 10;
    private int power = 75;
    private int angle = 50;

    private long lastFireTime = 0;
    private int lastFirePower = 0;

    private boolean paused = false;
    private boolean ctrlIsPressed = false;
    /***********************************************************************/
    private ScreenType displayScreen = ScreenType.MAIN_MENU;

    private final int width, height;
    /***********************************************************************/
    private Menu mainMenu;
    private Menu optionsMenu, levelSelectMenu, pauseMenu;

    private MusicManager musicManager;
    private ToggleMenuItem musicMenuItem;

    /***********************************************************************/

    /**
     * Creates a new game panel
     * @param width The width in pixels
     * @param height The height in pixels
     */
    public GamePanel(final int width, final int height) {
	super();
	this.setBackground(Color.WHITE);
	this.setFocusable(true);

	this.width = width;
	this.height = height;
	
	// Create a new Physics Engine
	engine = new PhysicsEngine(width, height);

	// Load all the images into their containers
	loadResources();

	// Create the game clock
	physicsTimer = new Timer(TICK_RATE, new GameClockTask(this));

	/* From here begins all the menu action and item setup code */
	// Setup actions for each menu action
	MenuItemAction playMenuItemAction = new MenuItemAction() {
	    @Override
	    public void doAction(MenuItem item) {
		LevelManager.loadLevels();
		repopulateLevelSelectMenu();
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
		String help = "<html><h2>Welcome to Trebuchet Demolition!</h2>" + "The goal of this game is to destroy a \"target\" (aka bricks) in a level, in order to complete the level.<br>"
			+ "The game employs physics, so you can bounce objects off walls and such.<br><br><hr>" + "<b>Controls:</b><br>"
			+ "<table style=\"width:100%\"><tr><td>UP DOWN</td><td>Fine control angle</td></tr>" + "<tr><td>LEFT RIGHT</td><td>Fine control power</td>"
			+ "<tr><td>ESC</td><td>Open pause menu</td>" + "<tr><td>SCROLLWHEEL</td><td>Coarse control power</td>"
			+ "<tr><td>CTRL + SCROLLWHEEL</td><td>Coarse control angle</td></table><br><hr>" + "<b>Scoring:</b><br>"
			+ "The game has a scoring system, based on a \"lower score is better\" policy.<br>" + "Your score is determined by the amount of time it takes for you to reach the goal,<br>"
			+ "and the amount of power you launch the projectile with. The less power launched with and the less time,<br>"
			+ "the better your score. The best score will be shown on the level select screen. If your launch fails, then just launch again.</html>";
		JOptionPane.showMessageDialog(null, help, "Trebuchet Demolition Help", JOptionPane.PLAIN_MESSAGE);
	    }
	};
	MenuItemAction aboutMenuItemAction = new MenuItemAction() {
	    public void doAction(MenuItem item) {
		JOptionPane
			.showMessageDialog(
				null,
				"<html>Created by <b>Gordon Guan</b><br>� 2015<br>Block textures from http://webtreats.mysitemyway.com/<br>Music is The Blue Danube Waltz by Johann Strauss II<br><a href=http://github.com/Netdex/Trebuchet-Demolition>GitHub Repository</a></html>",
				"About Trebuchet Demolition", JOptionPane.INFORMATION_MESSAGE);
	    }
	};
	MenuItemAction exitMenuItemAction = new MenuItemAction() {
	    public void doAction(MenuItem item) {
		System.exit(0);
	    }
	};
	MenuItemAction levelEditMenuItemAction = new MenuItemAction(){
	    public void doAction(MenuItem item){
		new LevelEditor().setVisible(true);
	    }
	};

	// Setup all the items in the main menu
	mainMenu = new Menu();
	int y = 150;
	int mheight = 55;
	int mlength = 600;
	int sep = 10;
	int shadowDist = 3;
	MenuItem playMenuItem = new MenuItem("PLAY", GraphicsTools.MAIN_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(10, y, mlength, mheight), playMenuItemAction, shadowDist);
	MenuItem levelEditMenuItem = new MenuItem("LEVEL EDITOR", GraphicsTools.MAIN_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(10, y + mheight + sep, mlength, mheight), levelEditMenuItemAction, shadowDist);
	MenuItem optionsMenuItem = new MenuItem("OPTIONS", GraphicsTools.MAIN_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(10, y + mheight * 2 + sep * 2, mlength, mheight), optionsMenuItemAction,
		shadowDist);
	MenuItem helpMenuItem = new MenuItem("HELP", GraphicsTools.MAIN_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(10, y + mheight * 3 + sep * 3, mlength, mheight), helpMenuItemAction,
		shadowDist);
	MenuItem aboutMenuItem = new MenuItem("ABOUT", GraphicsTools.MAIN_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(10, y + mheight * 4 + sep * 4, mlength, mheight),
		aboutMenuItemAction, shadowDist);
	MenuItem exitMenuItem = new MenuItem("EXIT", GraphicsTools.MAIN_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(10, y + mheight * 5 + sep * 5, mlength, mheight), exitMenuItemAction,
		shadowDist);
	mainMenu.addMenuItem(playMenuItem);
	mainMenu.addMenuItem(levelEditMenuItem);
	mainMenu.addMenuItem(optionsMenuItem);
	mainMenu.addMenuItem(helpMenuItem);
	mainMenu.addMenuItem(aboutMenuItem);
	mainMenu.addMenuItem(exitMenuItem);
	
	// Setup the music toggle button
	MenuItemAction musicMenuItemAction = new MenuItemAction() {
	    public void doAction(MenuItem item) {
		musicMenuItem.toggle();
		repaint();
		if (musicMenuItem.getEnabled()) {
		    musicManager.start();
		    ConfigurationManager.setProperty("music", "true");
		} else {
		    musicManager.stop();
		    ConfigurationManager.setProperty("music", "false");
		}

	    }
	};
	MenuItemAction returnMenuItemAction = new MenuItemAction() {
	    public void doAction(MenuItem item) {
		displayScreen = ScreenType.MAIN_MENU;
		repaint();
	    }
	};
	optionsMenu = new Menu();
	musicMenuItem = new ToggleMenuItem("Music", GraphicsTools.OPTIONS_FONT, Color.WHITE, Color.GREEN, Color.RED, new Rectangle(10, 75, 500, 40), musicMenuItemAction, 2);
	MenuItem returnMenuItem = new MenuItem("Return to Main Menu", GraphicsTools.SMALL_TEXT_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(10, 125, 500, 27), returnMenuItemAction, 1);
	optionsMenu.addMenuItem(musicMenuItem);
	optionsMenu.addMenuItem(returnMenuItem);
	levelSelectMenu = new Menu();
	pauseMenu = new Menu();
	MenuItemAction resumeMenuItemAction = new MenuItemAction() {
	    public void doAction(MenuItem item) {
		start();
		repaint();
	    }
	};
	MenuItem resumeMenuItem = new MenuItem("Resume", GraphicsTools.OPTIONS_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(110, 170, 550, 40), resumeMenuItemAction, 2);
	MenuItem returnFromPauseMenuItem = new MenuItem("Return to Main Menu", GraphicsTools.OPTIONS_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(110, 230, 550, 40), returnMenuItemAction,
		2);
	pauseMenu.addMenuItem(resumeMenuItem);
	pauseMenu.addMenuItem(returnFromPauseMenuItem);
	TrebuchetDemolition.LOGGER.info("Menu population complete");
	/* From here ends all menu action and item setup code */

	// Check if music is enabled, then turn it on if so
	musicManager = new MusicManager();
	if (ConfigurationManager.getBooleanProperty("music")) {
	    musicManager.start();
	    musicMenuItem.setEnabled(true);
	} else {
	    musicMenuItem.setEnabled(false);
	}
	TrebuchetDemolition.LOGGER.info("Loaded music");

	this.addMouseListener(new MouseAdapter() {
	    public void mousePressed(MouseEvent event) {
		// System.out.println(event.getX() + ", " + event.getY());
		if (displayScreen == ScreenType.MAIN_MENU) {
		    mainMenu.invokeAction(event);
		} else if (displayScreen == ScreenType.OPTIONS_MENU) {
		    optionsMenu.invokeAction(event);
		} else if (displayScreen == ScreenType.LEVEL_SELECT) {
		    levelSelectMenu.invokeAction(event);
		} else if (displayScreen == ScreenType.IN_GAME) {
		    if (paused) {
			pauseMenu.invokeAction(event);
		    } else {
			doShot();
		    }
		}
	    }
	});
	this.addMouseWheelListener(new MouseWheelListener() {
	    public void mouseWheelMoved(MouseWheelEvent event) {
		int notches = event.getWheelRotation();
		// Control angle and power with mouse wheel
		if (displayScreen == ScreenType.IN_GAME) {
		    if (ctrlIsPressed)
			changeAngle(notches * 5);
		    else
			changePower(notches * 5);
		}
	    }
	});
	this.addMouseMotionListener(new MouseMotionListener() {
	    @Override
	    public void mouseDragged(MouseEvent event) {

	    }

	    @Override
	    public void mouseMoved(MouseEvent event) {
		// Tell the menus to highlight certain menu items if they are mouse overed
		if (displayScreen == ScreenType.MAIN_MENU) {
		    mainMenu.handleHighlights(event);
		    repaint();
		} else if (displayScreen == ScreenType.OPTIONS_MENU) {
		    optionsMenu.handleHighlights(event);
		    repaint();
		} else if (displayScreen == ScreenType.LEVEL_SELECT) {
		    levelSelectMenu.handleHighlights(event);
		    repaint();
		} else if (displayScreen == ScreenType.IN_GAME) {
		    if (paused) {
			pauseMenu.handleHighlights(event);
			repaint();
		    }
		}

	    }
	});
	this.addKeyListener(new KeyAdapter() {
	    public void keyPressed(KeyEvent event) {
		int keycode = event.getKeyCode();
		// Define all the keyboard controls
		if (keycode == KeyEvent.VK_CONTROL)
		    ctrlIsPressed = true;
		// Respond to ingame events
		if (displayScreen == ScreenType.IN_GAME) {
		    if (keycode == KeyEvent.VK_RIGHT) {
			changePower(2);
		    } else if (keycode == KeyEvent.VK_LEFT) {
			changePower(-2);
		    } else if (keycode == KeyEvent.VK_UP) {
			changeAngle(2);
		    } else if (keycode == KeyEvent.VK_DOWN) {
			changeAngle(-2);
		    } else if (keycode == KeyEvent.VK_SPACE) {
			doShot();
		    } else if (keycode == KeyEvent.VK_0) {
			engine.removeLastProjectile();
		    } else if (keycode == KeyEvent.VK_ESCAPE) {
			if (paused)
			    start();
			else
			    pause();
			repaint();
		    }
		}
	    }

	    public void keyReleased(KeyEvent event) {
		int keycode = event.getKeyCode();
		if (keycode == KeyEvent.VK_CONTROL) {
		    ctrlIsPressed = false;
		}
	    }
	});
	TrebuchetDemolition.LOGGER.info("Loaded listeners");
    }

    /**
     * Fires a projectile from the trebuchet
     */
    public void doShot() {
	engine.removeLastProjectile();
	lastFireTime = System.currentTimeMillis();
	loadedLevel.setScore(0);
	engine.fireProjectile(power, angle);
	lastFirePower = power;
	TrebuchetDemolition.LOGGER.info("Fired shot");
    }

    /**
     * Deletes all the items in the level select menu and loads them all from LevelManager
     */
    public void repopulateLevelSelectMenu() {
	levelSelectMenu.clearMenu();
	int y = 75;
	int mheight = 25;
	int mlength = 600;
	int sep = 10;
	// Loop through every level
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
	    // Load highscores
	    String highscore = "Has not been played";
	    if (level.getMetadata().getProperty("highscore") != null) {
		highscore = "Highscore: " + level.getMetadata().getProperty("highscore");
	    }
	    String menuItemText = String.format("%-25s", level.getName().substring(0, Math.min(level.getName().length(), 40))) + String.format("%-20s", level.getFile().getName())
		    + String.format("%20s", highscore);
	    MenuItem menuItem = new MenuItem(menuItemText, GraphicsTools.LEVEL_SELECT_FONT, Color.WHITE, GraphicsTools.BG_COLOR.darker().darker(), new Rectangle(10, y, mlength, mheight),
		    levelSelectAction, 1);
	    levelSelectMenu.addMenuItem(menuItem);
	    y += mheight + sep;
	}
	y += mheight + sep;
	levelSelectMenu.addMenuItem(new MenuItem("Return to Main Menu", GraphicsTools.SMALL_TEXT_FONT, Color.WHITE, GraphicsTools.BG_COLOR, new Rectangle(10, y, mlength, mheight),
		new MenuItemAction() {
		    public void doAction(MenuItem item) {
			displayScreen = ScreenType.MAIN_MENU;
			repaint();
		    }
		}, 1));
	TrebuchetDemolition.LOGGER.info("Redrew level select menu");
    }

    /**
     * Load a level into the GamePanel, then into the PhysicsEngine
     * @param level The level to load
     */
    public void loadLevel(Level level) {
	try {
	    // if (level.getMetadata().getProperty("bgcolor") != null) {
	    // String[] colorRGB = level.getMetadata().getProperty("bgcolor").replaceAll(" ", "").split(",");
	    // Color color = new Color(Integer.parseInt(colorRGB[0]), Integer.parseInt(colorRGB[1]), Integer.parseInt(colorRGB[2]));
	    // this.setBackground(color);
	    // } else {
	    // this.setBackground(Color.WHITE);
	    // }
	} catch (Exception e) {
	    JOptionPane.showMessageDialog(null, "An error occured while loading a level: " + e.getMessage(), "Level Loading Error", JOptionPane.ERROR_MESSAGE);
	    TrebuchetDemolition.LOGGER.warning("Failed to load the level selected: " + e.getMessage());
	    return;
	}
	loadedLevel = level;
	lastFireTime = -1;
	TrebuchetDemolition.LOGGER.info("Loaded level \"" + level.getName() + "\" into Game Panel");
	engine.loadLevel(level);

    }

    /**
     * Checks for a win, then takes suitable action
     */
    public void checkWin() {
	if (engine.hasWon()) {
	    String winMessage = "You have won with a score of " + loadedLevel.getScore() + "!";
	    String highscore = loadedLevel.getMetadata().getProperty("highscore");
	    if (highscore == null) {
		winMessage += " You set a new highscore!";
	    } else if (Integer.parseInt(highscore) > loadedLevel.getScore()) {
		int diff = Integer.parseInt(highscore) - loadedLevel.getScore();
		winMessage += " You beat the highscore of " + highscore + " by " + diff + " points!";
	    }
	    JOptionPane.showMessageDialog(null, winMessage, "Level Clear", JOptionPane.INFORMATION_MESSAGE);
	    stop();
	    engine.clearAll();
	    // Reset the level and scores
	    lastFireTime = -1;
	    loadedLevel.save();
	    loadedLevel.setScore(0);
	    try {
		loadedLevel.loadEntities();
	    } catch (Exception e) {
		TrebuchetDemolition.LOGGER.warning("Failed to load entities: " + e.getMessage());
	    }
	    loadedLevel = null;
	    lastFirePower = 0;
	    displayScreen = ScreenType.LEVEL_SELECT;

	    repopulateLevelSelectMenu();
	    repaint();
	    TrebuchetDemolition.LOGGER.info("Player has won a level");
	}
    }

    /**
     * Calculates and sets the score based on a predetermined algorithm based on time and power
     */
    public void doScore() {
	if (loadedLevel != null) {
	    if (lastFireTime == -1) // Projectile never fired
		loadedLevel.setScore(0);
	    else
		loadedLevel.setScore((int) ((System.currentTimeMillis() - lastFireTime) * lastFirePower * lastFirePower)/100000);
	}
    }

    /**
     * Draws the menus, game, basically everything
     */
    @Override
    public void paintComponent(Graphics graphics) {
	super.paintComponent(graphics);

	Graphics2D g = (Graphics2D) graphics;
	// Make drawing less jagged
	RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g.setRenderingHints(rh);
	g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

	// Draw the ingame graphics
	if (displayScreen == ScreenType.IN_GAME) {
	    // Draw background textures
	    g.drawImage(ingameTexture, 0, 0, width, height, 0, 0, ingameTexture.getWidth(null), ingameTexture.getHeight(null), null);

	    g.setColor(Color.RED);
	    g.fillRect(10, 7, (int) (164 * power / 100.0), 15);
	    g.setColor(Color.BLACK);
	    g.drawRect(10, 7, 164, 15);
	    g.setFont(GraphicsTools.DETAIL_FONT);
	    g.setColor(Color.WHITE);
	    g.drawString("Power", 13, 19);

	    g.setFont(GraphicsTools.MAIN_FONT);
	    g.setColor(Color.WHITE);

	    /* Begin score drawing */
	    String score = loadedLevel.getScore() + "";
	    int widthScore = g.getFontMetrics().stringWidth(score);
	    int scoreX = width - widthScore - 10;
	    int scoreY = g.getFontMetrics().getAscent();
	    GraphicsTools.drawShadowedText(g, loadedLevel.getScore() + "", scoreX, scoreY, 2);
	    GraphicsTools.drawShadowedText(g, loadedLevel.getScore() + "", scoreX, scoreY, -2);
	    /* End score drawing */

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
		entity.drawEntity(g);
	    }
	    // Draw pause menu
	    if (paused) {
		g.setFont(GraphicsTools.MAIN_FONT);
		g.setColor(Color.GRAY);
		g.drawRect(100, 100, width - 200, height - 200);
		g.setColor(GraphicsTools.PANEL_COLOR.darker());
		g.fillRect(100, 100, width - 200, height - 200);
		g.setColor(GraphicsTools.PANEL_COLOR.brighter());
		g.fillRect(100, 100, width - 200, g.getFontMetrics().getAscent() + 10);
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
	    metalTexture = ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/metal.jpg"));
	    ingameTexture = ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/ingamebg.jpg"));
	    brickTexture = ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/brick.jpg"));
	    rockTexture = ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/wood.jpg"));
	    debugTexture = ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/debug.jpg"));
	    TrebuchetDemolition.LOGGER.info("Loaded resources");
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
	TrebuchetDemolition.LOGGER.info("Game clock has started");
    }

    /**
     * Pauses the game clock
     */
    public void pause() {
	physicsTimer.stop();
	paused = true;
	TrebuchetDemolition.LOGGER.info("Game clock has paused");
    }

    /**
     * Returns to the main menu
     */
    public void stop() {
	physicsTimer.stop();
	displayScreen = ScreenType.MAIN_MENU;
	paused = false;
	TrebuchetDemolition.LOGGER.info("Game clock has stopped");
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
	angle = Math.min(Math.max(wantedChange, 0), 180);
    }

    /**
     * Changes the power
     * 
     * @param difference The difference to change
     */
    public void changePower(int difference) {
	int wantedChange = power + difference;
	power = Math.min(Math.max(wantedChange, 0), 100);
    }
}
