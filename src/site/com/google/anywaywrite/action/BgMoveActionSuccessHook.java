/**
 *
 */
package site.com.google.anywaywrite.action;


import java.util.List;

import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.item.card.BgCardItem;

/**
 * AreaMoveActionが実行された後に呼ばれる処理を実装するためのフックです。
 * 
 * @author kitajima
 * 
 */
public interface BgMoveActionSuccessHook {
    void actionDone(BgAreaLabel fromArea, BgAreaLabel toArea,
	    List<BgCardItem> moveCards, List<Integer> moveIndexes);
}
