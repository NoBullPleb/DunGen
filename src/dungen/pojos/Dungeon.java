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

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import dungen.ui.Controls;
import dungen.ui.Map;
import dungen.ui.Room;

//used for serialization
public class Dungeon implements Serializable {
	private static final long serialVersionUID = 1586842009404988176L;
	public HashMap<Point, Room> rooms = null;
	public Room thisRoom = null;
	public int showX = 0, showY = 0;
	public Map mapView = null;

	public Dungeon() {
		rooms = Controls.rooms;
		showX = Controls.showX;
		showY = Controls.showY;
		thisRoom = Controls.thisRoom;
		mapView = Controls.mapView;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("rooms=").append("" + rooms).append("showX=").append(showX)
				.append("\n").append("Mapview: " + mapView + "\n\n")
				.append("RoomLocations: " + mapView.roomsLocations);
		return sb.toString();
	}

	public static File getFile(Boolean isSaving) {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Dungeon Files", "dgn", "*.dgn");
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(filter);
			if (isSaving)
				fileChooser.showSaveDialog(null);
			else
				fileChooser.showOpenDialog(null);

			File file = fileChooser.getSelectedFile();
			if (!file.getName().endsWith("dgn"))
				file = new File(file.getAbsolutePath() + ".dgn");
			return file;
		} catch (Exception err) {
			err.printStackTrace();
			return null;
		}
	}

	public static Dungeon load() {
		Dungeon returnable = null;
		try {
			File f = getFile(false);
			FileInputStream fileIn = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			returnable = (Dungeon) in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
		return returnable;
	}

	public void save(ActionEvent e) {
		try {
			File f = getFile(true);
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
