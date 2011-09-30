/**
 *
 */
package site.com.google.anywaywrite.util.control;

import java.util.Collections;
import java.util.List;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.item.card.BgCardItem;

/**
 * @author kitajima
 * 
 */
public class BgAreaControlUtil {

    public static void shuffle(BgAreaLabel area) {
	if (area == null) {
	    return;
	}
	List<BgCardItem> cards = area.getCards();
	if (cards == null) {
	    return;
	}
	Collections.shuffle(cards);
	area.repaint(0, 0, area.getWidth(), area.getHeight());
    }

    public static void move(BgAreaLabel from, BgAreaLabel to) {
	if (from == null || to == null) {
	    return;
	}
	if (from.getCards() == null || to.getCards() == null) {
	    return;
	}
	if (from.getCards().size() == 0) {
	    throw new RuntimeException(
		    "could not move because the cards of the area are empty.");
	}
	to.addCard(from.removeCardFromTop());
	from.repaint(0, 0, from.getWidth(), from.getHeight());
	to.repaint(0, 0, to.getWidth(), to.getHeight());
    }

    public static void moveFromBottom(int idx, BgAreaLabel from, BgAreaLabel to) {
	if (from == null || to == null) {
	    return;
	}
	if (from.getCards() == null || to.getCards() == null) {
	    return;
	}
	if (from.getCards().size() == 0) {
	    throw new RuntimeException(
		    "could not move because the cards of the area are empty.");
	}
	if (idx >= from.getCards().size()) {
	    throw new IllegalArgumentException(
		    "The index should be less than site.com.google.anywaywrite.card size ["
			    + from.getCards().size() + "], but was " + idx);
	}

	to.addCard(from.removeCardFromBottom(idx));

	from.repaint(0, 0, from.getWidth(), from.getHeight());
	to.repaint(0, 0, to.getWidth(), to.getHeight());
    }

    public static void moveFromBottomTo(int fromIdx, BgAreaLabel from,
	    int toIdx, BgAreaLabel to) {
	if (from == null || to == null) {
	    return;
	}
	if (from.getCards() == null || to.getCards() == null) {
	    return;
	}
	if (from.getCards().size() == 0) {
	    throw new RuntimeException(
		    "could not move because the cards of the area are empty.");
	}
	if (fromIdx >= from.getCards().size()) {
	    throw new IllegalArgumentException(
		    "The index should be less than card size ["
			    + from.getCards().size() + "], but was " + fromIdx);
	}

	if (toIdx > to.getCards().size()) {
	    throw new IllegalArgumentException(
		    "The to index should be less than or equal to card size ["
			    + to.getCards().size() + "], but was " + toIdx);
	}

	to.addCard(toIdx, from.removeCardFromBottom(fromIdx));

	// from.paintImmediately(0, 0, from.getWidth(), from.getHeight());
	// to.paintImmediately(0, 0, to.getWidth(), to.getHeight());

	from.repaint(0, 0, from.getWidth(), from.getHeight());
	to.repaint(0, 0, to.getWidth(), to.getHeight());
    }

    public static void removeFromBottom(int idx, BgAreaLabel area) {
	if (area == null) {
	    return;
	}
	if (area.getCards() == null) {
	    return;
	}
	if (area.getCards().size() == 0) {
	    throw new RuntimeException(
		    "could not move because the cards of the area are empty.");
	}
	if (idx >= area.getCards().size()) {
	    throw new IllegalArgumentException(
		    "The index should be less than site.com.google.anywaywrite.card size ["
			    + area.getCards().size() + "], but was " + idx);
	}

	area.removeCardFromBottom(idx);
	area.repaint(0, 0, area.getWidth(), area.getHeight());
    }

    public static void moveAll(BgAreaLabel from, BgAreaLabel to) {
	if (from == null || to == null) {
	    return;
	}
	if (from.getCards() == null || to.getCards() == null) {
	    return;
	}
	while (from.getCards().size() > 0) {
	    to.addCard(from.removeCardFromBottom(0));
	}
	from.repaint(0, 0, from.getWidth(), from.getHeight());
	to.repaint(0, 0, to.getWidth(), to.getHeight());
    }

    public static void removeAll(BgAreaLabel area) {
	if (area == null) {
	    return;
	}
	if (area.getCards() == null) {
	    return;
	}
	while (area.getCards().size() > 0) {
	    area.removeCardFromBottom(0);
	}
	area.repaint(0, 0, area.getWidth(), area.getHeight());
    }
}
