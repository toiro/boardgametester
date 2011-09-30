/**
 *
 */
package site.com.google.anywaywrite.component.gui;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import site.com.google.anywaywrite.action.BgCardDetailAction;
import site.com.google.anywaywrite.action.BgCardsDirectionAction;
import site.com.google.anywaywrite.action.BgCardsMoveAction;
import site.com.google.anywaywrite.action.BgCardsSideAction;
import site.com.google.anywaywrite.component.layout.BgLayoutPosition;
import site.com.google.anywaywrite.item.card.BgCardState;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;
import site.com.google.anywaywrite.util.swing.BgGuiUtil;

/**
 * ボードゲーム管理用のボード基底クラスです。 コンポーネントを置くエリアの管理、エリアに表示するメニューの管理を行います。
 * 
 * @author kitajima
 * 
 */
public class BgAreaLabelPane extends BgImageDesktopPane {

    private static final long serialVersionUID = 1L;

    private BgAreaLabelPane(String imagePath, BgLayoutPosition position,
	    double rate) {
	super(imagePath, position, rate);
    }

    public static BgAreaLabelPane newInstance(String imagePath,
	    BgLayoutPosition position, double rate) {
	return new BgAreaLabelPane(imagePath, position, rate);
    }

    private List<BgAreaLabel> areaSet;

    public List<BgAreaLabel> getAreaSet() {
	if (areaSet == null) {
	    areaSet = new ArrayList<BgAreaLabel>();
	}
	return areaSet;
    }

    private List<BgTempAreaInternalFrame> tempFrameSet;

    public List<BgTempAreaInternalFrame> getTempFrameSet() {
	if (tempFrameSet == null) {
	    tempFrameSet = new ArrayList<BgTempAreaInternalFrame>();
	}
	return tempFrameSet;
    }

    public void registerArea(String areaName, BgAreaLabel area) {
	if (areaName == null || areaName.length() == 0) {
	    throw new IllegalArgumentException(
		    "areaName should not be null or zero length, but was.");
	}
	if (area == null) {
	    throw new IllegalArgumentException(
		    "area should not be null, but was.");
	}

	if (isContainsAreaName(areaName)) {
	    throw new IllegalArgumentException(
		    "[AreaName Duplicate Exception] The areaName already setted, the areaName="
			    + areaName);
	}

	area.setAreaName(areaName);
	// super.add(area);
	getAreaSet().add(area);
	addMouseListener(area);
	area.setPlayBoard(this);
    }

    public void registerTempArea(String areaName, BgAreaLabel area,
	    Rectangle position) {
	if (areaName == null || areaName.length() == 0) {
	    throw new IllegalArgumentException(
		    "areaName should not be null or zero length, but was.");
	}
	if (area == null) {
	    throw new IllegalArgumentException(
		    "area should not be null, but was.");
	}

	if (isContainsTempAreaName(areaName)) {
	    throw new IllegalArgumentException(
		    "[AreaName Duplicate Exception] The areaName already setted, the areaName="
			    + areaName);
	}

	area.setAreaName(areaName);
	BgTempAreaInternalFrame inframe = BgTempAreaInternalFrame.newInstance(
		this, areaName, area, position);
	add(inframe);
	getTempFrameSet().add(inframe);
	addMouseListener(area, inframe);

	area.setPlayBoard(this);
	area.setPlayFrame(inframe);
    }

