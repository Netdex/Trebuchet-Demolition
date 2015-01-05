package game.level;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import physics.entity.Entity;

public class Level {

    private String name;
    private File file;
    private Properties metadata;
    private ArrayList<Entity> levelEntities;
    
    public Level(String name, File file, Properties metadata, ArrayList<Entity> levelEntities){
	this.name = name;
	this.file = file;
	this.metadata = metadata;
	this.levelEntities = levelEntities;
    }
    
    /**
     * Gets the name of the level
     * @return the name of the level
     */
    public String getName(){
	return name;
    }

    /**
     * Sets the name of the level
     * @param name the new name of the level
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the metadata for the level
     * @return the metadata for the level
     */
    public Properties getMetadata() {
        return metadata;
    }

    /**
     * Sets the metadata for the level
     * @param metadata The new metadata for the level
     */
    public void setMetadata(Properties metadata) {
        this.metadata = metadata;
    }

    /**
     * Gets the entities in this level
     * @return the entities in this level
     */
    public ArrayList<Entity> getEntities() {
        return levelEntities;
    }

    /**
     * Sets the entities in this level
     * @param levelEntities the new entities in this level
     */
    public void setLevelEntities(ArrayList<Entity> levelEntities) {
        this.levelEntities = levelEntities;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}