package com.pmnm.roy.ui.gameui.actions;

import com.doa.ui.action.DoaUIAction;
import com.doa.ui.button.DoaImageButton;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.TurnPhase;

public class NextPhaseButtonAction implements DoaUIAction {

	private DoaImageButton button;

	public NextPhaseButtonAction(DoaImageButton button) {
		this.button = button;
	}

	@Override
	public void execute() {
		GameManager gm = GameManager.INSTANCE;
		if (gm.currentPhase == TurnPhase.DRAFT) {
			gm.nextPhase();
		} else if (gm.currentPhase == TurnPhase.ATTACK) {
			gm.nextPhase();
		} else if (gm.currentPhase == TurnPhase.REINFORCE) {
			gm.nextPhase();
			//button.disable();
		}
	}
}