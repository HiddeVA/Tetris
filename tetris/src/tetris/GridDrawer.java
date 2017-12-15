package tetris;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;

public class GridDrawer
{
	private double size = 20;
	private double gap = 1;
	private int cols;
	private int rows;
	private GraphicsContext gc;
	private List<DrawRequest> queue = new ArrayList<DrawRequest>();
	
	public GridDrawer(GraphicsContext gc, int size, int gap, int cols, int rows)
	{
		this.size = size;
		this.gap = gap;
		this.cols = cols;
		this.rows = rows;
		this.gc = gc;
	}
	
	public GridDrawer(GraphicsContext gc, int cols, int rows)
	{
		this.gc = gc;
		this.cols = cols;
		this.rows = rows;
	}
	
	public GridDrawer(GraphicsContext gc)
	{
		this.gc = gc;
		cols = 10; rows = 17;
	}
	
	public void setBlockSize(double size)
	{
		this.size = size;
	}
	
	public void drawGrid()
	{
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, (size + gap) * (cols - 1), (size + gap) * (rows - 1));
		gc.setFill(Color.BLACK);
		for (int i = 0; i < cols; i++)
		{
			for (int j = 0; j < rows; j++)
			{
				gc.fillRect(i * (size + gap), j * (size + gap), size, size);
			}
		}
	}
	
	public void drawBlock(int x, int y)
	{
		if (x < 0 || y < 0 || x > cols || y > rows) return;
		queue.add(new DrawRequest(x * (size + gap), y * (size + gap), size, Color.YELLOW));
	}
	
	public void clearBlock(int x, int y)
	{
		if (x < 0 || y < 0 || x > cols || y > rows) return;
		queue.add(new DrawRequest(x * (size + gap), y * (size + gap), size, Color.BLACK));
	}
	
	public synchronized void draw()
	{
		for(DrawRequest dReq : queue)
		{
			dReq.executeRequest();
		}
		queue.clear();
	}
	
	private class DrawRequest
	{
		private double atX;
		private double atY;
		private double rSize;
		private Color color;
		
		public DrawRequest(double x, double y, double size, Color color)
		{
			this.atX = x;
			this.atY = y;
			this.rSize = size;
			this.color = color;
		}
		
		public void executeRequest()
		{
			gc.setFill(color);
			gc.fillRect(atX, atY, rSize, rSize);
		}
	}
}
