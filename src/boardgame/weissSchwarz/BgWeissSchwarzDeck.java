/**
 *
 */
package boardgame.weissSchwarz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import site.com.google.anywaywrite.item.BgDeck;
import site.com.google.anywaywrite.item.card.BgCardInfo;

/**
 * @author kitajima
 * 
 */
public class BgWeissSchwarzDeck implements BgDeck {
    List<BgCardInfo> cards = new ArrayList<BgCardInfo>();

    private static final String WEISS_SCHWARZ_IMAGE_PATH = "C:\\BOMPro\\workspace\\board\\resource\\ws\\card\\nanoha\\image\\";
    public static final String WEISS_SCHWARZ_HTML_PATH = "C:\\BOMPro\\workspace\\board\\resource\\ws\\card\\nanoha\\html\\";

    private static BgCardInfo newCardInfo(int no, String gifName) {
	BgCardInfo info = BgCardInfo.newInstance(no, WEISS_SCHWARZ_IMAGE_PATH
		+ gifName, WEISS_SCHWARZ_IMAGE_PATH + "wsdown.gif");
	info.setHtmlPath(WEISS_SCHWARZ_HTML_PATH
		+ gifName.replace(".gif", ".html"));
	return info;
    }

    private BgWeissSchwarzDeck() {
	cards.add(newCardInfo(1, "na_w12_070.gif"));
	cards.add(newCardInfo(2, "na_w12_070.gif"));
	cards.add(newCardInfo(3, "n1_we06_21.gif"));
	cards.add(newCardInfo(4, "n1_we06_21.gif"));
	cards.add(newCardInfo(5, "na_w12_002.gif"));
	cards.add(newCardInfo(6, "na_w12_002.gif"));
	cards.add(newCardInfo(7, "na_w12_002.gif"));
	cards.add(newCardInfo(8, "na_w12_007.gif"));
	cards.add(newCardInfo(9, "na_w12_007.gif"));
	cards.add(newCardInfo(10, "na_w12_007.gif"));
	cards.add(newCardInfo(11, "n1_we06_08.gif"));
	cards.add(newCardInfo(12, "n1_we06_08.gif"));
	cards.add(newCardInfo(13, "na_w12_022.gif"));
	cards.add(newCardInfo(14, "na_w12_022.gif"));
	cards.add(newCardInfo(15, "na_w12_022.gif"));
	cards.add(newCardInfo(16, "na_w12_017.gif"));
	cards.add(newCardInfo(17, "na_w12_017.gif"));
	cards.add(newCardInfo(18, "na_w12_017.gif"));
	cards.add(newCardInfo(19, "n1_we06_07.gif"));
	cards.add(newCardInfo(20, "n1_we06_07.gif"));
	cards.add(newCardInfo(21, "n1_we06_07.gif"));
	cards.add(newCardInfo(22, "n1_we06_07.gif"));
	cards.add(newCardInfo(23, "ns_w04_005.gif"));
	cards.add(newCardInfo(24, "n1_we06_25.gif"));
	cards.add(newCardInfo(25, "na_w12_010.gif"));
	cards.add(newCardInfo(26, "na_w12_010.gif"));
	cards.add(newCardInfo(27, "na_w12_054.gif"));
	cards.add(newCardInfo(28, "na_w12_054.gif"));
	cards.add(newCardInfo(29, "na_w12_054.gif"));
	cards.add(newCardInfo(30, "n1_we06_01.gif"));
	cards.add(newCardInfo(31, "n1_we06_01.gif"));
	cards.add(newCardInfo(32, "na_w12_015.gif"));
	cards.add(newCardInfo(33, "na_w12_015.gif"));
	cards.add(newCardInfo(34, "na_w12_015.gif"));
	cards.add(newCardInfo(35, "na_w12_015.gif"));
	cards.add(newCardInfo(36, "n1_we06_32.gif"));
	cards.add(newCardInfo(37, "ns_w04_007.gif"));
	cards.add(newCardInfo(38, "ns_w04_007.gif"));
	cards.add(newCardInfo(39, "ns_w04_007.gif"));
	cards.add(newCardInfo(40, "ns_w04_007.gif"));
	cards.add(newCardInfo(41, "na_w12_053.gif"));
	cards.add(newCardInfo(42, "na_w12_064.gif"));
	cards.add(newCardInfo(43, "na_w12_024.gif"));
	cards.add(newCardInfo(44, "na_w12_024.gif"));
	cards.add(newCardInfo(45, "na_w12_024.gif"));
	cards.add(newCardInfo(46, "na_w12_023.gif"));
	cards.add(newCardInfo(47, "na_w12_023.gif"));
	cards.add(newCardInfo(48, "na_w12_023.gif"));
	cards.add(newCardInfo(49, "na_w12_023.gif"));
	cards.add(newCardInfo(50, "na_w12_073.gif"));

	// cards.add(newCardInfo(1, "na_w12_001.gif"));
	// cards.add(newCardInfo(2, "na_w12_001.gif"));
	// cards.add(newCardInfo(3, "na_w12_001.gif"));
	// cards.add(newCardInfo(4, "na_w12_001.gif"));
	// cards.add(newCardInfo(5, "na_w12_002.gif"));
	// cards.add(newCardInfo(6, "na_w12_002.gif"));
	// cards.add(newCardInfo(7, "na_w12_002.gif"));
	// cards.add(newCardInfo(8, "na_w12_002.gif"));
	// cards.add(newCardInfo(9, "na_w12_003.gif"));
	// cards.add(newCardInfo(10, "na_w12_003.gif"));
	// cards.add(newCardInfo(11, "na_w12_003.gif"));
	// cards.add(newCardInfo(12, "na_w12_003.gif"));
	// cards.add(newCardInfo(13, "na_w12_004.gif"));
	// cards.add(newCardInfo(14, "na_w12_004.gif"));
	// cards.add(newCardInfo(15, "na_w12_004.gif"));
	// cards.add(newCardInfo(16, "na_w12_004.gif"));
	// cards.add(newCardInfo(17, "na_w12_005.gif"));
	// cards.add(newCardInfo(18, "na_w12_005.gif"));
	// cards.add(newCardInfo(19, "na_w12_005.gif"));
	// cards.add(newCardInfo(20, "na_w12_005.gif"));
	// cards.add(newCardInfo(21, "na_w12_006.gif"));
	// cards.add(newCardInfo(22, "na_w12_006.gif"));
	// cards.add(newCardInfo(23, "na_w12_006.gif"));
	// cards.add(newCardInfo(24, "na_w12_006.gif"));
	// cards.add(newCardInfo(25, "na_w12_007.gif"));
	// cards.add(newCardInfo(26, "na_w12_007.gif"));
	// cards.add(newCardInfo(27, "na_w12_007.gif"));
	// cards.add(newCardInfo(28, "na_w12_007.gif"));
	// cards.add(newCardInfo(29, "na_w12_008.gif"));
	// cards.add(newCardInfo(30, "na_w12_008.gif"));
	// cards.add(newCardInfo(31, "na_w12_008.gif"));
	// cards.add(newCardInfo(32, "na_w12_008.gif"));
	// cards.add(newCardInfo(33, "na_w12_009.gif"));
	// cards.add(newCardInfo(34, "na_w12_009.gif"));
	// cards.add(newCardInfo(35, "na_w12_009.gif"));
	// cards.add(newCardInfo(36, "na_w12_009.gif"));
	// cards.add(newCardInfo(37, "na_w12_010.gif"));
	// cards.add(newCardInfo(38, "na_w12_010.gif"));
	// cards.add(newCardInfo(39, "na_w12_010.gif"));
	// cards.add(newCardInfo(40, "na_w12_010.gif"));
	// cards.add(newCardInfo(41, "na_w12_011.gif"));
	// cards.add(newCardInfo(42, "na_w12_011.gif"));
	// cards.add(newCardInfo(43, "na_w12_024.gif"));
	// cards.add(newCardInfo(44, "na_w12_024.gif"));
	// cards.add(newCardInfo(45, "na_w12_024.gif"));
	// cards.add(newCardInfo(46, "na_w12_024.gif"));
	// cards.add(newCardInfo(47, "na_w12_025.gif"));
	// cards.add(newCardInfo(48, "na_w12_025.gif"));
	// cards.add(newCardInfo(49, "na_w12_025.gif"));
	// cards.add(newCardInfo(50, "na_w12_025.gif"));
    }

    public static BgWeissSchwarzDeck newInstance() {
	return new BgWeissSchwarzDeck();
    }

    @Override
    public List<BgCardInfo> getCards() {
	return Collections.unmodifiableList(new ArrayList<BgCardInfo>(cards));
    }
}
