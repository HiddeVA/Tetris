package tetris;

import blocks.*;

import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Tetris extends Application
{
	long speed;
	int speedtimer = 0;
	TetrisGrid tetrisgrid;
	GridDrawer drawer, nextBlockDrawer;
	Timer gameTimer, drawTimer;
	TetrisBlock activeBlock, nextBlock;
	int blockSize = 4;
	public enum GameState {GAMEOVER, CONTINUE, NEWBLOCK};
	boolean isRunning = false;
	boolean paused = false;
	
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
		miNewGame.setOnAction(e->this.startGame());
		MenuItem miSettings = new MenuItem("Settings");
		miSettings.setOnAction(e->showSettings(stage));
		MenuItem miPause = new MenuItem("Pause");
		miPause.setOnAction(e->{
			pauseGame();
			miPause.setText(paused?"Unpause":"Pause");
			});
		menuGame.getItems().addAll(miNewGame, miSettings, miPause);
		
		tetrisgrid = new TetrisGrid();
		border.setCenter(tetrisgrid);
		drawer = tetrisgrid.getGridDrawer();
		drawer.drawGrid();
		
		VBox vboxNextBlock = new VBox();
		TetrisGrid nextBlockGrid = new TetrisGrid(85, 85, 4, 4);
		nextBlockDrawer = nextBlockGrid.getGridDrawer();
		nextBlockDrawer.drawGrid();
		vboxNextBlock.setSpacing(10);
		vboxNextBlock.setAlignment(Pos.TOP_CENTER);
		vboxNextBlock.getChildren().addAll(new Label("Next:"), nextBlockGrid);
		border.setRight(vboxNextBlock);
		
		
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
		stage.setOnCloseRequest(e->stopTimer());
	}
	
	private void startGame()
	{
		stopTimer();
		gameTimer = new Timer();
		drawTimer = new Timer();
		tetrisgrid.reset();
		nextBlockDrawer.drawGrid();
		isRunning = true;
		activeBlock = null;
		nextBlock = generateBlock();
		speed = 500;
		setSpeed(speed);
		drawTimer.schedule(new drawLoop(), 0, 15);
	}
	
	private void stopTimer()
	{
		if (gameTimer != null) gameTimer.cancel();
		if (drawTimer != null) drawTimer.cancel();
	}
	
	private void pauseGame()
	{
		if (!isRunning)	return;
		if (!paused) {
			drawTimer.cancel();
			gameTimer.cancel();
			paused = true;
		}
		else {
			drawTimer = new Timer();
			drawTimer.schedule(new drawLoop(), 0, 15);
			setSpeed(speed);
			paused = false;
		}
	}
	
	private void setSpeed(long s)
	{
		gameTimer.cancel();
		gameTimer = new Timer();
		gameTimer.schedule(new GameLoop(), 0, s);
	}
	
	private class drawLoop extends TimerTask
	{
		public synchronized void run()
		{
			try {
				drawer.draw();
			}
			catch (ConcurrentModificationException cme) {
				activeBlock.tempFix();
			}
		}
	}
	
	private class GameLoop extends TimerTask
	{
		public synchronized void run()
		{
			if (activeBlock == null) {
				activeBlock = nextBlock;
				activeBlock.setPosition(tetrisgrid.getGridWidth() / 2);
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
					drawer.draw();
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
	
	private void newBlock()
	{
		activeBlock = nextBlock;
		activeBlock.setPosition(tetrisgrid.getGridWidth() / 2);
		for (int[] pos : nextBlock.getOutline()) {
			nextBlockDrawer.clearBlock(2 + pos[1], 2 + pos[0]);
		}
		nextBlock = generateBlock();
		for (int[] pos : nextBlock.getOutline()) {
			nextBlockDrawer.drawBlock(2 + pos[1], 2 + pos[0]);
		}
		nextBlockDrawer.draw();
		speedtimer++;
		if (speedtimer >= 25) {
			speed = (long)(speed * 0.8);
			setSpeed(speed);
			speedtimer = 0;
		}
	}
	
	private TetrisBlock generateBlock()
	{
		int rng;
		if (blockSize == 4)
			rng = (int)(Math.random() * 7);
		else
			rng = Math.abs((int)(Math.random() * 21) - 7); //generates number between 0 and 13 with 0-6 twice as often
		switch (rng) {
		case 0: return new LineBlock(tetrisgrid);
		case 1: return new LBlock(tetrisgrid);
		case 2: return new LBlockMirror(tetrisgrid);
		case 3: return new SBlock(tetrisgrid);
		case 4: return new SBlockMirror(tetrisgrid);
		case 5: return new SquareBlock(tetrisgrid);
		case 6: return new TBlock(tetrisgrid);
		case 7: return Math.random() < 0.5 ? new RBlock(tetrisgrid) : new RBlockMirror(tetrisgrid);
		case 8: return new BigTBlock(tetrisgrid);
		case 9: return Math.random() < 0.5 ? new UBlock(tetrisgrid) : new XBlock(tetrisgrid);
		case 10: return Math.random() < 0.5 ? new ZBlock(tetrisgrid) : new ZBlockMirror(tetrisgrid);
		case 11: return Math.random() < 0.5 ? new PBlock(tetrisgrid) : new PBlockMirror(tetrisgrid);
		case 12: return Math.random() < 0.5 ? new LongLBlock(tetrisgrid) : new LongLBlockMirror(tetrisgrid);
		case 13: return new HookBlock(tetrisgrid);
		default: return null;
		}
	}
	
	private void showSettings(Stage onStage)
	{
		Stage settings = new Stage();
		settings.initModality(Modality.APPLICATION_MODAL);
		settings.initOwner(onStage);
		VBox settingsPane = new VBox();
		settings.setScene(new Scene(settingsPane, 200, 150));
		
		HBox hboxBlockSize = new HBox();
		RadioButton rbtFour = new RadioButton();
		rbtFour.setOnAction(e->blockSize = 4);
		RadioButton rbtFive = new RadioButton();
		rbtFive.setOnAction(e->blockSize = 5);
		ToggleGroup tglBlockSize = new ToggleGroup();
		tglBlockSize.getToggles().addAll(rbtFour, rbtFive);
		hboxBlockSize.getChildren().addAll(new Label("BlockSize"), rbtFour, new Label("4"), rbtFive, new Label("5"));
		settingsPane.getChildren().addAll(hboxBlockSize);
		
		settings.show();
	}
}
