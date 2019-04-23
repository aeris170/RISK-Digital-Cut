package com.pmnm.risk.ui.gameui.actions;

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
		if (GameManager.currentPhase == TurnPhase.DRAFT) {
			GameManager.nextPhase();
		} else if (GameManager.currentPhase == TurnPhase.ATTACK) {
			GameManager.nextPhase();
		} else if (GameManager.currentPhase == TurnPhase.REINFORCE) {
			GameManager.nextPhase();
			// if (!GameManager.currentPlayer.isLocalPlayer()) {
			button.disable();
			// }
		}
	}
}
