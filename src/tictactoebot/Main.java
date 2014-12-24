package tictactoebot;

import java.util.Scanner;

public class Main {

	private static Scanner scanner;
	// Error Message
	public static final String INPUT_ERROR_MESSAGE = "Invalid Input.";

	public static void main(String[] args) {
		// Single scanner used to handle all future console input.
		scanner = new Scanner(System.in);
		// NOTE: Possible to improve by adding a GUI.
		GameManager manager = new GameManager();
		// Run the game until exit is called.
		boolean exit;
		do {
			exit = manager.startScreen();
			System.out.println();
		} while (!exit);

	}

	/**
	 * Verifies valid integer input and without a custom message.
	 * 
	 * @see Main#getNextIntegerInput(String) getNextIntegerInput(String)
	 */
	public static final int getNextIntegerInput() {

		while (!scanner.hasNextInt()) {
			System.out.println(INPUT_ERROR_MESSAGE);
			scanner.next();
		}

		return scanner.nextInt();

	}

	/**
	 * Loops to obtain the next valid integer input. Displays a custom message
	 * (provided as a parameter) on each failed attempt.
	 * 
	 * @param message
	 *            - the custom message to print upon failure
	 * @return - the next valid integer input
	 */
	public static final int getNextIntegerInput(String message) {

		while (!scanner.hasNextInt()) {
			System.out.println(INPUT_ERROR_MESSAGE);
			System.out.print(message);
			scanner.next();
		}

		return scanner.nextInt();

	}

}
