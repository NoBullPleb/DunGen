package dungen.ui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;

import dungen.pojos.Dungeon;
import dungen.pojos.Room;

import javax.swing.JScrollPane;
import javax.swing.JLabel;

import java.awt.BorderLayout;

public class Controls extends JFrame {
	public static String version = "1.8.0";
	private static final long serialVersionUID = 7985611292217902489L;
	public transient final static JButton southButton = new JButton("Go South"),
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
	private static transient DunGenMenu menuBar = new DunGenMenu();

	public static void saveRoom() {
		String temp = roomDetails.getText();
		if (!thisRoom.details.equals(temp)) {
			thisRoom.details = temp;
			mapView.clearRoom(showX, showY);
			mapView.addEventOnRoom(showX, showY, temp);
		}
		rooms.put(new Point(showX, showY), thisRoom);
	}

	public static void hideRoom() {
		saveRoom();
		roomDetails.setText("");
	}

	public static void addToRoomDescription(String s) {
		roomDetails.setText(s + "\n" + roomDetails.getText());

	}

	public static void showRoom() {
		if (InfoPanel.isSymmmetric() && thisRoom != null && !thisRoom.drawn)
			makeSymmetric();
		fixDoors();
		roomDetails.setText(thisRoom.details);
		lblRoomDetails.setText("Room Details: " + thisRoom.roomNumber);
		if (!thisRoom.drawn) {
			mapView.addRoom(showX, showY, thisRoom);
			thisRoom.drawn = true;
		}
		mapView.moveStar(showX, showY);
		northButton.setEnabled(!thisRoom.north.isEmpty());
		westButton.setEnabled(!thisRoom.west.isEmpty());
		eastButton.setEnabled(!thisRoom.east.isEmpty());
		southButton.setEnabled(!thisRoom.south.isEmpty());
	}

	public static void load(ActionEvent e) {
		Dungeon d = Dungeon.load();
		hideRoom();
		InfoPanel.setPartyLevel(d.partyLevel);
		Controls.showX = d.showX;
		Controls.showY = d.showY;
		Controls.rooms = d.rooms;
		Controls.mapView.redraw(true);
		InfoPanel.setPartySize(d.partySize);
		InfoPanel.setSymmetric(d.symmetric);
		for (int i = 0; i < d.types.size(); i++) {
			InfoPanel.setTruth(i, d.types.get(i));
		}
		showRoom();
	}

	public Controls() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("DunGen v" + version);
		setBounds(0, 50, 271, 580);
		thisRoom.addDoor("north");
		thisRoom.south = "";
		thisRoom.details = "This is the room where it all began... ";
		rooms.put(new Point(showX, showY), thisRoom);

		setJMenuBar(menuBar);
		setContentPane(contentPane);
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
					thisRoom.eastRoom.south = "";
				else if (showY >= yLimit)
					thisRoom.eastRoom.north = "";
				if (showX >= xLimit)
					thisRoom.eastRoom.east = "";
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
				} else {
					thisRoom.westRoom = new Room();
				}
				if (showY <= 0)
					thisRoom.westRoom.south = "";
				else if (showY >= yLimit)
					thisRoom.westRoom.north = "";
				if (showX <= -xLimit)
					thisRoom.westRoom.west = "";
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
				} else {
					thisRoom.northRoom = new Room();
				}
				if (showY >= yLimit)
					thisRoom.northRoom.north = "";
				if (showX >= xLimit)
					thisRoom.northRoom.east = "";
				else if (showX <= -xLimit)
					thisRoom.northRoom.west = "";
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
				} else {
					thisRoom.southRoom = new Room();
				}
				if (showY <= 0)
					thisRoom.southRoom.south = "";
				if (showX >= xLimit)
					thisRoom.southRoom.east = "";
				else if (showX <= -xLimit)
					thisRoom.southRoom.west = "";
			}
			thisRoom = thisRoom.southRoom;
			showRoom();
		});
		contentPane.setLayout(new BorderLayout());
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setLayout(new BorderLayout(0, 0));
		ButtonPanel.add(westButton, BorderLayout.WEST);
		ButtonPanel.add(northButton, BorderLayout.NORTH);
		ButtonPanel.add(eastButton, BorderLayout.EAST);
		ButtonPanel.add(southButton, BorderLayout.SOUTH);
		contentPane.add(lblRoomDetails);

		roomDetails.setColumns(10);
		roomDetails.setLineWrap(true);
		roomDetails.setWrapStyleWord(true);
		roomDetails.setLocation(0, 0);
		roomDetails.setSize(10, 10);
		roomDetails.setText(thisRoom.details);
		scrollPane.setViewportView(roomDetails);

		JButton clearRoom = new JButton("Clear");
		clearRoom.addActionListener(e -> {
			roomDetails.setText("");
			mapView.clearRoom(Controls.showX, Controls.showY);
			mapView.repaint();
		});
		ButtonPanel.add(clearRoom, BorderLayout.CENTER);
		contentPane.add(ButtonPanel, BorderLayout.NORTH);
		JPanel roomPanel = new JPanel();
		roomPanel.setLayout(new BorderLayout());
		roomPanel.add(scrollPane, BorderLayout.CENTER);
		roomPanel.add(lblRoomDetails, BorderLayout.NORTH);
		contentPane.add(roomPanel, BorderLayout.CENTER);

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

	private static void makeSymmetric() {
		if (showX == 0) {
			thisRoom.west = thisRoom.east;
			return;
		}
		int tempX = -1 * showX;
		Point p = new Point(tempX, showY);
		if (rooms.containsKey(p)) {
			Room otherRoom = rooms.get(p);
			thisRoom.north = otherRoom.north;
			thisRoom.west = otherRoom.east;
			thisRoom.east = otherRoom.west;
			thisRoom.south = otherRoom.south;
		}
	}
}
