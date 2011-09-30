/**
 *
 */
package site.com.google.anywaywrite.component.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.List;
import java.util.Set;

import site.com.google.anywaywrite.item.card.BgCardItem;

/**
 * カードを配置するエリアが実装すべきメソッドを定義したインターフェスです。
 * 
 * @author y-kitajima
 * 
 */
public interface BgCardArea {

    /** エリアに配置されているカードのリストを返します */
    public List<BgCardItem> getCards();

    /** このエリアを表現するのに用いているコンポーネントを返します */
    public Component getComponent();

    /** エリアを一意に識別するためのエリア名称を返します */
    public String getAreaName();

    /** エリアで選択状態にあるカードのIndexを返します */
    public Set<Integer> getSelectedIndexes();

    /** エリア名を表示するか否かを返します。表示する場合はtrue.それ以外はfalseを返します。 */
    public boolean isDisplayAreaName();

    /** エリア名を描画します */
    public void paintAreaName(Graphics g);

    /** カード選択時に表示される枠の色を返します */
    public Color getSelectionCardBorderColor();
}
