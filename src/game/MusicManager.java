package game;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class MusicManager {
    
    private AudioClip clip;

    public MusicManager() {
	URL url = TrebuchetDemolition.class.getResource("/resources/bluedanube.wav");
	this.clip = Applet.newAudioClip(url);
    }

    public void start() {
	clip.loop();
    }

    public void stop() {
	clip.stop();
    }
}
