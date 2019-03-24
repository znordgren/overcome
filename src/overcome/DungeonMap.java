package overcome;

import javafx.geometry.Point2D;

public class DungeonMap {

	int[][] map;
	int[][] discoveredMap;
	int[][] itemMap;
	int mapW, mapH;

	public DungeonMap(int w, int h) {
		mapW = w;
		mapH = h;
		map = new int[mapW][mapH];
		discoveredMap = new int[mapW][mapH];
		itemMap = new int[mapW][mapH];
	}

	public Point2D generate() {
		int x, y;
		Point2D startPosition = new Point2D(1, 1);

		for (y = 0; y < mapH; y++) {
			for (x = 0; x < mapW; x++) {
				map[x][y] = Terrain.WALL;
			}
		}

		for (y = 1; y < mapH - 1; y++) {
			for (x = 1; x < mapW - 1; x++) {
				map[x][y] = Terrain.EMPTY;
			}
		}

		return startPosition;

	}

	public int get(double x, double y) {
		if (x < 0 || y < 0 || x >= mapW || y >= mapH) {
			return Terrain.VOID;
		}
		return map[(int) x][(int) y];
	}

	public int get(Point2D p) {
		return get(p.getX(), p.getY());
	}

}
