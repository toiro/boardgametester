/**
 *
 */
package site.com.google.anywaywrite.component.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import site.com.google.anywaywrite.action.BgCardsMoveAction;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgTempAreaInternalFrame;

/**
 * @author kitajima
 * 
 */
public class BgCreateCardMoveActionMenu implements BgCreateActionMenu {

    public static BgCreateCardMoveActionMenu newInstance() {
	return new BgCreateCardMoveActionMenu();
    }

    @Override
    public BgMenuItem createActionMenu(BgAreaLabel area) {
	JMenu move = new JMenu("move");

	if (area.getPlayBoard() != null) {
	    Iterator<BgTempAreaInternalFrame> tempIt = area.getPlayBoard()
		    .getTempFrameSet().iterator();
	    while (tempIt.hasNext()) {
		BgTempAreaInternalFrame tempFrame = tempIt.next();
		final BgAreaLabel tempAl = tempFrame.getAreaLabel();
		JMenuItem item;
		if (area.getPlayFrame() != null) {
		    if (area.getAreaName().equals(tempAl.getAreaName())) {
			continue;
		    }
		    // item = new JMenuItem(BgCardsMoveAction
		    // .pointBothTempToLastInstance(frame, tempFrame, me
		    // .getPoint()));
		    item = new JMenuItem(BgCardsMoveAction
			    .bothTempToLastInstance(area.getPlayFrame(),
				    tempFrame, convertToList(area)));
		} else {
		    // item = new JMenuItem(BgCardsMoveAction
		    // .pointToTempToLastInstance(area, tempFrame, me
		    // .getPoint()));
		    item = new JMenuItem(BgCardsMoveAction
			    .toTempToLastInstance(area, tempFrame,
				    convertToList(area)));
		}
		move.add(item);
	    }

	    Iterator<BgAreaLabel> it = area.getPlayBoard().getAreaSet()
		    .iterator();
	    while (it.hasNext()) {
		final BgAreaLabel al = it.next();
		JMenuItem item;
		if (area.getPlayFrame() == null) {
		    if (area.getAreaName().equals(al.getAreaName())) {
			continue;
		    }
		    // item = new
		    // JMenuItem(BgCardsMoveAction.pointToLastInstance(
		    // area, al, me.getPoint()));
		    item = new JMenuItem(BgCardsMoveAction.toLastInstance(area,
			    al, convertToList(area)));
		} else {
		    // item = new JMenuItem(BgCardsMoveAction
		    // .pointFromTempToLastInstance(frame, al, me
		    // .getPoint()));
		    item = new JMenuItem(BgCardsMoveAction
			    .fromTempToLastInstance(area.getPlayFrame(), al,
				    convertToList(area)));
		}
		move.add(item);
	    }
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
