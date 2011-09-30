package site.com.google.anywaywrite.component.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import site.com.google.anywaywrite.component.BgCardPointChangedHook;
import site.com.google.anywaywrite.component.BgCardSelectionChangedHook;
import site.com.google.anywaywrite.component.layout.BgAreaLayout;
import site.com.google.anywaywrite.component.layout.BgSingleSimpleLayout;
import site.com.google.anywaywrite.component.menu.BgCreateActionMenu;
import site.com.google.anywaywrite.component.menu.BgCreateAllDirectionActionMenu;
import site.com.google.anywaywrite.component.menu.BgCreateAllMoveActionMenu;
import site.com.google.anywaywrite.component.menu.BgCreateAllSideActionMenu;
import site.com.google.anywaywrite.component.menu.BgCreateAreaDetailActionMenu;
import site.com.google.anywaywrite.component.menu.BgCreateCardDetailActionMenu;
import site.com.google.anywaywrite.component.menu.BgCreateCardDirectionActionMenu;
import site.com.google.anywaywrite.component.menu.BgCreateCardMoveActionMenu;
import site.com.google.anywaywrite.component.menu.BgCreateCardSideActionMenu;
import site.com.google.anywaywrite.component.menu.BgCreateShuffleActionMenu;
import site.com.google.anywaywrite.component.menu.BgMenuItem;
import site.com.google.anywaywrite.item.card.BgCardItem;

/**
 * ボードゲームにおいてコンポーネントを置いたりする場所の基準となる 【エリア】を描画するクラスです。 現在は、カードが置かれることのみを想定しています。
 * 
 * @author y-kitajima
 * 
 */
public class BgAreaLabel extends JLabel implements BgCardArea {
    private static final long serialVersionUID = 1L;

    /** ���̉�ʂ� */
    private final List<BgCardItem> cards;
    private BgAreaLayout areaLayout;
    private String areaName;
    private Set<Integer> selectedIndexes;
    private int pointedCardIndex;
    private boolean displayAreaName;
    private Color borderColor;
    private Color selectionCardBorderColor;

    private static final String AREA_NAME_SUFFIX = " ▼ ";
    private BgAreaNamePosition areaNamePosition;
    private boolean inAreaName;
    private float[] areaNameFractions;
    private Color[] areaNameGradientColors;
    private Font areaNameFont;
    private Color areaNameFontColor;
    private Color areaNameSelectionFontColor;
    private JPopupMenu areaActionMenu;
    private JPopupMenu cardActionMenu;

    private Map<String, BgCreateActionMenu> areaActionMenuMap;
    private Map<String, BgCreateActionMenu> selectedCardActionMenuMap;
    private Map<String, BgCreateActionMenu> pointedCardActionMenuMap;

    private BgAreaLabelPane playBoard;
    private BgTempAreaInternalFrame playFrame;

    public enum BgAreaNamePosition {
	LEFT_TOP, LEFT_BOTTOM, RIGHT_BOTTOM, RIGHT_TOP;
    }

    public enum BgDefaultAreaActionMenuMame {
	allMove, allDirection, allSide, shuffle, detail, ;

	public String getActionName() {
	    return this.name();
	}
    }

    public enum BgDefaultCardActionMenuMame {
	move, direction, side, detail, ;

	public String getActionName() {
	    return this.name();
	}
    }

