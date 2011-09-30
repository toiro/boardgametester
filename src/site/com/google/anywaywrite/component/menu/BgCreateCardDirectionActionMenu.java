/**
 *
 */
package site.com.google.anywaywrite.component.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import site.com.google.anywaywrite.action.BgCardsDirectionAction;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.item.card.BgCardState;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;

/**
 * @author kitajima
 * 
 */
public class BgCreateCardDirectionActionMenu implements BgCreateActionMenu {

    public static BgCreateCardDirectionActionMenu newInstance() {
	return new BgCreateCardDirectionActionMenu();
    }

    @Override
    public BgMenuItem createActionMenu(BgAreaLabel area) {
	JMenu direction = new JMenu("cardsDirection");
	Direction[] dirs = BgCardState.Direction.values();
	for (Direction dir : dirs) {
	    // JMenuItem item = new JMenuItem(BgCardsDirectionAction
	    // .pointInstance(area, dir, me.getPoint()));
	    JMenuItem item = new JMenuItem(BgCardsDirectionAction.newInstance(
		    area, dir, convertToList(area)));
	    direction.add(item);
	}
	return BgMenuItem.newInstance(direction);
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
