package dungen.generators;

public class Dice {
	public static int custom(int dice, int sides) {
		int result = 0;
		for (int i = 0; i < dice; i++)
			result += (int) Math.floor(Math.random() * sides) + 1;
		return result;
	}

	public static int custom(int sides) {
		return custom(1, sides);
	}
	public static int d20() {
		return custom(1, 20);
	}
}