    private BgAreaLabel(List<BgCardItem> cards, BgAreaLayout layout,
	    boolean displayAreaName) {
	super();
	setOpaque(false);
	this.cards = new ArrayList<BgCardItem>();
	addAllCards(cards);
	if (layout == null) {
	    setAreaLayout(createDefaultAreaLayout());
	} else {
	    setAreaLayout(layout);
	}
	verifyCards();
	setPointedCardIndex(-1);
	setInAreaName(false);
	setDisplayAreaName(displayAreaName);
	setBorderColor(Color.BLUE);
	setSelectionCardBorderColor(Color.YELLOW);

	setAreaNamePosition(BgAreaNamePosition.LEFT_TOP);
	setAreaNameFractions(new float[] { 0.0f, 0.249f, 0.25f, 1.0f });
	setAreaNameGradientColors(new Color[] { new Color(0x63a5f7),
		new Color(0x3799f4), new Color(0x2d7eeb), new Color(0x30a5f9) });
	setAreaNameFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
	setAreaNameFontColor(Color.WHITE);
	setAreaNameSelectionFontColor(Color.ORANGE);
	addMouseMotionListener();

	installAreaActionMenuItem(BgDefaultAreaActionMenuMame.allMove
		.getActionName(), BgCreateAllMoveActionMenu.newInstance());
	installAreaActionMenuItem(BgDefaultAreaActionMenuMame.allDirection
		.getActionName(), BgCreateAllDirectionActionMenu.newInstance());
	installAreaActionMenuItem(BgDefaultAreaActionMenuMame.allSide
		.getActionName(), BgCreateAllSideActionMenu.newInstance());
	installAreaActionMenuItem(BgDefaultAreaActionMenuMame.shuffle
		.getActionName(), BgCreateShuffleActionMenu.newInstance());
	installAreaActionMenuItem(BgDefaultAreaActionMenuMame.detail
		.getActionName(), BgCreateAreaDetailActionMenu.newInstance());

	installSelectedCardActionMenuItem(BgDefaultCardActionMenuMame.move
		.getActionName(), BgCreateCardMoveActionMenu.newInstance());
	installSelectedCardActionMenuItem(BgDefaultCardActionMenuMame.direction
		.getActionName(), BgCreateCardDirectionActionMenu.newInstance());
	installSelectedCardActionMenuItem(BgDefaultCardActionMenuMame.side
		.getActionName(), BgCreateCardSideActionMenu.newInstance());
	installSelectedCardActionMenuItem(BgDefaultCardActionMenuMame.detail
		.getActionName(), BgCreateCardDetailActionMenu.newInstance());

	installPointedCardActionMenuItem(BgDefaultCardActionMenuMame.detail
		.getActionName(), BgCreateCardDetailActionMenu.newInstance());
    }

    public static BgAreaLabel newInstance(List<BgCardItem> cards,
	    BgAreaLayout layout) {
	return new BgAreaLabel(cards, layout, true);
    }

    public static BgAreaLabel newDefaultLayoutInstance(List<BgCardItem> cards) {
	return new BgAreaLabel(cards, null, true);
    }

    @Override
    public Component getComponent() {
	return this;
    }

    @Override
    public List<BgCardItem> getCards() {
	return cards;
	// return Collections.unmodifiableList(cards);
    }

    public void setAreaName(String name) {
	this.areaName = name;
    }

    @Override
    public String getAreaName() {
	return this.areaName;
    }

    // TODO 外部に公開する場合、Unmodifiableにするべき。どう実装するか考えること
    @Override
    public Set<Integer> getSelectedIndexes() {
	if (selectedIndexes == null) {
	    selectedIndexes = new TreeSet<Integer>();
	}
	return selectedIndexes;
    }

    private void setPointedCardIndex(int val) {
	this.pointedCardIndex = val;
    }

    public final int getPointedCardIndex() {
	return pointedCardIndex;
    }

    public void setDisplayAreaName(boolean val) {
	this.displayAreaName = val;
    }

    @Override
    public boolean isDisplayAreaName() {
	return this.displayAreaName;
    }

    public void setBorderColor(Color val) {
	this.borderColor = val;
    }

    public Color getBorderColor() {
	return this.borderColor;
    }

    public void setSelectionCardBorderColor(Color val) {
	this.selectionCardBorderColor = val;
    }

    @Override
    public Color getSelectionCardBorderColor() {
	return this.selectionCardBorderColor;
    }

    private boolean isInAreaName() {
	return this.inAreaName;
    }

