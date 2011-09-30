/**
 *
 */
package site.com.google.anywaywrite.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import site.com.google.anywaywrite.component.gui.BgAreaBoxInternalFrame;
import site.com.google.anywaywrite.component.gui.BgAreaLabel;
import site.com.google.anywaywrite.component.gui.BgAreaLabelPane;
import site.com.google.anywaywrite.component.gui.BgTempAreaInternalFrame;
import site.com.google.anywaywrite.macro.BgMacroSet;

/**
 * ゲームを構成するエリアを全て管理するマネージャです。 ゲーム盤面が構築されてから呼ばれることを想定しています。
 * 
 * @author y-kitajima
 * 
 */
public enum BgGameAreaManager {
    Instance, ;

    public enum Place {
	BOX, BOARD, TEMP;
    }

    private BgAreaBoxInternalFrame box;

    private BgAreaLabelPane board;

    // private List<BgTempAreaInternalFrame> temps;

    public final BgAreaBoxInternalFrame getBox() {
	return box;
    }

    public final BgAreaLabelPane getBoard() {
	return board;
    }

    // public final List<BgTempAreaInternalFrame> getTemps() {
    // if (temps == null) {
    // temps = new ArrayList<BgTempAreaInternalFrame>();
    // }
    // return temps;
    // }

    public final void setBox(BgAreaBoxInternalFrame box) {
	if (this.box == null) {
	    List<BgAreaLabel> boxAreas = box.getAreaSet();
	    for (BgAreaLabel boxArea : boxAreas) {
		getBoxMap().put(boxArea.getAreaName(), boxArea);
	    }
	}
	this.box = box;
    }

    public final void setBoard(BgAreaLabelPane board) {
	if (this.board == null) {
	    List<BgAreaLabel> boardAreas = board.getAreaSet();
	    for (BgAreaLabel boardArea : boardAreas) {
		getBoardMap().put(boardArea.getAreaName(), boardArea);
	    }
	    List<BgTempAreaInternalFrame> tempFrameSet = board
		    .getTempFrameSet();
	    for (BgTempAreaInternalFrame tempFrame : tempFrameSet) {
		getTempMap().put(tempFrame.getAreaName(),
			tempFrame.getAreaLabel());
		getTempFrameMap().put(tempFrame.getAreaName(), tempFrame);
	    }
	}
	this.board = board;
    }

    private Map<String, BgAreaLabel> boxMap;

    private Map<String, BgAreaLabel> getBoxMap() {
	if (boxMap == null) {
	    boxMap = new HashMap<String, BgAreaLabel>();
	}
	return boxMap;
    }

    private Map<String, BgAreaLabel> boardMap;

    private Map<String, BgAreaLabel> getBoardMap() {
	if (boardMap == null) {
	    boardMap = new HashMap<String, BgAreaLabel>();
	}
	return boardMap;
    }

    private Map<String, BgAreaLabel> tempMap;

    private Map<String, BgAreaLabel> getTempMap() {
	if (tempMap == null) {
	    tempMap = new HashMap<String, BgAreaLabel>();
	}
	return tempMap;
    }

    private Map<String, BgTempAreaInternalFrame> tempFrameMap;

    public final Map<String, BgTempAreaInternalFrame> getTempFrameMap() {
	if (tempFrameMap == null) {
	    tempFrameMap = new HashMap<String, BgTempAreaInternalFrame>();
	}
	return tempFrameMap;
    }

    private BgGameAreaManager() {
    }

    private BgGameAreaManager(BgAreaBoxInternalFrame box, BgAreaLabelPane board) {
	setBox(box);
	setBoard(board);

	List<BgAreaLabel> boxAreas = box.getAreaSet();
	for (BgAreaLabel boxArea : boxAreas) {
	    getBoxMap().put(boxArea.getAreaName(), boxArea);
	}

	List<BgAreaLabel> boardAreas = board.getAreaSet();
	for (BgAreaLabel boardArea : boardAreas) {
	    getBoardMap().put(boardArea.getAreaName(), boardArea);
	}
    }

    public BgAreaLabel getArea(Place place, String areaName) {
	switch (place) {
	case BOX:
	    if (getBoxMap().containsKey(areaName)) {
		return getBoxMap().get(areaName);
	    }
	    throw new IllegalArgumentException(
		    "There is no area in Box, the area name was " + areaName);
	case BOARD:
	    if (getBoardMap().containsKey(areaName)) {
		return getBoardMap().get(areaName);
	    }
	    throw new IllegalArgumentException(
		    "There is no area in Board, the area name was " + areaName);
	case TEMP:
	    if (getTempMap().containsKey(areaName)) {
		return getTempMap().get(areaName);
	    }
	    throw new IllegalArgumentException(
		    "There is no area in Temp, the area name was " + areaName);
	default:
	    throw new RuntimeException("Program error.");
	}
    }

    private List<BgMacroSet> installedMacros;

    private List<BgMacroSet> getInstalledMacros() {
	if (installedMacros == null) {
	    installedMacros = new ArrayList<BgMacroSet>();
	}
	return installedMacros;
    }

    public void installAllMacroSet(List<BgMacroSet> addmacros) {
	Set<String> nameSet = new HashSet<String>();
	for (BgMacroSet macroset : getInstalledMacros()) {
	    nameSet.add(macroset.getName());
	}

	Set<String> duplicateName = new HashSet<String>();
	for (BgMacroSet addmacro : addmacros) {
	    if (nameSet.contains(addmacro.getName())) {
		duplicateName.add(addmacro.getName());

	    }
	}
	if (duplicateName.size() > 0) {
	    throw new RuntimeException(makeDuplicateErrMessage(duplicateName));
	}
	getInstalledMacros().addAll(addmacros);
    }

    private String makeDuplicateErrMessage(Set<String> duplicateName) {
	StringBuilder sb = new StringBuilder();
	sb
		.append("the macroSet name should not be duplicated, but was. The name = ");
	for (Iterator<String> it = duplicateName.iterator(); it.hasNext();) {
	    sb.append(it.next());
	    sb.append(", ");
	}
	sb.setLength(sb.length() - 2);
	return sb.toString();
    }

    public void installMacroSet(BgMacroSet addmacro) {
	Set<String> nameSet = new HashSet<String>();
	for (BgMacroSet macroset : getInstalledMacros()) {
	    nameSet.add(macroset.getName());
	}
	if (nameSet.contains(addmacro.getName())) {
	    throw new RuntimeException(
		    "the macroSet name should not be duplicated, but was. The name = "
			    + addmacro.getName());
	}
	getInstalledMacros().add(addmacro);
    }

}
