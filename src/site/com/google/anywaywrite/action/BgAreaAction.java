/**
 *
 */
package site.com.google.anywaywrite.action;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;

/**
 * @author kitajima
 * 
 */
public interface BgAreaAction {

    public BgAreaLabel getFromArea();

    public BgAreaLabel getToArea();

    public BgAreaActionEnableControlPlugin getEnableControlPlugin();
}
