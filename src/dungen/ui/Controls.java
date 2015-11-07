package dungen.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Controls extends JFrame {
	static JButton southButton, eastButton, westButton, northButton;
	// used to track where rooms are. If one is already at that location, we can
	// load it.
	HashMap<Point, Room> rooms = new HashMap<>();
	private static Room thisRoom = new Room(false);
	private static int showX = 0, showY = 0;
	{
		rooms.put(new Point(showX, showY), thisRoom);
	}
	private final static Map mapView = new Map(false);
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Controls controls = new Controls();
				controls.setVisible(true);
				mapView.setLocation(controls.getX(),
						controls.getY() + controls.getHeight());
				mapView.setVisible(true);
				controls.showRoom();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	private void hideRoom() {
		thisRoom.setVisible(false);
	}

	private void showRoom() {
		thisRoom.setLocation(this.getX() + this.getWidth(), this.getY());
		thisRoom.setVisible(true);

		this.setTitle("X: " + showX + " Y: " + showY);
		boolean hasNorth = thisRoom.north, hasWest = thisRoom.west, hasEast = thisRoom.east, hasSouth = thisRoom.south;
		if (!thisRoom.drawn) {
			mapView.addRoom(showX, showY);
			if (hasNorth)
				mapView.addHall(showX, showY, "north");
			if (hasSouth)
				mapView.addHall(showX, showY, "south");
			if (hasEast)
				mapView.addHall(showX, showY, "east");
			if (hasWest)
				mapView.addHall(showX, showY, "west");
			thisRoom.drawn = true;
		}
		mapView.moveStar(showX, showY);
		northButton.setEnabled(hasNorth);
		westButton.setEnabled(hasWest);
		eastButton.setEnabled(hasEast);
		southButton.setEnabled(hasSouth);
	}

	/**
	 * Create the frame.
	 */
	public Controls() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("X: " + showX + " Y: " + showY);
		setResizable(false);
		setBounds(50, 50, 200, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		westButton = new JButton("Go West");
		westButton.addActionListener(e -> {
			hideRoom();
			showX--;
			if (thisRoom.westRoom == null) {
				Point p = new Point(showX, showY);
				if (rooms.containsKey(p)) {
					thisRoom.westRoom = rooms.get(p);
				} else {
					thisRoom.westRoom = new Room(false);
				}
				thisRoom.westRoom.eastRoom = thisRoom;
				rooms.put(p, thisRoom.westRoom);
			}
			thisRoom = thisRoom.westRoom;
			thisRoom.addDoor("east");
			showRoom();
		});
		contentPane.add(westButton, BorderLayout.WEST);
		northButton = new JButton("Go North");
		northButton.addActionListener(e -> {
			hideRoom();
			showY++;

			if (thisRoom.northRoom == null) {
				Point p = new Point(showX, showY);
				if (rooms.containsKey(p)) {
					thisRoom.northRoom = rooms.get(p);
				} else {
					thisRoom.northRoom = new Room(false);
				}
				thisRoom.northRoom.southRoom = thisRoom;
				rooms.put(p, thisRoom.northRoom);
			}

			thisRoom = thisRoom.northRoom;
			thisRoom.addDoor("south");
			showRoom();
		});
		contentPane.add(northButton, BorderLayout.NORTH);

		eastButton = new JButton("Go East");
		eastButton.addActionListener(e -> {
			hideRoom();
			showX++;
			if (thisRoom.eastRoom == null) {
				Point p = new Point(showX, showY);
				if (rooms.containsKey(p)) {
					thisRoom.eastRoom = rooms.get(p);
				} else {
					thisRoom.eastRoom = new Room(false);
				}
				thisRoom.eastRoom.westRoom = thisRoom;
				rooms.put(p, thisRoom.eastRoom);
			}

			thisRoom = thisRoom.eastRoom;
			thisRoom.addDoor("west");
			showRoom();
		});
		contentPane.add(eastButton, BorderLayout.EAST);
		southButton = new JButton("Go South");
		southButton.addActionListener(e -> {
			hideRoom();
			showY--;
			if (thisRoom.southRoom == null) {
				Point p = new Point(showX, showY);
				if (rooms.containsKey(p)) {
					thisRoom.southRoom = rooms.get(p);
				} else {
					thisRoom.southRoom = new Room(false);
				}
				thisRoom.southRoom.northRoom = thisRoom;
				rooms.put(p, thisRoom.southRoom);
			}

			thisRoom = thisRoom.southRoom;
			thisRoom.addDoor("north");
			showRoom();
		});
		contentPane.add(southButton, BorderLayout.SOUTH);
	}

}
