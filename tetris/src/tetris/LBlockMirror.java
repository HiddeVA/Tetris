package tetris;

public class LBlockMirror extends TetrisBlock
{
	public LBlockMirror(TetrisGrid tetrisgrid)
	{
		super(tetrisgrid);
		int rotation = (int)(Math.random() * 4);
		blockSize = new int[]{1, 1, 1, 1};
		blockSize[rotation] = 0;
		blockShape = new int[][] {
			{0, 0},
			{1 - (rotation / 2) * 2, 1 - (rotation % 2) * 2},
			{(rotation + 1) % 3 - 1, 1 - (rotation + 1) / 2},
			{(4 - rotation) % 3 - 1, (rotation + 1) / 2 - 1}
			};
			//just to clarify:
			//1 - (rotation / 2) * 2 gives {1, 1 ,-1, -1}
			//1 - (rotation % 2) * 2 gives {1, -1, 1, -1}
			//These are the blocks that are 1 square diagonally from the origin
			//(rotation + 1) % 3 - 1 gives {0, 1, -1, 0}
			//(4 - rotation) % 3 - 1 gives {0, -1, 1, 0}
			//1 - (rotation + 1) / 2 gives {1, 0, 0, -1}
			//(rotation + 1) / 2 - 1 gives {-1, 0, 0, 1}
			//These are the blocks that are 1 square vertically or horizontally from the origin
		yPos = blockSize[0] - 1;
	}
}
