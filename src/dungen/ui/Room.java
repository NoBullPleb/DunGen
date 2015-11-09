package dungen.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import dungen.mobs.NPC;

@SuppressWarnings("serial")
public class Room extends JFrame {
	public static int floor = 1;
	private static int neverTellMeTheOdds = 70;
	public boolean drawn = false;
	private JPanel contentPane;
	private boolean hasNPCs = Math.random() * 10 > 9.5; // 5% ODDS OF NPCS
	private ArrayList<NPC> party = new ArrayList<NPC>();
	{ // If it has NPCs, generate the party. 1-3 adventurers.
		if (hasNPCs) {
			for (int i = 0; i < (Math.random() * 3); i++)
				party.add(new NPC());
		}
	}
	public Boolean north = (Math.random() * 100) > 100 - neverTellMeTheOdds,
			south = (Math.random() * 100) > 100 - neverTellMeTheOdds,
			east = (Math.random() * 100) > 100 - neverTellMeTheOdds,
			west = (Math.random() * 100) > 100 - neverTellMeTheOdds;
	public Room northRoom = null, southRoom = null, westRoom = null,
			eastRoom = null;

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
		neverTellMeTheOdds -= 3;
		for (String direction : directions)
			addDoor(direction);
		if (exit)
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		else
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 100, 183, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		JTextPane positionLbl = new JTextPane();

		positionLbl.setLocation(0, 0);
		positionLbl.setSize(200, 200);
		final StringBuilder sb = new StringBuilder();
		sb.append("\nSize of room: ").append(
				(int) Math.max(30 * Math.random(), 5));
		party.forEach(e -> sb.append("\n NPC: " + e.getAlignment() + " class: "
				+ e.getNpcClass() + " lvl: " + e.getLvl()));

		positionLbl.setText(sb.toString());
		contentPane.add(positionLbl, BorderLayout.NORTH);

	}
}
