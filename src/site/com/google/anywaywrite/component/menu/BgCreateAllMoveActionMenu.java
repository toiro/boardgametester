/**
 *
 */
package site.com.google.anywaywrite.component.menu;

import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import site.com.google.anywaywrite.action.BgAreaMoveAction;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgTempAreaInternalFrame;
import site.com.google.anywaywrite.component.gui.BgAreaLabel.BgDefaultAreaActionMenuMame;

/**
 * @author kitajima
 * 
 */
public class BgCreateAllMoveActionMenu implements BgCreateActionMenu {

    public static BgCreateAllMoveActionMenu newInstance() {
	return new BgCreateAllMoveActionMenu();
    }

    @Override
    public BgMenuItem createActionMenu(BgAreaLabel area) {
	JMenu allMove = new JMenu(BgDefaultAreaActionMenuMame.allMove
		.getActionName());

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
		    item = new JMenuItem(BgAreaMoveAction
			    .bothTemptoLastInstance(area.getPlayFrame(),
				    tempFrame));
		} else {
		    item = new JMenuItem(BgAreaMoveAction.toTemptoLastInstance(
			    area, tempFrame));
		}
		allMove.add(item);
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
		    item = new JMenuItem(BgAreaMoveAction.toLastInstance(area,
			    al));
		} else {
		    item = new JMenuItem(BgAreaMoveAction
			    .fromTemptoLastInstance(area.getPlayFrame(), al));
		}
		allMove.add(item);
	    }
	}
	return BgMenuItem.newInstance(allMove);
    }

}
