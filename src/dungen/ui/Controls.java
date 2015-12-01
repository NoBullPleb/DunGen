package dungen.ui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

import dungen.generators.Tables;
import dungen.pojos.Dungeon;
import dungen.pojos.Hoard;
import dungen.pojos.Room;
import dungen.pojos.Treasure;

import javax.swing.JScrollPane;
import javax.swing.JLabel;

import dungen.pojos.*;

public class Controls extends JFrame {
	public static String version = "1.5.2";
	private static final long serialVersionUID = 7985611292217902489L;
	transient final static JButton southButton = new JButton("Go South"),
			eastButton = new JButton("Go East"), westButton = new JButton(
					"Go West"), northButton = new JButton("Go North");
	// used to track where rooms are. If one is already at that location, we can
	// load it.
	static JScrollPane scrollPane = new JScrollPane();
	public static JTextArea roomDetails = new JTextArea();
	private static JLabel lblRoomDetails = new JLabel("Room Details");
	public static HashMap<Point, Room> rooms = new HashMap<>();
	public static Room thisRoom = new Room();
	public static int showX = 0, showY = 0;
	private static int xLimit = 6, yLimit = 13;
	public static Map mapView = new Map();
	public static InfoPanel ip = new InfoPanel();
	private transient final static JPanel contentPane = new JPanel();
	private transient JMenuBar menuBar;

	private static void hideRoom() {
		String temp = roomDetails.getText();
		if (!thisRoom.details.equals(temp)) {
			thisRoom.details = temp;
			mapView.clearRoom(showX, showY);
			mapView.addEventOnRoom(showX, showY, temp, thisRoom.hasNPCs);
		}
		rooms.put(new Point(showX, showY), thisRoom);
		roomDetails.setText("");
	}

	private static void showRoom() {
		fixDoors();
		roomDetails.setText(thisRoom.details);

		lblRoomDetails.setText("Room Details: " + thisRoom.roomNumber);
		if (!thisRoom.drawn) {
			mapView.addRoom(showX, showY, thisRoom.details, thisRoom.hasNPCs);
			if (thisRoom.north)
				mapView.addHall(showX, showY, "north");
			if (thisRoom.south)
				mapView.addHall(showX, showY, "south");
			if (thisRoom.east)
				mapView.addHall(showX, showY, "east");
			if (thisRoom.west)
				mapView.addHall(showX, showY, "west");
			thisRoom.drawn = true;
		}
		mapView.moveStar(showX, showY);
		northButton.setEnabled(thisRoom.north);
		westButton.setEnabled(thisRoom.west);
		eastButton.setEnabled(thisRoom.east);
		southButton.setEnabled(thisRoom.south);
	}

	public static void load(ActionEvent e) {
		Dungeon d = Dungeon.load();
		hideRoom();
		InfoPanel.setLevel(d.partyLevel);
		Controls.mapView.star = d.mapView.star;
		Controls.mapView.roomsLocations = d.mapView.roomsLocations;
		Controls.mapView.halls = d.mapView.halls;
		Controls.mapView.rooms = d.mapView.rooms;
		Controls.showX = d.showX;
		Controls.showY = d.showY;
		Controls.rooms = d.rooms;
		Controls.thisRoom = d.thisRoom;
		Controls.mapView.redraw();
		for (int i = 0; i < d.types.size(); i++) {
			InfoPanel.setTruth(i, d.types.get(i));
		}
		showRoom();
	}

