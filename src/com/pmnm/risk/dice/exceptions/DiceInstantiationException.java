package com.pmnm.risk.dice.exceptions;

public class DiceInstantiationException extends DiceException {

	private static final long serialVersionUID = 22638279614446556L;

	public DiceInstantiationException() {
		super("A Dice object must contain at least one dice!");
	}
}
