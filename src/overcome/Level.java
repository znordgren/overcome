package overcome;

import java.util.Iterator;
import javafx.scene.canvas.GraphicsContext;

public class Level {

	double score;
	PlayField field;
	Player player;
	MonsterList monsters;
	Bullet bullet;

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
		monsters = field.dungeonMap.monsters;
		monsters.sortByInitiative();
		bullet = new Bullet();
		System.out.println("I finished Creating the level");
	}

	public int spin(Requests r) {
		boolean newTurn = false;
		if (player.stats.health < 0) {
			return 1;
		}
		if (r.refresh == 1) {
			createLevel();
			return 0;
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
			bullet.shoot(player,Directions.movePoint(player.x, player.y, player.facing, 10));
			newTurn = true;
		}

		if (newTurn) {
			processTurn();
		}
		
		return 0;

	}

	public void processMove(int m) {
		// a turn occurs only if the player moves. The player can turn in place
		// without moving
		if (field.checkMove(m, player.stats.speed)) {
			player.setFacing(m);
			player.setPosition(field.move(m, player.stats.speed));
			System.out.println("current position: " + player.getPosition());
		}
		if (field.checkEndOfLevel(player.getPosition())) {
			createLevel();
		}
	}

	public void processTurn() {
		
		Iterator<Monster> m = monsters.iterator();
		while (m.hasNext()) {
			Monster monster = m.next();
			if(bullet.enabled) {
				if(bullet.onTrajectory(monster.getPosition()) && monster.stats.alive) {
					monster.kill();
					score += 10;
					bullet.enabled = false;
				}
			}
			if (monster.stats.alive) {
				if(player.getPosition().distance(monster.x,monster.y)<2) {
					player.stats.health -= monster.stats.power*5;
				} else {
					monster.update(field, player.getPosition());
				}
				
			}
		}

	}

	public void render(GraphicsContext gc) {

		field.render(gc);
		
		Iterator<Monster> m = monsters.iterator();
		while (m.hasNext()) {
			Sprite monster = m.next();
			if (monster.stats.enabled && monster.stats.visible) {
				field.placeSprite(gc, monster);
			}
		}
		
		field.placeSprite(gc, player);

	}

	public String getPlayerHealth() {
		return Double.toString(player.stats.health);
	}
	
	public String getPlayerScore() {
		return Double.toString(this.score);
	}
}
