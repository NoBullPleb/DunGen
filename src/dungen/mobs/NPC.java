package dungen.mobs;

import dungen.generators.Tables;

public class NPC {
	public String alignment = Tables.getAlignment();
	public String npcClass = Tables.getNpcClass();
	public int lvl = 0;

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
}