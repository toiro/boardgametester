/**
 *
 */
package site.com.google.anywaywrite.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgTempAreaInternalFrame;
import site.com.google.anywaywrite.item.card.BgCardItem;

/**
 * @author kitajima
 * 
 */
public class BgAreaMoveAction extends AbstractAction implements BgAreaAction {

    private static final long serialVersionUID = 1L;

    private BgAreaLabel fromArea;
    private BgAreaLabel toArea;
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

    private BgAreaMoveAction(BgAreaLabel from, BgAreaLabel to, int insertIndex) {
	setFromArea(from);
	setToArea(to);
	setInsertIndex(insertIndex);
	this.putValue(NAME, to.getAreaName());
    }

    public static BgAreaMoveAction newInstance(BgAreaLabel from,
	    BgAreaLabel to, int insertIndex) {
	return new BgAreaMoveAction(from, to, insertIndex);
    }

    public static BgAreaMoveAction toLastInstance(BgAreaLabel from,
	    BgAreaLabel to) {
	return new BgAreaMoveAction(from, to, to.getCards().size());
    }

    public static BgAreaMoveAction toFirstInstance(BgAreaLabel from,
	    BgAreaLabel to) {
	return new BgAreaMoveAction(from, to, 0);
    }

    private BgTempAreaInternalFrame fromFrame;
    private BgTempAreaInternalFrame toFrame;

    private BgAreaMoveAction(BgTempAreaInternalFrame from, BgAreaLabel to,
	    int insertIndex) {
	this.fromFrame = from;
	setFromArea(from.getAreaLabel());
	setToArea(to);
	setInsertIndex(insertIndex);
	this.putValue(NAME, to.getAreaName());
    }

    public static BgAreaMoveAction fromTempInstance(
	    BgTempAreaInternalFrame from, BgAreaLabel to, int insertIndex) {
	return new BgAreaMoveAction(from, to, insertIndex);
    }

    public static BgAreaMoveAction fromTemptoLastInstance(
	    BgTempAreaInternalFrame from, BgAreaLabel to) {
	return new BgAreaMoveAction(from, to, to.getCards().size());
    }

    public static BgAreaMoveAction fromTemptoFirstInstance(
	    BgTempAreaInternalFrame from, BgAreaLabel to) {
	return new BgAreaMoveAction(from, to, 0);
    }

    private BgAreaMoveAction(BgAreaLabel from, BgTempAreaInternalFrame to,
	    int insertIndex) {
	setFromArea(from);
	this.toFrame = to;
	setToArea(to.getAreaLabel());
	setInsertIndex(insertIndex);
	this.putValue(NAME, to.getAreaName());
    }

    public static BgAreaMoveAction toTempInstance(BgAreaLabel from,
	    BgTempAreaInternalFrame to, int insertIndex) {
	return new BgAreaMoveAction(from, to, insertIndex);
    }

    public static BgAreaMoveAction toTemptoLastInstance(BgAreaLabel from,
	    BgTempAreaInternalFrame to) {
	return new BgAreaMoveAction(from, to, to.getAreaLabel().getCards()
		.size());
    }

    public static BgAreaMoveAction toTemptoFirstInstance(BgAreaLabel from,
	    BgTempAreaInternalFrame to) {
	return new BgAreaMoveAction(from, to, 0);
    }

    private BgAreaMoveAction(BgTempAreaInternalFrame from,
	    BgTempAreaInternalFrame to, int insertIndex) {
	this.fromFrame = from;
	setFromArea(from.getAreaLabel());
	this.toFrame = to;
	setToArea(to.getAreaLabel());
	setInsertIndex(insertIndex);
	this.putValue(NAME, to.getAreaName());
    }

    public static BgAreaMoveAction bothTempInstance(
	    BgTempAreaInternalFrame from, BgTempAreaInternalFrame to,
	    int insertIndex) {
	return new BgAreaMoveAction(from, to, insertIndex);
    }

    public static BgAreaMoveAction bothTemptoLastInstance(
	    BgTempAreaInternalFrame from, BgTempAreaInternalFrame to) {
	return new BgAreaMoveAction(from, to, to.getAreaLabel().getCards()
		.size());
    }

    public static BgAreaMoveAction bothTemptoFirstInstance(
	    BgTempAreaInternalFrame from, BgTempAreaInternalFrame to) {
	return new BgAreaMoveAction(from, to, 0);
    }

    @Override
    public void actionPerformed(ActionEvent actionevent) {
	List<Integer> targetIndexes = new ArrayList<Integer>();
	for (int idx = 0, size = getFromArea().getCards().size(); idx < size; idx++) {
	    targetIndexes.add(idx);
	}

	BgCardsMoveAction action = BgCardsMoveAction.newInstance(getFromArea(),
		getToArea(), targetIndexes, getInsertIndex());
	action.setMoveActionSuccessHook(getMoveActionSuccessHook());

	action.actionPerformed(new ActionEvent(this,
		ActionEvent.ACTION_PERFORMED, getToArea().getAreaName()));

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

	List<BgCardItem> list = new ArrayList<BgCardItem>(getToArea()
		.getCards());

	List<BgCardItem> froms = getFromArea().getCards();
	for (BgCardItem c : froms) {
	    BgCardItem nc = BgCardItem.newInstance(c.getInfo());
	    nc.setDirection(getToArea().getAreaLayout().getInitialDirection());
	    nc.setSide(getToArea().getAreaLayout().getInitialSide());
	    list.add(nc);
	}

	if (getToArea().getAreaLayout().verify(list) != null) {
	    return false;
	}
	return true;
    }
}
