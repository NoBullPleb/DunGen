package dungen.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import dungen.generators.Tables;
import dungen.pojos.NPC;
import javax.swing.JScrollPane;

public class Room extends JFrame {
	private static final long serialVersionUID = 2020213751688226679L;
	public static int floor = 1;
	private static int numRooms = 0;
	{
		numRooms++;
	}
	public int room = numRooms;
	private static int neverTellMeTheOdds = 70;
	public boolean drawn = false;
	private JPanel contentPane;
	public boolean hasNPCs = Math.random() * 100 > 98; // 2% ODDS OF NPCS
	private ArrayList<NPC> party = new ArrayList<NPC>();
	public String encounter = "";
	{ // If it has NPCs, generate the party. 1-3 adventurers.
		if (hasNPCs) {
			for (int i = 0; i < (Math.random() * 3); i++) {
				party.add(new NPC());
				encounter += "\n" + party.get(i).alignment + " "
						+ party.get(i).npcClass;
			}
		} else {
			encounter = Tables.getEncounter();
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

	public Room(String... directions) {
		setLocation(this.getX(), this.getY() + this.getHeight());
		neverTellMeTheOdds -= 3;
		for (String direction : directions)
			addDoor(direction);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 100, 183, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		final StringBuilder sb = new StringBuilder();
		sb.append("Size of room: ").append(
				(int) Math.max(30 * Math.random(), 5));
		sb.append("\n" + encounter);
		setTitle("Room : " + numRooms);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		JTextArea positionLbl = new JTextArea();
		scrollPane.setViewportView(positionLbl);

		positionLbl.setLocation(0, 0);
		positionLbl.setSize(200, 200);
		positionLbl.setText(sb.toString());

	}
}
