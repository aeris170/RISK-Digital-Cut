package com.pmnm.risk.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.ui.ExitPopup;
import com.pmnm.risk.ui.MainMenu;
import com.pmnm.risk.ui.SettingsMenu;

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