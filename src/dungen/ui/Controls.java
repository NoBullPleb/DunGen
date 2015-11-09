package dungen.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Controls extends JFrame {
	static JButton southButton, eastButton, westButton, northButton;
	// used to track where rooms are. If one is already at that location, we can
	// load it.
	HashMap<Point, Room> rooms = new HashMap<>();
	private static Room thisRoom = new Room();
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
				mapView.setLocation(controls.getX() + controls.getWidth(),
						controls.getY());
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
		thisRoom.setLocation(this.getX(), this.getY() + this.getHeight());
		thisRoom.setVisible(true);
		this.setTitle("X: " + showX + " Y: " + showY);
		if (!thisRoom.drawn) {
			mapView.addRoom(showX, showY);
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

	/**
	 * Create the frame.
	 */
	public Controls() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("X: " + showX + " Y: " + showY);
		setResizable(false);
		setBounds(50, 50, 183, 110);
		contentPane = new JPanel();
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
					mapView.addHall(showX, showY, "east");
				} else {
					thisRoom.westRoom = new Room();
				}
				thisRoom.westRoom.eastRoom = thisRoom;
				thisRoom.westRoom.addDoor("east");
				rooms.put(p, thisRoom.westRoom);
			}
			thisRoom = thisRoom.westRoom;

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
					mapView.addHall(showX, showY, "south");
				} else {
					thisRoom.northRoom = new Room();
				}
				thisRoom.northRoom.southRoom = thisRoom;
				thisRoom.northRoom.addDoor("south");
				rooms.put(p, thisRoom.northRoom);
			}
			thisRoom = thisRoom.northRoom;
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
					mapView.addHall(showX, showY, "west");
				} else {
					thisRoom.eastRoom = new Room();
				}
				thisRoom.eastRoom.westRoom = thisRoom;
				rooms.put(p, thisRoom.eastRoom);
				thisRoom.eastRoom.addDoor("west");
			}
			thisRoom = thisRoom.eastRoom;
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
					mapView.addHall(showX, showY, "north");
				} else {
					thisRoom.southRoom = new Room("north");
				}
				thisRoom.southRoom.northRoom = thisRoom;
				rooms.put(p, thisRoom.southRoom);
				thisRoom.southRoom.addDoor("north");
			}
			thisRoom = thisRoom.southRoom;
			showRoom();
		});
		contentPane.add(southButton, BorderLayout.SOUTH);
	}

}
