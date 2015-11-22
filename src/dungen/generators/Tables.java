package dungen.generators;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import dungen.pojos.Hoard;
import dungen.pojos.Treasure;
import dungen.ui.InfoPanel;
import dungen.ui.Map;

public class Tables {

	public static List<String> getTable(String path) {
		try {
			return Files.readAllLines(new File(Map.class.getClassLoader()
					.getResource("tables/" + path).getPath()
					.replaceAll("%20", " ")).toPath());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private final static List<String> trapSeverity = getTable("Trap Severity.txt");
	private final static List<String> trapTriggers = getTable("Trap Triggers.txt");
	private final static List<String> trapDamage = getTable("Trap Damage.txt");
	private final static List<String> trapEffect = getTable("Trap Effects.txt");
	private final static List<String> hazards = getTable("Hazards.txt");
	private final static List<String> trickObjects = getTable("Trick Objects.txt");
	private final static List<String> trickEffects = getTable("Trick Effects.txt");
	private final static List<String> alignments = getTable("Alignments.txt");
	private final static List<String> classes = getTable("Classes.txt");

	public static String getNpcClass() {
		return getResultFromTable(Dice.roll(20), classes);
	}

	public static String getTrapTrigger() {
		return getResultFromTable(Dice.roll(6), trapTriggers);
	}

	public static String getTrapSeverity() {
		String severity = getResultFromTable(Dice.roll(6), trapSeverity);
		return severity + " " + getDamage(severity);
	}

	private static String getDamage(String severity) {
		int index = 0;
		if (severity.contains("Dangerous"))
			index = 1;
		else if (severity.contains("Deadly"))
			index = 2;
		return getResultFromTable(InfoPanel.partyLevel, trapDamage).split(",")[index];
	}

	public static String getAlignment() {
		return getResultFromTable(Dice.roll(20), alignments);
	}

	public static String getResultFromTable(int result, List<String> table) {
		final StringBuilder items = new StringBuilder();
		try {
			table.parallelStream()
					.map(e -> e.split(","))
					.filter(e -> e.length > 1)
					.filter(e -> {
						Integer max = new Integer(e[0].trim());
						return result <= max;
					})
					.findFirst()
					.ifPresent(
							e -> items.append(String.join(",", e).substring(
									e[0].length() + 1)));
			return items.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getTrap() {
		return "Severity: " + getTrapSeverity() + "\nTrigger: "
				+ getTrapTrigger() + "\nDescription: " + getTrapEffect();
	}

	public static String getHazard() {
		return getResultFromTable(Dice.d20(), hazards);
	}

	private static String getTrapEffect() {
		return getResultFromTable(Dice.roll(100), trapEffect);
	}

	private static int challenge = 60; // this will be used to slowly increase
										// difficulty.

	public static String getEvent() {
		String event = "";
		int result = Dice.roll(100);
		result = Dice.roll(challenge); // roll for type of encounter
		if (result > 95)
			event = "Deadly Encounter: " + getEncounter(deadlyTable);
		else if (result > 80)
			event = "Hard Encounter: " + getEncounter(hardTable);
		else if (result >= 60)
			event = "Medium Encounter: " + getEncounter(mediumTable);
		else if (result >= 50)
			event = "Easy Encounter: " + getEncounter(easyTable);
		else if (result >= 38)
			event = "Hazard!\n" + getHazard();
		else if (result >= 26)
			event = "Trap!\n" + getTrap();
		else if (result >= 20)
			event = "Trick!\n" + getTrick();

		challenge += 5;
		return event;
	}

	public static String getTrick() {
		return getResultFromTable(Dice.d20(), trickObjects) + "\n"
				+ getResultFromTable(Dice.roll(100), trickEffects);
	}

	public static String[][] easyTable = getEncounterTable("Easy.txt");
	public static String[][] mediumTable = getEncounterTable("Medium.txt");
	public static String[][] hardTable = getEncounterTable("Hard.txt");
	public static String[][] deadlyTable = getEncounterTable("Deadly.txt");

	private static String[][] getEncounterTable(String path) {
		List<String> table = getTable(path);
		String[][] result = new String[table.size()][];
		AtomicInteger ai = new AtomicInteger(0);
		table.forEach(e -> result[ai.getAndIncrement()] = e.split(","));
		return result;
	}

	private static String[] CRs = "LEVEL / CR,0,1/8,1/4,1/2,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20"
			.split(",");
	public static String[] monsterTypes = "Beast,Plant,Undead,Construct,Fiend,Aberration,Humanoid,Fey,Elemental,Dragon,Monstrosity,Ooze,Giant,Celestial"
			.split(",");
	private static HashMap<String, Integer> indexes = new HashMap<String, Integer>();

	static {
		for (int i = 0; i < monsterTypes.length; i++)
			indexes.put(monsterTypes[i], i);
	}
	private static String[][] monsters = getEncounterTable("MonsterList.txt");

	public static Double crToDouble(String CR) {
		if (!CR.contains("/")) {
			return Double.parseDouble(CR);
		} else {
			String[] params = CR.split("/");
			return Double.parseDouble(params[0])
					/ Double.parseDouble(params[1]);
		}
	}

	public static String getEncounter(String[][] table) {
		List<String[]> mobs = new ArrayList<>();
		StringBuilder encounter = new StringBuilder("");
		int attempts = 0;
		while (mobs.isEmpty() && attempts < 10) {
			encounter.delete(0, encounter.length());
			int result = Dice.roll(table[InfoPanel.partyLevel].length - 2) + 1;
			encounter.append(table[InfoPanel.partyLevel][result].trim());
			encounter.append(" CR:" + CRs[result]);
			int treasureCR = whichTreasureCR(crToDouble(CRs[result]));
			encounter.append("\n" + Treasure.getTreasure(treasureCR));
			// if it's hard or deadly, add a treasure hoard
			if (table.equals(hardTable) || table.equals(deadlyTable)) {
				encounter.append("\nHoard: " + Hoard.getHoard(treasureCR));
			}
			encounter.append("\nPossible Monsters:");
			mobs = Arrays.stream(monsters).parallel()
					// grab only the appropriate CR
					.filter(e -> e[0].equals(CRs[result]))
					// and only those matching the type
					.filter(e -> InfoPanel.getTruth(indexes.get(e[4])))
					.collect(Collectors.toList());
			mobs.forEach(e -> {
				encounter.append("\n" + e[1] + " XP: " + e[2] + " Book: "
						+ e[6]);
			});
			attempts++;
		}
		return encounter.toString();
	}

	private final static List<String> scrollMishaps = getTable("Scroll Mishaps.csv");
	private final static List<String> potionMishaps = getTable("Potion Mishaps.txt");

	public static String scrollMishap() {
		return getResultFromTable(Dice.roll(6), scrollMishaps);
	}

	public static String potionMishap() {
		return getResultFromTable(Dice.roll(100), potionMishaps);
	}

	public static int whichTreasureCR(Double result) {
		// rewards are grouped weird.
		if (result <= 4)
			return 0;
		else if (result <= 10)
			return 1;
		else if (result <= 16)
			return 2;
		else
			return 3;
	}

}
