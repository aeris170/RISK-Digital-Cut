package com.pmnm.roy.ui.gameui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.TurnPhase;
import com.pmnm.roy.ui.gameui.BottomPanel;

public class CenterPieceButtonAction implements DoaUIAction {

	public CenterPieceButtonAction() {}

	@Override
	public void execute() {
		GameManager gm = GameManager.INSTANCE;
		if (gm.currentPhase == TurnPhase.DRAFT) {
			gm.draftReinforce(BottomPanel.spinnerValues.get(BottomPanel.index));
		} else if (gm.currentPhase == TurnPhase.ATTACK) {
			if (BottomPanel.spinnerValues != null) {
				gm.moveTroopsAfterOccupying(BottomPanel.spinnerValues.get(BottomPanel.index));
				BottomPanel.nextPhaseButton.enable();
				BottomPanel.nullSpinner();
			}
		} else if (gm.currentPhase == TurnPhase.REINFORCE) {
			gm.reinforce(BottomPanel.spinnerValues.get(BottomPanel.index));
		}
	}
}