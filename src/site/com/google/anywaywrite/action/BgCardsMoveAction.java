/**
 *
 */
package site.com.google.anywaywrite.action;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgTempAreaInternalFrame;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.util.control.BgAreaControlUtil;

/**
 * @author kitajima
 * 
 */
public class BgCardsMoveAction extends AbstractAction implements BgAreaAction {

    private static final long serialVersionUID = 1L;

    private BgAreaLabel fromArea;
    private BgAreaLabel toArea;
    private List<Integer> cardIndexes;
    private int insertIndex;
    private BgMoveActionSuccessHook moveActionSuccessHook;
    private BgAreaActionEnableControlPlugin enableControlPlugin;

    @Override
    public final BgAreaLabel getFromArea() {
	return fromArea;
    }

    @Override
    public final BgAreaLabel getToArea() {
	return toArea;
    }

    private final void setFromArea(BgAreaLabel fromArea) {
	this.fromArea = fromArea;
    }

    private final void setToArea(BgAreaLabel toArea) {
	this.toArea = toArea;
    }

    private final BgMoveActionSuccessHook getMoveActionSuccessHook() {
	return moveActionSuccessHook;
    }

    public final void setMoveActionSuccessHook(BgMoveActionSuccessHook hook) {
	this.moveActionSuccessHook = hook;
    }

    private final List<Integer> getCardIndexes() {
	if (cardIndexes == null) {
	    cardIndexes = new ArrayList<Integer>();
	}
	return cardIndexes;
    }

    private void setInsertIndex(int val) {
	this.insertIndex = val;
    }

    private int getInsertIndex() {
	return this.insertIndex;
    }

    public final BgAreaActionEnableControlPlugin getEnableControlPlugin() {
	return enableControlPlugin;
    }

    public final void setEnableControlPlugin(
	    BgAreaActionEnableControlPlugin areaActionEnableControlPlugin) {
	this.enableControlPlugin = areaActionEnableControlPlugin;
    }

    private BgTempAreaInternalFrame fromFrame;
    private BgTempAreaInternalFrame toFrame;

    private BgCardsMoveAction(BgAreaLabel from, BgTempAreaInternalFrame to,
	    List<Integer> cardIndexes, int insertIndex) {
	setFromArea(from);
	this.toFrame = to;
	setToArea(to.getAreaLabel());
	getCardIndexes().addAll(cardIndexes);
	setInsertIndex(insertIndex);
	this.putValue(NAME, to.getAreaName());
    }

    private BgCardsMoveAction(BgAreaLabel from, BgAreaLabel to,
	    List<Integer> cardIndexes, int insertIndex) {
	setFromArea(from);
	setToArea(to);
	getCardIndexes().addAll(cardIndexes);
	setInsertIndex(insertIndex);
	this.putValue(NAME, to.getAreaName());
    }

    private BgCardsMoveAction(BgTempAreaInternalFrame from, BgAreaLabel to,
	    List<Integer> cardIndexes, int insertIndex) {
	this.fromFrame = from;
	setFromArea(from.getAreaLabel());
	setToArea(to);
	getCardIndexes().addAll(cardIndexes);
	setInsertIndex(insertIndex);
	this.putValue(NAME, to.getAreaName());
    }

    private BgCardsMoveAction(BgTempAreaInternalFrame from,
	    BgTempAreaInternalFrame to, List<Integer> cardIndexes,
	    int insertIndex) {
	this.fromFrame = from;
	setFromArea(from.getAreaLabel());
	this.toFrame = to;
	setToArea(to.getAreaLabel());
	getCardIndexes().addAll(cardIndexes);
	setInsertIndex(insertIndex);
	this.putValue(NAME, to.getAreaName());
    }

    public static BgCardsMoveAction newInstance(BgAreaLabel from,
	    BgAreaLabel to, List<Integer> targetIndexes, int insertIndex) {
	return new BgCardsMoveAction(from, to, targetIndexes, insertIndex);
    }

    public static BgCardsMoveAction toLastInstance(BgAreaLabel from,
	    BgAreaLabel to, List<Integer> targetIndexes) {
	return new BgCardsMoveAction(from, to, targetIndexes, to.getCards()
		.size());
    }

    public static BgCardsMoveAction toFirstInstance(BgAreaLabel from,
	    BgAreaLabel to, List<Integer> targetIndexes) {
	return new BgCardsMoveAction(from, to, targetIndexes, 0);
    }

    public static BgCardsMoveAction pointToLastInstance(BgAreaLabel from,
	    BgAreaLabel to, Point p) {
	List<Integer> list = makeIndexes(from, p);

	return new BgCardsMoveAction(from, to, list, to.getCards().size());
    }

