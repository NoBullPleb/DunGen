package dungen.pojos;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import dungen.ui.Controls;
import dungen.ui.Map;
import dungen.ui.Room;

//used for serialization
public class Dungeon implements Serializable {
	private static final long serialVersionUID = 1586842009404988176L;
	public static HashMap<Point, Room> rooms = null;
	public static Room thisRoom = null;
	public static int showX = 0, showY = 0;
	public static Map mapView = null;

	public Dungeon() {
		rooms = Controls.rooms;
		showX = Controls.showX;
		showY = Controls.showY;
		thisRoom = Controls.thisRoom;
		mapView = Controls.mapView;
	}

	// used to avoid memory gain
	public static void dump() {
		rooms = null;
		showX = 0;
		showY = 0;
		thisRoom = null;
		mapView = null;
	}

	public static Dungeon load() {
		Dungeon returnable = null;
		try {
			File f = new File("test.dgn");
			FileInputStream fileIn = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			returnable = (Dungeon) in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
		System.out.println(returnable.toString());
		return returnable;
	}

	public static void save(ActionEvent e) {
		System.out.println("SAVING");
		try {
			File f = new File("test.dgn");
			System.out.println("WRITING: " + f.getAbsolutePath());
			f.delete();
			f.createNewFile();
			FileOutputStream fileOut = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fileOut);
			oos.writeObject(new Dungeon());
			oos.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

}
