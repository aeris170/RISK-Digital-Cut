package com.pmnm.risk.dice.exceptions;

public class DiceAccessException extends DiceException {

	private static final long serialVersionUID = 7967077470982351889L;

	public DiceAccessException(int index, int capacity) {
		super("Dice collection index out of range! Index: " + index + " Size: " + capacity);
	}
}