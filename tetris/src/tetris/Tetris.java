package tetris;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Tetris extends Application
{
	long speed;
	int speedtimer = 0;
	TetrisGrid tetrisgrid;
	GridDrawer drawer;
	Timer gameTimer;
	Timer drawTimer;
	TetrisBlock activeBlock;
	public enum GameState {GAMEOVER, CONTINUE, NEWBLOCK};
	boolean isRunning = false;
	
	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override public void start(Stage stage)
	{
		BorderPane border = new BorderPane();
		MenuBar menuBar = new MenuBar();
		border.setTop(menuBar);
		Menu menuGame = new Menu("Game");
		menuBar.getMenus().addAll(menuGame);
		MenuItem miNewGame = new MenuItem("New Game");
		menuGame.getItems().addAll(miNewGame);
		miNewGame.setOnAction(e->this.startGame());
		
		Canvas canvas = new Canvas(240, 360);
		border.setCenter(canvas);
		
		tetrisgrid = new TetrisGrid(canvas);
		drawer = tetrisgrid.getGridDrawer();
		drawer.drawGrid();
		
		Scene scene = new Scene(border, 450, 500);
		scene.setOnKeyPressed(e->{
			if (isRunning && activeBlock != null) {
				switch (e.getCode()) {
				case LEFT: activeBlock.moveLeft(); break;
				case RIGHT: activeBlock.moveRight(); break;
				case DOWN: while (activeBlock.drop() == GameState.CONTINUE) {} 
					newBlock(); break;
				case ENTER:
				case SHIFT:
					activeBlock.rotate(); break;
				default:
				}
			}
		});
		
		stage.setTitle("Tetris");
		stage.setScene(scene);
		stage.show();
	}
	
	public void startGame()
	{
		if (gameTimer != null) gameTimer.cancel();
		gameTimer = new Timer();
		if (drawTimer != null) drawTimer.cancel();
		drawTimer = new Timer();
		tetrisgrid.reset();
		isRunning = true;
		activeBlock = null;
		speed = 500;
		setSpeed(speed);
		drawTimer.schedule(new drawLoop(), 0, 15);
	}
	
	public void setSpeed(long s)
	{
		gameTimer.cancel();
		gameTimer = new Timer();
		gameTimer.schedule(new GameLoop(), 0, s);
	}
	
	private class drawLoop extends TimerTask
	{
		public synchronized void run()
		{
			drawer.draw();
		}
	}
	
	private class GameLoop extends TimerTask
	{
		public void run()
		{
			if (activeBlock == null) {
				activeBlock = generateBlock();
			}
			else {
				switch (activeBlock.drop()) {
				case CONTINUE: break;
				case NEWBLOCK:
					newBlock();
					break;
				case GAMEOVER:
					gameTimer.cancel();
					drawTimer.cancel();
					isRunning = false;
					Platform.runLater(new Runnable() {
					    @Override
					    public void run() {
					    	Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Game Finished!");
							alert.setHeaderText(null);
							alert.setContentText("Your final score is: " + tetrisgrid.getScore());
							alert.showAndWait();
					    }
					});
					break;
				default:
				}				
			}
		}
	}
	
	private TetrisBlock generateBlock()
	{
		int rng = (int)(Math.random() * 7);
		switch (rng) {
		case 0: return new LineBlock(tetrisgrid);
		case 1: return new LBlock(tetrisgrid);
		case 2: return new LBlockMirror(tetrisgrid);
		case 3: return new SBlock(tetrisgrid);
		case 4: return new SBlockMirror(tetrisgrid);
		case 5: return new SquareBlock(tetrisgrid);
		default: return new TBlock(tetrisgrid);
		}
	}
	
	public void newBlock()
	{
		activeBlock = generateBlock();
		speedtimer++;
		if (speedtimer == 25) {
			speed = (long)(speed * 0.8);
			setSpeed(speed);
			speedtimer = 0;
		}
	}
}
