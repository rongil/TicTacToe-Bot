package tictactoebot;

public abstract class Player {

	private int player;

	public Player(int player) {
		// Either X or O
		this.player = player;
	}

	/**
	 * Getter for the player ID (O or X)
	 * 
	 * @return - the ID of the player
	 */
	public int getPlayerID() {
		return player;
	}

	// Abstract method to get a move.
	// Necessary for every player...
	public abstract int[] getMove();

	public static class HumanPlayer extends Player {

		public HumanPlayer(int player) {
			super(player);
		}

		public int[] getMove() {

			return new int[1]; // Placeholder
		}

	}

	public static class ComputerPlayer extends Player {

		public ComputerPlayer(int player) {
			super(player);
		}

		public int[] getMove() {

			return new int[1]; // Placeholder
		}

	}

}
