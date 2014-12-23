package tictactoebot;

public abstract class Player {

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
	 * Returns the player type (Human or Computer)
	 * 
	 * @return - String stating the player type
	 */
	public String getPlayerType() {
		return playerType;
	}

	// Abstract method to get a move and return coordinates.
	// Necessary for every player...
	public abstract int[] getMove();

	public static class HumanPlayer extends Player {

		public HumanPlayer(int player) {
			super(player, "Human");
		}

		public int[] getMove() {

			final String X_COORDINATE_MESSAGE = "Please type the x coordinate of the next move: ";
			final String Y_COORDINATE_MESSAGE = "Please type the y coordinate of the next move: ";

			int[] coordinates = new int[2];
			// Ask the user for coordinates for his next move.
			// Subtracts 1 to make coordinates 1-indexed for the user.
			System.out.println();
			System.out.println(this.getPlayerType() + " Player "
					+ this.getPlayer() + "'s Turn.");
			System.out.print(X_COORDINATE_MESSAGE);
			coordinates[0] = Main.getNextIntegerInput(X_COORDINATE_MESSAGE) - 1;
			System.out.print(Y_COORDINATE_MESSAGE);
			coordinates[1] = Main.getNextIntegerInput(Y_COORDINATE_MESSAGE) - 1;
			return coordinates;
		}
	}

	public static class ComputerPlayer extends Player {

		public ComputerPlayer(int player) {
			super(player, "Computer");
		}

		public int[] getMove() {

			return new int[2]; // Placeholder
		}

	}

}
