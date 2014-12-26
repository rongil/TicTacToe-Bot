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
	// Initial Depth
	private static final int INITIAL_DEPTH = 3;

	// Search types
	private static final int MINIMAX_ALPHA_BETA = 0;
	private static final int NEGAMAX_ALPHA_BETA = 1;

	// Actual search method used.
	// Can be made variable during runtime in the future.
	private static final int searchType = NEGAMAX_ALPHA_BETA;

	// State variables
	private Move bestMove;
	private int currentMaxDepth;
	private Board originalBoard;
	private Player player;

	/**
	 * Initializes a new search by setting the board, player, and starting
	 * depth.
	 * 
	 * @param board
	 *            - the current board
	 * @param player
	 *            - the player searching for a move
	 */
	public Search(Board board, Player player) {
		this.originalBoard = board;
		this.player = player;

		currentMaxDepth = INITIAL_DEPTH;
	}

	/**
	 * Main method called to start executing a search.
	 */
	@Override
	public Move call() throws TimeoutException {

		/*
		 * Note: Search time increases exponentially with depth. The modified
		 * timeout can be used to stop the program from starting a new search if
		 * more than that time has passed (faster return). Regardless, the
		 * search will be stopped once the actual timeout is reached.
		 */

		/*
		 * --------------------------------------------------------------------
		 * Timeout Modifications (unused ones are commented out)
		 * --------------------------------------------------------------------
		 */
		// long modifiedTimeout = TIMEOUT * 1000; // Regular Timeout
		double modifiedTimeout = Math.sqrt(TIMEOUT); // Square Root of Timeout

		// A conversion needs to be made to either seconds or milliseconds for
		// consistency. Here the conversion is made to milliseconds.
		modifiedTimeout *= 1000;

		long startTime = System.currentTimeMillis();

		/*
		 * --------------------------------------------------------------------
		 * Search Switch (and Loops)
		 * --------------------------------------------------------------------
		 * All searches loop until modifiedTimeout time has passed or they are
		 * forced to stop by the caller because the thread exceeded the set
		 * timeout.
		 */
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
	 * Calculates the heuristic value of a game state.
	 * 
	 * @param board
	 *            - the board being analyzed
	 * @param playerID
	 *            - the player which the state is being analyzed in respect to
	 * @return - the heuristic value
	 */
	private int calculateHeuristic(Board board, int playerID, int result) {

		/*
		 * --------------------------------------------------------------------
		 * End game conditions
		 * --------------------------------------------------------------------
		 * Adds/Subtracts the number of moves to favor faster wins and slower
		 * losses. Since the number of moves will be at most 9, the outcomes do
		 * not overlap.
		 */
		if (result == Board.TIE) {
			return 0;
		} else if (result == playerID) {
			return 100 - board.getNumberOfMoves(); // Faster win (less moves)
		} else if (result == player.getOtherPlayerID(playerID)) {
			return -100 + board.getNumberOfMoves(); // Slower loss (more moves)
		}

		/*
		 * --------------------------------------------------------------------
		 * Incomplete game conditions (maximum depth of current search reached)
		 * --------------------------------------------------------------------
		 */
		int heuristicValue = 0;

		// Prefer corners
		heuristicValue += evaluate(board.getPositionValue(0, 0), playerID, 2);
		heuristicValue += evaluate(board.getPositionValue(0, 2), playerID, 2);
		heuristicValue += evaluate(board.getPositionValue(2, 0), playerID, 2);
		heuristicValue += evaluate(board.getPositionValue(2, 2), playerID, 2);
		// Prefer center
		heuristicValue += evaluate(board.getPositionValue(1, 1), playerID, 10);

		return heuristicValue;

	}

	/**
	 * Used in heuristic calculation to check who is in control of a square and
	 * thereby assign a value to it.
	 * 
	 * @param square
	 *            - the value of the square being analyzed
	 * @param playerID
	 *            - the player
	 * @param value
	 *            - the value to assign
	 * @return - heuristic value depending on owner of the square
	 */
	private int evaluate(int square, int playerID, int value) {
		if (square == playerID) {
			return value;
		} else if (square == player.getOtherPlayerID(playerID)) {
			return -value;
		} else { // Empty square case
			return 0;
		}
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
			int playerID = maximizingPlayer ? player.getPlayerID() : player
					.getOtherPlayerID();
			return calculateHeuristic(board, playerID, result);
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
			newValue = -negamaxAlphaBeta(
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

		// Static Evaluation
		int result = board.checkGameOver();
		if (result != Board.INCOMPLETE || depth == 0) {
			return calculateHeuristic(board, playerID, result);
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
