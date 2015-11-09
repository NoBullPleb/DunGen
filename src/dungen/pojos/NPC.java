package dungen.pojos;

import java.io.Serializable;

import dungen.generators.Tables;

public class NPC implements Serializable {
	private static final long serialVersionUID = 2954732759119047136L;
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