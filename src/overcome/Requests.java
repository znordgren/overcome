package overcome;

public class Requests {
	public boolean keyPressed;
	public int move;
	public int shoot;
	public int refresh;
	
	public void clear() {
		keyPressed = false;
		move = Directions.STOPPED;
		shoot = 0;
		refresh = 0;
	}
	
}
