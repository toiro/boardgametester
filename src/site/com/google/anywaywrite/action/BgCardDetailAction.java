/**
 *
 */
package site.com.google.anywaywrite.action;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.text.html.HTMLDocument;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgAreaLabelPane;
import site.com.google.anywaywrite.component.layout.BgReflectionLayout;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.util.file.BgFileUtil;
import boardgame.weissSchwarz.BgWeissSchwarzDeck;

/**
 * カード
 * 
 * @author kitajima
 * 
 */
public class BgCardDetailAction extends AbstractAction implements BgAreaAction {
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

    private int cardIndex;

    private final int getCardIndex() {
	return cardIndex;
    }

    private final void setCardIndex(int cardIndex) {
	this.cardIndex = cardIndex;
    }

    private int cardWidth;

    private final int getCardWidth() {
	return cardWidth;
    }

    private final void setCardWidth(int cardWidth) {
	this.cardWidth = cardWidth;
    }

    public final BgAreaActionEnableControlPlugin getEnableControlPlugin() {
	return enableControlPlugin;
    }

    public final void setEnableControlPlugin(
	    BgAreaActionEnableControlPlugin areaActionEnableControlPlugin) {
	this.enableControlPlugin = areaActionEnableControlPlugin;
    }

    private BgCardDetailAction(BgAreaLabel area, BgAreaLabelPane labelPane,
	    Window owner, int cardIndex, int cardWidth) {
	setArea(area);
	setLabelPane(labelPane);
	setOwner(owner);
	setCardIndex(cardIndex);
	setCardWidth(cardWidth);
	this.putValue(NAME, "detail");
    }

    public static BgCardDetailAction pointInstance(BgAreaLabel area,
	    BgAreaLabelPane labelPane, Window owner, Point p, int cardWidth) {
	List<Rectangle> cr = area.getAreaLayout().getCardRectangles();
	int cardIndex = -1;
	for (int idx = 0, size = cr.size(); idx < size; idx++) {
	    if (cr.get(idx).contains(p.getX(), p.getY())) {
		cardIndex = idx;
	    }
	}
	return new BgCardDetailAction(area, labelPane, owner, cardIndex,
		cardWidth);
    }

    public static BgCardDetailAction newInstance(BgAreaLabel area,
	    BgAreaLabelPane labelPane, Window owner, int cardIndex,
	    int cardWidth) {
	return new BgCardDetailAction(area, labelPane, owner, cardIndex,
		cardWidth);
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
	// TODO DetailDialogの位置は、自分で決めれるようにするか？
	getDialog().setBounds(getOwner().getWidth() / 8,
		getOwner().getHeight() / 8, getOwner().getWidth() * 3 / 4,
		getOwner().getHeight() * 3 / 4);

	getDialog().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	getDialog().setModalityType(ModalityType.DOCUMENT_MODAL);
	getDialog().setTitle(getArea().getAreaName()); // TODO タイトルをどうするかは課題。

	JPanel westScp = new JPanel();
	// JScrollPane westScp = new JScrollPane();
	getDetailArea().setPreferredSize(
		new Dimension(150, getOwner().getHeight() * 3 / 4));

	BorderLayout border = new BorderLayout();
	westScp.setLayout(border);
	westScp.add(getDetailArea(), BorderLayout.CENTER);
	// westScp.setViewportView(getDetailArea());

	JScrollPane centerScp = new JScrollPane();

	setDetailEditor(new JEditorPane());

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

	String path = area.getCards().get(getCardIndex()).getInfo()
		.getHtmlPath();
	if (path == null) {
	    getDetailEditor()
		    .setText(
			    area.getCards().get(getCardIndex()).getInfo()
				    .getFacepath());
	    return;
	}

	List<String> texts = BgFileUtil.readTextList(new File(path), "UTF-8");

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

	getDetailEditor().setEditable(false);

	centerScp.setPreferredSize(new Dimension(800, 200));
	centerScp.setViewportView(getDetailEditor());

	getDialog().getContentPane().add(westScp, BorderLayout.WEST);

	getDialog().getContentPane().add(centerScp, BorderLayout.CENTER);

	getDialog().setVisible(true);
    }

    BgAreaLabel detailArea;

    private BgAreaLabel getDetailArea() {
	if (this.detailArea == null) {
	    BgReflectionLayout layout = BgReflectionLayout
		    .newInstance(getCardWidth());
	    layout.setMargin(new Insets(10, 10, 10, 10));

	    // BgCardItem item = getArea().getCards().get(getCardIndex());
	    BgCardItem item = BgCardItem.newInstance(getArea().getCards().get(
		    getCardIndex()).getInfo());
	    List<BgCardItem> cards = new ArrayList<BgCardItem>();
	    cards.add(item);
	    this.detailArea = BgAreaLabel.newInstance(cards, layout);
	    this.detailArea.setAreaName("cardDetail");
	    this.detailArea.setDisplayAreaName(false);
	}
	return this.detailArea;
    }

    @Override
    public boolean isEnabled() {
	if (getEnableControlPlugin() != null
		&& !getEnableControlPlugin().isEnable()) {
	    return false;
	}

	if (getCardIndex() == -1) {
	    return false;
	}
	return true;
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

}
