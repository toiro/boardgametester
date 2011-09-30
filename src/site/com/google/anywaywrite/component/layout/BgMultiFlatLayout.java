/**
 *
 */
package site.com.google.anywaywrite.component.layout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
 * 複数枚のカードを平行に並べて表示するレイアウトです。<br />
 * 方向が異なったDirectionのカードを並べることはできません。<br />
 * 
 * @author y-kitajima
 * 
 */
public class BgMultiFlatLayout implements BgAreaLayout {

    public enum FlatDirection {
	LEFT_TO_RIGHT, UP_TO_DOWN, RIGHT_TO_LEFT, DOWN_TO_UP, ;
    }

    public enum BaseEdge {
	WIDTH, HEIGHT, ;
    }

    private Insets margin;
    private Component comp;
    private List<BgCardItem> clist;
    private String blankName;
    private BgCardArea area;

    private int gapPerecentage;
    private FlatDirection flatDirection;

    private BaseEdge baseEdge;

    private BgMultiFlatLayout(FlatDirection flatDirection, BaseEdge baseEdge) {
	setMargin(createDefaultMargin());
	setFlatDirection(flatDirection);
	setBaseEdge(baseEdge);
	setGapPercentage(100);
    }

    @Override
    public String verify(List<BgCardItem> clist) {
	if (clist == null) {
	    return "The BgCardItem list should not be null, but was";
	}
	if (clist.size() == 0) {
	    return null;
	}
	if (existDifferentDirection(clist)) {
	    return "The all cards direction should be same, but was not.";
	}
	BgCardItem ci = clist.get(0);
	switch (getBaseEdge()) {
	case HEIGHT:
	    if (getFlatDirection() == FlatDirection.LEFT_TO_RIGHT
		    || getFlatDirection() == FlatDirection.RIGHT_TO_LEFT) {
		if (ci.getDirection() == Direction.RIGHT
			|| ci.getDirection() == Direction.LEFT) {
		    return "The card direction should be Direction.UP or Direction.REVERSE, but was "
			    + ci.getDirection();
		}
		return null;
	    } else if (getFlatDirection() == FlatDirection.UP_TO_DOWN
		    || getFlatDirection() == FlatDirection.DOWN_TO_UP) {
		if (ci.getDirection() == Direction.UP
			|| ci.getDirection() == Direction.REVERSE) {
		    return "The card direction should be Direction.RIGHT or Direction.LEFT, but was "
			    + ci.getDirection();
		}
		return null;
	    } else {
		throw new RuntimeException("Program error");
	    }
	case WIDTH:
	    if (getFlatDirection() == FlatDirection.LEFT_TO_RIGHT
		    || getFlatDirection() == FlatDirection.RIGHT_TO_LEFT) {
		if (ci.getDirection() == Direction.UP
			|| ci.getDirection() == Direction.REVERSE) {
		    return "The card direction should be Direction.RIGHT or Direction.LEFT, but was "
			    + ci.getDirection();
		}
		return null;
	    } else if (getFlatDirection() == FlatDirection.UP_TO_DOWN
		    || getFlatDirection() == FlatDirection.DOWN_TO_UP) {
		if (ci.getDirection() == Direction.RIGHT
			|| ci.getDirection() == Direction.LEFT) {
		    return "The card direction should be Direction.UP or Direction.REVERSE, but was "
			    + ci.getDirection();
		}
		return null;
	    }
	    break;
	default:
	    throw new RuntimeException("Program Error");
	}

	return null;
    }

    private boolean existDifferentDirection(List<BgCardItem> clist) {
	Direction firstDirection = clist.get(0).getDirection();
	boolean existDifferentDirection = false;
	for (BgCardItem c : clist) {
	    if (c.getDirection() != firstDirection) {
		existDifferentDirection = true;
		break;
	    }
	}
	return existDifferentDirection;
    }

    public static BgMultiFlatLayout newDefaultInstance() {
	return new BgMultiFlatLayout(FlatDirection.LEFT_TO_RIGHT,
		BaseEdge.HEIGHT);
    }

