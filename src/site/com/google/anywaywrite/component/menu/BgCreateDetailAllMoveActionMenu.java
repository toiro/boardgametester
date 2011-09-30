/**
 *
 */
package site.com.google.anywaywrite.component.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import site.com.google.anywaywrite.action.BgAreaActionEnableControlPlugin;
import site.com.google.anywaywrite.action.BgAreaRemoveAction;
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
public class BgCreateDetailAllMoveActionMenu implements BgCreateActionMenu {

    private BgAreaLabel fromArea;
    private BgAreaLabelPane labelPane;
    private BgTempAreaInternalFrame frame;
    private JDialog dialog;

    private BgCreateDetailAllMoveActionMenu(BgAreaLabel fromArea,
	    BgAreaLabelPane labelPane, BgTempAreaInternalFrame frame,
	    JDialog dialog) {
	setFromArea(fromArea);
	setLabelPane(labelPane);
	setFrame(frame);
	setDialog(dialog);
    }

    public static BgCreateDetailAllMoveActionMenu newInstance(
	    BgAreaLabel fromArea, BgAreaLabelPane labelPane,
	    BgTempAreaInternalFrame frame, JDialog dialog) {
	return new BgCreateDetailAllMoveActionMenu(fromArea, labelPane, frame,
		dialog);
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
    public BgMenuItem createActionMenu(BgAreaLabel area) {
	JMenu allMove = new JMenu("allMove");
	Iterator<BgAreaLabel> it = getLabelPane().getAreaSet().iterator();
	while (it.hasNext()) {
	    final BgAreaLabel al = it.next();
	    if (getFromArea().getAreaName().equals(al.getAreaName())
		    || "temp".equals(al.getAreaName())) {
		continue;
	    }
	    BgAreaRemoveAction action = BgAreaRemoveAction.namedInstance(area,
		    al.getAreaName());
	    action.setRemoveActionSuccessHook(new BgRemoveActionSuccessHook() {
		@Override
		public void actionDone(BgAreaLabel fromArea,
			List<BgCardItem> moveCards, List<Integer> moveIndexes) {
		    BgAreaControlUtil.moveAll(getFromArea(), al);
		    getDialog().setTitle(
			    getFromArea().getAreaName() + " ["
				    + getFromArea().getCards().size() + "]");
		    getDialog().dispose();
		    if (getFrame() != null) {
			if (getFromArea().getCards().size() > 0) {
			    getFrame().setVisible(true);
			} else {
			    getFrame().setVisible(false);
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
			    for (BgCardItem card : getFromArea().getCards()) {
				BgCardItem item = BgCardItem.newInstance(card
					.getInfo());
				item.setDirection(al.getAreaLayout()
					.getInitialDirection());
				item.setSide(al.getAreaLayout()
					.getInitialSide());
				cards.add(item);
			    }
			    if (al.getAreaLayout().verify(cards) != null) {
				return false;
			    }
			    return true;
			}
		    });
	    JMenuItem item = new JMenuItem(action);
	    allMove.add(item);
	}
	return BgMenuItem.newInstance(allMove);
    }
}
