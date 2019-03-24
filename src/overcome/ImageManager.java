package overcome;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * Overcome.java
 * 
 * Load Sprite sheet and 
 * 
 * @author Zachary Nordgren
 * @version 3/15/2019
 *
 */

public class ImageManager {
	
	static Image[] small = new Image[100];
	
	/*
	 * Return small image number "look"
	 */
	public static Image get(int look)
	{
		return small[look];
	}
	
	public static Image get(Terrain t)
	{
		int look = 11;
		switch (t.terrain) {
			case Terrain.EMPTY:
			case Terrain.OCCUPIED:
			case Terrain.PUSHABLE:
			case Terrain.BREAKABLE:
			case Terrain.SLIPPERY:
			case Terrain.TRAP:
				look = 11;
				break;
			case Terrain.WALL:
				look = 10;
				break;
			case Terrain.VOID:
				look = 12;
				break;
			case Terrain.UPSTAIR:
				look = 14;
				break;
			case Terrain.DOWNSTAIR:
				look = 15;
				break;
			case Terrain.CHEST:
				look = 13;
				break;
		}
		
		return small[look];
				
	}
	
	/**
	 * Read in the large image file and chop it
	 * into little images.
	 */
	public static void loadImages()
	{
		Image full = new Image("spritesheet.png");
		int i = 0;
		for (int row = 0; row < 10; row++)
			for (int col = 0; col < 10; col++)
			{
				small[i] = (Image)new WritableImage(full.getPixelReader(),
						Main.CELLSIZE*col, Main.CELLSIZE*row, Main.CELLSIZE, Main.CELLSIZE);
				i++;
			}
	}
}
