package com.pmnm.risk.dice;

import java.util.concurrent.ThreadLocalRandom;

import com.pmnm.risk.card.Card;
import com.pmnm.risk.dice.exceptions.DiceAccessException;
import com.pmnm.risk.dice.exceptions.DiceInstantiationException;

public class Dice {

	public static final Dice ATTACK_DICE_3 = new Dice(3);
	public static final Dice ATTACK_DICE_2 = new Dice(2);
	public static final Dice ATTACK_DICE_1 = new Dice(1);
	public static final Dice DEFENCE_DICE_2 = new Dice(2);
	public static final Dice DEFENCE_DICE_1 = new Dice(1);

	private int[] values;

	private Dice(int numberOfDice) {
		if (numberOfDice <= 0) {
			throw new DiceInstantiationException();
		}
		values = new int[numberOfDice];
	}

	public int[] rollAllAndGetAll() {
		rollAll();
		return values;
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
