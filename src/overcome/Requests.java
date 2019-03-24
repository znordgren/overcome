package overcome;

public class Requests {
	public boolean keyPressed;
	public int move;
	public int shoot;
	
	public void clear() {
		keyPressed = false;
		move = Directions.STOPPED;
		shoot = 0;
	}
	
}
