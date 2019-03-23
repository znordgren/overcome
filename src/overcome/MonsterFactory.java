package overcome;

/**
 * Class for creating monsters, will be able to create monsters of different types
 * 
 * @author Zachary Nordgren
 * @version 3/21/2019
 *
 */
public class MonsterFactory {

	
	public Monster makeMonster(int startX, int startY) {
		Monster m = new Monster(startX,startY);
		return m;
	}
	
	
	
	
	
}
