package dungen.pojos;

import java.util.Arrays;
import java.util.List;

import dungen.generators.Dice;
import dungen.generators.Tables;
import dungen.resourceLoader.ResourceLoader;

@SuppressWarnings("serial")
public class Sorcerer extends NPC {
	private static int[][] numSpells = {
			// 1
			{ 4, 2 },
			// 2
			{ 4, 3 },
			// 3
			{ 4, 3, 1 },
			// 4
			{ 5, 3, 2 },
			// 5
			{ 5, 3, 2, 1 },
			// 6
			{ 5, 3, 3, 2 },
			// 7
			{ 5, 3, 3, 2, 1 },
			// 8
			{ 5, 3, 3, 2, 2 },
			// 9
			{ 5, 3, 3, 2, 2, 1 },
			// 10
			{ 5, 3, 3, 2, 2, 2 },
			// 11
			{ 6, 3, 3, 2, 2, 2, 1 },
			// 12
			{ 6, 3, 3, 2, 2, 2, 1 },
			// 13
			{ 6, 3, 3, 2, 2, 2, 1, 1 },
			// 14
			{ 6, 3, 3, 2, 2, 2, 1, 1 },
			// 15
			{ 6, 3, 3, 2, 2, 2, 1, 1, 1 },
			// 16
			{ 6, 3, 3, 2, 2, 2, 1, 1, 1 },
			// 17
			{ 6, 3, 3, 3, 3, 2, 1, 1, 1, 1 },
			// 18
			{ 6, 3, 3, 3, 3, 2, 1, 1, 1, 1 },
			// 19
			{ 6, 3, 3, 3, 3, 2, 1, 1, 1, 1 },
			// 20
			{ 6, 3, 3, 3, 3, 2, 1, 1, 1, 1 } };
	// Ensures properly prioritized stats, gives spells
	private static List<String> sorSpellList = ResourceLoader
			.getTable("Sor Spells.txt");
	protected int[][] resources = {
			// 1
			{ 2 },
			// 2
			{ 3 },
			// 3
			{ 4, 2 },
			// 4
			{ 4, 3 },
			// 5
			{ 4, 3, 2 },
			// 6
			{ 4, 3, 3 },
			// 7
			{ 4, 3, 3, 1 },
			// 8
			{ 4, 3, 3, 2 },
			// 9
			{ 4, 3, 3, 3, 1 },
			// 10
			{ 4, 3, 3, 3, 2 },
			// 11
			{ 4, 3, 3, 3, 2, 1 },
			// 12
			{ 4, 3, 3, 3, 2, 1 },
			// 13
			{ 4, 3, 3, 3, 2, 1, 1 },
			// 14
			{ 4, 3, 3, 3, 2, 1, 1 },
			// 15
			{ 4, 3, 3, 3, 2, 1, 1, 1 },
			// 16
			{ 4, 3, 3, 3, 2, 1, 1, 1 },
			// 17
			{ 4, 3, 3, 3, 2, 1, 1, 1, 1 },
			// 18
			{ 4, 3, 3, 3, 2, 1, 1, 1, 1 },
			// 19
			{ 4, 3, 3, 3, 2, 1, 1, 1, 1 },
			// 20
			{ 4, 3, 3, 3, 2, 1, 1, 1, 1 } };

	public Sorcerer() {
		super("Sorcerer");
		super.resources = this.resources;
		int[] stats = { Dice.statroll(), Dice.statroll(), Dice.statroll(),
				Dice.statroll(), Dice.statroll(), Dice.statroll() };

		Arrays.sort(stats);
		Cha = stats[5];
		Con = stats[4];
		Wis = stats[3];
		Dex = stats[2];
		Int = stats[1];
		Str = stats[0];
		for (int i = 0; i < numSpells[lvl - 1].length; i++) {
			for (int x = 0; x < numSpells[lvl - 1][i]; x++) {
				spells[i].add(Tables.getSpell(i, sorSpellList));
			}
		}
	}
}
