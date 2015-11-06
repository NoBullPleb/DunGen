package dungen.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Controls extends JFrame {
	static JButton southButton, eastButton, westButton, northButton;
	private static ArrayList<ArrayList<Room>> map = new ArrayList<>();
	private static int x = 0, y = 0, showX = 0, showY = 0;
	private final static Map mapView = new Map(false);
	static {
		// adding the start room
		map.add(new ArrayList<Room>());
		map.get(x).add(new Room(false));

	}
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
		map.get(x).get(y).setVisible(false);
	}

	private void showRoom() {
		map.get(x).get(y)
				.setLocation(this.getX() + this.getWidth(), this.getY());
		map.get(x).get(y).setVisible(true);

		this.setTitle("X: " + showX + " Y: " + showY);
		boolean hasNorth = map.get(x).get(y).north, hasWest = map.get(x).get(y).west, hasEast = map
				.get(x).get(y).east, hasSouth = map.get(x).get(y).south;
		if (!map.get(x).get(y).drawn) {
			mapView.addRoom(showX, showY);
			if (hasNorth)
				mapView.addHall(showX, showY, "north");
			if (hasSouth)
				mapView.addHall(showX, showY, "south");
			if (hasEast)
				mapView.addHall(showX, showY, "east");
			if (hasWest)
				mapView.addHall(showX, showY, "west");
			map.get(x).get(y).drawn = true;
		}
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
			x--;
			showX--;
			if (x < 0) {
				x = 0;
				map.add(0, new ArrayList<Room>());
				for (int i = 0; i <= y; i++)
					map.get(x).add(0, new Room(false));
			} else if (y >= map.get(x).size()) {
				for (int i = map.get(x).size(); i <= y; i++)
					map.get(x).add(i, new Room(false));
			}
			map.get(x).get(y).addDoor("east");
			showRoom();
		});
		contentPane.add(westButton, BorderLayout.WEST);
		JPanel centerMap = new JPanel(null);
		JLabel star = new JLabel("*");
		star.setLocation((int) (centerMap.getAlignmentX() + (showX * 5)),
				(int) (centerMap.getAlignmentY() + (showY * 5)));
		star.setVisible(true);
		centerMap.add(star);
		contentPane.add(centerMap, BorderLayout.CENTER);

		northButton = new JButton("Go North");
		northButton.addActionListener(e -> {
			hideRoom();
			y++;
			showY++;
			if (y >= map.get(x).size()) {
				map.get(x).add(y, new Room(false));
			}
			map.get(x).get(y).addDoor("south");
			showRoom();
		});
		contentPane.add(northButton, BorderLayout.NORTH);

		eastButton = new JButton("Go East");
		eastButton.addActionListener(e -> {
			hideRoom();
			x++;
			showX++;
			if (x >= map.size()) {
				map.add(x, new ArrayList<Room>());
				for (int i = 0; i <= y; i++)
					map.get(x).add(new Room(false));
			} else if (y >= map.get(x).size()) {
				for (int i = map.get(x).size(); i <= y; i++)
					map.get(x).add(new Room(false));
			}
			map.get(x).get(y).addDoor("west");
			showRoom();
		});
		contentPane.add(eastButton, BorderLayout.EAST);
		southButton = new JButton("Go South");
		southButton.addActionListener(e -> {
			hideRoom();
			y--;
			showY--;
			if (y < 0) {
				y = 0;
				map.get(x).add(0, new Room(false));
			}
			map.get(x).get(y).addDoor("north");
			showRoom();
		});
		contentPane.add(southButton, BorderLayout.SOUTH);
	}

}
