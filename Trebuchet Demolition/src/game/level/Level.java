package game.level;

import game.TrebuchetDemolition;
import game.physics.entity.AABB2D;
import game.physics.entity.Circle2D;
import game.physics.entity.Entity2D;
import game.physics.entity.Rectangle2D;
import game.physics.entity.Target2D;
import game.physics.util.Vector2D;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Represents a game level
 * 
 * @author Gordon Guan
 * @version Jan 2015
 */
public class Level
{

	private String name;
	private File file;
	/**
	 * Contains settings such as gravity, name, entities, colors
	 */
	private Properties metadata;
	private Vector<Entity2D> levelEntities;

	private int score = 0;

	/**
	 * Constructs a level
	 * 
	 * @param name The name of the level
	 * @param file The file which represents the level
	 * @param metadata The metadata (properties) of the level
	 * @param levelEntities The entities in this level
	 */
	public Level(String name, File file, Properties metadata,
			Vector<Entity2D> levelEntities)
	{
		this.name = name;
		this.file = file;
		this.metadata = metadata;
		this.levelEntities = levelEntities;
	}

	/**
	 * Saves the highscore and other properties into the properties
	 */
	public void save()
	{
		try
		{
			if (metadata.getProperty("highscore") == null
					|| Integer.parseInt(metadata.getProperty("highscore")) > score)
			{
				metadata.setProperty("highscore", score + "");
			}
			metadata.store(new FileOutputStream(file),
					"This level has been played!");
		}
		catch (Exception e)
		{
			TrebuchetDemolition.LOGGER
					.warning("Failed to save highscore information (file tampering?): "
							+ e.getMessage());
		}
		TrebuchetDemolition.LOGGER.info("Saved level data");
	}

	/**
	 * Loads the entities from the properties
	 * 
	 * @throws Exception when there is no target
	 */
	public void loadEntities() throws Exception
	{
		levelEntities.clear();
		StringTokenizer st = new StringTokenizer(
				metadata.getProperty("entities"), ":");

		// Loop through the string tokenizer
		boolean hasTarget = false;
		while (st.hasMoreTokens())
		{
			String line = st.nextToken().replaceAll(" ", "");
			String[] entityData = line.split(",");
			// Based on the type, load it differently
			String type = entityData[0];
			try
			{
				if (type.equals("circ"))
				{
					double x = Double.parseDouble(entityData[1]);
					double y = Double.parseDouble(entityData[2]);
					int radius = Integer.parseInt(entityData[3]);
					Vector2D loc = new Vector2D(x, y);
					Circle2D circle = new Circle2D(loc, Vector2D.ZERO, radius);
					levelEntities.add(circle);
				}
				else if (type.equals("rect"))
				{
					double x1 = Double.parseDouble(entityData[1]);
					double y1 = Double.parseDouble(entityData[2]);
					double x2 = Double.parseDouble(entityData[3]);
					double y2 = Double.parseDouble(entityData[4]);
					double rot = Double.parseDouble(entityData[5]);
					Vector2D p1 = new Vector2D(x1, y1);
					Vector2D p2 = new Vector2D(x2, y2);
					Rectangle2D rect = new Rectangle2D(p1, p2);
					rect.rotate(rot);
					levelEntities.add(rect);
				}
				else if (type.equals("aabb"))
				{
					double x1 = Double.parseDouble(entityData[1]);
					double y1 = Double.parseDouble(entityData[2]);
					double x2 = Double.parseDouble(entityData[3]);
					double y2 = Double.parseDouble(entityData[4]);
					boolean physics = Boolean.valueOf(entityData[5]);
					Vector2D p1 = new Vector2D(x1, y1);
					Vector2D p2 = new Vector2D(x2, y2);
					AABB2D aabb = new AABB2D(p1, p2, Vector2D.ZERO, physics);
					levelEntities.add(aabb);
				}
				else if (type.equals("targ"))
				{
					double x1 = Double.parseDouble(entityData[1]);
					double y1 = Double.parseDouble(entityData[2]);
					double x2 = Double.parseDouble(entityData[3]);
					double y2 = Double.parseDouble(entityData[4]);
					Vector2D p1 = new Vector2D(x1, y1);
					Vector2D p2 = new Vector2D(x2, y2);
					Target2D targ = new Target2D(p1, p2);
					levelEntities.add(targ);
					hasTarget = true;
				}
			}
			catch (Exception e)
			{
				TrebuchetDemolition.LOGGER
						.warning("Failed to load an entity in level: "
								+ e.getMessage());
			}
		}

		// Check if the level has a valid target
		if (!hasTarget)
		{
			TrebuchetDemolition.LOGGER.info("Couldn't load level \""
					+ metadata.getProperty("name") + "\", has no target");
			throw new Exception("No target in level");
		}

	}

	/**
	 * Gets the name of the level
	 * 
	 * @return the name of the level
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name of the level
	 * 
	 * @param name the new name of the level
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the metadata for the level
	 * 
	 * @return the metadata for the level
	 */
	public Properties getMetadata()
	{
		return metadata;
	}

	/**
	 * Sets the metadata for the level
	 * 
	 * @param metadata The new metadata for the level
	 */
	public void setMetadata(Properties metadata)
	{
		this.metadata = metadata;
	}

	/**
	 * Gets the entities in this level
	 * 
	 * @return the entities in this level
	 */
	public Vector<Entity2D> getEntities()
	{
		return levelEntities;
	}

	/**
	 * Sets the entities in this level
	 * 
	 * @param levelEntities the new entities in this level
	 */
	public void setLevelEntities(Vector<Entity2D> levelEntities)
	{
		this.levelEntities = levelEntities;
	}

	/**
	 * Gets the file this level is based on
	 * 
	 * @return the file this level is based on
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * Sets the file this level is based on
	 * 
	 * @param file The new file this level should be based on
	 */
	public void setFile(File file)
	{
		this.file = file;
	}

	/**
	 * Gets a Vector of all the cloned entities
	 * 
	 * @return all the cloned entities in a Vector
	 */
	public Vector<Entity2D> getEntitiesClone()
	{
		Vector<Entity2D> entities = new Vector<Entity2D>();
		for (Entity2D entity : levelEntities)
		{
			entities.add(entity.clone());
		}
		return entities;
	}

	/**
	 * Gets the score of this level
	 * 
	 * @return the score of this level
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * Sets the score of this level
	 * 
	 * @param score The new score of this level
	 */
	public void setScore(int score)
	{
		this.score = score;
	}
}
