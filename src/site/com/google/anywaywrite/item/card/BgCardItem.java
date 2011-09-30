/**
 *
 */
package site.com.google.anywaywrite.item.card;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;
import site.com.google.anywaywrite.util.gui.BgCompatibleUtil;

/**
 * ���Ƃ��ẴJ�[�h��\������N���X�ł�
 * 
 * @author y-kitajima
 * 
 */
public class BgCardItem {

    private BgCardInfo info;

    private void setInfo(BgCardInfo val) {
	this.info = val;
    }

    public BgCardInfo getInfo() {
	return this.info;
    }

    private BgCardState state;

    private void setState(BgCardState val) {
	this.state = val;
    }

    private BgCardState getState() {
	return this.state;
    }

    private BgCardItem(BgCardInfo info, BgCardState state) {
	setInfo(info);
	setState(state);
    }

    public static BgCardItem newInstance(BgCardInfo info) {
	return new BgCardItem(info, BgCardState
		.newInstance(Side.FACE, Direction.UP));
    }

    public void setDirection(BgCardState.Direction dir) {
	getState().setDirection(dir);
    }

    public void setSide(BgCardState.Side side) {
	getState().setSide(side);
    }

    public Direction getDirection() {
	return getState().getDirection();
    }

    public Side getSide() {
	return getState().getSide();
    }

    /**
     * �A���^�b�v��ԂȂ�^�b�v��ԂɁA �^�b�v��ԂȂ�A���^�b�v��Ԃɂ���B ����ȊO���Ȃ牽�����Ȃ�
     */
    public void toggleTap() {
	switch (getState().getDirection()) {
	case UP:
	    getState().setDirection(Direction.RIGHT);
	    break;
	case RIGHT:
	    getState().setDirection(Direction.UP);
	    break;
	case REVERSE:
	    // DO NOTHING;
	    break;
	case LEFT:
	    // DO NOTHING;
	    break;
	default:
	    throw new RuntimeException("Program error");
	}
    }

    public void tap() {
	getState().setDirection(Direction.RIGHT);
    }

    public void untap() {
	getState().setDirection(Direction.UP);
    }

    public void toggleSide() {
	switch (getState().getSide()) {
	case FACE:
	    getState().setSide(Side.DOWN);
	    break;
	case DOWN:
	    getState().setSide(Side.FACE);
	    break;
	default:
	    throw new RuntimeException("Program error");
	}
    }

    /**
     * Direction��UP�Œ�ŁASide���l�������݊��摜��Ԃ��܂��B
     * 
     * @return
     */
    public BufferedImage getImage() {
	BufferedImage bimg;
	String path = null;
	try {
	    if (getState().getSide() == Side.FACE) {
		path = getInfo().getFacepath();
	    } else if (getState().getSide() == Side.DOWN) {
		path = getInfo().getDownpath();
	    } else {
		throw new RuntimeException("Program error");
	    }

	    bimg = BgCompatibleUtil.loadCompatibleImage(new File(path));
	} catch (IOException e) {
	    e.printStackTrace();
	    throw new RuntimeException("Image File does not exist on " + path);
	}
	return bimg;
    }
}
