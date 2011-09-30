/**
 *
 */
package site.com.google.anywaywrite.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.item.card.BgCardState.Side;

/**
 * @author kitajima
 * 
 */
public class BgAreaSideAction extends AbstractAction implements BgAreaAction {
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
    private Side side;
    private BgAreaActionEnableControlPlugin enableControlPlugin;

    private final Side getSide() {
	return side;
    }

    private final void setSide(Side direction) {
	this.side = direction;
    }

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

    public BgAreaSideAction(BgAreaLabel area, Side side) {
	setArea(area);
	setSide(side);
	// switch (side) {
	// case FACE:
	// this.putValue(NAME, "face");
	// break;
	// case DOWN:
	// this.putValue(NAME, "down");
	// break;
	// default:
	// throw new RuntimeException("Program error.");
	// }
	this.putValue(NAME, side.name());
    }

    @Override
    public void actionPerformed(ActionEvent actionevent) {
	List<BgCardItem> cards = getArea().getCards();
	for (BgCardItem card : cards) {
	    card.setSide(getSide());
	}
	getArea().repaint(0, 0, getArea().getWidth(), getArea().getHeight());
    }

    @Override
    public boolean isEnabled() {
	if (getEnableControlPlugin() != null
		&& !getEnableControlPlugin().isEnable()) {
	    return false;
	}

	if (getArea().getCards().size() == 0) {
	    return false;
	}

	List<BgCardItem> list = new ArrayList<BgCardItem>();

	List<BgCardItem> froms = getArea().getCards();
	boolean existDifferentSide = false;
	for (BgCardItem c : froms) {
	    if (getSide() != c.getSide()) {
		existDifferentSide = true;
		break;
	    }
	}
	if (!existDifferentSide) {
	    return false;
	}

	for (BgCardItem c : froms) {
	    BgCardItem nc = BgCardItem.newInstance(c.getInfo());
	    nc.setDirection(c.getDirection());
	    nc.setSide(getSide());
	    list.add(nc);
	}
	if (getArea().getAreaLayout().verify(list) != null) {
	    return false;
	}
	return true;
    }

}
