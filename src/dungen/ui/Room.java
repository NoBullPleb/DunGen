package dungen.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class Room extends JFrame {
	private static int numRooms = 0;
	public boolean drawn = false;
	private int neverTellMeTheOdds = 70 - (5 * numRooms);
	private static final long serialVersionUID = -2853163315903184535L;
	private JPanel contentPane;
	int numdoors = 0;
	public Boolean north = (Math.random() * 100) > 100 - neverTellMeTheOdds,
			south = (Math.random() * 100) > 100 - neverTellMeTheOdds,
			east = (Math.random() * 100) > 100 - neverTellMeTheOdds,
			west = (Math.random() * 100) > 100 - neverTellMeTheOdds;

	public Room() {
		new Room(true);
	}

	public void addDoor(String direction) {
		if (direction.equalsIgnoreCase("north")) {
			north = true;
		} else if (direction.equalsIgnoreCase("south")) { 
			south = true;
		} else if (direction.equalsIgnoreCase("east")) { 
			east = true;
		} else if (direction.equalsIgnoreCase("west")) { 
			west = true;
		}
	}

	public Room(boolean exit, String... directions) {
		numRooms++;
		for (String direction : directions) {
			addDoor(direction);
		}
		if (exit)
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		else
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setBounds(200, 100, 200, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		JTextPane positionLbl = new JTextPane();

		positionLbl.setLocation(0, 0);
		positionLbl.setSize(200, 200);
		StringBuilder sb = new StringBuilder();
		sb.append("Room Number: " + numRooms);
		sb.append("\nSize of room: ").append(
				(int) Math.max(50 * Math.random(), 5));

		positionLbl.setText(sb.toString());
		contentPane.add(positionLbl, BorderLayout.NORTH);

	}
}
