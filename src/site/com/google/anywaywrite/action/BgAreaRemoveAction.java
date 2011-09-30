/**
 *
 */
package site.com.google.anywaywrite.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.item.card.BgCardItem;

/**
 * ゲーム上から完全に「エリア上の全てのカード」を削除するActionです。 Detail機能から呼ばれることを想定して作っているActionで、
 * 通常このActionを呼ぶ必要はないはずです。
 * 
 * @author kitajima
 * 
 */
public class BgAreaRemoveAction extends AbstractAction implements BgAreaAction {

    private static final long serialVersionUID = 1L;

    private BgAreaLabel fromArea;
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

    public final BgAreaActionEnableControlPlugin getEnableControlPlugin() {
	return enableControlPlugin;
    }

    public final void setEnableControlPlugin(
	    BgAreaActionEnableControlPlugin areaActionEnableControlPlugin) {
	this.enableControlPlugin = areaActionEnableControlPlugin;
    }

    private BgAreaRemoveAction(BgAreaLabel from, String areaName) {
	setFromArea(from);
	this.putValue(NAME, areaName);
    }

    public static BgAreaRemoveAction newInstance(BgAreaLabel from) {
	return new BgAreaRemoveAction(from, from.getAreaName());
    }

    public static BgAreaRemoveAction namedInstance(BgAreaLabel from,
	    String areaName) {
	return new BgAreaRemoveAction(from, areaName);
    }

    @Override
    public void actionPerformed(ActionEvent actionevent) {
	List<Integer> indexList = new ArrayList<Integer>();
	List<BgCardItem> backupList = new ArrayList<BgCardItem>();
	List<BgCardItem> cards = getFromArea().getCards();
	for (int idx = 0, size = cards.size(); idx < size; idx++) {
	    BgCardItem backup = BgCardItem.newInstance(getFromArea().getCards()
		    .get(idx).getInfo());
	    backup.setDirection(getFromArea().getCards().get(idx)
		    .getDirection());
	    backup.setSide(getFromArea().getCards().get(idx).getSide());
	    backupList.add(backup);
	    indexList.add(idx);
	}

	BgCardsRemoveAction action = BgCardsRemoveAction.newInstance(
		getFromArea(), indexList);
	action.setRemoveActionSuccessHook(getRemoveActionSuccessHook());

	action.actionPerformed(new ActionEvent(this,
		ActionEvent.ACTION_PERFORMED, getFromArea().getAreaName()));

	// BgAreaControlUtil.removeAll(getFromArea());
	//
	// if (getRemoveActionSuccessHook() == null) {
	// return;
	// }
	// getRemoveActionSuccessHook().actionDone(getFromArea(), backupList,
	// indexList);
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
	return true;
    }
}
