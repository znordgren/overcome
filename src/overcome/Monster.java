package overcome;

/**
 * Base class for implementing the actions generic monsters can take.
 * There will be specific classes for different monster types derived from this one.
 * 
 * @author Zachary Nordgren
 * @version 3/21/2019
 *
 */
public class Monster extends Sprite {

	int sprites[] = {20};
	Directions dirObject = new Directions();
	
	public Monster(int startX, int startY) {
		super(startX, startY, 20);
		stats.makeAnimate();
		stats.setStats(10, 100, 20, 50, 1, 1, 10, 10, 10);
	}
	
	public void update(PlayField field)
	{
		super.update(field);
		int testDir;
		if(Main.moveRequest != Directions.STOPPED){
			testDir = dirObject.randDirection();
			while(!field.checkMove(dirObject.movePoint(x, y, testDir, stats.speed), testDir, stats.speed)) {
				testDir = dirObject.randDirection();
			}
			setPosition(dirObject.movePoint(x, y, testDir, stats.speed));
		}
		stats.onScreen = field.checkOnScreen(x, y);
	}
	
}
