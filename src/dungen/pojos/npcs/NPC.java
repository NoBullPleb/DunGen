package dungen.pojos.npcs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import dungen.generators.Dice;
import dungen.generators.Tables;
import dungen.ui.InfoPanel;

public class NPC implements Serializable {
	protected static final long serialVersionUID = 2954732759119047136L;
	protected String alignment = Tables.getAlignment();
	protected String ideals = Tables.getIdeals(alignment);
	protected String npcClass = Tables.getNpcClass();
	protected String interactive = Tables.getInteractiveTrait();
	protected String specialty = Tables.getSpecialty();
	protected String quirk = Tables.getQuirk();
	@SuppressWarnings("unchecked")
	protected ArrayList<String>[] spells = new ArrayList[10];
	{
		Arrays.parallelSetAll(spells, ArrayList<String>::new);
	}
	protected int[][] resources = new int[20][10];

	protected int lvl = Math.max(
			InfoPanel.getPartyLevel()
					+ (Dice.roll(5) - 3), 1);
	public int Int = Dice.statroll(), Wis = Dice.statroll(), Cha = Dice
			.statroll(), Dex = Dice.statroll(), Str = Dice.statroll(),
			Con = Dice.statroll();

	public String getAlignment() {
		return alignment;
	}

	public String getNpcClass() {
		return npcClass;
	}

	public int getLvl() {
		return lvl;
	}

	public NPC(String npcclass) {
		npcClass = npcclass;
	}

	public String getStats() {
		return "(Int:" + Int + ", Wis:" + Wis + ", Cha:" + Cha + ", Str:" + Str
				+ ", Dex:" + Dex + ", Con:" + Con + ")";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[").append(alignment).append("_")
				.append("NPC_");
		sb.append("Level_").append(lvl).append("_").append(npcClass)
				.append("_").append(ideals).append("_").append(quirk)
				.append("_").append(interactive).append("_").append(specialty)
				.append("_").append(getStats()).append("]");
		for (int i = 0; i < spells.length; i++) {
			if (spells[i] != null && spells[i].size() > 0) {
				sb.append("\n__LEVEL " + i + " SPELLS__ ");
				if (i > 0 && resources[lvl - 1][i - 1] != 0)
					sb.append(" uses:" + resources[lvl - 1][i - 1]);
				spells[i].stream().forEach(
						e -> sb.append("\n").append(
								e.substring(e.indexOf(',') + 1)));
			}
		}

		return sb.toString();
	}

	public String getInteractive() {
		return interactive;
	}

	public String getQuirk() {
		return quirk;
	}

}