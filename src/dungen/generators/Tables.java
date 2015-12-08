package dungen.generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import dungen.pojos.Hoard;
import dungen.pojos.Treasure;
import dungen.resourceLoader.ResourceLoader;
import dungen.ui.InfoPanel;

public class Tables {

	private final static List<String> trapSeverity = ResourceLoader
			.getTable("Trap Severity.txt");
	private final static List<String> trapTriggers = ResourceLoader
			.getTable("Trap Triggers.txt");
	private final static List<String> trapDamage = ResourceLoader
			.getTable("Trap Damage.txt");
	private final static List<String> trapEffect = ResourceLoader
			.getTable("Trap Effects.txt");
	private final static List<String> hazards = ResourceLoader
			.getTable("Hazards.txt");
	private final static List<String> trickObjects = ResourceLoader
			.getTable("Trick Objects.txt");
	private final static List<String> trickEffects = ResourceLoader
			.getTable("Trick Effects.txt");
	private final static List<String> alignments = ResourceLoader
			.getTable("Alignments.txt");
	private final static List<String> classes = ResourceLoader
			.getTable("Classes.txt");
	private final static List<String> urbanChase = ResourceLoader
			.getTable("Urban Chase.txt");
	private final static List<String> wildernessChase = ResourceLoader
			.getTable("Wilderness Chase.txt");

	private final static List<String> shortTerm = ResourceLoader
			.getTable("Short Mad.txt");
	private final static List<String> longTerm = ResourceLoader
			.getTable("Long Mad.txt");
	private final static List<String> indefinite = ResourceLoader
			.getTable("Indefinite Mad.txt");

	private final static List<String> spellList = ResourceLoader
			.getTable("Spells.txt");
	private final static List<String> injuries = ResourceLoader
			.getTable("Injuries.txt");

	public static String getInjury() {
		return getResultFromTable(Dice.d20(), injuries);
	}

	public static String getSpell(Integer Level, List<String> spellList) {
		// get spells of a particular level (0 for cantrip)
		return getResultFromTable(spellList.parallelStream()
				.filter(e -> e.startsWith(Level.toString()))
				.collect(Collectors.toList()));
	}

	public static String getSpell(Integer Level) {
		// get spells of a particular level (0 for cantrip)
		return getResultFromTable(spellList.parallelStream()
				.filter(e -> e.startsWith(Level.toString()))
				.collect(Collectors.toList()));
	}

	private final static List<String> poisons = ResourceLoader
			.getTable("Poisons.txt");

	public static String getPoison(String x) {
		String poison = getResultFromTable(poisons.parallelStream()
				.filter(e -> e.toLowerCase().contains(x.toLowerCase()))
				.collect(Collectors.toList()));
		return poison;
	}

	public static String getUrbanMishap() {
		return getResultFromTable(Dice.d20(), urbanChase);
	}

	public static String getNpcClass() {
		return getResultFromTable(Dice.d20(), classes);
	}

	public static String getTrapTrigger() {
		return getResultFromTable(Dice.d6(), trapTriggers);
	}

	public static String getTrapSeverity() {
		String severity = getResultFromTable(Dice.d6(), trapSeverity);
		return severity + " " + getDamage(severity);
	}

	private static String getDamage(String severity) {
		int index = 0;
		if (severity.contains("Dangerous"))
			index = 1;
		else if (severity.contains("Deadly"))
			index = 2;
		return getResultFromTable(InfoPanel.getPartyLevel(), trapDamage).split(
				",")[index];
	}

	private final static List<String> npcInteractive = ResourceLoader
			.getTable("NPC Interactive Trait.txt");
	private final static List<String> npcSpecialty = ResourceLoader
			.getTable("NPC Specialty.txt");
	private final static List<String> npcQuirks = ResourceLoader
			.getTable("NPC quirks.txt");

	public static String getInteractiveTrait() {
		return getResultFromTable(Dice.roll(12), npcInteractive);
	}

	public static String getQuirk() {
		return getResultFromTable(Dice.d20(), npcQuirks);
	}

	private final static List<String> insults = ResourceLoader
			.getTable("Trash Talk.txt");

	public static String getInsult() {
		return getResultFromTable(insults);
	}

	public static String getSpecialty() {
		return getResultFromTable(Dice.d20(), npcSpecialty);
	}

	public static String getAlignment() {
		return getResultFromTable(Dice.d20(), alignments);
	}

	private static String getResultFromTable(List<String> table) {
		return table.get((int) Math.floor(Math.random() * table.size()));
	}