    private void setInAreaName(boolean val) {
	if (val != this.inAreaName) {
	    this.inAreaName = val;
	    paintImmediately(getAreaNameRectangle());
	}
    }

    public void setAreaNamePosition(BgAreaNamePosition val) {
	this.areaNamePosition = val;
    }

    public BgAreaNamePosition getAreaNamePosition() {
	return this.areaNamePosition;
    }

    public void setAreaNameFractions(float[] fractions) {
	this.areaNameFractions = fractions;
    }

    private float[] getAreaNameFractions() {
	return this.areaNameFractions;
    }

    private Color[] getAreaNameGradientColors() {
	return this.areaNameGradientColors;
    }

    public void setAreaNameGradientColors(Color[] colors) {
	this.areaNameGradientColors = colors;
    }

    private Font getAreaNameFont() {
	return this.areaNameFont;
    }

    public void setAreaNameFont(Font f) {
	this.areaNameFont = f;
    }

    private Color getAreaNameFontColor() {
	return this.areaNameFontColor;
    }

    public void setAreaNameFontColor(Color c) {
	this.areaNameFontColor = c;
    }

    private Color getAreaNameSelectionFontColor() {
	return areaNameSelectionFontColor;
    }

    public void setAreaNameSelectionFontColor(Color c) {
	this.areaNameSelectionFontColor = c;
    }

    private JPopupMenu getAreaActionMenu() {
	if (this.areaActionMenu == null) {
	    this.areaActionMenu = new JPopupMenu();
	}
	return this.areaActionMenu;
    }

    public Map<String, BgCreateActionMenu> getAreaActionMenuMap() {
	if (this.areaActionMenuMap == null) {
	    this.areaActionMenuMap = new LinkedHashMap<String, BgCreateActionMenu>();
	}
	return this.areaActionMenuMap;
    }

    public void installAreaActionMenuItem(String actionName,
	    BgCreateActionMenu menuItem) {
	getAreaActionMenuMap().put(actionName, menuItem);
    }

    public BgCreateActionMenu uninstallAreaActionMenu(String actionName) {
	return getAreaActionMenuMap().remove(actionName);
    }

    public List<BgCreateActionMenu> uninstallAllAreaActionMenu() {
	List<BgCreateActionMenu> ret = new ArrayList<BgCreateActionMenu>();
	for (Iterator<Entry<String, BgCreateActionMenu>> it = getAreaActionMenuMap()
		.entrySet().iterator(); it.hasNext();) {
	    ret.add(it.next().getValue());
	    it.remove();
	}
	return ret;
    }

    private JPopupMenu getCardActionMenu() {
	if (this.cardActionMenu == null) {
	    this.cardActionMenu = new JPopupMenu();
	}
	return this.cardActionMenu;
    }

    public Map<String, BgCreateActionMenu> getSelectedCardActionMenuMap() {
	if (this.selectedCardActionMenuMap == null) {
	    this.selectedCardActionMenuMap = new LinkedHashMap<String, BgCreateActionMenu>();
	}
	return this.selectedCardActionMenuMap;
    }

    public void installSelectedCardActionMenuItem(String actionName,
	    BgCreateActionMenu menuItem) {
	getSelectedCardActionMenuMap().put(actionName, menuItem);
    }

    public BgCreateActionMenu uninstallSelectedCardActionMenu(String actionName) {
	return getSelectedCardActionMenuMap().remove(actionName);
    }

    public List<BgCreateActionMenu> uninstallAllSelectedCardActionMenu() {
	List<BgCreateActionMenu> ret = new ArrayList<BgCreateActionMenu>();
	for (Iterator<Entry<String, BgCreateActionMenu>> it = getSelectedCardActionMenuMap()
		.entrySet().iterator(); it.hasNext();) {
	    ret.add(it.next().getValue());
	    it.remove();
	}
	return ret;
    }

    public Map<String, BgCreateActionMenu> getPointedCardActionMenuMap() {
	if (this.pointedCardActionMenuMap == null) {
	    this.pointedCardActionMenuMap = new LinkedHashMap<String, BgCreateActionMenu>();
	}
	return this.pointedCardActionMenuMap;
    }

