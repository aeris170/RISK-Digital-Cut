package com.pmnm.risk.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.ui.MainMenu;
import com.pmnm.risk.ui.MultiPlayerMenu;

public class PlayOnlineButtonAction implements DoaUIAction {

	MainMenu mm;
	MultiPlayerMenu mpm;

	public PlayOnlineButtonAction(MainMenu mm, MultiPlayerMenu mpm) {
		this.mm = mm;
		this.mpm = mpm;
	}

	@Override
	public void execute() {
		mm.hide();
		mpm.show();
	}
}