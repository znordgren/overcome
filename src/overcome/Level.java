package overcome;

import java.util.Iterator;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Level {

	long score;
	PlayField field;
	Player player;
	MonsterList monsters;

	void initialize() {
		score = 0;
		field = new PlayField(Main.WIDTH, Main.PLAY_HEIGHT);
	}

	public void createLevel() {
		field = new PlayField(Main.WIDTH, Main.PLAY_HEIGHT);
		field.generateField();
		player = new Player(field.x, field.y);
		field.dungeonMap.updateDiscoveredMap(player.getPosition(), Directions.DOWN);
		field.dungeonMap.updateDiscoveredMap(player.getPosition(), Directions.UP); // see whole starting room so it is a little less disorienting
		monsters = new MonsterList();
		monsters.addRandomMonster(new Point2D(field.x,field.y));
		monsters.addRandomMonster(new Point2D(field.x, field.y));
		monsters.sortByInitiative();
		System.out.println("I finished Creating the level");
	}

	public void spin(Requests r) {
		boolean newTurn = false;
		if (r.refresh == 1) {
			createLevel();
			return;
		}
		if (r.move != Directions.STOPPED) {
			if (r.move == player.getFacing()) {
				processMove(r.move);
				newTurn = true;
			} else {
				player.setFacing(r.move);
			}
			field.dungeonMap.updateDiscoveredMap(player.getPosition(), player.facing);
		}
		if (r.shoot == 1) {
			// bulletList.shoot(player);
			// newTurn = true;
		}

		if (newTurn) {
			processTurn();
		}

	}

	public void processMove(int m) {
		// a turn occurs only if the player moves. The player can turn in place
		// without moving
		if (field.checkMove(m, player.stats.speed)) {
			player.setFacing(m);
			player.setPosition(field.move(m, player.stats.speed));
			System.out.println("current position: " + player.getPosition());
		}
	}

	public void processTurn() {

		Iterator<Sprite> m = monsters.iterator();
		while (m.hasNext()) {
			Sprite monster = m.next();
			if (monster.stats.enabled) {
				monster.update(field);
			}
		}

	}

	public void render(GraphicsContext gc) {

		field.render(gc);
		field.placeSprite(gc, player);

		Iterator<Sprite> m = monsters.iterator();
		while (m.hasNext()) {
			Sprite monster = m.next();
			if (monster.stats.enabled && monster.stats.visible) {
				field.placeSprite(gc, monster);
			}
		}

		// field.placeBullets(bulletList);
	}

	public String getPlayerHealth() {
		return Double.toString(player.stats.health);
	}
}