    public void installPointedCardActionMenuItem(String actionName,
	    BgCreateActionMenu menuItem) {
	getPointedCardActionMenuMap().put(actionName, menuItem);
    }

    public BgCreateActionMenu uninstallPointedCardActionMenu(String actionName) {
	return getPointedCardActionMenuMap().remove(actionName);
    }

    public List<BgCreateActionMenu> uninstallAllPointedCardActionMenu() {
	List<BgCreateActionMenu> ret = new ArrayList<BgCreateActionMenu>();
	for (Iterator<Entry<String, BgCreateActionMenu>> it = getPointedCardActionMenuMap()
		.entrySet().iterator(); it.hasNext();) {
	    ret.add(it.next().getValue());
	    it.remove();
	}
	return ret;
    }

    public void setPlayBoard(BgAreaLabelPane val) {
	this.playBoard = val;
    }

    public BgAreaLabelPane getPlayBoard() {
	return this.playBoard;
    }

    public void setPlayFrame(BgTempAreaInternalFrame val) {
	this.playFrame = val;
    }

    public BgTempAreaInternalFrame getPlayFrame() {
	return this.playFrame;
    }

    public void addCard(BgCardItem card) {
	clearCardsSelection();
	if (getAreaLayout() != null && card != null) {
	    card.setDirection(getAreaLayout().getInitialDirection());
	    card.setSide(getAreaLayout().getInitialSide());
	}

	this.cards.add(card);
	verifyCards();
    }

    public void addCard(int idx, BgCardItem card) {
	clearCardsSelection();
	if (getAreaLayout() != null && card != null) {
	    card.setDirection(getAreaLayout().getInitialDirection());
	    card.setSide(getAreaLayout().getInitialSide());
	}

	this.cards.add(idx, card);
	verifyCards();
    }

    public void addAllCards(List<BgCardItem> cards) {
	clearCardsSelection();
	if (getAreaLayout() != null && cards != null) {

	    for (BgCardItem card : cards) {
		card.setDirection(getAreaLayout().getInitialDirection());
		card.setSide(getAreaLayout().getInitialSide());
	    }
	}

	this.cards.addAll(cards);
	verifyCards();
    }

    public void addAllCards(int idx, List<BgCardItem> cards) {
	clearCardsSelection();
	if (getAreaLayout() != null && cards != null) {

	    for (BgCardItem card : cards) {
		card.setDirection(getAreaLayout().getInitialDirection());
		card.setSide(getAreaLayout().getInitialSide());
	    }
	}

	this.cards.addAll(idx, cards);
	verifyCards();
    }

    public List<BgCardItem> removeAllCards() {
	clearCardsSelection();
	List<BgCardItem> ret = new ArrayList<BgCardItem>();
	ret.addAll(cards);
	this.cards.clear();
	verifyCards();
	return ret;
    }

    public BgCardItem removeCardFromTop() {
	clearCardsSelection();
	if (this.cards.size() == 0) {
	    throw new IllegalStateException(
		    "the cards no is zero, so colud not remove top site.com.google.anywaywrite.card.");
	}
	BgCardItem remove = this.cards.remove(this.cards.size() - 1);
	verifyCards();
	return remove;
    }

    public List<BgCardItem> removeCardsFromTop(int no) {
	clearCardsSelection();
	if (this.cards.size() < no) {
	    throw new IllegalStateException("the rest cards no is "
		    + this.cards.size() + ", so colud not remove top " + no
		    + " site.com.google.anywaywrite.card.");
	}

	List<BgCardItem> ret = new ArrayList<BgCardItem>();
	for (int idx = 0; idx < no; idx++) {
	    ret.add(this.cards.remove(this.cards.size() - 1));
	}
	verifyCards();
	return ret;
    }

