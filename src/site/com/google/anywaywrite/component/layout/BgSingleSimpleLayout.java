/**
 *
 */
package site.com.google.anywaywrite.component.layout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
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
 * �P��̃J�[�h��\�����邽�߂̃��C�A�E�g�ł��B �J�[�h���^�b�v����X�y�[�X���m�ۂ��܂���B
 * 
 * @author y-kitajima
 * 
 */
public class BgSingleSimpleLayout implements BgAreaLayout {

    private Insets margin;
    private BgLayoutBase base;
    private BgLayoutPosition position;
    private Component comp;
    private List<BgCardItem> clist;
    private String blankName;
    private BgCardArea area;

    private BgSingleSimpleLayout(BgLayoutBase base, BgLayoutPosition position) {
	setMargin(createDefaultMargin());
	setPosition(position);
	setBase(base);
    }

    @Override
    public String verify(List<BgCardItem> clist) {
	if (clist == null) {
	    return "The BgCardItem list should not be null, but was";
	}
	if (clist.size() != 0 && clist.size() != 1) {
	    return "The BgCardItem list size should be ZERO or ONE, but was "
		    + clist.size();
	}
	boolean existIllegalDirection = false;
	for (BgCardItem c : clist) {
	    if (c.getDirection() != Direction.UP
		    && c.getDirection() != Direction.REVERSE) {
		existIllegalDirection = true;
		break;
	    }
	}
	if (existIllegalDirection) {
	    return "The All CardItems Direction should be UP or REVERSE, but existed Illegal direction cardItem.";
	}
	return null;
    }

    public static BgSingleSimpleLayout newDefaultInstance() {
	return new BgSingleSimpleLayout(BgLayoutBase.BOTH,
		BgLayoutPosition.CENTER);
    }

    public static BgSingleSimpleLayout newInstance(BgLayoutBase base,
	    BgLayoutPosition position) {
	return new BgSingleSimpleLayout(base, position);
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
	this.position = position;
    }

    private BgLayoutPosition getPosition() {
	return this.position;
    }

    @Override
    public final void setBase(BgLayoutBase base) {
	this.base = base;
    }

    private BgLayoutBase getBase() {
	return this.base;
    }

    @Override
    public void setArea(BgCardArea area) {
	if (area == null) {
	    throw new RuntimeException("area should not be null, but was.");
	}
	setClist(area.getCards());
	setComponent(area.getComponent());
	setBlankName(area.getAreaName());
	this.area = area;
    }

    private BgCardArea getArea() {
	return this.area;
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

    @Override
    public final void paintComponent(Graphics g) {
	getCardRectangles().clear();
	Graphics2D g2d = (Graphics2D) g.create();
	// Graphics2D g2d = (Graphics2D) getG2d().create();

	// g2d.setColor(Color.BLUE);
	// g2d.fillRect(0, 0, getComponent().getWidth(), getComponent()
	// .getHeight());

	if (getClist().size() == 0) {
	    if (getBlankName() != null && getBlankName().length() > 0) {
		Font font = g.getFont()
			.deriveFont(Font.BOLD | Font.ITALIC, 24f);

		FontMetrics fm = g.getFontMetrics(font);
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
		return;
	    }
	    g2d.dispose();
	    return;
	}

	BufferedImage bimg;
	bimg = getClist().get(0).getImage();

	int sw = bimg.getWidth(null);
	int sh = bimg.getHeight(null);

	int ch = createPaintableHeight();
	int cw = createPaintableWidth();

	double rh = ((double) ch) / sh;
	double rw = ((double) cw) / sw;

	double rate;
	switch (getBase()) {
	case BOTH:
	    rate = Math.min(rh, rw);
	    break;
	case HORIZONTAL:
	    rate = rw;
	    break;
	case VERTICAL:
	    rate = rh;
	    break;
	default:
	    throw new IllegalArgumentException();
	}

	int dh = (int) (sh * rate);
	int dw = (int) (sw * rate);

	int dx1 = createDestinationXPotision(dw);
	int dy1 = createDestinationYPosition(dh);

	BufferedImage bImage = BgScaleUtil.getFasterScaledInstance(bimg, dw,
		dh, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
	AffineTransform form = new AffineTransform();
	if (getClist().get(0).getDirection() == Direction.UP) {
	    form.translate(dx1, dy1);
	    g2d.drawImage(bImage, form, null);

	    // g2d.drawImage(bImage, dx1, dy1, null);
	    getCardRectangles().add(
		    new Rectangle(dx1, dy1, bImage.getWidth(), bImage
			    .getHeight()));
	} else if (getClist().get(0).getDirection() == Direction.REVERSE) {
	    form.translate(dw + dx1, dh + dy1);
	    form.rotate(Math.toRadians(180));
	    g2d.drawImage(bImage, form, null);

	    // g2d.translate(dw + dx1, dh + dy1);
	    // g2d.rotate(Math.toRadians(180));
	    // g2d.drawImage(bImage, 0, 0, null);
	    getCardRectangles().add(
		    new Rectangle(dx1, dy1, bImage.getWidth(), bImage
			    .getHeight()));
	}
	Rectangle cr = getCardRectangles().get(0);
	if (getArea().getSelectedIndexes().contains(0)) {

	    g2d.setColor(getArea().getSelectionCardBorderColor());
	    g2d.setStroke(new BasicStroke(4.0f));

	    g2d.drawRect(cr.x + 3, cr.y + 2, cr.width - 6, cr.height - 4);
	} else {
	    g2d.setColor(new Color(0, 0, 0, 0));
	    g2d.setStroke(new BasicStroke(4.0f));
	    g2d.drawRect(cr.x + 3, cr.y + 2, cr.width - 6, cr.height - 4);
	}

	g2d.dispose();
    }

    private int createDestinationXPotision(int dw) {
	if (getMargin().left + getMargin().right >= getComponent().getWidth()) {
	    return 0;
	}
	if (getPosition() == BgLayoutPosition.CENTER
		|| getPosition() == BgLayoutPosition.NORTH
		|| getPosition() == BgLayoutPosition.SOUTH) {
	    return getMargin().left + (createPaintableWidth() - dw) / 2;
	}
	if (getPosition() == BgLayoutPosition.WEST
		|| getPosition() == BgLayoutPosition.NORTH_WEST
		|| getPosition() == BgLayoutPosition.SOUTH_WEST) {
	    return getMargin().left;
	}
	if (getPosition() == BgLayoutPosition.EAST
		|| getPosition() == BgLayoutPosition.NORTH_EAST
		|| getPosition() == BgLayoutPosition.SOUTH_EAST) {
	    return getComponent().getWidth() - getMargin().right - dw;
	}
	throw new RuntimeException("Program error");
    }

    private int createDestinationYPosition(int dh) {
	if (getMargin().top + getMargin().bottom >= getComponent().getHeight()) {
	    return 0;
	}
	if (getPosition() == BgLayoutPosition.CENTER
		|| getPosition() == BgLayoutPosition.WEST
		|| getPosition() == BgLayoutPosition.EAST) {
	    return getMargin().top + (createPaintableHeight() - dh) / 2;
	}
	if (getPosition() == BgLayoutPosition.NORTH
		|| getPosition() == BgLayoutPosition.NORTH_WEST
		|| getPosition() == BgLayoutPosition.NORTH_EAST) {
	    return getMargin().top;
	}
	if (getPosition() == BgLayoutPosition.SOUTH
		|| getPosition() == BgLayoutPosition.SOUTH_WEST
		|| getPosition() == BgLayoutPosition.SOUTH_EAST) {
	    return getComponent().getHeight() - getMargin().bottom - dh;
	}
	throw new RuntimeException("Program error");
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
