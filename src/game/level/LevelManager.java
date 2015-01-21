package game.level;

import game.TrebuchetDemolition;
import game.physics.entity.Entity2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

/**
 * Manages loaded levels, and loads them
 * 
 * @author Gordon Guan
 * @version Jan 2015
 */
public class LevelManager {
    public static ArrayList<Level> levels = new ArrayList<Level>();
    
    /**
     * Loads all valid levels in the "levels" directory
     */
    public static void loadLevels() {
	levels.clear();

	File levelFolder = new File("levels");
	if (!levelFolder.exists()) {
	    levelFolder.mkdir();
	}
	// Loop through every text file
	for (File levelFile : levelFolder.listFiles(new FilenameFilter() {
	    public boolean accept(File dir, String name) {
		return name.toLowerCase().endsWith(".txt");
	    }
	})) {
	    try {
		Properties metadata = new Properties();
		metadata.load(new FileInputStream(levelFile));
		Vector<Entity2D> levelEntities = new Vector<Entity2D>();
		
		Level level = new Level(metadata.getProperty("name"), levelFile, metadata, levelEntities);
		level.loadEntities();
		levels.add(level);
	    } catch (Exception e) {
		TrebuchetDemolition.LOGGER.warning("Error while loading level: " + e.getMessage());
	    }

	}
	TrebuchetDemolition.LOGGER.info("Loaded all levels");
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
