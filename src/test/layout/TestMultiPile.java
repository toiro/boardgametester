/**
 *
 */
package test.layout;


import java.awt.Dimension;
import java.awt.Insets;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.layout.BgAreaLayout;
import site.com.google.anywaywrite.component.layout.BgLayoutBase;
import site.com.google.anywaywrite.component.layout.BgMultiPileLayout;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;

import boardgame.cribagge.BgTrump;

/**
 * @author y-kitajima
 * 
 */
public class TestMultiPile {
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(250, 250));

		BgAreaLabel area = BgAreaLabel.newInstance(createCards(),
			createAreaLayout());
		area.setAreaName("MultiPile");
		// area.setPreferredSize(new Dimension(200, 200));

		area.setBounds(10, 10, 130, 180);
		f.add(area);

		// f.setLayout(new BorderLayout());
		// f.getContentPane().add(area, BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);
	    }
	});
    }

    public static List<BgCardItem> createCards() {
	java.util.List<BgCardItem> cards = new java.util.ArrayList<BgCardItem>();
	for (int idx = 13; idx < 20; idx++) {
	    BgCardItem c = BgCardItem.newInstance(BgTrump.newInstance().getCards()
		    .get(idx));
	    c.setDirection(Direction.UP);
	    c.setSide(Side.DOWN);
	    cards.add(c);
	}
	return cards;

	// List<BgCardInfo> cards = BgWeissSchwarzDeck.newInstance().getCards();
	// ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
	// for (BgCardInfo info : cards) {
	// BgCardItem site.com.google.anywaywrite.item = BgCardItem.newInstance(info);
	// site.com.google.anywaywrite.item.setDirection(Direction.UP);
	// site.com.google.anywaywrite.item.setSide(Side.FACE);
	// items.add(site.com.google.anywaywrite.item);
	// }
	//
	// return items;
    }

    public static BgAreaLayout createAreaLayout() {
	BgMultiPileLayout layout = BgMultiPileLayout.newInstance(BgLayoutBase.BOTH,
		10, 10, 10);
	layout.setMargin(new Insets(10, 20, 10, 20));
	return layout;
    }
}
