package com.pmnm.risk.ui.gameui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.TurnPhase;
import com.pmnm.risk.map.province.ProvinceHitArea;

public class NextPhaseButtonAction implements DoaUIAction {

	public NextPhaseButtonAction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		if (ProvinceHitArea.numberOfRemainingBeginningTroops <= 0) {
			if (GameManager.currentPhase == TurnPhase.DRAFT) {
				if (ProvinceHitArea.remainingTroopsToPut <= 0) {
					GameManager.nextPhase();
				}
			} else if (GameManager.currentPhase == TurnPhase.ATTACK) {
				GameManager.nextPhase();
			} else if (GameManager.currentPhase == TurnPhase.REINFORCE) {
				GameManager.nextPhase();
				GameManager.turnCount++;
			}
		}
	}
}
