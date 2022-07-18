package com.pmnm.roy.ui.gameui.actions;

public class DiceButtonAction implements UIAction {

	private int diceAmount;

	public DiceButtonAction(int diceAmount) {
		this.diceAmount = diceAmount;
	}

	@Override
	public void execute() {
		// TODO
		// GameManager.INSTANCE.toss(diceAmount);
	}
}