    /**
     * 下から指定された枚数目のカードを取り除きます。<br />
     * 一番下を取り除く時は、0を指定します。
     * 
     * @param no
     * @return
     */
    public BgCardItem removeCardFromBottom(int no) {
	if (no < 0) {
	    throw new IllegalArgumentException(
		    "the no should be more than or equal ZERO, but was " + no);
	}
	if (this.cards.size() <= no) {
	    throw new IllegalStateException("the rest cards no is "
		    + this.cards.size() + ", so colud not remove top " + no
		    + " site.com.google.anywaywrite.card.");
	}
	clearCardsSelection();
	BgCardItem remove = this.cards.remove(no);
	verifyCards();
	return remove;
    }

    /**
     * 一番上から数えて指定枚数目のカードを取り除きます。<br />
     * 一番上を取り除くときは、1を指定します。(0ではありません)
     */
    public BgCardItem removeCardFromTop(int no) {
	if (no <= 0) {
	    throw new IllegalArgumentException(
		    "the no should be more than ZERO, but was " + no);
	}
	if (this.cards.size() < no) {
	    throw new IllegalStateException("the rest cards no is "
		    + this.cards.size() + ", so colud not remove top " + no
		    + " site.com.google.anywaywrite.card.");
	}
	clearCardsSelection();
	BgCardItem remove = this.cards.remove(this.cards.size() - no);
	verifyCards();
	return remove;
    }

    public void clearCardsSelection() {
	Set<Integer> selIdxes = getSelectedIndexes();
	getSelectedIndexes().clear();
	for (int selIdx : selIdxes) {
	    paintRectangleImmediately(selIdx);
	    if (getSelectionChangedHook() != null) {
		getSelectionChangedHook().selectionChanged(BgAreaLabel.this,
			selIdx);
	    }
	}
    }

    private void verifyCards() {
	if (getAreaLayout() != null) {
	    String errMsg = getAreaLayout().verify(this.cards);
	    if (errMsg != null) {
		throw new RuntimeException(errMsg);
	    }
	}
    }

    public void setAreaLayout(BgAreaLayout layout) {
	this.areaLayout = layout;
	this.areaLayout.setArea(this);
    }

    public BgAreaLayout getAreaLayout() {
	return this.areaLayout;
    }

    protected BgAreaLayout createDefaultAreaLayout() {
	return BgSingleSimpleLayout.newDefaultInstance();
    }

