package test.boardgame;

import javax.swing.SwingUtilities;

import boardgame.cribagge.BgCribbageBoard;


public class TestCribbage {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				BgCribbageBoard board = new BgCribbageBoard();
				board.pack();
				board.setVisible(true);
			}
		});

	}

}
