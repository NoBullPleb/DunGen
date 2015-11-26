package dungen.resourceLoader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.swing.ImageIcon;

public class ResourceLoader {
	static final ClassLoader cl = ResourceLoader.class.getClassLoader();
	static final String tableString = cl.getResource(".").getPath();
	static final Path table = new File(tableString).toPath();

	public static ImageIcon getImage(String path) {
		try {
			ImageIcon i = new ImageIcon(getResourcePath(path).toString());
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Path getResourcePath(String path) throws Exception {
		Optional<Path> paths = Files
				.find(table, 2, (a, b) -> a.toString().contains(path))
				.parallel().findFirst();
		if (!paths.isPresent()) {
			System.out.println("No matches found for " + path);
			throw new Exception("No matches found for resource: " + path);
		}
		return paths.get();
	}

	public static List<String> getTable(String path) {
		try {
			return Files.readAllLines(getResourcePath(path));

		} catch (Exception e) {
			System.out.println("FAILED- " + path + " : " + e.toString() + ", "
					+ e.getStackTrace()[0].getLineNumber());
			return null;
		}
	}
}
