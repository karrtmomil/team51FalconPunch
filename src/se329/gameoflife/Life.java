package se329.gameoflife;

import java.util.Random;

import android.content.Context;

public class Life {

	public static final int CELL_SIZE = 8;
	public static final int WIDTH = 320 / CELL_SIZE;
	public static final int HEIGHT = 480 / CELL_SIZE;

	private static final int[][] _lifeGrid = new int[HEIGHT][WIDTH];

	private Context _context;

	public Life(Context context) {
		this._context = context;
		initializeGrid();
	}

	public static int[][] getGrid() {
		return _lifeGrid;
	}

	public void initializeGrid() {
		resetGrid(_lifeGrid);

		_lifeGrid[8][(WIDTH / 2) - 1] = 1;
		_lifeGrid[8][(WIDTH / 2) + 1] = 1;
		_lifeGrid[9][(WIDTH / 2) - 1] = 1;
		_lifeGrid[9][(WIDTH / 2) + 1] = 1;
		_lifeGrid[10][(WIDTH / 2) - 1] = 1;
		_lifeGrid[10][(WIDTH / 2)] = 1;
		_lifeGrid[10][(WIDTH / 2) + 1] = 1;
	}

	public void generateNextGeneration() {
		int neighbours;
		int minimum = Integer.parseInt(PreferencesActivity
				.getMinimumVariable(this._context));
		int maximum = Integer.parseInt(PreferencesActivity
				.getMaximumVariable(this._context));
		int spawn = Integer.parseInt(PreferencesActivity
				.getSpawnVariable(this._context));

		int[][] nextGenerationLifeGrid = new int[HEIGHT][WIDTH];

		for (int h = 0; h < HEIGHT; h++) {
			for (int w = 0; w < WIDTH; w++) {
				neighbours = calculateNeighbours(h, w);

				if (_lifeGrid[h][w] != 0) {
					if ((neighbours >= minimum) && (neighbours <= maximum)) {
						nextGenerationLifeGrid[h][w] = neighbours;
					}
				} else {
					if (neighbours == spawn) {
						nextGenerationLifeGrid[h][w] = spawn;
					}
				}
			}
		}
		copyGrid(nextGenerationLifeGrid, _lifeGrid);
	}

	private void resetGrid(int[][] grid) {
		for (int h = 0; h < HEIGHT; h++) {
			for (int w = 0; w < WIDTH; w++) {
				grid[h][w] = 0;
			}
		}
	}

	private int calculateNeighbours(int y, int x) {
		int total = (_lifeGrid[y][x] != 0) ? -1 : 0;
		for (int h = -1; h <= +1; h++) {
			for (int w = -1; w <= +1; w++) {
				if (_lifeGrid[(HEIGHT + (y + h)) % HEIGHT][(WIDTH + (x + w))
						% WIDTH] != 0) {
					total++;
				}
			}
		}
		return total;
	}

	private void copyGrid(int[][] source, int[][] destination) {
		for (int h = 0; h < HEIGHT; h++) {
			for (int w = 0; w < WIDTH; w++) {
				destination[h][w] = source[h][w];
			}
		}
	}
	
