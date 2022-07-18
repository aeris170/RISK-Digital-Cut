package com.pmnm.roy.ui.actions;

import com.pmnm.roy.ui.menu.MainMenu;
import com.pmnm.roy.ui.menu.PlayOnlineMenu;
import com.pmnm.roy.ui.menu.extensions.ExitPopup;

import doa.engine.ui.action.DoaUIAction;

public class PlayOnlineButtonAction implements DoaUIAction {

	MainMenu mm;
	PlayOnlineMenu ponm;
	ExitPopup ep;

	public PlayOnlineButtonAction(MainMenu mm, PlayOnlineMenu ponm, ExitPopup ep) {
		this.mm = mm;
		this.ponm = ponm;
		this.ep = ep;
	}

	@Override
	public void execute() {
		mm.hide();
		ep.hide();
		ponm.show();
	}
}