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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
public class BgMultiSimpleDisplayLayout implements BgAreaLayout {

    private Insets margin;
    private Component comp;
    private List<BgCardItem> clist;
    private String blankName;
    private BgCardArea area;

    private int horizontalWidth;
    private int gapHorizontalPerecentage;
    private int gapVerticalPercentage;

    private BgMultiSimpleDisplayLayout(int horizontalWidth) {
	setMargin(createDefaultMargin());
	setGapHorizontalPerecentage(150);
	setGapVerticalPercentage(120);
	setHorizontalWidth(horizontalWidth);
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
	if (ci.getDirection() != Direction.UP) {
	    return "The all cards direction should be Direction.UP, but was "
		    + ci.getDirection();
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

    public static BgMultiSimpleDisplayLayout newDefaultInstance() {
	return new BgMultiSimpleDisplayLayout(130);
    }

    public static BgMultiSimpleDisplayLayout newInstance(int horizontalWidth) {
	return new BgMultiSimpleDisplayLayout(horizontalWidth);
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

    public final int getHorizontalWidth() {
	return horizontalWidth;
    }

    private final void setHorizontalWidth(int horizontalWidth) {
	this.horizontalWidth = horizontalWidth;
    }

    private final int getGapHorizontalPerecentage() {
	return gapHorizontalPerecentage;
    }

    private final int getGapVerticalPercentage() {
	return gapVerticalPercentage;
    }

    public final void setGapHorizontalPerecentage(int gapHorizontalPerecentage) {
	this.gapHorizontalPerecentage = gapHorizontalPerecentage;
    }

    public final void setGapVerticalPercentage(int gapVerticalPercentage) {
	this.gapVerticalPercentage = gapVerticalPercentage;
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

	g2d.setColor(Color.BLACK);
	g2d.fillRect(0, 0, getComponent().getWidth(), getComponent()
		.getHeight());

	if (getClist().size() == 0) {
	    if (getBlankName() != null && getBlankName().length() > 0) {
		drawBlankName(g2d);
		return;
	    }
	    g2d.dispose();
	    return;
	}

	int cw = createPaintableWidth();

	int horizontalNo = calcHorizontalNo(cw);
	if (horizontalNo == 0) {
	    g2d.dispose();
	    return;
	}

	int hNo = 0;
	int vNo = 0;
	Map<Integer, Integer> maxHeightMap = new LinkedHashMap<Integer, Integer>();

	for (int idx = 0, size = getClist().size(); idx < size; idx++) {
	    hNo = calcHNo(idx, horizontalNo);
	    vNo = calcVNo(idx, horizontalNo);

	    BufferedImage bimg = getClist().get(idx).getImage();

	    int sw = bimg.getWidth(null);
	    int sh = bimg.getHeight(null);

	    double rate = ((double) getHorizontalWidth()) / sw;

	    int dh = (int) (sh * rate);
	    int dw = (int) (sw * rate);

	    int dx;
	    int dy;
	    dx = createDestinationXPotision(dw, hNo);
	    dy = createDestinationYPosition(dh, vNo);

	    BufferedImage bImage = BgScaleUtil.getFasterScaledInstance(bimg,
		    dw, dh, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

	    drawFromLeftToRigth(g2d, dx, dy, bImage);

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

	    if (!maxHeightMap.containsKey(vNo)) {
		maxHeightMap.put(vNo, dh);
	    } else {
		if (maxHeightMap.get(vNo) < dh) {
		    maxHeightMap.put(vNo, dh);
		}
	    }
	}

	g2d.dispose();

	int size = maxHeightMap.size();
	if (size == 0) {
	    return;
	}
	int idx = 0;
	int accHeight = 0;
	for (Iterator<Entry<Integer, Integer>> it = maxHeightMap.entrySet()
		.iterator(); it.hasNext();) {
	    Entry<Integer, Integer> entry = it.next();
	    if (idx == size - 1) {
		accHeight += entry.getValue();
	    } else {
		accHeight += entry.getValue()
			* (((double) getGapVerticalPercentage()) / 100);
	    }
	    idx++;
	}

	setPreferredHeight(getMargin().top + accHeight + getMargin().bottom);
    }

    int preferredHeight;

    private void setPreferredHeight(int height) {
	this.preferredHeight = height;
    }

    public int getPreferredHeight() {
	return this.preferredHeight;
    }

    private int calcHorizontalNo(int paintableWidth) {
	int ret = 0;
	int accWidth = 0;
	int infinitGaurdCounter = 0;
	while (accWidth < paintableWidth && infinitGaurdCounter < 1000) {
	    infinitGaurdCounter++;
	    if (ret == 0) {
		accWidth += getHorizontalWidth();
	    } else {
		double rate = ((double) getGapHorizontalPerecentage()) / 100;
		accWidth += getHorizontalWidth() * rate;
	    }
	    ret++;
	}
	return ret - 1;
    }

    private int calcHNo(int idx, int horizontalNo) {
	// if (idx % horizontalNo != 0) {
	// return idx % horizontalNo;
	// }
	// return horizontalNo;
	return (idx % horizontalNo) + 1;
    }

    private int calcVNo(int idx, int horizontalNo) {
	// if (idx == 0) {
	// return 1;
	// }
	// int no = idx / horizontalNo;
	// if (idx % horizontalNo != 0) {
	// return no + 1;
	// }
	// return no;
	return (idx / horizontalNo) + 1;
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

    private void drawFromLeftToRigth(Graphics2D g2d, int dx, int dy,
	    BufferedImage bImage) {
	AffineTransform form = new AffineTransform();
	form.translate(dx, dy);
	g2d.drawImage(bImage, form, null);

	// g2d.drawImage(bImage, dx, dy, null);
	getCardRectangles().add(
		new Rectangle(dx, dy, bImage.getWidth(), bImage.getHeight()));
    }

    private int createDestinationXPotision(int dw, int hNo) {
	if (getMargin().left + getMargin().right >= getComponent().getWidth()) {
	    return 0;
	}

	return (int) (getMargin().left + dw
		* ((double) getGapHorizontalPerecentage() / 100) * (hNo - 1));
    }

    private int createDestinationYPosition(int dh, int vNo) {
	if (getMargin().top + getMargin().bottom >= getComponent().getHeight()) {
	    return 0;
	}
	return (int) (getMargin().top + dh
		* ((double) getGapVerticalPercentage() / 100) * (vNo - 1));
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
