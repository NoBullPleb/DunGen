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
import dungen.pojos.NPC;

public class Controls extends JFrame {
	public static String version = "1.5.1";
	private static final long serialVersionUID = 7985611292217902489L;
	transient final static JButton southButton = new JButton("Go South"),
			eastButton = new JButton("Go East"), westButton = new JButton(
					"Go West"), northButton = new JButton("Go North");
	// used to track where rooms are. If one is already at that location, we can
	// load it.
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
		roomDetails.setText("");
	}

	private static void showRoom() {
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
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Trap Generated");
			jd.setText(Tables.getTrap());
			jd.setVisible(true);
		});
		JMenuItem treasure = new JMenuItem("Treasure");
		treasure.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Treasure Generated");
			jd.setText(Treasure.getTreasure());
			jd.setVisible(true);
		});
		JMenuItem trick = new JMenuItem("Trick");
		trick.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Trick Generated");
			jd.setText(Tables.getTrick());
			jd.setVisible(true);
		});
		JMenu encounter = new JMenu("Encounter");
		JMenuItem deadly = new JMenuItem("Deadly");
		deadly.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Deadly Encounter Generated");
			jd.setText(Tables.getEncounter(Tables.deadlyTable));
			jd.setVisible(true);
		});
		JMenuItem hard = new JMenuItem("Hard");
		hard.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Hard Encounter Generated");
			jd.setText(Tables.getEncounter(Tables.hardTable));
			jd.setVisible(true);
		});
		JMenuItem medium = new JMenuItem("Medium");
		medium.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Medium Encounter Generated");
			jd.setText(Tables.getEncounter(Tables.mediumTable));
			jd.setVisible(true);
		});
		JMenuItem easy = new JMenuItem("Easy");
		easy.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Easy Encounter Generated");
			jd.setText(Tables.getEncounter(Tables.easyTable));
			jd.setVisible(true);
		});
		encounter.add(deadly);
		encounter.add(hard);
		encounter.add(medium);
		encounter.add(easy);

		JMenuItem hazard = new JMenuItem("Hazard");
		hazard.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Hazard Generated");
			jd.setText(Tables.getHazard());
			jd.setVisible(true);
		});
		JMenuItem npc = new JMenuItem("NPC");
		npc.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("NPC Generated");
			jd.setText(new NPC().toString());
			jd.setVisible(true);
		});

		JMenuItem hoard = new JMenuItem("Hoard");
		hoard.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Hoard Generated");
			jd.setText(Hoard.getHoard());
			jd.setVisible(true);
		});

		JMenu items = new JMenu("Items");
		JMenuItem anyItem = new JMenuItem("Any Magic Item");
		anyItem.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Item Generated");
			jd.setText(Hoard.getMagicItem(""));
			jd.setVisible(true);
		});
		JMenuItem sentientItem = new JMenuItem("Sentient Magic Item");
		sentientItem.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Sentient Item Generated");
			jd.setText(Hoard.getSentientMagicItem(""));
			jd.setVisible(true);
		});
		JMenuItem rareItem = new JMenuItem("Rare Item");
		rareItem.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Rare Item Generated");
			jd.setText(Hoard.getMagicItem("Rare"));
			jd.setVisible(true);
		});
		JMenuItem veryRareItem = new JMenuItem("Very Rare Item");
		veryRareItem.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Very Rare Item Generated");
			jd.setText(Hoard.getMagicItem("Very Rare"));
			jd.setVisible(true);
		});
		JMenuItem legendaryItem = new JMenuItem("Legendary Item");
		legendaryItem.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Legendary Item Generated");
			jd.setText(Hoard.getMagicItem("Legendary"));
			jd.setVisible(true);
		});
		JMenuItem uncommonItem = new JMenuItem("Uncommon Item");
		uncommonItem.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Uncommon Item Generated");
			jd.setText(Hoard.getMagicItem("Uncommon"));
			jd.setVisible(true);
		});
		JMenuItem commonItem = new JMenuItem("Common Item");
		commonItem.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Common Item Generated");
			jd.setText(Hoard.getMagicItem("Common"));
			jd.setVisible(true);
		});

		items.add(anyItem);
		items.add(sentientItem);
		items.add(legendaryItem);
		items.add(veryRareItem);
		items.add(rareItem);
		items.add(uncommonItem);
		items.add(commonItem);

		generate.add(encounter);
		generate.add(hazard);
		generate.add(hoard);
		generate.add(items);
		generate.add(npc);
		generate.add(trap);
		generate.add(treasure);
		generate.add(trick);
		menuBar.add(generate);

		JMenu mishaps = new JMenu("Mishaps");
		JMenuItem scroll = new JMenuItem("Scroll");
		scroll.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Scroll mishap!");
			jd.setText(Tables.scrollMishap());
			jd.setVisible(true);
		});
		JMenuItem potion = new JMenuItem("Potion");
		potion.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Scroll mishap!");
			jd.setText(Tables.potionMishap());
			jd.setVisible(true);
		});
		JMenuItem attack = new JMenuItem("Attack");
		attack.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Attack mishap!");
			jd.setText(Tables.meleeMishap());
			jd.setVisible(true);
		});
		JMenuItem injury = new JMenuItem("Injury");
		injury.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Injury mishap!");
			jd.setText(Tables.getInjury());
			jd.setVisible(true);
		});
		JMenu chases = new JMenu("Chase Complication");
		JMenuItem urban = new JMenuItem("Urban");
		urban.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Urban Chase Complication!");
			jd.setText(Tables.getUrbanMishap());
			jd.setVisible(true);
		});
		JMenuItem wilderness = new JMenuItem("Wilderness");
		wilderness.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Wilderness Chase Complication!");
			jd.setText(Tables.getWildernessMishap());
			jd.setVisible(true);
		});
		chases.add(urban);
		chases.add(wilderness);

		JMenu madness = new JMenu("Madness");
		JMenuItem shortterm = new JMenuItem("Short Term");
		shortterm.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Short Term Madness");
			jd.setText(Tables.getMadness(0));
			jd.setVisible(true);
		});
		madness.add(shortterm);

		JMenuItem longterm = new JMenuItem("Long Term");
		longterm.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Long Term Madness");
			jd.setText(Tables.getMadness(1));
			jd.setVisible(true);
		});
		madness.add(longterm);
		JMenuItem indefinite = new JMenuItem("Indefinite");
		indefinite.addActionListener(e -> {
			DunGenPop jd = new DunGenPop();
			jd.setTitle("Indefinite Madness");
			jd.setText(Tables.getMadness(2));
			jd.setVisible(true);
		});
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
					mapView.addHall(showX, showY, "west");
				} else {
					thisRoom.eastRoom = new Room();
				}
				thisRoom.eastRoom.westRoom = thisRoom;
				if (showY <= 0)
					thisRoom.eastRoom.south = false;
				else if (showY >= yLimit)
					thisRoom.eastRoom.north = false;
				if (showX >= xLimit)
					thisRoom.eastRoom.east = false;
				thisRoom.eastRoom.addDoor("west");
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
					mapView.addHall(showX, showY, "east");
				} else {
					thisRoom.westRoom = new Room();
				}
				thisRoom.westRoom.eastRoom = thisRoom;
				thisRoom.westRoom.addDoor("east");
				if (showY <= 0)
					thisRoom.westRoom.south = false;
				else if (showY >= yLimit)
					thisRoom.westRoom.north = false;
				if (showX <= -xLimit)
					thisRoom.westRoom.west = false;
				rooms.put(p, thisRoom.westRoom);
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
					mapView.addHall(showX, showY, "south");
				} else {
					thisRoom.northRoom = new Room();
				}
				thisRoom.northRoom.southRoom = thisRoom;
				thisRoom.northRoom.addDoor("south");
				if (showY >= yLimit)
					thisRoom.northRoom.north = false;
				if (showX >= xLimit)
					thisRoom.northRoom.east = false;
				else if (showX <= -xLimit)
					thisRoom.northRoom.west = false;
				rooms.put(p, thisRoom.northRoom);
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
					mapView.addHall(showX, showY, "north");
				} else {
					thisRoom.southRoom = new Room();
				}
				thisRoom.southRoom.northRoom = thisRoom;
				rooms.put(p, thisRoom.southRoom);
				thisRoom.southRoom.north = true;
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

		JScrollPane scrollPane = new JScrollPane();
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
}
