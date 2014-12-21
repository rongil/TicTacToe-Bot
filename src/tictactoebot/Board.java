package tictactoebot;

public class Board {

	// Board position statuses.
	private static final int EMPTY = 0;
	private static final int X = 1; // Player X
	private static final int O = 2; // Player O
	// End game statuses.
	private static final int INCOMPLETE = 10;
	private static final int TIE = 11;
	// Instance details.
	private int[][] board;
	private int numberOfMoves;

	public Board() {
		board = new int[3][3];
		createBoard();
	}

	/**
	 * Initializes a new board.
	 */
	private void createBoard() {

		// Row loop
		for (int i = 0; i < 3; ++i) {

			// Column Loop
			for (int j = 0; j < 3; ++j) {

				// Set everything to empty
				board[i][j] = EMPTY;
			}
		}

		// No moves currently.
		numberOfMoves = 0;
	}

	/**
	 * Check if the game has ended: 1) Winner 2) Full board
	 */
	private int checkGameOver() {

		// Loop through all values.
		for (int i = 0; i < 3; ++i) {
			// Split conditionals to allow distinguishing the type of win.
			// Check row win.
			if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
				return board[i][0];
				// Check column win.
			} else if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
				return board[0][i];
			}
		}

		// Check diagonals.
		if ((board[0][0] == board[1][1] && board[1][1] == board[2][2])
				|| (board[2][0] == board[1][1] && board[1][1] == board[0][2])) {
			return board[1][1];
		}

		// Check for a tie.
		if (numberOfMoves == 9) {
			return TIE;
		}

		// Game is not done...
		return INCOMPLETE;

	}

	/**
	 * Clears the board.
	 */
	public void clearBoard() {
		createBoard();
	}

	/**
	 * Prints out the current board.
	 */
	public void showBoard() {

		// Top border
		System.out.println("----------");

		// Row loop
		for (int i = 0; i < 3; ++i) {

			// Left border
			System.out.print('|');

			// Column Loop
			for (int j = 0; j < 3; ++j) {

				// Values and right borders
				System.out.print(board[i][j] + '|');
			}

			// Insert newline
			System.out.println();
			// Bottom borders
			System.out.println("----------");

		}
	}

}
