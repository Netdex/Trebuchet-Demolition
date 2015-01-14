package game;

import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TrebuchetDemolition extends JFrame {
    private static final long serialVersionUID = -7601853692438229300L;

    public TrebuchetDemolition() throws Exception {
	super("Trebuchet Demolition");

	// Don't use the ugly swing theme
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

	
	final int width = 900;
	final int height = 600;
	
	int panelWidth = width - 5;
	int panelHeight = height - 28;
	this.setLocationByPlatform(true);
	this.setResizable(false);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	this.setIconImage(ImageIO.read(TrebuchetDemolition.class.getResourceAsStream("/resources/icon.png")));
	
	
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
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});

    }

}
