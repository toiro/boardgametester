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
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.layout.BgAreaLayout;
import site.com.google.anywaywrite.component.layout.BgMultiSimpleDisplayLayout;
import site.com.google.anywaywrite.item.card.BgCardInfo;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;

import boardgame.weissSchwarz.BgWeissSchwarzDeck;

/**
 * @author y-kitajima
 * 
 */
public class TestMultiSimpleDisplay {
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(1000, 600));

		BgAreaLabel area = BgAreaLabel.newInstance(createCards(),
			createAreaLayout());
		area.setAreaName("MultiSimpleDisplay");
		area.setPreferredSize(new Dimension(600, 2000));

		f.setLayout(new BorderLayout());
		JScrollPane scp = new JScrollPane();
		scp.setViewportView(area);

		f.getContentPane().add(scp, BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);
	    }
	});
    }

    public static List<BgCardItem> createCards() {
	List<BgCardInfo> cards = BgWeissSchwarzDeck.newInstance().getCards();
	ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
	for (int idx = 0; idx < 50; idx++) {
	    BgCardItem item = BgCardItem.newInstance(cards.get(idx));
	    item.setDirection(Direction.UP);
	    item.setSide(Side.FACE);
	    items.add(item);
	}

	return items;
    }

    public static BgAreaLayout createAreaLayout() {
	BgMultiSimpleDisplayLayout layout = BgMultiSimpleDisplayLayout
		.newInstance(130);
	layout.setMargin(new Insets(10, 20, 10, 20));
	layout.setGapHorizontalPerecentage(120);
	layout.setGapVerticalPercentage(110);
	return layout;
    }
}
