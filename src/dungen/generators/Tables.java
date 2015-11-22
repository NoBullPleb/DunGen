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
	private final static List<String> magicItemH = getTable("Magic Item Table H.txt");
	private final static List<String> magicItemI = getTable("Magic Item Table I.txt");

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

	public static String getMagicItem(int number, List<String> table) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < number; i++)
			result.append(getMagicItem(table) + " ");
		return result.toString();
	}

	public static String getMagicItem(List<String> table) {
		return getResultFromTable(Dice.roll(100), table);
	}

	public static String getAlignment() {
		return getResultFromTable(Dice.roll(20), alignments);
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
	private static Supplier<String>[][] iTreasureByCR = new Supplier[4][];
	static {
		iTreasureByCR[0] = new Supplier[5];
		iTreasureByCR[0][0] = () -> Dice.roll(5, 6) + " CP";
		iTreasureByCR[0][1] = () -> Dice.roll(4, 6) + " SP";
		iTreasureByCR[0][2] = () -> Dice.roll(3, 6) + " EP";
		iTreasureByCR[0][3] = () -> Dice.roll(3, 6) + " GP";
		iTreasureByCR[0][4] = () -> Dice.roll(6) + " PP";

		iTreasureByCR[1] = new Supplier[5];
		iTreasureByCR[1][0] = () -> Dice.roll(4, 6) * 100 + " CP "
				+ Dice.roll(6) * 10 + " EP";
		iTreasureByCR[1][1] = () -> Dice.roll(6, 6) * 10 + " SP "
				+ Dice.roll(2, 6) * 10 + " GP";
		iTreasureByCR[1][2] = () -> Dice.roll(3, 6) * 10 + " EP "
				+ Dice.roll(2, 6) + " GP";
		iTreasureByCR[1][3] = () -> Dice.roll(4, 6) * 10 + " GP";
		iTreasureByCR[1][4] = () -> Dice.roll(2, 6) * 10 + " GP "
				+ Dice.roll(3, 6) + " PP";

		iTreasureByCR[2] = new Supplier[4];
		iTreasureByCR[2][0] = () -> Dice.roll(4, 6) * 100 + " SP "
				+ Dice.roll(6) * 100 + " GP";
		iTreasureByCR[2][1] = () -> Dice.roll(6) * 100 + " EP " + Dice.roll(6)
				* 100 + " GP";
		iTreasureByCR[2][2] = () -> Dice.roll(2, 6) * 100 + " GP "
				+ Dice.roll(6) * 10 + " PP";
		iTreasureByCR[2][3] = () -> Dice.roll(2, 6) * 100 + " GP "
				+ Dice.roll(2, 6) * 10 + " PP";

		iTreasureByCR[3] = new Supplier[3];
		iTreasureByCR[3][0] = () -> Dice.roll(2, 6) * 1000 + " EP "
				+ Dice.roll(8, 6) * 100 + " GP";
		iTreasureByCR[3][1] = () -> Dice.roll(6) * 1000 + " GP " + Dice.roll(6)
				* 100 + " PP";
		iTreasureByCR[3][2] = () -> Dice.roll(6) * 1000 + " GP "
				+ Dice.roll(2, 6) * 100 + " PP";

	}

	private static int whichTreasureCR(Double result) {
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

	private static int whichTreasureSize(int CRGroup) {
		int result = Dice.roll(100);
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

	public static String getTreasure(int treasureCR) {
		return "Individual Treasure: "
				+ iTreasureByCR[treasureCR][whichTreasureSize(treasureCR)]
						.get();
	}

	// assumes medium encounter.
	public static String getTreasure() {
		Double result = Dice.roll(mediumTable[InfoPanel.partyLevel].length - 2) + 1.0;
		int treasureCR = whichTreasureCR(result);
		return "Individual Treasure: "
				+ iTreasureByCR[treasureCR][whichTreasureSize(treasureCR)]
						.get();
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
			int treasureCR = whichTreasureCR(Double.parseDouble(CRs[result]));
			encounter.append("\n" + getTreasure(treasureCR));
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
		iHoardByCR[0][1] = () -> hoardC0() + Dice.roll("2d6 10gp gems ");
		iHoardByCR[0][2] = () -> hoardC0() + Dice.roll("2d4 25 gp art objects");
		iHoardByCR[0][3] = () -> hoardC0() + Dice.roll("2d6 50 gp gems ");
		// A prizes
		iHoardByCR[0][4] = () -> hoardC0() + Dice.roll("2d6 10 gp gem ")
				+ getMagicItem(Dice.roll(6), magicItemA);
		iHoardByCR[0][5] = () -> hoardC0()
				+ Dice.roll("2d4  25 gp art objects ")
				+ getMagicItem(Dice.roll(6), magicItemA);
		iHoardByCR[0][6] = () -> hoardC0() + Dice.roll("2d6 50 gp gems ")
				+ getMagicItem(Dice.roll(6), magicItemA);
		// B prizes
		iHoardByCR[0][7] = () -> hoardC0() + Dice.roll("2d6 10 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemB);
		iHoardByCR[0][8] = () -> hoardC0()
				+ Dice.roll("2d4 25 gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemB);
		iHoardByCR[0][9] = () -> hoardC0() + Dice.roll("2d6 50 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemB);
		// C prizes
		iHoardByCR[0][10] = () -> hoardC0() + Dice.roll("2d6 10 gp gems ")
				+ getMagicItem(Dice.roll(1, 4), magicItemC);
		iHoardByCR[0][11] = () -> hoardC0()
				+ Dice.roll("2d4 25 gp art objects ")
				+ getMagicItem(Dice.roll(1, 4), magicItemC);
		iHoardByCR[0][12] = () -> hoardC0() + Dice.roll("2d6 50 gp gems")
				+ getMagicItem(Dice.roll(1, 4), magicItemC);
		// F Prizes
		iHoardByCR[0][13] = () -> hoardC0()
				+ Dice.roll("2d4 25 gp art objects")
				+ getMagicItem(Dice.roll(1, 4), magicItemF);
		iHoardByCR[0][14] = () -> hoardC0() + Dice.roll("2d6 50 gp gems")
				+ getMagicItem(Dice.roll(1, 4), magicItemF);
		// G Prizes
		iHoardByCR[0][15] = () -> hoardC0()
				+ Dice.roll("2d4  25 gp art objects ")
				+ getMagicItem(magicItemG);
		iHoardByCR[0][16] = () -> hoardC0() + Dice.roll("2d6 50 gp gems ")
				+ getMagicItem(magicItemG);
		// HOARD GROUP 2
		iHoardByCR[1] = new Supplier[29];
		iHoardByCR[1][0] = () -> hoardC1();
		iHoardByCR[1][1] = () -> hoardC1() + " "
				+ Dice.roll("2d4 25gp art objects");
		iHoardByCR[1][2] = () -> hoardC1() + " " + Dice.roll("3d6 50 gp gems");
		iHoardByCR[1][3] = () -> hoardC1() + " " + Dice.roll("3d6 100 gp gems");
		iHoardByCR[1][4] = () -> hoardC1() + " "
				+ Dice.roll("2d4 250 gp art objects ");
		iHoardByCR[1][5] = () -> hoardC1() + " "
				+ Dice.roll("2d4 25gp art objects ")
				+ getMagicItem(Dice.roll(6), magicItemA);
		iHoardByCR[1][6] = () -> hoardC1() + " " + Dice.roll("3d6 50 gp gems ")
				+ getMagicItem(Dice.roll(6), magicItemA);
		iHoardByCR[1][7] = () -> hoardC1() + " "
				+ Dice.roll("3d6 100 gp gems ")
				+ getMagicItem(Dice.roll(6), magicItemA);
		iHoardByCR[1][8] = () -> hoardC1() + " "
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(Dice.roll(6), magicItemA);
		iHoardByCR[1][9] = () -> hoardC1() + " "
				+ Dice.roll("2d4 25gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemB);
		iHoardByCR[1][10] = () -> hoardC1() + " "
				+ Dice.roll("3d6 50 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemB);
		iHoardByCR[1][11] = () -> hoardC1() + " "
				+ Dice.roll("3d6 100 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemB);
		iHoardByCR[1][12] = () -> hoardC1() + " "
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemB);
		iHoardByCR[1][13] = () -> hoardC1() + " "
				+ Dice.roll("2d4 25gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemC);
		iHoardByCR[1][14] = () -> hoardC1() + " "
				+ Dice.roll("3d6 50 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemC);
		iHoardByCR[1][15] = () -> hoardC1() + " "
				+ Dice.roll("3d6 100 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemC);
		iHoardByCR[1][16] = () -> hoardC1() + " "
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemC);
		iHoardByCR[1][17] = () -> hoardC1() + " "
				+ Dice.roll("2d4 25gp art objects ") + getMagicItem(magicItemD);
		iHoardByCR[1][18] = () -> hoardC1() + " "
				+ Dice.roll("3d6 50 gp gems ") + getMagicItem(magicItemD);
		iHoardByCR[1][19] = () -> hoardC1() + " "
				+ Dice.roll("3d6 100 gp gems ") + getMagicItem(magicItemD);
		iHoardByCR[1][20] = () -> hoardC1() + " "
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(magicItemD);
		iHoardByCR[1][21] = () -> hoardC1() + " "
				+ Dice.roll("2d4 25gp art objects ")
				+ getMagicItem(Dice.roll(1, 4), magicItemF);
		iHoardByCR[1][22] = () -> hoardC1() + " "
				+ Dice.roll("3d6 50 gp gems ")
				+ getMagicItem(Dice.roll(1, 4), magicItemF);
		iHoardByCR[1][23] = () -> hoardC1() + " "
				+ Dice.roll("3d6 100 gp gems ")
				+ getMagicItem(Dice.roll(1, 4), magicItemF);
		iHoardByCR[1][24] = () -> hoardC1() + " "
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(Dice.roll(1, 4), magicItemF);
		iHoardByCR[1][25] = () -> hoardC1() + " "
				+ Dice.roll("2d4 25gp art objects ")
				+ getMagicItem(Dice.roll(1, 4), magicItemG);
		iHoardByCR[1][26] = () -> hoardC1() + " "
				+ Dice.roll("3d6 50 gp gems ")
				+ getMagicItem(Dice.roll(1, 4), magicItemG);
		iHoardByCR[1][27] = () -> hoardC1() + " "
				+ Dice.roll("3d6 100 gp gems ") + getMagicItem(magicItemH);
		iHoardByCR[1][28] = () -> hoardC1() + " "
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(magicItemH);

		// 2
		iHoardByCR[2] = new Supplier[33];
		iHoardByCR[2][0] = () -> hoardC2();

		iHoardByCR[2][1] = () -> hoardC2()
				+ Dice.roll("2d4 250 gp art objects");
		iHoardByCR[2][2] = () -> hoardC2()
				+ Dice.roll("2d4 750 gp art objects");
		iHoardByCR[2][3] = () -> hoardC2() + Dice.roll("3d6 500 gp gems");
		iHoardByCR[2][4] = () -> hoardC2() + Dice.roll("3d6 1,000 gp gems");

		iHoardByCR[2][5] = () -> hoardC2()
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemA) + " "
				+ getMagicItem(Dice.roll(6), magicItemB);
		iHoardByCR[2][6] = () -> hoardC2()
				+ Dice.roll("2d4 750 gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemA) + " "
				+ getMagicItem(Dice.roll(6), magicItemB);
		iHoardByCR[2][7] = () -> hoardC2() + Dice.roll("3d6 500 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemA) + " "
				+ getMagicItem(Dice.roll(6), magicItemB);
		iHoardByCR[2][8] = () -> hoardC2() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemA) + " "
				+ getMagicItem(Dice.roll(6), magicItemB);

		iHoardByCR[2][9] = () -> hoardC2()
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(Dice.roll(6), magicItemC);
		iHoardByCR[2][10] = () -> hoardC2()
				+ Dice.roll("2d4 750 gp art objects ")
				+ getMagicItem(Dice.roll(6), magicItemC);
		iHoardByCR[2][11] = () -> hoardC2() + Dice.roll("3d6 500 gp gems ")
				+ getMagicItem(Dice.roll(6), magicItemC);
		iHoardByCR[2][12] = () -> hoardC2() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(Dice.roll(6), magicItemC);

		iHoardByCR[2][13] = () -> hoardC2()
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemD);
		iHoardByCR[2][14] = () -> hoardC2()
				+ Dice.roll("2d4 750 gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemD);
		iHoardByCR[2][15] = () -> hoardC2() + Dice.roll("3d6 500 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemD);
		iHoardByCR[2][16] = () -> hoardC2() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemD);

		iHoardByCR[2][17] = () -> hoardC2()
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(magicItemE);
		iHoardByCR[2][18] = () -> hoardC2()
				+ Dice.roll("2d4 750 gp art objects ")
				+ getMagicItem(magicItemE);
		iHoardByCR[2][19] = () -> hoardC2() + Dice.roll("3d6 500 gp gems ")
				+ getMagicItem(magicItemE);
		iHoardByCR[2][20] = () -> hoardC2() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(magicItemE);

		iHoardByCR[2][21] = () -> hoardC2()
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(magicItemF) + " "
				+ getMagicItem(Dice.roll(4), magicItemG);
		iHoardByCR[2][22] = () -> hoardC2()
				+ Dice.roll("2d4 750 gp art objects ")
				+ getMagicItem(magicItemF) + " "
				+ getMagicItem(Dice.roll(4), magicItemG);
		iHoardByCR[2][23] = () -> hoardC2() + Dice.roll("3d6 500 gp gems ")
				+ getMagicItem(magicItemF) + " "
				+ getMagicItem(Dice.roll(4), magicItemG);
		iHoardByCR[2][24] = () -> hoardC2() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(magicItemF) + " "
				+ getMagicItem(Dice.roll(4), magicItemG);

		iHoardByCR[2][25] = () -> hoardC2()
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemH);
		iHoardByCR[2][26] = () -> hoardC2()
				+ Dice.roll("2d4 750 gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemH);
		iHoardByCR[2][27] = () -> hoardC2() + Dice.roll("3d6 500 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemH);
		iHoardByCR[2][28] = () -> hoardC2() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemH);

		iHoardByCR[2][29] = () -> hoardC2()
				+ Dice.roll("2d4 250 gp art objects ")
				+ getMagicItem(magicItemI);
		iHoardByCR[2][30] = () -> hoardC2()
				+ Dice.roll("2d4 750 gp art objects ")
				+ getMagicItem(magicItemI);
		iHoardByCR[2][31] = () -> hoardC2() + Dice.roll("3d6 500 gp gems ")
				+ getMagicItem(magicItemI);
		iHoardByCR[2][32] = () -> hoardC2() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(magicItemI);
	}

	private static String hoardC0() {
		return Dice.roll(6, 6) * 100 + "CP " + Dice.roll(3, 6) * 100 + " SP "
				+ Dice.roll(2, 6) + "GP ";
	}

	private static String hoardC1() {
		return Dice.roll(2, 6) * 100 + "CP " + Dice.roll(2, 6) * 1000 + "SP "
				+ Dice.roll(6, 6) * 100 + "GP " + Dice.roll(3, 6) * 10 + "PP";
	}

	private static String hoardC2() {
		return Dice.roll(4, 6) * 1000 + "GP " + Dice.roll(5, 6) * 100 + "PP ";
	}

	static int[][] percentagesforHoard = new int[3][];
	static {
		int[] percents0 = { 6, 16, 26, 36, 44, 52, 60, 65, 70, 75, 78, 80, 85,
				92, 97, 99, 100 };
		int[] percents1 = { 04, 10, 16, 22, 28, 32, 36, 40, 44, 49, 54, 59, 63,
				66, 69, 72, 74, 76, 78, 79, 80, 84, 88, 91, 94, 96, 98, 99, 100 };
		int[] percents2 = { 3, 6, 9, 12, 15, 19, 23, 26, 29, 35, 40, 45, 50,
				54, 58, 62, 66, 68, 70, 72, 74, 76, 78, 80, 82, 85, 88, 90, 92,
				94, 96, 98, 100 };
		percentagesforHoard[0] = percents0;
		percentagesforHoard[1] = percents1;
		percentagesforHoard[2] = percents2;
	}

	private static String getHoard(int treasureCR) {
		int result = Dice.roll(100);
		if (treasureCR < percentagesforHoard.length
				&& percentagesforHoard[treasureCR] != null)
			for (int i = 0; i < percentagesforHoard[treasureCR].length; i++)
				if (result <= percentagesforHoard[treasureCR][i])
					return iHoardByCR[treasureCR][i].get();
		return "Hoard for CR GROUP " + (treasureCR + 1)
				+ " not yet implemented.";
	}

	// assumes the CR = ECL
	public static String getHoard() {
		return getHoard(whichTreasureCR(InfoPanel.partyLevel + 0.0));
	}
}
