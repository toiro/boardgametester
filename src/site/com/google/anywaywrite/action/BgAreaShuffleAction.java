/**
 *
 */
package site.com.google.anywaywrite.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.util.control.BgAreaControlUtil;

/**
 * @author kitajima
 * 
 */
public class BgAreaShuffleAction extends AbstractAction implements BgAreaAction {

    private static final long serialVersionUID = 1L;

    @Override
    public BgAreaLabel getFromArea() {
	return getArea();
    }

    @Override
    public BgAreaLabel getToArea() {
	return null;
    }

    private BgAreaLabel area;
    private BgAreaActionEnableControlPlugin enableControlPlugin;

    private final BgAreaLabel getArea() {
	return area;
    }

    private final void setArea(BgAreaLabel area) {
	this.area = area;
    }

    public final BgAreaActionEnableControlPlugin getEnableControlPlugin() {
	return enableControlPlugin;
    }

    public final void setEnableControlPlugin(
	    BgAreaActionEnableControlPlugin areaActionEnableControlPlugin) {
	this.enableControlPlugin = areaActionEnableControlPlugin;
    }

    private BgAreaShuffleAction(BgAreaLabel area, String actionName) {
	setArea(area);
	this.putValue(NAME, actionName);
    }

    public static BgAreaShuffleAction newInstance(BgAreaLabel area) {
	return new BgAreaShuffleAction(area, "shuffle");
    }

    public static BgAreaShuffleAction namedInstance(BgAreaLabel area,
	    String actionName) {
	return new BgAreaShuffleAction(area, actionName);
    }

    @Override
    public void actionPerformed(ActionEvent actionevent) {
	BgAreaControlUtil.shuffle(getArea());
	getFromArea().clearCardsSelection();
    }

    @Override
    public boolean isEnabled() {
	if (getEnableControlPlugin() != null
		&& !getEnableControlPlugin().isEnable()) {
	    return false;
	}
	if (getArea().getCards().size() <= 1) {
	    return false;
	}
	return true;
    }

}
