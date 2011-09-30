/**
 *
 */
package site.com.google.anywaywrite.item.card;

/**
 * �J�[�h�̓��I�ȏ�Ԃ�ێ����܂�
 * 
 * @author y-kitajima
 * 
 */
public class BgCardState {
    /**
     * �J�[�h�̕\�����Ǘ�����萔
     * 
     * @author y-kitajima
     * 
     */
    public enum Side {
	/**
	 * �\
	 */
	FACE,
	/**
	 * ��
	 */
	DOWN, ;
    }

    /**
     * �J�[�h�̌���Ǘ�����萔
     * 
     * @author y-kitajima
     * 
     */
    public enum Direction {
	/**
	 * �c��(�A���^�b�v���)
	 */
	UP,
	/**
	 * �E����(�^�b�v���)
	 */
	RIGHT,
	/**
	 * �t��
	 */
	REVERSE,
	/**
	 * ������
	 */
	LEFT, ;
    }

    private Side side;

    public void setSide(Side val) {
	this.side = val;
    }

    public Side getSide() {
	return this.side;
    }

    private Direction direction;

    public void setDirection(Direction val) {
	this.direction = val;
    }

    public Direction getDirection() {
	return this.direction;
    }

    private BgCardState(Side side, Direction direction) {
	setSide(side);
	setDirection(direction);
    }

    public static BgCardState newInstance(Side side, Direction direction) {
	return new BgCardState(side, direction);
    }
}
