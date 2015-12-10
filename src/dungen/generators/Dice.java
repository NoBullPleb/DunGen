package dungen.generators;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.IntStream;

public class Dice {

	public static int roll(int dice, int sides) {
		return IntStream.generate(() -> roll(sides)).limit(dice).sum();
	}

	public final static int statroll() {
		return IntStream.generate(() -> roll(6)).limit(4).sorted().skip(1)
				.sum();
	}

	public static String roll(String s) {
		int result = 0;
		String[] params = s.split("d");
		int dice = Integer.parseInt(params[0].trim()), sides = Integer
				.parseInt(params[1].split(" ")[0].trim());
		String items = s.substring(params.length + 1
				+ params[1].split(" ").length);
		result = roll(dice, sides);
		return result + " " + items;
	}

	public static int roll(int sides) {
		try {
			return (Math.abs(SecureRandom.getInstanceStrong().nextInt()) % sides) + 1;
		} catch (NoSuchAlgorithmException err) {
			err.printStackTrace();
			return 0;
		}
	}

	public static int d100() {
		return roll(100);
	}

	public static int d20() {
		return roll(20);
	}

	public static int d6() {
		return roll(6);
	}

	public static int d4() {
		return roll(4);
	}
}
