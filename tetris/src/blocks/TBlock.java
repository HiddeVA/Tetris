package blocks;

import tetris.TetrisGrid;

public class TBlock extends TetrisBlock
{
	public TBlock(TetrisGrid tetrisgrid)
	{
		super(tetrisgrid);
		int rotation = (int)(Math.random() * 4);
		blockSize = new int[]{1, 1, 1, 1};
		blockSize[rotation] = 0;
		blockShape = new int[][] {
			{0,0},
			{1 - (rotation + 1) / 2, (4 - rotation) % 3 - 1},
			{(4 - rotation) % 3 - 1, (rotation + 1) / 2 - 1},
			{(rotation + 1) % 3 - 1, 1 - (rotation + 1) / 2}
		};
	}
}
