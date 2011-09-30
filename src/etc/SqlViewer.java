/**
 *
 */
package etc;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import site.com.google.anywaywrite.util.file.BgFileUtil;

/**
 * @author y-kitajima
 * 
 */
public class SqlViewer extends JFrame {

    List<String> texts = new ArrayList<String>();

    private String sqlFilePath = "C:\\Users\\y-kitajima.DSOL.000\\Desktop\\BOM2.txt";

    public SqlViewer() {
	List<String> txts = BgFileUtil
		.readTextList(new File(sqlFilePath), "sjis");

	List<SqlStmt> stmts = getSqlStmts(txts);

	CardLayout cl = new CardLayout();
	JPanel cardPanel = new JPanel();
	cardPanel.setLayout(cl);
	int no = 0;
	for (SqlStmt stmt : stmts) {
	    no++;
	    JPanel panel = new JPanel();
	    JScrollPane scp = new JScrollPane();
	    JTextArea area = new JTextArea();
	    for (String line : stmt.getLines()) {
		area.append(line);
		area.append("\n");
	    }
	    scp.setViewportView(area);
	    panel.add(scp);
	    cardPanel.add(panel, "sql" + no);
	}
	getContentPane().add(cardPanel, BorderLayout.CENTER);

	// if (stmts.size() > 0) {
	// cl.show(this, "sql1");
	// }
    }

    private static class SqlStmt {
	private List<String> lines;
	private List<Integer> indexes;

	private List<String> getLines() {
	    if (lines == null) {
		lines = new ArrayList<String>();
	    }
	    return lines;
	}

	private List<Integer> getIndexes() {
	    if (indexes == null) {
		indexes = new ArrayList<Integer>();
	    }
	    return indexes;
	}

	private void addLine(int idx, String line) {
	    getIndexes().add(idx);
	    getLines().add(line);
	}
    }

    private List<SqlStmt> getSqlStmts(List<String> txts) {
	List<SqlStmt> ret = new ArrayList<SqlStmt>();

	boolean isStmt = false;
	SqlStmt stmt = null;
	for (int idx = 0, size = txts.size(); idx < size; idx++) {
	    String line = txts.get(idx);
	    if (line == null) {
		continue;
	    }

	    line = line.trim();
	    if (line.startsWith("sql_stmt :=")) {
		if (!isStmt) {
		    stmt = new SqlStmt();
		    stmt.addLine(idx, line);
		    isStmt = true;
		} else {
		    stmt.addLine(idx, line);
		}
	    } else {
		if (isStmt) {
		    ret.add(stmt);
		    stmt = null;
		    isStmt = false;
		}
	    }
	}
	return ret;
    }

    public static void main(String[] args) {
	final SqlViewer sv = new SqlViewer();
	SwingUtilities.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		sv.setSize(200, 200);
		sv.setVisible(true);
	    }

	});
    }

}
