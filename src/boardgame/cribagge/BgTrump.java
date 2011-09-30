/**
 *
 */
package boardgame.cribagge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import site.com.google.anywaywrite.item.BgDeck;
import site.com.google.anywaywrite.item.card.BgCardInfo;

/**
 * @author y-kitajima
 * 
 */
public class BgTrump implements BgDeck {
    List<BgCardInfo> cards = new ArrayList<BgCardInfo>();

    private BgTrump() {
	cards.add(BgCardInfo.newTrumpInfo("C_01.png"));
	cards.add(BgCardInfo.newTrumpInfo("C_02.png"));
	cards.add(BgCardInfo.newTrumpInfo("C_03.png"));
	cards.add(BgCardInfo.newTrumpInfo("C_04.png"));
	cards.add(BgCardInfo.newTrumpInfo("C_05.png"));
	cards.add(BgCardInfo.newTrumpInfo("C_06.png"));
	cards.add(BgCardInfo.newTrumpInfo("C_07.png"));
	cards.add(BgCardInfo.newTrumpInfo("C_08.png"));
	cards.add(BgCardInfo.newTrumpInfo("C_09.png"));
	cards.add(BgCardInfo.newTrumpInfo("C_10.png"));
	cards.add(BgCardInfo.newTrumpInfo("C_11.png"));
	cards.add(BgCardInfo.newTrumpInfo("C_12.png"));
	cards.add(BgCardInfo.newTrumpInfo("C_13.png"));

	cards.add(BgCardInfo.newTrumpInfo("D_01.png"));
	cards.add(BgCardInfo.newTrumpInfo("D_02.png"));
	cards.add(BgCardInfo.newTrumpInfo("D_03.png"));
	cards.add(BgCardInfo.newTrumpInfo("D_04.png"));
	cards.add(BgCardInfo.newTrumpInfo("D_05.png"));
	cards.add(BgCardInfo.newTrumpInfo("D_06.png"));
	cards.add(BgCardInfo.newTrumpInfo("D_07.png"));
	cards.add(BgCardInfo.newTrumpInfo("D_08.png"));
	cards.add(BgCardInfo.newTrumpInfo("D_09.png"));
	cards.add(BgCardInfo.newTrumpInfo("D_10.png"));
	cards.add(BgCardInfo.newTrumpInfo("D_11.png"));
	cards.add(BgCardInfo.newTrumpInfo("D_12.png"));
	cards.add(BgCardInfo.newTrumpInfo("D_13.png"));

	cards.add(BgCardInfo.newTrumpInfo("H_01.png"));
	cards.add(BgCardInfo.newTrumpInfo("H_02.png"));
	cards.add(BgCardInfo.newTrumpInfo("H_03.png"));
	cards.add(BgCardInfo.newTrumpInfo("H_04.png"));
	cards.add(BgCardInfo.newTrumpInfo("H_05.png"));
	cards.add(BgCardInfo.newTrumpInfo("H_06.png"));
	cards.add(BgCardInfo.newTrumpInfo("H_07.png"));
	cards.add(BgCardInfo.newTrumpInfo("H_08.png"));
	cards.add(BgCardInfo.newTrumpInfo("H_09.png"));
	cards.add(BgCardInfo.newTrumpInfo("H_10.png"));
	cards.add(BgCardInfo.newTrumpInfo("H_11.png"));
	cards.add(BgCardInfo.newTrumpInfo("H_12.png"));
	cards.add(BgCardInfo.newTrumpInfo("H_13.png"));

	cards.add(BgCardInfo.newTrumpInfo("S_01.png"));
	cards.add(BgCardInfo.newTrumpInfo("S_02.png"));
	cards.add(BgCardInfo.newTrumpInfo("S_03.png"));
	cards.add(BgCardInfo.newTrumpInfo("S_04.png"));
	cards.add(BgCardInfo.newTrumpInfo("S_05.png"));
	cards.add(BgCardInfo.newTrumpInfo("S_06.png"));
	cards.add(BgCardInfo.newTrumpInfo("S_07.png"));
	cards.add(BgCardInfo.newTrumpInfo("S_08.png"));
	cards.add(BgCardInfo.newTrumpInfo("S_09.png"));
	cards.add(BgCardInfo.newTrumpInfo("S_10.png"));
	cards.add(BgCardInfo.newTrumpInfo("S_11.png"));
	cards.add(BgCardInfo.newTrumpInfo("S_12.png"));
	cards.add(BgCardInfo.newTrumpInfo("S_13.png"));
    }

    public static BgTrump newInstance() {
	return new BgTrump();
    }

    @Override
    public List<BgCardInfo> getCards() {
	return Collections.unmodifiableList(new ArrayList<BgCardInfo>(cards));
    }
}
