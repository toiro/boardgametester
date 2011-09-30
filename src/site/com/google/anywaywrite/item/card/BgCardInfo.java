/**
 *
 */
package site.com.google.anywaywrite.item.card;

/**
 * �J�[�h�̐ÓI�ȏ���ێ����܂��B
 * 
 * @author y-kitajima
 * 
 */
public class BgCardInfo {

    private String id;
    private String facepath;
    private String downpath;
    private String htmlPath;

    private void setId(String val) {
	this.id = val;
    }

    public final String getId() {
	return this.id;
    }

    private void setFacepath(String path) {
	this.facepath = path;
    }

    public final String getFacepath() {
	return this.facepath;
    }

    private void setDownpath(String path) {
	this.downpath = path;
    }

    public final String getDownpath() {
	return this.downpath;
    }

    public final String getHtmlPath() {
	return htmlPath;
    }

    public final void setHtmlPath(String detail) {
	this.htmlPath = detail;
    }

    private BgCardInfo(String id, String facepath, String downpath) {
	setId(id);
	setFacepath(facepath);
	setDownpath(downpath);
    }

    public static final String TRUMP_PATH = "C:\\BOMPro\\workspace\\board\\resource\\trump";

    public static BgCardInfo newTrumpInfo(String gifName) {
	return new BgCardInfo(TRUMP_PATH + "\\" + gifName, TRUMP_PATH + "\\"
		+ gifName, TRUMP_PATH + "\\" + "H_06.png");
    }

    public static BgCardInfo newInstance(int no, String gifPath, String downPath) {
	return new BgCardInfo(Integer.toString(no), gifPath, downPath);
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	// sb.append("id = " + id);
	// sb.append("\n");
	// sb.append("facepath = " + facepath);
	// sb.append("\n");
	// sb.append("downpath = " + downpath);
	// sb.append("\n");

	sb.append("<html><body>");
	String html = "";
	sb.append(html);
	sb.append("</body></html>");
	return sb.toString();
    }
}
