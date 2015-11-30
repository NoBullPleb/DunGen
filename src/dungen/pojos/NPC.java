package dungen.pojos;

import java.io.Serializable;

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
	protected int lvl = Math
			.max(InfoPanel.partyLevel
					+ ((int) Math.floor(Math.random() * 5) - 2), 1);
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
		return sb.toString();
	}

	public String getInteractive() {
		return interactive;
	}

	public String getQuirk() {
		return quirk;
	}

}