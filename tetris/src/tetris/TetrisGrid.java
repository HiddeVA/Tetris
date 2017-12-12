package tetris;

import javafx.scene.canvas.Canvas;

public class TetrisGrid extends Canvas
{
	private int cols = 10;
	private int rows = 17;
	int score;
	boolean[][] grid;
	GridDrawer gd;
	
	public TetrisGrid()
	{
		super(240, 360);
		grid = new boolean[this.cols][this.rows];
		gd = new GridDrawer(this.getGraphicsContext2D(), this.cols, this.rows);
	}
	
	public TetrisGrid(int rows, int cols)
	{
		super(240, 360);
		this.rows = rows;
		this.cols = cols;
		grid = new boolean[cols][rows];
		gd = new GridDrawer(this.getGraphicsContext2D(), cols, rows);
	}
	
	public GridDrawer getGridDrawer()
	{
		return this.gd;
	}

	public int getGridWidth()
	{
		return this.cols;
	}
	
	public int getGridHeight()
	{
		return this.rows - 1;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public boolean isNotOpen(int x, int y)
	{
		if (x < 0 || x >= cols || y < 0 || y >= rows)
			return true;
		return grid[x][y];
	}
	
	public boolean placeBlock(TetrisBlock block)
	{
		int x = block.getX();
		int y = block.getY();
		boolean result = true;
		for (int[] pos : block.getOutline()) {
			if (y + pos[0] < 0) {result = false; continue;}
			grid[x + pos[1]][y + pos[0]] = true;
		}
		if (!result) return false;
		
		int top = block.getBlockSize()[0];
		int bot = block.getBlockSize()[3];
		boolean fullRow;
		int fullRows = 0;
		for (int i = y + bot; i >= Integer.max(y - top, 0); i--) {
			fullRow = true;
			for (int j = 0; j < cols; j++) {
				if (!grid[j][i]) {
					fullRow = false;
					break;
				}
			}
			if (fullRow) {
				fullRows++;
				removeRow(i);
				i++;
				y++;
			}
		}
		switch (fullRows) {
		case 1: score += 1;
			break;
		case 2: score += 3;
			break;
		case 3: score += 7;
			break;
		case 4: score += 15;
			break;
		default:		
		}
		return result;
	}
	
	private void removeRow(int row)
	{
		for (int k = row; k > 1; k--) {
			for (int j = 0; j < cols; j++) {
				if (grid[j][k-1]) {
					grid[j][k] = true;
					gd.clearBlock(j, k - 1);
					grid[j][k-1] = false;
					gd.drawBlock(j, k);
				}
				else {
					grid[j][k] = false;
					gd.clearBlock(j,k);
				}
			}
		}
	}
	
	public void clearBlock(int x, int y)
	{
		gd.clearBlock(x, y);
	}
	
	public void drawBlock(int x, int y)
	{
		gd.drawBlock(x, y);
	}
	
	public void reset()
	{
		for (int i = 0; i < cols; i ++) {
			for (int j = 0; j < rows; j++) {
				grid[i][j] = false;
			}
		}
		gd.drawGrid();
		score = 0;
	}
}
