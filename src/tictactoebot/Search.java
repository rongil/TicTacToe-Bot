package tictactoebot;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import tictactoebot.Board.Move;

/**
 * Contains all implemented search methods. Runs iterative deepening and
 * implements the callable interface to allow for an easily timed out execution.
 * 
 * @author rongil
 *
 */
public class Search implements Callable<Move> {

	// Timeout
	public static final int TIMEOUT = 5; // Seconds

	// State variables
	private Move bestMove;
	private int currentMaxDepth;
	private Board originalBoard;
	private Player player;

	// Search types
	private static final int MINIMAX_ALPHA_BETA = 0;
	private static final int NEGAMAX_ALPHA_BETA = 1;

	// Actual search method used.
	// Can be made variable during runtime in the future.
	private static final int searchType = NEGAMAX_ALPHA_BETA;

	public Search(Board board, Player player) {
		this.originalBoard = board;
		this.player = player;

		currentMaxDepth = 1;
	}

	/**
	 * Main method called to start executing a search.
	 */
	@Override
	public Move call() throws TimeoutException {

		/*
		 * Note: Search time increases exponentially with depth. The modified
		 * timeout can be used to stop the program from starting a new search if
		 * more than that time has passed. Regardless, the search will be
		 * stopped once the actual timeout is reached.
		 */

		// Timeout Modifications (unused ones are commented out)
		// -----------------------------------------------------
		// long modifiedTimeout = TIMEOUT * 1000; // Regular Timeout
		double modifiedTimeout = Math.sqrt(TIMEOUT); // Square Root of Timeout

		// A conversion needs to be made to either seconds or milliseconds for
		// consistency. Here the conversion is made to milliseconds.
		modifiedTimeout *= 1000;

		long startTime = System.currentTimeMillis();

		switch (searchType) {
		case MINIMAX_ALPHA_BETA:
			System.out.println("Running Minimax w/ Alpha-Beta Pruning...");
			do {
				bestMove = minimaxAlphaBeta();
				++currentMaxDepth; // Search one level deeper next time
			} while ((System.currentTimeMillis() - startTime) < modifiedTimeout);
			break;
		case NEGAMAX_ALPHA_BETA:
			System.out.println("Running Negamax w/ Alpha-Beta Pruning...");
			do {
				bestMove = negamaxAlphaBeta();
				++currentMaxDepth; // Search one level deeper next time
			} while ((System.currentTimeMillis() - startTime) < modifiedTimeout);
			break;
		default:
			System.out.println("An invalid search method is being used.");
			System.exit(1);
		}

		return bestMove;

	}

	/**
	 * Getter for the best move found so far.
	 * 
	 * @return - the best move found
	 */
	public Move getBestMove() {
		return bestMove;
	}

	/**
	 * Method called to run MiniMax search with Alpha-Beta Pruning.
	 * 
	 * @return - the optimal move given the depth restriction
	 */
	private Move minimaxAlphaBeta() {

		int newValue;
		int maxValue = Integer.MIN_VALUE;
		for (Move move : originalBoard.getValidMoves()) {
			newValue = minimaxAlphaBeta(
					originalBoard.testMove(move, player.getPlayerID()),
					currentMaxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE,
					false);
			if (newValue > maxValue) {
				maxValue = newValue;
				bestMove = move;
			}
		}
		return bestMove;

	}

	/**
	 * Helper method for MiniMax with Alpha-Beta pruning that recursively calls
	 * itself and returns the heuristic value of a certain move. The move is
	 * implicitly passed in as a board which had the move already occur.
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
	private int minimaxAlphaBeta(Board board, int depth, int alpha, int beta,
			boolean maximizingPlayer) {

		int result = board.checkGameOver();
		if (result != Board.INCOMPLETE || depth == 0) {
			// Subtracts number of moves to favor faster wins and slower
			// losses. Since the number of moves will be at most 9, the
			// outcomes do not overlap.
			if (result == Board.TIE) {
				return 0 - board.getNumberOfMoves();
			} else if (result == player.getPlayerID()) {
				return 10 - board.getNumberOfMoves();
			} else if (result == player.getOtherPlayerID()) {
				return -10 - board.getNumberOfMoves();
			} else {
				// TODO: Static evaluation of incomplete state
			}
		}

		if (maximizingPlayer) {
			int newAlpha = Integer.MIN_VALUE;
			for (Move move : board.getValidMoves()) {
				newAlpha = Math.max(
						newAlpha,
						minimaxAlphaBeta(
								board.testMove(move, player.getPlayerID()),
								depth - 1, newAlpha, beta, false));
				if (beta <= newAlpha) {
					break; // Beta cutoff
				}
			}
			return newAlpha;

		} else {
			int newBeta = Integer.MAX_VALUE;
			for (Move move : board.getValidMoves()) {
				newBeta = Math
						.min(newBeta,
								minimaxAlphaBeta(
										board.testMove(move,
												player.getOtherPlayerID()),
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
	 * @return - the optimal move given the depth restriction
	 */
	private Move negamaxAlphaBeta() {

		int newValue;
		int bestValue = Integer.MIN_VALUE;
		Move bestMove = null;
		for (Move move : originalBoard.getValidMoves()) {
			newValue = negamaxAlphaBeta(
					originalBoard.testMove(move, player.getPlayerID()),
					currentMaxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE,
					player.getOtherPlayerID());
			if (newValue > bestValue) {
				bestValue = newValue;
				bestMove = move;
			}
		}
		return bestMove;

	}

	/**
	 * Helper method for NegaMax with Alpha-Beta pruning that recursively calls
	 * itself and returns the heuristic value of a certain move. The move is
	 * implicitly passed in as a board which had the move already occur.
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
	private int negamaxAlphaBeta(Board board, int depth, int alpha, int beta,
			int playerID) {

		int result = board.checkGameOver();
		if (result != Board.INCOMPLETE || depth == 0) {
			// Subtracts number of moves to favor faster wins and slower
			// losses. Since the number of moves will be at most 9, the
			// outcomes do not overlap.
			if (result == Board.TIE) {
				return 0 - board.getNumberOfMoves();
			} else if (result == player.getPlayerID()) {
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
					depth - 1, -beta, -alpha, player.getOtherPlayerID(playerID));
			bestValue = Math.max(bestValue, newValue);
			newAlpha = Math.max(newAlpha, newValue);
			if (newAlpha >= beta) {
				break;
			}
		}
		return bestValue;

	}

}
