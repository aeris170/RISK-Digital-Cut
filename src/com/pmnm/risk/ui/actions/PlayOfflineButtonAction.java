package com.pmnm.risk.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.ui.MainMenu;

public class PlayOfflineButtonAction implements DoaUIAction {

	MainMenu mm;

	public PlayOfflineButtonAction(MainMenu mm) {
		this.mm = mm;
	}

	@Override
	public void execute() {
		mm.setActive(false);
	}

}