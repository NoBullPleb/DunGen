package dungen.generators;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Dice {
	public static int roll(int dice, int sides) {
		int result = 0;
		for (int i = 0; i < dice; i++)
			try {
				result += (Math.abs(SecureRandom.getInstanceStrong().nextInt()) % sides) + 1;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		return result;
	}

	public final static int statroll() {
		int[] rolls = new int[4];
		Arrays.setAll(rolls, e -> roll(6));
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
		result = roll(dice, sides);
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
