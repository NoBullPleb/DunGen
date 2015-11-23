package dungen.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import dungen.generators.Tables;
import dungen.pojos.Dungeon;
import dungen.pojos.Hoard;
import dungen.pojos.Treasure;

public class Controls extends JFrame {
	public static String version = "1.0.0";
	private static final long serialVersionUID = 7985611292217902489L;
	transient final static JButton southButton = new JButton("Go South"),
			eastButton = new JButton("Go East"), westButton = new JButton(
					"Go West"), northButton = new JButton("Go North");
	// used to track where rooms are. If one is already at that location, we can
	// load it.
	public static HashMap<Point, Room> rooms = new HashMap<>();
	public static Room thisRoom = new Room();
	public static int showX = 0, showY = 0;
	{
		thisRoom.north = true;
		thisRoom.south = false;
		thisRoom.west = false;
		thisRoom.east = false;
		thisRoom.encounter = "This is the room where it all began... ";
		rooms.put(new Point(showX, showY), thisRoom);
	}
	public static Map mapView = new Map();
	public static InfoPanel ip = new InfoPanel();
	private transient final static JPanel contentPane = new JPanel();
	private transient JMenuBar menuBar;
	private transient JMenu file;
	private transient JMenuItem Save;
	private transient JMenuItem load;
	private static Controls controls = new Controls();

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				controls.setVisible(true);
				mapView.setLocation(controls.getX() + controls.getWidth(),
						controls.getY());
				mapView.setTitle("Dungeon Map");
				mapView.setVisible(true);
				ip.setVisible(true);
				Controls.showRoom();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	private static void hideRoom() {
		thisRoom.encounter = thisRoom.roomDetails.getText();
		thisRoom.setVisible(false);
	}

