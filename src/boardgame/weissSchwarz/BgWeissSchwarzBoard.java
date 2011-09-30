/**
 *
 */
package boardgame.weissSchwarz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import site.com.google.anywaywrite.component.gui.BgAreaBoxInternalFrame;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgAreaLabelPane;
import site.com.google.anywaywrite.component.layout.BgLayoutBase;
import site.com.google.anywaywrite.component.layout.BgLayoutPosition;
import site.com.google.anywaywrite.component.layout.BgMultiFlatLayout;
import site.com.google.anywaywrite.component.layout.BgMultiPileLayout;
import site.com.google.anywaywrite.component.layout.BgSingleTappableLayout;
import site.com.google.anywaywrite.component.layout.BgMultiFlatLayout.BaseEdge;
import site.com.google.anywaywrite.component.layout.BgMultiFlatLayout.FlatDirection;
import site.com.google.anywaywrite.item.card.BgCardInfo;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;
import site.com.google.anywaywrite.macro.BgMacro;
import site.com.google.anywaywrite.macro.BgMacroSet;
import site.com.google.anywaywrite.macro.BgMacro.ActionName;
import site.com.google.anywaywrite.macro.BgMacro.BgMacroParameter;
import site.com.google.anywaywrite.macro.BgMacro.BgMacroParameter.CountFrom;
import site.com.google.anywaywrite.manage.BgGameAreaManager;

/**
 * @author kitajima
 * 
 */
