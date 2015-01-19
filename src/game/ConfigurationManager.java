package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Manages the game configuration
 * 
 * @author Gordon Guan
 * @version Jan 2015
 */

public class ConfigurationManager {
    private static File configFile = new File("config.prop");
    private static Properties prop;

    public static void loadConfiguration() {
	try {
	    prop = new Properties();
	    if (configFile.exists()) {
		FileInputStream fis = new FileInputStream(configFile);
		prop.load(fis);
	    }
	} catch (Exception e) {
	    TrebuchetDemolition.LOGGER.warning("Failed to load configuration: " + e.getMessage());
	}
    }

    public static void saveConfiguration() {
	try {
	    if(!configFile.exists()){
		configFile.createNewFile();
	    }
	    FileOutputStream fos = new FileOutputStream(configFile);
	    prop.store(fos, "Configuration Options for Trebuchet Demolition");
	} catch (Exception e) {
	    TrebuchetDemolition.LOGGER.warning("Failed to save configuration: " + e.getMessage());
	}
    }
    
    public static Properties getProperties(){
	return prop;
    }
    
    public static void setProperty(String key, String value){
	prop.setProperty(key, value);
	saveConfiguration();
    }
    public static boolean getBooleanProperty(String property){
	try{
	    return Boolean.valueOf(prop.getProperty(property));
	}catch(Exception e){
	    TrebuchetDemolition.LOGGER.warning("Failed to get boolean property: " + e.getMessage());
	}
	return false;
    }
}
