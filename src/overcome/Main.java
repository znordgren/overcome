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
	final int FPS = 30;
	final static int WIDTH = 750;

	final static int PLAY_HEIGHT = 650;
	final static int INFO_HEIGHT = 100;
	
	public static int gameState;
	static long score;
	static int mapX=100, mapY=100;
	public static PlayField field = new PlayField(WIDTH, PLAY_HEIGHT, mapX, mapY);
	public static Sprite[] creatures = new Sprite[30];
	public static int actors;
	public MonsterFactory monsterFactory = new MonsterFactory();
	
	public static int moveRequest;
	public static int shootRequest;
	
	/**
	 * Set up initial data structures/values
	 */
	void initialize()
	{
		ImageManager.loadImages();
		score = 0;
		gameState = 0;
		moveRequest = 0;
		shootRequest = 0;
	}
	
	void setHandlers(Scene scene)
	{
		// Use wasd or arrow keys to move
		scene.setOnKeyPressed(
			e -> {
					if (gameState==2)
					{
						switch (e.getCode())
						{
							case A:
							case LEFT:
								moveRequest = Directions.LEFT;
								break;
							case D:
							case RIGHT:
								moveRequest = Directions.RIGHT;
								break;
							case W:
							case UP:
								moveRequest = Directions.UP;
								break;
							case S:
							case DOWN:
								moveRequest = Directions.DOWN;
								break;
							case SPACE:
								shootRequest = 1;
								break;
							default:
								break;
						}
					}
				}
			);
				
		scene.setOnKeyReleased(
			e -> {
				moveRequest=0;
				}
			);
	}
	
	/**
	 *  Update variables for one time step
	 *  
	 *  What needs to be done depends on gameState
	 */
	public void update()
	{
		switch(gameState){
		case 0: // create new level
			field.generateField();
			creatures[0] = new Player(1,1);
			actors++;
			creatures[1] = monsterFactory.makeMonster(3, 7);
			actors++;
			creatures[2] = monsterFactory.makeMonster(7, 7);
			actors++;
			creatures[3] = monsterFactory.makeMonster(7, 3);
			actors++;
			gameState = 1;
			break;
		case 1:
			gameState=2;
			break;
		case 2: // Main loop
			gameLoop();
			break;
		default:
			break;
		}
	}
	
	void gameLoop()
	{
		
		if(moveRequest != Directions.STOPPED) {
		// update each creature on the play field

			if(field.checkMove(moveRequest, creatures[0].stats.speed)) {
				creatures[0].setPosition(field.move(moveRequest,creatures[0].stats.speed));
			}
			
			for (int i=0;i<actors;i++)
				    creatures[i].update(field);
			moveRequest = 0;
		}
	}
	
	void render(GraphicsContext info_gc, GraphicsContext gc) {
		
		switch(gameState){
		case 0:
		case 1:
			gc.setFill(Color.BLACK);
			gc.fillRect(0,0,WIDTH,PLAY_HEIGHT);
			break;
		case 2:
		case 3:
			field.render(gc);
			for (int i=0;i<actors;i++)
				field.placeSprite(gc, creatures[i]);
//				creatures[i].render(gc);
			break;
		default:
			gc.setFill(Color.BLACK);
			gc.fillRect(0,0,WIDTH,PLAY_HEIGHT);
			break;
		}
		
		renderInfo(info_gc);
		
	}
	
	void renderInfo(GraphicsContext info_gc) {
		info_gc.setFill(Color.BLACK);
		info_gc.fillRect(0,0,WIDTH,INFO_HEIGHT);
	}
	
	void drawText(GraphicsContext gc, String s, double x, double y)
	{
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
		//  and one for the playing field
		Canvas play_canvas = new Canvas(WIDTH, PLAY_HEIGHT);
		frame.getChildren().add(play_canvas);
		
		Canvas info_canvas = new Canvas(WIDTH, INFO_HEIGHT);
		frame.getChildren().add(info_canvas);
		
		root.getChildren().add(frame);

		GraphicsContext play_gc = play_canvas.getGraphicsContext2D();
		play_gc.setFont(Font.font(24.0));
		GraphicsContext info_gc = info_canvas.getGraphicsContext2D();
		info_gc.setFont(Font.font(24.0));

		// Initial setup
		initialize();
		setHandlers(theScene);
		
		// Setup and start animation loop (Timeline)
		KeyFrame kf = new KeyFrame(Duration.millis(1000 / FPS),
				e -> {
					// update position
					update();
					// draw frame
					render(info_gc, play_gc);
				}
			);
		Timeline mainLoop = new Timeline(kf);
		mainLoop.setCycleCount(Animation.INDEFINITE);
		mainLoop.play();

		theStage.show();
		
	}

	
	
}
