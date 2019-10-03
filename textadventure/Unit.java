package textadventure;

/** Represents a Unit in combat that can attack and take damage */
public class Unit {
	/** Name of unit  */
	public String name;
	/** Current health of unit */
	public int hp;
	/** Max health of unit */
	public int maxHp;
	
	/** Damage of unit. */
	public int damage;
	/** Speed of unit. Currently does nothing. */
	public int speed;
	
	public Unit(String name, int hp, int damage, int speed) {
		this.name = name;
		this.hp = hp;
		this.maxHp = hp;
		this.damage = damage;
		this.speed = speed;
	}
	
	public String toString() {
		// Do some formatting on the unit's stats
		return Main.rightPad(name + "'s Stats:", 20)
				+ " Health: " + Main.leftPad(""+hp, 4) + " / " + Main.leftPad("" + maxHp, 4)
				+ " Damage: " + Main.leftPad(""+damage, 4)
				+ " Speed: " + Main.leftPad(""+speed, 3);
	}
}
