/**
 *
 */
package site.com.google.anywaywrite.util.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;

/**
 * @author y-kitajima
 * 
 */
public class BgGuiUtil {

    public static Window getOwnerWindow(Component c) {
	if (c == null) {
	    throw new IllegalArgumentException(
		    "parameter c should not be null, but was.");
	}

	if (c instanceof Window) {
	    return (Window) c;
	}
	Container parent = c.getParent();
	while (parent != null) {
	    if (parent instanceof Window) {
		break;
	    }
	    parent = parent.getParent();
	}
	return (Window) parent;
    }
}
