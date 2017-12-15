package blocks;

import tetris.TetrisGrid;

public class ZBlockMirror extends TetrisBlock
{
	public ZBlockMirror(TetrisGrid tetrisgrid)
	{
		super(tetrisgrid);
		int rotation = (int)(Math.random() * 4);
		blockSize = new int[]{1, 1, 1, 1};
		blockShape = new int[][] {
			{0,0},
			{(rotation % 2) * 2 - 1, (rotation / 2) * 2 - 1},
			{1 - (rotation + 1) / 2, (4 - rotation) % 3 - 1},
			{(rotation + 1) / 2 - 1, (rotation + 1) % 3 - 1},
			{1 - (rotation % 2) * 2, 1 - (rotation / 2) * 2}
		};
	}
}