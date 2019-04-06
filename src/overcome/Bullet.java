package overcome;

import javafx.geometry.Point2D;

/**
 * Class for representing bullets that are shooting.
 * 
 * @author Zachary Nordgren
 * @version 3/21/2019
 *
 */
public class Bullet {
	
	boolean enabled;
	double x,y;
	int dir;
	double range;
	double damage;
	double punchThrough;
	
	
	public Bullet() {
		this.enabled = false;
		this.range = 10;
		this.damage = 50;
		this.punchThrough = 2;
	}
	
	public Point2D getPosition() {
		return new Point2D(x,y);
	}
	
	public void shoot(Sprite s, Point2D target) {
		this.enabled = true;
		this.x = s.x;
		this.y = s.y;
		this.dir = s.facing;
	}
	
	public boolean onTrajectory(Point2D p) {
		for(int i = 1; i<range; i++) {
			if(p.equals(Directions.movePoint(x, y, dir, i))) return true;
		}
		return false;
		
	}
	
	double thetaBetweenPoints(Point2D p1, Point2D p2) {
		double delta_x = p2.getX() - p1.getX();
		double delta_y = p2.getY() - p1.getY();
		return Math.atan2(delta_y, delta_x);
	}
	
}
