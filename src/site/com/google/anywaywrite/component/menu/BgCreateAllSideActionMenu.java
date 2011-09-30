/**
 *
 */
package site.com.google.anywaywrite.component.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import site.com.google.anywaywrite.action.BgAreaSideAction;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgAreaLabel.BgDefaultAreaActionMenuMame;
import site.com.google.anywaywrite.item.card.BgCardState;
import site.com.google.anywaywrite.item.card.BgCardState.Side;

/**
 * @author kitajima
 * 
 */
public class BgCreateAllSideActionMenu implements BgCreateActionMenu {

    public static BgCreateAllSideActionMenu newInstance() {
	return new BgCreateAllSideActionMenu();
    }

    @Override
    public BgMenuItem createActionMenu(BgAreaLabel area) {
	JMenu menu = new JMenu(BgDefaultAreaActionMenuMame.allSide.getActionName());
	Side[] sides = BgCardState.Side.values();
	for (Side side : sides) {
	    JMenuItem item = new JMenuItem(new BgAreaSideAction(area, side));
	    menu.add(item);
	}
	return BgMenuItem.newInstance(menu);
    }

}
