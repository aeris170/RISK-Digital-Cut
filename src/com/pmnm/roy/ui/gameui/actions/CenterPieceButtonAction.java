package com.pmnm.roy.ui.gameui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.TurnPhase;
import com.pmnm.roy.ui.gameui.BottomPanel;

public class CenterPieceButtonAction implements DoaUIAction {

	public CenterPieceButtonAction() {}

	@Override
	public void execute() {
		if (GameManager.currentPhase == TurnPhase.DRAFT) {
			GameManager.draftReinforce(BottomPanel.spinnerValues.get(BottomPanel.index));
		} else if (GameManager.currentPhase == TurnPhase.ATTACK) {
			if (BottomPanel.spinnerValues != null) {
				GameManager.moveTroopsAfterOccupying(BottomPanel.spinnerValues.get(BottomPanel.index));
				BottomPanel.nextPhaseButton.enable();
				BottomPanel.nullSpinner();
			}
		} else if (GameManager.currentPhase == TurnPhase.REINFORCE) {
			GameManager.reinforce(BottomPanel.spinnerValues.get(BottomPanel.index));
		}
	}
}