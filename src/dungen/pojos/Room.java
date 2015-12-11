package dungen.pojos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import dungen.generators.Dice;
import dungen.generators.Tables;
import dungen.ui.InfoPanel;
import dungen.pojos.npcs.*;
import dungen.resourceLoader.ResourceLoader;

public class Room implements Serializable {

	private static final long serialVersionUID = -4916367751063974331L;
	public static int numRooms = 1;
	public int roomNumber = numRooms;
	public static int neverTellMeTheOdds = 90;
	public boolean drawn = false;
	public String details = "";
	public boolean hasNPCs = InfoPanel.getSpwnNpcs()
			&& Math.random() * 100 > 98; // 2% ODDS OF NPCS
	private static List<String> doorTypes = ResourceLoader
			.getTable("Door Type.txt");
	private HashMap<String, String> doors = new HashMap<String, String>();

	public Room northRoom = null, southRoom = null, westRoom = null,
			eastRoom = null;

	private String maybeDoor() {
		if ((Math.random() * 100) > 100 - neverTellMeTheOdds)
			return Tables.getResultFromTable(Dice.d20(), doorTypes);
		else
			return "";
	}

	public Set<Entry<String, String>> getDoors() {
		return doors.entrySet();
	}

	public String setDoor(String direction, String description) {
		return doors.put(direction, description);
	}

	public String getDoor(String direction) {
		return doors.getOrDefault(direction, "");
	}

	public void addDoor(String direction) {
		doors.put(direction, Tables.getResultFromTable(Dice.d20(), doorTypes));
	}

	public void removeDoor(String direction) {
		doors.put(direction, "");
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
		doors.put("north", maybeDoor());
		doors.put("west", maybeDoor());
		doors.put("east", maybeDoor());
		doors.put("south", maybeDoor());
	}

	public boolean hasDoor(String string) {
		return !doors.getOrDefault(string, "").isEmpty();
	}
}
