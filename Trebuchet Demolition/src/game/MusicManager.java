package game;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

/**
 * Handles the music being played (Johann Strauss's Blue Danube Waltz)
 * 
 * @author Gordon Guan
 * @version Jan 2015
 */
public class MusicManager
{

	private AudioClip clip;

	/**
	 * Constructs a MusicManager
	 */
	public MusicManager()
	{
		// Load the music from resources
		URL url = TrebuchetDemolition.class
				.getResource("/resources/bluedanube.wav");
		this.clip = Applet.newAudioClip(url);
	}

	/**
	 * Play the song
	 */
	public void start()
	{
		clip.loop();
	}

	/**
	 * Stop playing the song
	 */
	public void stop()
	{
		clip.stop();
	}
}
