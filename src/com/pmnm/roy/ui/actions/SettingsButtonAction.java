package com.pmnm.roy.ui.actions;

import com.pmnm.roy.ui.menu.MainMenu;
import com.pmnm.roy.ui.menu.SettingsMenu;
import com.pmnm.roy.ui.menu.extensions.ExitPopup;

import doa.engine.ui.action.DoaUIAction;

public class SettingsButtonAction implements DoaUIAction {

	MainMenu mm;
	SettingsMenu sm;
	ExitPopup ep;

	public SettingsButtonAction(MainMenu mm, SettingsMenu sm, ExitPopup ep) {
		this.mm = mm;
		this.sm = sm;
		this.ep = ep;
	}

	@Override
	public void execute() {
		mm.hide();
		ep.hide();
		sm.show();
	}
}