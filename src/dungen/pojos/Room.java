package dungen.pojos;

import java.io.Serializable;

import dungen.generators.Tables;
import dungen.ui.InfoPanel;
import dungen.pojos.npcs.*;

public class Room implements Serializable {

	private static final long serialVersionUID = -4916367751063974331L;
	private static int numRooms = 1;
	public int roomNumber = numRooms;
	private static int neverTellMeTheOdds = 90;
	public boolean drawn = false;
	public String details = "";
	public boolean hasNPCs = InfoPanel.getSpwnNpcs()
			&& Math.random() * 100 > 98; // 2% ODDS OF NPCS

	public Boolean north = (Math.random() * 100) > 100 - neverTellMeTheOdds,
			south = (Math.random() * 100) > 100 - neverTellMeTheOdds,
			east = (Math.random() * 100) > 100 - neverTellMeTheOdds,
			west = (Math.random() * 100) > 100 - neverTellMeTheOdds;
	public Room northRoom = null, southRoom = null, westRoom = null,
			eastRoom = null;

	public void addDoor(String direction) {
		if (direction.equalsIgnoreCase("north")) {
			north = true;
		} else if (direction.equalsIgnoreCase("south")) {
			south = true;
		} else if (direction.equalsIgnoreCase("east")) {
			east = true;
		} else if (direction.equalsIgnoreCase("west")) {
			west = true;
		}

	}

	public Room() {
		numRooms++;
		if (roomNumber > 1) {
			// If it has NPCs, generate the party. 1-3 adventurers.
			if (hasNPCs) {
				String npcClass = Tables.getNpcClass();
				NPC n = null;
				if (npcClass.equalsIgnoreCase("Wizard"))
					n = new Wizard();
				else if (npcClass.equalsIgnoreCase("Sorcerer"))
					n = new Sorcerer();
				else
					n = new NPC(npcClass);
				details += n.toString() + "\n";
			}
			details += Tables.getEvent();
		} else
			details = "This is the room where it all began... ";
		neverTellMeTheOdds -= 2 * InfoPanel.dungeonSize();
	}
}
