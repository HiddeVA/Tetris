package tetris;

public class SquareBlock extends TetrisBlock 
{
	public SquareBlock(TetrisGrid tetrisgrid)
	{
		super(tetrisgrid);
		blockSize = new int[]{1, 1, 0, 0};
		blockShape = new int[][] {{-1, 0},{-1, -1},{0,0},{0,-1}};
		yPos = blockSize[0] - 1;
	}
	
	public void rotate()
	{
	}
}