	public static String getIdeals(String alignment) {
		Character a1 = alignment.charAt(0);
		Character a2 = alignment.charAt(1);

		StringBuilder ideals = new StringBuilder("Believes in ");
		if (a1.equals('L'))
			ideals.append(getResultFromTable(Dice.d6(), lawfulIdeal));
		else if (a1.equals('C'))
			ideals.append(getResultFromTable(Dice.d6(), chaoticIdeal));
		else
			ideals.append(getResultFromTable(Dice.d6(), neutral1Ideal));
		ideals.append(" and ");

		if (a2.equals('G'))
			ideals.append(getResultFromTable(Dice.d6(), goodIdeal));
		else if (a2.equals('E'))
			ideals.append(getResultFromTable(Dice.d6(), evilIdeal));
		else
			ideals.append(getResultFromTable(Dice.d6(), neutral2Ideal));

		return ideals.toString();

	}

	private final static List<String> chaoticIdeal = ResourceLoader
			.getTable("Chaos Ideal.txt");
	private final static List<String> evilIdeal = ResourceLoader
			.getTable("Evil Ideal.txt");
	private final static List<String> goodIdeal = ResourceLoader
			.getTable("Good Ideal.txt");
	private final static List<String> lawfulIdeal = ResourceLoader
			.getTable("Law Ideal.txt");
	private final static List<String> neutral1Ideal = ResourceLoader
			.getTable("Neutral 1 Ideal.txt");
	private final static List<String> neutral2Ideal = ResourceLoader
			.getTable("Neutral 2 Ideal.txt");

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
		return getResultFromTable(Dice.d100(), trapEffect);
	}

	private static int challenge = 60; // this will be used to slowly increase
										// difficulty.

	public static String getEvent() {
		String event = "";
		int result = Dice.roll(challenge); // roll for type of encounter
		if (result > 95) {
			if (InfoPanel.getSpwnEncounters())
				event = "Deadly Encounter: "
						+ getEncounter(deadlyTable[InfoPanel.getPartySize()]);
		} else if (result > 80) {
			if (InfoPanel.getSpwnEncounters())
				event = "Hard Encounter: "
						+ getEncounter(hardTable[InfoPanel.getPartySize()]);
		} else if (result >= 60) {
			if (InfoPanel.getSpwnEncounters())
				event = "Medium Encounter: "
						+ getEncounter(mediumTable[InfoPanel.getPartySize()]);
		} else if (result >= 50) {
			if (InfoPanel.getSpwnEncounters())
				event = "Easy Encounter: "
						+ getEncounter(easyTable[InfoPanel.getPartySize()]);
		} else if (result >= 38) {
			if (InfoPanel.getSpwnHazards())
				event = "Hazard!\n" + getHazard();
		} else if (result >= 26) {
			if (InfoPanel.getSpwnTraps())
				event = "Trap!\n" + getTrap();
		} else if (result >= 20) {
			event = "Trick!\n" + getTrick();
		}
		if (challenge < 100) // caps challenge to avoid spamming deadlies
			challenge += 5;
		return event;
	}

	public static String getTrick() {
		return getResultFromTable(Dice.d20(), trickObjects) + "\n"
				+ getResultFromTable(Dice.d100(), trickEffects);
	}

	public static String[][][] easyTable = new String[InfoPanel.maxPartySize + 1][][],
			mediumTable = new String[InfoPanel.maxPartySize + 1][][],
			hardTable = new String[InfoPanel.maxPartySize + 1][][],
			deadlyTable = new String[InfoPanel.maxPartySize + 1][][];
	static {
		for (Integer i = 1; i <= InfoPanel.maxPartySize; i++) {
			easyTable[i] = getEncounterTable(i + " Easy.txt");
			mediumTable[i] = getEncounterTable(i + " Medium.txt");
			hardTable[i] = getEncounterTable(i + " Hard.txt");
			deadlyTable[i] = getEncounterTable(i + " Deadly.txt");
		}
	}

	private static String[][] getEncounterTable(String path) {
		List<String> table = ResourceLoader.getTable(path);
		if (table != null) {
			String[][] result = new String[table.size()][];
			AtomicInteger ai = new AtomicInteger(0);
			for (String e : table)
				result[ai.getAndIncrement()] = e.split(",");
			return result;
		}
		return null;
	}

	private static String[] CRs = ",0,1/8,1/4,1/2,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20"
			.split(",");
	public static String[] monsterTypes = "Beast,Plant,Undead,Construct,Fiend,Aberration,Humanoid,Fey,Elemental,Dragon,Monstrosity,Ooze,Giant,Celestial"
			.split(",");
	private static HashMap<String, Integer> indexes = new HashMap<String, Integer>();

	static {
		for (int i = 0; i < monsterTypes.length; i++)
			indexes.put(monsterTypes[i], i);
	}
	private static String[][] monsters = getEncounterTable("MonsterList.txt");

	public static Double crStringToDouble(String CR) {
		if (!CR.contains("/")) {
			return Double.parseDouble(CR);
		} else {
			String[] params = CR.split("/");
			return Double.parseDouble(params[0])
					/ Double.parseDouble(params[1]);
		}
	}

	private static boolean isHardOrDeadlyEncounter(String[][] table) {
		return Arrays.stream(hardTable).parallel()
				.anyMatch(e -> table.equals(e))
				|| Arrays.stream(deadlyTable).parallel()
						.anyMatch(e -> table.equals(e));
	}

	public static String getEncounter(String[][] table) {
		List<String[]> mobs = new ArrayList<>();
		StringBuilder encounter = new StringBuilder("");
		int attempts = 0;
		int partyLevelIndex = InfoPanel.getPartyLevel() - 1;
		while (mobs.isEmpty() && attempts < 20) {
			encounter.delete(0, encounter.length());
			int result = Dice.roll(table[partyLevelIndex].length - 1);
			if (table[partyLevelIndex][result].trim().isEmpty())
				continue; // skips "empty" encounters.
			encounter.append(table[partyLevelIndex][result].trim());
			encounter.append(" CR:" + CRs[result]);
			int treasureCR = whichTreasureCR(crStringToDouble(CRs[result]));
			encounter.append("\n" + Treasure.getTreasure(treasureCR));
			// if it's hard or deadly, add a treasure hoard
			if (isHardOrDeadlyEncounter(table)) {
				encounter.append("\nHoard: " + Hoard.getHoard(treasureCR));
			}
			encounter.append("\nPossible Monsters:");
			mobs = Arrays.stream(monsters).parallel().unordered()
					// grab only the appropriate CR
					.filter(e -> e[0].equals(CRs[result]))
					// and only those matching the type
					.filter(e -> InfoPanel.getTruth(indexes.get(e[4])))
					.collect(Collectors.toList());
			for (int i = 0; (i < 10 && i < mobs.size()); i++)
				encounter.append("\n" + mobs.get(i)[1] + " XP: "
						+ mobs.get(i)[2] + " Book: " + mobs.get(i)[6]);
			attempts++;
		}
		return encounter.toString();
	}

	private final static List<String> scrollMishaps = ResourceLoader
			.getTable("ScrollMishaps.txt");
	private final static List<String> potionMishaps = ResourceLoader
			.getTable("Potion Mishaps.txt");
	private final static List<String> meleeMishaps = ResourceLoader
			.getTable("Melee Mishaps.txt");

	public static String scrollMishap() {
		return getResultFromTable(Dice.d6(), scrollMishaps);
	}

	public static String meleeMishap() {
		return getResultFromTable(Dice.d100(), meleeMishaps);
	}

	public static String potionMishap() {
		return getResultFromTable(Dice.d100(), potionMishaps);
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

	private final static List<String> itemCommunication = ResourceLoader
			.getTable("Item Communication.txt");
	private final static List<String> itemSenses = ResourceLoader
			.getTable("Item Senses.txt");
	private final static List<String> itemAlign = ResourceLoader
			.getTable("Item Alignment.txt");
	private final static List<String> itemPurpose = ResourceLoader
			.getTable("Item Purpose.txt");

	public static String getSentientItem(String item) {
		item = "_Sentient " + item + "_";
		item += "\nAlignment: " + getResultFromTable(Dice.d100(), itemAlign);
		item += "\nPurpose: " + getResultFromTable(Dice.roll(10), itemPurpose);
		item += "\nCommunication: "
				+ getResultFromTable(Dice.d100(), itemCommunication);
		item += "\nSenses: " + getResultFromTable(Dice.d4(), itemSenses);
		item += "\nInt: " + Dice.statroll();
		item += "\nWis: " + Dice.statroll();
		item += "\nCha: " + Dice.statroll();
		item += "\n______________________";
		return item;
	}

	public static String getWildernessMishap() {
		return getResultFromTable(Dice.d20(), wildernessChase);
	}

	public static String getMadness(int i) {
		if (i == 1)
			return "For " + Dice.roll(10) * 10 + " hours:\n"
					+ getResultFromTable(Dice.d100(), longTerm);
		else if (i == 2)
			return "Lasts until cured:\n"
					+ getResultFromTable(Dice.d100(), indefinite);
		else
			return "For " + Dice.roll(10) + " minutes:\n"
					+ getResultFromTable(Dice.d100(), shortTerm);
	}

}
