package overcome;

/**
 * Class for storing terrain types
 * 
 * @author Zachary Nordgren
 * @version 3/21/2019
 *
 */
public class Terrain
{
	public int terrain;
	
	public final static int EMPTY = 0;
	public final static int WALL = 1;
	public final static int VOID = 2;
	public final static int OCCUPIED = 3;
	public final static int PUSHABLE = 4;
	public final static int BREAKABLE = 5;
	public final static int SLIPPERY = 6;
	public final static int TRAP = 7;
	public final static int UPSTAIR = 8;
	public final static int DOWNSTAIR = 9;
	public final static int CHEST = 10;
	
	public static boolean moveIsValid(int t) {
		return (t == EMPTY || t == SLIPPERY || t == TRAP || t == UPSTAIR || t == DOWNSTAIR );
	}
	
	
}
