package overcome;

import javafx.geometry.Point2D;

/**
 * Base class for all non terrain objects
 * 
 * @author Zachary Nordgren
 * @version 3/21/2019
 *
 */
public class Sprite
{
  double x,y;
  int look;
  int facing;
  public Stats stats;

  public Sprite(double x, double y, int look, int facing) {
	  this.x = x;
	  this.y = y;
	  this.look = look;
	  this.facing = facing;
	  this.stats = new Stats();
  }
  
  public void updateLook() {
  }
  
  public void setFacing(int d) {
	  facing = d;
	  updateLook();
  }

  public void setPosition(double a, double b) {
  	x = a; y = b;
  }
  
  public void setPosition(Point2D p) {
	  	x = p.getX();
	  	y = p.getY();
  }
  
  public int getFacing() {
	  return facing;
  }
  
  public Point2D getPosition() {
	  return new Point2D(x,y);
  }

  boolean isActive() {
  	return stats.enabled;
  }

  void suspend() {
    stats.enabled = false;
  }

  void resume() {
    stats.enabled = true;
  }
  
  public void update(PlayField field){}

}
