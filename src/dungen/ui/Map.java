package dungen.ui;

import java.awt.Color;
import java.awt.Dimension;
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
	private transient static ImageIcon roomImage = getImage("Room.png");
	private transient static ImageIcon trapImage = getImage("Trap.png");
	private transient static ImageIcon hazardImage = getImage("Hazard.png");
	private transient static ImageIcon trickImage = getImage("Trick.png");
	private transient static ImageIcon hallImage = getImage("Hall.png");
	private transient static ImageIcon hallImage2 = getImage("Hall2.png");
	private transient static ImageIcon partyImage = getImage("Party.png");
	private transient static ImageIcon encounterImage = getImage("Encounter.png");
	private transient static ImageIcon otherPartyImage = getImage("otherParty.png");
	public transient JLayeredPane contentPane = new JLayeredPane();
	private static Dimension imagesize = new Dimension(20, 20);
	{
		this.setBackground(Color.BLACK);
		star.setSize(imagesize);
		star.setIcon(partyImage);
	}

	private static ImageIcon getImage(String path) {
		try {

			ImageIcon i = new ImageIcon(Map.class.getClassLoader().getResource(
					"images/" + path));

			return i;
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

	public void addEventOnRoom(int x, int y, String encounter, boolean hasParty) {
		Point p = getPosition(x, y);
		JLabel encounterLbl = new JLabel();

		if (encounter.contains("Trap"))
			encounterLbl.setIcon(trapImage);
		else if (encounter.contains("Hazard"))
			encounterLbl.setIcon(hazardImage);
		else if (encounter.contains("Trick"))
			encounterLbl.setIcon(trickImage);
		else if (hasParty)
			encounterLbl.setIcon(otherPartyImage);
		else
			encounterLbl.setIcon(encounterImage);
		encounterLbl.setSize(imagesize);
		encounterLbl.setLocation((int) p.getX(), (int) p.getY());
		encounterLbl.setVisible(true);
		contentPane.add(encounterLbl, contentPane.highestLayer());
		encounters.add(encounterLbl);
		contentPane.repaint();
	}

	public void addRoom(Integer x, Integer y, String hasEncounter,
			boolean hasParty) {
		Point p = getPosition(x, y);
		if (roomsLocations != null && !roomsLocations.contains(p)) {
			JLabel room = new JLabel();
			room.setIcon(roomImage);
			room.setSize(imagesize);
			room.setLocation((int) p.getX(), (int) p.getY());
			room.setVisible(true);
			contentPane.add(room);
			rooms.add(room);
			roomsLocations.add(p);
			if (!hasEncounter.isEmpty() && !hasEncounter.contains("began"))
				addEventOnRoom(x, y, hasEncounter, hasParty);
		}
		contentPane.repaint();

	}

	public void moveStar(int x, int y) {
		Point p = getPosition(x, y);
		star.setLocation(p);
		star.setVisible(true);
		contentPane.add(star, contentPane.highestLayer());
		star.repaint();
	}

	private Point getPosition(int x, int y) {
		return new Point((this.getWidth() / 2) + x
				* ((int) imagesize.getWidth() * 2), (this.getHeight() - 45) - y
				* ((int) imagesize.getHeight() * 2));
	}

	public void addHall(Integer x, Integer y, String direction) {
		Point p = getPosition(x, y);
		int modX = 0, modY = 0;
		JLabel room = rooms.get(roomsLocations.indexOf(p));
		JLabel hall = new JLabel();
		if (direction.equalsIgnoreCase("north")
				|| direction.equalsIgnoreCase("south")) {
			hall.setIcon(hallImage);
		} else {
			hall.setIcon(hallImage2);
		}
		hall.setSize(imagesize);
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
