package dungen.pojos;

import java.io.Serializable;

import dungen.generators.Dice;
import dungen.generators.Tables;
import dungen.ui.InfoPanel;

public class NPC implements Serializable {
	private static final long serialVersionUID = 2954732759119047136L;
	public String alignment = Tables.getAlignment();
	public String npcClass = Tables.getNpcClass();
	public int lvl = Math
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

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public String getStats() {
		return "[Int:" + Int + ", Wis:" + Wis + ", Cha:" + Cha + ", Str:" + Str
				+ ", Dex:" + Dex + ", Con:" + Con + "]";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[").append(alignment).append("_")
				.append("NPC_");
		sb.append("Level_").append(lvl).append("_").append(npcClass)
				.append("]");
		return sb.toString();
	}
}