package game;

import game.graphics.GraphicsTools;
import game.graphics.menu.AlignedMenu;
import game.graphics.menu.CenteredMenu;
import game.graphics.menu.MenuItem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

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
import tasks.PanoramaTask;

/**
 * A panel which draws all the shapes and contains a physics engine
 * 
 * @author Gordon Guan
 * @version Dec 2014
 * 
 */
public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public PhysicsEngine engine;

    // Approximately 60 frames per second
    private static final int TICK = 16;
    private int power = 75;
    private int angle = 50;

    private static final Font mainMenuItemFont = new Font("Optima", Font.BOLD, 40);
    
    /**
     * SCREEN NUMBER ALLOCATIONS
     * Note: This is a Java Doc comment since they allow line breaking<br>
     * Stores the screen which should be painted<br>
     * 0. Main Menu<br>
     * 1. In-game<br>
     * 2. Overlay Pause Menu<br>
     * 3. Options<br>
     */
    private ScreenType displayScreen = ScreenType.MAIN_MENU;

    private int width;
    private int height;

    private Timer gameTimer;
    
    private CenteredMenu mainMenu;
    private AlignedMenu optionsMenu;
    
    private ArrayList<Image> resources;

    public GamePanel(final int width, final int height) {
	super();
	this.setBackground(Color.WHITE);
	this.setFocusable(true);

	this.width = width;
	this.height = height;
	engine = new PhysicsEngine(width, height);

	// Load all the images
	resources = getResources();

	gameTimer = new Timer(TICK, new GameClockTask(this));
	
	mainMenu = new CenteredMenu();
	mainMenu.addMenuItem(new MenuItem("PLAY", mainMenuItemFont, Color.GRAY));
	mainMenu.addMenuItem(new MenuItem("OPTIONS", mainMenuItemFont, Color.GRAY));
	mainMenu.addMenuItem(new MenuItem("EXIT", mainMenuItemFont, Color.GRAY));
	
	Font optionsFont = new Font("Optima", Font.BOLD, 27);
	optionsMenu = new AlignedMenu();
	optionsMenu.addMenuItem(new MenuItem("Music", optionsFont, Color.GRAY));
	optionsMenu.addMenuItem(new MenuItem("Exit", optionsFont, Color.GRAY));
	
	this.addMouseListener(new MouseAdapter() {
	    public void mousePressed(MouseEvent event) {

	    }
	});

	this.addKeyListener(new KeyAdapter() {
	    public void keyPressed(KeyEvent event) {
		int keycode = event.getKeyCode();
		if (displayScreen == ScreenType.IN_GAME) {
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
			int selected = mainMenu.getSelectedItem();
			if (selected == 0) {
			    start();
			} else if (selected == 1) {
			    displayScreen = ScreenType.OPTIONS_MENU;
			} else if (selected == 2) {
			    int accept = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			    if (accept == JOptionPane.YES_OPTION)
				System.exit(0);
			}
			repaint();
		    }
		}
		// Respond to events in options menu
		else if (displayScreen == ScreenType.OPTIONS_MENU) {
		    // Return to main menu
		    if (keycode == KeyEvent.VK_ESCAPE) {
			displayScreen = ScreenType.MAIN_MENU;
			repaint();
		    }
		    else if(keycode == KeyEvent.VK_UP){
			optionsMenu.shiftUp();
			repaint();
		    }
		    else if(keycode == KeyEvent.VK_DOWN){
			optionsMenu.shiftDown();
			repaint();
		    }
		    else if(keycode == KeyEvent.VK_ENTER){
			int selectedIndex = optionsMenu.getSelectedItem();
			// Music
			if(selectedIndex == 0){
			    
			}
			// Return to main menu
			else if(selectedIndex == 1){
			    displayScreen = ScreenType.MAIN_MENU;
			}
		    }
		}
	    }
	});
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
	    g.drawString("Entities: " + engine.getEntities().size(), 10, 15);
	    g.drawString("Collisions: " + engine.collisionsInTick, 10, 30);
	    g.drawString("[GAME VARIABLES]: ", 10, 65);
	    g.drawString("Power: " + power + "%", 10, 80);
	    g.drawString("Angle: " + angle + "°", 10, 95);

	    // Draw all the entities on the screen
	    for (Entity entity : engine.getEntities()) {
		g.setColor(entity.getColor());
		Shape shape = null;

		if (entity instanceof Circle) {
		    Circle c = (Circle) entity;
		    Vector loc = c.loc;
		    int radius = c.getRadius();
		    shape = new Ellipse2D.Double(loc.x - radius, loc.y - radius, radius * 2, radius * 2);
		    g.draw(shape);
		} else if (entity instanceof AABB) {
		    AABB a = (AABB) entity;
		    shape = new Rectangle2D.Double(a.p1.x, a.p1.y, a.getWidth(), a.getHeight());
		    g.draw(shape);
		} else if (entity instanceof Rectangle) {
		    Rectangle rect = (Rectangle) entity;
		    System.out.println(rect.p1.x + " " + rect.p1.y + " " + rect.p2.x + " " + rect.p2.y);
		    g.drawLine((int) rect.p1.x, (int) rect.p1.y, (int) rect.p1.x, (int) rect.p2.y);
		    g.drawLine((int) rect.p1.x, (int) rect.p1.y, (int) rect.p1.y, (int) rect.p2.x);
		    g.drawLine((int) rect.p2.x, (int) rect.p2.y, (int) rect.p1.x, (int) rect.p2.y);
		    g.drawLine((int) rect.p2.x, (int) rect.p2.y, (int) rect.p2.y, (int) rect.p1.x);
		}
	    }
	}
	// Main Menu
	else if (displayScreen == ScreenType.MAIN_MENU) {
	    // Get the images for easier access
	    Image title = resources.get(0);
	    
	    // Draw the title, and a slightly transparent box behind it
	    int titleWidth = title.getWidth(null);
	    int titleHeight = title.getHeight(null);
	    g.setColor(new Color(255, 218, 117, 128));
	    g.fillRect(0, 0, width, titleHeight + 20);
	    g.setColor(Color.BLACK);
	    g.drawLine(0, titleHeight + 20, width, titleHeight + 20);
	    g.drawImage(title, width / 2 - titleWidth / 2, 10, null);

	    // Draw the menu
	    mainMenu.drawMenu(g, width, height, 250, 75);
	}
	// Options Menu
	else if (displayScreen == ScreenType.OPTIONS_MENU) {

	    // Draw the background image

	    g.setColor(new Color(255, 218, 117, 128));

	    final int topBarHeight = 50;
	    g.fillRect(0, 0, width, topBarHeight);
	    g.setColor(Color.BLACK);
	    g.drawLine(0, topBarHeight, width, topBarHeight);
	    g.setFont(mainMenuItemFont);
	    g.setColor(Color.WHITE);
	    GraphicsTools.drawShadowedText(g, "Options", 10, 37);
	    
	    optionsMenu.drawMenu(g, 10, 0, 120, 50);
	}

    }

    /**
     * Loads game resources and images<br>
     * Indices are as follows:<br>
     * 0. Title Logo<br>
     * 
     * @return An ArrayList of all the resources
     */
    public static ArrayList<Image> getResources() {
	ArrayList<Image> images = new ArrayList<Image>();
	try {
	    images.add(ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/title.png")));
	} catch (Exception e) {

	}
	return images;
    }

    /**
     * (Re)starts the game clock
     */
    public void start() {
	gameTimer.start();
	displayScreen = ScreenType.IN_GAME;
    }

    /**
     * Pauses the game clock
     */
    public void pause() {
	gameTimer.stop();
	displayScreen = ScreenType.PAUSE_MENU;
    }

    /**
     * Returns to the main menu
     */
    public void stop() {
	gameTimer.stop();
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
