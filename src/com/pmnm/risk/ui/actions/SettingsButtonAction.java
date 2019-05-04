package com.pmnm.risk.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.ui.MainMenu;
import com.pmnm.risk.ui.SettingsMenu;

public class SettingsButtonAction implements DoaUIAction {

	MainMenu mm;
	SettingsMenu sm;

	public SettingsButtonAction(MainMenu mm, SettingsMenu sm) {
		this.mm = mm;
		this.sm = sm;
	}

	@Override
	public void execute() {
		mm.hide();
		sm.show();
	}
}