	private static void showRoom() {
		thisRoom.setLocation(controls.getX(),
				controls.getY() + controls.getHeight());
		thisRoom.setVisible(true);
		controls.setTitle("DunGen v" + version);
		if (!thisRoom.drawn) {
			mapView.addRoom(showX, showY, thisRoom.encounter, thisRoom.hasNPCs);
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
		setBounds(50, 50, 183, 137);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		file = new JMenu("File");
		menuBar.add(file);

		Save = new JMenuItem("Save");
		Save.addActionListener(new Dungeon()::save);
		file.add(Save);

		load = new JMenuItem("Load");
		load.addActionListener(Controls::load);
		file.add(load);

		JMenu generate = new JMenu("Generate");
		JMenuItem trap = new JMenuItem("Trap");
		trap.addActionListener(e -> {
			GeneratedDialog jd = new GeneratedDialog();
			jd.setTitle("Trap Generated");
			jd.setText(Tables.getTrap());
			jd.setVisible(true);
		});
		JMenuItem treasure = new JMenuItem("Treasure");
		treasure.addActionListener(e -> {
			GeneratedDialog jd = new GeneratedDialog();
			jd.setTitle("Treasure Generated");
			jd.setText(Treasure.getTreasure());
			jd.setVisible(true);
		});
		JMenuItem trick = new JMenuItem("Trick");
		trick.addActionListener(e -> {
			GeneratedDialog jd = new GeneratedDialog();
			jd.setTitle("Trick Generated");
			jd.setText(Tables.getTrick());
			jd.setVisible(true);
		});
		JMenu encounter = new JMenu("Encounter");
		JMenuItem deadly = new JMenuItem("Deadly");
		deadly.addActionListener(e -> {
			GeneratedDialog jd = new GeneratedDialog();
			jd.setTitle("Deadly Encounter Generated");
			jd.setText(Tables.getEncounter(Tables.deadlyTable));
			jd.setVisible(true);
		});
		JMenuItem hard = new JMenuItem("Hard");
		hard.addActionListener(e -> {
			GeneratedDialog jd = new GeneratedDialog();
			jd.setTitle("Hard Encounter Generated");
			jd.setText(Tables.getEncounter(Tables.hardTable));
			jd.setVisible(true);
		});
		JMenuItem medium = new JMenuItem("Medium");
		medium.addActionListener(e -> {
			GeneratedDialog jd = new GeneratedDialog();
			jd.setTitle("Medium Encounter Generated");
			jd.setText(Tables.getEncounter(Tables.mediumTable));
			jd.setVisible(true);
		});
		JMenuItem easy = new JMenuItem("Easy");
		easy.addActionListener(e -> {
			GeneratedDialog jd = new GeneratedDialog();
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
			GeneratedDialog jd = new GeneratedDialog();
			jd.setTitle("Hazard Generated");
			jd.setText(Tables.getHazard());
			jd.setVisible(true);
		});

		JMenuItem hoard = new JMenuItem("Hoard");
		hoard.addActionListener(e -> {
			GeneratedDialog jd = new GeneratedDialog();
			jd.setTitle("Hoard Generated");
			jd.setText(Hoard.getHoard());
			jd.setVisible(true);
		});
		generate.add(encounter);
		generate.add(hazard);
		generate.add(hoard);
		generate.add(trap);
		generate.add(treasure);
		generate.add(trick);
		menuBar.add(generate);

		JMenu mishaps = new JMenu("Mishaps");
		JMenuItem scroll = new JMenuItem("Scroll");
		scroll.addActionListener(e -> {
			GeneratedDialog jd = new GeneratedDialog();
			jd.setTitle("Scroll mishap!");
			jd.setText(Tables.scrollMishap());
			jd.setVisible(true);
		});
		JMenuItem potion = new JMenuItem("Potion");
		potion.addActionListener(e -> {
			GeneratedDialog jd = new GeneratedDialog();
			jd.setTitle("Scroll mishap!");
			jd.setText(Tables.potionMishap());
			jd.setVisible(true);
		});
		JMenuItem melee = new JMenuItem("Melee");
		melee.addActionListener(e -> {
			GeneratedDialog jd = new GeneratedDialog();
			jd.setTitle("Melee mishap!");
			jd.setText(Tables.meleeMishap());
			jd.setVisible(true);
		});
		mishaps.add(potion);
		mishaps.add(scroll);
		mishaps.add(melee);
		menuBar.add(mishaps);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
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
				else if (showY >= 10)
					thisRoom.eastRoom.north = false;
				if (showX >= 5)
					thisRoom.eastRoom.east = false;
				thisRoom.eastRoom.addDoor("west");
				rooms.put(p, thisRoom.eastRoom);
			}
			thisRoom = thisRoom.eastRoom;
			showRoom();
		});
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
				else if (showY >= 10)
					thisRoom.westRoom.north = false;
				if (showX <= -5)
					thisRoom.westRoom.west = false;
				rooms.put(p, thisRoom.westRoom);
			}
			thisRoom = thisRoom.westRoom;
			showRoom();
		});
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
				if (showY >= 10)
					thisRoom.northRoom.north = false;
				if (showX >= 5)
					thisRoom.northRoom.east = false;
				else if (showX <= -5)
					thisRoom.northRoom.west = false;
				rooms.put(p, thisRoom.northRoom);
			}
			thisRoom = thisRoom.northRoom;
			showRoom();
		});
		southButton.addActionListener(e -> {
			hideRoom();
			showY--;
			if (thisRoom.southRoom == null) {
				Point p = new Point(showX, showY);
				if (rooms.containsKey(p)) {
					thisRoom.southRoom = rooms.get(p);
					mapView.addHall(showX, showY, "north");
				} else {
					thisRoom.southRoom = new Room("north");
				}
				thisRoom.southRoom.northRoom = thisRoom;
				rooms.put(p, thisRoom.southRoom);
				thisRoom.southRoom.north = true;
				if (showY <= 0)
					thisRoom.southRoom.south = false;
				if (showX >= 5)
					thisRoom.southRoom.east = false;
				else if (showX <= -5)
					thisRoom.southRoom.west = false;
			}
			thisRoom = thisRoom.southRoom;
			showRoom();
		});
		contentPane.add(westButton, BorderLayout.WEST);
		contentPane.add(northButton, BorderLayout.NORTH);
		contentPane.add(eastButton, BorderLayout.EAST);
		contentPane.add(southButton, BorderLayout.SOUTH);
	}
}