	public boolean[][] getNextGen(boolean[][] prev, int h, int w) {
		boolean[][] ret = new boolean[h][w];
		int count = 0;
		//Evolving Corners
		//top left
		if(prev[h-1][w-1]) count++;
		if(prev[h-1][0]) count++;
		if(prev[h-1][1]) count++;
		if(prev[0][w-1]) count++;
		if(prev[0][1]) count++;
		if(prev[1][w-1]) count++;
		if(prev[1][0]) count++;
		if(prev[1][1]) count++;
		if((prev[0][0]&&count==2)||count==3) ret[0][0]=true;
		else ret[0][0]=false;
		//top right
		count = 0;
		if(prev[h-1][w-2]) count++;
		if(prev[h-1][w-1]) count++;
		if(prev[h-1][0]) count++;
		if(prev[0][w-2]) count++;
		if(prev[0][0]) count++;
		if(prev[1][w-2]) count++;
		if(prev[1][w-1]) count++;
		if(prev[1][0]) count++;
		if((prev[0][w-1]&&count==2)||count==3) ret[0][w-1]=true;
		else ret[0][w-1]=false;
		//bottom left
		count = 0;
		if(prev[h-2][w-1]) count++;
		if(prev[h-2][0]) count++;
		if(prev[h-2][1]) count++;
		if(prev[h-1][w-1]) count++;
		if(prev[h-1][1]) count++;
		if(prev[0][w-1]) count++;
		if(prev[0][0]) count++;
		if(prev[0][1]) count++;
		if((prev[h-1][0]&&count==2)||count==3) ret[h-1][0]=true;
		else ret[h-1][0]=false;
		//bottom right
		count = 0;
		if(prev[h-2][w-2]) count++;
		if(prev[h-2][w-1]) count++;
		if(prev[h-2][0]) count++;
		if(prev[h-1][w-2]) count++;
		if(prev[h-1][0]) count++;
		if(prev[0][w-2]) count++;
		if(prev[0][w-1]) count++;
		if(prev[0][0]) count++;
		if((prev[h-1][w-1]&&count==2)||count==3) ret[h-1][w-1]=true;
		else ret[h-1][w-1]=false;
		
		//Evolving Edges
		//top edge
		for(int i=1; i<w-1; i++){
			count = 0;
			if(prev[h-1][i-1]) count++;
			if(prev[h-1][i]) count++;
			if(prev[h-1][i+1]) count++;
			if(prev[0][i-1]) count++;
			if(prev[0][i+1]) count++;
			if(prev[1][i-1]) count++;
			if(prev[1][i]) count++;
			if(prev[1][i+1]) count++;
			if((prev[0][i]&&count==2)||count==3) ret[0][i]=true;
			else ret[0][i]=false;
		}
		//bottom edge
		for(int i=1; i<w-1; i++){
			count = 0;
			if(prev[h-2][i-1]) count++;
			if(prev[h-2][i]) count++;
			if(prev[h-2][i+1]) count++;
			if(prev[h-1][i-1]) count++;
			if(prev[h-1][i+1]) count++;
			if(prev[0][i-1]) count++;
			if(prev[0][i]) count++;
			if(prev[0][i+1]) count++;
			if((prev[h-1][i]&&count==2)||count==3) ret[h-1][i]=true;
			else ret[h-1][i]=false;
		}
		//left edge
		for(int i=1; i<h-1; i++){
			count = 0;
			if(prev[i-1][w-1]) count++;
			if(prev[i][w-1]) count++;
			if(prev[i+1][w-1]) count++;
			if(prev[i-1][0]) count++;
			if(prev[i+1][0]) count++;
			if(prev[i-1][1]) count++;
			if(prev[i][1]) count++;
			if(prev[i+1][1]) count++;
			if((prev[i][0]&&count==2)||count==3) ret[i][0]=true;
			else ret[i][0]=false;
		}
		//right edge
		for(int i=1; i<h-1; i++){
			count = 0;
			if(prev[i-1][w-2]) count++;
			if(prev[i][w-2]) count++;
			if(prev[i+1][w-2]) count++;
			if(prev[i-1][w-1]) count++;
			if(prev[i+1][w-1]) count++;
			if(prev[i-1][0]) count++;
			if(prev[i][0]) count++;
			if(prev[i+1][0]) count++;
			if((prev[i][w-1]&&count==2)||count==3) ret[i][w-1]=true;
			else ret[i][w-1]=false;
		}
		
		//Evolving center area
		for(int i=1; i<h-1; i++){
			for(int j=1; j<w-1; j++){
				count = 0;
				if(prev[i-1][j-1]) count++;
				if(prev[i-1][j]) count++;
				if(prev[i-1][j+1]) count++;
				if(prev[i][j-1]) count++;
				if(prev[i][j+1]) count++;
				if(prev[i+1][j-1]) count++;
				if(prev[i+1][j]) count++;
				if(prev[i+1][j+1]) count++;
				if((prev[i][j]&&count==2)||count==3) ret[i][j]=true;
				else ret[i][j]=false;
			}
		}
		return ret;
	}

	public boolean[][] genRandom(int h, int w) {
		boolean[][] ret = new boolean[h][w];
		Random ran = new Random();
		for(int i=0; i<h; i++){
			for(int j=0; j<w; j++){
				ret[i][j] = ran.nextBoolean();
			}
		}
		return ret;
	}
}
