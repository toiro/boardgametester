/**
 *
 */
package site.com.google.anywaywrite.action;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.html.HTMLDocument;

import site.com.google.anywaywrite.component.BgCardPointChangedHook;
import site.com.google.anywaywrite.component.BgCardSelectionChangedHook;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgAreaLabelPane;
import site.com.google.anywaywrite.component.gui.BgTempAreaInternalFrame;
import site.com.google.anywaywrite.component.layout.BgMultiSimpleDisplayLayout;
import site.com.google.anywaywrite.component.menu.BgCreateActionMenu;
import site.com.google.anywaywrite.component.menu.BgCreateDetailAllMoveActionMenu;
import site.com.google.anywaywrite.component.menu.BgCreateDetailSelectedCardMoveActionMenu;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;
import site.com.google.anywaywrite.util.control.BgAreaControlUtil;
import site.com.google.anywaywrite.util.file.BgFileUtil;
import boardgame.weissSchwarz.BgWeissSchwarzDeck;

/**
 * エリアの詳細表示ダイアログを表示します。
 * 
 * @author kitajima
 * 
 */
public class BgAreaDetailAction extends AbstractAction implements BgAreaAction {

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
    private Window owner;
    private BgAreaLabelPane labelPane;
    private BgAreaActionEnableControlPlugin enableControlPlugin;

    private final BgAreaLabel getArea() {
	return area;
    }

    private final void setArea(final BgAreaLabel area) {
	this.area = area;
    }

    private final Window getOwner() {
	return owner;
    }

    private final void setOwner(Window val) {
	this.owner = val;
    }

    private final BgAreaLabelPane getLabelPane() {
	return labelPane;
    }

    private final void setLabelPane(BgAreaLabelPane labelPane) {
	this.labelPane = labelPane;
    }

    private boolean enableMove;

    public void setEnableMove(boolean val) {
	enableMove = val;
    }

    public boolean isEnableMove() {
	return enableMove;
    }

    private final BgTempAreaInternalFrame getFrame() {
	return frame;
    }

    private final void setFrame(BgTempAreaInternalFrame frame) {
	this.frame = frame;
    }

    public final BgAreaActionEnableControlPlugin getEnableControlPlugin() {
	return enableControlPlugin;
    }

    public final void setEnableControlPlugin(
	    BgAreaActionEnableControlPlugin areaActionEnableControlPlugin) {
	this.enableControlPlugin = areaActionEnableControlPlugin;
    }

    private BgAreaDetailAction(BgAreaLabel area, BgAreaLabelPane labelPane,
	    Window owner, boolean enableMove, String actionName) {
	setArea(area);
	setLabelPane(labelPane);
	setOwner(owner);
	this.putValue(NAME, actionName);
	setEnableMove(enableMove);
    }

    private BgTempAreaInternalFrame frame;

    private BgAreaDetailAction(BgTempAreaInternalFrame frame,
	    BgAreaLabelPane labelPane, Window owner, boolean enableMove,
	    String actionName) {
	setFrame(frame);
	setArea(frame.getAreaLabel());
	setLabelPane(labelPane);
	setOwner(owner);
	this.putValue(NAME, actionName);
	setEnableMove(enableMove);
    }

    public static BgAreaDetailAction newInstance(BgAreaLabel area,
	    BgAreaLabelPane labelPane, Window owner) {
	return new BgAreaDetailAction(area, labelPane, owner, true, "detail");
    }

    public static BgAreaDetailAction namedInstance(BgAreaLabel area,
	    BgAreaLabelPane labelPane, Window owner, String actionName) {
	return new BgAreaDetailAction(area, labelPane, owner, true, actionName);
    }

    public static BgAreaDetailAction displayInstance(BgAreaLabel area,
	    BgAreaLabelPane labelPane, Window owner) {
	return new BgAreaDetailAction(area, labelPane, owner, false, "detail");
    }

    public static BgAreaDetailAction mamedDisplayInstance(BgAreaLabel area,
	    BgAreaLabelPane labelPane, Window owner, String actionName) {
	return new BgAreaDetailAction(area, labelPane, owner, false, actionName);
    }

    public static BgAreaDetailAction tempInstance(
	    BgTempAreaInternalFrame frame, BgAreaLabelPane labelPane,
	    Window owner) {
	return new BgAreaDetailAction(frame, labelPane, owner, true, "detail");
    }

    public static BgAreaDetailAction namedTempInstance(
	    BgTempAreaInternalFrame frame, BgAreaLabelPane labelPane,
	    Window owner, String actionName) {
	return new BgAreaDetailAction(frame, labelPane, owner, true, actionName);
    }

    private BgAreaLabel detailArea;

