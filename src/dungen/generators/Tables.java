package dungen.generators;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import dungen.ui.InfoPanel;
import dungen.ui.Map;

@SuppressWarnings("unchecked")
public class Tables {

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
	private final static List<String> magicItemC = getTable("Magic Item Table C.txt");
	private final static List<String> magicItemD = getTable("Magic Item Table D.txt");
	private final static List<String> magicItemE = getTable("Magic Item Table E.txt");
	private final static List<String> magicItemF = getTable("Magic Item Table F.txt");
	private final static List<String> magicItemG = getTable("Magic Item Table G.txt");

	private final static List<String> trapSeverity = getTable("Trap Severity.txt");
	private final static List<String> trapTriggers = getTable("Trap Triggers.txt");
	private final static List<String> trapDamage = getTable("Trap Damage.txt");
	private final static List<String> trapEffect = getTable("Trap Effects.txt");
	private final static List<String> hazards = getTable("Hazards.txt");

	private final static List<String> alignments = getTable("Alignments.txt");
	private final static List<String> classes = getTable("Classes.txt");

	public static String getNpcClass() {
		return getResultFromTable(Dice.custom(20), classes);
	}

	public static String getTrapTrigger() {
		return getResultFromTable(Dice.custom(6), trapTriggers);
	}

	public static String getTrapSeverity() {
		String severity = getResultFromTable(Dice.custom(6), trapSeverity);
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

	public static String getMagicItem(int number, List<String> table) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < number; i++)
			result.append(getResultFromTable(Dice.custom(100), table) + " ");
		return result.toString();
	}

	public static String getAlignment() {
		return getResultFromTable(Dice.custom(20), alignments);
	}

	private static String getResultFromTable(int result, List<String> table) {
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

	private static String getHazard() {
		return getResultFromTable(Dice.d20(), hazards);
	}

	private static String getTrapEffect() {
		return getResultFromTable(Dice.custom(100), trapEffect);
	}

	private static int challenge = 40; // this will be used to slowly increase
										// difficulty.

	public static String getEvent() {
		String event = "";
		int result = Dice.custom(100);

		result = Dice.custom(challenge); // roll for type of encounter
		if (result > 95)
			event = "Deadly Encounter: " + getEncounter(deadlyTable);
		else if (result > 80)
			event = "Hard Encounter: " + getEncounter(hardTable);
		else if (result >= 30)
			event = "Medium Encounter: " + getEncounter(mediumTable);
		else if (result >= 20)
			event = "Easy Encounter: " + getEncounter(easyTable);
		else if (result >= 10)
			event = "Hazard!\n"+getHazard();
		else
			event = "Trap!\n" + getTrap();

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
	public static String[] monsterTypes = "Beast,Plant,Undead,Construct,Fiend,Aberration,Humanoid,Fey,Elemental,Dragon,Monstrosity,Ooze,Giant,Celestial"
			.split(",");
	private static HashMap<String, Integer> indexes = new HashMap<String, Integer>();

	static {
		for (int i = 0; i < monsterTypes.length; i++)
			indexes.put(monsterTypes[i], i);
	}
	private static String[][] monsters = getEncounterTable("MonsterList.txt");
	private static Supplier<String>[][] iTreasureByCR = new Supplier[4][];
	static {
		iTreasureByCR[0] = new Supplier[5];
		iTreasureByCR[0][0] = () -> Dice.custom(5, 6) + " CP";
		iTreasureByCR[0][1] = () -> Dice.custom(4, 6) + " SP";
		iTreasureByCR[0][2] = () -> Dice.custom(3, 6) + " EP";
		iTreasureByCR[0][3] = () -> Dice.custom(3, 6) + " GP";
		iTreasureByCR[0][4] = () -> Dice.custom(6) + " PP";

		iTreasureByCR[1] = new Supplier[5];
		iTreasureByCR[1][0] = () -> Dice.custom(4, 6) * 100 + " CP "
				+ Dice.custom(6) * 10 + " EP";
		iTreasureByCR[1][1] = () -> Dice.custom(6, 6) * 10 + " SP "
				+ Dice.custom(2, 6) * 10 + " GP";
		iTreasureByCR[1][2] = () -> Dice.custom(3, 6) * 10 + " EP "
				+ Dice.custom(2, 6) + " GP";
		iTreasureByCR[1][3] = () -> Dice.custom(4, 6) * 10 + " GP";
		iTreasureByCR[1][4] = () -> Dice.custom(2, 6) * 10 + " GP "
				+ Dice.custom(3, 6) + " PP";

		iTreasureByCR[2] = new Supplier[4];
		iTreasureByCR[2][0] = () -> Dice.custom(4, 6) * 100 + " SP "
				+ Dice.custom(6) * 100 + " GP";
		iTreasureByCR[2][1] = () -> Dice.custom(6) * 100 + " EP "
				+ Dice.custom(6) * 100 + " GP";
		iTreasureByCR[2][2] = () -> Dice.custom(2, 6) * 100 + " GP "
				+ Dice.custom(6) * 10 + " PP";
		iTreasureByCR[2][3] = () -> Dice.custom(2, 6) * 100 + " GP "
				+ Dice.custom(2, 6) * 10 + " PP";

		iTreasureByCR[3] = new Supplier[3];
		iTreasureByCR[3][0] = () -> Dice.custom(2, 6) * 1000 + " EP "
				+ Dice.custom(8, 6) * 100 + " GP";
		iTreasureByCR[3][1] = () -> Dice.custom(6) * 1000 + " GP "
				+ Dice.custom(6) * 100 + " PP";
		iTreasureByCR[3][2] = () -> Dice.custom(6) * 1000 + " GP "
				+ Dice.custom(2, 6) * 100 + " PP";

	}

	private static int whichTreasureCR(int result) {
		if (result <= 8)
			return 0;
		else if (result <= 14)
			return 1;
		else if (result <= 20)
			return 2;
		else
			return 3;
	}

	private static int whichTreasureSize(int CRGroup) {
		int result = Dice.custom(100);
		if (CRGroup <= 1) {
			if (result <= 30)
				return 0;
			if (result <= 60)
				return 1;
			if (result <= 70)
				return 2;
			if (result <= 95)
				return 3;
			return 4;

		} else if (CRGroup == 2) {
			if (result <= 20)
				return 0;
			if (result <= 35)
				return 1;
			if (result < 75)
				return 2;
			return 3;
		} else {
			if (result <= 15)
				return 0;
			if (result <= 55)
				return 1;
			return 2;
		}
	}

	private static String getEncounter(String[][] table) {
		List<String[]> mobs = new ArrayList<>();
		StringBuilder encounter = new StringBuilder("");
		int attempts = 0;
		while (mobs.isEmpty() && attempts < 10) {
			encounter.delete(0, encounter.length());
			int result = Dice.custom(table[InfoPanel.partyLevel].length - 2) + 1;
			encounter.append(table[InfoPanel.partyLevel][result].trim());
			encounter.append(" CR:" + CRs[result]);
			int treasureCR = whichTreasureCR(result);
			encounter
					.append("\nIndividual Treasure: "
							+ iTreasureByCR[whichTreasureCR(result)][whichTreasureSize(treasureCR)]
									.get());
			// if it's hard or deadly, add a treasure hoard
			if (table.equals(hardTable) || table.equals(deadlyTable)) {
				encounter.append("\nHoard: " + getHoard(treasureCR));
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

	private static Supplier<String>[][] iHoardByCR = new Supplier[4][];
	static {
		iHoardByCR[0] = new Supplier[17];
		// No magic items
		iHoardByCR[0][0] = () -> hoardC0();
		iHoardByCR[0][1] = () -> hoardC0() + Dice.custom(2, 6) + " 10gpgems ";
		iHoardByCR[0][2] = () -> hoardC0() + Dice.custom(2, 4)
				+ " 25 gp art objects";
		iHoardByCR[0][3] = () -> hoardC0() + Dice.custom(2, 6) + " 50 gp gems ";
		// A prizes
		iHoardByCR[0][4] = () -> hoardC0() + Dice.custom(2, 6) + " 10 gp gem "
				+ getMagicItem(Dice.custom(6), magicItemA);
		iHoardByCR[0][5] = () -> hoardC0() + Dice.custom(2, 4)
				+ " 25 gp art objects "
				+ getMagicItem(Dice.custom(6), magicItemA);
		iHoardByCR[0][6] = () -> hoardC0() + Dice.custom(2, 6) + " 50 gp gems "
				+ getMagicItem(Dice.custom(6), magicItemA);
		// B prizes
		iHoardByCR[0][7] = () -> hoardC0() + Dice.custom(2, 6) + " 10 gp gems "
				+ getMagicItem(Dice.custom(4), magicItemB);
		iHoardByCR[0][8] = () -> hoardC0() + Dice.custom(2, 4)
				+ " 25 gp art objects "
				+ getMagicItem(Dice.custom(4), magicItemB);
		iHoardByCR[0][9] = () -> hoardC0() + Dice.custom(2, 6) + " 50 gp gems "
				+ getMagicItem(Dice.custom(4), magicItemB);
		// C prizes
		iHoardByCR[0][10] = () -> hoardC0() + Dice.custom(2, 6)
				+ " 10 gp gems " + getMagicItem(Dice.custom(1, 4), magicItemC);
		iHoardByCR[0][11] = () -> hoardC0() + Dice.custom(2, 4)
				+ " 25 gp art objects "
				+ getMagicItem(Dice.custom(1, 4), magicItemC);
		iHoardByCR[0][12] = () -> hoardC0() + Dice.custom(2, 6)
				+ " 50 gp gems " + getMagicItem(Dice.custom(1, 4), magicItemC);
		// F Prizes
		iHoardByCR[0][13] = () -> hoardC0() + Dice.custom(2, 4)
				+ " 25 gp art objects\n"
				+ getMagicItem(Dice.custom(1, 4), magicItemF);
		iHoardByCR[0][14] = () -> hoardC0() + Dice.custom(2, 6)
				+ " 50 gp gems\n" + getMagicItem(Dice.custom(1, 4), magicItemF);
		// G Prizes
		iHoardByCR[0][15] = () -> hoardC0() + Dice.custom(2, 4)
				+ " 25 gp art objects " + getMagicItem(1, magicItemG);
		iHoardByCR[0][16] = () -> hoardC0() + Dice.custom(2, 6)
				+ " 50 gp gems " + getMagicItem(Dice.custom(1), magicItemG);
	}

	private static String hoardC0() {
		return Dice.custom(6, 6) * 100 + " CP " + Dice.custom(3, 6) * 100
				+ " SP " + Dice.custom(2, 6) + " GP ";
	}

	static int[] percentagesforHoard0 = { 6, 16, 26, 36, 44, 52, 60, 65, 70,
			75, 78, 80, 85, 92, 97, 99, 100 };

	private static String getHoard(int treasureCR) {
		int result = Dice.custom(100);
		if (treasureCR == 0)
			for (int i = 0; i < percentagesforHoard0.length; i++)
				if (result <= percentagesforHoard0[i])
					return iHoardByCR[treasureCR][10].get();
		return "Hoard for CR " + treasureCR + " not yet implemented.";
	}
}
