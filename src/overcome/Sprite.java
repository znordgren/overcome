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
  public Stats stats;

  public Sprite(double x1, double y1, int look) {
	  this.x = x1;
	  this.y = y1;
	  this.look = look;
	  this.stats = new Stats();
  }

  public void setPosition(double a, double b) {
  	x = a; y = b;
  }
  
  public void setPosition(Point2D p) {
	  	x = p.getX();
	  	y = p.getY();
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
