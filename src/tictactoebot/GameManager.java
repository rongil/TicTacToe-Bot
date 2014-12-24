package tictactoebot;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import tictactoebot.Board.Move;
import tictactoebot.Player.ComputerPlayer;
import tictactoebot.Player.HumanPlayer;

public class GameManager {

	private final Logger logger = Logger.getLogger(getClass().getName());

	// Menu Option Names
	private static final String NEW_GAME = "New Game";
	private static final String EXIT = "Exit";
	// Menu Option Numbers
	private static final int NEW_GAME_OPTION = 1;
	private static final int EXIT_OPTION = 2;

	// Game Type Names
	private static final String HUMAN_VS_HUMAN = "Human vs. Human";
	private static final String COMPUTER_VS_HUMAN = "Computer vs. Human";
	private static final String COMPUTER_VS_COMPUTER = "Computer vs. Computer";
	// Game Type Numbers
	private static final int HUMAN_VS_HUMAN_OPTION = 1;
	private static final int COMPUTER_VS_HUMAN_OPTION = 2;
	private static final int COMPUTER_VS_COMPUTER_OPTION = 3;

	public GameManager() {
		logger.log(Level.INFO, "Game manager created successfully.");
	}

	/**
	 * The main application screen.
	 * 
	 * @return - True only if exit is requested.
	 */
	public boolean startScreen() {
		// Main screen list.
		System.out.println("Select an option:");
		System.out.println(Integer.toString(NEW_GAME_OPTION) + ") " + NEW_GAME);
		System.out.println(Integer.toString(EXIT_OPTION) + ") " + EXIT);

		int selection = Main.getNextIntegerInput();
		switch (selection) {
		case NEW_GAME_OPTION:
			newGameScreen();
			break;
		case EXIT_OPTION:
			return true;
		default:
			System.out.println(Main.INPUT_ERROR_MESSAGE);
			return false;
		}

		return false;

	}

	/**
	 * Screen to select game type.
	 */
	private void newGameScreen() {

		// Game type options.
		System.out.println();
		System.out.println("What type of game would you like to play/watch?");
		System.out.println(Integer.toString(HUMAN_VS_HUMAN_OPTION) + ") "
				+ HUMAN_VS_HUMAN);
		System.out.println(Integer.toString(COMPUTER_VS_HUMAN_OPTION) + ") "
				+ COMPUTER_VS_HUMAN);
		System.out.println(Integer.toString(COMPUTER_VS_COMPUTER_OPTION) + ") "
				+ COMPUTER_VS_COMPUTER);

		int selection = Main.getNextIntegerInput();
		// Passes the two player types.
		switch (selection) {
		case HUMAN_VS_HUMAN_OPTION:
			playGame(new HumanPlayer(Board.X), new HumanPlayer(Board.O));
			break;
		case COMPUTER_VS_HUMAN_OPTION:
			playGame(new ComputerPlayer(Board.X), new HumanPlayer(Board.O));
			break;
		case COMPUTER_VS_COMPUTER_OPTION:
			playGame(new ComputerPlayer(Board.X), new ComputerPlayer(Board.O));
			break;
		default:
			System.out.println(Main.INPUT_ERROR_MESSAGE);
		}

	}

	/**
	 * Starts a game.
	 * 
	 * @param gameType
	 *            - Game Type: Human versus Human, Computer versus Human,
	 *            Computer versus Computer
	 */
	private void playGame(Player playerOne, Player playerTwo) {

		// Create a new board and display it.
		Board board = new Board();
		board.showBoard();

		// Randomly choose the starting player.
		Random random = new Random();
		int turn = random.nextInt(2); // Chooses 0 or 1

		boolean validMove;
		Move coordinates;
		int result;
		do {
			switch (turn) {
			case 0:
				// Loop until a valid move is provided.
				do {
					playerOne.printPlayerTurn();
					coordinates = playerOne.getMove(board);
					validMove = board.makeMove(coordinates,
							playerOne.getPlayerID());
				} while (!validMove);
				break;
			case 1:
				// Loop until a valid move is provided.
				do {
					playerTwo.printPlayerTurn();
					coordinates = playerTwo.getMove(board);
					validMove = board.makeMove(coordinates,
							playerTwo.getPlayerID());
				} while (!validMove);
				break;
			}

			// Switch turns.
			turn = (turn + 1) % 2;
			// Current result.
			result = board.checkGameOver();
			// Show resulting board.
			board.showBoard();

		} while (result == Board.INCOMPLETE);

		System.out.println();
		switch (result) {
		case Board.TIE:
			System.out.println("It is a tie!");
			break;
		case Board.X:
			System.out.println("Player X wins!");
			break;
		case Board.O:
			System.out.println("Player O wins!");
			break;
		default:
			System.out.println("An error has occurred!");
			break;
		}

		// Add blank line...
		System.out.println();

	}
}