    private void addMouseMotionListener() {
	this.addMouseMotionListener(new MouseMotionAdapter() {

	    @Override
	    public void mouseMoved(MouseEvent e) {
		setInAreaName(e);

		List<Rectangle> recs = BgAreaLabel.this.getAreaLayout()
			.getCardRectangles();
		int index = -1;
		for (int idx = 0, size = recs.size(); idx < size; idx++) {
		    if (recs.get(idx).contains(e.getPoint())) {
			index = idx;
		    }
		}
		if (index == getPointedCardIndex()) {
		    return;
		}
		setPointedCardIndex(index);
		if (getPointChangedHook() != null) {
		    getPointChangedHook().pointChanged(BgAreaLabel.this, index);
		}

	    }

	    private void setInAreaName(MouseEvent e) {
		if (isDisplayAreaName()) {
		    if (getAreaNameRectangle().contains(e.getPoint())) {
			BgAreaLabel.this.setInAreaName(true);
		    } else {
			BgAreaLabel.this.setInAreaName(false);
		    }
		} else {
		    BgAreaLabel.this.setInAreaName(false);
		}
	    }
	});

	this.addMouseListener(new MouseAdapter() {

	    // JPopupMenu menu = new JPopupMenu();

	    @Override
	    public void mouseEntered(MouseEvent mouseevent) {
		setBorder(new MatteBorder(2, 2, 2, 2, getBorderColor()));
	    }

	    @Override
	    public void mouseExited(MouseEvent mouseevent) {
		setBorder(new EmptyBorder(2, 2, 2, 2));
		setInAreaName(false);
		setPointedCardIndex(-1);
	    }

	    @Override
	    public void mouseClicked(MouseEvent me) {
		if (SwingUtilities.isLeftMouseButton(me)) {
		    if (BgAreaLabel.this.getCards() == null
			    || BgAreaLabel.this.getCards().size() == 0) {
			return;
		    }

		    if (isDisplayAreaName()
			    && getAreaNameRectangle().contains(me.getPoint())) {
			getAreaActionMenu().removeAll();
			for (Iterator<Entry<String, BgCreateActionMenu>> it = getAreaActionMenuMap()
				.entrySet().iterator(); it.hasNext();) {
			    BgCreateActionMenu createMenu = it.next()
				    .getValue();
			    BgMenuItem item = createMenu
				    .createActionMenu(BgAreaLabel.this);
			    if (item.isHasSeparator()
				    && getAreaActionMenu().getComponentCount() > 0) {
				getAreaActionMenu().addSeparator();
			    }
			    getAreaActionMenu().add(item.getMenuItem());
			}

			getAreaActionMenu().show(BgAreaLabel.this,
				(int) getAreaNameRectangle().getX(),
				(int) getAreaNameRectangle().getHeight());
			return;
		    }

		    if (getPointedCardIndex() == -1) {
			return;
		    }

		    if (getSelectedIndexes().contains(getPointedCardIndex())) {
			getSelectedIndexes().remove(getPointedCardIndex());
		    } else {
			getSelectedIndexes().add(getPointedCardIndex());
		    }
		    // Rectangle rec = getAreaLayout().getCardRectangles().get(
		    // getPointedCardIndex());
		    // BgAreaLabel.this.paintImmediately(rec.x - 3, rec.y - 3,
		    // rec.width + 6, rec.height + 6);
		    paintRectangleImmediately(getPointedCardIndex());

		    if (isDisplayAreaName()) {
			BgAreaLabel.this
				.paintImmediately(getAreaNameRectangle());
		    }
		    if (getSelectionChangedHook() != null) {
			getSelectionChangedHook().selectionChanged(
				BgAreaLabel.this, getPointedCardIndex());
		    }
		} else if (SwingUtilities.isRightMouseButton(me)) {
		    if (getCards() == null || getCards().size() == 0) {
			return;
		    }

		    getCardActionMenu().removeAll();
		    if (isInSelectedCards()) {

			for (Iterator<Entry<String, BgCreateActionMenu>> it = getSelectedCardActionMenuMap()
				.entrySet().iterator(); it.hasNext();) {
			    BgCreateActionMenu createMenu = it.next()
				    .getValue();
			    BgMenuItem item = createMenu
				    .createActionMenu(BgAreaLabel.this);
			    if (item.isHasSeparator()
				    && getCardActionMenu().getComponentCount() > 0) {
				getCardActionMenu().addSeparator();
			    }
			    getCardActionMenu().add(item.getMenuItem());
			}

			// menu.add(createMoveMenu(area, me));
			// // menu.add(createAllMoveMenu(area));// →BgAreaLabel
			// // menu.addSeparator();
			//
			// menu.add(createCardsDirectionMenu(area, me));
			// // menu.add(createAreaDirectionMenu(area));//
			// // →BgAreaLabel
			// menu.add(createCardsSideMenu(area, me));
			// // menu.add(createAreaSideMenu(area));// →BgAreaLabel
			//
			// // menu.addSeparator();
			// // menu.add(new JMenuItem(new
			// // BgAreaShuffleAction(area)));//
			// // →BgAreaLabel
			// menu.addSeparator();
		    } else {
			if (getPointedCardIndex() != -1) {
			    for (Iterator<Entry<String, BgCreateActionMenu>> it = getPointedCardActionMenuMap()
				    .entrySet().iterator(); it.hasNext();) {
				BgCreateActionMenu createMenu = it.next()
					.getValue();
				BgMenuItem item = createMenu
					.createActionMenu(BgAreaLabel.this);
				if (item.isHasSeparator()
					&& getCardActionMenu()
						.getComponentCount() > 0) {
				    getCardActionMenu().addSeparator();
				}
				getCardActionMenu().add(item.getMenuItem());
			    }
			}
		    }

		    // menu.add(createAreaDetailMenu(area));// →BgAreaLabel
		    // menu.add(createCardDetailMenu(BgAreaLabel.this, me));

		    getCardActionMenu().show(BgAreaLabel.this, me.getX(),
			    me.getY());
		}
	    }

	    private boolean isInSelectedCards() {
		return getSelectedIndexes().contains(getPointedCardIndex());
	    }
	});
    }

