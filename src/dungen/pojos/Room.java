package dungen.pojos;

import java.io.Serializable;
import java.util.ArrayList;

import dungen.generators.Tables;

public class Room implements Serializable {

	private static final long serialVersionUID = -4916367751063974331L;
	private static int numRooms = 1;
	public int roomNumber = numRooms;
	private static int neverTellMeTheOdds = 90;
	public boolean drawn = false;
	public String details = "";
	public boolean hasNPCs = Math.random() * 100 > 98; // 2% ODDS OF NPCS
	private ArrayList<NPC> party = new ArrayList<NPC>();

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
		if (roomNumber > 1)
			// If it has NPCs, generate the party. 1-3 adventurers.
			if (hasNPCs) {
				details += "NPCs: ";
				for (int i = 0; i < (Math.random() * 3); i++) {
					party.add(new NPC());
					details += "\n" + party.get(i).toString() + "\nSTATS: "
							+ party.get(i).getStats();

				}
			} else {
				details = Tables.getEvent();
			}
		else
			details = "This is the room where it all began... ";
		neverTellMeTheOdds -= 3;
		final StringBuilder sb = new StringBuilder();
		sb.append("Size of room: ").append(
				(int) Math.max(20 * Math.random(), 5));
		sb.append("\n" + details);
	}

}
