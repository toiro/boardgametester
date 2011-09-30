/**
 *
 */
package site.com.google.anywaywrite.component.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/**
 * @author kitajima
 * 
 */
public class BgPaintAreaNameSupport {

    public static final void paintAreaName(Graphics g, String areaName) {
	String dispAreaName = areaName + " ▼ ";

	LinearGradientPaint lgp = new LinearGradientPaint(0.0f, 0.0f, 0.0f, 16,
		new float[] { 0.0f, 0.249f, 0.25f, 1.0f }, new Color[] {
			new Color(0x63a5f7), new Color(0x3799f4),
			new Color(0x2d7eeb), new Color(0x30a5f9) });

	Graphics2D g2d = (Graphics2D) g.create();
	Font font = g2d.getFont().deriveFont(Font.BOLD | Font.ITALIC, 12f);
	g2d.setFont(font);
	FontMetrics fm = g2d.getFontMetrics(font);
	drawGradient(g2d, dispAreaName, lgp, fm);
	g2d.dispose();

	Graphics2D g3d = (Graphics2D) g.create();
	drawAreaName(g3d, dispAreaName, font);
	g3d.dispose();
    }

    private static void drawAreaName(Graphics2D g2d, String dispAreaName,
	    Font font) {
	g2d.setFont(font);
	g2d.setColor(Color.WHITE);
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.drawString(dispAreaName, 10, 12);
    }

    private static void drawGradient(Graphics2D g2d, String dispAreaName,
	    LinearGradientPaint lgp, FontMetrics fm) {
	g2d.setPaint(lgp);
	Rectangle rec = getDisplayRectangle(fm, dispAreaName);
	g2d.fillRect(rec.x, rec.y, rec.width, rec.height);
	g2d.dispose();
    }

    private static Rectangle getDisplayRectangle(FontMetrics fm,
	    String dispAreaName) {
	Rectangle ret = new Rectangle();
	ret.x = 0;
	ret.y = 0;
	ret.width = fm.stringWidth(dispAreaName) + 20;
	ret.height = 16;
	return ret;
    }
}
