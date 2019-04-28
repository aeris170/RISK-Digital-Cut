package com.pmnm.risk.ui.gameui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.TurnPhase;
import com.pmnm.risk.ui.gameui.BottomPanel;

public class CenterPieceButtonAction implements DoaUIAction {

	public CenterPieceButtonAction() {}

	@Override
	public void execute() {
		if (GameManager.currentPhase == TurnPhase.DRAFT) {
			GameManager.draftReinforce(BottomPanel.spinnerValues.get(BottomPanel.index));
		} else if (GameManager.currentPhase == TurnPhase.ATTACK) {

		} else if (GameManager.currentPhase == TurnPhase.REINFORCE) {

		}
	}
}