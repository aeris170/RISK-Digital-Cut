package com.pmnm.roy.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.roy.ui.ExitPopup;
import com.pmnm.roy.ui.LoadMenu;
import com.pmnm.roy.ui.MainMenu;
import com.pmnm.roy.ui.RulesMenu;

public class LoadButtonAction implements DoaUIAction {

	MainMenu mm;
	LoadMenu lm;
	ExitPopup ep;

	public LoadButtonAction(MainMenu mm, LoadMenu lm, ExitPopup ep) {
		this.mm = mm;
		this.lm = lm;
		this.ep = ep;
	}

	@Override
	public void execute() {
		mm.hide();
		ep.hide();
		lm.show();
	}
}