    private BgCardPointChangedHook pointChangedHook;

    /** カードの指し示す場所が変更された直後に行いたい処理を記述します */
    public final void setPointChangedHook(BgCardPointChangedHook hook) {
	pointChangedHook = hook;
    }

    public final BgCardPointChangedHook getPointChangedHook() {
	return this.pointChangedHook;
    }

    public BgCardSelectionChangedHook selectionChangedHook;

    /** カードの選択状態が変更された直後に行いたい処理を記述します */
    public final void setSelectionChangedHook(BgCardSelectionChangedHook hook) {
	selectionChangedHook = hook;
    }

    public final BgCardSelectionChangedHook getSelectionChangedHook() {
	return this.selectionChangedHook;
    }

    @Override
    public void paintComponent(Graphics g) {
	// System.out.println("BgAreaLabel#getSelectedIndexes()#size() == "
	// + getSelectedIndexes().size());
	if (isInAreaName() || getAreaActionMenu().isVisible()) {
	    getAreaLayout().paintComponent(g);
	    if (isDisplayAreaName()) {
		paintAreaName(g);
	    }

	    // RadialGradientPaint rgp = new RadialGradientPaint(
	    // new Point2D.Double(getAreaNameRectangle().width / 2.0,
	    // // getAreaNameRectangle().height * 1.5),
	    // getAreaNameRectangle().height * 1.0),
	    // getAreaNameRectangle().width / 2.3f, new Point2D.Double(
	    // getAreaNameRectangle().width / 2.0,
	    // // getAreaNameRectangle().height * 1.75 + 6),
	    // getAreaNameRectangle().height * 1.25), new float[] {
	    // 0.0f, 0.8f }, new Color[] {
	    // new Color(64, 142, 203, 255),
	    // new Color(64, 142, 203, 0) },
	    // RadialGradientPaint.CycleMethod.NO_CYCLE,
	    // RadialGradientPaint.ColorSpaceType.SRGB, AffineTransform
	    // .getScaleInstance(1.0, 0.5));
	    // Graphics2D g2d = (Graphics2D) g.create();
	    // g2d.setPaint(rgp);
	    // g2d.fillOval(0, 0, getAreaNameRectangle().width,
	    // getAreaNameRectangle().height);
	    // g2d.dispose();
	} else {
	    if (getSelectedIndexes().size() > 0) {
		if (isDisplayAreaName()) {
		    paintAreaName(g);
		}
		getAreaLayout().paintComponent(g);
	    } else {
		getAreaLayout().paintComponent(g);
		if (isDisplayAreaName()) {
		    paintAreaName(g);
		}
	    }
	}
    }

    private void paintRectangleImmediately(int idx) {
	if (idx < 0) {
	    throw new IllegalArgumentException(
		    "idx should not be less than ZERO, but was.");
	}
	Rectangle rec = getAreaLayout().getCardRectangles().get(idx);
	BgAreaLabel.this.paintImmediately(rec.x - 3, rec.y - 3, rec.width + 6,
		rec.height + 6);
    }

    public void addSelection(int idx) {
	if (idx < 0) {
	    throw new IllegalArgumentException(
		    "idx should not be less than ZERO, but was.");
	}
	if (getSelectedIndexes().contains(idx)) {
	    return;
	}
	getSelectedIndexes().add(idx);
	paintRectangleImmediately(idx);
	if (getSelectionChangedHook() != null) {
	    getSelectionChangedHook().selectionChanged(BgAreaLabel.this, idx);
	}
    }

