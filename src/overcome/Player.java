package overcome;

/**
 * Class for implementing the actions the player can take
 * 
 * @author Zachary Nordgren
 * @version 3/21/2019
 *
 */
public class Player extends Sprite {

	final int sprites[] = {1, 0, 3, 2, 4};

	public Player(int startX, int startY) {
		super(startX, startY, Directions.DOWN-2, Directions.DOWN);
		stats.makeAnimate();
		stats.setStats(10, 100, 20, 50, 1, 1, 10, 10, 10);
		stats.onScreen = true;
	}

	@Override
	public void updateLook() {
		setLook(getFacing());
	}
	
	public int getLookforDirection(int d) {
		return sprites[d-1];
	}
	
	public void setLook(int d) {
		this.look = sprites[d-1];
	}

	public void update(PlayField field)
	{
		super.update(field);
	}

}
