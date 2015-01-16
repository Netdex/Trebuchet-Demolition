package game.level;

import game.TrebuchetDemolition;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import physics.entity.AABB2D;
import physics.entity.Circle2D;
import physics.entity.Entity2D;
import physics.entity.Rectangle2D;
import physics.entity.Target2D;
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
		return name.toLowerCase().endsWith(".txt");
	    }
	})) {
	    try {
		Properties metadata = new Properties();

		metadata.load(new FileInputStream(levelFile));
		Vector<Entity2D> levelEntities = new Vector<Entity2D>();
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
			    Circle2D circle = new Circle2D(loc, Vector2D.ZERO, radius, color);
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
			    Rectangle2D rect = new Rectangle2D(p1, p2, color);
			    rect.rotate(rot);
			    levelEntities.add(rect);
			} else if (type.equals("aabb")) {
			    double x1 = Double.parseDouble(entityData[1]);
			    double y1 = Double.parseDouble(entityData[2]);
			    double x2 = Double.parseDouble(entityData[3]);
			    double y2 = Double.parseDouble(entityData[4]);
			    Color color = new Color(Integer.parseInt(entityData[5]), Integer.parseInt(entityData[6]), Integer.parseInt(entityData[7]));
			    boolean physics = Boolean.valueOf(entityData[8]);
			    Vector2D p1 = new Vector2D(x1, y1);
			    Vector2D p2 = new Vector2D(x2, y2);
			    AABB2D aabb = new AABB2D(p1, p2, Vector2D.ZERO, color, physics);
			    levelEntities.add(aabb);
			} else if (type.equals("targ")) {
			    double x1 = Double.parseDouble(entityData[1]);
			    double y1 = Double.parseDouble(entityData[2]);
			    double x2 = Double.parseDouble(entityData[3]);
			    double y2 = Double.parseDouble(entityData[4]);
			    Color color = new Color(Integer.parseInt(entityData[5]), Integer.parseInt(entityData[6]), Integer.parseInt(entityData[7]));
			    Vector2D p1 = new Vector2D(x1, y1);
			    Vector2D p2 = new Vector2D(x2, y2);
			    Target2D targ = new Target2D(p1, p2, color);
			    levelEntities.add(targ);
			    hasTarget = true;
			}
		    } catch (Exception e) {
			TrebuchetDemolition.LOGGER.warning("Failed to load an entity in level: " + e.getMessage());
		    }
		}
		if (hasTarget) {
		    Level level = new Level(metadata.getProperty("name"), levelFile, metadata, levelEntities);
		    levels.add(level);
		}
		else{
		    TrebuchetDemolition.LOGGER.info("Couldn't load level \"" + metadata.getProperty("name") + "\", has no target");
		}
	    } catch (Exception e) {
		TrebuchetDemolition.LOGGER.warning("Error while loading level: " + e.getMessage());;
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
