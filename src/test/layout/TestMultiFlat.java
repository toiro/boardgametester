/**
 *
 */
package test.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.layout.BgAreaLayout;
import site.com.google.anywaywrite.component.layout.BgMultiFlatLayout;
import site.com.google.anywaywrite.component.layout.BgMultiFlatLayout.BaseEdge;
import site.com.google.anywaywrite.component.layout.BgMultiFlatLayout.FlatDirection;
import site.com.google.anywaywrite.item.card.BgCardInfo;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;
import boardgame.weissSchwarz.BgWeissSchwarzDeck;

/**
 * @author y-kitajima
 * 
 */
public class TestMultiFlat {
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(250, 250));

		BgAreaLabel area = BgAreaLabel.newInstance(createCards(),
			createAreaLayout());
		area.setAreaName("MultiFlat");
		area.setPreferredSize(new Dimension(200, 200));

		f.setLayout(new BorderLayout());
		f.getContentPane().add(area, BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);
	    }
	});
    }

    public static List<BgCardItem> createCards() {
	// java.util.List<BgCardItem> cards = new
	// java.util.ArrayList<BgCardItem>();
	// for (int idx = 13; idx < 20; idx++) {
	// BgCardItem c =
	// BgCardItem.newInstance(BgTrump.newInstance().getCards()
	// .get(idx));
	// c.setDirection(Direction.RIGHT);
	// cards.add(c);
	// }
	//
	// return cards;

	List<BgCardInfo> cards = BgWeissSchwarzDeck.newInstance().getCards();
	ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
	for (int idx = 0; idx < 42; idx++) {
	    BgCardItem item = BgCardItem.newInstance(cards.get(idx));
	    item.setDirection(Direction.UP);
	    item.setSide(Side.FACE);
	    items.add(item);
	}

	return items;
    }

    public static BgAreaLayout createAreaLayout() {
	BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
		FlatDirection.LEFT_TO_RIGHT, BaseEdge.HEIGHT);
	layout.setMargin(new Insets(10, 20, 10, 20));
	layout.setGapPercentage(50);
	return layout;
    }
}
