package com.pmnm.risk.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.ui.MainMenu;
import com.pmnm.risk.ui.SinglePlayerMenu;

public class PlayOfflineButtonAction implements DoaUIAction {

	MainMenu mm;
	SinglePlayerMenu spm;

	public PlayOfflineButtonAction(MainMenu mm, SinglePlayerMenu spm) {
		this.mm = mm;
		this.spm = spm;
	}

	@Override
	public void execute() {
		mm.hide();
		spm.show();
	}
}