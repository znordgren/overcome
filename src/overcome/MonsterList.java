package overcome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import javafx.geometry.Point2D;

public class MonsterList {

	public ArrayList<Sprite> monsters = new ArrayList<Sprite>();
	public int count;

	public MonsterFactory monsterFactory = new MonsterFactory();

	public MonsterList() {

	}

	public Iterator<Sprite> iterator() {
		return monsters.iterator();
	}

	public void addRandomMonster(Point2D startLocation) {
		Sprite monster = monsterFactory.makeMonster(startLocation);
		monsters.add(monster);
		count = monsters.size();
	}

	public void removeMonster(Sprite m) {
		monsters.remove(m);
		count = monsters.size();
	}

	public void sortByInitiative() {
		monsters.sort(new Comparator<Sprite>() {
			@Override
			public int compare(Sprite a, Sprite b) {
				return (int) (a.stats.initiative - b.stats.initiative);
			}
		});
	}

	public boolean checkCollision(Point2D p) {
		boolean hit = false;
		Iterator<Sprite> i = monsters.iterator();
		while (i.hasNext()) {
			hit = p == i.next().getPosition();
		}
		return hit;
	}

}
