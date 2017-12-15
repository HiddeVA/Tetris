package blocks;

import tetris.TetrisGrid;

public class SquareBlock extends TetrisBlock 
{
	public SquareBlock(TetrisGrid tetrisgrid)
	{
		super(tetrisgrid);
		blockSize = new int[]{1, 1, 0, 0};
		blockShape = new int[][] {{-1, 0},{-1, -1},{0,0},{0,-1}};
	}
	
	public void rotate()
	{
	}
}
