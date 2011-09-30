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
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.util.control.BgAreaControlUtil;

/**
 * @author kitajima
 * 
 */
public class BgCardsRemoveAction extends AbstractAction implements BgAreaAction {

    private static final long serialVersionUID = 1L;

    private BgAreaLabel fromArea;
    private List<Integer> cardIndexes;
    private BgRemoveActionSuccessHook removeActionSuccessHook;
    private BgAreaActionEnableControlPlugin enableControlPlugin;

    @Override
    public final BgAreaLabel getFromArea() {
	return fromArea;
    }

    @Override
    public final BgAreaLabel getToArea() {
	return null;
    }

    private final void setFromArea(BgAreaLabel fromArea) {
	this.fromArea = fromArea;
    }

    private final BgRemoveActionSuccessHook getRemoveActionSuccessHook() {
	return removeActionSuccessHook;
    }

    public final void setRemoveActionSuccessHook(BgRemoveActionSuccessHook hook) {
	this.removeActionSuccessHook = hook;
    }

    private final List<Integer> getCardIndexes() {
	if (cardIndexes == null) {
	    cardIndexes = new ArrayList<Integer>();
	}
	return cardIndexes;
    }

    public final BgAreaActionEnableControlPlugin getEnableControlPlugin() {
	return enableControlPlugin;
    }

    public final void setEnableControlPlugin(
	    BgAreaActionEnableControlPlugin areaActionEnableControlPlugin) {
	this.enableControlPlugin = areaActionEnableControlPlugin;
    }

    private BgCardsRemoveAction(BgAreaLabel from, List<Integer> cardIndexes,
	    String actionName) {
	setFromArea(from);
	getCardIndexes().addAll(cardIndexes);
	this.putValue(NAME, actionName);
    }

    public static BgCardsRemoveAction pointInstance(BgAreaLabel from, Point p) {
	List<Rectangle> cr = from.getAreaLayout().getCardRectangles();
	int cardIndex = -1;
	for (int idx = 0, size = cr.size(); idx < size; idx++) {
	    if (cr.get(idx).contains(p.getX(), p.getY())) {
		cardIndex = idx;
	    }
	}
	List<Integer> list = new ArrayList<Integer>();
	list.add(cardIndex);

	return new BgCardsRemoveAction(from, list, "remove");
    }

    public static BgCardsRemoveAction newInstance(BgAreaLabel from,
	    List<Integer> targetIndexes) {
	return new BgCardsRemoveAction(from, targetIndexes, "remove");
    }

    public static BgCardsRemoveAction namedInstance(BgAreaLabel from,
	    List<Integer> targetIndexes, String actionName) {
	return new BgCardsRemoveAction(from, targetIndexes, actionName);
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
	    BgAreaControlUtil.removeFromBottom(indexList.get(idx) - idx,
		    getFromArea());
	}

	getFromArea().clearCardsSelection();

	if (getRemoveActionSuccessHook() == null) {
	    return;
	}
	if (getCardIndexes().contains(-1)) {
	    getRemoveActionSuccessHook().actionDone(getFromArea(),
		    new ArrayList<BgCardItem>(), new ArrayList<Integer>());
	} else {
	    getRemoveActionSuccessHook().actionDone(getFromArea(), backupList,
		    indexList);
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

	return true;
    }

}
