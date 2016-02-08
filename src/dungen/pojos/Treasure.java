package dungen.pojos;

import java.util.function.Supplier;

import dungen.generators.Dice;
import dungen.generators.Tables;
import dungen.ui.InfoPanel;

@SuppressWarnings("unchecked")
public class Treasure {

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

		int treasureCR = Tables.whichTreasureCR(Double.valueOf(InfoPanel
				.getPartyLevel()));
		return "Individual Treasure: "
				+ iTreasureByCR[treasureCR][whichTreasureSize(treasureCR)]
						.get();
	}
}
