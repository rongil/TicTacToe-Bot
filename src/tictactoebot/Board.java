package tictactoebot;

public class Board {

	// Board position statuses.
	private static final int EMPTY = 0;
	public static final int X = 1; // Player X
	public static final int O = 2; // Player O
	// End game statuses.
	public static final int INCOMPLETE = 10;
	public static final int TIE = 11;
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
	 * Makes a move by changing the corresponding square on the board.
	 * 
	 * @param i
	 *            - i coordinate
	 * @param j
	 *            - j coordinate
	 * @param player
	 *            - ID of player (O or X)
	 * @return - True if move is valid, false otherwise.
	 */
	public boolean makeMove(int i, int j, int player) {

		// Check to see if the move is valid.
		if (i < 0 || i > 3 || j < 0 || j > 3 || board[i][j] != Board.EMPTY) {
			System.out.println("Move is not valid.");
			return false;
		}

		board[i][j] = player;
		return true;
	}

	/**
	 * Check if the game has ended: 1) Winner 2) Full board
	 */
	public int checkGameOver() {

		// NOTE: By comparing positions between themselves and making sure they
		// are not empty, the player does not need to be passed in as an
		// argument.

		// Loop through all values.
		for (int i = 0; i < 3; ++i) {
			// Split conditionals to allow distinguishing the type of win.
			// Check row win.
			if (board[i][0] != EMPTY && board[i][0] == board[i][1]
					&& board[i][1] == board[i][2]) {
				return board[i][0];
				// Check column win.
			} else if (board[0][i] != EMPTY && board[0][i] == board[1][i]
					&& board[1][i] == board[2][i]) {
				return board[0][i];
			}
		}

		// Check diagonals.
		if (board[1][1] != EMPTY
				&& ((board[0][0] == board[1][1] && board[1][1] == board[2][2]) || (board[2][0] == board[1][1] && board[1][1] == board[0][2]))) {
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

		System.out.println();
		// Top border and axis
		System.out.print(" ");
		for (int x = 1; x < 4; ++x) {
			System.out.print(" " + Integer.toString(x) + "  ");
		}
		System.out.println();
		System.out.println("-------------");

		// Row loop
		for (int i = 0; i < 3; ++i) {

			// Left border
			System.out.print('|');

			// Column Loop
			for (int j = 0; j < 3; ++j) {

				// Values and right borders
				String value;
				// NOTE: The i, j values are printed in reverse compared to how
				// they are stored.
				switch (board[j][i]) {
				case EMPTY:
					value = "   ";
					break;
				case O:
					value = " O ";
					break;
				case X:
					value = " X ";
					break;
				default:
					value = "Err";
				}
				System.out.print(value + '|');
			}

			// Right axis
			System.out.println(" " + Integer.toString(i + 1));
			// Bottom borders
			System.out.println("-------------");

		}

	}

}