    private BgAreaLabel getDetailArea() {
	if (this.detailArea == null) {
	    BgMultiSimpleDisplayLayout layout = BgMultiSimpleDisplayLayout
		    .newInstance(130);
	    layout.setGapHorizontalPerecentage(120);
	    layout.setGapVerticalPercentage(110);
	    layout.setMargin(new Insets(10, 10, 10, 10));

	    List<BgCardItem> originals = getArea().getCards();
	    List<BgCardItem> cards = new ArrayList<BgCardItem>();
	    for (BgCardItem c : originals) {
		BgCardItem item = BgCardItem.newInstance(c.getInfo());
		item.setDirection(Direction.UP);
		item.setSide(Side.FACE);
		cards.add(item);
	    }
	    this.detailArea = BgAreaLabel.newInstance(cards, layout);
	    this.detailArea.setAreaName("detail");
	    this.detailArea.setPlayBoard(getLabelPane());
	    // this.detailArea.addMouseListener(new DetailAreaMouseListener());
	    this.detailArea
		    .addComponentListener(new DetailAreaComponentLisneter());
	    this.detailArea
		    .setPointChangedHook(new DetailAreaCardPointChangedHook());
	    this.detailArea
		    .setSelectionChangedHook(new DetailAreaCardSelectionChangedHook());
	    this.detailArea.uninstallAllAreaActionMenu();
	    this.detailArea.installAreaActionMenuItem("allMove",
		    BgCreateDetailAllMoveActionMenu.newInstance(getFromArea(),
			    getLabelPane(), getFrame(), getDialog()));

	    BgCreateActionMenu defaultDetail = this.detailArea
		    .getSelectedCardActionMenuMap().get(
			    BgAreaLabel.BgDefaultCardActionMenuMame.detail
				    .getActionName());
	    this.detailArea.uninstallAllSelectedCardActionMenu();
	    this.detailArea.installSelectedCardActionMenuItem("move",
		    BgCreateDetailSelectedCardMoveActionMenu.newInstance(
			    getFromArea(), getLabelPane(), getFrame(),
			    getDialog()));
	    this.detailArea.installSelectedCardActionMenuItem(
		    BgAreaLabel.BgDefaultCardActionMenuMame.detail
			    .getActionName(), defaultDetail);
	}
	return this.detailArea;
    }

    private class DetailAreaComponentLisneter implements ComponentListener {
	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
	    BgAreaDetailAction.this.detailArea
		    .setPreferredSize(new Dimension(
			    BgAreaDetailAction.this.detailArea
				    .getPreferredSize().width,
			    ((BgMultiSimpleDisplayLayout) getDetailArea()
				    .getAreaLayout()).getPreferredHeight()));
	    BgAreaDetailAction.this.detailArea.repaint();
	}

