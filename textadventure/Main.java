package textadventure;

import java.util.Scanner;

/** Class which prepares and starts the game */
public class Main {
	
	/** Scanner to read input from user */
	public static Scanner stdIn = new Scanner(System.in);
	
	/** Locked room, user can't enter where they wanted to (yet) */
	public static Room lockedRoom = new LockedRoom("Locked Room", "You can't travel here yet!");
	
	/** Starting room */
	public static Room start = new Room("AzrakCity", "Tall stone buildings tower around you...", 3);
	
	/** A combat room with a lion, and a healing spring beyond. */
	public static CombatRoom laythDen = new CombatRoom("LaythDen", 
			"Beware of Lions!", 
			2, // how many exits from this room after victory
			new Unit("Lion", 100, 3, 15), // Foe to fight
			Main::unlockNamirForest); // Code to run on combat victory
	
	/** A room with a grue, and a different healing room beyond. */
	public static CombatRoom namirForest = new CombatRoom("NamirForest", 
			"It is dark, and you are likely to be eaten by a grue.",
			2,
			new Unit("Grue", 300, 5, 55),
			Main::unlockSandDesert);
	
	/** A room with the final boss. */
	public static CombatRoom lostSandDesert = new CombatRoom("LostSandDesert",
			"Dunes stretch as far as your eye can see.", 
			1,
			new Unit("Worm", 600, 8, 15),
			Main::winTheGame);
	
	/** A room that lets the player heal. */
	public static Room healingSpring = new HealingRoom("HealingSprings", "drink the healing water");
	/** Another room for healing */
	public static Room forestTemple = new HealingRoom("ForestTemple", "pray to the forest spirits");
	
	/** Victory room. */
	public static Room victory = new Room("Victory", "You win! Have a party!", 0);
	
	public static void main(String[] args) {
		// stitch the rooms together
		start.setExit(0, laythDen, "LaythDen");
		start.setExit(1, lockedRoom, "NamirForest");
		start.setExit(2, lockedRoom, "LostSandDesert");
		
		laythDen.setExit(0, healingSpring, "HealingSprings");
		laythDen.setExit(1, start, "Return to city");
		
		namirForest.setExit(0, forestTemple, "ForestTemple");
		namirForest.setExit(1, start, "Return to city");
		
		lostSandDesert.setExit(0, victory, "Celebrate");
		
		// lockedRoom and healingSprings rooms are special...
		//		they use the previousRoom static field from the GameState class 
		//		so they could be placed in multiple spots...
		
		// Initialize the game for the first time.
		initialize();
		
		// Begin the game!
		play();
	}
	
	/** Re-initialize the game state. Called when the player loses. */
	public static void initialize() {
		// Initial game state 
		GameState.currentRoom = start;
		GameState.previousRoom = null;
		GameState.player = new Unit("Player", 60, 12, 20);
		
		// In case the player is retrying, respawn the mobs.
		laythDen.resetFoe();
		namirForest.resetFoe();
		lostSandDesert.resetFoe();
		
		// And reconnect locked rooms in place of unlocks...
		start.setExit(1, lockedRoom, "NamirForest");
		start.setExit(2, lockedRoom, "LostSandDesert");
	}
	
