package tetris;

public class LineBlock extends TetrisBlock
{
	public LineBlock(TetrisGrid tetrisgrid)
	{
		super(tetrisgrid);
		int rotation = (int)(Math.random() * 2);
		blockSize = new int[]{2 * (1 - rotation), rotation, 2 * rotation , 1 - rotation};
		blockShape = new int[][] {
			{-2 * (1 - rotation), -2 * rotation},
			{rotation - 1, rotation * -1},
			{0,0},
			{1 - rotation, rotation}
			};
		yPos = blockSize[0] - 1;
	}
}
