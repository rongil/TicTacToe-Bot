package tictactoebot;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import tictactoebot.Board.Move;

/**
 * Abstract superclass for all player types. Implements helpful methods
 * pertinent to players and places a requirement for players to have certain
 * methods.
 * 
 * @author rongil
 *
 */
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

	/**
	 * Implements methods used for a human player.
	 * 
	 * @author rongil
	 *
	 */
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

	/**
	 * Implements methods used for a computer player. Runs a thread with a
	 * timeout in order to get a move.
	 * 
	 * @author rongil
	 *
	 */
	public static class ComputerPlayer extends Player {

		private ExecutorService executor;

		public ComputerPlayer(int player) {
			super(player, "Computer");
			// The executor to run the thread for the optimal move search.
			executor = Executors.newSingleThreadExecutor();
		}

		/**
		 * Runs a thread to search for the best possible move given a time
		 * constraint (set by Search.TIMEOUT) for searching.
		 * 
		 * @param - The current board
		 * @return - The optimal move found given the time constraint.
		 */
		public Move getMove(Board board) {

			// Creates new search task (which implements iterative deepening)
			Search search = new Search(board, this);
			Future<Move> future = executor.submit(search);

			Move bestMove;
			try {
				bestMove = future.get(Search.TIMEOUT, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				bestMove = search.getBestMove();
			} catch (InterruptedException e) {
				logger.log(Level.WARNING,
						"The thread was interrupted.\n" + e.toString());
				bestMove = search.getBestMove();
			} catch (ExecutionException e) {
				logger.log(Level.WARNING,
						"There was an error in the execution.\n" + e.toString());
				bestMove = search.getBestMove();
			}

			return bestMove;
		}
	}
}
