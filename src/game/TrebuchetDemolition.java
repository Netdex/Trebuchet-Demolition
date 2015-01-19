package game;

import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * A game which involves physics and calculations
 * @author Gordon Guan
 * @version Jan 2015
 */
public class TrebuchetDemolition extends JFrame {
    
    // Get rid of the yellow squigglies
    private static final long serialVersionUID = -7601853692438229300L;
    
    // Initiate a logger for my own logging purposes
    public static final Logger LOGGER = Logger.getLogger(TrebuchetDemolition.class.getName());

    public TrebuchetDemolition() throws Exception {
	super("Trebuchet Demolition");

	// Load options
	ConfigurationManager.loadConfiguration();
	
	// Change the theme to a more appealing one
//	UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

	// TODO Set level to Level.OFF before handing it in [in case I didn't, ignore all the log messages]
	LOGGER.setLevel(Level.WARNING);
	
	// Create window
	final int width = 900;
	final int height = 600;

	int panelWidth = width - 5;
	int panelHeight = height - 28;
	this.setLocationByPlatform(true);
	this.setResizable(false);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	this.setIconImage(ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/icon.png")));

	// Create the panel and everything inside
	GamePanel sp = new GamePanel(panelWidth, panelHeight);
	sp.setBounds(0, 0, panelWidth, panelHeight);
	sp.setPreferredSize(new Dimension(panelWidth, panelHeight));
	sp.setMinimumSize(new Dimension(panelWidth, panelHeight));
	this.add(sp);
    }

    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		try {
		    TrebuchetDemolition td = new TrebuchetDemolition();
		    td.setVisible(true);
		    td.pack();
		} catch (Throwable e) {
		    LOGGER.severe("Failed to load program : " + e.getMessage());
		}
	    }
	});

    }

}
