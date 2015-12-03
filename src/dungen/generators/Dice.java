package dungen.generators;

import java.util.Arrays;

public class Dice {
	public static int roll(int dice, int sides) {
		int result = 0;
		for (int i = 0; i < dice; i++)
			result += (int) Math.floor(Math.random() * sides) + 1;
		return result;
	}

	public final static int statroll() {
		int[] rolls = new int[4];
		Arrays.setAll(rolls, e -> (int) (Math.random() * 6) + 1);
		Arrays.sort(rolls);
		return (rolls[3] + rolls[2] + rolls[1]);
	}

	public static String roll(String s) {
		int result = 0;
		String[] params = s.split("d");
		int dice = Integer.parseInt(params[0].trim());
		int sides = Integer.parseInt(params[1].split(" ")[0].trim());
		String items = s.substring(params.length + 1
				+ params[1].split(" ").length);
		for (int i = 0; i < dice; i++)
			result += (int) Math.floor(Math.random() * sides) + 1;
		return result + " " + items;
	}

	public static int roll(int sides) {
		return roll(1, sides);
	}

	public static int d100() {
		return roll(1, 20);
	}

	public static int d20() {
		return roll(1, 20);
	}

	public static int d6() {
		return roll(1, 6);
	}

	public static int d4() {
		return roll(1, 4);
	}
}