	@Override
	public void componentShown(ComponentEvent e) {
	    BgAreaDetailAction.this.detailArea
		    .setPreferredSize(new Dimension(
			    BgAreaDetailAction.this.detailArea
				    .getPreferredSize().width,
			    ((BgMultiSimpleDisplayLayout) getDetailArea()
				    .getAreaLayout()).getPreferredHeight()));
	    BgAreaDetailAction.this.detailArea.repaint();
	}
    }

    private class DetailAreaMouseListener extends MouseAdapter {
	@Override
	public void mouseClicked(MouseEvent e) {
	    if (!SwingUtilities.isRightMouseButton(e)) {
		return;
	    }

	    if (!isEnableMove()) {
		return;
	    }

	    JPopupMenu menu = new JPopupMenu();
	    menu.add(createMoveMenu(BgAreaDetailAction.this.detailArea, e));
	    menu.add(createAllMoveMenu(BgAreaDetailAction.this.detailArea));
	    menu.show(BgAreaDetailAction.this.detailArea, e.getX(), e.getY());
	}

	/**
	 * このDetailアクションでは、moveという名前だが、実際には、Detail自身からはremoveし、
	 * このDetailアクションの元になっているエリアから移動アクションが呼ばれる
	 */
	private JMenu createMoveMenu(final BgAreaLabel area, final MouseEvent me) {
	    JMenu move = new JMenu("move");
	    Iterator<BgAreaLabel> it = getLabelPane().getAreaSet().iterator();
	    while (it.hasNext()) {
		final BgAreaLabel al = it.next();
		if (getArea().getAreaName().equals(al.getAreaName())
			|| "temp".equals(al.getAreaName())) {
		    continue;
		}

		BgCardsRemoveAction action = BgCardsRemoveAction.pointInstance(
			area, me.getPoint());
		action.putValue(NAME, al.getAreaName());
		action
			.setRemoveActionSuccessHook(new BgRemoveActionSuccessHook() {
			    @Override
			    public void actionDone(BgAreaLabel fromArea,
				    List<BgCardItem> moveCards,
				    List<Integer> moveIndexes) {
				for (int idx = 0, size = moveIndexes.size(); idx < size; idx++) {
				    BgAreaControlUtil
					    .moveFromBottom(moveIndexes
						    .get(idx), getArea(), al);
				    getDialog().setTitle(
					    getArea().getAreaName()
						    + " ["
						    + getArea().getCards()
							    .size() + "]");
				    if (getArea().getCards().size() == 0) {
					getDialog().dispose();
				    }
				    if (getFrame() != null) {
					if (getFromArea().getCards().size() > 0) {
					    getFrame().setVisible(true);
					} else {
					    getFrame().setVisible(false);
					}
				    }
				}
			    }
			});
		JMenuItem item = new JMenuItem(action);
		move.add(item);
	    }
	    return move;
	}

	private JMenu createAllMoveMenu(final BgAreaLabel area) {
	    JMenu allMove = new JMenu("allMove");
	    Iterator<BgAreaLabel> it = getLabelPane().getAreaSet().iterator();
	    while (it.hasNext()) {
		final BgAreaLabel al = it.next();
		if (getArea().getAreaName().equals(al.getAreaName())
			|| "temp".equals(al.getAreaName())) {
		    continue;
		}
		BgAreaRemoveAction action = BgAreaRemoveAction
			.newInstance(area);
		action.putValue(NAME, al.getAreaName());
		action
			.setRemoveActionSuccessHook(new BgRemoveActionSuccessHook() {
			    @Override
			    public void actionDone(BgAreaLabel fromArea,
				    List<BgCardItem> moveCards,
				    List<Integer> moveIndexes) {
				BgAreaControlUtil.moveAll(getArea(), al);
				getDialog().setTitle(
					getArea().getAreaName() + " ["
						+ getArea().getCards().size()
						+ "]");
				getDialog().dispose();
				if (getFrame() != null) {
				    if (getFromArea().getCards().size() > 0) {
					getFrame().setVisible(true);
				    } else {
					getFrame().setVisible(false);
				    }
				}
			    }
			});
		JMenuItem item = new JMenuItem(action);
		allMove.add(item);
	    }
	    return allMove;
	}
    }

    private class DetailAreaCardPointChangedHook implements
	    BgCardPointChangedHook {
	@Override
	public void pointChanged(BgAreaLabel area, int selectedIndex) {
	    if (selectedIndex == -1) {
		// detail.setText(null);
	    } else {
		String path = area.getCards().get(selectedIndex).getInfo()
			.getHtmlPath();
		if (path == null) {
		    getDetailEditor().setText(
			    area.getCards().get(selectedIndex).getInfo()
				    .getFacepath());
		    return;
		}

		List<String> texts = BgFileUtil.readTextList(new File(path),
			"UTF-8");

		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<style type=\"text/css\">\r\n");
		sb.append(CSS_STR);
		sb.append("</style>");
		sb.append("</head>");
		sb.append("<body>");
		for (int idx = 0, size = texts.size(); idx < size; idx++) {
		    if (idx == 162 || (166 <= idx && idx <= 224)) {
			sb.append(texts.get(idx));
		    }
		}
		sb.append("</body></html>");
		getDetailEditor().setText(sb.toString());
	    }
	}
    }

    private class DetailAreaCardSelectionChangedHook implements
	    BgCardSelectionChangedHook {
	@Override
	public void selectionChanged(BgAreaLabel area, int selectedIndex) {
	    if (selectedIndex == -1) {
		// detail.setText(null);
	    } else {
		if (area.getSelectedIndexes().contains(selectedIndex)) {
		    getFromArea().addSelection(selectedIndex);
		} else {
		    getFromArea().removeSelection(selectedIndex);
		}
	    }
	}
    }

    private JEditorPane detailEditor;

    private void setDetailEditor(JEditorPane detail) {
	this.detailEditor = detail;
    }

    private JEditorPane getDetailEditor() {
	return this.detailEditor;
    }

    private JDialog dialog;

    private void setDialog(JDialog dialog) {
	this.dialog = dialog;
    }

    private JDialog getDialog() {
	return this.dialog;
    }

    @Override
    public void actionPerformed(ActionEvent actionevent) {
	setDialog(new JDialog(getOwner()));
	// getDialog().setSize(new Dimension(800, 600));
	getDialog().setBounds(getOwner().getWidth() / 8,
		getOwner().getHeight() / 8, getOwner().getWidth() * 3 / 4,
		getOwner().getHeight() * 3 / 4);
	//
	getDialog().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	getDialog().setModalityType(ModalityType.DOCUMENT_MODAL);
	getDialog().setTitle(
		getArea().getAreaName() + " [" + getArea().getCards().size()
			+ "]");

	JScrollPane centerScp = new JScrollPane();
	getDetailArea().setPreferredSize(
		new Dimension(getOwner().getWidth() * 3 / 4, getOwner()
			.getHeight() * 3 / 4));
	getDetailArea().clearCardsSelection();
	getDetailArea().getSelectedIndexes().addAll(
		getFromArea().getSelectedIndexes());

	centerScp.setViewportView(getDetailArea());

	JScrollPane southScp = new JScrollPane();

	setDetailEditor(new JEditorPane());
	// final JEditorPane detail = new JEditorPane();

	getDetailEditor().setContentType("text/html");
	HTMLDocument doc = (HTMLDocument) getDetailEditor().getDocument();
	doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

	URL u = null;
	try {
	    u = new URL("file:" + BgWeissSchwarzDeck.WEISS_SCHWARZ_HTML_PATH
		    + "/");
	} catch (MalformedURLException e1) {
	    e1.printStackTrace();
	}
	doc.setBase(u);

	// getDetailEditor().setCaretPosition(0);

	// final JTextArea detail = new JTextArea();
	getDetailEditor().setEditable(false);

	southScp.setPreferredSize(new Dimension(800, 200));
	southScp.setViewportView(getDetailEditor());

	getDialog().getContentPane().add(centerScp, BorderLayout.CENTER);

	// getDialog().getContentPane().add(southScp, BorderLayout.SOUTH);

	getDialog().setVisible(true);
    }

    private final static String CSS_STR = "body {\r\n"
	    + "	background-color: #000000\r\n" + "}\r\n" + "table {\r\n"
	    + "	border: 1px solid #999999;\r\n"
	    + "	border-collapse: collapse;\r\n" + "}\r\n" + "table th,\r\n"
	    + "table td {\r\n" + "	border: 1px solid #999999;\r\n"
	    + "	padding: 4px;\r\n" + "	line-height: 120%;\r\n"
	    + "	color: #FFFFFF\r\n" + "}\r\n" + "table th {\r\n" + "}\r\n"
	    + "table.status {\r\n" + "	width: 536px;\r\n"
	    + "	margin-left: 0px !important;\r\n" + "	margin-bottom: 8px;\r\n"
	    + "}\r\n" + "table.status th {\r\n" + "	white-space: nowrap;\r\n"
	    + "	text-align: right;\r\n" + "	background-color: #555555;\r\n"
	    + "}\r\n" + "table.status th,\r\n" + "table.status td {\r\n"
	    + "	font-size: 10px;\r\n" + "}\r\n"
	    + "table.status tr.first th,\r\n"
	    + "table.status tr.first td {\r\n" + "	font-size: 12px;\r\n"
	    + "}\r\n" + "table.status .cell1 {\r\n" + "	width: 62px;\r\n"
	    + "}\r\n" + "table.status .cell2 {\r\n" + "	width: auto;\r\n"
	    + "}\r\n" + "table.status .cell3 {\r\n" + "	width: 56px;\r\n"
	    + "}\r\n" + "table.status .cell4 {\r\n" + "	width: 56px;\r\n"
	    + "}\r\n" + "table.status td.graphic {\r\n" + "	width: 192px;\r\n"
	    + "	height: 200px;\r\n" + "	vertical-align: middle;\r\n"
	    + "	text-align: center;\r\n" + "	border-color:black!important;\r\n"
	    + "	border-right-color:#999999!important;\r\n" + "}\r\n"
	    + "span.kana {\r\n" + "	font-size: 10px!important;\r\n" + "}";

    // private void readHtmlFile(JEditorPane editor, String file_name) {
    // HTMLDocument doc = (HTMLDocument) editor.getDocument();
    // doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
    // editor.setContentType("text/html");
    //
    // File f = new File(file_name);
    // try {
    // URL u = new URL("file:" + f.getParent() + "/");
    // doc.setBase(u);
    // } catch (MalformedURLException ignore) {
    // ignore.printStackTrace();
    // return;
    // }
    //
    // EditorKit kit = editor.getEditorKit();
    //
    // Reader r = null;
    // try {
    // r = new FileReader(f);
    // kit.read(r, doc, 0);
    // } catch (Exception e) {
    // throw new RuntimeException(e);
    // } finally {
    // if (r != null)
    // try {
    // r.close();
    // } catch (IOException e) {
    // }
    // }
    // }

    @Override
    public boolean isEnabled() {
	if (getEnableControlPlugin() != null
		&& !getEnableControlPlugin().isEnable()) {
	    return false;
	}
	if (getArea().getCards().size() == 0) {
	    return false;
	}
	return true;
    }

}
