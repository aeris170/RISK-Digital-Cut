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
		if(ProvinceHitArea.numberOfRemainingBeginningTroops <= 0) {
			GameManager.nextPhase();
			if(GameManager.currentPhase == TurnPhase.DRAFT) {
				GameManager.turnCount++;
			}
		}
	}

}
