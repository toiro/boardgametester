/**
 *
 */
package site.com.google.anywaywrite.component.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import site.com.google.anywaywrite.action.BgCardsSideAction;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.item.card.BgCardState;
import site.com.google.anywaywrite.item.card.BgCardState.Side;

/**
 * @author kitajima
 * 
 */
public class BgCreateCardSideActionMenu implements BgCreateActionMenu {

    public static BgCreateCardSideActionMenu newInstance() {
	return new BgCreateCardSideActionMenu();
    }

    @Override
    public BgMenuItem createActionMenu(BgAreaLabel area) {
	JMenu side = new JMenu("cardsSide");
	Side[] sides = BgCardState.Side.values();
	for (Side sd : sides) {
	    // JMenuItem item = new
	    // JMenuItem(BgCardsSideAction.pointInstance(
	    // area, sd, me.getPoint()));
	    JMenuItem item = new JMenuItem(BgCardsSideAction.newInstance(area,
		    sd, convertToList(area)));
	    side.add(item);
	}
	return BgMenuItem.newInstance(side);
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
