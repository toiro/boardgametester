/**
 *
 */
package site.com.google.anywaywrite.macro;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

/**
 * @author y-kitajima
 * 
 */
public class BgMacroSet extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private String name;

    public final String getName() {
	return name;
    }

    private final void setName(String name) {
	if (name == null || name.length() == 0) {
	    throw new IllegalArgumentException(
		    "name should not be null or zero length, but was.");
	}
	this.name = name;
	this.putValue(NAME, name);
    }

    private List<BgMacro> macros;

    private List<BgMacro> getMacros() {
	if (macros == null) {
	    macros = new ArrayList<BgMacro>();
	}
	return macros;
    }

    private BgMacroSet(String name, List<BgMacro> macros) {
	setName(name);
	getMacros().addAll(macros);
    }

    public static BgMacroSet newInstance(String name, List<BgMacro> macros) {
	return new BgMacroSet(name, macros);
    }

    public static BgMacroSet newInstance(String name, BgMacro... macros) {
	List<BgMacro> list = new ArrayList<BgMacro>();
	for (BgMacro macro : macros) {
	    list.add(macro);
	}
	return new BgMacroSet(name, list);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	for (BgMacro macro : getMacros()) {
	    macro.execute();
	}
    }

}