    public void removeSelection(int idx) {
	if (idx < 0) {
	    throw new IllegalArgumentException(
		    "idx should not be less than ZERO, but was.");
	}
	if (!getSelectedIndexes().contains(idx)) {
	    return;
	}
	getSelectedIndexes().remove(idx);
	paintRectangleImmediately(idx);
	if (getSelectionChangedHook() != null) {
	    getSelectionChangedHook().selectionChanged(BgAreaLabel.this, idx);
	}
    }

    @Override
    public void paintAreaName(Graphics g) {

	drawGradient(g);

	drawAreaName(g);
    }

    public String getAreaDisplayName() {
	return getAreaName() + AREA_NAME_SUFFIX;
    }

    private void drawAreaName(Graphics g) {
	Graphics2D g2d = (Graphics2D) g.create();
	g2d.setFont(getAreaNameFont());
	if (isInAreaName() || getAreaActionMenu().isVisible()) {
	    g2d.setColor(getAreaNameSelectionFontColor());
	} else {
	    g2d.setColor(getAreaNameFontColor());
	}
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.drawString(getAreaDisplayName(), getAreaNameRectangle().x + 5,
		getAreaNameRectangle().y + 12);
	g2d.dispose();
    }

    private void drawGradient(Graphics g) {
	Graphics2D g2d = (Graphics2D) g.create();
	g2d.setFont(getAreaNameFont());
	FontMetrics fm = g2d.getFontMetrics(getAreaNameFont());
	LinearGradientPaint lgp = new LinearGradientPaint(0.0f, 0.0f, 0.0f, 16,
		getAreaNameFractions(), getAreaNameGradientColors());
	g2d.setPaint(lgp);
	Rectangle rec = getDisplayRectangle(fm, getAreaDisplayName());
	g2d.fillRect(rec.x, rec.y, rec.width, rec.height);
	g2d.dispose();
    }

    private Rectangle getDisplayRectangle(FontMetrics fm, String dispAreaName) {
	Rectangle ret = new Rectangle();
	ret.width = fm.stringWidth(dispAreaName) + 20;
	ret.height = 16;
	switch (getAreaNamePosition()) {
	case LEFT_TOP:
	    ret.x = 0;
	    ret.y = 0;
	    break;
	case LEFT_BOTTOM:
	    ret.x = 0;
	    ret.y = getHeight() - ret.height;
	    break;
	case RIGHT_BOTTOM:
	    ret.x = getWidth() - ret.width;
	    ret.y = getHeight() - ret.height;
	    break;
	case RIGHT_TOP:
	    ret.x = getWidth() - ret.width;
	    ret.y = 0;
	    break;
	default:
	    throw new RuntimeException("Program error.");
	}
	return ret;
    }

    public Rectangle getAreaNameRectangle() {
	Graphics g = this.getGraphics();
	Graphics2D g2d = (Graphics2D) g.create();
	Rectangle ret = new Rectangle();
	ret.width = g2d.getFontMetrics(getAreaNameFont()).stringWidth(
		getAreaDisplayName()) + 20;
	ret.height = 16;
	switch (getAreaNamePosition()) {
	case LEFT_TOP:
	    ret.x = 0;
	    ret.y = 0;
	    break;
	case LEFT_BOTTOM:
	    ret.x = 0;
	    ret.y = getHeight() - ret.height;
	    break;
	case RIGHT_BOTTOM:
	    ret.x = getWidth() - ret.width;
	    ret.y = getHeight() - ret.height;
	    break;
	case RIGHT_TOP:
	    ret.x = getWidth() - ret.width;
	    ret.y = 0;
	    break;
	default:
	    throw new RuntimeException("Program error.");
	}
	g2d.dispose();
	return ret;
    }
}
