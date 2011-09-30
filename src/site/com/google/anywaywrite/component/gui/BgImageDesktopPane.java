/**
 *
 */
package site.com.google.anywaywrite.component.gui;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JDesktopPane;

import site.com.google.anywaywrite.component.layout.BgLayoutPosition;
import site.com.google.anywaywrite.util.gui.BgCompatibleUtil;
import site.com.google.anywaywrite.util.gui.BgScaleUtil;

/**
 * @author kitajima
 * 
 */
public class BgImageDesktopPane extends JDesktopPane {

    private static final long serialVersionUID = 1L;

    private String imagePath;
    private BgLayoutPosition layoutPosition;
    private double rate;

    public BgImageDesktopPane(String imagePath, BgLayoutPosition position,
	    double rate) {
	setImagePath(imagePath);
	setLayoutPosition(position);
	setRate(rate);
    }

    public void setImagePath(String val) {
	this.imagePath = val;
    }

    public String getImagePath() {
	return this.imagePath;
    }

    public void setLayoutPosition(BgLayoutPosition position) {
	this.layoutPosition = position;
    }

    public BgLayoutPosition getLayoutPosition() {
	return this.layoutPosition;
    }

    public void setRate(double rate) {
	this.rate = rate;
    }

    public double getRate() {
	return this.rate;
    }

    @Override
    public final void paintComponent(Graphics g) {
	if (getImagePath() == null) {
	    return;
	}
	Graphics2D g2d = (Graphics2D) g.create();
	try {
	    BufferedImage bimg = BgCompatibleUtil.loadCompatibleImage(new File(
		    getImagePath()));

	    BufferedImage bImage = BgScaleUtil.getFasterScaledInstance(bimg,
		    (int) (bimg.getWidth() * getRate()), (int) (bimg
			    .getHeight() * getRate()),
		    RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);

	    int dx = createDxPosition(bImage);
	    int dy = createDyPosition(bImage);

	    g2d.drawImage(bImage, dx, dy, null);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	g2d.dispose();
    }

    private int createDxPosition(BufferedImage image) {
	if (getLayoutPosition() == BgLayoutPosition.NORTH_WEST
		|| getLayoutPosition() == BgLayoutPosition.WEST
		|| getLayoutPosition() == BgLayoutPosition.SOUTH_WEST) {
	    return 0;
	} else if (getLayoutPosition() == BgLayoutPosition.NORTH_EAST
		|| getLayoutPosition() == BgLayoutPosition.EAST
		|| getLayoutPosition() == BgLayoutPosition.SOUTH_EAST) {
	    return getWidth() - image.getWidth();
	} else {
	    return (getWidth() - image.getWidth()) / 2;
	}
    }

    private int createDyPosition(BufferedImage image) {
	if (getLayoutPosition() == BgLayoutPosition.NORTH_WEST
		|| getLayoutPosition() == BgLayoutPosition.NORTH
		|| getLayoutPosition() == BgLayoutPosition.NORTH_EAST) {
	    return 0;
	} else if (getLayoutPosition() == BgLayoutPosition.WEST
		|| getLayoutPosition() == BgLayoutPosition.CENTER
		|| getLayoutPosition() == BgLayoutPosition.EAST) {
	    return (getHeight() - image.getHeight()) / 2;
	} else {
	    return getHeight() - image.getHeight();
	}
    }
}
