/**
 *
 */
package site.com.google.anywaywrite.component.layout;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import site.com.google.anywaywrite.component.gui.BgCardArea;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;
import site.com.google.anywaywrite.util.gui.BgScaleUtil;

/**
 * 水面に浮かんだようなエフェクトでエリアを表示するレイアウトです。 <br />
 * 2011-09-14 時点で未完成です。(反射エリアは100パーセント未満にもできるように実装しなければなりません。<br />
 * 今はそう実装しても、100 パーセント反射エリアが作成されてしまいます。)
 * 
 * @author y-kitajima
 * 
 */
public class BgReflectionLayout implements BgAreaLayout {

    private Insets margin;
    private Component comp;
    private List<BgCardItem> clist;
    private String blankName;
    private int horizontalWidth;

    private final int getHorizontalWidth() {
	return horizontalWidth;
    }

    private final void setHorizontalWidth(int horizontalWidth) {
	this.horizontalWidth = horizontalWidth;
    }

    private BgReflectionLayout(int horizontalWidth) {
	setMargin(createDefaultMargin());
	setHorizontalWidth(horizontalWidth);
    }

    public static BgReflectionLayout newInstance(int horizontalWidth) {
	return new BgReflectionLayout(horizontalWidth);
    }

    @Override
    public String verify(List<BgCardItem> clist) {
	if (clist == null) {
	    return "The BgCardItem list should not be null, but was";
	}
	if (clist.size() != 1) {
	    return "The BgCardItem size should be one, but was.";
	}
	return null;
    }

    @Override
    public final void setMargin(Insets is) {
	this.margin = new Insets(is.top, is.left, is.bottom, is.right);
    }

    protected Insets createDefaultMargin() {
	return new Insets(0, 0, 0, 0);
    }

    private Insets getMargin() {
	return this.margin;
    }

    @Override
    public final void setPosition(BgLayoutPosition position) {
	throw new RuntimeException("This method is unsupported.");
    }

    @Override
    public final void setBase(BgLayoutBase base) {
	throw new RuntimeException("This method is unsupported");
    }

    @Override
    public void setArea(BgCardArea area) {
	if (area == null) {
	    throw new RuntimeException("area should not be null, but was.");
	}
	setClist(area.getCards());
	setComponent(area.getComponent());
	setBlankName(area.getAreaName());
    }

    private void setComponent(Component comp) {
	this.comp = comp;
    }

    private Component getComponent() {
	return this.comp;
    }

    private void setClist(List<BgCardItem> clist) {
	String errMsg = verify(clist);
	if (errMsg == null) {
	    // getClist().clear();
	    // getClist().addAll(new ArrayList<BgCardItem>(clist));
	    this.clist = clist;
	} else {
	    throw new RuntimeException(errMsg);
	}
    }

    private List<BgCardItem> getClist() {
	if (clist == null) {
	    clist = new ArrayList<BgCardItem>();
	}
	return this.clist;
    }

    private void setBlankName(String name) {
	this.blankName = name;
    }

    private String getBlankName() {
	return this.blankName;
    }

    private Direction direction = Direction.UP;

    @Override
    public Direction getInitialDirection() {
	return direction;
    }

    public void setInitialDirection(Direction direction) {
	this.direction = direction;
    }

    private Side side = Side.FACE;

    @Override
    public Side getInitialSide() {
	return side;
    }

    public void setInitialSide(Side side) {
	this.side = side;
    }

    private List<Rectangle> cardRectangles;

    @Override
    public List<Rectangle> getCardRectangles() {
	if (cardRectangles == null) {
	    cardRectangles = new ArrayList<Rectangle>();
	}
	return cardRectangles;
    }

