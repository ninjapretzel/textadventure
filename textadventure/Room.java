package textadventure;

/** Base class for a room that can be entered and exited */
public class Room {
	/** Name of the room */
	public final String name;
	private final String description;
	private final Room[] exits;
	private final String[] exitActions;	
	
	/** Room constructor. Takes a description, and number of intended exits. */
	public Room(String name, String description, int numExits) {
		this.name = name;
		this.description = description;
		this.exits = new Room[numExits];
		this.exitActions = new String[numExits];
	}
	
	/** Overridable function that allows us to get the description from a room. */
	public String getDescription() {
		return description;
	}
	
	/** Allow this room to be exited into another room */
	public void setExit(int exit, Room room, String action) {
		exits[exit] = room;
		exitActions[exit] = action;
	}
	
	/** Overridable function for getting exit destinations from a room */
	public Room[] getExits() {
		return exits;
	}
	
	/** Overridable function for getting names of exits from a room */
	public String[] getExitActions() {
		return exitActions;
	}
	
	/** Overridable function for rooms to do custom stuff when the player picks an exit. */
	public void onExit(int exit) {	}
	
}
