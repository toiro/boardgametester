/**
 *
 */
package site.com.google.anywaywrite.component.gui;


import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import site.com.google.anywaywrite.action.BgAreaDetailAction;
import site.com.google.anywaywrite.component.layout.BgLayoutBase;
import site.com.google.anywaywrite.component.layout.BgMultiPileLayout;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;
import site.com.google.anywaywrite.util.control.BgAreaControlUtil;
import site.com.google.anywaywrite.util.swing.BgGuiUtil;

/**
 * @author y-kitajima
 * 
 */
public class BgAreaBoxInternalFrame extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    // private List<BgAreaLabel> areaSet;
    // private List<List<BgCardItem>> areaCards;
    private List<AreaInfo> areaInfos;

    private List<AreaInfo> getAreaInfos() {
	if (areaInfos == null) {
	    areaInfos = new ArrayList<AreaInfo>();
	}
	return areaInfos;
    }

    public List<BgAreaLabel> getAreaSet() {
	List<BgAreaLabel> ret = new ArrayList<BgAreaLabel>();
	for (AreaInfo info : getAreaInfos()) {
	    ret.add(info.getAreaLabel());
	}
	return ret;
    }

    public List<List<BgCardItem>> getAreaCards() {
	List<List<BgCardItem>> ret = new ArrayList<List<BgCardItem>>();
	for (AreaInfo info : getAreaInfos()) {
	    ret.add(info.getCards());
	}
	return ret;
    }

    private BgAreaLabelPane playArea;

    private void setPlayArea(BgAreaLabelPane val) {
	this.playArea = val;
    }

    public BgAreaLabelPane getPlayArea() {
	return this.playArea;
    }

    private BgAreaBoxInternalFrame(BgAreaLabelPane playArea, AreaInfo... areaInfos) {
	super();
	setPlayArea(playArea);
	registerInitialArea(areaInfos);
    }

    public static BgAreaBoxInternalFrame newInstance(BgAreaLabelPane playArea,
	    AreaInfo... areaInfos) {
	return new BgAreaBoxInternalFrame(playArea, areaInfos);
    }

    /** ゲームを開けた状態、すなわちゲームボードが広がっていて、ゲームボックスにコンポーネントが表示されている、という状態にする */
    public final void initialize() {
	List<BgAreaLabel> playAreas = getPlayArea().getAreaSet();
	for (BgAreaLabel playArea : playAreas) {
	    BgAreaControlUtil.removeAll(playArea);
	}
	for (BgAreaLabel area : getAreaSet()) {
	    BgAreaControlUtil.removeAll(area);
	}
	registerInitialArea(getAreaInfos().toArray(
		new AreaInfo[getAreaInfos().size()]));
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		setVisible(true);
	    }
	});
    }

    public final void startGame() {
	BgAreaControlUtil.shuffle(getAreaInfos().get(0).getAreaLabel());

	for (int idx = 0; idx < 50; idx++) {
	    BgAreaControlUtil.move(getAreaInfos().get(0).getAreaLabel(),
		    getPlayArea().getAreaSet().get(13));
	}
	for (int idx = 0; idx < 5; idx++) {
	    BgAreaControlUtil.move(getPlayArea().getAreaSet().get(13),
		    getPlayArea().getAreaSet().get(1));
	}
    }

    public static class AreaInfo {
	private String areaName;
	private List<BgCardItem> cards;
	private BgAreaLabel areaLabel;

	public AreaInfo(String areaName, List<BgCardItem> cards) {
	    this.areaName = areaName;
	    this.cards = createCopyInfoCardList(cards);
	}

	private String getAreaName() {
	    return this.areaName;
	}

	private List<BgCardItem> getCards() {
	    return this.cards;
	}

	private void setAreaLabel(BgAreaLabel val) {
	    this.areaLabel = val;
	}

	private BgAreaLabel getAreaLabel() {
	    return this.areaLabel;
	}
    }

    /** ゲームボックスの中身を作成します。 */
    private void registerInitialArea(AreaInfo... areaInfos) {
	if (areaInfos == null) {
	    throw new IllegalArgumentException(
		    "areaInfos should not be null, but was.");
	}

	getContentPane().removeAll();
	getAreaInfos().clear();

	for (AreaInfo areaInfo : areaInfos) {
	    if (areaInfo.getAreaName() == null
		    || areaInfo.getAreaName().length() == 0) {
		throw new IllegalArgumentException(
			"areaName should not be null or zero length, but was.");
	    }

	    if (isContainsAreaName(areaInfo.getAreaName())) {
		throw new IllegalArgumentException(
			"[AreaName Duplicate Exception] The areaName already setted, the areaName="
				+ areaInfo.getAreaName());
	    }
	    if (areaInfo.getCards() == null || areaInfo.getCards().size() == 0) {
		throw new IllegalArgumentException(
			"cards should not be null and should be more than zero size list, but was not.");
	    }
	    BgAreaLabel areaLabel = createInitialAreaLabel(
		    areaInfo.getAreaName(), areaInfo.getCards());
	    areaInfo.setAreaLabel(areaLabel);
	    getContentPane().add(areaLabel);
	    getAreaInfos().add(areaInfo);
	}
    }

    private BgAreaLabel createInitialAreaLabel(String areaName,
	    List<BgCardItem> cards) {
	BgMultiPileLayout layout = BgMultiPileLayout.newInstance(BgLayoutBase.BOTH,
		cards.size(), cards.size() / 5, cards.size() / 5);
	layout.setMargin(new Insets(10, 10, 10, 10));
	layout.setInitialDirection(Direction.UP);
	layout.setInitialSide(Side.DOWN);
	BgAreaLabel area = BgAreaLabel.newInstance(cards, layout);
	area.setBounds(createAreaRectangle());

	area.setAreaName(areaName);
	getAreaSet().add(area);
	getAreaCards().add(createCopyInfoCardList(cards));
	addMouseListener(area);
	return area;
    }

    private static List<BgCardItem> createCopyInfoCardList(List<BgCardItem> cards) {
	if (cards == null) {
	    return null;
	}
	List<BgCardItem> ret = new ArrayList<BgCardItem>();
	for (BgCardItem card : cards) {
	    ret.add(BgCardItem.newInstance(card.getInfo()));
	}
	return ret;
    }

    private Rectangle createAreaRectangle() {
	int areaNo = getAreaSet().size();
	int hNo = areaNo % 3;
	int vNo = areaNo / 3;
	return new Rectangle(hNo * 200, vNo * 200, 200, 200);
    }

    private boolean isContainsAreaName(String name) {
	Iterator<BgAreaLabel> it = getAreaSet().iterator();
	while (it.hasNext()) {
	    BgAreaLabel area = it.next();
	    if (area.getAreaName().equals(name)) {
		return true;
	    }
	}
	return false;
    }

    private void addMouseListener(final BgAreaLabel area) {
	area.addMouseListener(new MouseAdapter() {

	    @Override
	    public void mouseClicked(final MouseEvent me) {
		if (!SwingUtilities.isRightMouseButton(me)) {
		    return;
		}

		JPopupMenu menu = new JPopupMenu();
		// menu.add(createMoveMenu(area, me));
		// menu.add(createAllMoveMenu(area));

		// menu.add(createDirectionMenu(area));
		// menu.add(createSideMenu(area));

		// menu.addSeparator();
		// menu.add(new JMenuItem(new BgAreaShuffleAction(area)));

		// menu.addSeparator();
		menu.add(new JMenuItem(BgAreaDetailAction.displayInstance(area,
			getPlayArea(), BgGuiUtil.getOwnerWindow(area))));

		menu.show(area, me.getX(), me.getY());
	    }

	    // private JMenu createMoveMenu(final BgAreaLabel area,
	    // final MouseEvent me) {
	    // JMenu move = new JMenu("move");
	    // Iterator<BgAreaLabel> it = getPlayArea().getAreaSet().iterator();
	    // while (it.hasNext()) {
	    // final BgAreaLabel al = it.next();
	    // if (BgAreaLabelPane.TEMP_AREA_NAME.equals(al.getAreaName())) {
	    // JMenuItem site.com.google.anywaywrite.item = new JMenuItem(new BgAreaMoveAction(area,
	    // al, me.getPoint()));
	    // move.add(site.com.google.anywaywrite.item);
	    // break;
	    // }
	    // }
	    // return move;
	    // }
	    //
	    // private JMenu createAllMoveMenu(final BgAreaLabel area) {
	    // JMenu allMove = new JMenu("allMove");
	    // Iterator<BgAreaLabel> it = getPlayArea().getAreaSet().iterator();
	    // while (it.hasNext()) {
	    // final BgAreaLabel al = it.next();
	    // if (BgAreaLabelPane.TEMP_AREA_NAME.equals(al.getAreaName())) {
	    // JMenuItem site.com.google.anywaywrite.item = new JMenuItem(new BgAreaMoveAction(
	    // area, al));
	    // allMove.add(site.com.google.anywaywrite.item);
	    // break;
	    // }
	    // }
	    // return allMove;
	    // }
	    //
	    // private JMenu createDirectionMenu(final BgAreaLabel area) {
	    // JMenu direction = new JMenu("direction");
	    // Direction[] dirs = BgCardState.Direction.values();
	    // for (Direction dir : dirs) {
	    // JMenuItem site.com.google.anywaywrite.item = new JMenuItem(new BgAreaDirectionAction(
	    // area, dir));
	    // direction.add(site.com.google.anywaywrite.item);
	    // }
	    // return direction;
	    // }
	    //
	    // private JMenu createSideMenu(final BgAreaLabel area) {
	    // JMenu menu = new JMenu("side");
	    // Side[] sides = BgCardState.Side.values();
	    // for (Side side : sides) {
	    // JMenuItem site.com.google.anywaywrite.item = new JMenuItem(new BgAreaSideAction(area,
	    // side));
	    // menu.add(site.com.google.anywaywrite.item);
	    // }
	    // return menu;
	    // }

	});
    }

}
