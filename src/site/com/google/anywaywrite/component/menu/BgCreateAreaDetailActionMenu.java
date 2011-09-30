/**
 *
 */
package site.com.google.anywaywrite.component.menu;

import javax.swing.JMenuItem;

import site.com.google.anywaywrite.action.BgAreaDetailAction;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgAreaLabel.BgDefaultAreaActionMenuMame;
import site.com.google.anywaywrite.util.swing.BgGuiUtil;

/**
 * @author kitajima
 * 
 */
public class BgCreateAreaDetailActionMenu implements BgCreateActionMenu {

    public static BgCreateAreaDetailActionMenu newInstance() {
	return new BgCreateAreaDetailActionMenu();
    }

    @Override
    public BgMenuItem createActionMenu(BgAreaLabel area) {
	JMenuItem item;

	if (area.getPlayFrame() != null) {
	    item = new JMenuItem(BgAreaDetailAction.namedTempInstance(area
		    .getPlayFrame(), area.getPlayBoard(), BgGuiUtil
		    .getOwnerWindow(area), BgDefaultAreaActionMenuMame.detail
		    .getActionName()));
	} else {
	    item = new JMenuItem(BgAreaDetailAction.namedInstance(area, area
		    .getPlayBoard(), BgGuiUtil.getOwnerWindow(area),
		    BgDefaultAreaActionMenuMame.detail.getActionName()));
	}
	return BgMenuItem.hasSeparateInstance(item);
    }

}
