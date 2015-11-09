package dungen.ui;

import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Map extends JFrame {
	
	private static final long serialVersionUID = 3925862084304805067L;
	static final ArrayList<Point> roomsLocations = new ArrayList<>();
	static final ArrayList<JLabel> rooms = new ArrayList<>();
	static final ArrayList<JLabel> halls = new ArrayList<>();
	private static final JPanel contentPane = new JPanel();
	private static final Font font = new Font(Font.MONOSPACED, 0, 9);

	public void addRoom(Integer x, Integer y) {
		Point p = new Point((contentPane.getWidth() / 2) + x * 20, this.getHeight()
				/ 2 - y * 20);
		if (!roomsLocations.contains(p)) {
			JLabel room = new JLabel("[]");
			room.setSize(10, 8);
			room.setLocation((int) p.getX(), (int) p.getY());
			room.setFont(font);
			room.setVisible(true);
			contentPane.add(room);
			rooms.add(room);
			roomsLocations.add(p);
		}
		contentPane.repaint();

	}

	public void moveStar(int x, int y) {
		Point p = new Point((this.getWidth() / 2) + (x * 20) + 3,
				(this.getHeight() / 2) - (y * 20) + 2);
		star.setLocation(p);
		star.setVisible(true);
		contentPane.add(star);
		star.repaint();
	}

	private static JLabel star = new JLabel("*");
	static {
		star.setSize(5, 5);
		star.setFont(font);
	}

	public void addHall(Integer x, Integer y, String direction) {
		Point p = new Point((this.getWidth() / 2) + x * 20, (this.getHeight()
				/ 2) - y * 20);
		int modX = 0, modY = 0;
		JLabel room = rooms.get(roomsLocations.indexOf(p));
		JLabel hall = new JLabel("+");
		hall.setFont(font);
		hall.setSize(10, 10);
		if (direction.equalsIgnoreCase("north")) {
			modX = room.getX() + 3;
			modY = room.getY() - room.getHeight();
		} else if (direction.equalsIgnoreCase("south")) {
			modX = room.getX() + 3;
			modY = room.getY() + room.getHeight() - 3;
		} else if (direction.equalsIgnoreCase("east")) {
			modX = room.getX() + room.getWidth() - 2;
			modY = room.getY() - 1;
		} else if (direction.equalsIgnoreCase("west")) {
			modX = room.getX() - 3;
			modY = room.getY() - 1;
		}
		hall.setLocation(modX, modY);

		hall.setVisible(true);
		contentPane.add(hall);
		halls.add(hall);
		contentPane.repaint();
	}

	public Map(boolean example) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		if (example)
			for (int x = -5; x < 5; x++)
				for (int y = -5; y < 5; y++)
					addRoom(x, y);
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}

}
