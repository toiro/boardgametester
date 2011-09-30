/**
 *
 */
package test.layout;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.layout.BgAreaLayout;
import site.com.google.anywaywrite.component.layout.BgLayoutBase;
import site.com.google.anywaywrite.component.layout.BgLayoutPosition;
import site.com.google.anywaywrite.component.layout.BgSingleSimpleLayout;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;

import boardgame.cribagge.BgTrump;

/**
 * @author y-kitajima
 * 
 */
public class TestSingleSimple {
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(600, 600));

		BgAreaLabel area = BgAreaLabel.newInstance(createCards(),
			createAreaLayout());
		area.setPreferredSize(new Dimension(200, 200));

		f.setLayout(new BorderLayout());
		f.getContentPane().add(area, BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);
	    }
	});
    }

    public static List<BgCardItem> createCards() {
	BgCardItem c = BgCardItem
		.newInstance(BgTrump.newInstance().getCards().get(1));
	java.util.List<BgCardItem> cards = new java.util.ArrayList<BgCardItem>();
	c.setDirection(Direction.UP);
	cards.add(c);
	return cards;
    }

    public static BgAreaLayout createAreaLayout() {
	BgSingleSimpleLayout layout = BgSingleSimpleLayout.newInstance(
		BgLayoutBase.BOTH, BgLayoutPosition.NORTH_EAST);
	layout.setMargin(new Insets(10, 20, 10, 20));
	return layout;
    }
}
