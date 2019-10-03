/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textadventure;

/** Room that only lets the user return to the previous room */
public class LockedRoom extends Room {
	
	public LockedRoom(String name, String desc) {
		super(name, desc, 1);
	}
	
	@Override public Room[] getExits() {
		return new Room[] { GameState.previousRoom };
	}
	
	@Override public String[] getExitActions() {
		return new String[] { "Return." };
	}
	
}
