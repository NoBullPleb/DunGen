package dungen.pojos.npcs;

import java.util.Arrays;
import java.util.List;

import dungen.generators.Dice;
import dungen.generators.Tables;
import dungen.resourceLoader.ResourceLoader;

@SuppressWarnings("serial")
public class Wizard extends NPC {

	private static int[][] numSpells = {
			// 1
			{ 3, 2 },
			// 2
			{ 3, 3 },
			// 3
			{ 3, 4, 2 },
			// 4
			{ 4, 4, 3 },
			// 5
			{ 4, 4, 3, 2 },
			// 6
			{ 4, 4, 3, 3 },
			// 7
			{ 4, 4, 3, 3, 1 },
			// 8
			{ 4, 4, 3, 3, 2 },
			// 9
			{ 4, 4, 3, 3, 3, 1 },
			// 10
			{ 5, 4, 3, 3, 3, 2 },
			// 11
			{ 5, 4, 3, 3, 3, 2, 1 },
			// 12
			{ 5, 4, 3, 3, 3, 2, 1 },
			// 13
			{ 5, 4, 3, 3, 3, 2, 1, 1 },
			// 14
			{ 5, 4, 3, 3, 3, 2, 1, 1 },
			// 15
			{ 5, 4, 3, 3, 3, 2, 1, 1, 1 },
			// 16
			{ 5, 4, 3, 3, 3, 2, 1, 1, 1 },
			// 17
			{ 5, 4, 3, 3, 3, 2, 1, 1, 1, 1 },
			// 18
			{ 5, 4, 3, 3, 3, 3, 1, 1, 1, 1 },
			// 19
			{ 5, 4, 3, 3, 3, 3, 2, 1, 1, 1 },
			// 20
			{ 5, 4, 3, 3, 3, 3, 2, 2, 1, 1 } };

	// Ensures properly prioritized stats, gives spells
	private static List<String> wizSpellList = ResourceLoader
			.getTable("Wiz Spells.txt");

	public Wizard() {
		super("Wizard");
		int[] stats = { Dice.statroll(), Dice.statroll(), Dice.statroll(),
				Dice.statroll(), Dice.statroll(), Dice.statroll() };
		Arrays.sort(stats);
		Int = stats[5];
		Dex = stats[4];
		Wis = stats[3];
		Con = stats[2];
		Cha = stats[1];
		Str = stats[0];
		for (int i = 0; i < numSpells[lvl].length; i++) {
			for (int x = 0; x < numSpells[lvl][i]; x++) {
				spells[i].add(Tables.getSpell(i, wizSpellList));
			}
		}
	}
}
