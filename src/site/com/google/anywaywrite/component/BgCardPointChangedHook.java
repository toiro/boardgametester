/**
 *
 */
package site.com.google.anywaywrite.component;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;

/**
 * @author kitajima
 * 
 */
public interface BgCardPointChangedHook {
    void pointChanged(BgAreaLabel area, int pointIndex);
}
