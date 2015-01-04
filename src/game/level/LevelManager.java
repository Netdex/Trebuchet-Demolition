package game.level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import physics.entity.Entity;

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
		BufferedReader br = new BufferedReader(new FileReader(levelFile));
		HashMap<String, Object> metadata = new HashMap<String, Object>();
		
		String line;
		while(!(line = br.readLine()).equals("entities") && line != null){
		    String[] input = line.split(":");
		    metadata.put(input[0], input[1]);
		}
		
		Level level = new Level(String.valueOf(metadata.get("name")), levelFile, metadata, new ArrayList<Entity>());
		levels.add(level);
		System.out.println("loaded " + level.getName() + " - " + levelFile.getName());
	    } catch (Exception e) {

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
