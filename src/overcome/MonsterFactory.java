package overcome;

import java.util.Random;

import javafx.geometry.Point2D;

/**
 * Class for creating monsters, will be able to create monsters of different types
 * 
 * @author Zachary Nordgren
 * @version 3/21/2019
 *
 */
public class MonsterFactory {

	Random rng = new Random();
	
	public Monster makeMonster(double startX, double startY) {
		Monster m = new Monster(startX,startY);
		m.stats.initiative = rng.nextInt(100);
		return m;
	}
	
	public Monster makeMonster(Point2D startP) {
		return makeMonster(startP.getX(),startP.getY());
	}
	
	
	
	
	
}
