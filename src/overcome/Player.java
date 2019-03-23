package overcome;

public class Player extends Sprite {
	
	final int sprites[] = {1, 0, 3, 2, 4};
	
	public Player(int startX, int startY) {
		super(startX, startY, 2);
		stats.makeAnimate();
		stats.setStats(10, 100, 20, 50, 1, 1, 10, 10, 10);
		stats.onScreen = true;
	}
	
	public void update(PlayField field)
	{
		super.update(field);
		if(Main.moveRequest != Directions.STOPPED){
			this.look = sprites[Main.moveRequest-1];
		}
	}
	
}
