package com.pmnm.risk.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.ui.ExitPopup;
import com.pmnm.risk.ui.MainMenu;
import com.pmnm.risk.ui.MultiPlayerMenu;

public class PlayOnlineButtonAction implements DoaUIAction {

	MainMenu mm;
	MultiPlayerMenu mpm;
	ExitPopup ep;

	public PlayOnlineButtonAction(MainMenu mm, MultiPlayerMenu mpm, ExitPopup ep) {
		this.mm = mm;
		this.mpm = mpm;
		this.ep = ep;
	}

	@Override
	public void execute() {
		mm.hide();
		ep.hide();
		mpm.show();
	}
}