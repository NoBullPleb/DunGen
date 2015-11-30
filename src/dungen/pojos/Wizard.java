package dungen.pojos;

import java.util.Arrays;

import dungen.generators.Dice;

@SuppressWarnings("serial")
public class Wizard extends NPC {

	// Ensures properly prioritized stats
	public Wizard() {
		super();
		int[] stats = { Dice.statroll(), Dice.statroll(), Dice.statroll(),
				Dice.statroll(), Dice.statroll(), Dice.statroll() };
		Arrays.sort(stats);
		Int = stats[5];
		Dex = stats[4];
		Wis = stats[3];
		Con = stats[2];
		Cha = stats[1];
		Str = stats[0];
		npcClass = "Wizard";
	}
}
