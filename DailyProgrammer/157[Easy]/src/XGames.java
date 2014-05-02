/**
 * Created by Victor Kwak on 4/7/14.
 */
class XGames {

    static String[][] grid = new String[3][3];
    static String[] check = new String[3];

    private static void makeEmptyGrid() {
	    for (int i = 0; i < grid.length; i++) {
		    for (int j = 0; j < grid[i].length; j++) {
			    grid[i][j] = " - ";
			}
		}
	}

	private static void printGrid() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				System.out.print(grid[i][j]);
			}
			System.out.println("");
		}
	}

	private static void checkWin() {

		boolean win = false;
		for (int i = 0; i < check.length; i++) {
			check[i] = grid[i][0];
			if (check[0].equals(check[1]) && check[0].equals(check[2])) {
				win = true;
			}
		}

		for (int i = 0; i < check.length; i++) {
			check[i] = grid[0][i];
			if (check[0].equals(check[1]) && check[0].equals(check[2])) {
				win = true;
			}
		}
	}

	private static void setGrid(int x, int y) {
		int cartesianX = 0;
		switch (y) {
			case 0:
				cartesianX = 2;
				break;
			case 1:
				cartesianX = 1;
				break;
			case 2:
				cartesianX = 0;
		}

		grid[cartesianX][x] = " X ";
	}

	public static void main(String[] args) {
		makeEmptyGrid();
		printGrid();
		System.out.println("");
		setGrid(2, 1);
		printGrid();//save?
	}
}
