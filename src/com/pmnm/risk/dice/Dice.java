package com.pmnm.risk.dice;

import java.util.concurrent.ThreadLocalRandom;

import com.pmnm.risk.dice.exceptions.DiceAccessException;
import com.pmnm.risk.dice.exceptions.DiceInstantiationException;

public class Dice {

	private int[] values;

	public Dice(int numberOfDice) {
		if (numberOfDice <= 0) {
			throw new DiceInstantiationException();
		}
		values = new int[numberOfDice];
	}

	public int[] getAllValues() {
		return values;
	}

	public int getValueAt(int index) {
		if (index < 0 || index >= values.length) {
			throw new DiceAccessException(index, values.length);
		}
		return values[index];
	}

	public void rollOne(int diceIndex) {
		values[diceIndex] = ThreadLocalRandom.current().nextInt(6) + 1;
	}

	public void rollAll() {
		for (int i = 0; i < values.length; i++) {
			values[i] = ThreadLocalRandom.current().nextInt(6) + 1;
		}
	}

	public static Dice randomlyGenerate(int numberOfDice) {
		Dice d = new Dice(numberOfDice);
		d.rollAll();
		return d;
	}
}
