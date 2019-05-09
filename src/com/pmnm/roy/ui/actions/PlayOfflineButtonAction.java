package com.pmnm.roy.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.roy.ui.ExitPopup;
import com.pmnm.roy.ui.MainMenu;
import com.pmnm.roy.ui.SinglePlayerMenu;

public class PlayOfflineButtonAction implements DoaUIAction {

	MainMenu mm;
	SinglePlayerMenu spm;
	ExitPopup ep;

	public PlayOfflineButtonAction(MainMenu mm, SinglePlayerMenu spm, ExitPopup ep) {
		this.mm = mm;
		this.spm = spm;
		this.ep = ep;
	}

	@Override
	public void execute() {
		mm.hide();
		ep.hide();
		spm.show();
	}
}