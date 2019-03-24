package overcome;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Overcome.java
 * 
 * Roguelike dungeon crawler with guns and loot. Stay alive, get out.
 * 
 * Move with WASD, Space to shoot, E to open chests
 * 
 * @author Zachary Nordgren
 * @version 3/15/2019
 *
 */

public class Main extends Application {

	final String appName = "Overcome";
	final static int FPS = 30;
	final static int CELLSIZE = 50;
	final static int WIDTH = 750;
	final static int PLAY_HEIGHT = 650;
	final static int INFO_HEIGHT = 100;

	//SplashScreen splash = new Splash();
	Level level = new Level();
	Requests requests = new Requests();

	int gameState;

	/**
	 * Set up initial data structures/values
	 */
	void initialize() {
		ImageManager.loadImages();
		level.initialize();
		requests.clear();
		gameState = 0;
	}

	void setHandlers(Scene scene) {
		// Use wasd or arrow keys to move, space to shoot
		scene.setOnKeyPressed(e -> {
			requests.keyPressed = true;
			switch (e.getCode()) {
			case A:
			case LEFT:
				requests.move = Directions.LEFT;
				break;
			case D:
			case RIGHT:
				requests.move = Directions.RIGHT;
				break;
			case W:
			case UP:
				requests.move = Directions.UP;
				break;
			case S:
			case DOWN:
				requests.move = Directions.DOWN;
				break;
			case SPACE:
				requests.shoot = 1;
				break;
			default:
				break;
			}
		});

		scene.setOnKeyReleased(e -> {
			requests.clear();
		});
	}

	public void update() {

		switch (gameState) {
		case 0: // splash screen
			//splash.spin(requests);
			gameState = 1;
			break;
		case 1: // create new level
			level.createLevel();
			gameState = 2;
			break;
		case 2: // Main loop
			level.spin(requests);
			break;
		default:
			break;
		}

		requests.clear();
	}

	void render(GraphicsContext info_gc, GraphicsContext gc) {

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, PLAY_HEIGHT); // clear screen

		switch (gameState) {
		case 0:
		case 1:
			//splash.render(gc);
			break;
		case 2:
		case 3:
			level.render(gc);
			break;
		default:
			break;
		}

		renderInfo(info_gc);

	}

	void renderInfo(GraphicsContext gc) {

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, INFO_HEIGHT); // clear screen

		gc.setFill(Color.YELLOW);
		gc.setStroke(Color.YELLOW);

		switch (gameState) {
		case 0:
		case 1:
			break;
		case 2:
		case 3:
			drawText(gc, "HEALTH: " + level.getPlayerHealth(), 10, 30);
			break;
		}

	}

	void drawText(GraphicsContext gc, String s, double x, double y) {
		gc.fillText(s, x, y);
		gc.strokeText(s, x, y);
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) throws Exception {

		theStage.setTitle(appName);

		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);

		// VBox lets us group the two Canvases
		// one above the other on the screen
		VBox frame = new VBox();

		// We use two Canvas objects - one for the top status bar,
		// and one for the playing field
		Canvas play_canvas = new Canvas(WIDTH, PLAY_HEIGHT);
		frame.getChildren().add(play_canvas);
		Canvas info_canvas = new Canvas(WIDTH, INFO_HEIGHT);
		frame.getChildren().add(info_canvas);

		root.getChildren().add(frame);

		GraphicsContext play_gc = play_canvas.getGraphicsContext2D();
		play_gc.setFont(Font.font(24.0));
		GraphicsContext info_gc = info_canvas.getGraphicsContext2D();
		info_gc.setFont(Font.font(24.0));

		initialize();
		setHandlers(theScene);

		KeyFrame kf = new KeyFrame(Duration.millis(1000 / FPS), e -> {
			update();
			render(info_gc, play_gc);
		});

		Timeline mainLoop = new Timeline(kf);
		mainLoop.setCycleCount(Animation.INDEFINITE);
		mainLoop.play();

		theStage.show();

	}

}
