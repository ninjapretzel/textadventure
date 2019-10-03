package textadventure;

/** A room that allows the player to fight an enemy. */
public class CombatRoom extends Room {
	/** Foe to fight. */
	public Unit foe;
	/** Copy of foe to fight, so we can respawn it. */
	public Unit originalFoe;
	/** Code to run when the user wins! */
	public Action onVictory;
	
	public CombatRoom(String name, String description, int numExits, Unit foe, Action onVictory) {
		super(name, description, numExits);
		this.foe = foe;
		this.originalFoe = new Unit(foe.name, foe.maxHp, foe.damage, foe.speed);
		this.onVictory = onVictory;
	}
	
	/** Reset the combat room by resetting the Foe */
	public void resetFoe() {
		foe = new Unit(originalFoe.name, originalFoe.maxHp, originalFoe.damage, originalFoe.speed);
	}
	
	@Override public String getDescription() {
		// Get assigned description...
		String prevDescription = super.getDescription();
		
		// ...then tack the state of combat
		if (foe.hp > 0) {
			return prevDescription + "\nA " + foe.name + " approaches!"
					// appending the foe/player Unit Objects to a String invokes their toString() method, and appends the result.
					+ "\n" + foe
					+ "\n" + GameState.player;
			
		} else {
			// ... or just the corpse if a defeated foe exists.
			return prevDescription + "\nA corpse of a " + foe.name + " lies motionless.";
		}
	}

	
	@Override public Room[] getExits() {
		// If the foe is alive, make the only options FIGHT and FLEE.
		if (foe.hp > 0) {
			return new Room[] { this, GameState.previousRoom };
		}
		// Otherwise allow the user to progress as intended. 
		return super.getExits();
	}

	@Override public String[] getExitActions() {
		// If the foe is alive, make the only options FIGHT and FLEE.
		if (foe.hp > 0) {
			return new String[] { "Fight!", "Flee!" };
		}
		// Otherwise allow the user to progress as intended. 
		return super.getExitActions(); 
	}
	
	@Override public void onExit(int exit) {
		// This is a combat zone, so we handle the user fighting their foe or fleeing...
		if (foe.hp > 0 && exit == 0) {
			
			System.out.println("The round of combat between " + GameState.player.name + " and the " + foe.name + " begins!");
			
			int damage = GameState.player.damage;
			System.out.println("You attack the " + foe.name + " for " + damage + " damage!");
			foe.hp -= damage;
			if (foe.hp <= 0) { 
				System.out.println("The " + foe.name + " is felled!");
				System.out.println("You are victorious!");
				
				// Maybe eventually we want fights that don't give you anything?	
				if (onVictory != null) {
					onVictory.execute();
				}
				return;
			}
			
			int foeDamage = foe.damage;
			System.out.println("The " + foe.name + " retaliates for " + foeDamage + " damage!");
			GameState.player.hp -= foeDamage;
			if (GameState.player.hp <= 0) {
				System.out.println("You are dead! You lose!");
				return;
			}
			
			System.out.println("Both you and the " + foe.name + " catch your breath...\n\n");
			
		} else if (foe.hp > 0 && exit == 1) {
			System.out.println("You flee like a coward! Best to live and fight another day!");
		}
		
			
			
		
	}
	
	
	
}
