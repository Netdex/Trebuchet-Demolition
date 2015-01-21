package game.level;

import game.TrebuchetDemolition;
import game.physics.PhysicsEngine;
import game.physics.entity.Entity2D;
import game.physics.entity.Rectangle2D;
import game.physics.entity.Target2D;
import game.physics.util.Vector2D;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 * A level editor for Trebuchet Demolition
 * 
 * @author Gordon Guan
 * @version Jan 2015
 */
public class LevelEditor extends JFrame
{
	private static final Cursor CROSSHAIR_CURSOR = Cursor
			.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	private static final Cursor NORMAL_CURSOR = Cursor
			.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private static final Cursor HAND_CURSOR = Cursor
			.getPredefinedCursor(Cursor.HAND_CURSOR);

	private JFrame levelEditor = this;
	private JPanel contentPane;
	private PhysicsEngine virtualEngine;
	private Entity2D selectedEntity;

	private Point lastDragClick;
	private Point lastClick;
	private int creating = -1;

	public LevelEditor()
	{
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				LevelEditor.class.getResource("/resources/trebuchet.png")));
		setTitle("Trebuchet Demolition Level Editor");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1097, 643);

		this.setFocusable(true);

		// Add all components
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmNew);

		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmLoad);

		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);

		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				InputEvent.CTRL_MASK));
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmHowToUse = new JMenuItem("How to Use");
		mntmHowToUse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane
						.showMessageDialog(
								null,
								"<html><h2>Trebuchet Demolition Level Editor Help</h2>Select an entity type from the side or by pressing the key shortcuts, then drag and release to draw the entity.<br>To move the entity, just drag it around.<br>Controls are available on the side, which adjust the positioning further. In order for a level to load successfully, <b>it must have a target</b>.",
								"Trebuchet Demolition Level Editor Help",
								JOptionPane.PLAIN_MESSAGE);
			}
		});
		mnHelp.add(mntmHowToUse);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(5, 5, 1081, 578);
		contentPane.add(splitPane);

		final JPanel drawPanel = new JPanel() {
			@Override
			/**
			 * Draw the level editor
			 */
			public void paintComponent(Graphics graphics)
			{
				super.paintComponent(graphics);
				Graphics2D g = (Graphics2D) graphics;
				RenderingHints rh = new RenderingHints(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHints(rh);
				g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
						RenderingHints.VALUE_COLOR_RENDER_QUALITY);
				// Draw every entity from the virtual physics engine
				for (Entity2D e : virtualEngine.getEntities())
				{
					e.drawEntity(g);
					if (e.equals(selectedEntity))
					{
						g.setColor(Color.RED);
						g.draw(e.getShape());
						g.setColor(Color.BLACK);
					}
				}
				// If an entity is being created, then draw the traced box
				if (creating != -1)
				{
					g.setColor(Color.LIGHT_GRAY);
					int width = lastDragClick.x - lastClick.x;
					int height = lastDragClick.y - lastClick.y;

					int x = lastClick.x;
					int y = lastClick.y;

					if (width < 0)
					{
						x = lastClick.x + width;
					}
					if (height < 0)
					{
						y = lastClick.y + height;
					}
					g.drawRect(x, y, Math.abs(width), Math.abs(height));
				}
			}
		};
		drawPanel.setBackground(Color.WHITE);
		drawPanel.setForeground(Color.WHITE);
		drawPanel.setMinimumSize(new Dimension(900, 600));
		splitPane.setLeftComponent(drawPanel);

		// Create a virtual physics engine which contains all the entities, but
		// don't update() it so no physics actually occurs
		virtualEngine = new PhysicsEngine(900, 600);

		JPanel controlPanel = new JPanel();
		splitPane.setRightComponent(controlPanel);
		controlPanel.setLayout(null);

		JLabel lblAdd = new JLabel("Add Entity:");
		lblAdd.setBounds(10, 11, 59, 14);
		controlPanel.add(lblAdd);

		JButton btnRectangle = new JButton("Rectangle [r]");
		btnRectangle.setBounds(10, 29, 152, 23);
		controlPanel.add(btnRectangle);

		JLabel lblRotateEntity = new JLabel("Rotate Entity:");
		lblRotateEntity.setBounds(10, 91, 75, 14);
		controlPanel.add(lblRotateEntity);

		final JButton btnCcw = new JButton("CCW -");
		btnCcw.setEnabled(false);
		btnCcw.setBounds(10, 109, 75, 23);
		controlPanel.add(btnCcw);

		final JButton btnCw = new JButton("CW +");
		btnCw.setEnabled(false);
		btnCw.setBounds(87, 109, 75, 23);
		controlPanel.add(btnCw);

		final JButton btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		btnDelete.setBounds(10, 153, 152, 23);
		controlPanel.add(btnDelete);

		JButton btnTarget = new JButton("Target [t]");
		btnTarget.setBounds(10, 51, 152, 23);
		controlPanel.add(btnTarget);

		JButton btnClearAll = new JButton("Clear All");
		btnClearAll.setBackground(Color.RED);
		btnClearAll.setBounds(10, 215, 152, 33);
		controlPanel.add(btnClearAll);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 140, 152, 2);
		controlPanel.add(separator);

		addKeyListener(new KeyAdapter() {
			@Override
			/**
			 * Listen for key presses for shortcuts
			 */
			public void keyPressed(KeyEvent event)
			{
				int keycode = event.getKeyCode();
				if (keycode == KeyEvent.VK_DELETE)
				{
					if (selectedEntity != null)
					{
						virtualEngine.removeEntity(selectedEntity);
						drawPanel.repaint();
					}
				}
				else if (keycode == KeyEvent.VK_R)
				{
					creating = 0;
					drawPanel.setCursor(CROSSHAIR_CURSOR);
				}
				else if (keycode == KeyEvent.VK_T)
				{
					creating = 1;
					drawPanel.setCursor(CROSSHAIR_CURSOR);
				}
			}
		});

		drawPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event)
			{
				Point point = event.getPoint();
				Entity2D entity = null;
				// Check if any entities are inside the click, if there are then
				// start dragging them
				for (Entity2D e : virtualEngine.getEntities())
				{
					Shape shape = e.getShape();
					if (shape.contains(point))
					{
						entity = e;
					}
				}

				// Set the buttons to enabled or disabled based on if an entity
				// is selected
				if (entity != null)
				{
					btnDelete.setEnabled(true);
					btnCcw.setEnabled(true);
					btnCw.setEnabled(true);
				}
				else
				{
					btnDelete.setEnabled(false);
					btnCcw.setEnabled(false);
					btnCw.setEnabled(false);
				}
				selectedEntity = entity;
				lastDragClick = point;
				lastClick = point;
				drawPanel.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent event)
			{
				// Find the position of the click
				Point point = event.getPoint();
				int width = point.x - lastClick.x;
				int height = point.y - lastClick.y;

				int x = lastClick.x;
				int y = lastClick.y;

				if (width < 0)
				{
					x = lastClick.x + width;
				}
				if (height < 0)
				{
					y = lastClick.y + height;
				}
				Vector2D p1 = new Vector2D(x, y);
				Vector2D p2 = new Vector2D(x + Math.abs(width), y
						+ Math.abs(height));
				// If an object is being created, then create it
				if (creating == 0)
				{
					Rectangle2D rect = new Rectangle2D(p1, p2);
					virtualEngine.addEntity(rect);
					creating = -1;
					drawPanel.setCursor(NORMAL_CURSOR);
					drawPanel.repaint();
				}
				else if (creating == 1)
				{
					Target2D targ = new Target2D(p1, p2);
					virtualEngine.addEntity(targ);
					creating = -1;
					drawPanel.setCursor(NORMAL_CURSOR);
					drawPanel.repaint();
				}
			}
		});
		drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent event)
			{
				// Update the drawing outline
				Point current = event.getPoint();
				if (selectedEntity != null)
				{
					Point diff = new Point(current.x - lastDragClick.x,
							current.y - lastDragClick.y);
					selectedEntity.translate(diff.x, diff.y);
					lastDragClick = current;
					drawPanel.repaint();
				}
				else if (creating != -1)
				{
					lastDragClick = current;
					drawPanel.repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent event)
			{
				// Change the cursor to a hand if it mouses over an entity
				if (creating == -1)
				{
					Point point = event.getPoint();
					Entity2D entity = null;
					for (Entity2D e : virtualEngine.getEntities())
					{
						Shape shape = e.getShape();
						if (shape.contains(point))
						{
							entity = e;
						}
					}
					if (entity != null)
					{
						drawPanel.setCursor(HAND_CURSOR);
					}
					else
					{
						drawPanel.setCursor(NORMAL_CURSOR);
					}
				}
			}
		});
		btnRectangle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Set to create a rectangle
				creating = 0;
				drawPanel.setCursor(CROSSHAIR_CURSOR);
			}
		});
		btnTarget.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// Set to create a target
				creating = 1;
				drawPanel.setCursor(CROSSHAIR_CURSOR);
			}
		});
		btnCcw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (selectedEntity != null)
				{
					// Rotate counter-clockwise
					Rectangle2D rect = (Rectangle2D) selectedEntity;
					rect.rotate(-5);
					drawPanel.repaint();
				}
			}
		});
		btnCw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Rotate clockwise
				if (selectedEntity != null)
				{
					Rectangle2D rect = (Rectangle2D) selectedEntity;
					rect.rotate(5);
					drawPanel.repaint();
				}
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (selectedEntity != null)
				{
					// Remove selected entity
					virtualEngine.removeEntity(selectedEntity);
					drawPanel.repaint();
				}
			}
		});

		btnClearAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// Clear the physics engine
				virtualEngine.clearAll();
				drawPanel.repaint();
			}
		});
		mntmNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Do the same as clear all
				virtualEngine.clearAll();
				drawPanel.repaint();
			}
		});
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					// Select the file
					JFileChooser chooser = new JFileChooser();
					chooser.setDialogTitle("Save Level");
					chooser.setDialogType(JFileChooser.SAVE_DIALOG);
					chooser.setCurrentDirectory(new File("levels"));

					// Check to make sure the user actually pressed OK and not
					// CANCEL
					int returnVal = chooser.showSaveDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						File chosen = chooser.getSelectedFile();

						String levelName = JOptionPane.showInputDialog(null,
								"Enter level name: ",
								"Trebuchet Demolition Level Editor",
								JOptionPane.PLAIN_MESSAGE);
						String entities = "";
						// Loop through all entities and generate a message to
						// save for the game to load, based on a certain format
						for (int entity = 0; entity < virtualEngine
								.getEntities().size(); entity++)
						{
							Entity2D currentEntity = virtualEngine
									.getEntities().get(entity);
							String entityDesc = "";
							if (currentEntity instanceof Rectangle2D)
							{
								Rectangle2D rect = (Rectangle2D) currentEntity;
								double angle = rect.angle;
								rect.rotate(-angle);
								entityDesc = "rect," + rect.p1.x + ","
										+ rect.p1.y + "," + rect.p4.x + ","
										+ rect.p4.y + "," + angle;
								rect.rotate(angle);
							}
							else if (currentEntity instanceof Target2D)
							{
								Target2D targ = (Target2D) currentEntity;
								entityDesc = "targ," + targ.p1.x + ","
										+ targ.p1.y + "," + targ.p2.x + ","
										+ targ.p2.y;
							}
							entities += entityDesc;
							if (entity != virtualEngine.getEntities().size() - 1)
							{
								entities += ":";
							}
						}
						// Save using Java Properties
						Properties prop = new Properties();
						prop.setProperty("entities", entities);
						prop.setProperty("name", levelName);
						prop.store(new FileOutputStream(chosen),
								"Created with Trebuchet Demolition Level Editor");
						JOptionPane.showMessageDialog(null, "Level saved!",
								"Trebuchet Demolition Level Editor",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null,
							"Failed to save level.", "Level Export Error",
							JOptionPane.ERROR_MESSAGE);
					TrebuchetDemolition.LOGGER.severe("Failed to save level: "
							+ ex.getMessage());
				}
			}
		});
		mntmLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					// Select file to load
					JFileChooser chooser = new JFileChooser();
					chooser.setDialogTitle("Load Level");
					chooser.setDialogType(JFileChooser.OPEN_DIALOG);
					chooser.setCurrentDirectory(new File("levels"));
					int returnVal = chooser.showSaveDialog(null);
					// Import with properties then create a new level, then get
					// entities in that level and import into virtual engine
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						File chosen = chooser.getSelectedFile();

						// Use Java Properties to easily load the level
						Properties metadata = new Properties();
						metadata.load(new FileInputStream(chosen));
						Vector<Entity2D> levelEntities = new Vector<Entity2D>();

						Level level = new Level(metadata.getProperty("name"),
								chosen, metadata, levelEntities);
						level.loadEntities();
						virtualEngine.clearAll();
						// Add every entity
						for (Entity2D e : level.getEntities())
						{
							virtualEngine.addEntity(e);
						}
						repaint();
					}

				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null,
							"Failed to load level.", "Level Import Error",
							JOptionPane.ERROR_MESSAGE);
					TrebuchetDemolition.LOGGER.severe("Failed to load level: "
							+ ex.getMessage());
				}

			}
		});
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// Hide then dispose
				levelEditor.setVisible(false);
				levelEditor.dispose();
			}
		});
	}
}
