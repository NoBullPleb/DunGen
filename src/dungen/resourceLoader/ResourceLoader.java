package dungen.resourceLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

public class ResourceLoader {
	static final ClassLoader cl = ResourceLoader.class.getClassLoader();
	static String tableString = cl.getResource("dungen").getPath();
	static final Path table;

	static {
		tableString = tableString.substring(0, tableString.lastIndexOf("/"))
				.replaceAll("%20", " ").replaceAll("file:", "");
		table = new File(tableString).toPath();
		System.out.println("table string: " + tableString);
	}

	public static ImageIcon getImage(String path) {
		try {

			ImageIcon i = new ImageIcon(cl.getResource("images/" + path));
			System.out.println("Found image: "
					+ cl.getResource("images/" + path).getPath());
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<String> getTable(String path) {
		try {
			InputStream in = cl.getResourceAsStream("tables/" + path);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			return reader.lines().collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			// represents empty file if there was an error loading.
			return new ArrayList<String>();
		}
	}

}
