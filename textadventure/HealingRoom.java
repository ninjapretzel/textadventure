package textadventure;

/** Room that allows the player to heal themselves, and return to the previous room. */
public class HealingRoom extends Room {
	
	/** Custom action for healing */
	private final String healAction;
	
	public HealingRoom(String name, String healAction) {
		super(name, "You are in the " + name +".\nThis is a magical healing place.", 1);
		this.healAction = healAction;
	}
	
	/** Exit 0 is healing.*/
	@Override public void onExit(int exit) {
		if (exit == 0) {
			System.out.println("You " + healAction + ", and your wounds are healed.");
			GameState.player.hp = GameState.player.maxHp;
			System.out.println("\n\n" + GameState.player);
		}
	}
	
	@Override public Room[] getExits() {
		return new Room[] { this, GameState.previousRoom };
	}

	@Override public String[] getExitActions() {
		return new String[] { healAction, "Return" };
	}
	
}
