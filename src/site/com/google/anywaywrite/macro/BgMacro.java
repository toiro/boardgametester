/**
 *
 */
package site.com.google.anywaywrite.macro;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import site.com.google.anywaywrite.action.BgAreaDetailAction;
import site.com.google.anywaywrite.action.BgAreaMoveAction;
import site.com.google.anywaywrite.action.BgCardsDirectionAction;
import site.com.google.anywaywrite.action.BgCardsMoveAction;
import site.com.google.anywaywrite.action.BgCardsSideAction;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgTempAreaInternalFrame;
import site.com.google.anywaywrite.item.card.BgCardState.Direction;
import site.com.google.anywaywrite.item.card.BgCardState.Side;
import site.com.google.anywaywrite.manage.BgGameAreaManager;
import site.com.google.anywaywrite.manage.BgGameAreaManager.Place;
import site.com.google.anywaywrite.util.control.BgAreaControlUtil;
import site.com.google.anywaywrite.util.swing.BgGuiUtil;

/**
 * @author y-kitajima
 * 
 */
public class BgMacro {
    int count;

    ActionName actionName;

    public enum ActionName {
	// 各カード用アクション
	Move, SideFace, SideDown, DirectionUp, DirectionRight, DirectionReverse, DirectionLeft,

	// 各エリア用アクション
	MoveAll, SideFaceAll, SideDownAll, DirectionUpAll, DirectionRightAll, DirectionReverseAll, DirectionLeftAll, Shuffle, Detail,

	// WAITアクション
	Wait, ;
    }

    BgMacroParameter firstParameter;

    BgMacroParameter secondParameter;

    public static class BgMacroParameter {
	/** カードの枚数を数え始める時のスタート地点を決めます */
	public enum CountFrom {
	    /** 1番はじめから */
	    First,
	    /** １番最後から */
	    Last, ;
	}

	BgGameAreaManager.Place place;
	String areaName;
	CountFrom countFrom;
	List<Integer> indexes;

	private final BgGameAreaManager.Place getPlace() {
	    return place;
	}

	private final String getAreaName() {
	    return areaName;
	}

	private final CountFrom getCountFrom() {
	    return countFrom;
	}

	private final List<Integer> getIndexes() {
	    if (indexes == null) {
		indexes = new ArrayList<Integer>();
	    }
	    return indexes;
	}

	private final void setPlace(BgGameAreaManager.Place place) {
	    this.place = place;
	}

	private final void setAreaName(String areaName) {
	    this.areaName = areaName;
	}

	private final void setCountFrom(CountFrom countFrom) {
	    this.countFrom = countFrom;
	}

	private BgMacroParameter(Place place, String areaName,
		CountFrom countFrom, int... indexes) {
	    setPlace(place);
	    setAreaName(areaName);
	    setCountFrom(countFrom);
	    for (int idx : indexes) {
		if (getIndexes().contains(idx)) {
		    throw new IllegalArgumentException(
			    "indexes should not contain the same number, but was at least number="
				    + idx);
		}
		getIndexes().add(idx);
	    }
	}

	public static BgMacroParameter newInstance(Place place,
		String areaName, CountFrom countFrom, int... indexes) {
	    return new BgMacroParameter(place, areaName, countFrom, indexes);
	}
    }

    private final int getCount() {
	return count;
    }

    private final ActionName getActionName() {
	return actionName;
    }

    // TODO 本当に公開の必要があるか？
    private final BgMacroParameter getFirstParameter() {
	return firstParameter;
    }

    // TODO 本当に公開の必要があるか？
    private final BgMacroParameter getSecondParameter() {
	return secondParameter;
    }

    private final void setCount(int count) {
	this.count = count;
    }

    private final void setActionName(ActionName actionName) {
	this.actionName = actionName;
    }

    private final void setFirstParameter(BgMacroParameter firstParam) {
	this.firstParameter = firstParam;
    }

    private final void setSecondParameter(BgMacroParameter secondParam) {
	this.secondParameter = secondParam;
    }

    private long waitTime;

    private final void setWaitTime(long val) {
	this.waitTime = val;
    }

    private final long getWaitTime() {
	return this.waitTime;
    }

    private BgMacro(int count, ActionName actionName,
	    BgMacroParameter firstParam, BgMacroParameter secondParam) {
	setCount(count);
	setActionName(actionName);
	setFirstParameter(firstParam);
	setSecondParameter(secondParam);
    }

    private BgMacro(long waitTime) {
	setWaitTime(waitTime);
	setCount(1);
	setActionName(ActionName.Wait);
	setFirstParameter(null);
	setSecondParameter(null);

    }

