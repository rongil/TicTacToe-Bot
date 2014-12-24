package tictactoebot;

import java.util.logging.Logger;

import tictactoebot.Board.Move;

public abstract class Player {

	protected final Logger logger = Logger.getLogger(getClass().getName());

	private String player;
	private int playerID;
	private String playerType;

	public Player(int playerID, String playerType) {

		this.playerID = playerID;
		if (playerID == Board.X) {
			this.player = "X";
		} else {
			this.player = "O";
		}
		this.playerType = playerType;
	}

	/**
	 * Returns the player (O or X)
	 * 
	 * @return - String representing the player
	 */
	public String getPlayer() {
		return player;
	}

	/**
	 * Returns the player ID (integer representing O or X)
	 * 
	 * @return - integer representing O or X
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Returns the opponent's ID (integer representing O or X)
	 * 
	 * @return - integer representing O or X
	 */
	public int getOtherPlayerID() {
		if (this.playerID == Board.X) {
			return Board.O;
		} else {
			return Board.X;
		}
	}

	/**
	 * Returns the ID of the opposite player (integer representing O or X)
	 * 
	 * @param playerID
	 *            - the actual player's ID
	 * @return - the opposing player's ID
	 */
	public int getOtherPlayerID(int playerID) {
		if (playerID == Board.X) {
			return Board.O;
		} else {
			return Board.X;
		}
	}

	/**
	 * Returns the player type (Human or Computer)
	 * 
	 * @return - String stating the player type
	 */
	public String getPlayerType() {
		return playerType;
	}

	public void printPlayerTurn() {
		System.out.println(playerType + " Player " + player + "'s Turn.");
	}

	// Abstract method to get a move and return coordinates.
	// Necessary for every player...
	public abstract Move getMove(Board board);

	public static class HumanPlayer extends Player {

		public HumanPlayer(int player) {
			super(player, "Human");
		}

		public Move getMove(Board board) {

			final String X_COORDINATE_MESSAGE = "Please type the x coordinate of the next move: ";
			final String Y_COORDINATE_MESSAGE = "Please type the y coordinate of the next move: ";

			// Ask the user for coordinates for his next move.
			// Subtracts 1 to make coordinates 1-indexed for the user.
			System.out.println();
			System.out.print(X_COORDINATE_MESSAGE);
			int x = Main.getNextIntegerInput(X_COORDINATE_MESSAGE) - 1;
			System.out.print(Y_COORDINATE_MESSAGE);
			int y = Main.getNextIntegerInput(Y_COORDINATE_MESSAGE) - 1;
			return new Move(x, y);
		}
	}

	public static class ComputerPlayer extends Player {

		// Search types
		private static final int MINIMAX_ALPHA_BETA = 0;
		private static final int NEGAMAX_ALPHA_BETA = 1;

		// Actual search method used.
		// Can be made variable during runtime in the future.
		private static final int searchType = NEGAMAX_ALPHA_BETA;

		public ComputerPlayer(int player) {
			super(player, "Computer");
		}

		public Move getMove(Board board) {

			Move move = null;
			switch (searchType) {
			case MINIMAX_ALPHA_BETA:
				System.out.println("Running Minimax w/ Alpha-Beta Pruning...");
				move = minimaxAlphaBeta(board);
				break;
			case NEGAMAX_ALPHA_BETA:
				System.out.println("Running Negamax w/ Alpha-Beta Pruning...");
				move = negamaxAlphaBeta(board);
				break;
			default:
				System.out.println("An invalid search method is being used.");
				System.exit(1);
			}

			return move;
		}

		/**
		 * Method called to run MiniMax search with Alpha-Beta Pruning.
		 * 
		 * @param board
		 *            - the current board
		 * @return - the optimal move given the depth restriction
		 */
		private Move minimaxAlphaBeta(Board board) {

			int depth = 10;

			int newValue;
			int maxValue = Integer.MIN_VALUE;
			Move bestMove = null;
			for (Move move : board.getValidMoves()) {
				newValue = minimaxAlphaBeta(
						board.testMove(move, this.getPlayerID()), depth - 1,
						Integer.MIN_VALUE, Integer.MAX_VALUE, true);
				if (newValue > maxValue) {
					maxValue = newValue;
					bestMove = move;
				}
			}
			return bestMove;

		}

