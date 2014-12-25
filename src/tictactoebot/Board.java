package tictactoebot;

import java.util.HashSet;

/**
 * Class to represent a game board.
 * 
 * @author rongil
 *
 */
public class Board {

	// Board position statuses.
	public static final int EMPTY = 0;
	public static final int X = 1; // Player X
	public static final int O = 2; // Player O
	// End game statuses.
	public static final int INCOMPLETE = 10;
	public static final int TIE = 11;
	// Instance details.
	private int[][] board;
	private int numberOfMoves;
	private HashSet<Move> validMoves;

	/**
	 * Initializes a new empty board.
	 */
	public Board() {
		this.board = new int[3][3];
		createBoard();
	}

	/**
	 * Initializes a new board to the same values as the provided board.
	 * 
	 * @param board
	 *            - The board to copy
	 */
	public Board(Board board) {
		this.board = new int[3][3];
		copyBoard(board);
	}

	/**
	 * Initializes a new board.
	 */
	private void createBoard() {

		validMoves = new HashSet<Move>();
		// Row loop
		for (int i = 0; i < 3; ++i) {

			// Column Loop
			for (int j = 0; j < 3; ++j) {

				// Set everything to empty
				board[i][j] = EMPTY;
				// Every move is valid
				validMoves.add(new Move(i, j));

			}
		}

		// No moves currently.
		numberOfMoves = 0;

	}

	/**
	 * Copies the provided board onto the instance's board. This is called only
	 * from the constructor that passes in a board to make a copy of.
	 * 
	 * @param board
	 *            - The board to copy
	 */
	private void copyBoard(Board board) {

		// Row loop
		for (int i = 0; i < 3; ++i) {

			// Column Loop
			for (int j = 0; j < 3; ++j) {

				// Set everything to empty
				this.board[i][j] = board.getPositionValue(i, j);
			}
		}

		// Copy the state of the board that was passed in.
		numberOfMoves = board.getNumberOfMoves();
		validMoves = new HashSet<Move>(board.getValidMoves());
	}

	/**
	 * Gets the value at the specified board position.
	 * 
	 * @param i
	 * @param j
	 * @return - The value (O, X, or EMPTY) at the board position.
	 */
	private int getPositionValue(int i, int j) {
		return board[i][j];
	}

	/**
	 * Gets the number of moves.
	 * 
	 * @return - Integer representing the number of moves
	 */
	public int getNumberOfMoves() {
		return numberOfMoves;
	}

	/**
	 * Returns a hash map of the current valid moves.
	 * 
	 * @return - Hash map containing the current valid moves
	 */
	public HashSet<Move> getValidMoves() {
		return validMoves;
	}

	/**
	 * Makes a move by changing the corresponding square on the board.
	 * 
	 * @param move
	 *            - the move to be played
	 * @param player
	 *            - ID of player (O or X)
	 * @return - True if move is valid, false otherwise.
	 */
	public boolean makeMove(Move move, int player) {

		// Check to see if the move is valid.
		if (!validMoves.contains(move)) {
			System.out.println("Move is not valid.");
			return false;
		}

		board[move.getX()][move.getY()] = player;
		validMoves.remove(move);
		++numberOfMoves;
		return true;
	}

	/**
	 * Makes a move on a new board instance and returns the new instance.
	 * 
	 * @param move
	 *            - the move to be played
	 * @param player
	 *            - the player making the move
	 * @return - the new board instance with the move that was played
	 */
	public Board testMove(Move move, int player) {

		Board testBoard = new Board(this);
		testBoard.makeMove(move, player);
		return testBoard;
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

	/**
	 * Immutable object class (no setters) to hold moves, which are (x,y) pairs,
	 * for safe hashing.
	 */
	public static final class Move {

		private Integer x;
		private Integer y;

		public Move(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Integer getX() {
			return x;
		}

		public Integer getY() {
			return y;
		}

		@Override
		public boolean equals(Object object) {
			return (object instanceof Move)
					&& (((Move) object).getX().equals(this.x))
					&& (((Move) object).getY().equals(this.y));
		}

		@Override
		public int hashCode() {
			return (10 * x.hashCode()) + y.hashCode();
		}

		@Override
		public String toString() {
			return "(" + x.toString() + ", " + y.toString() + ")";
		}

	}

}