    public static BgMacro newInstance(int count, ActionName actionName,
	    BgMacroParameter firstParam, BgMacroParameter secondParam) {
	return new BgMacro(count, actionName, firstParam, secondParam);
    }

    public static BgMacro waitInstance(long waitTime) {
	return new BgMacro(waitTime);
    }

    private int getInsertIndex() {
	if (getSecondParameter() == null) {
	    return -1;
	}
	BgGameAreaManager manager = BgGameAreaManager.Instance;
	BgAreaLabel toArea = manager.getArea(getSecondParameter().getPlace(),
		getSecondParameter().getAreaName());
	return BgMacroUtil.getInsertIndex(toArea.getCards().size(),
		getSecondParameter().getCountFrom(), getSecondParameter()
			.getIndexes().get(0));
    }

    private List<Integer> getSelectedIndexes() {
	List<Integer> ret = new ArrayList<Integer>();
	if (getFirstParameter() == null) {
	    ret.add(-1);
	    return ret;
	}

	BgGameAreaManager manager = BgGameAreaManager.Instance;
	BgAreaLabel fromArea = manager.getArea(getFirstParameter().getPlace(),
		getFirstParameter().getAreaName());

	for (int idx = 0, size = getFirstParameter().getIndexes().size(); idx < size; idx++) {
	    ret.add(BgMacroUtil.getSelectIndex(fromArea.getCards().size(),
		    getFirstParameter().getCountFrom(), getFirstParameter()
			    .getIndexes().get(idx)));
	}

	return ret;
    }

    private List<Integer> getAllIndexes(BgAreaLabel area) {
	List<Integer> allIdxes = new ArrayList<Integer>();
	for (int idx = 0, size = area.getCards().size(); idx < size; idx++) {
	    allIdxes.add(idx);
	}
	return allIdxes;
    }

    private void paintImmediately(BgMacroParameter param) {
	if (param == null) {
	    return;
	}
	switch (param.getPlace()) {
	case BOARD:
	    BgAreaLabel area = BgGameAreaManager.Instance.getArea(Place.BOARD,
		    param.getAreaName());
	    area.paintImmediately(0, 0, area.getWidth(), area.getHeight());
	    break;
	case BOX:
	    break;
	case TEMP:
	    BgTempAreaInternalFrame inframe = BgGameAreaManager.Instance
		    .getTempFrameMap().get(param.getAreaName());

	    boolean originalVisible = inframe.isVisible();
	    if (inframe.getAreaLabel().getCards().size() == 0) {
		inframe.setVisible(false);
	    } else {
		inframe.setVisible(true);
	    }

	    if (originalVisible != inframe.isVisible()) {
		if (inframe.isVisible()) {
		    animationPaint(inframe, 100);
		} else {
		    BgGameAreaManager.Instance.getBoard().paintImmediately(
			    inframe.getX(), inframe.getY(), inframe.getWidth(),
			    inframe.getHeight());
		}
	    } else {
		BgGameAreaManager.Instance.getBoard().paintImmediately(
			inframe.getX(), inframe.getY(), inframe.getWidth(),
			inframe.getHeight());
	    }

	    break;
	default:
	    throw new RuntimeException("Program error.");
	}
    }

    private void animationPaint(BgTempAreaInternalFrame inframe, int frameRate) {
	for (int idx = 0; idx < frameRate; idx++) {
	    // xstart = inframe.getX() + (inframe.getWidth() / 5) * idx;
	    // xend = xstart + inframe.getWidth() / 5;
	    // ystart = inframe.getY() + (inframe.getHeight() / 5) * idx;
	    // yend = ystart + inframe.getHeight() / 5;

	    if (idx != frameRate - 1) {
		BgGameAreaManager.Instance.getBoard().paintImmediately(
			inframe.getX(), inframe.getY(),
			(inframe.getWidth() / frameRate) * (idx + 1),
			(inframe.getHeight() / frameRate) * (idx + 1));
	    } else {
		BgGameAreaManager.Instance.getBoard().paintImmediately(
			inframe.getX(), inframe.getY(), inframe.getWidth(),
			inframe.getHeight());
		continue;
	    }
	    try {
		Thread.sleep(100L / frameRate);
	    } catch (InterruptedException ignore) {
		ignore.printStackTrace();
	    }
	}
    }