		/**
		 * Helper method for MiniMax with Alpha-Beta pruning that recursively
		 * calls itself and returns the heuristic value of a certain move. The
		 * move is implicitly passed in as a board which had the move already
		 * occur.
		 * 
		 * @param board
		 *            - the board with the move being analyzed
		 * @param depth
		 *            - current depth of the search
		 * @param alpha
		 * @param beta
		 * @param maximizingPlayer
		 *            - the person who's move it is on this level
		 * @return - heuristic value of the move
		 */
		private int minimaxAlphaBeta(Board board, int depth, int alpha,
				int beta, boolean maximizingPlayer) {

			int result = board.checkGameOver();
			if (result != Board.INCOMPLETE || depth == 0) {
				// Subtracts number of moves to favor faster wins and slower
				// losses. Since the number of moves will be at most 9, the
				// outcomes do not overlap.
				if (result == Board.TIE) {
					return 0 - board.getNumberOfMoves();
				} else if (result == this.getPlayerID()) {
					return 10 - board.getNumberOfMoves();
				} else {
					return -10 - board.getNumberOfMoves();
				}
			}

			if (maximizingPlayer) {
				int newAlpha = Integer.MIN_VALUE;
				for (Move move : board.getValidMoves()) {
					newAlpha = Math.max(
							newAlpha,
							minimaxAlphaBeta(
									board.testMove(move, this.getPlayerID()),
									depth - 1, newAlpha, beta, false));
					if (beta <= newAlpha) {
						break; // Beta cutoff
					}
				}
				return newAlpha;

			} else {
				int newBeta = Integer.MAX_VALUE;
				for (Move move : board.getValidMoves()) {
					newBeta = Math.min(
							newBeta,
							minimaxAlphaBeta(
									board.testMove(move,
											this.getOtherPlayerID()),
									depth - 1, alpha, newBeta, true));
					if (newBeta <= alpha) {
						break; // Alpha cutoff
					}
				}
				return newBeta;

			}

		}

		/**
		 * Method called to run NegaMax with Alpha-Beta pruning.
		 * 
		 * @param board
		 *            - the current board
		 * @return - the optimal move given the depth restriction
		 */
		private Move negamaxAlphaBeta(Board board) {

			int depth = 10;

			int newValue;
			int bestValue = Integer.MIN_VALUE;
			Move bestMove = null;
			for (Move move : board.getValidMoves()) {
				newValue = negamaxAlphaBeta(board, depth - 1,
						Integer.MIN_VALUE, Integer.MAX_VALUE,
						this.getPlayerID());
				if (newValue > bestValue) {
					bestValue = newValue;
					bestMove = move;
				}
			}
			return bestMove;

		}

		/**
		 * Helper method for NegaMax with Alpha-Beta pruning that recursively
		 * calls itself and returns the heuristic value of a certain move. The
		 * move is implicitly passed in as a board which had the move already
		 * occur.
		 * 
		 * @param board
		 *            - the board with the move being analyzed
		 * @param depth
		 *            - current depth of the search
		 * @param alpha
		 * @param beta
		 * @param playerID
		 *            - the person who's move it is on this level
		 * @return - heuristic value of the move
		 */
		private int negamaxAlphaBeta(Board board, int depth, int alpha,
				int beta, int playerID) {

			int result = board.checkGameOver();
			if (result != Board.INCOMPLETE || depth == 0) {
				// Subtracts number of moves to favor faster wins and slower
				// losses. Since the number of moves will be at most 9, the
				// outcomes do not overlap.
				if (result == Board.TIE) {
					return 0 - board.getNumberOfMoves();
				} else if (result == this.getPlayerID()) {
					return 10 - board.getNumberOfMoves();
				} else {
					return -10 - board.getNumberOfMoves();
				}
			}

			int bestValue = Integer.MIN_VALUE;
			int newAlpha = alpha;
			int newValue;
			for (Move move : board.getValidMoves()) {
				newValue = -negamaxAlphaBeta(board.testMove(move, playerID),
						depth - 1, -beta, -alpha,
						this.getOtherPlayerID(playerID));
				bestValue = Math.max(bestValue, newValue);
				newAlpha = Math.max(newAlpha, newValue);
				if (newAlpha >= beta) {
					break;
				}
			}
			return bestValue;

		}
	}

}