public class BgWeissSchwarzBoard extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String MAT_IMAGE_BASE = "C:\\BOMPro\\workspace\\board\\resource\\ws\\playmat\\";

    public BgWeissSchwarzBoard() {
	super();

	initializeGui();

	JMenuBar mbar = new JMenuBar();
	mbar.setPreferredSize(new Dimension(1100, 20));

	JMenu macro = new JMenu("macro");
	macro.setMnemonic(KeyEvent.VK_M);

	BgGameAreaManager.Instance.setBox(getBoxFrame());
	BgGameAreaManager.Instance.setBoard(getDesktop());

	// BgGameAreaManager.Instance.installMacroSet(BgMacroSet.newInstance(
	// "gameStart", makeGameStartMacros()));

	macro.add(makeItemGameStart());
	macro.add(makeItemStand_start());
	macro.add(makeItemDraw());

	macro.add(makeItemClockDraw());
	// main
	// climax
	// attack
	// end
	// wait

	// attack
	// trigger
	// counter
	// damage
	// battle
	// anchole

	// reflesh(山札切れ処理)

	mbar.add(macro);
	setJMenuBar(mbar);
    }

    private JMenuBar menuBar;

    @Override
    public JMenuBar getJMenuBar() {
	if (menuBar == null) {
	    menuBar = new JMenuBar();
	}
	return menuBar;
    }

    private void initializeGui() {
	setTitle("WeissSchwarz");
	setSize(1100, 800);
	setMaximumSize(new Dimension(1100, 830));
	setMinimumSize(new Dimension(1100, 830));
	setResizable(false);
	setVisible(true);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	getContentPane().add(getDesktop(), BorderLayout.CENTER);
    }

    private BgAreaLabelPane desktop;

    private BgAreaLabelPane getDesktop() {
	if (desktop == null) {
	    // desktop = BgAreaLabelPane.tempInstance(
	    // MAT_IMAGE_BASE + "mat_dummy.png", BgLayoutPosition.NORTH_EAST,
	    // 2.5D);
	    desktop = BgAreaLabelPane.newInstance(MAT_IMAGE_BASE
		    + "mat_nas.jpg", BgLayoutPosition.NORTH_EAST, 2.5D);
	    desktop.setBackground(Color.BLACK);

	    desktop.setLayout(null);

	    // desktop.add(get_temp());
	    desktop.add(get_hand());
	    desktop.add(get_waitingRoom());
	    desktop.add(get_clock());
	    desktop.add(get_stock());
	    desktop.add(get_climax());
	    desktop.add(get_memory());
	    desktop.add(get_level());
	    desktop.add(get_stage1());
	    desktop.add(get_stage2());
	    desktop.add(get_stage3());
	    desktop.add(get_stage4());
	    desktop.add(get_stage5());
	    desktop.add(get_deck());

	    registerTempArea();
	    registerArea();

	    desktop.add(getBoxFrame());
	}
	return desktop;
    }

    private BgAreaBoxInternalFrame boxFrame;

    private BgAreaBoxInternalFrame getBoxFrame() {
	if (boxFrame == null) {
	    List<BgCardInfo> cards = BgWeissSchwarzDeck.newInstance()
		    .getCards();
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

    private void registerTempArea() {
	desktop.registerTempArea("temp", get_temp(), new Rectangle(500, 200,
		300, 220));
    }

    private void registerArea() {
	// desktop.registerArea("temp", get_temp());
	desktop.registerArea("hand", get_hand());
	desktop.registerArea("waitingRoom", get_waitingRoom());
	desktop.registerArea("clock", get_clock());
	desktop.registerArea("stock", get_stock());
	desktop.registerArea("climax", get_climax());
	desktop.registerArea("memory", get_memory());
	desktop.registerArea("level", get_level());
	desktop.registerArea("frontLeft", get_stage1());
	desktop.registerArea("frontCenter", get_stage2());
	desktop.registerArea("frontRight", get_stage3());
	desktop.registerArea("backLeft", get_stage4());
	desktop.registerArea("backRight", get_stage5());
	desktop.registerArea("deck", get_deck());
    }

    private BgAreaLabel _memory;

    private BgAreaLabel get_memory() {
	if (_memory == null) {
	    BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
		    FlatDirection.UP_TO_DOWN, BaseEdge.HEIGHT);
	    layout.setGapPercentage(0);
	    _memory = BgAreaLabel.newInstance(new ArrayList<BgCardItem>(),
		    layout);

	    _memory.getAreaLayout().setInitialDirection(Direction.RIGHT);
	    _memory.getAreaLayout().setInitialSide(Side.FACE);
	    _memory.setBounds(new Rectangle(891, 14, 182, 130));
	    // _memory.setBounds(new Rectangle(893, 14, 180, 130));
	}
	return _memory;
    }

    private BgAreaLabel _deck;

    private BgAreaLabel get_deck() {
	if (_deck == null) {
	    List<BgCardInfo> cards = BgWeissSchwarzDeck.newInstance()
		    .getCards();
	    ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
	    for (BgCardInfo info : cards) {
		BgCardItem item = BgCardItem.newInstance(info);
		item.setDirection(Direction.UP);
		item.setSide(Side.DOWN);
		items.add(item);
	    }
	    BgMultiPileLayout layout = BgMultiPileLayout.newInstance(
		    BgLayoutBase.BOTH, 50, 10, 10);

	    // _deck = BgAreaLabel.newInstance(items, layout);
	    _deck = BgAreaLabel
		    .newInstance(new ArrayList<BgCardItem>(), layout);
	    // _deck.setBounds(new Rectangle(930, 134, 143, 200));
	    _deck.setBounds(new Rectangle(943, 154, 130, 180));
	    _deck.getAreaLayout().setInitialDirection(Direction.UP);
	    _deck.getAreaLayout().setInitialSide(Side.DOWN);
	}
	return _deck;
    }

    private BgAreaLabel _waitingRoom;

    private BgAreaLabel get_waitingRoom() {
	if (_waitingRoom == null) {
	    _waitingRoom = BgAreaLabel.newInstance(new ArrayList<BgCardItem>(),
		    BgMultiPileLayout
			    .newInstance(BgLayoutBase.BOTH, 50, 10, 10));
	    // _waitingRoom.setBounds(new Rectangle(930, 324, 143, 200));
	    _waitingRoom.setBounds(new Rectangle(943, 344, 130, 180));
	    _waitingRoom.getAreaLayout().setInitialDirection(Direction.UP);
	    _waitingRoom.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _waitingRoom;
    }

    private BgAreaLabel _climax;

    private BgAreaLabel get_climax() {
	if (_climax == null) {
	    BgSingleTappableLayout layout = BgSingleTappableLayout
		    .newInstance(BgLayoutPosition.SOUTH);

	    _climax = BgAreaLabel.newInstance(new ArrayList<BgCardItem>(),
		    layout);
	    _climax.setBounds(new Rectangle(232, 170, 182, 182));
	    // _climax.setBounds(new Rectangle(234, 172, 180, 180));
	    // _climax.setBounds(new Rectangle(234, 222, 180, 130)); ぴったりの時の大きさ
	    _climax.getAreaLayout().setInitialDirection(Direction.LEFT);
	    _climax.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _climax;
    }

    private BgAreaLabel _level;

    private BgAreaLabel get_level() {
	if (_level == null) {
	    BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
		    FlatDirection.DOWN_TO_UP, BaseEdge.HEIGHT);
	    layout.setGapPercentage(25);
	    _level = BgAreaLabel.newInstance(new ArrayList<BgCardItem>(),
		    layout);
	    _level.setBounds(new Rectangle(232, 362, 182, 224));
	    // _level.setBounds(new Rectangle(234, 362, 180, 224));
	    _level.getAreaLayout().setInitialDirection(Direction.RIGHT);
	    _level.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _level;
    }

    private BgAreaLabel _clock;

    private BgAreaLabel get_clock() {
	if (_clock == null) {

	    BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
		    FlatDirection.LEFT_TO_RIGHT, BaseEdge.HEIGHT);
	    layout.setGapPercentage(35);
	    ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();

	    _clock = BgAreaLabel.newInstance(items, layout);
	    _clock.setBounds(424, 404, 340, 182);
	    // _clock.setBounds(424, 416, 340, 170);
	    _clock.getAreaLayout().setInitialDirection(Direction.UP);
	    _clock.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _clock;
    }

    private BgAreaLabel _stock;

    private BgAreaLabel get_stock() {
	if (_stock == null) {
	    BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
		    FlatDirection.UP_TO_DOWN, BaseEdge.HEIGHT);
	    layout.setGapPercentage(25);
	    _stock = BgAreaLabel.newInstance(new ArrayList<BgCardItem>(),
		    layout);
	    _stock.setBounds(32, 222, 182, 364);
	    // _stock.setBounds(34, 222, 180, 364);
	    // _stock.setBounds(92, 222, 122, 364);//全て画面内に収める場合
	    _stock.getAreaLayout().setInitialDirection(Direction.RIGHT);
	    _stock.getAreaLayout().setInitialSide(Side.DOWN);
	}
	return _stock;
    }

    private BgAreaLabel _stage1;

    private BgAreaLabel get_stage1() {
	if (_stage1 == null) {
	    _stage1 = BgAreaLabel.newInstance(new ArrayList<BgCardItem>(),
		    BgSingleTappableLayout.newInstance(BgLayoutPosition.EAST));
	    _stage1.setBounds(321, 14, 182, 182);
	    // _stage1.setBounds(324, 14, 180, 180);
	    // _stage1.setBounds(324, 14, 180, 180);
	    _stage1.getAreaLayout().setInitialDirection(Direction.UP);
	    _stage1.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _stage1;
    }

    private BgAreaLabel _stage2;

    private BgAreaLabel get_stage2() {
	if (_stage2 == null) {
	    _stage2 = BgAreaLabel
		    .newInstance(new ArrayList<BgCardItem>(),
			    BgSingleTappableLayout
				    .newInstance(BgLayoutPosition.CENTER));
	    _stage2.setBounds(503, 14, 182, 182);
	    // _stage2.setBounds(504, 14, 180, 180);
	    _stage2.getAreaLayout().setInitialDirection(Direction.UP);
	    _stage2.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _stage2;
    }

    private BgAreaLabel _stage3;

    private BgAreaLabel get_stage3() {
	if (_stage3 == null) {
	    _stage3 = BgAreaLabel.newInstance(new ArrayList<BgCardItem>(),
		    BgSingleTappableLayout.newInstance(BgLayoutPosition.WEST));
	    _stage3.setBounds(685, 14, 182, 182);
	    // _stage3.setBounds(684, 14, 180, 180);
	    _stage3.getAreaLayout().setInitialDirection(Direction.UP);
	    _stage3.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _stage3;
    }

    private BgAreaLabel _stage4;

    private BgAreaLabel get_stage4() {
	if (_stage4 == null) {
	    _stage4 = BgAreaLabel
		    .newInstance(new ArrayList<BgCardItem>(),
			    BgSingleTappableLayout
				    .newInstance(BgLayoutPosition.CENTER));
	    _stage4.setBounds(412, 224, 182, 182);
	    // _stage4.setBounds(414, 222, 180, 180);
	    _stage4.getAreaLayout().setInitialDirection(Direction.UP);
	    _stage4.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _stage4;
    }

    private BgAreaLabel _stage5;

    private BgAreaLabel get_stage5() {
	if (_stage5 == null) {
	    _stage5 = BgAreaLabel
		    .newInstance(new ArrayList<BgCardItem>(),
			    BgSingleTappableLayout
				    .newInstance(BgLayoutPosition.CENTER));
	    _stage5.setBounds(594, 224, 182, 182);
	    // _stage5.setBounds(594, 222, 180, 180);
	    _stage5.getAreaLayout().setInitialDirection(Direction.UP);
	    _stage5.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _stage5;
    }

    private BgAreaLabel _hand;

    private BgAreaLabel get_hand() {
	if (_hand == null) {
	    BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
		    FlatDirection.LEFT_TO_RIGHT, BaseEdge.HEIGHT);
	    layout.setGapPercentage(102);
	    layout.setMargin(new Insets(9, 20, 9, 10));

	    ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
	    _hand = BgAreaLabel.newInstance(items, layout);
	    _hand.setBounds(0, 600, 1100, 182);
	    _hand.getAreaLayout().setInitialDirection(Direction.UP);
	    _hand.getAreaLayout().setInitialSide(Side.FACE);
	}
	return _hand;
    }

    // private JDialog tempDialog;
    //
    // private JDialog getTempDialog() {
    // if (tempDialog == null) {
    // tempDialog = new JDialog(this);
    // tempDialog.setResizable(true);
    // tempDialog.setBounds(500, 200, 300, 210);
    // tempDialog.setResizable(false);
    // tempDialog.setModalityType(ModalityType.MODELESS);
    // tempDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    // JScrollPane scp = new JScrollPane();
    // scp.setViewportView(get_temp());
    // tempDialog.getContentPane().add(scp, BorderLayout.CENTER);
    // }
    // return tempDialog;
    // }

    private BgAreaLabel _temp;

    private BgAreaLabel get_temp() {
	if (_temp == null) {
	    BgMultiFlatLayout layout = BgMultiFlatLayout.newInstance(
		    FlatDirection.LEFT_TO_RIGHT, BaseEdge.HEIGHT);
	    layout.setGapPercentage(50);
	    layout.setMargin(new Insets(0, 10, 0, 10));

	    ArrayList<BgCardItem> items = new ArrayList<BgCardItem>();
	    _temp = BgAreaLabel.newInstance(items, layout);
	    _temp.setBounds(200, 200, 600, 200);
	    _temp.getAreaLayout().setInitialDirection(Direction.UP);
	    _temp.getAreaLayout().setInitialSide(Side.FACE);

	    _temp.setPreferredSize(new Dimension(5000, 160));
	    // _temp.setMaximumSize(new Dimension(5000, 160));
	    // _temp.setMinimumSize(new Dimension(5000, 160));
	    _temp.setBackground(Color.BLACK);
	}
	return _temp;
    }

    // private class TempThread implements Runnable {
    // @Override
    // public void run() {
    // try {
    // while (true) {
    // if (BgWeissSchwarzBoard.this.get_temp().getCards().size() > 0) {
    // if (!BgWeissSchwarzBoard.this.getTempDialog()
    // .isVisible()) {
    // SwingUtilities.invokeAndWait(new Runnable() {
    // @Override
    // public void run() {
    // BgWeissSchwarzBoard.this.get_temp()
    // .repaint();
    // BgWeissSchwarzBoard.this.getTempDialog()
    // .setVisible(true);
    // }
    // });
    // }
    // } else {
    // if (BgWeissSchwarzBoard.this.getTempDialog()
    // .isVisible()) {
    // SwingUtilities.invokeAndWait(new Runnable() {
    // @Override
    // public void run() {
    // BgWeissSchwarzBoard.this.get_temp()
    // .repaint();
    // BgWeissSchwarzBoard.this.getTempDialog()
    // .setVisible(false);
    // }
    // });
    // }
    // }
    // }
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // } catch (InvocationTargetException e) {
    // e.printStackTrace();
    // }
    // }
    // }

    // //////////////////////////////////////////////////////////
    // Macro
    // //////////////////////////////////////////////////////////
    private JMenuItem makeItemGameStart() {
	BgMacroSet gameStart = BgMacroSet.newInstance("gameStart",
		makeGameStartMacros());
	JMenuItem itemGameStart = new JMenuItem(gameStart.getName());
	itemGameStart.setAction(gameStart);
	return itemGameStart;
    }

    private List<BgMacro> makeGameStartMacros() {
	List<BgMacro> macros = new ArrayList<BgMacro>();

	// ボックスからデッキに全て移動
	macros.add(BgMacro.newInstance(1, ActionName.MoveAll, BgMacroParameter
		.newInstance(BgGameAreaManager.Place.BOX, "deck1",
			CountFrom.First, 1), BgMacroParameter.newInstance(
		BgGameAreaManager.Place.BOARD, "deck", CountFrom.First, 0)));

	// デッキをシャッフル
	macros.add(BgMacro.newInstance(1, ActionName.Shuffle, BgMacroParameter
		.newInstance(BgGameAreaManager.Place.BOARD, "deck",
			CountFrom.First, 1), null));

	// ドロー5
	macros.add(BgMacro.newInstance(1, ActionName.Move, BgMacroParameter
		.newInstance(BgGameAreaManager.Place.BOARD, "deck",
			CountFrom.Last, 1, 2, 3, 4, 5), BgMacroParameter
		.newInstance(BgGameAreaManager.Place.BOARD, "hand",
			CountFrom.First, 0)));

	return macros;
    }

    private JMenuItem makeItemStand_start() {
	BgMacroSet stand_start = BgMacroSet.newInstance("stand (start)",
		makeStand_startMacros());
	JMenuItem itemStand_start = new JMenuItem(stand_start.getName());
	itemStand_start.setAction(stand_start);
	return itemStand_start;
    }

    private List<BgMacro> makeStand_startMacros() {
	List<BgMacro> macros = new ArrayList<BgMacro>();

	// キャラクターのエリアにあるカードを全てDirecion.UPにする。
	macros.add(BgMacro.newInstance(1, ActionName.DirectionUpAll,
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOARD,
			"frontLeft", CountFrom.First, 0), null));
	macros.add(BgMacro.waitInstance(200));
	macros.add(BgMacro.newInstance(1, ActionName.DirectionUpAll,
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOARD,
			"frontCenter", CountFrom.First, 0), null));
	macros.add(BgMacro.waitInstance(200));
	macros.add(BgMacro.newInstance(1, ActionName.DirectionUpAll,
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOARD,
			"frontRight", CountFrom.First, 0), null));
	macros.add(BgMacro.waitInstance(200));
	macros.add(BgMacro.newInstance(1, ActionName.DirectionUpAll,
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOARD,
			"backLeft", CountFrom.First, 0), null));
	macros.add(BgMacro.waitInstance(200));
	macros.add(BgMacro.newInstance(1, ActionName.DirectionUpAll,
		BgMacroParameter.newInstance(BgGameAreaManager.Place.BOARD,
			"backRight", CountFrom.First, 0), null));

	return macros;
    }

    private JMenuItem makeItemDraw() {
	BgMacroSet draw = BgMacroSet.newInstance("draw", makeDrawMacros());
	JMenuItem itemDraw = new JMenuItem(draw.getName());
	itemDraw.setAction(draw);
	return itemDraw;
    }

    private List<BgMacro> makeDrawMacros() {
	List<BgMacro> macros = new ArrayList<BgMacro>();

	macros.add(BgMacro.newInstance(1, ActionName.Move, BgMacroParameter
		.newInstance(BgGameAreaManager.Place.BOARD, "deck",
			CountFrom.Last, 1), BgMacroParameter.newInstance(
		BgGameAreaManager.Place.TEMP, "temp", CountFrom.First, 0)));
	macros.add(BgMacro.waitInstance(1500));
	macros.add(BgMacro.newInstance(1, ActionName.Move, BgMacroParameter
		.newInstance(BgGameAreaManager.Place.TEMP, "temp",
			CountFrom.First, 1), BgMacroParameter.newInstance(
		BgGameAreaManager.Place.BOARD, "hand", CountFrom.Last, 0)));

	return macros;
    }

    private JMenuItem makeItemClockDraw() {
	BgMacroSet clockDraw = BgMacroSet.newInstance("clockDraw",
		makeClockDrawMacros());
	JMenuItem itemClockDraw = new JMenuItem(clockDraw.getName());
	itemClockDraw.setAction(clockDraw);
	return itemClockDraw;
    }

    private List<BgMacro> makeClockDrawMacros() {
	List<BgMacro> macros = new ArrayList<BgMacro>();

	// macros.add(BgMacro.newInstance(1, ActionName.Move, BgMacroParameter
	// .newInstance(BgGameAreaManager.Place.BOARD, "deck",
	// CountFrom.Last, 1), BgMacroParameter.newInstance(
	// BgGameAreaManager.Place.TEMP, "temp", CountFrom.Last, 0)));
	//
	// macros.add(BgMacro.newInstance(1, ActionName.Move, BgMacroParameter
	// .newInstance(BgGameAreaManager.Place.BOARD, "deck",
	// CountFrom.Last, 1), BgMacroParameter.newInstance(
	// BgGameAreaManager.Place.TEMP, "temp", CountFrom.Last, 0)));

	macros.add(BgMacro.newInstance(2, ActionName.Move, BgMacroParameter
		.newInstance(BgGameAreaManager.Place.BOARD, "deck",
			CountFrom.Last, 1), BgMacroParameter.newInstance(
		BgGameAreaManager.Place.TEMP, "temp", CountFrom.First, 0)));
	macros.add(BgMacro.waitInstance(1500));
	macros.add(BgMacro.newInstance(2, ActionName.Move, BgMacroParameter
		.newInstance(BgGameAreaManager.Place.TEMP, "temp",
			CountFrom.First, 1), BgMacroParameter.newInstance(
		BgGameAreaManager.Place.BOARD, "hand", CountFrom.Last, 0)));

	return macros;
    }
}