    public void execute() {
	BgGameAreaManager manager = BgGameAreaManager.Instance;
	BgAreaLabel fromArea = null;
	if (getFirstParameter() != null) {
	    fromArea = manager.getArea(getFirstParameter().getPlace(),
		    getFirstParameter().getAreaName());
	}
	BgAreaLabel toArea = null;
	if (getSecondParameter() != null) {
	    toArea = manager.getArea(getSecondParameter().getPlace(),
		    getSecondParameter().getAreaName());
	}

	switch (getActionName()) {
	case Move:
	    invokeMoveAction(fromArea, toArea);
	    break;
	case DirectionUp:
	    invokeDirectionAction(fromArea, Direction.UP);
	    break;
	case DirectionRight:
	    invokeDirectionAction(fromArea, Direction.RIGHT);
	    break;
	case DirectionReverse:
	    invokeDirectionAction(fromArea, Direction.REVERSE);
	    break;
	case DirectionLeft:
	    invokeDirectionAction(fromArea, Direction.LEFT);
	    break;
	case SideFace:
	    invokeSideAction(fromArea, Side.FACE);
	    break;
	case SideDown:
	    invokeSideAction(fromArea, Side.DOWN);
	    break;
	case MoveAll:
	    BgAreaMoveAction.newInstance(fromArea, toArea, getInsertIndex())
		    .actionPerformed(
			    new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
				    toArea.getAreaName()));
	    break;
	case DirectionUpAll:
	    BgCardsDirectionAction.newInstance(fromArea, Direction.UP,
		    getAllIndexes(fromArea)).actionPerformed(
		    new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
			    Direction.UP.name()));
	    break;
	case DirectionRightAll:
	    BgCardsDirectionAction.newInstance(fromArea, Direction.RIGHT,
		    getAllIndexes(fromArea)).actionPerformed(
		    new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
			    Direction.UP.name()));
	    break;
	case DirectionReverseAll:
	    BgCardsDirectionAction.newInstance(fromArea, Direction.REVERSE,
		    getAllIndexes(fromArea)).actionPerformed(
		    new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
			    Direction.UP.name()));
	    break;
	case DirectionLeftAll:
	    BgCardsDirectionAction.newInstance(fromArea, Direction.LEFT,
		    getAllIndexes(fromArea)).actionPerformed(
		    new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
			    Direction.UP.name()));
	    break;
	case SideFaceAll:
	    BgCardsSideAction.newInstance(fromArea, Side.FACE,
		    getAllIndexes(fromArea)).actionPerformed(
		    new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
			    Side.FACE.name()));
	    break;
	case SideDownAll:
	    BgCardsSideAction.newInstance(fromArea, Side.DOWN,
		    getAllIndexes(fromArea)).actionPerformed(
		    new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
			    Side.DOWN.name()));
	    break;
	case Shuffle:
	    BgAreaControlUtil.shuffle(fromArea);
	    break;
	case Detail:
	    BgAreaDetailAction.newInstance(fromArea, manager.getBoard(),
		    BgGuiUtil.getOwnerWindow(fromArea)).actionPerformed(
		    new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
			    "detail"));
	    break;
	case Wait:
	    try {
		Thread.sleep(getWaitTime());
	    } catch (InterruptedException ignore) {
		ignore.printStackTrace();
	    }
	    break;
	default:
	    throw new RuntimeException("Program error");
	}

	paintImmediately(getFirstParameter());
	paintImmediately(getSecondParameter());
    }

    private void invokeMoveAction(BgAreaLabel fromArea, BgAreaLabel toArea) {
	for (int idx = 0; idx < getCount(); idx++) {
	    BgCardsMoveAction action = BgCardsMoveAction.newInstance(fromArea,
		    toArea, getSelectedIndexes(), getInsertIndex());
	    action.actionPerformed(new ActionEvent(this,
		    ActionEvent.ACTION_PERFORMED, action.getToArea()
			    .getAreaName()));
	}
    }

    private void invokeDirectionAction(BgAreaLabel area, Direction dir) {
	for (int idx = 0; idx < getCount(); idx++) {
	    BgCardsDirectionAction action = BgCardsDirectionAction.newInstance(
		    area, dir, getSelectedIndexes());
	    action.actionPerformed(new ActionEvent(this,
		    ActionEvent.ACTION_PERFORMED, Direction.UP.name()));
	}
    }

    private void invokeSideAction(BgAreaLabel area, Side side) {
	for (int idx = 0; idx < getCount(); idx++) {
	    BgCardsSideAction action = BgCardsSideAction.newInstance(area,
		    side, getSelectedIndexes());
	    action.actionPerformed(new ActionEvent(this,
		    ActionEvent.ACTION_PERFORMED, Direction.UP.name()));
	}
    }
    // "move (Box deck1 all) (Board deck)
    // 5 move (Board deck last) (Board hand last)
    // move (Board deck first 2) (Board hand last 3)
    // side (Board hand) down
    // side (Board hand) reverse //表裏逆にする"
    // -> sideDown (Board hand)
    // -> sideReverse (Board hand)
}
