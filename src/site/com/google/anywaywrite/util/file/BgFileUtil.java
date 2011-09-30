/**
 *
 */
package site.com.google.anywaywrite.util.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import site.com.google.anywaywrite.util.file.extention.BgExtensionFilter;
import site.com.google.anywaywrite.util.file.extention.BgExtensionFilterFactory;


/**
 * @author kitajima
 * 
 */
public class BgFileUtil {

    /**
     * 指定したパスにファイルを作成し、そのファイルを返します。 指定したパスのディレクトリが存在しない場合、ディレクトリを作成します。
     * 
     * @param force
     *            <ul>
     *            <li>true : 指定したパスにファイルが存在する場合削除します。</li>
     *            <li>false : 指定したパスにファイルが存在する場合nullを返します。</li>
     *            </ul>
     * @return
     */
    public static File mkFile(String path, boolean force) {
	File f = new File(path);
	if (f.exists()) {
	    if (!force) {
		return null;
	    }
	    f.delete();
	}

	prepareMakeFile(f);

	try {
	    f.createNewFile();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return f;
    }

    private static void prepareMakeFile(File f) {
	if (f.exists()) {
	    return;
	}
	File dir = new File(f.getParent());
	dir.mkdirs();
    }

    /**
     * 指定したファイルを読み込んで、一行単位のStringをリストに詰めて返します。 行末の改行文字はリストに含まれません。<br />
     * {@link #writeTextFile(File, List)}で本メソッドの返り値をファイルに書き込むと、
     * 読み込んだ内容と同一内容が書き込まれることが保証されています。
     * 
     * @param file
     * @return 何らかの原因で失敗した場合、空リストが返ります。
     * @exception IllegalArgumentException
     *                パラメータのファイルがディレクトリであった場合
     */
    public static List<String> readTextList(File file, String encoding) {
	if (file.isDirectory()) {
	    throw new IllegalArgumentException(
		    "file should not be directory, but was.");
	}

	List<String> txts = new ArrayList<String>();

	InputStream is = null;
	Reader r = null;
	BufferedReader br = null;
	try {
	    is = new FileInputStream(file);
	    r = new InputStreamReader(is, encoding);
	    br = new BufferedReader(r);

	    // br = new BufferedReader(new InputStreamReader(new
	    // FileInputStream(file), "MS932"));
	    while (br.ready()) {
		txts.add(br.readLine());
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    close(br);
	    close(r);
	    close(is);
	}

	return txts;
    }

    /**
     * 指定したファイルに文字列のリストの内容を書き込みます。 文字列のリストの各要素が順に、一行ごとに書き込まれ、各行の行末に改行文字が書き込まれます。<br />
     * {@link #readTextList(File)}で読み込まれた文字列をそのまま書き込むことができます。
     * 
     * @param file
     * @param txts
     */
    public static void writeTextFile(File file, List<String> txts) {
	OutputStream os = null;
	Writer w = null;
	BufferedWriter bw = null;
	try {
	    os = new FileOutputStream(file);
	    w = new OutputStreamWriter(os, "MS932");
	    bw = new BufferedWriter(w);
	    // bw = new BufferedWriter(new OutputStreamWriter(new
	    // FileOutputStream(file), "MS932"));
	    for (String txt : txts) {
		bw.write(txt);
		bw.newLine();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    close(bw);
	    if (bw != null) {
		try {
		    bw.close();
		} catch (IOException ignore) {
		}
	    }
	}
    }

    /**
     * {@link Closeable}なオブジェクトを安全にクローズします。 {@code Closeable}
     * なオブジェクトをfinallyブロックで閉じる際に各共通処理を 楽にする意図で提供されているメソッドです。
     * 
     * @param cl
     */
    public static void close(Closeable cl) {
	if (cl != null) {
	    try {
		cl.close();
	    } catch (IOException ignore) {
		ignore.printStackTrace();
	    }
	}
    }

    /**
     * 指定したディレクトリ以下に含まれる全てのファイルを再帰的に取得し、 その中で第２引数で指定された拡張子のファイルのみを返します。<br />
     * このメソッドは再帰的にサブディレクトリをすべてたどりますが、その返り値にはディレクトリは含めないことに注意してください。
     * 
     * @param dir
     * @param extension
     *            値が
     *            {@link site.com.google.anywaywrite.util.file.extension.BlueExtensionFilter#ALL_EXTENSION}
     *            の時、全てのファイルを返します。
     * @return
     */
    public static List<File> createExtensionFiles(File dir, String extension) {
	if (!dir.isDirectory()) {
	    throw new IllegalArgumentException(
		    "dir should not be null, but was.");
	}

	List<File> acc = new ArrayList<File>();

	createExtensionFilesRecursively(dir, acc, BgExtensionFilterFactory
		.getExtensionFilter(extension));

	return acc;
    }

    private static void createExtensionFilesRecursively(File dir,
	    List<File> acc, BgExtensionFilter bfil) {
	File[] fs = dir.listFiles();
	for (File f : fs) {
	    if (f.isFile() && bfil.accept(f))
	    // if (f.isFile() && f.getPath().endsWith(".log"))
	    {
		acc.add(f);
		continue;
	    }
	    if (f.isDirectory()) {
		createExtensionFilesRecursively(f, acc, bfil);
	    }
	}
    }
}
