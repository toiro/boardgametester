/**
 *
 */
package site.com.google.anywaywrite.util.gui;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * クライアントのプライマリディスプレイの環境を取得して、 そのディスプレイに最適な画像表示形式での画像を返すユーティるクラスです。 <br/>
 * こうした画像のことを、互換(Compatible)画像といいます。
 * 
 * @author y-kitajima
 * 
 */
public class BgCompatibleUtil {

    private static GraphicsConfiguration getConfiguration() {
	return GraphicsEnvironment.getLocalGraphicsEnvironment()
		.getDefaultScreenDevice().getDefaultConfiguration();
    }

    public static BufferedImage createCompatibleImage(BufferedImage image) {
	return createCompatibleImage(image, image.getWidth(), image.getHeight());
    }

    public static BufferedImage createCompatibleImage(BufferedImage image,
	    int width, int height) {
	return getConfiguration().createCompatibleImage(width, height,
		image.getTransparency());
    }

    public static BufferedImage createCompatibleImage(int width, int height) {
	return getConfiguration().createCompatibleImage(width, height);
    }

    public static BufferedImage createCompatibleTransLucentImage(int width,
	    int height) {
	return getConfiguration().createCompatibleImage(width, height,
		Transparency.TRANSLUCENT);
    }

    public static BufferedImage loadCompatibleImage(URL resource)
	    throws IOException {
	BufferedImage image = ImageIO.read(resource);
	return toCompatibleImage(image);
    }

    public static BufferedImage loadCompatibleImage(File file)
	    throws IOException {
	BufferedImage image = ImageIO.read(file);
	return toCompatibleImage(image);
    }

    public static BufferedImage toCompatibleImage(BufferedImage image) {
	GraphicsConfiguration gc = getConfiguration();
	if (image.getColorModel().equals(gc.getColorModel())) {
	    return image;
	}

	BufferedImage compatibleImage = gc.createCompatibleImage(image
		.getWidth(), image.getHeight(), image.getTransparency());
	Graphics g = compatibleImage.getGraphics();
	g.drawImage(image, 0, 0, null);
	g.dispose();
	return compatibleImage;
    }

}
