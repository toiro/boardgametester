/**
 *
 */
package site.com.google.anywaywrite.component.menu;

import javax.swing.JMenuItem;

import site.com.google.anywaywrite.action.BgCardDetailAction;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.util.swing.BgGuiUtil;

/**
 * @author kitajima
 * 
 */
public class BgCreateCardDetailActionMenu implements BgCreateActionMenu {

    public static BgCreateCardDetailActionMenu newInstance() {
	return new BgCreateCardDetailActionMenu();
    }

    @Override
    public BgMenuItem createActionMenu(BgAreaLabel area) {
	JMenuItem item;
	if (area.getPlayFrame() != null) {
	    // item = new JMenuItem(BgCardDetailAction.pointInstance(frame,
	    // BgAreaLabelPane.this, BgGuiUtil.getOwnerWindow(area), e
	    // .getPoint()));
	} else {
	    item = new JMenuItem(BgCardDetailAction.newInstance(area, area
		    .getPlayBoard(), BgGuiUtil.getOwnerWindow(area), area
		    .getPointedCardIndex(), 130));
	    // item = new JMenuItem(BgCardDetailAction.pointInstance(area,
	    // BgAreaLabelPane.this, BgGuiUtil.getOwnerWindow(area), e
	    // .getPoint(), 130));
	}

	item = new JMenuItem(BgCardDetailAction.newInstance(area, area
		.getPlayBoard(), BgGuiUtil.getOwnerWindow(area), area
		.getPointedCardIndex(), 130));
	return BgMenuItem.hasSeparateInstance(item);
    }

}