    public static BgMultiFlatLayout newInstance(FlatDirection flatDirection,
	    BaseEdge baseEdge) {
	return new BgMultiFlatLayout(flatDirection, baseEdge);
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

    public void setGapPercentage(int val) {
	this.gapPerecentage = val;
    }

    private int getGapPercentage() {
	return this.gapPerecentage;
    }

    public void setFlatDirection(FlatDirection val) {
	this.flatDirection = val;
    }

    private FlatDirection getFlatDirection() {
	return this.flatDirection;
    }

    public void setBaseEdge(BaseEdge val) {
	this.baseEdge = val;
    }

    private BaseEdge getBaseEdge() {
	return this.baseEdge;
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

	    int ch = createPaintableHeight();
	    int cw = createPaintableWidth();

	    double rate;
	    if (getFlatDirection() == FlatDirection.LEFT_TO_RIGHT
		    || getFlatDirection() == FlatDirection.RIGHT_TO_LEFT) {
		if (getBaseEdge() == BaseEdge.HEIGHT) {
		    rate = ((double) ch) / sh;
		} else if (getBaseEdge() == BaseEdge.WIDTH) {
		    rate = ((double) ch) / sw;
		} else {
		    throw new RuntimeException("Program error.");
		}
	    } else if (getFlatDirection() == FlatDirection.UP_TO_DOWN
		    || getFlatDirection() == FlatDirection.DOWN_TO_UP) {
		if (getBaseEdge() == BaseEdge.HEIGHT) {
		    rate = ((double) cw) / sh;
		} else if (getBaseEdge() == BaseEdge.WIDTH) {
		    rate = ((double) cw) / sw;
		} else {
		    throw new RuntimeException("Program error.");
		}
	    } else {
		throw new RuntimeException("Program error.");
	    }

	    int dh = (int) (sh * rate);
	    int dw = (int) (sw * rate);

	    int dx;
	    int dy;
	    dx = createDestinationXPotision(dw, dh, getBaseEdge(), idx);
	    dy = createDestinationYPosition(dw, dh, getBaseEdge(), idx);

	    BufferedImage bImage = BgScaleUtil.getFasterScaledInstance(bimg,
		    dw, dh, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

	    switch (getFlatDirection()) {
	    case LEFT_TO_RIGHT:
		drawFromLeftToRigth(g2d, idx, dh, dw, dx, dy, bImage);
		break;
	    case RIGHT_TO_LEFT:
		break;
	    case UP_TO_DOWN:
		drawFromUpToDown(g2d, idx, dh, dw, dx, dy, bImage);
		break;
	    case DOWN_TO_UP:
		drawFromDownToUp(g2d, idx, dh, dw, dx, dy, bImage);
		break;
	    default:
		throw new RuntimeException("Program error");
	    }

	    Rectangle cr = getCardRectangles().get(idx);
	    if (getArea().getSelectedIndexes().contains(idx)) {

		g2d.setColor(getArea().getSelectionCardBorderColor());
		g2d.setStroke(new BasicStroke(4.0f));

		g2d.drawRect(cr.x + 3, cr.y + 2, cr.width - 6, cr.height - 4);
	    } else {
		g2d.setColor(new Color(0, 0, 0, 0));
		g2d.setStroke(new BasicStroke(4.0f));
		g2d.drawRect(cr.x + 3, cr.y + 2, cr.width - 6, cr.height - 4);
	    }
	}
	if (getArea().getCards().size() > 0) {
	    switch (getFlatDirection()) {
	    case LEFT_TO_RIGHT:
		Rectangle rec = getCardRectangles().get(
			getArea().getCards().size() - 1);
		getArea().getComponent().setPreferredSize(
			new Dimension(rec.x + rec.width + getMargin().right,
				(int) getArea().getComponent()
					.getPreferredSize().getHeight()));
		break;
	    case RIGHT_TO_LEFT:
		break;
	    case UP_TO_DOWN:
		rec = getCardRectangles().get(getArea().getCards().size() - 1);
		getArea().getComponent().setPreferredSize(
			new Dimension((int) getArea().getComponent()
				.getPreferredSize().getWidth(), rec.y
				+ rec.height + getMargin().bottom));
		break;
	    case DOWN_TO_UP:
		break;
	    default:
		throw new RuntimeException("Program error");
	    }
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

    private void drawFromLeftToRigth(Graphics2D g2d, int idx, int dh, int dw,
	    int dx, int dy, BufferedImage bImage) {
	AffineTransform form = new AffineTransform();
	switch (getClist().get(idx).getDirection()) {
	case UP:
	    form.translate(dx, dy);
	    g2d.drawImage(bImage, form, null);

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

	    getCardRectangles()
		    .add(
			    new Rectangle(dx, dy, bImage.getHeight(), bImage
				    .getWidth()));
	    break;
	default:
	    throw new RuntimeException("Program error.");
	}
    }

    private void drawFromUpToDown(Graphics2D g2d, int idx, int dh, int dw,
	    int dx, int dy, BufferedImage bImage) {
	AffineTransform form = new AffineTransform();
	switch (getClist().get(idx).getDirection()) {
	case UP:
	    form.translate(dh, dy);
	    g2d.drawImage(bImage, form, null);

	    getCardRectangles()
		    .add(
			    new Rectangle(dx, dy, bImage.getWidth(), bImage
				    .getHeight()));
	    break;
	case RIGHT:
	    form.translate(dh + dx, dy);
	    form.rotate(Math.toRadians(90));
	    g2d.drawImage(bImage, form, null);

	    getCardRectangles()
		    .add(
			    new Rectangle(dx, dy, bImage.getHeight(), bImage
				    .getWidth()));
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
	    form.translate(dx, dw + dy);
	    form.rotate(Math.toRadians(270));
	    g2d.drawImage(bImage, form, null);

	    getCardRectangles()
		    .add(
			    new Rectangle(dx, dy, bImage.getHeight(), bImage
				    .getWidth()));
	    break;
	default:
	    throw new RuntimeException("Program error.");
	}
    }

    private void drawFromDownToUp(Graphics2D g2d, int idx, int dh, int dw,
	    int dx, int dy, BufferedImage bImage) {
	AffineTransform form = new AffineTransform();
	switch (getClist().get(idx).getDirection()) {
	case UP:
	    form.translate(dh, dy);
	    g2d.drawImage(bImage, form, null);

	    getCardRectangles()
		    .add(
			    new Rectangle(dx, dy, bImage.getWidth(), bImage
				    .getHeight()));
	    break;
	case RIGHT:
	    form.translate(dh + dx, dy);
	    form.rotate(Math.toRadians(90));
	    g2d.drawImage(bImage, form, null);

	    getCardRectangles()
		    .add(
			    new Rectangle(dx, dy, bImage.getHeight(), bImage
				    .getWidth()));
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
	    form.translate(dx, dw + dy);
	    form.rotate(Math.toRadians(270));
	    g2d.drawImage(bImage, form, null);

	    getCardRectangles()
		    .add(
			    new Rectangle(dx, dy, bImage.getHeight(), bImage
				    .getWidth()));
	    break;
	default:
	    throw new RuntimeException("Program error.");
	}
    }

    private int createDestinationXPotision(int dw, int dh, BaseEdge edge,
	    int idx) {
	if (getMargin().left + getMargin().right >= getComponent().getWidth()) {
	    return 0;
	}

	switch (edge) {
	case HEIGHT:
	    if (getFlatDirection() == FlatDirection.LEFT_TO_RIGHT) {
		return (int) (getMargin().left + (((double) dw)
			* getGapPercentage() / 100)
			* idx);
	    } else if (getFlatDirection() == FlatDirection.RIGHT_TO_LEFT) {
		return (int) (getComponent().getWidth() - getMargin().right
			- dw - (((double) dw) * getGapPercentage() / 100) * idx);
	    } else if (getFlatDirection() == FlatDirection.UP_TO_DOWN
		    || getFlatDirection() == FlatDirection.DOWN_TO_UP) {
		return getMargin().left;
	    } else {
		throw new RuntimeException("Program error");
	    }
	case WIDTH:
	    if (getFlatDirection() == FlatDirection.LEFT_TO_RIGHT) {
		return (int) (getMargin().left + (((double) dh)
			* getGapPercentage() / 100)
			* idx);
	    } else if (getFlatDirection() == FlatDirection.RIGHT_TO_LEFT) {
		return (int) (getComponent().getWidth() - getMargin().right
			- dh - (((double) dh) * getGapPercentage() / 100) * idx);
	    } else if (getFlatDirection() == FlatDirection.UP_TO_DOWN
		    || getFlatDirection() == FlatDirection.DOWN_TO_UP) {
		return getMargin().left;
	    } else {
		throw new RuntimeException("Program error");
	    }
	default:
	    throw new RuntimeException("Program error");
	}
    }

    private int createDestinationYPosition(int dw, int dh, BaseEdge edge,
	    int idx) {
	if (getMargin().top + getMargin().bottom >= getComponent().getHeight()) {
	    return 0;
	}

	switch (edge) {
	case HEIGHT:
	    if (getFlatDirection() == FlatDirection.UP_TO_DOWN) {
		return (int) (getMargin().top + (((double) dw)
			* getGapPercentage() / 100)
			* idx);
	    } else if (getFlatDirection() == FlatDirection.DOWN_TO_UP) {
		return (int) (getComponent().getHeight() - getMargin().bottom
			- dw - (((double) dw) * getGapPercentage() / 100) * idx);
	    } else if (getFlatDirection() == FlatDirection.LEFT_TO_RIGHT
		    || getFlatDirection() == FlatDirection.RIGHT_TO_LEFT) {
		return getMargin().top;
	    } else {
		throw new RuntimeException("Program error");
	    }
	case WIDTH:
	    if (getFlatDirection() == FlatDirection.UP_TO_DOWN) {
		return (int) (getMargin().top + (((double) dh)
			* getGapPercentage() / 100)
			* idx);
	    } else if (getFlatDirection() == FlatDirection.DOWN_TO_UP) {
		return (int) (getComponent().getHeight() - getMargin().bottom
			- dh - (((double) dh) * getGapPercentage() / 100) * idx);
	    } else if (getFlatDirection() == FlatDirection.LEFT_TO_RIGHT
		    || getFlatDirection() == FlatDirection.RIGHT_TO_LEFT) {
		return getMargin().top;
	    } else {
		throw new RuntimeException("Program error");
	    }
	default:
	    throw new RuntimeException("Program error");
	}

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
