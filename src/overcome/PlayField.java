package overcome;

import javafx.geometry.Point2D;
import java.util.Random;
import javafx.scene.canvas.GraphicsContext;

/**
 * generates and stores the map
 * 
 * @author Zachary Nordgren
 * @version 3/15/2019
 *
 */

public class PlayField {

	DungeonMap dungeonMap;

	int mapW, mapH;
	int visibleW, visibleH;
	static int centerX, centerY;

	Random rng = new Random();
	Directions dirObject = new Directions();

	double x, y;
	int nRooms = 20;

	PlayField(int windowW, int windowH) {
		this.visibleW = windowW / Main.CELLSIZE;
		this.visibleH = windowH / Main.CELLSIZE;
		PlayField.centerX = this.visibleW / 2;
		PlayField.centerY = this.visibleH / 2;
		this.mapW = 100;
		this.mapH = 100;

		this.dungeonMap = new DungeonMap(mapW, mapH);
	}

	public boolean checkMove(Point2D p, int dir, double dist) {
		Point2D potentialP = Directions.movePoint(p.getX(), p.getY(), dir, dist);
		return Terrain.moveIsValid(dungeonMap.get(potentialP));
	}

	public boolean checkMove(int dir, double dist) {
		return checkMove(new Point2D(x, y), dir, dist);
	}

	public Point2D move(int dir, double dist) {
		Point2D p = Directions.movePoint(x, y, dir, dist);
		setPosition(p);
		return p;
	}
	
	public boolean checkEndOfLevel(Point2D p) {
		return dungeonMap.map[(int) p.getX()][(int) p.getY()] == Terrain.UPSTAIR;
	}

	public boolean checkOnScreen(double xx, double yy) {
		return (dungeonMap.discoveredMap[(int)xx][(int)yy] == 1) && (Math.abs(x - xx) <= centerX && Math.abs(y - yy) <= centerY);
	}

	public boolean checkOnScreen(Point2D p) {
		return checkOnScreen(p.getX(), p.getY());
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setPosition(Point2D p) {
		setPosition(p.getX(), p.getY());
	}

	public int get(double xx, double yy) {
		return dungeonMap.get(xx, yy);
	}

	public int get(Point2D p) {
		return dungeonMap.get(p);
	}

	public void generateField() {
		setPosition(dungeonMap.generate());
	}

	public void placeSprite(GraphicsContext gc, Sprite s) {
		if (checkOnScreen(s.getPosition()) && s.stats.enabled && s.stats.visible) {
			gc.drawImage(ImageManager.get(s.look), Main.CELLSIZE * (centerX + s.x - x),
					Main.CELLSIZE * (centerY + s.y - y));
		}
	}

	public void render(GraphicsContext gc) {
		double xx, yy, currentX, currentY;
		Terrain t = new Terrain();
		// the player position x,y is always at the center of the screen
		//System.out.println("xy = (" + x + "," + y + ")");
		for (yy = 0; yy < visibleH; yy++) {
			for (xx = 0; xx < visibleW; xx++) {

				currentX = (x - centerX) + xx;
				currentY = (y - centerY) + yy;

				t.terrain = dungeonMap.get(currentX, currentY);
				
				if(dungeonMap.getVisible(currentX, currentY))
					gc.drawImage(ImageManager.get(t), Main.CELLSIZE * (xx), Main.CELLSIZE * (yy));
			}
		}
	}

}
