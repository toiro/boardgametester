/**
 *
 */
package test.layout;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.layout.BgAreaLayout;
import site.com.google.anywaywrite.component.layout.BgLayoutBase;
import site.com.google.anywaywrite.component.layout.BgMultiPileLayout;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;

import boardgame.weissSchwarz.BgWeissSchwarzDeck;

/**
 * @author y-kitajima
 * 
 */
public class TestFixMultiPile {
    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("���@�C�X�V���o���c");
		f.setSize(1100, 800);
		f.setMaximumSize(new Dimension(1100, 800));
		f.setMinimumSize(new Dimension(1100, 800));

		BgAreaLabel area = BgAreaLabel.newInstance(createCards(),
			createAreaLayout());
		area.setAreaName("MultiPile");
		// area.setPreferredSize(new Dimension(200, 200));

		// area.setBounds(10, 10, 130, 180);
		// area.setBounds(new Rectangle(943, 154, 130, 180));
		// area.setPreferredSize(new Dimension(130, 180));
		// area.setBounds(943, 154, 130, 180);

		// f.setLayout(new BorderLayout());
		// f.getContentPane().add(area, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(130, 180));
		panel.setBounds(943, 154, 130, 180);
		panel.setLayout(new BorderLayout());
		panel.add(area, BorderLayout.CENTER);

		f.setLayout(new BorderLayout());
		f.add(panel, BorderLayout.EAST);
		f.pack();
		f.setVisible(true);
	    }
	});
    }

    public static List<BgCardItem> createCards() {
	java.util.List<BgCardItem> cards = new java.util.ArrayList<BgCardItem>();
	for (int idx = 0; idx < 20; idx++) {
	    BgCardItem c = BgCardItem.newInstance(BgWeissSchwarzDeck.newInstance()
		    .getCards().get(idx));
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
		42, 25, 25);
	layout.setMargin(new Insets(0, 0, 0, 0));
	return layout;
    }
}
