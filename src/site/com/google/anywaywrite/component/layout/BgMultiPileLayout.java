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
 * �J�[�h�̎R��\�����邽�߂̃��C�A�E�g�ł��B �J�[�h���^�b�v����X�y�[�X���m�ۂ��܂���B
 * 
 * @author y-kitajima
 * 
 */
public class BgMultiPileLayout implements BgAreaLayout {

    private Insets margin;
    private Component comp;
    private BgLayoutBase base;
    private List<BgCardItem> clist;
    private String blankName;
    private BgCardArea area;

    private int maxCardNo;
    private int horizontalGapPercentage;
    private int verticalGapPercentage;

    private BgMultiPileLayout(BgLayoutBase base, int maxCardNo, int hgp, int vgp) {
	setMargin(createDefaultMargin());
	setBase(base);
	setMaxCardNo(maxCardNo);
	setHorizontalGapPercentage(hgp);
	setVerticalGapPercentage(vgp);
    }

    @Override
    public String verify(List<BgCardItem> clist) {
	if (clist == null) {
	    return "The BgCardItem list should not be null, but was";
	}
	if (clist.size() == 0) {
	    return null;
	}
	if (clist.size() > getMaxCardNo()) {
	    return "The site.com.google.anywaywrite.card no should be less than or equal maxCardNo["
		    + getMaxCardNo() + "], but was " + clist.size() + ".";
	}
	if (existDifferentDirection(clist)) {
	    return "The all cards direction should be same, but was not.";
	}
	if (clist.size() > 0) {
	    if (clist.get(0).getDirection() != Direction.UP) {
		return "The site.com.google.anywaywrite.card direction should be Direction.UP, but was "
			+ clist.get(0).getDirection();
	    }
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

    public static BgMultiPileLayout newDefaultInstance(BgLayoutBase base,
	    int maxCardNo) {
	return new BgMultiPileLayout(base, maxCardNo, 15, 15);
    }

    public static BgMultiPileLayout newInstance(BgLayoutBase base,
	    int maxCardNo, int horizontalGapPercentage,
	    int verticalGapPercentage) {
	return new BgMultiPileLayout(base, maxCardNo, horizontalGapPercentage,
		verticalGapPercentage);
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

    public void setMaxCardNo(int maxNo) {
	this.maxCardNo = maxNo;
    }

    public int getMaxCardNo() {
	return this.maxCardNo;
    }

    public void setHorizontalGapPercentage(int hp) {
	this.horizontalGapPercentage = hp;
    }

    public int getHorizontalGapPercentage() {
	return this.horizontalGapPercentage;
    }

    public void setVerticalGapPercentage(int vp) {
	this.verticalGapPercentage = vp;
    }

    public int getVerticalGapPercentage() {
	return this.verticalGapPercentage;
    }

    @Override
    public final void setPosition(BgLayoutPosition position) {
	throw new RuntimeException("This method is unsupported.");
    }

    @Override
    public final void setBase(BgLayoutBase base) {
	this.base = base;
    }

    public BgLayoutBase getBase() {
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

    private Side side = Side.DOWN;

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

	// g2d.getClipBounds();

	for (int idx = 0, size = getClist().size(); idx < size; idx++) {
	    BufferedImage bimg = getClist().get(idx).getImage();

	    int sw = bimg.getWidth(null);
	    int sh = bimg.getHeight(null);

	    int ch = createPaintableHeight();
	    int cw = createPaintableWidth();

	    double rate;
	    switch (getBase()) {
	    case HORIZONTAL:
		rate = cw
			/ (sw + sw * ((double) getHorizontalGapPercentage())
				/ 100);
		break;
	    case VERTICAL:
		rate = ch
			/ (sh + sh * ((double) getVerticalGapPercentage())
				/ 100);
		break;
	    case BOTH:
		rate = Math.min((cw / (sw + sw
			* ((double) getHorizontalGapPercentage()) / 100)), ch
			/ (sh + sh * ((double) getVerticalGapPercentage())
				/ 100));
		break;
	    default:
		throw new RuntimeException("Program Error");
	    }

	    int dh = (int) (sh * rate);
	    int dw = (int) (sw * rate);

	    int dx;
	    int dy;
	    dx = createDestinationXPotision(dw, idx);
	    dy = createDestinationYPosition(dh, idx);

	    BufferedImage bImage = BgScaleUtil.getFasterScaledInstance(bimg,
		    dw, dh, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

	    AffineTransform form = new AffineTransform();
	    form.translate(dx, dy);
	    g2d.drawImage(bImage, form, null);

	    // g2d.drawImage(bImage, dx, dy, null);
	    if (idx == size - 1) {
		getCardRectangles().add(
			new Rectangle(dx, dy, bImage.getWidth(), bImage
				.getHeight()));

		Rectangle cr = getCardRectangles().get(idx);
		if (getArea().getSelectedIndexes().contains(idx)) {

		    g2d.setColor(getArea().getSelectionCardBorderColor());
		    g2d.setStroke(new BasicStroke(4.0f));

		    g2d.drawRect(cr.x + 3, cr.y + 2, cr.width - 6,
			    cr.height - 4);
		} else {
		    g2d.setColor(new Color(0, 0, 0, 0));
		    g2d.setStroke(new BasicStroke(4.0f));
		    g2d.drawRect(cr.x + 3, cr.y + 2, cr.width - 6,
			    cr.height - 4);
		}
	    } else {
		getCardRectangles().add(new Rectangle(0, 0, 0, 0));// �J�[�h�G���A�Ƃ��Ă͕ێ����Ȃ�
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

    private int createDestinationXPotision(int dw, int idx) {
	if (getMargin().left + getMargin().right >= getComponent().getWidth()) {
	    return 0;
	}

	return getMargin().left
		+ (int) (dw * (((double) getHorizontalGapPercentage() / 100)) * (((double) idx / getMaxCardNo())));
    }

    private int createDestinationYPosition(int dh, int idx) {
	if (getMargin().top + getMargin().bottom >= getComponent().getHeight()) {
	    return 0;
	}
	return getComponent().getHeight()
		- getMargin().bottom
		- dh
		- (int) (dh * (((double) getVerticalGapPercentage() / 100)) * (((double) idx / getMaxCardNo())));
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