	// Loop for playing the game 
	public static void play() {
		while (true) {
			// Intro text
			System.out.println("You wake up from an ominous dream in a cold sweat. ");
			System.out.println("There are people in a panic, running past you.");
			System.out.println("You decide to see what they are so afraid of.");
			// a throw-away, resuable variable for user choices.
			int choice;
			
			// Loop while the player hasn't won.
			while (GameState.player.hp > 0) {
				// Check for win condition
				if (GameState.currentRoom == victory) {
					System.out.println("Horray you win!");
					return;
				}
				
				// Print out some newlines to give space between rooms
				System.out.println("\n---------------------------------------------------");
				System.out.println("You are in " + GameState.currentRoom.name + ".");
				// Otherwise, print out current room info
				System.out.println(GameState.currentRoom.getDescription());

				// Get the exits and names of exit actions for the current room.
				Room[] exits = GameState.currentRoom.getExits();
				String[] exitActions = GameState.currentRoom.getExitActions();
				
				// Build a prompt to give to the user, showing them the exits
				String str = "Exits are: ";
				for (int i = 0; i < exitActions.length; i++) {
					// Offset index by +1 here so user choices start at 1...
					str += "\n" + (1+i) + ": " + exitActions[i];
				}
				str += "\nMake your decision. ";
				
				// Get a valid choice from the user
				// Offset user choice by -1 to pick the matching index (starting at 0...)
				choice = getNumberInRange(str, 1, exitActions.length) - 1;
				
				// Print out some newlines to give space between room messages
				// and transition (onExit) messages
				System.out.println("\n\n");

				// Tell our room what the user chose.
				GameState.currentRoom.onExit(choice);
				
				// Get the room the player will move into.
				Room chosenExit = exits[choice];
				
				// If we are not in the same room, update the previous room.
				if (chosenExit != GameState.currentRoom) {
					GameState.previousRoom = GameState.currentRoom;
					GameState.currentRoom = chosenExit;
				}
				
				// back to top of loop!
			}
			
			choice = getNumberInRange("Want to try again?\n\nEnter 1 to try again, or 2 to quit.", 1, 2);
			
			if (choice == 2) {
				break;
			} else {
				initialize();
			}

		}
		System.out.println("Okay, bye.");
	}
	
	/** Function which prevents the user from giving us funny buisness and crashing our game.
	 Repeatedly prints out a prompt and asks the user for a number, only stopping
	 when we get something we like. */
	public static int getNumberInRange(String prompt, int min, int max) {
		while (true) {
			System.out.print(prompt);
			String line = stdIn.nextLine();
			try {
				int num = Integer.parseInt(line);
				// Check number against requested range..
				if (num >= min && num <= max) { 
					return num;
				} else {
					// Complain to user 
					System.out.println("Sorry, " + num + " is not between [" + min + ", " + max + "]");
				}
			} catch (Exception e) {
				// Complain to user
				System.out.println("Sorry, {" + line + "} is not a valid number.");
			}
			
			System.out.print("Please enter a number between [" + min + ", " + max + "]");
		}
	}
	
	/** Function to call when the Lion is defeated. */
	public static void unlockNamirForest() {
		// Make the player a little stronger 
		System.out.println("\n\n");
		System.out.println("You feel stronger.");
		System.out.println("Damage increased by 10!");
		System.out.println("Max Health increased by 50!");
		GameState.player.damage += 10;
		GameState.player.maxHp += 50;
		// Unlock the next area 
		System.out.println("\n\n");
		System.out.println("You hear a wind blow, trees shaking in the distance.");
		System.out.println("It seems the forest spirits will let you in now.");
		start.setExit(1, namirForest, "NamirForest");
	}
	
	/** Function to call when the Grue is defeated. */
	public static void unlockSandDesert() {
		// Make the player a little stronger 
		System.out.println("\n\n");
		System.out.println("You feel stronger.");
		System.out.println("Damage increased by 20!");
		System.out.println("Max Health increased by 100!");
		GameState.player.damage += 20;
		GameState.player.maxHp += 100;
		// Unlock the next area 
		System.out.println("\n\n");
		System.out.println("You hear the distant shifting of sand.");
		System.out.println("It seems the desert will let you in now.");
		start.setExit(2, lostSandDesert, "LostSandDesert");
	}
	
	/** Function to call when the sand worm is defeated. */
	public static void winTheGame() {
		System.out.println("You defeated the sandworm! You win!");
	}
	
	/** Helper function to pad a string to be some number of characters long, with spaces on the left. */
	public static String leftPad(String str, int length) {
		while (str.length() < length) {
			str = " " + str;
		}
		return str;
	}
	/** Helper function to pad a string to be some number of characters long, with spaces on the right. */
	public static String rightPad(String str, int length) {
		while (str.length() < length) {
			str = str + " ";
		}
		return str;
	}
}
