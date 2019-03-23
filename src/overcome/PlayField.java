package overcome;

import javafx.geometry.Point2D;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * generates and stores the map
 * 
 * @author Zachary Nordgren
 * @version 3/15/2019
 *
 */

public class PlayField {
	
	final static int CELLSIZE = 50;
	int[][] playArea;
	int[][] discoveredMap;
	
	int mapW, mapH;
	int visibleW, visibleH;
	static int centerX, centerY;
	
	Random rng = new Random();
	Directions dirObject = new Directions();
	
	double x, y;
	int nRooms = 20;
	
	PlayField(int windowW, int windowH, int mapW, int mapH) {
		
		this.visibleW = windowW / CELLSIZE;
		this.visibleH = windowH / CELLSIZE;
		PlayField.centerX = this.visibleW / 2;
		PlayField.centerY = this.visibleH / 2;
		this.mapW = mapW;
		this.mapH = mapH;
		
		
		this.playArea = new int[mapH+2][mapW+2];
		this.discoveredMap = new int[mapH+2][mapW+2];
		
	}
	
	public boolean checkMove(Point2D p, int dir, double dist) {
		Point2D potentialP = dirObject.movePoint(p.getX(), p.getY(), dir, dist);
		return Terrain.moveIsValid(this.get(potentialP));
	}
	
	public boolean checkMove(int dir, double dist) {
		Point2D potentialP = dirObject.movePoint(x, y, dir, dist);
		return Terrain.moveIsValid(this.get(potentialP));
	}
	
	public Point2D move(int dir, double dist) {
		Point2D p = dirObject.movePoint(x, y, dir, dist);
		x = p.getX();
		y = p.getY();
		return p;
	}
	
	public boolean checkOnScreen(double xx, double yy) {
		return (Math.abs(x-xx) <= centerX && Math.abs(y-yy) <= centerY);
	}
	
	public int get(int xx, int yy)
	{
		try
		{
			return playArea[xx][yy];
		} catch(ArrayIndexOutOfBoundsException e)
		{
			return 1;
		}
	}
	
	public int get(Point2D p)
	{
		try
		{
			return playArea[(int) p.getX()][(int) p.getY()];
		} catch(ArrayIndexOutOfBoundsException e)
		{
			return 1;
		}
	}
	public void set(double xx, double yy, int v)
	{
		try
		{
			playArea[(int) xx][(int) yy] = v;
		} catch(ArrayIndexOutOfBoundsException e)
		{}
	}
	public void set(int xx, int yy, int v)
	{
		try
		{
			playArea[xx][yy] = v;
		} catch(ArrayIndexOutOfBoundsException e)
		{}
	}
	
	public void clear(int xx, int yy)
	{
		try
		{
			playArea[xx][yy] = Terrain.VOID;
		} catch(ArrayIndexOutOfBoundsException e)
		{}
	}
	
	public void generateField()
	{
		int xx, yy;
		
		x = 1;
		y = 1;
		
		for (yy = 0; yy <= mapH; yy++) {
			for (xx = 0; xx <= mapW; xx++) {
				playArea[xx][yy] = Terrain.WALL; // set whole map as wall for now, later I will have it be void when I can generate maps
				discoveredMap[xx][yy] = 1; //set all visible for now
			}
		}

		for (yy = 1; yy <= mapH-2; yy++) {
			for (xx = 1; xx <= mapW-2; xx++) {
				playArea[xx][yy] = Terrain.EMPTY; //clear center of map until I implement maze generation
			}
		}
		
	}
	
	public void placeSprite(GraphicsContext gc, Sprite s) {
		if(s.stats.enabled && s.stats.visible && s.stats.onScreen)
	  		gc.drawImage(ImageManager.get(s.look), PlayField.CELLSIZE*(centerX + s.x - x), PlayField.CELLSIZE*(centerY + s.y - y));
	}
	
	public void render(GraphicsContext gc)
	{
		double xx, yy;
		Terrain t = new Terrain();
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, CELLSIZE*visibleW, CELLSIZE*visibleH);
		
		// the player position x,y is always at the center of the screen
		System.out.println("xy = (" + x + "," + y + ")");
		for (yy = 0; yy < visibleH; yy++) {
			for (xx = 0; xx < visibleW; xx++) {
				int currentX = (int) ((x - centerX) + xx);
				int currentY = (int) ((y - centerY) + yy);
				
				if(currentX < 0 || currentY < 0 || currentX >= mapW || currentY >= mapH) {
					t.terrain = Terrain.VOID;
				} else {
					t.terrain = playArea[currentX][currentY];
				}
				gc.drawImage(ImageManager.get(t), CELLSIZE*(xx), CELLSIZE*(yy));
			}
		}
	}
	
	
	
	
	
}
