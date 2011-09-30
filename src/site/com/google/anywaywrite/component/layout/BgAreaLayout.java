/**
 *
 */
package site.com.google.anywaywrite.component.layout;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.List;

import site.com.google.anywaywrite.component.gui.BgCardArea;
import site.com.google.anywaywrite.item.card.BgCardItem;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;

/**
 * @author y-kitajima
 * 
 */
public interface BgAreaLayout {
    void paintComponent(Graphics g);

    void setMargin(Insets inset);

    void setPosition(BgLayoutPosition position);

    void setBase(BgLayoutBase base);

    void setArea(BgCardArea area);

    String verify(List<BgCardItem> cards);

    public void setInitialDirection(Direction dir);

    public Direction getInitialDirection();

    public void setInitialSide(Side side);

    public Side getInitialSide();

    public List<Rectangle> getCardRectangles();

}