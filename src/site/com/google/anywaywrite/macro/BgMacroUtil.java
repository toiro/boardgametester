/**
 *
 */
package site.com.google.anywaywrite.macro;

import site.com.google.anywaywrite.macro.BgMacro.BgMacroParameter.CountFrom;

/**
 * マクロ表記からJavaで用いる際のIndex指定を返すメソッドです。 マクロ表記については、別資料で説明します。//TODO
 * 
 * @author y-kitajima
 * 
 */
public class BgMacroUtil {

    /**
     * カードを直接選択する場合のIndexを返します<br />
     * {A, B, C} とカードがあった場合、cardsCount == 3となり、<br />
     * CountFromがFirstの時、Aを指定するには、no == 1 で、返り値は 0 となります。<br />
     * CountFromがLastの時、Aを指定するには、no == 3 で、返り値は 0 となります。<br />
     */
    public static int getSelectIndex(int cardsCount, CountFrom start, int no) {
	if (cardsCount <= 0) {
	    throw new IllegalArgumentException(
		    "cardsCount should be more than ZERO, but was "
			    + cardsCount);
	}
	if (no <= 0) {
	    throw new IllegalArgumentException(
		    "cardsCount should be more than ZERO, but was " + no);
	}
	if (no > cardsCount) {
	    throw new IllegalArgumentException(
		    "the no should be less than or equal to cardsCount, but was [no, cardsCount]=["
			    + no + ", " + cardsCount + "]");
	}
	switch (start) {
	case First:
	    return no - 1;
	case Last:
	    return cardsCount - no;
	default:
	    throw new RuntimeException("Program error");
	}
    }

    /**
     * カードとカードの間を指定する場合のIndexを返します<br />
     * {A, B, C} とカードがあった場合、cardsCount == 3となり、<br />
     * CountFromがFirstの時、AとBの間、を指定するには、no == 1 で、返り値は 1 となります。<br />
     * CountFromがLastの時、AとBの間、を指定するには、no == 2 で、返り値は 1 となります。<br />
     */
    public static int getInsertIndex(int cardsCount, CountFrom start, int no) {
	if (cardsCount < 0) {
	    throw new IllegalArgumentException(
		    "cardsCount should be more than or equal to ZERO, but was "
			    + cardsCount);
	}
	if (no < 0) {
	    throw new IllegalArgumentException(
		    "cardsCount should be more than or equal to ZERO, but was "
			    + no);
	}
	if (no > cardsCount) {
	    throw new IllegalArgumentException(
		    "the no should be less than or equal to cardsCount, but was [no, cardsCount]=["
			    + no + ", " + cardsCount + "]");
	}
	switch (start) {
	case First:
	    return no;
	case Last:
	    return cardsCount - no;
	default:
	    throw new RuntimeException("Program error");
	}
    }
}
