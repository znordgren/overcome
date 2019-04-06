package overcome;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javafx.geometry.Point2D;

public class DungeonMap {

	Random rng = new Random();
	int[][] map;
	int[][] discoveredMap;
	int[][] itemMap;
	int mapW, mapH;
	
	MonsterList monsters;

	public DungeonMap(int w, int h) {
		mapW = w;
		mapH = h;
		map = new int[mapW][mapH];
		discoveredMap = new int[mapW][mapH];
		itemMap = new int[mapW][mapH];
		monsters = new MonsterList();
	}
	
	void updateDiscoveredMap(Point2D p, int dir) {
		int sightLength = 10;
		double sightIncrements = 0.3;
		double degreeIncrements = 1;
		
		double degStart, degEnd;
		
		double expand = 15;
		//north is 0
		if(dir == Directions.UP) {
			degStart = (-90)-expand;
			degEnd = 90 + expand;
		} else if(dir == Directions.RIGHT) {
			degStart = 0 - expand;
			degEnd = 180 + expand;
		} else if(dir == Directions.DOWN) {
			degStart = 90 - expand;
			degEnd = 270 + expand;
		} else if(dir == Directions.LEFT) {
			degStart = 180 - expand;
			degEnd = 360 + expand;
		} else {
			return;
		}
		
		int x,y;
		
		for(double degree = degStart; degree<degEnd; degree+=degreeIncrements) {
			for(double h = 0; h < sightLength; h += sightIncrements) {
				x = (int) (p.getX() + Math.round((Math.sin(Math.toRadians(degree))*h)));
				y = (int) (p.getY() - Math.round((Math.cos(Math.toRadians(degree))*h)));
				
				if (map[x][y] == Terrain.WALL) {
					discoveredMap[x][y] = 1;
					break;
				} else if (map[x][y] == Terrain.VOID){
					break;
				}
				discoveredMap[x][y] = 1;
			}
		}
		
		
	}
	
	void setPosition(Point2D p, int t) {
		map[(int) p.getX()][(int) p.getY()] = t;
	}
	
	void makeVoidMap() {
		map = new int[mapW][mapH];
		for (int r = 0; r < mapH; r++) {
			for (int c = 0; c < mapW; c++) {
				discoveredMap[c][r] = 0;
				map[c][r] = Terrain.VOID;
			}
		}
	}

	void makeRoom(Room room) {
		int type;
		if (room.walls) {
			for (int r = (int) room.tl.getY() - 1; r < room.tl.getY() + room.h + 1; r++) {
				for (int c = (int) room.tl.getX() - 1; c < room.tl.getX() + room.w + 1; c++) {
					if (r < 0 || c < 0 || r >= mapH || c >= mapW) {
						continue;
					}
					
					type = map[c][r];
					if (type == Terrain.VOID || type == Terrain.WALL) {
						map[c][r] = Terrain.WALL;
					}
				}
			}
		}
		for (int r = (int) room.tl.getY(); r < room.tl.getY() + room.h; r++) {
			for (int c = (int) room.tl.getX(); c < room.tl.getX() + room.w; c++) {
				if (r < 0 || c < 0 || r >= mapH || c >= mapW) {
					continue;
				}
				type = map[c][r];
				if (type == Terrain.VOID || type == Terrain.WALL || type == Terrain.EMPTY)
					map[c][r] = Terrain.EMPTY;
			}
		}
		if(room.starting) {
			System.out.println("Made Ending: " + room.getCenter());
			setPosition(room.getCenter(), Terrain.DOWNSTAIR);
		}
		if(room.ending) {
			System.out.println("Made Ending: " + room.getCenter());
			setPosition(room.getCenter(), Terrain.UPSTAIR);
		}
	}

	void makeRoom(Point2D start, double length, double height) {
		makeRoom(new Room(start, length, height));
	}

	void makeHallway(Point2D start, Point2D end, double width, boolean overFirst) {
		int sx, sy, ex, ey;
		
		sx = (int) start.getX();
		sy = (int) start.getY();
		ex = (int) end.getX();
		ey = (int) end.getY();
		
		if (overFirst) {
			makeRoom(new Point2D(sx, sy), ex - sx, width);
			makeRoom(new Point2D(ex, sy), width, width);
			makeRoom(new Point2D(ex, sy), width, ey - sy);
		} else {
			makeRoom(new Point2D(sx, sy), width, ey - sy);
			makeRoom(new Point2D(sx, ey), width, width);
			makeRoom(new Point2D(sx, ey), ex - sx, width);
		}
	}

	int findNearest(ArrayList<Room> roomList, Room r, boolean connected) {
		Iterator<Room> roomIterator = roomList.iterator();
		Room thisRoom;
		int i = 0;
		int iClosest = -1;
		double dist;
		double minDist = mapW * mapH;

		while (roomIterator.hasNext()) {
			thisRoom = roomIterator.next();

			if (thisRoom==r)
				continue;
			if (thisRoom.connected != connected)
				continue;
			

			dist = r.centerDistance(thisRoom);
			if (dist < minDist) {
				iClosest = i;
			}
			i++;
		}
		return iClosest;
	}
	
	boolean allConnected(ArrayList<Room> roomList) {
		for(int i = 0; i<roomList.size(); i++) { 
			if (!roomList.get(i).connected)
				return false;
		}
		return true;
	}
	
