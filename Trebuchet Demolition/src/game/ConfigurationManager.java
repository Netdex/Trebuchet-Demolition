package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Manages the game configuration, and loads any configuration that any other
 * classes my access
 * 
 * @author Gordon Guan
 * @version Jan 2015
 */

public class ConfigurationManager
{
	private static File configFile = new File("config.prop");
	private static Properties prop;

	/**
	 * Loads the configuration from a properties file
	 */
	public static void loadConfiguration()
	{
		try
		{
			prop = new Properties();
			if (configFile.exists())
			{
				FileInputStream fis = new FileInputStream(configFile);
				prop.load(fis);
			}
		}
		catch (Exception e)
		{
			TrebuchetDemolition.LOGGER.warning("Failed to load configuration: "
					+ e.getMessage());
		}
	}

	/**
	 * Saves the configuration to a properties file
	 */
	public static void saveConfiguration()
	{
		try
		{
			if (!configFile.exists())
			{
				configFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(configFile);
			prop.store(fos, "Configuration Options for Trebuchet Demolition");
		}
		catch (Exception e)
		{
			TrebuchetDemolition.LOGGER.warning("Failed to save configuration: "
					+ e.getMessage());
		}
	}

	/**
	 * Gets the properties object with all the settings
	 * 
	 * @return the properties object with all the settings
	 */
	public static Properties getProperties()
	{
		return prop;
	}

	/**
	 * Gets a specific string property
	 * 
	 * @param key The name of the property
	 * @param value The value of the property
	 */
	public static void setProperty(String key, String value)
	{
		prop.setProperty(key, value);
		saveConfiguration();
	}

	/**
	 * Gets a boolean property
	 * 
	 * @param property The name of the property
	 * @return the boolean property
	 */
	public static boolean getBooleanProperty(String property)
	{
		try
		{
			return Boolean.valueOf(prop.getProperty(property));
		}
		catch (Exception e)
		{
			TrebuchetDemolition.LOGGER
					.warning("Failed to get boolean property: "
							+ e.getMessage());
		}
		return false;
	}
}
