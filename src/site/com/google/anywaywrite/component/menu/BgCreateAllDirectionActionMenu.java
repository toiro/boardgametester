/**
 *
 */
package site.com.google.anywaywrite.component.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import site.com.google.anywaywrite.action.BgAreaDirectionAction;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgAreaLabel.BgDefaultAreaActionMenuMame;
import site.com.google.anywaywrite.item.card.BgCardState;

/**
 * @author kitajima
 * 
 */
public class BgCreateAllDirectionActionMenu implements BgCreateActionMenu {

    public static BgCreateAllDirectionActionMenu newInstance() {
	return new BgCreateAllDirectionActionMenu();
    }

    @Override
    public BgMenuItem createActionMenu(BgAreaLabel area) {
	JMenu direction = new JMenu(BgDefaultAreaActionMenuMame.allDirection
		.getActionName());
	BgCardState.Direction[] dirs = BgCardState.Direction.values();
	for (BgCardState.Direction dir : dirs) {
	    JMenuItem item = new JMenuItem(BgAreaDirectionAction.newInstance(
		    area, dir));
	    direction.add(item);
	}
	return BgMenuItem.newInstance(direction);
    }

}
