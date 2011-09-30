/**
 *
 */
package site.com.google.anywaywrite.util.gui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * �����ȉ摜�ϊ����T�|�[�g����N���X�ł�
 * 
 * @author y-kitajima
 * 
 */
public class BgScaleUtil {

    public static BufferedImage getFasterScaledInstance(BufferedImage img,
	    int targetWidth, int targetHeight, Object hint,
	    boolean progressiveBilinear) {
	int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
		: BufferedImage.TYPE_INT_ARGB;
	BufferedImage ret = img;
	BufferedImage scratchImage = null;
	Graphics2D g2 = null;
	int w, h;
	int prevW = ret.getWidth();
	int prevH = ret.getHeight();

	if (progressiveBilinear) {
	    w = img.getWidth();
	    h = img.getHeight();
	} else {
	    w = targetWidth;
	    h = targetHeight;
	}

	do {
	    if (progressiveBilinear && w > targetWidth) {
		w /= 2;
		if (w < targetWidth) {
		    w = targetWidth;
		}
	    } else if (w < targetWidth) {
		w = targetWidth;
	    }

	    if (progressiveBilinear && h > targetHeight) {
		h /= 2;
		if (h < targetHeight) {
		    h = targetHeight;
		}
	    } else if (h < targetHeight) {
		h = targetHeight;
	    }

	    if (scratchImage == null) {
		scratchImage = new BufferedImage(w, h, type);
		g2 = scratchImage.createGraphics();
	    }

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);

	    g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);
	    prevW = w;
	    prevH = h;
	    ret = scratchImage;
	} while (w != targetWidth || h != targetHeight);

	if (g2 != null) {
	    g2.dispose();
	}

	if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
	    scratchImage = new BufferedImage(targetWidth, targetHeight, type);
	    g2 = scratchImage.createGraphics();
	    g2.drawImage(ret, 0, 0, null);
	    g2.dispose();
	    ret = scratchImage;
	}

	return ret;
    }

    // public static BufferedImage getFasterBiggerInstance(BufferedImage img,
    // int targetWidth, int targetHeight, Object hint,
    // boolean progressiveBilinear) {
    //
    // int type = (img.getTransparency() == Transparency.OPAQUE) ?
    // BufferedImage.TYPE_INT_RGB
    // : BufferedImage.TYPE_INT_ARGB;
    // BufferedImage ret = img;
    // BufferedImage scratchImage = null;
    // Graphics2D g2 = null;
    // int w, h;
    // int prevW = ret.getWidth();
    // int prevH = ret.getHeight();
    //
    // if (progressiveBilinear) {
    // w = img.getWidth();
    // h = img.getHeight();
    // } else {
    // w = targetWidth;
    // h = targetHeight;
    // }
    //
    // do {
    // if (progressiveBilinear && w < targetWidth) {
    // w *= 2;
    // if (w > targetWidth) {
    // w = targetWidth;
    // }
    // }
    // if (progressiveBilinear && h < targetHeight) {
    // h *= 2;
    // if (h > targetHeight) {
    // h = targetHeight;
    // }
    // }
    //
    // if (scratchImage == null) {
    // scratchImage = new BufferedImage(w, h, type);
    // g2 = scratchImage.createGraphics();
    // }
    //
    // g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
    //
    // g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);
    // prevW = w;
    // prevH = h;
    // ret = scratchImage;
    // } while (w > targetWidth || h > targetHeight);
    //
    // if (g2 != null) {
    // g2.dispose();
    // }
    //
    // if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
    // scratchImage = new BufferedImage(targetWidth, targetHeight, type);
    // g2 = scratchImage.createGraphics();
    // g2.drawImage(ret, 0, 0, null);
    // g2.dispose();
    // ret = scratchImage;
    // }
    //
    // return ret;
    // }

    // public static BufferedImage getFasterSmallerInstance(BufferedImage img,
    // int targetWidth, int targetHeight, Object hint,
    // boolean progressiveBilinear) {
    // int type = (img.getTransparency() == Transparency.OPAQUE) ?
    // BufferedImage.TYPE_INT_RGB
    // : BufferedImage.TYPE_INT_ARGB;
    //
    // if (targetWidth > img.getWidth() && targetHeight > img.getHeight()) {
    //
    // }
    //
    // }
}
