package com.pmnm.risk.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.ui.MainMenu;
import com.pmnm.risk.ui.gameui.RiskGameScreenUI;

public class PlayOfflineButtonAction implements DoaUIAction {

	MainMenu mm;

	public PlayOfflineButtonAction(MainMenu mm) {
		this.mm = mm;
	}

	@Override
	public void execute() {
		mm.setActive(false);
		RiskGameScreenUI.initUI();
	}

}