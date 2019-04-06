package overcome;

import java.util.Random;
import javafx.geometry.Point2D;

/**
 * Class for storing the directions of the map and some handy helper functions
 * 
 * @author Zachary Nordgren
 * @version 3/21/2019
 *
 */
public class Directions {
	
	Random rng = new Random();
	public int direction;
	
	public final static int STOPPED = 0;
	public final static int LEFT = 1;
	public final static int RIGHT = 2;
	public final static int UP = 3;
	public final static int DOWN = 4;
	
	public static Point2D movePoint(double x, double y, int dir, double dist) {
		switch(dir) {
		case LEFT: // left
			x -= dist;
			break;
		case RIGHT: // right
			x += dist;
			break;
		case UP: // up
			y -= dist;
			break;
		case DOWN: // down
			y += dist;
			break;
		default:
			break;
		}
		return new Point2D(x,y);
	}
	
	public int getNextStepInPath(double xs, double ys, double xe, double ye) {
		System.out.println(" xs: " + Double.toString(xs) + " ys: " + Double.toString(ys) + " xe: " + Double.toString(xe) + " ye: " + Double.toString(ye));
		if(xs == xe && ys == ye) return STOPPED;
		if(Math.abs(xe-xs) > Math.abs(ye-ys)) {
			if (xe-xs > 0) return RIGHT;
			return LEFT;
		}
		if (ye-ys > 0) return DOWN;
		return UP;
	}
	
	public int randDirection() { //including stopped
		return rng.nextInt(DOWN+1);
	}
	
}
