package overcome;

import javafx.geometry.Point2D;

/**
 * Base class for implementing the actions generic monsters can take. There will
 * be specific classes for different monster types derived from this one.
 * 
 * @author Zachary Nordgren
 * @version 3/21/2019
 *
 */
public class Monster extends Sprite {

	int sprites[] = { 20 };
	Directions dirObject = new Directions();

	public Monster(double d, double e) {
		super(d, e, 20, Directions.DOWN);
		stats.makeAnimate();
		stats.setStats(10, 100, 20, 50, 1, 1, 10, 10, 10);
	}

	public void update(PlayField field, Point2D playerLoc) {
		super.update(field);
		int testDir;
		testDir = dirObject.randDirection();
		while (!field.checkMove(Directions.movePoint(x, y, testDir, stats.speed), testDir, stats.speed)) {
			testDir = dirObject.randDirection();
		}
		if(playerLoc.distance(x,y)<8) {
			int tempTestDir = dirObject.getNextStepInPath(x, y, playerLoc.getX(), playerLoc.getY());
			if(!Directions.movePoint(x, y,tempTestDir,stats.speed).equals(playerLoc)) {
				if(field.checkMove(Directions.movePoint(x, y,tempTestDir,stats.speed), tempTestDir, stats.speed)){
					testDir = tempTestDir;
				}
			}
		}
		
		setPosition(Directions.movePoint(x, y, testDir, stats.speed));
		stats.onScreen = field.checkOnScreen(x, y);
	}
	
	

}
