package tictactoebot;

import java.util.Scanner;

public class Main {

	public static Scanner scanner;

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

}