    private BufferedImage createReflection(BufferedImage image, Graphics2D g2) {
	int height = image.getHeight();
	BufferedImage result = new BufferedImage(image.getWidth(),
		((int) (height * 2.0)), BufferedImage.TYPE_INT_ARGB);
	// Graphics2D g2 = result.createGraphics();

	g2.drawImage(image, 0, 0, null);

	g2.scale(1.0, -1.0);
	g2.drawImage(image, 0, -height - height, null);
	g2.scale(1.0, 1.0);

	g2.translate(0, height);

	GradientPaint mask;
	// mask = new GradientPaint(0, 0, new Color(1.0f, 1.0f, 1.0f, 0.5f), 0,
	// height / 2, new Color(1.0f, 1.0f, 1.0f, 0.0f));
	mask = new GradientPaint(0, 0, Color.GREEN, 0, height / 2, Color.RED);
	g2.setPaint(mask);

	g2.setComposite(AlphaComposite.DstIn);

	g2.fillRect(0, 0, image.getWidth(), height);

	g2.dispose();

	return result;
    }

    @Override
    public final void paintComponent(Graphics g) {
	getCardRectangles().clear();

	Graphics2D g2d = (Graphics2D) g.create();
	g2d.setColor(Color.BLACK);
	g2d.fillRect(0, 0, getComponent().getWidth(), getComponent()
		.getHeight());

	// g2d.setColor(Color.BLUE);
	// g2d.fillRect(0, 0, getComponent().getWidth(), getComponent()
	// .getHeight());

	if (getClist().size() == 0) {
	    if (getBlankName() != null && getBlankName().length() > 0) {
		drawBlankName(g2d);
		return;
	    }
	    g2d.dispose();
	    return;
	}

	for (int idx = 0, size = getClist().size(); idx < size; idx++) {
	    BufferedImage bimg = getClist().get(idx).getImage();

	    int sw = bimg.getWidth(null);
	    int sh = bimg.getHeight(null);

	    double rate = ((double) getHorizontalWidth()) / sw;

	    int dw = getHorizontalWidth();
	    int dh = (int) (sh * rate);

	    BufferedImage bImage = BgScaleUtil.getFasterScaledInstance(bimg,
		    dw, dh, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

	    int dx = getMargin().left;
	    int dy = getMargin().top;

	    g2d.drawImage(bImage, dx, dy, null);

	    g2d.scale(1.0, -1.0);
	    g2d.drawImage(bImage, dx, -dy - bImage.getHeight()
		    - bImage.getHeight(), null);
	    g2d.scale(1.0, 1.0);

	    g2d.translate(dx, dy + bImage.getHeight());

	    GradientPaint mask;
	    mask = new GradientPaint(0, 0, new Color(1.0f, 1.0f, 1.0f, 0.5f),
		    0, bImage.getHeight() / 2,
		    new Color(1.0f, 1.0f, 1.0f, 0.0f));

	    g2d.setPaint(mask);

	    g2d.setComposite(AlphaComposite.DstIn);

	    g2d.fillRect(0, 0, bImage.getWidth(), bImage.getHeight());

	}
	g2d.dispose();
    }

    private void drawBlankName(Graphics2D g2d) {
	Font font = g2d.getFont().deriveFont(Font.BOLD | Font.ITALIC, 24f);

	FontMetrics fm = g2d.getFontMetrics(font);
	Rectangle2D bounds = fm.getStringBounds(getBlankName(), g2d);
	double bw = bounds.getWidth();
	double bh = bounds.getHeight();

	int cw = createPaintableWidth();
	int ch = createPaintableHeight();

	double rate = Math.max(bw / (cw * 0.6), bh / (ch * 0.6));

	int dx = (int) (getMargin().left + cw / 2 - bw * rate / 2);
	int dy = (int) (getMargin().top + ch / 2 + bh * rate / 2);

	g2d.setFont(font.deriveFont((float) (24f * rate)));
	g2d.setColor(Color.WHITE);
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.drawString(getBlankName(), dx, dy);

	g2d.dispose();
    }

    private int createPaintableHeight() {
	int ch = getComponent().getHeight();
	int top = getMargin().top;
	int bottom = getMargin().bottom;
	if (top + bottom >= ch) {
	    return ch;
	}
	return ch - top - bottom;
    }

    private int createPaintableWidth() {
	int ch = getComponent().getWidth();
	int left = getMargin().left;
	int right = getMargin().right;
	if (left + right >= ch) {
	    return ch;
	}
	return ch - left - right;
    }

}
