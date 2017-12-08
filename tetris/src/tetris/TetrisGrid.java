package tetris;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TetrisGrid
{
	int score;
	GraphicsContext gc;
	boolean[][] grid;
	double size = 20;
	double gap = 1;
	int cols = 10;
	int rows = 17;
	
	public TetrisGrid(GraphicsContext gc)
	{
		this.gc = gc;
		grid = new boolean[this.cols][this.rows];
	}
	
	public TetrisGrid(GraphicsContext gc, int rows, int cols)
	{
		this.gc = gc;
		grid = new boolean[cols][rows];
	}
	
	public int getWidth()
	{
		return this.cols;
	}
	
	public int getHeight()
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
	
	public void drawGrid()
	{
		score = 0;
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, (size + gap) * (cols - 1), (size + gap) * (rows - 1));
		gc.setFill(Color.BLACK);
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[i].length; j++)
			{
				gc.fillRect(i * (size + gap), j * (size + gap), size, size);
			}
		}
	}
	
	public void drawBlock(int x, int y)
	{
		if (x < 0 || y < 0 || x > cols || y > rows) return;
		gc.setFill(Color.YELLOW);
		gc.fillRect(x * (size + gap), y * (size + gap), size, size);
	}
	
	public void clearBlock(int x, int y)
	{
		if (x < 0 || y < 0 || x > cols || y > rows) return;
		gc.setFill(Color.BLACK);
		gc.fillRect(x * (size + gap), y * (size + gap), size, size);
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
					clearBlock(j, k - 1);
					grid[j][k-1] = false;
					drawBlock(j, k);
				}
				else {
					grid[j][k] = false;
					clearBlock(j,k);
				}
			}
		}
	}
	
	public void reset()
	{
		for (int i = 0; i < cols; i ++) {
			for (int j = 0; j < rows; j++) {
				grid[i][j] = false;
			}
		}
		drawGrid();
	}
}
