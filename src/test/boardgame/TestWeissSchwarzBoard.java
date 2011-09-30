/**
 *
 */
package test.boardgame;

import javax.swing.SwingUtilities;

import boardgame.weissSchwarz.BgWeissSchwarzBoard;

/**
 * @author kitajima
 * 
 */
public class TestWeissSchwarzBoard {

    public static void main(String[] args) {
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		BgWeissSchwarzBoard board = new BgWeissSchwarzBoard();
		board.pack();
		board.setVisible(true);
	    }
	});

    }

}
