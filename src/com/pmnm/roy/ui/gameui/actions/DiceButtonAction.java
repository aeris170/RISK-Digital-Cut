package com.pmnm.roy.ui.gameui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.main.GameManager;

public class DiceButtonAction implements DoaUIAction {

	private int diceAmount;

	public DiceButtonAction(int diceAmount) {
		this.diceAmount = diceAmount;
	}

	@Override
	public void execute() {
		GameManager.toss(diceAmount);
	}
}