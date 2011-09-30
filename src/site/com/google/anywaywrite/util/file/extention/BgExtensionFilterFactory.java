/**
 *
 */
package site.com.google.anywaywrite.util.file.extention;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

/**
 * �g���q����v����t�@�C���݂̂�Ԃ��t�B���^���擾���ĕԂ�Factory�N���X�ł��B
 * @author kitajima
 *
 */
public class BgExtensionFilterFactory
{
	private static Map<String, BgExtensionFilter> efche = new HashMap<String, BgExtensionFilter>();

	private final static FilenameFilter ALL_FILTER = new FilenameFilter()
	{
		public boolean accept(File file, String name)
		{
			return true;
		}
	};

	private final static BgExtensionFilter ALL_EXTENSION_FILTER = new BgExtensionFilter()
	{
		@Override
		public String getExtension()
		{
			return ALL_EXTENSION;
		}

		@Override
		public boolean accept(File file)
		{
			return ALL_FILTER.accept(file, getExtension());
		}
	};

	static
	{
		efche.put(BgExtensionFilter.ALL_EXTENSION, ALL_EXTENSION_FILTER);
	}

	public static BgExtensionFilter getExtensionFilter(String extension)
	{
		if (efche.containsKey(extension))
		{
			return efche.get(extension);
		}
		final String _extension = extension;
		final FilenameFilter ffil = new FilenameFilter()
		{
            public boolean accept(File file, String name) {
                return file.getPath().endsWith(_extension);
            }
		};
		final BgExtensionFilter efil = new BgExtensionFilter()
		{
			@Override
			public String getExtension()
			{
				return _extension;
			}
			@Override
			public boolean accept(File file)
			{
				return ffil.accept(file, getExtension());
			}
		};
		efche.put(extension, efil);
		return efil;
	}
}