	int findUnconnected(ArrayList<Room> roomList) {
		for(int i = 0; i<roomList.size(); i++) { 
			if (!roomList.get(i).connected)
				return i;
		}
		return -1;
	}

	public Point2D generate() {

		makeVoidMap();

		int roomMinSize = 5;
		int roomMaxSize = 10;
		double maxDistApart = 25;
		double roomToVoidRatioTarget = 0.2;
		double roomToVoidRatioActual = 0;
		double monsterRate = 0.4;

		ArrayList<Room> roomList = new ArrayList<Room>();
		Room thisRoom;
		Point2D testLocation;
		int iRoom, iNearC, iNearU;
		double distC, distU;

		while (roomToVoidRatioActual < roomToVoidRatioTarget) {
			testLocation = new Point2D(1 + rng.nextInt(mapW - roomMaxSize - 1),
					1 + rng.nextInt(mapH - roomMaxSize - 1));
			thisRoom = new Room(testLocation, roomMinSize + rng.nextInt(roomMaxSize-roomMinSize),
					roomMinSize + rng.nextInt(roomMaxSize-roomMinSize));
			
			if(rng.nextDouble() < monsterRate && roomList.size()>1) {
				monsters.addRandomMonster(thisRoom.getCenter());
			}
			roomList.add(thisRoom);
			roomToVoidRatioActual += (thisRoom.getSize() / (mapW * mapH));
		}
		
		iRoom = 0;
		roomList.get(iRoom).setStarting();
		makeRoom(roomList.get(iRoom));
		roomList.get(iRoom).connected = true;
		
		while(!allConnected(roomList)) {
			
			iRoom = findUnconnected(roomList);
			
			iNearC = findNearest(roomList, roomList.get(iRoom), true);
			iNearU = findNearest(roomList, roomList.get(iRoom), false);
			distC = maxDistApart+1;
			distU = maxDistApart+1;
			if(!(iNearC<0)) {
				distC = roomList.get(iRoom).centerDistance(roomList.get(iNearC));
			}
			if(!(iNearU<0)) {
				distU = roomList.get(iRoom).centerDistance(roomList.get(iNearU));
			}
			
			if(distC<maxDistApart) {
				makeRoom(roomList.get(iRoom));
				if(!roomList.get(iRoom).overlap(roomList.get(iNearC))) {
					makeHallway(roomList.get(iRoom).getCenter(), roomList.get(iNearC).getCenter(), 1 + rng.nextInt(2), rng.nextBoolean());
				}
				roomList.get(iRoom).connected = true;
				continue;
			} else if(distU < distC) {
				if(iNearU<0) break;
				iNearC = findNearest(roomList, roomList.get(iNearU), true);
				if(iNearC<0) break;
				distC = roomList.get(iRoom).centerDistance(roomList.get(iNearC));
				if(distC<maxDistApart) {
					thisRoom = roomList.get(iNearU);
					roomList.remove(iNearU);
					roomList.add(iRoom,thisRoom);
					continue;
				}
				
			}
			
			thisRoom = roomList.get(iRoom);
			roomList.add(thisRoom);
			roomList.remove(iRoom);
			maxDistApart += 0.1;
		}

		roomList.get(iRoom).setEnding();
		makeRoom(roomList.get(iRoom));
		iRoom = 0;
		while (!roomList.get(iRoom).starting) {
			iRoom++;
		}
		return roomList.get(iRoom).getCenter();

	}
	
	public boolean getVisible(double x, double y) {
		if (x < 0 || y < 0 || x >= mapW || y >= mapH) {
			return false;
		}
		return discoveredMap[(int) x][(int) y] == 1;
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

class Room {

	Point2D tl;
	double w, h;
	boolean connected;

	boolean walls;
	boolean starting;
	boolean ending;
	int enemy;
	int chest;
	int boss;

	public Room(Point2D tl, double w, double h) {
		if(w<0) {
			tl = tl.add(w,0);
			w *= -1;
		}
		if(h<0) {
			tl = tl.add(0,h);
			h *= -1;
		}
		this.tl = tl;
		this.w = w;
		this.h = h;
		this.connected = false;
		this.walls = true;
		this.starting = false;
		this.ending = false;
		this.enemy = 0;
		this.chest = 0;
		this.boss = 0;
	}

	public double centerDistance(Room r) {
		return getCenter().distance(r.getCenter());
	}

	public boolean overlap(Point2D p) {
		return overlap(new Room(p, 1, 1));
	}

	public boolean overlap(Room r) {
		return ((r.tl.getX()<tl.getX()+this.w) && (r.tl.getX()+r.w>tl.getX()) && (r.tl.getY()<tl.getY()+this.h) && (r.tl.getY()+r.h>tl.getY()));

//		Point2D d = xyDistance(r);
//		return d.getX() < 0 || d.getY() < 0;
	}

	public Point2D getCenter() {
		return new Point2D((int) (tl.getX() + w / 2), (int) (tl.getY() + h / 2));
	}

	public double getSize() {
		return w * h;
	}

	public void setWalls(boolean walls) {
		this.walls = walls;
	}

	public void setStarting() {
		this.starting = true;
	}

	public void setEnding() {
		this.ending = true;
	}

	public void setEnemy(int enemy) {
		this.enemy = enemy;
	}

	public void setChest(int chest) {
		this.chest = chest;
	}

	public void setBoss(int boss) {
		this.boss = boss;
	}

}
