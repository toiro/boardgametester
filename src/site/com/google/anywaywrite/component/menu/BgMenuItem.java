/**
 *
 */
package site.com.google.anywaywrite.component.menu;

import javax.swing.JMenuItem;

/**
 * @author kitajima
 * 
 */
public class BgMenuItem {

    private JMenuItem menuItem;
    private boolean hasSeparator;

    private BgMenuItem(JMenuItem menuItem, boolean hasSeparator) {
	setMenuItem(menuItem);
	setHasSeparator(hasSeparator);
    }

    public static BgMenuItem newInstance(JMenuItem menuItem) {
	return new BgMenuItem(menuItem, false);
    }

    public static BgMenuItem hasSeparateInstance(JMenuItem menuItem) {
	return new BgMenuItem(menuItem, true);
    }

    private void setMenuItem(JMenuItem val) {
	this.menuItem = val;
    }

    public JMenuItem getMenuItem() {
	return this.menuItem;
    }

    private void setHasSeparator(boolean val) {
	this.hasSeparator = val;
    }

    public boolean isHasSeparator() {
	return this.hasSeparator;
    }
}
