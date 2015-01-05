package game.level;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import physics.entity.Circle;
import physics.entity.Entity;
import physics.util.Vector;

/**
 * Manages loaded levels, and loads them
 * 
 * @author Gordon Guan
 * @version Jan 2015 TODO Add functionality
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
		ArrayList<Entity> levelEntities = new ArrayList<Entity>();
		StringTokenizer st = new StringTokenizer(metadata.getProperty("entities"), ":");
		
		while (st.hasMoreTokens()) {
		    String line = st.nextToken().replaceAll(" ", "");
		    String[] entityData = line.split(",");
		    String type = entityData[0];
		    try {
			if (type.equals("circ")) {
			    // Loads a circle with format
			    // circ,13,14,112,255,255,255
			    int x = Integer.parseInt(entityData[1]);
			    int y = Integer.parseInt(entityData[2]);
			    int radius = Integer.parseInt(entityData[3]);
			    Color color = new Color(Integer.parseInt(entityData[4]), Integer.parseInt(entityData[5]), Integer.parseInt(entityData[6]));
			    Vector loc = new Vector(x, y);
			    Circle circle = new Circle(loc, Vector.ZERO, Vector.ZERO, radius, color);
			    levelEntities.add(circle);
			} else if (type.equals("rect")) {
			    // Loads a rectangle with format rect:
			}
		    } catch (Exception e) {

		    }
		}
		Level level = new Level(metadata.getProperty("name"), levelFile, metadata, levelEntities);
		levels.add(level);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    public static ArrayList<Level> getLevels() {
	return levels;
    }

    public static void setLevels(ArrayList<Level> levels) {
	LevelManager.levels = levels;
    }
}