	public Controls() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("DunGen v" + version);
		setBounds(0, 50, 271, 580);
		thisRoom.north = true;
		thisRoom.south = false;
		thisRoom.west = false;
		thisRoom.east = false;
		thisRoom.details = "This is the room where it all began... ";
		rooms.put(new Point(showX, showY), thisRoom);
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu file = new JMenu("File");
		menuBar.add(file);

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
			new DunGenPop("Deadly Encounter Generated", () -> Tables
					.getEncounter(Tables.deadlyTable[InfoPanel.partySize]));
		});
		JMenuItem hard = new JMenuItem("Hard");
		hard.addActionListener(e -> new DunGenPop("Hard Encounter Generated",
				() -> Tables
						.getEncounter(Tables.hardTable[InfoPanel.partySize])));
		JMenuItem medium = new JMenuItem("Medium");
		medium.addActionListener(e -> new DunGenPop(
				"Medium Encounter Generated", () -> Tables
						.getEncounter(Tables.mediumTable[InfoPanel.partySize])));
		JMenuItem easy = new JMenuItem("Easy");
		easy.addActionListener(e -> new DunGenPop("Easy Encounter Generated",
				() -> Tables
						.getEncounter(Tables.easyTable[InfoPanel.partySize])));
		encounter.add(deadly);
		encounter.add(hard);
		encounter.add(medium);
		encounter.add(easy);

		JMenuItem hazard = new JMenuItem("Hazard");
		hazard.addActionListener(e -> new DunGenPop("Hazard Generated",
				() -> Tables.getHazard()));
		JMenu npcs = new JMenu("NPCs");
		JMenuItem npc = new JMenuItem("Any");
		npc.addActionListener(e -> new DunGenPop("NPC Generated",
				() -> new NPC().toString()));
		JMenuItem wizard = new JMenuItem("Wizard");
		wizard.addActionListener(e -> new DunGenPop("Wizard Generated",
				() -> new Wizard().toString()));
		npcs.add(npc);
		npcs.add(wizard);

		JMenuItem hoard = new JMenuItem("Hoard");
		hoard.addActionListener(e -> new DunGenPop("Hoard Generated",
				() -> Hoard.getHoard()));

		JMenu items = new JMenu("Items");
		JMenuItem anyItem = new JMenuItem("Any Magic Item");
		anyItem.addActionListener(e -> new DunGenPop("Item Generated",
				() -> Hoard.getMagicItem("")));
		JMenuItem sentientItem = new JMenuItem("Sentient Magic Item");
		sentientItem
				.addActionListener(e -> new DunGenPop(
						"Sentient Item Generated", () -> Hoard
								.getSentientMagicItem("")));
		JMenuItem rareItem = new JMenuItem("Rare Item");
		rareItem.addActionListener(e -> new DunGenPop("Rare Item Generated",
				() -> Hoard.getMagicItem("Rare")));
		JMenuItem veryRareItem = new JMenuItem("Very Rare Item");
		veryRareItem.addActionListener(e -> new DunGenPop(
				"Very Rare Item Generated", () -> Hoard
						.getMagicItem("Very Rare")));
		JMenuItem legendaryItem = new JMenuItem("Legendary Item");
		legendaryItem.addActionListener(e -> new DunGenPop(
				"Legendary Item Generated", () -> Hoard
						.getMagicItem("Legendary")));
		JMenuItem uncommonItem = new JMenuItem("Uncommon Item");
		uncommonItem
				.addActionListener(e -> new DunGenPop(
						"Uncommon Item Generated", () -> Hoard
								.getMagicItem("Uncommon")));
		JMenuItem commonItem = new JMenuItem("Common Item");
		commonItem.addActionListener(e -> new DunGenPop(
				"Common Item Generated", () -> Hoard.getMagicItem("Common")));

		items.add(anyItem);
		items.add(sentientItem);
		items.add(legendaryItem);
		items.add(veryRareItem);
		items.add(rareItem);
		items.add(uncommonItem);
		items.add(commonItem);
		JMenu poisons = new JMenu("Poisons");
		JMenuItem anyPoison = new JMenuItem("Any Poison");
		anyPoison.addActionListener(e -> new DunGenPop("Poison Generated",
				() -> Tables.getPoison("")));
		JMenuItem injuryPoison = new JMenuItem("Injury");
		injuryPoison.addActionListener(e -> new DunGenPop("Poison Generated",
				() -> Tables.getPoison("Injury")));
		JMenuItem contactPoison = new JMenuItem("Contact");
		contactPoison.addActionListener(e -> new DunGenPop("Poison Generated",
				() -> Tables.getPoison("Contact")));
		JMenuItem inhaledPoison = new JMenuItem("Inhaled");
		inhaledPoison.addActionListener(e -> new DunGenPop("Poison Generated",
				() -> Tables.getPoison("Inhaled")));
		JMenuItem ingestedPoison = new JMenuItem("Ingested");
		ingestedPoison.addActionListener(e -> new DunGenPop("Poison Generated",
				() -> Tables.getPoison("Ingested")));
		poisons.add(anyPoison);
		poisons.add(contactPoison);
		poisons.add(ingestedPoison);
		poisons.add(inhaledPoison);
		poisons.add(injuryPoison);

		JMenuItem insult = new JMenuItem("Insult");
		insult.addActionListener(e -> new DunGenPop("Insult Generated",
				Tables::getInsult));
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
		menuBar.add(generate);

		JMenu mishaps = new JMenu("Mishaps");
		JMenuItem scroll = new JMenuItem("Scroll");
		scroll.addActionListener(e -> new DunGenPop("Scroll mishap!",
				Tables::scrollMishap));
		JMenuItem potion = new JMenuItem("Potion");
		potion.addActionListener(e -> new DunGenPop("Potion mishap!",
				Tables::potionMishap));
		JMenuItem attack = new JMenuItem("Attack");
		attack.addActionListener(e -> new DunGenPop("Attack mishap!",
				Tables::meleeMishap));
		JMenuItem injury = new JMenuItem("Injury");
		injury.addActionListener(e -> new DunGenPop("Injury mishap!",
				Tables::getInjury));
		JMenu chases = new JMenu("Chase Complication");
		JMenuItem urban = new JMenuItem("Urban");
		urban.addActionListener(e -> new DunGenPop("Urban Chase Complication!",
				Tables::getUrbanMishap));
		JMenuItem wilderness = new JMenuItem("Wilderness");
		wilderness.addActionListener(e -> new DunGenPop(
				"Wilderness Chase Complication!", () -> Tables
						.getWildernessMishap()));
		chases.add(urban);
		chases.add(wilderness);

		JMenu madness = new JMenu("Madness");
		JMenuItem shortterm = new JMenuItem("Short Term");
		shortterm.addActionListener(e -> new DunGenPop("Short Term Madness",
				() -> Tables.getMadness(0)));
		madness.add(shortterm);

		JMenuItem longterm = new JMenuItem("Long Term");
		longterm.addActionListener(e -> new DunGenPop("Long Term Madness",
				() -> Tables.getMadness(1)));
		madness.add(longterm);
		JMenuItem indefinite = new JMenuItem("Indefinite");
		indefinite.addActionListener(e -> new DunGenPop("Indefinite Madness",
				() -> Tables.getMadness(2)));
		madness.add(indefinite);

		mishaps.add(attack);
		mishaps.add(chases);
		mishaps.add(injury);
		mishaps.add(potion);
		mishaps.add(madness);
		mishaps.add(scroll);

		menuBar.add(mishaps);
		setContentPane(contentPane);
		eastButton.setBounds(142, 29, 123, 35);
		eastButton.addActionListener(e -> {
			hideRoom();
			showX++;
			if (thisRoom.eastRoom == null) {
				Point p = new Point(showX, showY);
				if (rooms.containsKey(p)) {
					thisRoom.eastRoom = rooms.get(p);
				} else {
					thisRoom.eastRoom = new Room();
				}
				if (showY <= 0)
					thisRoom.eastRoom.south = false;
				else if (showY >= yLimit)
					thisRoom.eastRoom.north = false;
				if (showX >= xLimit)
					thisRoom.eastRoom.east = false;
				rooms.put(p, thisRoom.eastRoom);
			}
			thisRoom = thisRoom.eastRoom;
			showRoom();
		});
		westButton.setBounds(0, 29, 130, 35);
		westButton.addActionListener(e -> {
			hideRoom();
			showX--;
			if (thisRoom.westRoom == null) {
				Point p = new Point(showX, showY);
				if (rooms.containsKey(p)) {
					thisRoom.westRoom = rooms.get(p);
				} else {
					thisRoom.westRoom = new Room();
				}
				if (showY <= 0)
					thisRoom.westRoom.south = false;
				else if (showY >= yLimit)
					thisRoom.westRoom.north = false;
				if (showX <= -xLimit)
					thisRoom.westRoom.west = false;
			}
			thisRoom = thisRoom.westRoom;
			showRoom();
		});
		northButton.setBounds(0, 0, 265, 29);
		northButton.addActionListener(e -> {
			hideRoom();
			showY++;
			if (thisRoom.northRoom == null) {
				Point p = new Point(showX, showY);
				if (rooms.containsKey(p)) {
					thisRoom.northRoom = rooms.get(p);
				} else {
					thisRoom.northRoom = new Room();
				}
				if (showY >= yLimit)
					thisRoom.northRoom.north = false;
				if (showX >= xLimit)
					thisRoom.northRoom.east = false;
				else if (showX <= -xLimit)
					thisRoom.northRoom.west = false;
			}
			thisRoom = thisRoom.northRoom;
			showRoom();
		});
		southButton.setBounds(0, 64, 265, 29);
		southButton.addActionListener(e -> {
			hideRoom();
			showY--;
			if (thisRoom.southRoom == null) {
				Point p = new Point(showX, showY);
				if (rooms.containsKey(p)) {
					thisRoom.southRoom = rooms.get(p);
				} else {
					thisRoom.southRoom = new Room();
				}
				if (showY <= 0)
					thisRoom.southRoom.south = false;
				if (showX >= xLimit)
					thisRoom.southRoom.east = false;
				else if (showX <= -xLimit)
					thisRoom.southRoom.west = false;
			}
			thisRoom = thisRoom.southRoom;
			showRoom();
		});
		contentPane.setLayout(null);
		contentPane.add(westButton);
		contentPane.add(northButton);
		contentPane.add(eastButton);
		contentPane.add(southButton);

		scrollPane.setBounds(11, 110, 254, 420);
		contentPane.add(scrollPane);

		lblRoomDetails.setBounds(10, 91, 147, 16);
		contentPane.add(lblRoomDetails);

		roomDetails.setColumns(10);
		roomDetails.setLineWrap(true);
		roomDetails.setWrapStyleWord(true);
		roomDetails.setLocation(0, 0);
		roomDetails.setSize(200, 200);
		roomDetails.setText(thisRoom.details);
		scrollPane.setViewportView(roomDetails);

		JButton clearRoom = new JButton("Clear Room");
		clearRoom.addActionListener(e -> {
			roomDetails.setText(null);
			mapView.clearRoom(Controls.showX, Controls.showY);
			mapView.repaint();
		});
		clearRoom.setBounds(142, 91, 117, 16);
		contentPane.add(clearRoom);
		Controls.showRoom();
	}

	private static void fixDoors() {
		Room north = rooms.getOrDefault(new Point(showX, showY + 1), null), south = rooms
				.getOrDefault(new Point(showX, showY - 1), null), east = rooms
				.getOrDefault(new Point(showX + 1, showY), null), west = rooms
				.getOrDefault(new Point(showX - 1, showY), null);

		if (north != null) {
			thisRoom.north = north.south;
			thisRoom.northRoom = north;
		}
		if (south != null) {
			thisRoom.south = south.north;
			thisRoom.southRoom = south;
		}
		if (west != null) {
			thisRoom.west = west.east;
			thisRoom.westRoom = west;
		}
		if (east != null) {
			thisRoom.east = east.west;
			thisRoom.eastRoom = east;
		}

	}
}
