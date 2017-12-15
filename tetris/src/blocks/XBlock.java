package blocks;

import tetris.TetrisGrid;

public class XBlock extends TetrisBlock
{
	public XBlock(TetrisGrid tetrisgrid)
	{
		super(tetrisgrid);
		blockSize = new int[]{1, 1, 1, 1};
		blockShape = new int[][] {
			{0,0},
			{-1,0},
			{0,-1},
			{1,0},
			{0,1}
			
		};
	}
	
	public void rotate()
	{		
	}
}
