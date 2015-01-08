package game;

import game.graphics.GraphicsTools;
import game.graphics.ScreenType;
import game.graphics.menu.AlignedMenu;
import game.graphics.menu.CenteredMenu;
import game.graphics.menu.MenuItem;
import game.graphics.menu.ToggleMenuItem;
import game.level.Level;
import game.level.LevelManager;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import physics.PhysicsEngine;
import physics.entity.AABB;
import physics.entity.Circle;
import physics.entity.Entity;
import physics.entity.Rectangle;
import physics.util.Vector;
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
    public static Image titleImage;
    public static Image backgroundImage;

    /***********************************************************************/
    public PhysicsEngine engine;
    private Timer physicsTimer;

    // Approximately 60 frames per second
    private static final int TICK_RATE = 10;
    private int power = 75;
    private int angle = 50;

    private boolean paused = false;
    /***********************************************************************/
    private ScreenType displayScreen = ScreenType.MAIN_MENU;

    private final int width;
    private final int height;
    /***********************************************************************/
    private CenteredMenu mainMenu;
    private AlignedMenu optionsMenu;
    private AlignedMenu levelSelectMenu;
    private AlignedMenu pauseMenu;

    private ToggleMenuItem musicToggleMenuItem = new ToggleMenuItem("Music", GraphicsTools.OPTIONS_FONT, Color.GREEN, Color.RED);

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

	mainMenu = new CenteredMenu(3);
	mainMenu.addMenuItem(new MenuItem("PLAY", GraphicsTools.MAIN_FONT, Color.GRAY));
	mainMenu.addMenuItem(new MenuItem("OPTIONS", GraphicsTools.MAIN_FONT, Color.GRAY));
	mainMenu.addMenuItem(new MenuItem("HELP", GraphicsTools.MAIN_FONT, Color.GRAY));
	mainMenu.addMenuItem(new MenuItem("ABOUT", GraphicsTools.MAIN_FONT, Color.GRAY));
	mainMenu.addMenuItem(new MenuItem("EXIT", GraphicsTools.MAIN_FONT, Color.GRAY));

	optionsMenu = new AlignedMenu(2);
	optionsMenu.addMenuItem(musicToggleMenuItem);
	musicToggleMenuItem.setEnabled(true);
	optionsMenu.addMenuItem(new MenuItem("Return to Main Menu", GraphicsTools.OPTIONS_FONT, Color.GRAY));

	levelSelectMenu = new AlignedMenu(1);

	pauseMenu = new AlignedMenu(1);
	pauseMenu.addMenuItem(new MenuItem("Resume", GraphicsTools.OPTIONS_FONT, Color.GRAY));
	pauseMenu.addMenuItem(new MenuItem("Return to Main Menu", GraphicsTools.OPTIONS_FONT, Color.GRAY));

	this.addMouseListener(new MouseAdapter() {
	    public void mousePressed(MouseEvent event) {

	    }
	});

	this.addKeyListener(new KeyAdapter() {
	    public void keyPressed(KeyEvent event) {
		int keycode = event.getKeyCode();
		if (displayScreen == ScreenType.IN_GAME) {
		    if (paused) {
			if (keycode == KeyEvent.VK_UP) {
			    pauseMenu.shiftUp();
			} else if (keycode == KeyEvent.VK_DOWN) {
			    pauseMenu.shiftDown();
			} else if (keycode == KeyEvent.VK_ESCAPE) {
			    start();
			}
		    } else {
			if (keycode == KeyEvent.VK_RIGHT) {
			    if (power <= 95)
				power += 5;
			} else if (keycode == KeyEvent.VK_LEFT) {
			    if (power >= 5)
				power -= 5;
			} else if (keycode == KeyEvent.VK_UP) {
			    if (angle <= 175)
				angle += 5;
			} else if (keycode == KeyEvent.VK_DOWN) {
			    if (angle >= 5)
				angle -= 5;
			} else if (keycode == KeyEvent.VK_SPACE) {
			    int vecX = (int) (Math.cos(Math.toRadians(180 - angle)) * power / 10);
			    int vecY = (int) (Math.sin(Math.toRadians(180 - angle)) * power / 10);
			    Vector vel = new Vector(vecX, vecY);

			    Circle c = new Circle(new Vector(50, height - 50), vel, new Vector(0, 0), 10, Color.BLACK);
			    engine.addEntity(c);
			} else if (keycode == KeyEvent.VK_0) {
			    engine.clearAll();
			} else if (keycode == KeyEvent.VK_ESCAPE) {
			    pause();
			    repaint();
			}
		    }
		}
		// Respond to menu events, such as allowing arrow keys to select
		// menu items
		else if (displayScreen == ScreenType.MAIN_MENU) {
		    if (keycode == KeyEvent.VK_UP) {
			mainMenu.shiftUp();
			repaint();
		    } else if (keycode == KeyEvent.VK_DOWN) {
			mainMenu.shiftDown();
			repaint();
		    } else if (keycode == KeyEvent.VK_ENTER || keycode == KeyEvent.VK_SPACE) {
			int selectedIndex = mainMenu.getSelectedItem();
			if (selectedIndex == 0) {
			    LevelManager.loadLevels();
			    levelSelectMenu.clearMenu();
			    for (Level level : LevelManager.getLevels()) {
				MenuItem menuItem = new MenuItem(level.getName() + " - " + level.getFile().getName(), GraphicsTools.LEVEL_SELECT_FONT, Color.GRAY);
				levelSelectMenu.addMenuItem(menuItem);
			    }
			    levelSelectMenu.addMenuItem(new MenuItem("Return to Main Menu", GraphicsTools.LEVEL_SELECT_FONT, Color.GRAY));
			    displayScreen = ScreenType.LEVEL_SELECT;
			} else if (selectedIndex == 1) {
			    displayScreen = ScreenType.OPTIONS_MENU;
			} else if (selectedIndex == 2) {
			    JOptionPane.showMessageDialog(null, "Use the up and down arrow keys to traverse the menus.\n" + "Use the enter key to select a menu item.\n\n"
				    + "Use the scroll wheel to increase power.", "Trebuchet Demolition Help", JOptionPane.INFORMATION_MESSAGE);
			} else if (selectedIndex == 3) {
			    JOptionPane.showMessageDialog(null, "Created by Gordon Guan\n(c) 2015 \n \"The essence of OOP\"", "About Trebuchet Demolition", JOptionPane.INFORMATION_MESSAGE);
			} else if (selectedIndex == 4) {
			    int accept = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			    if (accept == JOptionPane.YES_OPTION)
				System.exit(0);
			}
			repaint();
		    } else if (keycode == KeyEvent.VK_F10) {
			System.out.println("DEBUG MODE");
			start();
			repaint();
		    }
		}
		// Respond to events in options menu
		else if (displayScreen == ScreenType.OPTIONS_MENU) {
		    // Return to main menu
		    if (keycode == KeyEvent.VK_ESCAPE) {
			displayScreen = ScreenType.MAIN_MENU;
			repaint();
		    } else if (keycode == KeyEvent.VK_UP) {
			optionsMenu.shiftUp();
			repaint();
		    } else if (keycode == KeyEvent.VK_DOWN) {
			optionsMenu.shiftDown();
			repaint();
		    } else if (keycode == KeyEvent.VK_ENTER) {
			int selectedIndex = optionsMenu.getSelectedItem();
			// Music
			if (selectedIndex == 0) {
			    musicToggleMenuItem.toggle();
			}
			// Return to main menu
			else if (selectedIndex == 1) {
			    displayScreen = ScreenType.MAIN_MENU;
			}
			repaint();
		    }
		} else if (displayScreen == ScreenType.LEVEL_SELECT) {
		    if (keycode == KeyEvent.VK_ESCAPE) {
			displayScreen = ScreenType.MAIN_MENU;
			repaint();
		    } else if (keycode == KeyEvent.VK_UP) {
			levelSelectMenu.shiftUp();
			repaint();
		    } else if (keycode == KeyEvent.VK_DOWN) {
			levelSelectMenu.shiftDown();
			repaint();
		    } else if (keycode == KeyEvent.VK_ENTER) {
			int selectedIndex = levelSelectMenu.getSelectedItem();

			// Check if the selected MenuItem is the last one, which is return to main menu
			if (selectedIndex == LevelManager.getLevels().size()) {
			    displayScreen = ScreenType.MAIN_MENU;
			    repaint();
			} else {
			    Level level = LevelManager.getLevels().get(selectedIndex);
			    loadLevel(level);
			    
			    start();
			    repaint();
			}

		    }
		}
	    }
	});

	// DEBUG CODE
	 final Rectangle rect = new Rectangle(new Vector(100, 10), new Vector(200, 300), Color.BLACK);
	 engine.addEntity(rect);
	// END DEBUG CODE
    }

    public void loadLevel(Level level){
	try{
	if(level.getMetadata().getProperty("bgcolor") != null){
	    String[] colorRGB = level.getMetadata().getProperty("bgcolor").replaceAll(" ", "").split(",");
	    Color color = new Color(Integer.parseInt(colorRGB[0]), Integer.parseInt(colorRGB[1]), Integer.parseInt(colorRGB[2]));
	    this.setBackground(color);
	}
	}catch(Exception e){
	    JOptionPane.showMessageDialog(null, "An error occured while loading a level: " + e.getMessage(), "Level Loading Error", JOptionPane.ERROR_MESSAGE);
	    return;
	}
	engine.loadLevel(level);
    }
    /**
     * Draws the game
     */
    @Override
    public void paintComponent(Graphics graphics) {
	super.paintComponent(graphics);

	Graphics2D g = (Graphics2D) graphics;
	RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	g.setRenderingHints(rh);

	// Playing game
	if (displayScreen == ScreenType.IN_GAME) {
	    // Draw debug info
	    g.setColor(Color.LIGHT_GRAY);
	    g.fillRect(0, 0, 110, 100);
	    g.setColor(Color.BLACK);
	    g.drawString("Entities: " + engine.getEntities().size(), 10, 15);
	    g.drawString("Collisions: " + engine.collisionsInTick, 10, 30);
	    g.drawString("[GAME VARIABLES]: ", 10, 65);
	    g.drawString("Power: " + power + "%", 10, 80);
	    g.drawString("Angle: " + angle + "°", 10, 95);

	    // Draw all the entities on the screen
	    for (Entity entity : engine.getEntities()) {
		g.setColor(entity.getColor());
		if (entity.isHandling())
		    g.setColor(Color.RED);
		Shape shape = null;

		if (entity instanceof Circle) {
		    Circle c = (Circle) entity;
		    Vector loc = c.loc;
		    int radius = c.getRadius();
		    shape = new Ellipse2D.Double(loc.x - radius, loc.y - radius, radius * 2, radius * 2);
		    g.fill(shape);
		} else if (entity instanceof AABB) {
		    AABB a = (AABB) entity;
		    shape = new Rectangle2D.Double(a.p1.x, a.p1.y, a.getWidth(), a.getHeight());
		    g.draw(shape);
		} else if (entity instanceof Rectangle) {
		    Rectangle rect = (Rectangle) entity;

		    int xPoly[] = { (int) rect.p1.x, (int) rect.p2.x, (int) rect.p4.x, (int) rect.p3.x };
		    int yPoly[] = { (int) rect.p1.y, (int) rect.p2.y, (int) rect.p4.y, (int) rect.p3.y };

		    Polygon poly = new Polygon(xPoly, yPoly, xPoly.length);
		    g.fill(poly);

		    /* DEBUG TODO REMOVE DEBUG CODE */
		    AABB bounds = rect.getBoundingBox();
		    g.setColor(Color.GREEN);
		    g.drawRect((int) bounds.p1.x, (int) bounds.p1.y, (int) bounds.getWidth(), (int) bounds.getHeight());
		    /* END DEBUG */
		}
		if (paused) {
		    g.setFont(GraphicsTools.MAIN_FONT);
		    g.setColor(Color.GRAY);
		    GraphicsTools.drawShadowedText(g, "PAUSED", 100, 100, 2);
		}
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
	    mainMenu.drawMenu(g, width, height, 220, 60);
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

	    optionsMenu.drawMenu(g, 10, 0, 120, 50);
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

	    levelSelectMenu.drawMenu(g, 10, 0, 80, 25);
	}
    }

    /**
     * Loads game resources and images
     */
    public static void loadResources() {
	try {
	    titleImage = ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/title.png"));
	    backgroundImage = ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/background.jpg"));
	} catch (Exception e) {
	    e.printStackTrace();
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
    }

    /**
     * Causes the physics engine to do a tick, and repaints
     */
    public void tick() {
	engine.tick();
	repaint();
    }
}
