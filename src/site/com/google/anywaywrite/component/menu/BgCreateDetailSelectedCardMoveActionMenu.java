/**
 *
 */
package site.com.google.anywaywrite.component.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import site.com.google.anywaywrite.action.BgAreaActionEnableControlPlugin;
import site.com.google.anywaywrite.action.BgCardsRemoveAction;
import site.com.google.anywaywrite.action.BgRemoveActionSuccessHook;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgAreaLabelPane;
import site.com.google.anywaywrite.component.gui.BgTempAreaInternalFrame;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.util.control.BgAreaControlUtil;

/**
 * @author kitajima
 * 
 */
public class BgCreateDetailSelectedCardMoveActionMenu implements
	BgCreateActionMenu {

    private BgAreaLabel fromArea;
    private BgAreaLabelPane labelPane;
    private BgTempAreaInternalFrame frame;
    private JDialog dialog;

    private BgCreateDetailSelectedCardMoveActionMenu(BgAreaLabel fromArea,
	    BgAreaLabelPane labelPane, BgTempAreaInternalFrame frame,
	    JDialog dialog) {
	setFromArea(fromArea);
	setLabelPane(labelPane);
	setFrame(frame);
	setDialog(dialog);
    }

    public static BgCreateDetailSelectedCardMoveActionMenu newInstance(
	    BgAreaLabel fromArea, BgAreaLabelPane labelPane,
	    BgTempAreaInternalFrame frame, JDialog dialog) {
	return new BgCreateDetailSelectedCardMoveActionMenu(fromArea,
		labelPane, frame, dialog);
    }

    private final BgAreaLabel getFromArea() {
	return fromArea;
    }

    private final void setFromArea(BgAreaLabel fromArea) {
	this.fromArea = fromArea;
    }

    private final BgAreaLabelPane getLabelPane() {
	return labelPane;
    }

    private final void setLabelPane(BgAreaLabelPane labelPane) {
	this.labelPane = labelPane;
    }

    private final void setFrame(BgTempAreaInternalFrame frame) {
	this.frame = frame;
    }

    private final BgTempAreaInternalFrame getFrame() {
	return this.frame;
    }

    private final JDialog getDialog() {
	return dialog;
    }

    private final void setDialog(JDialog dialog) {
	this.dialog = dialog;
    }

    @Override
    public BgMenuItem createActionMenu(final BgAreaLabel area) {
	JMenu move = new JMenu("move");
	Iterator<BgAreaLabel> it = getLabelPane().getAreaSet().iterator();
	while (it.hasNext()) {
	    final BgAreaLabel al = it.next();
	    if (getFromArea().getAreaName().equals(al.getAreaName())
		    || "temp".equals(al.getAreaName())) {
		continue;
	    }

	    BgCardsRemoveAction action = BgCardsRemoveAction.namedInstance(
		    area, convertToList(area), al.getAreaName());

	    action.setRemoveActionSuccessHook(new BgRemoveActionSuccessHook() {
		@Override
		public void actionDone(BgAreaLabel fromArea,
			List<BgCardItem> moveCards, List<Integer> moveIndexes) {
		    for (int idx = 0, size = moveIndexes.size(); idx < size; idx++) {
			BgAreaControlUtil.moveFromBottom(moveIndexes.get(idx)
				- idx, getFromArea(), al);
			if (getFromArea().getCards().size() == 0) {
			    getDialog().dispose();
			}
			if (getFrame() != null) {
			    if (getFromArea().getCards().size() > 0) {
				getFrame().setVisible(true);
			    } else {
				getFrame().setVisible(false);
			    }
			}
		    }
		}
	    });
	    action
		    .setEnableControlPlugin(new BgAreaActionEnableControlPlugin() {
			@Override
			public boolean isEnable() {
			    List<BgCardItem> cards = new ArrayList<BgCardItem>(
				    al.getCards());

			    Set<Integer> selIdxes = area.getSelectedIndexes();
			    for (Iterator<Integer> it = selIdxes.iterator(); it
				    .hasNext();) {
				BgCardItem item = BgCardItem.newInstance(area
					.getCards().get(it.next()).getInfo());
				item.setDirection(al.getAreaLayout()
					.getInitialDirection());
				item.setSide(al.getAreaLayout()
					.getInitialSide());
				cards.add(item);
			    }
			    // for (BgCardItem card : getFromArea().getCards())
			    // {
			    // BgCardItem item = BgCardItem.newInstance(card
			    // .getInfo());
			    // item.setDirection(al.getAreaLayout()
			    // .getInitialDirection());
			    // item.setSide(al.getAreaLayout()
			    // .getInitialSide());
			    // cards.add(item);
			    // }
			    if (al.getAreaLayout().verify(cards) != null) {
				return false;
			    }
			    return true;
			}
		    });
	    JMenuItem item = new JMenuItem(action);
	    move.add(item);
	}
	return BgMenuItem.newInstance(move);
    }

    private List<Integer> convertToList(BgAreaLabel area) {
	List<Integer> ret = new ArrayList<Integer>();
	for (Iterator<Integer> it = area.getSelectedIndexes().iterator(); it
		.hasNext();) {
	    ret.add(it.next());
	}
	return ret;
    }
}
