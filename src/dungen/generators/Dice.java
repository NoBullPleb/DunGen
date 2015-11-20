package dungen.generators;

public class Dice {
	public static int custom(int dice, int sides) {
		int result = 0;
		for (int i = 0; i < dice; i++)
			result += (int) Math.floor(Math.random() * sides) + 1;
		return result;
	}

	public static String custom(String s) {
		int result = 0;
		String[] params = s.split("d");
		int dice = Integer.parseInt(params[0]);
		int sides = Integer.parseInt(params[1].split(" ")[0]);
		String items = s.substring(params.length + 1
				+ params[1].split(" ").length);
		for (int i = 0; i < dice; i++)
			result += (int) Math.floor(Math.random() * sides) + 1;
		return result + " " + items;
	}

	public static int custom(int sides) {
		return custom(1, sides);
	}

	public static int d20() {
		return custom(1, 20);
	}
}
