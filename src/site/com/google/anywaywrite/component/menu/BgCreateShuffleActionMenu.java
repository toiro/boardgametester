/**
 *
 */
package site.com.google.anywaywrite.component.menu;

import javax.swing.JMenuItem;

import site.com.google.anywaywrite.action.BgAreaShuffleAction;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgAreaLabel.BgDefaultAreaActionMenuMame;

/**
 * @author kitajima
 * 
 */
public class BgCreateShuffleActionMenu implements BgCreateActionMenu {

    public static BgCreateShuffleActionMenu newInstance() {
	return new BgCreateShuffleActionMenu();
    }

    @Override
    public BgMenuItem createActionMenu(BgAreaLabel area) {
	return BgMenuItem.newInstance(new JMenuItem(BgAreaShuffleAction
		.namedInstance(area, BgDefaultAreaActionMenuMame.shuffle
			.getActionName())));
    }

}
