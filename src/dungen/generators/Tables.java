package dungen.generators;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import dungen.ui.Map;

public class Tables {
	public static void main(String[] args) {
		System.out.println(getMagicItemA());
		System.out.println(getMagicItemB());

		System.err.println(getTrapSeverity());
		System.err.println(getTrapTrigger());
	}

	private static List<String> getTable(String path) {
		try {
			return Files.readAllLines(new File(Map.class.getClassLoader()
					.getResource("tables/" + path).getPath()
					.replaceAll("%20", " ")).toPath());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private final static List<String> magicItemA = getTable("Magic Item Table A.txt");
	private final static List<String> magicItemB = getTable("Magic Item Table B.txt");
	private final static List<String> trapSeverity = getTable("Trap Severity.txt");
	private final static List<String> trapTriggers = getTable("Trap Triggers.txt");
	private final static List<String> alignments = getTable("Alignments.txt");
	private final static List<String> classes = getTable("Classes.txt");

	public static String getNpcClass() {
		return getResultFromTable(Dice.custom(20), classes);
	}

	public static String getTrapTrigger() {
		return getResultFromTable(Dice.custom(6), trapTriggers);
	}

	public static String getTrapSeverity() {
		return getResultFromTable(Dice.custom(6), trapSeverity);
	}

	public static String getMagicItemB() {
		return getResultFromTable(Dice.custom(100), magicItemB);
	}

	public static String getMagicItemA() {
		return getResultFromTable(Dice.custom(100), magicItemA);
	}

	public static String getAlignment() {
		return getResultFromTable(Dice.custom(20), alignments);
	}

	private static String getResultFromTable(int result, List<String> table) {
		final StringBuilder items = new StringBuilder();
		try {
			table.parallelStream()
					.map(e -> e.split(","))
					.filter(e -> e.length > 2)
					.filter(e -> {
						Integer min = new Integer(e[0].trim()), max = new Integer(
								e[1].trim());
						return result >= min && result <= max;
					}).forEach(e -> items.append(e[e.length - 1]));
			return items.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getTrap() {
		return "Severity: " + getTrapSeverity() + "\nTrigger: "
				+ getTrapTrigger();
	}

	public static String getEncounter() {
		String encounter = "";
		int result = Dice.custom(100);
		if (result > 60) { // 30% of rooms will have an encounter
			result = Dice.custom(10); // roll for type of encounter
			if (result > 9)
				encounter = "Overpowering or Boss";
			else if (result > 7)
				encounter = "Challenging encounter";
			else if (result >= 4)
				encounter = "Moderate Encounter";
			else
				encounter = "Trap!\n" + getTrap();
		}
		return encounter;
	}
}
