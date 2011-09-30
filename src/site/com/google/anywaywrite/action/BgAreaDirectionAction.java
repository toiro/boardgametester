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
import site.com.google.anywaywrite.item.card.BgCardState.Direction;

/**
 * @author kitajima
 * 
 */
public class BgAreaDirectionAction extends AbstractAction implements
	BgAreaAction {
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
    private Direction direction;
    private BgAreaActionEnableControlPlugin enableControlPlugin;

    private final Direction getDirection() {
	return direction;
    }

    private final void setDirection(Direction direction) {
	this.direction = direction;
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

    private BgAreaDirectionAction(BgAreaLabel area, Direction dir) {
	setArea(area);
	setDirection(dir);
	// switch (dir) {
	// case UP:
	// this.putValue(NAME, "��");
	// break;
	// case RIGHT:
	// this.putValue(NAME, "��");
	// break;
	// case LEFT:
	// this.putValue(NAME, "��");
	// break;
	// case REVERSE:
	// this.putValue(NAME, "��");
	// break;
	// default:
	// throw new RuntimeException("Program error");
	// }
	this.putValue(NAME, dir.name());
    }

    public static BgAreaDirectionAction newInstance(BgAreaLabel area,
	    Direction dir) {
	return new BgAreaDirectionAction(area, dir);
    }

    @Override
    public void actionPerformed(ActionEvent actionevent) {
	List<BgCardItem> cards = getArea().getCards();
	for (BgCardItem card : cards) {
	    card.setDirection(getDirection());
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
	boolean existDifferentDirection = false;
	for (BgCardItem c : froms) {
	    if (getDirection() != c.getDirection()) {
		existDifferentDirection = true;
		break;
	    }
	}
	if (!existDifferentDirection) {
	    return false;
	}

	for (BgCardItem c : froms) {
	    BgCardItem nc = BgCardItem.newInstance(c.getInfo());
	    nc.setDirection(getDirection());
	    nc.setSide(c.getSide());
	    list.add(nc);
	}
	if (getArea().getAreaLayout().verify(list) != null) {
	    return false;
	}
	return true;
    }

}
