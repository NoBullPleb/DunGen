package dungen.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class Map extends JFrame {

	private static final long serialVersionUID = 3925862084304805067L;
	public ArrayList<Point> roomsLocations = new ArrayList<>();
	public ArrayList<JLabel> rooms = new ArrayList<>();
	public ArrayList<JLabel> halls = new ArrayList<>();
	public ArrayList<JLabel> encounters = new ArrayList<>();

	public JLabel star = new JLabel();
	private transient ImageIcon roomImage = getImage("Room.png");
	private transient ImageIcon trapImage = getImage("Trap.png");
	private transient ImageIcon hallImage = getImage("Hall.png");
	private transient ImageIcon hallImage2 = getImage("Hall2.png");
	private transient ImageIcon partyImage = getImage("Party.png");
	private transient ImageIcon encounterImage = getImage("Encounter.png");
	private transient ImageIcon otherPartyImage = getImage("otherParty.png");
	public transient JLayeredPane contentPane = new JLayeredPane();
	private transient static final Font font = new Font(Font.MONOSPACED, 0, 9);
	{
		this.setBackground(Color.BLACK);
		star.setSize(15, 15);
		star.setIcon(partyImage);
		star.setFont(font);
	}

	private ImageIcon getImage(String path) {
		try {
			return new ImageIcon(Map.class.getClassLoader().getResource(
					"images/" + path));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void redraw() {
		contentPane.removeAll();
		rooms.forEach(contentPane::add);
		halls.forEach(contentPane::add);
		encounters.forEach(contentPane::add);
		contentPane.add(star);
		contentPane.revalidate();
		contentPane.repaint();
	}

	public void addEncounterOnRoom(int x, int y, String encounter,
			boolean hasParty) {
		Point p = new Point((contentPane.getWidth() / 2) + x * 30,
				this.getHeight() / 2 - y * 30);
		JLabel encounterLbl = new JLabel();

		if (encounter.contains("Trap"))
			encounterLbl.setIcon(trapImage);
		else if (hasParty)
			encounterLbl.setIcon(otherPartyImage);
		else
			encounterLbl.setIcon(encounterImage);
		encounterLbl.setSize(15, 15);
		encounterLbl.setLocation((int) p.getX(), (int) p.getY());
		encounterLbl.setFont(font);
		encounterLbl.setVisible(true);
		contentPane.add(encounterLbl, contentPane.highestLayer());
		encounters.add(encounterLbl);
		contentPane.repaint();
	}

	public void addRoom(Integer x, Integer y, String hasEncounter,
			boolean hasParty) {
		Point p = new Point((contentPane.getWidth() / 2) + x * 30,
				this.getHeight() / 2 - y * 30);
		if (roomsLocations != null && !roomsLocations.contains(p)) {
			JLabel room = new JLabel();
			room.setIcon(roomImage);
			room.setSize(15, 15);
			room.setLocation((int) p.getX(), (int) p.getY());
			room.setFont(font);
			room.setVisible(true);
			contentPane.add(room);
			rooms.add(room);
			roomsLocations.add(p);
			if (!hasEncounter.isEmpty())
				addEncounterOnRoom(x, y, hasEncounter, hasParty);
		}
		contentPane.repaint();

	}

	public void moveStar(int x, int y) {
		Point p = new Point((this.getWidth() / 2) + (x * 30),
				(this.getHeight() / 2) - (y * 30));
		star.setLocation(p);
		star.setVisible(true);
		contentPane.add(star, contentPane.highestLayer());
		star.repaint();
	}

	public void addHall(Integer x, Integer y, String direction) {
		Point p = new Point((this.getWidth() / 2) + x * 30,
				(this.getHeight() / 2) - y * 30);
		int modX = 0, modY = 0;
		JLabel room = rooms.get(roomsLocations.indexOf(p));
		JLabel hall = new JLabel();
		if (direction.equalsIgnoreCase("north")
				|| direction.equalsIgnoreCase("south")) {
			hall.setIcon(hallImage);
		} else {
			hall.setIcon(hallImage2);
		}
		hall.setFont(font);
		hall.setSize(15, 15);
		if (direction.equalsIgnoreCase("north")) {
			modX = room.getX();
			modY = room.getY() - room.getHeight();
		} else if (direction.equalsIgnoreCase("south")) {
			modX = room.getX();
			modY = room.getY() + room.getHeight();
		} else if (direction.equalsIgnoreCase("east")) {
			modX = room.getX() + room.getWidth();
			modY = room.getY();
		} else if (direction.equalsIgnoreCase("west")) {
			modX = room.getX() - hall.getWidth();
			modY = room.getY();
		}
		hall.setLocation(modX, modY);

		hall.setVisible(true);
		contentPane.add(hall);
		halls.add(hall);
		contentPane.repaint();
	}

	public Map() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}

}