    // TODO これは実装が不完全であるため、消した方が良いか検討する。(完全な実装にするのもあり)
    public void unRegisterArea(String areaName) {
	if (areaName == null || areaName.length() == 0) {
	    throw new IllegalArgumentException(
		    "areaName should not be null or zero length, but was.");
	}

	int targetIdx = -1;
	for (int idx = 0, size = getAreaSet().size(); idx < size; idx++) {
	    if (getAreaSet().get(idx).getAreaName().equals(areaName)) {
		targetIdx = idx;
		break;
	    }
	}

	if (targetIdx == -1) {
	    throw new IllegalArgumentException(
		    "areaName should be already registered, but was not. areaName="
			    + areaName);
	}

	BgAreaLabel area = getAreaSet().get(targetIdx);
	MouseListener[] mouseListeners = area.getMouseListeners();
	for (MouseListener listener : mouseListeners) {
	    if (listener instanceof AreaMouseListener) {
		area.removeMouseListener(listener);
		break;
	    }
	}
	getAreaSet().remove(targetIdx);
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

    private boolean isContainsTempAreaName(String name) {
	Iterator<BgTempAreaInternalFrame> it = getTempFrameSet().iterator();
	while (it.hasNext()) {
	    BgAreaLabel area = it.next().getAreaLabel();
	    if (area.getAreaName().equals(name)) {
		return true;
	    }
	}
	return false;
    }

    // private boolean isContainsTempAreaName(String name) {
    // Iterator<BgAreaLabel> it = getTempAreaSet().iterator();
    // while (it.hasNext()) {
    // BgAreaLabel area = it.next();
    // if (area.getAreaName().equals(name)) {
    // return true;
    // }
    // }
    // return false;
    // }

    private class AreaMouseListener extends MouseAdapter {
	private BgAreaLabel area;
	private BgTempAreaInternalFrame frame;

	private AreaMouseListener(BgAreaLabel area) {
	    this.area = area;
	}

	private AreaMouseListener(BgAreaLabel area,
		BgTempAreaInternalFrame frame) {
	    this.area = area;
	    this.frame = frame;
	}

	@Override
	public void mouseClicked(final MouseEvent me) {
	    if (!SwingUtilities.isRightMouseButton(me)) {
		return;
	    }

	    if (area.getCards() == null || area.getCards().size() == 0) {
		return;
	    }

	    if (true) {// TODO debug用
		return;
	    }

	    JPopupMenu menu = new JPopupMenu();
	    if (isInSelectedCards()) {
		menu.add(createMoveMenu(area, me));
		// menu.add(createAllMoveMenu(area));// →BgAreaLabel
		// menu.addSeparator();

		menu.add(createCardsDirectionMenu(area, me));
		// menu.add(createAreaDirectionMenu(area));// →BgAreaLabel
		menu.add(createCardsSideMenu(area, me));
		// menu.add(createAreaSideMenu(area));// →BgAreaLabel

		// menu.addSeparator();
		// menu.add(new JMenuItem(new BgAreaShuffleAction(area)));//
		// →BgAreaLabel
		menu.addSeparator();
	    }

	    // menu.add(createAreaDetailMenu(area));// →BgAreaLabel
	    menu.add(createCardDetailMenu(area, me));

	    menu.show(area, me.getX(), me.getY());
	}

	private boolean isInSelectedCards() {
	    return area.getSelectedIndexes().contains(
		    area.getPointedCardIndex());
	}

	private JMenuItem createCardDetailMenu(final BgAreaLabel area,
		final MouseEvent e) {
	    JMenuItem item;
	    if (frame != null) {
		// item = new JMenuItem(BgCardDetailAction.pointInstance(frame,
		// BgAreaLabelPane.this, BgGuiUtil.getOwnerWindow(area), e
		// .getPoint()));
	    } else {
		item = new JMenuItem(BgCardDetailAction.pointInstance(area,
			BgAreaLabelPane.this, BgGuiUtil.getOwnerWindow(area), e
				.getPoint(), 130));
	    }

	    item = new JMenuItem(BgCardDetailAction.pointInstance(area,
		    BgAreaLabelPane.this, BgGuiUtil.getOwnerWindow(area), e
			    .getPoint(), 130));
	    return item;
	}

	// private JMenuItem createAreaDetailMenu(final BgAreaLabel area) {
	// JMenuItem item;
	// if (frame != null) {
	// item = new JMenuItem(BgAreaDetailAction.tempInstance(frame,
	// BgAreaLabelPane.this, BgGuiUtil.getOwnerWindow(area)));
	// } else {
	// item = new JMenuItem(BgAreaDetailAction.newInstance(area,
	// BgAreaLabelPane.this, BgGuiUtil.getOwnerWindow(area)));
	// }
	// return item;
	// }

	private List<Integer> convertToList(Set<Integer> set) {
	    List<Integer> ret = new ArrayList<Integer>();
	    for (Iterator<Integer> it = area.getSelectedIndexes().iterator(); it
		    .hasNext();) {
		ret.add(it.next());
	    }
	    return ret;
	}

	private JMenu createMoveMenu(final BgAreaLabel area, final MouseEvent me) {
	    JMenu move = new JMenu("move");

	    Iterator<BgTempAreaInternalFrame> tempIt = getTempFrameSet()
		    .iterator();
	    // Iterator<BgAreaLabel> tempIt = getTempAreaSet().iterator();
	    while (tempIt.hasNext()) {
		BgTempAreaInternalFrame tempFrame = tempIt.next();
		final BgAreaLabel tempAl = tempFrame.getAreaLabel();
		JMenuItem item;
		if (frame != null) {
		    if (area.getAreaName().equals(tempAl.getAreaName())) {
			continue;
		    }
		    // item = new JMenuItem(BgCardsMoveAction
		    // .pointBothTempToLastInstance(frame, tempFrame, me
		    // .getPoint()));
		    item = new JMenuItem(BgCardsMoveAction
			    .bothTempToLastInstance(frame, tempFrame,
				    convertToList(area.getSelectedIndexes())));
		} else {
		    // item = new JMenuItem(BgCardsMoveAction
		    // .pointToTempToLastInstance(area, tempFrame, me
		    // .getPoint()));
		    item = new JMenuItem(BgCardsMoveAction
			    .toTempToLastInstance(area, tempFrame,
				    convertToList(area.getSelectedIndexes())));
		}
		move.add(item);
	    }

	    Iterator<BgAreaLabel> it = getAreaSet().iterator();
	    while (it.hasNext()) {
		final BgAreaLabel al = it.next();
		JMenuItem item;
		if (frame == null) {
		    if (area.getAreaName().equals(al.getAreaName())) {
			continue;
		    }
		    // item = new
		    // JMenuItem(BgCardsMoveAction.pointToLastInstance(
		    // area, al, me.getPoint()));
		    item = new JMenuItem(BgCardsMoveAction.toLastInstance(area,
			    al, convertToList(area.getSelectedIndexes())));
		} else {
		    // item = new JMenuItem(BgCardsMoveAction
		    // .pointFromTempToLastInstance(frame, al, me
		    // .getPoint()));
		    item = new JMenuItem(BgCardsMoveAction
			    .fromTempToLastInstance(frame, al,
				    convertToList(area.getSelectedIndexes())));
		}
		move.add(item);
	    }
	    return move;
	}

	// private JMenu createAllMoveMenu(final BgAreaLabel area) {
	// JMenu allMove = new JMenu("allMove");
	// Iterator<BgTempAreaInternalFrame> tempIt = getTempFrameSet()
	// .iterator();
	// // Iterator<BgAreaLabel> tempIt = getTempAreaSet().iterator();
	// while (tempIt.hasNext()) {
	// BgTempAreaInternalFrame tempFrame = tempIt.next();
	// final BgAreaLabel tempAl = tempFrame.getAreaLabel();
	// JMenuItem item;
	// if (frame != null) {
	// if (area.getAreaName().equals(tempAl.getAreaName())) {
	// continue;
	// }
	// item = new JMenuItem(BgAreaMoveAction
	// .bothTemptoLastInstance(frame, tempFrame));
	// } else {
	// item = new JMenuItem(BgAreaMoveAction.toTemptoLastInstance(
	// area, tempFrame));
	// }
	// allMove.add(item);
	// }
	//
	// Iterator<BgAreaLabel> it = getAreaSet().iterator();
	// while (it.hasNext()) {
	// final BgAreaLabel al = it.next();
	// JMenuItem item;
	// if (frame == null) {
	// if (area.getAreaName().equals(al.getAreaName())) {
	// continue;
	// }
	// item = new JMenuItem(BgAreaMoveAction.toLastInstance(area,
	// al));
	// } else {
	// item = new JMenuItem(BgAreaMoveAction
	// .fromTemptoLastInstance(frame, al));
	// }
	// allMove.add(item);
	// }
	// return allMove;
	// }

	private JMenu createCardsDirectionMenu(final BgAreaLabel area,
		final MouseEvent me) {
	    JMenu direction = new JMenu("cardsDirection");
	    Direction[] dirs = BgCardState.Direction.values();
	    for (Direction dir : dirs) {
		// JMenuItem item = new JMenuItem(BgCardsDirectionAction
		// .pointInstance(area, dir, me.getPoint()));
		JMenuItem item = new JMenuItem(BgCardsDirectionAction
			.newInstance(area, dir, convertToList(area
				.getSelectedIndexes())));
		direction.add(item);
	    }
	    return direction;
	}

	// private JMenu createAreaDirectionMenu(final BgAreaLabel area) {
	// JMenu direction = new JMenu("areaDirection");
	// Direction[] dirs = BgCardState.Direction.values();
	// for (Direction dir : dirs) {
	// JMenuItem item = new JMenuItem(BgAreaDirectionAction
	// .newInstance(area, dir));
	// direction.add(item);
	// }
	// return direction;
	// }

	private JMenu createCardsSideMenu(final BgAreaLabel area,
		final MouseEvent me) {
	    JMenu side = new JMenu("cardsSide");
	    Side[] sides = BgCardState.Side.values();
	    for (Side sd : sides) {
		// JMenuItem item = new
		// JMenuItem(BgCardsSideAction.pointInstance(
		// area, sd, me.getPoint()));
		JMenuItem item = new JMenuItem(BgCardsSideAction.newInstance(
			area, sd, convertToList(area.getSelectedIndexes())));
		side.add(item);
	    }
	    return side;
	}

	// private JMenu createAreaSideMenu(final BgAreaLabel area) {
	// JMenu menu = new JMenu("areaSide");
	// Side[] sides = BgCardState.Side.values();
	// for (Side side : sides) {
	// JMenuItem item = new JMenuItem(new BgAreaSideAction(area, side));
	// menu.add(item);
	// }
	// return menu;
	// }

	// private JMenu createSideMenu(final BgAreaLabel area) {
	// JMenu menu = new JMenu("side");
	// Side[] sides = BgCardState.Side.values();
	// for (Side side : sides) {
	// JMenuItem item = new JMenuItem(new BgAreaSideAction(area, side));
	// menu.add(item);
	// }
	// return menu;
	// }
    }

    private void addMouseListener(final BgAreaLabel area) {
	area.addMouseListener(new AreaMouseListener(area));
    }

    private void addMouseListener(final BgAreaLabel area,
	    BgTempAreaInternalFrame frame) {
	area.addMouseListener(new AreaMouseListener(area, frame));
    }

    // private JDialog tempDialog;

    // private JDialog getTempDialog() {
    // if (tempDialog == null) {
    // tempDialog = new JDialog(BgGuiUtil.getOwnerWindow(this));
    // tempDialog.setResizable(true);
    // tempDialog.setBounds(500, 200, 300, 210);
    // tempDialog.setResizable(false);
    // tempDialog.setModalityType(ModalityType.MODELESS);
    // tempDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    // // tempDialog.setUndecorated(true);
    // JScrollPane scp = new JScrollPane();
    // scp.setViewportView(get_temp());
    // tempDialog.getContentPane().add(scp, BorderLayout.CENTER);
    // }
    // return tempDialog;
    // }

    // private BgAreaLabel _temp;
    //
    // private BgAreaLabel get_temp() {
    // if (_temp == null) {
    // BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
    // FlatDirection.LEFT_TO_RIGHT, BaseEdge.HEIGHT);
    // layout.setGapPercentage(50);
    // layout.setMargin(new Insets(0, 10, 0, 10));
    //
    // ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
    // _temp = BgAreaLabel.newInstance(items, layout);
    // _temp.setBounds(200, 200, 600, 200);
    // _temp.getAreaLayout().setInitialDirection(Direction.UP);
    // _temp.getAreaLayout().setInitialSide(Side.FACE);
    //
    // _temp.setPreferredSize(new Dimension(800, 160));
    // _temp.setMaximumSize(new Dimension(800, 160));
    // _temp.setMinimumSize(new Dimension(800, 160));
    // _temp.setBackground(Color.BLACK);
    // }
    // return _temp;
    // }

    // private class TempThread implements Runnable {
    // @Override
    // public void run() {
    // try {
    // while (true) {
    // if (get_temp().getCards().size() > 0) {
    // if (!getTempDialog().isVisible()) {
    // SwingUtilities.invokeAndWait(new Runnable() {
    // @Override
    // public void run() {
    // get_temp().repaint();
    // getTempDialog().setVisible(true);
    // }
    // });
    // }
    // } else {
    // if (getTempDialog().isVisible()) {
    // SwingUtilities.invokeAndWait(new Runnable() {
    // @Override
    // public void run() {
    // get_temp().repaint();
    // getTempDialog().setVisible(false);
    // }
    // });
    // }
    // }
    // }
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // } catch (InvocationTargetException e) {
    // e.printStackTrace();
    // }
    // }
    // }
}
