package boardgame.cribagge;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import site.com.google.anywaywrite.component.gui.BgAreaBoxInternalFrame;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgAreaLabelPane;
import site.com.google.anywaywrite.component.layout.BgLayoutBase;
import site.com.google.anywaywrite.component.layout.BgLayoutPosition;
import site.com.google.anywaywrite.component.layout.BgMultiFlatLayout;
import site.com.google.anywaywrite.component.layout.BgMultiPileLayout;
import site.com.google.anywaywrite.component.layout.BgMultiFlatLayout.BaseEdge;
import site.com.google.anywaywrite.component.layout.BgMultiFlatLayout.FlatDirection;
import site.com.google.anywaywrite.item.card.BgCardInfo;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;
import site.com.google.anywaywrite.macro.BgMacro;
import site.com.google.anywaywrite.macro.BgMacro.ActionName;
import site.com.google.anywaywrite.macro.BgMacro.BgMacroParameter;
import site.com.google.anywaywrite.macro.BgMacro.BgMacroParameter.CountFrom;
import site.com.google.anywaywrite.manage.BgGameAreaManager;

public class BgCribbageBoard extends JFrame {
    private static final long serialVersionUID = 1L;

    public BgCribbageBoard() {
	super();
	setTitle("Cribbage");
	setSize(800, 600);
	setMaximumSize(new Dimension(800, 600));
	setMinimumSize(new Dimension(800, 600));
	setResizable(false);
	setVisible(true);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	getContentPane().add(getDesktop(), BorderLayout.CENTER);

	BgGameAreaManager.Instance.setBox(getBoxFrame());
	BgGameAreaManager.Instance.setBoard(getDesktop());
	BgMacro.newInstance(
		1,
		ActionName.Move,
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOX,
			"deck1", CountFrom.Last, 3, 6),
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOARD,
			"p1_hand", CountFrom.First, 0)).execute();

	BgMacro.newInstance(
		2,
		ActionName.Move,
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOX,
			"deck1", CountFrom.First, 2),
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOARD,
			"p1_hand", CountFrom.Last, 1)).execute();

	// BgMacro.newInstance(
	// 1,
	// ActionName.Detail,
	// BgMacroParameter.newInstance(BgGameAreaManager.Place.BOARD,
	// "p1_hand", CountFrom.First, 0), null).execute();

	BgMacro.newInstance(
		1,
		ActionName.Move,
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOARD,
			"p1_hand", CountFrom.First, 2),
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOARD,
			"p2_hand", CountFrom.First, 0)).execute();

	BgMacro.newInstance(
		1,
		ActionName.MoveAll,
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOARD,
			"p1_hand", CountFrom.First, 1),
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOARD,
			"p2_hand", CountFrom.Last, 0)).execute();

	// getBoxFrame().setVisible(true);
    }

    private BgAreaLabelPane desktop;
    private BgAreaLabel _deck;
    private BgAreaLabel _crib;
    private BgAreaLabel _p1_hand;
    private BgAreaLabel _p2_hand;
    private BgAreaLabel _p1_play;
    private BgAreaLabel _p2_play;

    private BgAreaLabelPane getDesktop() {
	if (desktop == null) {
	    desktop = BgAreaLabelPane
		    .newInstance(
			    "C:\\BOMPro\\workspace\\board\\resource\\ws\\playmat\\mat_dummy.png",
			    BgLayoutPosition.NORTH_EAST, 1.0D);
	    desktop.setBackground(Color.GREEN);
	    desktop.setLayout(null);

	    desktop.add(get_deck());
	    desktop.add(get_p1_hand());
	    desktop.add(get_p2_hand());
	    desktop.add(get_p1_play());
	    desktop.add(get_p2_play());
	    desktop.add(get_crib());

	    registerArea();

	    desktop.add(getBoxFrame());
	}
	return desktop;
    }

    private BgAreaBoxInternalFrame boxFrame;

    private BgAreaBoxInternalFrame getBoxFrame() {
	if (boxFrame == null) {
	    List<BgCardInfo> cards = BgTrump.newInstance().getCards();
	    ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
	    for (BgCardInfo info : cards) {
		BgCardItem item = BgCardItem.newInstance(info);
		item.setDirection(Direction.UP);
		item.setSide(Side.DOWN);
		items.add(item);
	    }

	    boxFrame = BgAreaBoxInternalFrame.newInstance(desktop,
		    new BgAreaBoxInternalFrame.AreaInfo("deck1", items));

	    boxFrame.setBounds(100, 100, 400, 200);
	}
	return boxFrame;
    }

    private void registerArea() {
	desktop.registerArea("deck", get_deck());
	desktop.registerArea("p1_hand", get_p1_hand());
	desktop.registerArea("p2_hand", get_p2_hand());
	desktop.registerArea("p1_play", get_p1_play());
	desktop.registerArea("p2_play", get_p2_play());
	desktop.registerArea("crib", get_crib());
    }

    private BgAreaLabel get_deck() {
	if (_deck == null) {
	    // List<BgCardInfo> cards = BgTrump.newInstance().getCards();
	    // ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
	    // for (BgCardInfo info : cards) {
	    // BgCardItem item = BgCardItem.newInstance(info);
	    // item.setDirection(Direction.UP);
	    // item.setSide(Side.DOWN);
	    // items.add(item);
	    // }

	    BgMultiPileLayout layout = BgMultiPileLayout.newInstance(
		    BgLayoutBase.BOTH, 52, 10, 10);
	    _deck = BgAreaLabel
		    .newInstance(new ArrayList<BgCardItem>(), layout);
	    _deck.setBounds(new Rectangle(10, 180, 117, 162));
	    _deck.getAreaLayout().setInitialDirection(Direction.UP);
	    _deck.getAreaLayout().setInitialSide(Side.DOWN);
	}
	return _deck;
    }

    private BgAreaLabel get_p1_hand() {
	if (_p1_hand == null) {
	    BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
		    FlatDirection.LEFT_TO_RIGHT, BaseEdge.HEIGHT);
	    layout.setGapPercentage(102);
	    layout.setMargin(new Insets(10, 20, 10, 10));

	    ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
	    _p1_hand = BgAreaLabel.newInstance(items, layout);
	    _p1_hand.setBounds(150, 500, 650, 172);
	    _p1_hand.getAreaLayout().setInitialDirection(Direction.UP);
	    _p1_hand.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _p1_hand;
    }

    private BgAreaLabel get_p2_hand() {
	if (_p2_hand == null) {
	    BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
		    FlatDirection.LEFT_TO_RIGHT, BaseEdge.HEIGHT);
	    layout.setGapPercentage(102);
	    layout.setMargin(new Insets(10, 20, 10, 10));

	    ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
	    _p2_hand = BgAreaLabel.newInstance(items, layout);
	    _p2_hand.setBounds(150, -100, 650, 172);
	    _p2_hand.getAreaLayout().setInitialDirection(Direction.UP);
	    _p2_hand.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _p2_hand;
    }

    private BgAreaLabel get_p1_play() {
	if (_p1_play == null) {
	    BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
		    FlatDirection.LEFT_TO_RIGHT, BaseEdge.HEIGHT);
	    layout.setGapPercentage(102);
	    layout.setMargin(new Insets(10, 20, 10, 10));

	    ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
	    _p1_play = BgAreaLabel.newInstance(items, layout);
	    _p1_play.setBounds(150, 300, 440, 172);
	    _p1_play.getAreaLayout().setInitialDirection(Direction.UP);
	    _p1_play.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _p1_play;
    }

    private BgAreaLabel get_p2_play() {
	if (_p2_play == null) {
	    BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
		    FlatDirection.LEFT_TO_RIGHT, BaseEdge.HEIGHT);
	    layout.setGapPercentage(102);
	    layout.setMargin(new Insets(10, 20, 10, 10));

	    ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
	    _p2_play = BgAreaLabel.newInstance(items, layout);
	    _p2_play.setBounds(150, 128, 440, 172);
	    _p2_play.getAreaLayout().setInitialDirection(Direction.UP);
	    _p2_play.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _p2_play;
    }

    private BgAreaLabel get_crib() {
	if (_crib == null) {
	    _crib = BgAreaLabel
		    .newInstance(new ArrayList<BgCardItem>(), BgMultiPileLayout
			    .newInstance(BgLayoutBase.BOTH, 4, 60, 40));
	    _crib.setBounds(new Rectangle(600, 200, 180, 220));
	    _crib.getAreaLayout().setInitialDirection(Direction.UP);
	    _crib.getAreaLayout().setInitialSide(Side.DOWN);
	}
	return _crib;
    }

}
