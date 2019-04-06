package overcome;

/**
 * Class for maintaining all of the stats about a creature
 * 
 * @author Zachary Nordgren
 * @version 3/21/2019
 *
 */
public class Stats
{
	//stats
	public double initiative;	// 0-99 turn order, higher initiative goes first
	public double health;		// current health of creature
	public double defense; 		// 0-99 defense of creature
	public double weight; 		// 0-999 weight of creature
	public double speed; 		// 0-3 number of tiles creature can move per turn
	public double power; 		// 0-99 attack damage modifier
	public double range; 		// 0-9 how far away creature can attack
	public double dexterity; 	// 0-99 how easily a creature can dodge an attack
	public double intelligence; // 0-99 how well the creature can track you
	public double level; 		// not sure if I want to use this yet
	
	//conditions
	public boolean animate; 	// if creature can move or not
	public boolean onScreen; 	// if creature is currently on screen
	public boolean alive; 		// dead creatures display the grave marker for a turn and then disappear
	public boolean enabled; 	// turns on or off updating and rendering of sprite
	public boolean visible;		// turns on or off rendering of creature
	public boolean poisoned; 	// poisoned creatures take damage per turn scaled to their defense
	public boolean prone; 		// creatures who are prone cannot move and have range of 1
	public boolean asleep; 		// creatures who are asleep cannot move or attack
	
	public void makeInanimate()
	{
		this.animate = false;
		this.enabled = true;
		this.visible = true;
	}
	
	public void makeAnimate()
	{
		this.animate = true;
		this.alive = true;
		this.enabled = true;
		this.visible = true;
		clearStatsEffects();
	}
	
	public void kill()
	{
		this.alive = false;
		this.animate = false;
	}
	
	public void setStats(double initiative, double health, double defense, double weight, double speed, double power, double range, double dexterity, double intelligence)
	{
		this.initiative = initiative;
		this.health = health;
		this.defense = defense;
		this.weight = weight;
		this.speed = speed;
		this.power = power;
		this.range = range;
		this.dexterity = dexterity;
		this.intelligence = intelligence;
	}
	
	public void clearStatsEffects()
	{
		this.visible = true;
		this.poisoned = false;
		this.prone = false;
		this.asleep = false;
	}
	
}
