/**
 *
 */
package site.com.google.anywaywrite.util.file.extention;

import java.io.File;

/**
 * @author kitajima
 *
 */
public interface BgExtensionFilter
{
	public static final String ALL_EXTENSION = "*";

	String getExtension();

	boolean accept(File file);

}
