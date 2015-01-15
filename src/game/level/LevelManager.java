package game.level;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import physics.entity.AABB;
import physics.entity.Circle;
import physics.entity.Entity;
import physics.entity.Rectangle;
import physics.entity.Target;
import physics.util.Vector2D;

/**
 * Manages loaded levels, and loads them
 * 
 * @author Gordon Guan
 * @version Jan 2015
 */
public class LevelManager {
    public static ArrayList<Level> levels = new ArrayList<Level>();

    public static void loadLevels() {
	levels.clear();

	File levelFolder = new File("levels");
	if (!levelFolder.exists()) {
	    levelFolder.mkdir();
	}
	for (File levelFile : levelFolder.listFiles(new FilenameFilter() {
	    public boolean accept(File dir, String name) {
		return name.toLowerCase().endsWith(".dat");
	    }
	})) {
	    try {
		Properties metadata = new Properties();

		metadata.load(new FileInputStream(levelFile));
		Vector<Entity> levelEntities = new Vector<Entity>();
		StringTokenizer st = new StringTokenizer(metadata.getProperty("entities"), ":");

		boolean hasTarget = false;
		while (st.hasMoreTokens()) {
		    String line = st.nextToken().replaceAll(" ", "");
		    String[] entityData = line.split(",");
		    String type = entityData[0];
		    try {
			if (type.equals("circ")) {
			    // Loads a circle with format
			    // circ,13,14,112,255,255,255
			    double x = Double.parseDouble(entityData[1]);
			    double y = Double.parseDouble(entityData[2]);
			    int radius = Integer.parseInt(entityData[3]);
			    Color color = new Color(Integer.parseInt(entityData[4]), Integer.parseInt(entityData[5]), Integer.parseInt(entityData[6]));
			    Vector2D loc = new Vector2D(x, y);
			    Circle circle = new Circle(loc, Vector2D.ZERO, Vector2D.ZERO, radius, color);
			    levelEntities.add(circle);
			} else if (type.equals("rect")) {
			    // Loads a rectangle with format rect:
			    double x1 = Double.parseDouble(entityData[1]);
			    double y1 = Double.parseDouble(entityData[2]);
			    double x2 = Double.parseDouble(entityData[3]);
			    double y2 = Double.parseDouble(entityData[4]);
			    double rot = Double.parseDouble(entityData[5]);
			    Color color = new Color(Integer.parseInt(entityData[6]), Integer.parseInt(entityData[7]), Integer.parseInt(entityData[8]));
			    Vector2D p1 = new Vector2D(x1, y1);
			    Vector2D p2 = new Vector2D(x2, y2);
			    Rectangle rect = new Rectangle(p1, p2, color);
			    rect.rotate(rot);
			    levelEntities.add(rect);
			} else if (type.equals("aabb")) {
			    double x1 = Double.parseDouble(entityData[1]);
			    double y1 = Double.parseDouble(entityData[2]);
			    double x2 = Double.parseDouble(entityData[3]);
			    double y2 = Double.parseDouble(entityData[4]);
			    Color color = new Color(Integer.parseInt(entityData[5]), Integer.parseInt(entityData[6]), Integer.parseInt(entityData[7]));
			    Vector2D p1 = new Vector2D(x1, y1);
			    Vector2D p2 = new Vector2D(x2, y2);
			    AABB aabb = new AABB(p1, p2, Vector2D.ZERO, color);
			    levelEntities.add(aabb);
			} else if (type.equals("targ")) {
			    double x1 = Double.parseDouble(entityData[1]);
			    double y1 = Double.parseDouble(entityData[2]);
			    double x2 = Double.parseDouble(entityData[3]);
			    double y2 = Double.parseDouble(entityData[4]);
			    Color color = new Color(Integer.parseInt(entityData[5]), Integer.parseInt(entityData[6]), Integer.parseInt(entityData[7]));
			    Vector2D p1 = new Vector2D(x1, y1);
			    Vector2D p2 = new Vector2D(x2, y2);
			    Target targ = new Target(p1, p2, color);
			    levelEntities.add(targ);
			    hasTarget = true;
			}
		    } catch (Exception e) {

		    }
		}
		if (hasTarget) {
		    Level level = new Level(metadata.getProperty("name"), levelFile, metadata, levelEntities);
		    levels.add(level);
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Gets all the levels loaded
     * 
     * @return the levels loaded
     */
    public static ArrayList<Level> getLevels() {
	return levels;
    }

    /**
     * Sets the loaded levels
     * 
     * @param levels the new levels to set
     */
    public static void setLevels(ArrayList<Level> levels) {
	LevelManager.levels = levels;
    }
}
