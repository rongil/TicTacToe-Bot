package tictactoebot;

import java.util.logging.Level;
import java.util.logging.Logger;

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

	// Error Message
	private static final String INPUT_ERROR_MESSAGE = "Invalid Input.";

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

		int selection = 0;
		// Loop through user input until valid.
		try {
			// Wait for valid user input.
			// Make sure it is an integer value.
			while (!Main.scanner.hasNextInt()) {
				System.out.println(INPUT_ERROR_MESSAGE);
				Main.scanner.next();
			}
			selection = Main.scanner.nextInt();

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.toString());
			System.out
					.println("The program encountered an error while processing your input!");
			return false;
		}

		switch (selection) {
		case NEW_GAME_OPTION:
			newGameScreen();
			break;
		case EXIT_OPTION:
			System.exit(0);
			return true;
		default:
			System.out.println(INPUT_ERROR_MESSAGE);
			return false;
		}

		return false;

	}

	/**
	 * Screen to select game type.
	 */
	private void newGameScreen() {
		// Game type options.
		System.out.println("What type of game would you like to play/watch?");
		System.out.println(Integer.toString(HUMAN_VS_HUMAN_OPTION) + ") "
				+ HUMAN_VS_HUMAN);
		System.out.println(Integer.toString(COMPUTER_VS_HUMAN_OPTION) + ") "
				+ COMPUTER_VS_HUMAN);
		System.out.println(Integer.toString(COMPUTER_VS_COMPUTER_OPTION) + ") "
				+ COMPUTER_VS_COMPUTER);

		int selection;
		// Loop through user input until valid.
		try {
			// Wait for valid user input.
			// Make sure it is an integer value.
			while (!Main.scanner.hasNextInt()) {
				System.out.println(INPUT_ERROR_MESSAGE);
				Main.scanner.next();
			}
			selection = Main.scanner.nextInt();

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.toString());
			System.out
					.println("The program encountered an error while processing your input!");
			return;
		}

		// NOTE: Uses fall through since all cases take the same action with the
		// argument being selection.
		switch (selection) {
		case HUMAN_VS_HUMAN_OPTION:
		case COMPUTER_VS_HUMAN_OPTION:
		case COMPUTER_VS_COMPUTER_OPTION:
			newGame(selection);
			break;
		default:
			System.out.println(INPUT_ERROR_MESSAGE);
		}

	}

	/**
	 * Sets up everything to start a game.
	 * 
	 * @param gameType
	 *            - Game Type: Human versus Human, Computer versus Human,
	 *            Computer versus Computer
	 */
	private void newGame(int gameType) {

		Board board = new Board();
	}

	/**
	 * Starts a game between the two selected players.
	 * 
	 * @param playerOne
	 *            - First player type.
	 * @param playerTwo
	 *            - Second player type.
	 * @return - Winner ID
	 */
	private int startGame(int playerOne, int playerTwo) {

		return 0;
	}
}