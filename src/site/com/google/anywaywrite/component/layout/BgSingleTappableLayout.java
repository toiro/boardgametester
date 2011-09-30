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
 * �P��̃J�[�h��\�����邽�߂̃��C�A�E�g�ł��B
 * �J�[�h���^�b�v����X�y�[�X���m�ۂ��邽�߁A����`�̃G���A��z�肵�Ă��܂��B
 * 
 * @author y-kitajima
 * 
 */
public class BgSingleTappableLayout implements BgAreaLayout {

    private Insets margin;
    private BgLayoutPosition position;
    private Component comp;
    private List<BgCardItem> clist;
    private String blankName;
    private BgCardArea area;

    private BgSingleTappableLayout(BgLayoutPosition position) {
	setMargin(createDefaultMargin());
	setPosition(position);
    }

    public String verify(List<BgCardItem> clist) {
	if (clist == null) {
	    return "The BgCardItem list should not be null, but was";
	}
	if (clist.size() != 0 && clist.size() != 1) {
	    return "The BgCardItem list size should be ZERO or ONE, but was "
		    + clist.size();
	}
	return null;
    }

    public static BgSingleTappableLayout newDefaultInstance() {
	return new BgSingleTappableLayout(BgLayoutPosition.CENTER);
    }

    public static BgSingleTappableLayout newInstance(BgLayoutPosition position) {
	return new BgSingleTappableLayout(position);
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
	throw new RuntimeException(
		"BgSingleTappableLayout does not support this method.");
    }

    @Override
    public void setArea(BgCardArea area) {
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
	if (getComponent() == null) {
	    throw new IllegalStateException(
		    "This method should not be called because component is not setted.");
	}

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

	double rate = ((double) Math.min(ch, cw)) / Math.max(sh, sw);

	int dh = (int) (sh * rate);
	int dw = (int) (sw * rate);

	int dx = createDestinationXPotision(dw, dh);
	int dy = createDestinationYPosition(dw, dh);

	BufferedImage bImage;
	// if (dw < sw) {
	bImage = BgScaleUtil.getFasterScaledInstance(bimg, dw, dh,
		RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
	// } else if (dw == sw) {
	// bImage = bimg;
	// } else {
	// bImage = BgScaleUtil.getFasterBiggerInstance(bimg, dw, dh,
	// RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
	// }

	AffineTransform form = new AffineTransform();
	switch (getClist().get(0).getDirection()) {
	case UP:
	    form.translate(dx, dy);
	    g2d.drawImage(bImage, form, null);

	    // g2d.drawImage(bImage, dx, dy, null);
	    getCardRectangles()
		    .add(
			    new Rectangle(dx, dy, bImage.getWidth(), bImage
				    .getHeight()));
	    break;
	case RIGHT:
	    form.translate(dh + dx, dh - dw + dy);
	    form.rotate(Math.toRadians(90));
	    g2d.drawImage(bImage, form, null);

	    getCardRectangles().add(
		    new Rectangle(dx, dh - dw + dy, bImage.getHeight(), bImage
			    .getWidth()));
	    // getCardRectangles().add(
	    // new Rectangle(dx, dh + dy, bImage.getHeight(), bImage
	    // .getWidth()));
	    break;
	case REVERSE:
	    form.translate(dw + dx, dh + dy);
	    form.rotate(Math.toRadians(180));
	    g2d.drawImage(bImage, form, null);

	    getCardRectangles()
		    .add(
			    new Rectangle(dx, dy, bImage.getWidth(), bImage
				    .getHeight()));
	    break;
	case LEFT:
	    form.translate(dx, dh + dy);
	    form.rotate(Math.toRadians(270));
	    g2d.drawImage(bImage, form, null);

	    // getCardRectangles()
	    // .add(
	    // new Rectangle(dx, dy, bImage.getHeight(), bImage
	    // .getWidth()));
	    getCardRectangles().add(
		    new Rectangle(dx, dh - dw + dy, bImage.getHeight(), bImage
			    .getWidth()));
	    break;
	default:
	    throw new RuntimeException("Program error.");
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

    private int createDestinationXPotision(int dw, int dh) {
	if (getMargin().left + getMargin().right >= getComponent().getWidth()) {
	    return 0;
	}
	if (getPosition() == BgLayoutPosition.CENTER
		|| getPosition() == BgLayoutPosition.NORTH
		|| getPosition() == BgLayoutPosition.SOUTH) {
	    if (getClist().get(0).getDirection() == Direction.UP
		    || getClist().get(0).getDirection() == Direction.REVERSE) {
		return getMargin().left + (createPaintableWidth() - dw) / 2;
	    }
	    return getMargin().left + (createPaintableWidth() - dh) / 2;
	}
	if (getPosition() == BgLayoutPosition.WEST
		|| getPosition() == BgLayoutPosition.NORTH_WEST
		|| getPosition() == BgLayoutPosition.SOUTH_WEST) {
	    return getMargin().left;
	}
	if (getPosition() == BgLayoutPosition.EAST
		|| getPosition() == BgLayoutPosition.NORTH_EAST
		|| getPosition() == BgLayoutPosition.SOUTH_EAST) {
	    if (getClist().get(0).getDirection() == Direction.UP
		    || getClist().get(0).getDirection() == Direction.REVERSE) {
		return getComponent().getWidth() - getMargin().right - dw;
	    }
	    return getComponent().getWidth() - getMargin().right - dh;
	}
	throw new RuntimeException("Program error");
    }

    private int createDestinationYPosition(int dw, int dh) {
	if (getMargin().top + getMargin().bottom >= getComponent().getHeight()) {
	    return 0;
	}
	if (getPosition() == BgLayoutPosition.CENTER
		|| getPosition() == BgLayoutPosition.WEST
		|| getPosition() == BgLayoutPosition.EAST) {
	    if (getClist().get(0).getDirection() == Direction.UP
		    || getClist().get(0).getDirection() == Direction.REVERSE) {
		return getMargin().top + (createPaintableHeight() - dh) / 2;
	    }
	    return getMargin().top + ((createPaintableHeight() - dw) / 2)
		    + (dw - dh);
	}
	if (getPosition() == BgLayoutPosition.NORTH
		|| getPosition() == BgLayoutPosition.NORTH_WEST
		|| getPosition() == BgLayoutPosition.NORTH_EAST) {
	    if (getClist().get(0).getDirection() == Direction.UP
		    || getClist().get(0).getDirection() == Direction.REVERSE) {
		return getMargin().top;
	    }
	    return getMargin().top + (dw - dh);
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
