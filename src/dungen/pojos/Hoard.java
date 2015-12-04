package dungen.pojos;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import dungen.generators.Dice;
import dungen.generators.Tables;
import dungen.resourceLoader.ResourceLoader;
import dungen.ui.InfoPanel;

@SuppressWarnings("unchecked")
public class Hoard {

	public static String getHoard(int treasureCR) {
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
		return getHoard(Tables.whichTreasureCR(InfoPanel.getPartyLevel() + 0.0));
	}

	public static String getMagicItem(int number, List<String> table) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < number; i++) {
			result.append("\n" + getMagicItem(table));
		}
		return result.toString();
	}

	private static boolean rarityMatch(String rarity, String item) {
		item = item.split(",")[1].trim().toLowerCase();
		rarity = rarity.toLowerCase().trim();
		return item.equals(rarity) || rarity.isEmpty();
	}

	public static String getSentientMagicItem(String s) {
		return Tables.getSentientItem(getMagicItem(s));
	}

	public static String getMagicItem(String string) {
		List<String> items = magicItemDetails.parallelStream()
				.filter(item -> rarityMatch(string, item))
				.collect(Collectors.toList());
		// picks one at random.
		return items.get(Dice.roll(items.size()) - 1);
	}

	public static String getMagicItem(List<String> table) {
		String item = Tables.getResultFromTable(Dice.roll(100), table);
		if (item.toLowerCase().contains("scroll"))
			item = generateScroll(item);
		else {
			item = getItemDetails(item);
			// if the stars align...
			if (Dice.roll(100) == 42 && !item.toLowerCase().contains("potion")) {
				item = Tables.getSentientItem(item);
			}
		}
		return item;
	}

	private final static List<String> magicItemDetails = ResourceLoader
			.getTable("Magic Items.txt");

	private static String getItemDetails(String item) {
		return magicItemDetails.parallelStream()
				.filter(e -> e.split(",")[0].equalsIgnoreCase(item))
				// Tries to find a match, if not, returns the original
				.findAny().orElse(item);
	}

	private static String generateScroll(String item) {
		if (item.contains("cantrip")) {
			item = item.replace("cantrip", Tables.getSpell(0));
		} else if (item.contains("1st level")) {
			item = item.replace("1st level", Tables.getSpell(1));
		} else if (item.contains("2nd level")) {
			item = item.replace("2nd level", Tables.getSpell(2));
		} else if (item.contains("3rd level")) {
			item = item.replace("3rd level", Tables.getSpell(3));
		} else if (item.contains("4th level")) {
			item = item.replace("4th level", Tables.getSpell(4));
		} else if (item.contains("5th level")) {
			item = item.replace("5th level", Tables.getSpell(5));
		} else if (item.contains("6th level")) {
			item = item.replace("6th level", Tables.getSpell(6));
		} else if (item.contains("7th level")) {
			item = item.replace("7th level", Tables.getSpell(7));
		} else if (item.contains("8th level")) {
			item = item.replace("8th level", Tables.getSpell(8));
		} else if (item.contains("9th level")) {
			item = item.replace("9th level", Tables.getSpell(9));
		}
		return item;
	}

	private final static List<String> magicItemA = ResourceLoader
			.getTable("Magic Item Table A.txt");
	private final static List<String> magicItemB = ResourceLoader
			.getTable("Magic Item Table B.txt");
	private final static List<String> magicItemC = ResourceLoader
			.getTable("Magic Item Table C.txt");
	private final static List<String> magicItemD = ResourceLoader
			.getTable("Magic Item Table D.txt");
	private final static List<String> magicItemE = ResourceLoader
			.getTable("Magic Item Table E.txt");
	private final static List<String> magicItemF = ResourceLoader
			.getTable("Magic Item Table F.txt");
	private final static List<String> magicItemG = ResourceLoader
			.getTable("Magic Item Table G.txt");
	private final static List<String> magicItemH = ResourceLoader
			.getTable("Magic Item Table H.txt");
	private final static List<String> magicItemI = ResourceLoader
			.getTable("Magic Item Table I.txt");

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
		/*
		 * 3d6 1,000 gp gems 1d10 2,500 gp art objects 1d4 7,500 gp art objects
		 * 1d8 5,000 gp gems
		 */
		// GROUP 3 AND FINAL
		iHoardByCR[3] = new Supplier[25];
		iHoardByCR[3][0] = () -> hoardC3();

		iHoardByCR[3][1] = () -> hoardC3() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(Dice.roll(8), magicItemC);
		iHoardByCR[3][2] = () -> hoardC3()
				+ Dice.roll("1d10 2,500 gp art objects ")
				+ getMagicItem(Dice.roll(8), magicItemC);
		iHoardByCR[3][3] = () -> hoardC3()
				+ Dice.roll("1d4 7,500 gp art objects ")
				+ getMagicItem(Dice.roll(8), magicItemC);
		iHoardByCR[3][4] = () -> hoardC3() + Dice.roll("1d8 5,000 gp gems ")
				+ getMagicItem(Dice.roll(8), magicItemC);

		iHoardByCR[3][5] = () -> hoardC3() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(Dice.roll(6), magicItemD);
		iHoardByCR[3][6] = () -> hoardC3()
				+ Dice.roll("1d10 2,500 gp art objects ")
				+ getMagicItem(Dice.roll(6), magicItemD);
		iHoardByCR[3][7] = () -> hoardC3()
				+ Dice.roll("1d4 7,500 gp art objects")
				+ getMagicItem(Dice.roll(6), magicItemD);
		iHoardByCR[3][8] = () -> hoardC3() + Dice.roll("1d8 5,000 gp gems ")
				+ getMagicItem(Dice.roll(6), magicItemD);

		iHoardByCR[3][9] = () -> hoardC3() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(Dice.roll(6), magicItemE);
		iHoardByCR[3][10] = () -> hoardC3()
				+ Dice.roll("1d10 2,500 gp art objects ")
				+ getMagicItem(Dice.roll(6), magicItemE);
		iHoardByCR[3][11] = () -> hoardC3()
				+ Dice.roll("1d4 7,500 gp art objects")
				+ getMagicItem(Dice.roll(6), magicItemE);
		iHoardByCR[3][12] = () -> hoardC3() + Dice.roll("1d8 5,000 gp gems ")
				+ getMagicItem(Dice.roll(6), magicItemE);

		iHoardByCR[3][13] = () -> hoardC3() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemG);
		iHoardByCR[3][14] = () -> hoardC3()
				+ Dice.roll("1d10 2,500 gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemG);
		iHoardByCR[3][15] = () -> hoardC3()
				+ Dice.roll("1d4 7,500 gp art objects")
				+ getMagicItem(Dice.roll(4), magicItemG);
		iHoardByCR[3][16] = () -> hoardC3() + Dice.roll("1d8 5,000 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemG);

		iHoardByCR[3][17] = () -> hoardC3() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemH);
		iHoardByCR[3][18] = () -> hoardC3()
				+ Dice.roll("1d10 2,500 gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemH);
		iHoardByCR[3][19] = () -> hoardC3()
				+ Dice.roll("1d4 7,500 gp art objects")
				+ getMagicItem(Dice.roll(4), magicItemH);
		iHoardByCR[3][20] = () -> hoardC3() + Dice.roll("1d8 5,000 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemH);

		iHoardByCR[3][21] = () -> hoardC3() + Dice.roll("3d6 1,000 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemI);
		iHoardByCR[3][22] = () -> hoardC3()
				+ Dice.roll("1d10 2,500 gp art objects ")
				+ getMagicItem(Dice.roll(4), magicItemI);
		iHoardByCR[3][23] = () -> hoardC3()
				+ Dice.roll("1d4 7,500 gp art objects")
				+ getMagicItem(Dice.roll(4), magicItemI);
		iHoardByCR[3][24] = () -> hoardC3() + Dice.roll("1d8 5,000 gp gems ")
				+ getMagicItem(Dice.roll(4), magicItemI);

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

	private static String hoardC3() {
		return Dice.roll(12, 6) * 1000 + "GP " + Dice.roll(8, 6) * 1000 + "PP ";
	}

	static int[][] percentagesforHoard = new int[4][];
	static {
		int[] percents0 = { 6, 16, 26, 36, 44, 52, 60, 65, 70, 75, 78, 80, 85,
				92, 97, 99, 100 };
		int[] percents1 = { 04, 10, 16, 22, 28, 32, 36, 40, 44, 49, 54, 59, 63,
				66, 69, 72, 74, 76, 78, 79, 80, 84, 88, 91, 94, 96, 98, 99, 100 };
		int[] percents2 = { 3, 6, 9, 12, 15, 19, 23, 26, 29, 35, 40, 45, 50,
				54, 58, 62, 66, 68, 70, 72, 74, 76, 78, 80, 82, 85, 88, 90, 92,
				94, 96, 98, 100 };
		int[] percents3 = { 2, 5, 8, 11, 14, 22, 30, 38, 46, 52, 58, 63, 68,
				69, 70, 71, 72, 74, 76, 78, 80, 85, 90, 95, 100 };
		percentagesforHoard[0] = percents0;
		percentagesforHoard[1] = percents1;
		percentagesforHoard[2] = percents2;
		percentagesforHoard[3] = percents3;
	}
}
