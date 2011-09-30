/**
 *
 */
package site.com.google.anywaywrite.component;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;

/**
 * @author kitajima
 * 
 */
public interface BgCardSelectionChangedHook {
    void selectionChanged(BgAreaLabel area, int selectedIndex);
}
