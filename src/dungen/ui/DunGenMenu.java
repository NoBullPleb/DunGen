package dungen.ui;

import java.awt.Point;
import java.util.HashMap;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import dungen.generators.Dice;
import dungen.generators.Tables;
import dungen.pojos.Dungeon;
import dungen.pojos.Hoard;
import dungen.pojos.Room;
import dungen.pojos.npcs.*;
import dungen.pojos.Treasure;

@SuppressWarnings("serial")
public class DunGenMenu extends JMenuBar {

	public DunGenMenu() {
		JMenu file = new JMenu("File");
		this.add(file);
		JMenuItem New = new JMenuItem("New");
		New.addActionListener(e -> {
			Controls.hideRoom();
			Controls.showSecrets = true;
			Controls.rooms = new HashMap<>();
			Room.numRooms = 1;
			Room.neverTellMeTheOdds = 90;
			Controls.thisRoom = new Room();
			Controls.thisRoom.addDoor("north");
			Controls.thisRoom.removeDoor("south");
			Controls.rooms.put(new Point(0, 0), Controls.thisRoom);
			InfoPanel.setPartyLevel(1);
			Controls.showX = 0;
			Controls.showY = 0;
			Controls.mapView.redraw(true);
			InfoPanel.setPartySize(4);
			InfoPanel.setAllTypesFalse();
			Controls.showRoom();
		});
		file.add(New);
		JMenuItem Save = new JMenuItem("Save");
		Save.addActionListener(new Dungeon()::save);
		file.add(Save);

		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(Controls::load);
		file.add(load);
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(e -> System.exit(0));
		file.add(exit);

		JMenu generate = new JMenu("Generate");
		JMenuItem trap = new JMenuItem("Trap");
		trap.addActionListener(e -> {
			new DunGenPop("Trap Generated", Tables::getTrap);
		});
		JMenuItem treasure = new JMenuItem("Treasure");
		treasure.addActionListener(e -> {
			new DunGenPop("Treasure Generated", Treasure::getTreasure);
		});
		JMenuItem trick = new JMenuItem("Trick");
		trick.addActionListener(e -> {
			new DunGenPop("Trick Generated", Tables::getTrick);
		});
		JMenu encounter = new JMenu("Encounter");
		JMenuItem deadly = new JMenuItem("Deadly");
		deadly.addActionListener(e -> {
			new DunGenPop("Deadly Encounter Generated",
					() -> Tables.getEncounter(Tables.deadlyTable[InfoPanel.getPartySize()]));
		});
		JMenuItem hard = new JMenuItem("Hard");
		hard.addActionListener(e -> new DunGenPop("Hard Encounter Generated",
				() -> Tables.getEncounter(Tables.hardTable[InfoPanel.getPartySize()])));
		JMenuItem medium = new JMenuItem("Medium");
		medium.addActionListener(e -> new DunGenPop("Medium Encounter Generated",
				() -> Tables.getEncounter(Tables.mediumTable[InfoPanel.getPartySize()])));
		JMenuItem easy = new JMenuItem("Easy");
		easy.addActionListener(e -> new DunGenPop("Easy Encounter Generated",
				() -> Tables.getEncounter(Tables.easyTable[InfoPanel.getPartySize()])));
		encounter.add(deadly);
		encounter.add(hard);
		encounter.add(medium);
		encounter.add(easy);

		JMenuItem hazard = new JMenuItem("Hazard");
		hazard.addActionListener(e -> new DunGenPop("Hazard Generated", Tables::getHazard));
		JMenu npcs = new JMenu("NPCs");
		JMenuItem npc = new JMenuItem("Commoner");
		npc.addActionListener(e -> new DunGenPop("NPC Generated", () -> new NPC("Commoner").toString()));
		JMenuItem wizard = new JMenuItem("Wizard");
		wizard.addActionListener(e -> new DunGenPop("NPC Generated", () -> new Wizard().toString()));

		JMenuItem sorcerer = new JMenuItem("Sorcerer");
		sorcerer.addActionListener(e -> new DunGenPop("NPC Generated", () -> new Sorcerer().toString()));
		npcs.add(npc);
		npcs.add(wizard);
		npcs.add(sorcerer);

		JMenuItem hoard = new JMenuItem("Hoard");
		hoard.addActionListener(e -> new DunGenPop("Hoard Generated", () -> Hoard.getHoard()));

		JMenu items = new JMenu("Items");
		JMenu scrolls = new JMenu("Scrolls");
		JMenuItem[] levels = new JMenuItem[10];
		int i = 0;
		while (i < levels.length) {
			final int x = i;
			levels[x] = new JMenuItem("Level " + x);
			levels[x].addActionListener(e -> new DunGenPop("Scroll Level " + x, () -> Hoard.generateScroll(x)));
			scrolls.add(levels[x]);
			i++;
		}

		JMenuItem anyItem = new JMenuItem("Any Magic Item");
		anyItem.addActionListener(e -> new DunGenPop("Item Generated", () -> Hoard.getMagicItem("")));
		JMenuItem sentientItem = new JMenuItem("Sentient Magic Item");
		sentientItem
				.addActionListener(e -> new DunGenPop("Sentient Item Generated", () -> Hoard.getSentientMagicItem("")));
		JMenuItem rareItem = new JMenuItem("Rare Item");
		rareItem.addActionListener(e -> new DunGenPop("Rare Item Generated", () -> Hoard.getMagicItem("Rare")));
		JMenuItem veryRareItem = new JMenuItem("Very Rare Item");
		veryRareItem.addActionListener(
				e -> new DunGenPop("Very Rare Item Generated", () -> Hoard.getMagicItem("Very Rare")));
		JMenuItem legendaryItem = new JMenuItem("Legendary Item");
		legendaryItem.addActionListener(
				e -> new DunGenPop("Legendary Item Generated", () -> Hoard.getMagicItem("Legendary")));
		JMenuItem uncommonItem = new JMenuItem("Uncommon Item");
		uncommonItem
				.addActionListener(e -> new DunGenPop("Uncommon Item Generated", () -> Hoard.getMagicItem("Uncommon")));
		JMenuItem commonItem = new JMenuItem("Common Item");
		commonItem.addActionListener(e -> new DunGenPop("Common Item Generated", () -> Hoard.getMagicItem("Common")));

		items.add(anyItem);
		items.add(scrolls);
		items.add(sentientItem);
		items.add(legendaryItem);
		items.add(veryRareItem);
		items.add(rareItem);
		items.add(uncommonItem);
		items.add(commonItem);
		JMenu poisons = new JMenu("Poisons");
		JMenuItem anyPoison = new JMenuItem("Any Poison");
		anyPoison.addActionListener(e -> new DunGenPop("Poison Generated", () -> Tables.getPoison("")));
		JMenuItem injuryPoison = new JMenuItem("Injury");
		injuryPoison.addActionListener(e -> new DunGenPop("Poison Generated", () -> Tables.getPoison("Injury")));
		JMenuItem contactPoison = new JMenuItem("Contact");
		contactPoison.addActionListener(e -> new DunGenPop("Poison Generated", () -> Tables.getPoison("Contact")));
		JMenuItem inhaledPoison = new JMenuItem("Inhaled");
		inhaledPoison.addActionListener(e -> new DunGenPop("Poison Generated", () -> Tables.getPoison("Inhaled")));
		JMenuItem ingestedPoison = new JMenuItem("Ingested");
		ingestedPoison.addActionListener(e -> new DunGenPop("Poison Generated", () -> Tables.getPoison("Ingested")));
		poisons.add(anyPoison);
		poisons.add(contactPoison);
		poisons.add(ingestedPoison);
		poisons.add(inhaledPoison);
		poisons.add(injuryPoison);

		JMenuItem insult = new JMenuItem("Insult");
		insult.addActionListener(e -> new DunGenPop("Insult Generated", Tables::getInsult));
		generate.add(encounter);
		generate.add(hazard);
		generate.add(hoard);
		generate.add(items);
		generate.add(insult);
		generate.add(npcs);
		generate.add(poisons);
		generate.add(trap);
		generate.add(treasure);
		generate.add(trick);
		this.add(generate);

		JMenu mishaps = new JMenu("Mishaps");
		JMenuItem scroll = new JMenuItem("Scroll");
		scroll.addActionListener(e -> new DunGenPop("Scroll mishap!", Tables::scrollMishap));
		JMenuItem potion = new JMenuItem("Potion");
		potion.addActionListener(e -> new DunGenPop("Potion mishap!", Tables::potionMishap));
		JMenuItem attack = new JMenuItem("Attack");
		attack.addActionListener(e -> new DunGenPop("Attack mishap!", Tables::meleeMishap));
		JMenuItem injury = new JMenuItem("Injury");
		injury.addActionListener(e -> new DunGenPop("Injury mishap!", Tables::getInjury));
		JMenu chases = new JMenu("Chase Complication");
		JMenuItem urban = new JMenuItem("Urban");
		urban.addActionListener(e -> new DunGenPop("Urban Chase Complication!", Tables::getUrbanMishap));
		JMenuItem wilderness = new JMenuItem("Wilderness");
		wilderness.addActionListener(
				e -> new DunGenPop("Wilderness Chase Complication!", () -> Tables.getWildernessMishap()));
		chases.add(urban);
		chases.add(wilderness);

		JMenu madness = new JMenu("Madness");
		JMenuItem mad = new JMenuItem("Any");
		mad.addActionListener(e -> new DunGenPop("Any Madness", () -> Tables.getMadness(Dice.roll(3) - 1)));
		madness.add(mad);
		JMenuItem shortterm = new JMenuItem("Short Term");
		shortterm.addActionListener(e -> new DunGenPop("Short Term Madness", () -> Tables.getMadness(0)));
		madness.add(shortterm);

		JMenuItem longterm = new JMenuItem("Long Term");
		longterm.addActionListener(e -> new DunGenPop("Long Term Madness", () -> Tables.getMadness(1)));
		madness.add(longterm);
		JMenuItem indefinite = new JMenuItem("Indefinite");
		indefinite.addActionListener(e -> new DunGenPop("Indefinite Madness", () -> Tables.getMadness(2)));
		madness.add(indefinite);

		mishaps.add(attack);
		mishaps.add(chases);
		mishaps.add(injury);
		mishaps.add(potion);
		mishaps.add(madness);
		mishaps.add(scroll);

		this.add(mishaps);

		JMenu map = new JMenu("Map");
		JMenuItem DMView = new JMenuItem("DM View");
		DMView.addActionListener(e -> {
			Controls.showSecrets = true;
			Controls.hideRoom();
			Controls.mapView.redraw(Controls.showSecrets);
			Controls.mapView.moveStar(Controls.showX, Controls.showY);
			Controls.showRoom();
		});
		JMenuItem playerView = new JMenuItem("Player View");
		playerView.addActionListener(e -> {
			Controls.showSecrets = false;
			Controls.hideRoom();
			Controls.mapView.redraw(Controls.showSecrets);
			Controls.mapView.moveStar(Controls.showX, Controls.showY);
			Controls.showRoom();
		});
		map.add(DMView);
		map.add(playerView);
		this.add(map);
		JMenu tools = new JMenu("Tools");
		JMenuItem monPop = new JMenuItem("Monster Stats");
		monPop.addActionListener(e -> new MonsterPop().setVisible(true));
		tools.add(monPop);
		JMenuItem monsterCalc = new JMenuItem("Monster Calculator");
		monsterCalc.addActionListener(e -> new MonsterCalc().setVisible(true));
		tools.add(monsterCalc);
		this.add(tools);
	}
}