    public static BgCardsMoveAction pointToFirstInstance(BgAreaLabel from,
	    BgAreaLabel to, Point p) {
	List<Integer> list = makeIndexes(from, p);
	return new BgCardsMoveAction(from, to, list, 0);
    }

    public static BgCardsMoveAction toTempInstance(BgAreaLabel from,
	    BgTempAreaInternalFrame toFrame, List<Integer> targetIndexes,
	    int insertIndex) {
	return new BgCardsMoveAction(from, toFrame, targetIndexes, insertIndex);
    }

    public static BgCardsMoveAction toTempToLastInstance(BgAreaLabel from,
	    BgTempAreaInternalFrame toFrame, List<Integer> targetIndexes) {
	return new BgCardsMoveAction(from, toFrame, targetIndexes, toFrame
		.getAreaLabel().getCards().size());
    }

    public static BgCardsMoveAction toTempToFirstInstance(BgAreaLabel from,
	    BgTempAreaInternalFrame toFrame, List<Integer> targetIndexes) {
	return new BgCardsMoveAction(from, toFrame, targetIndexes, 0);
    }

    public static BgCardsMoveAction pointToTempToLastInstance(BgAreaLabel from,
	    BgTempAreaInternalFrame toFrame, Point p) {
	return new BgCardsMoveAction(from, toFrame, makeIndexes(from, p),
		toFrame.getAreaLabel().getCards().size());
    }

    public static BgCardsMoveAction pointToTempToFirstInstance(
	    BgAreaLabel from, BgTempAreaInternalFrame toFrame, Point p) {
	return new BgCardsMoveAction(from, toFrame, makeIndexes(from, p), 0);
    }

    public static BgCardsMoveAction fromTempInstance(
	    BgTempAreaInternalFrame fromFrame, BgAreaLabel to,
	    List<Integer> targetIndexes, int insertIndex) {
	return new BgCardsMoveAction(fromFrame, to, targetIndexes, insertIndex);
    }

    public static BgCardsMoveAction fromTempToLastInstance(
	    BgTempAreaInternalFrame fromFrame, BgAreaLabel to,
	    List<Integer> targetIndexes) {
	return new BgCardsMoveAction(fromFrame, to, targetIndexes, to
		.getCards().size());
    }

    public static BgCardsMoveAction fromTempToFirstInstance(
	    BgTempAreaInternalFrame fromFrame, BgAreaLabel to,
	    List<Integer> targetIndexes) {
	return new BgCardsMoveAction(fromFrame, to, targetIndexes, 0);
    }

    public static BgCardsMoveAction pointFromTempToLastInstance(
	    BgTempAreaInternalFrame fromFrame, BgAreaLabel to, Point p) {
	return new BgCardsMoveAction(fromFrame, to, makeIndexes(fromFrame
		.getAreaLabel(), p), to.getCards().size());
    }

    public static BgCardsMoveAction pointFromTempToFirstInstance(
	    BgTempAreaInternalFrame fromFrame, BgAreaLabel to, Point p) {
	return new BgCardsMoveAction(fromFrame, to, makeIndexes(fromFrame
		.getAreaLabel(), p), 0);
    }

    public static BgCardsMoveAction bothTempInstance(
	    BgTempAreaInternalFrame fromFrame, BgTempAreaInternalFrame toFrame,
	    List<Integer> targetIndexes, int insertIndex) {
	return new BgCardsMoveAction(fromFrame, toFrame, targetIndexes,
		insertIndex);
    }

    public static BgCardsMoveAction bothTempToLastInstance(
	    BgTempAreaInternalFrame fromFrame, BgTempAreaInternalFrame toFrame,
	    List<Integer> targetIndexes) {
	return new BgCardsMoveAction(fromFrame, toFrame, targetIndexes, toFrame
		.getAreaLabel().getCards().size());
    }

    public static BgCardsMoveAction bothTempToFirstInstance(
	    BgTempAreaInternalFrame fromFrame, BgTempAreaInternalFrame toFrame,
	    List<Integer> targetIndexes) {
	return new BgCardsMoveAction(fromFrame, toFrame, targetIndexes, 0);
    }

    public static BgCardsMoveAction pointBothTempToLastInstance(
	    BgTempAreaInternalFrame fromFrame, BgTempAreaInternalFrame toFrame,
	    Point p) {
	return new BgCardsMoveAction(fromFrame, toFrame, makeIndexes(fromFrame
		.getAreaLabel(), p), toFrame.getAreaLabel().getCards().size());
    }

