/**
 *
 */
package site.com.google.anywaywrite.action;

import java.awt.Point;
import java.awt.Rectangle;
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
public class BgCardsSideAction extends AbstractAction implements BgAreaAction {
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
    // private Point point;
    private List<Integer> cardIndexes;
    private BgAreaActionEnableControlPlugin enableControlPlugin;

    private final Side getSide() {
	return side;
    }

    private final void setSide(Side side) {
	this.side = side;
    }

    private final BgAreaLabel getArea() {
	return area;
    }

    private final void setArea(BgAreaLabel area) {
	this.area = area;
    }

    private final List<Integer> getCardIndexes() {
	if (cardIndexes == null) {
	    cardIndexes = new ArrayList<Integer>();
	}
	return cardIndexes;
    }

    public final BgAreaActionEnableControlPlugin getEnableControlPlugin() {
	return enableControlPlugin;
    }

    public final void setEnableControlPlugin(
	    BgAreaActionEnableControlPlugin areaActionEnableControlPlugin) {
	this.enableControlPlugin = areaActionEnableControlPlugin;
    }

    private BgCardsSideAction(BgAreaLabel area, Side side,
	    List<Integer> cardIndexes) {
	setArea(area);
	setSide(side);
	getCardIndexes().addAll(cardIndexes);
	switch (side) {
	case FACE:
	    this.putValue(NAME, "face");
	    break;
	case DOWN:
	    this.putValue(NAME, "down");
	    break;
	default:
	    throw new RuntimeException("Program error");
	}
	this.putValue(NAME, side.name());
    }

    public static BgCardsSideAction pointInstance(BgAreaLabel area, Side side,
	    Point p) {
	List<Rectangle> cr = area.getAreaLayout().getCardRectangles();
	int cardIndex = -1;
	for (int idx = 0, size = cr.size(); idx < size; idx++) {
	    if (cr.get(idx).contains(p.getX(), p.getY())) {
		cardIndex = idx;
	    }
	}
	List<Integer> list = new ArrayList<Integer>();
	list.add(cardIndex);

	return new BgCardsSideAction(area, side, list);
    }

    public static BgCardsSideAction newInstance(BgAreaLabel area, Side side,
	    List<Integer> targetIndexes) {
	return new BgCardsSideAction(area, side, targetIndexes);
    }

    @Override
    public void actionPerformed(ActionEvent actionevent) {

	if (getCardIndexes().contains(-1)) {
	    return;
	}

	for (int cardIndex : getCardIndexes()) {
	    getArea().getCards().get(cardIndex).setSide(getSide());
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

	if (getCardIndexes().size() == 0 || getCardIndexes().contains(-1)) {
	    return false;
	}

	List<BgCardItem> cards = getIndexCards();

	boolean isAllSidesAlreadySetted = true;
	for (BgCardItem c : cards) {
	    if (getSide() != c.getSide()) {
		isAllSidesAlreadySetted = false;
		break;
	    }
	}

	if (isAllSidesAlreadySetted) {
	    return false;
	}

	List<BgCardItem> list = new ArrayList<BgCardItem>();
	for (int idx = 0, size = getArea().getCards().size(); idx < size; idx++) {
	    BgCardItem nc = BgCardItem.newInstance(getArea().getCards()
		    .get(idx).getInfo());
	    if (getCardIndexes().contains(idx)) {
		nc.setDirection(getArea().getCards().get(idx).getDirection());
		nc.setSide(getSide());
	    }
	    list.add(nc);
	}

	if (getArea().getAreaLayout().verify(list) != null) {
	    return false;
	}
	return true;
    }

    private List<BgCardItem> getIndexCards() {
	List<BgCardItem> ret = new ArrayList<BgCardItem>();

	for (int idx = 0, size = getCardIndexes().size(); idx < size; idx++) {
	    ret.add(getArea().getCards().get(getCardIndexes().get(idx)));
	}

	return ret;
    }

}
