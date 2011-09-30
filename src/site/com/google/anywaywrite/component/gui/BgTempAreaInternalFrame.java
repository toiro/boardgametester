/**
 *
 */
package site.com.google.anywaywrite.component.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

/**
 * @author y-kitajima
 * 
 */
public class BgTempAreaInternalFrame extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    private BgAreaLabel areaLabel;

    private void setAreaLabel(BgAreaLabel val) {
	this.areaLabel = val;
    }

    public final BgAreaLabel getAreaLabel() {
	return this.areaLabel;
    }

    private String areaName;

    private void setAreaName(String val) {
	this.areaName = val;
    }

    public final String getAreaName() {
	return this.areaName;
    }

    // private Rectangle framePosition;
    //
    // private void setFramePosition(Rectangle val) {
    // this.framePosition = val;
    // }

    private BgAreaLabelPane playArea;

    private void setPlayArea(BgAreaLabelPane val) {
	this.playArea = val;
    }

    public BgAreaLabelPane getPlayArea() {
	return this.playArea;
    }

    private BgTempAreaInternalFrame(BgAreaLabelPane playArea, String areaName,
	    BgAreaLabel areaLabel, Rectangle framePosition) {
	super();
	setPlayArea(playArea);
	registerInitialArea(areaName, areaLabel, framePosition);
    }

    public static BgTempAreaInternalFrame newInstance(BgAreaLabelPane playArea,
	    String areaName, BgAreaLabel areaLabel, Rectangle framePosition) {
	return new BgTempAreaInternalFrame(playArea, areaName, areaLabel,
		framePosition);
    }

    private void registerInitialArea(String areaName, BgAreaLabel areaLabel,
	    Rectangle framePosition) {
	if (areaName == null || areaName.length() == 0) {
	    throw new IllegalArgumentException(
		    "areaName should not be null or zero length, but was.");
	}
	if (areaLabel == null) {
	    throw new IllegalArgumentException(
		    "areaLabel should not be null, but was.");
	}
	setAreaName(areaName);
	setAreaLabel(areaLabel);
	// setFramePosition(framePosition);

	getScp().setViewportView(areaLabel);
	getContentPane().add(getScp(), BorderLayout.CENTER);
	setBounds(framePosition);
    }

    private JScrollPane scp;

    private JScrollPane getScp() {
	if (scp == null) {
	    scp = new JScrollPane();
	    scp.getViewport().setBackground(Color.BLACK);
	}
	return scp;
    }

}