    public static BgCardsMoveAction pointBothTempToFirstInstance(
	    BgTempAreaInternalFrame fromFrame, BgTempAreaInternalFrame toFrame,
	    Point p) {
	return new BgCardsMoveAction(fromFrame, toFrame, makeIndexes(fromFrame
		.getAreaLabel(), p), 0);
    }

    private static List<Integer> makeIndexes(BgAreaLabel from, Point p) {
	List<Rectangle> cr = from.getAreaLayout().getCardRectangles();
	int cardIndex = -1;
	for (int idx = 0, size = cr.size(); idx < size; idx++) {
	    if (cr.get(idx).contains(p.getX(), p.getY())) {
		cardIndex = idx;
	    }
	}
	List<Integer> list = new ArrayList<Integer>();
	list.add(cardIndex);
	return list;
    }

    @Override
    public void actionPerformed(ActionEvent actionevent) {
	if (getCardIndexes().contains(-1)) {
	    return;
	}

	List<Integer> indexList = new ArrayList<Integer>(getCardIndexes());
	Collections.sort(indexList);

	List<BgCardItem> backupList = makeBackupList(indexList);

	for (int idx = 0, size = indexList.size(); idx < size; idx++) {
	    BgAreaControlUtil.moveFromBottomTo(indexList.get(idx) - idx,
		    getFromArea(), getInsertIndex() + idx, getToArea());
	}

	// for (int idx = 0, size = indexList.size(); idx < size; idx++) {
	// // BgAreaControlUtil.moveFromBottom(indexList.get(idx) - idx,
	// // getFromArea(), getToArea());
	// }

	if (this.fromFrame != null) {
	    if (getFromArea().getCards().size() > 0) {
		this.fromFrame.setVisible(true);
	    } else {
		this.fromFrame.setVisible(false);
	    }
	}

	if (this.toFrame != null) {
	    if (getToArea().getCards().size() > 0) {
		this.toFrame.setVisible(true);
	    } else {
		this.toFrame.setVisible(false);
	    }
	}

	getFromArea().clearCardsSelection();
	getToArea().clearCardsSelection();

	if (getMoveActionSuccessHook() == null) {
	    return;
	}
	if (getCardIndexes().contains(-1)) {
	    getMoveActionSuccessHook().actionDone(getFromArea(), getToArea(),
		    new ArrayList<BgCardItem>(), new ArrayList<Integer>());
	} else {
	    getMoveActionSuccessHook().actionDone(getFromArea(), getToArea(),
		    backupList, indexList);
	}
    }

    private List<BgCardItem> makeBackupList(List<Integer> indexList) {
	List<BgCardItem> backupList = new ArrayList<BgCardItem>();
	for (int idx = 0, size = indexList.size(); idx < size; idx++) {
	    BgCardItem backup = BgCardItem.newInstance(getFromArea().getCards()
		    .get(indexList.get(idx)).getInfo());
	    backup.setDirection(getFromArea().getCards()
		    .get(indexList.get(idx)).getDirection());
	    backup.setSide(getFromArea().getCards().get(indexList.get(idx))
		    .getSide());
	    backupList.add(backup);
	}
	return backupList;
    }

    @Override
    public boolean isEnabled() {
	if (getEnableControlPlugin() != null
		&& !getEnableControlPlugin().isEnable()) {
	    return false;
	}

	if (getFromArea().getCards().size() == 0) {
	    return false;
	}

	if (getCardIndexes().size() == 0 || getCardIndexes().contains(-1)) {
	    return false;
	}

	List<BgCardItem> cards = getIndexCards();
	List<BgCardItem> list = new ArrayList<BgCardItem>(getToArea()
		.getCards());

	List<BgCardItem> insertCards = new ArrayList<BgCardItem>();
	for (BgCardItem card : cards) {
	    BgCardItem nc = BgCardItem.newInstance(card.getInfo());
	    nc.setDirection(getToArea().getAreaLayout().getInitialDirection());
	    nc.setSide(getToArea().getAreaLayout().getInitialSide());
	    insertCards.add(nc);
	}

	list.addAll(getInsertIndex(), insertCards);

	if (getToArea().getAreaLayout().verify(list) != null) {
	    return false;
	}

	return true;
    }

    private List<BgCardItem> getIndexCards() {
	List<BgCardItem> ret = new ArrayList<BgCardItem>();

	for (int idx = 0, size = getCardIndexes().size(); idx < size; idx++) {
	    ret.add(getFromArea().getCards().get(getCardIndexes().get(idx)));
	}

	return ret;
    }

}
