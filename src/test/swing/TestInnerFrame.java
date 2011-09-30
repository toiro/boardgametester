/**
 *
 */
package test.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import site.com.google.anywaywrite.component.gui.BgAreaBoxInternalFrame;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgAreaLabelPane;
import site.com.google.anywaywrite.component.layout.BgAreaLayout;
import site.com.google.anywaywrite.component.layout.BgLayoutPosition;
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
public class TestInnerFrame {

    private static final String MAT_IMAGE_BASE = "C:\\BOMPro\\workspace\\board\\resource\\ws\\playmat\\";

    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(250, 250));

		BgAreaLabelPane desktop = BgAreaLabelPane.newInstance(
			MAT_IMAGE_BASE + "mat_dummy.png",
			BgLayoutPosition.NORTH_EAST, 1.0D);

		BgAreaLabel area = BgAreaLabel.newInstance(createCards(),
			createAreaLayout());
		area.setAreaName("MultiFlat");
		area.setPreferredSize(new Dimension(200, 200));

		desktop.add(area, BorderLayout.CENTER);

		List<BgCardInfo> cards = BgWeissSchwarzDeck.newInstance()
			.getCards();
		ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
		for (BgCardInfo info : cards) {
		    BgCardItem item = BgCardItem.newInstance(info);
		    item.setDirection(Direction.UP);
		    item.setSide(Side.DOWN);
		    items.add(item);
		}

		BgAreaBoxInternalFrame inframe = BgAreaBoxInternalFrame
			.newInstance(desktop,
				new BgAreaBoxInternalFrame.AreaInfo("deck1",
					items));
		inframe.setBounds(10, 10, 200, 200);
		// inframe.registerInitialArea(new
		// BgAreaBoxInternalFrame.AreaInfo(
		// "deck1", items));
		inframe.setVisible(true);
		desktop.add(inframe);

		f.getContentPane().add(desktop, BorderLayout.CENTER);

		f.setLayout(new BorderLayout());
		f.getContentPane().add(desktop);
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
	    item.setDirection(Direction.RIGHT);
	    item.setSide(Side.FACE);
	    items.add(item);
	}

	return items;
    }

    public static BgAreaLayout createAreaLayout() {
	BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
		FlatDirection.UP_TO_DOWN, BaseEdge.HEIGHT);
	layout.setMargin(new Insets(10, 20, 10, 20));
	layout.setGapPercentage(20);
	return layout;
    }
}
