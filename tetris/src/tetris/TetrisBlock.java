package tetris;

import tetris.Tetris.GameState;

public abstract class TetrisBlock
{
	protected int xPos;
	protected int yPos;
	protected TetrisGrid grid;
	protected int[] blockSize = new int[4]; //top, right, left, bottom
	protected int[][] blockShape;
	
	public TetrisBlock(TetrisGrid tetrisgrid)
	{
		this.grid = tetrisgrid;
		xPos = grid.getWidth()/2;
		yPos = 0;
	}
	
	public GameState drop()
	{
		if (dropavailable()) {
			clearBlock();
			yPos++;
			drawBlock();
			return GameState.CONTINUE;
		}
		else {
			if (grid.placeBlock(this))
				return GameState.NEWBLOCK;
			else {
				drawBlock();
				return GameState.GAMEOVER;
			}
		}
	}
	
	private boolean dropavailable()
	{
		boolean result = true;
		for (int[] pos : blockShape) {
			if (grid.isNotOpen(xPos + pos[1], yPos + pos[0] + 1))
				result = false;
		}
		return result;
	}
	
	public int getX()
	{
		return xPos;
	}
	
	public int getY()
	{
		return yPos;
	}
	
	public int[][] getOutline()
	{
		return this.blockShape;
	}
	
	public int[] getBlockSize()
	{
		return this.blockSize;
	}
	
	protected void clearBlock()
	{
		for(int[] coords : blockShape) {
			grid.clearBlock(xPos + coords[1], yPos + coords[0]);
		}
	}
	
	protected void drawBlock()
	{
		for(int[] coords : blockShape) {
			grid.drawBlock(xPos + coords[1], yPos + coords[0]);
		}
	}
	
	public void moveRight()
	{
		if (xPos + blockSize[1] > grid.getWidth())
			return;
		for (int[] pos : blockShape) {
			if (grid.isNotOpen(xPos + pos[1] + 1, yPos + pos[0]))
				return;
		}
		clearBlock();
		xPos++;
		drawBlock();
	}
	
	public void moveLeft()
	{
		if (xPos - blockSize[2] < 0) return;
		for (int[] pos : blockShape) {
			if (grid.isNotOpen(xPos + pos[1] - 1, yPos + pos[0]))
				return;
		}
		clearBlock();
		xPos--;
		drawBlock();
	}
	
	public void rotate()
	{
		if (xPos < blockSize[3] || xPos > grid.getWidth() - blockSize[0] - 1 || yPos < blockSize[2] - 1) return;
		clearBlock();
		for (int[] pos : blockShape) {
			int tmp = pos[1];
			pos[1] = -pos[0];
			pos[0] = tmp;
		}
		drawBlock();
		fixBlockSize();
	}
	
	protected void fixBlockSize()
	{
		int tmp = blockSize[0];
		blockSize[0] = blockSize[2];
		blockSize[2] = blockSize[3];
		blockSize[3] = blockSize[1];
		blockSize[1] = tmp;
	}
}
