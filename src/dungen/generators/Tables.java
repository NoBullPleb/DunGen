package dungen.generators;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import dungen.ui.Map;

public class Tables {
	public static void main(String[] args) {
		System.out.println(getEvent());
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

	private static int challenge = 30; // this will be used to slowly increase
										// difficulty.

	public static String getEvent() {
		String event = "";
		int result = Dice.custom(100);
		if (result > 0) { // 40% of rooms will have an event
			result = Dice.custom(challenge); // roll for type of encounter
			if (result > 90)
				event = "Deadly Encounter: " + getEncounter(deadlyTable);
			else if (result > 70)
				event = "Hard Encounter: " + getEncounter(hardTable);
			else if (result >= 30)
				event = "Medium Encounter: " + getEncounter(mediumTable);
			else if (result >= 20)
				event = "Easy Encounter: " + getEncounter(easyTable);
			else
				event = "Trap!\n" + getTrap();
		}
		challenge += 5;
		return event;
	}

	private static String[][] easyTable = getEncounterTable("Easy.txt");
	private static String[][] mediumTable = getEncounterTable("Medium.txt");
	private static String[][] hardTable = getEncounterTable("Hard.txt");
	private static String[][] deadlyTable = getEncounterTable("Deadly.txt");

	private static String[][] getEncounterTable(String path) {
		List<String> table = getTable(path);
		String[][] result = new String[table.size()][];
		AtomicInteger ai = new AtomicInteger(0);
		table.forEach(e -> result[ai.getAndIncrement()] = e.split(","));
		return result;
	}

	private static String[] CRs = "LEVEL / CR,0,1/8,1/4,1/2,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20"
			.split(",");

	private static String[][] monsters = getEncounterTable("MonsterList.txt");

	private static String getEncounter(String[][] table) {
		int partylevel = 1;
		StringBuilder encounter = new StringBuilder("");
		int result = Dice.custom(table[partylevel].length - 2) + 1;
		encounter.append(table[partylevel][result].trim());
		encounter.append(" CR:" + CRs[result]);
		encounter.append("\nPossible Monsters:");
		Arrays.stream(monsters)
				.parallel()
				.filter(e -> e[0].equals(CRs[result]))
				.forEach(
						e -> {
							encounter.append("\n" + e[1] + " Size: " + e[3]
									+ " XP: " + e[2] + " Book: " + e[7]);
						});
		return encounter.toString();
	}
}
