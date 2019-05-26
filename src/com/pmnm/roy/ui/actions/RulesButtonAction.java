package com.pmnm.roy.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.roy.ui.menu.ExitPopup;
import com.pmnm.roy.ui.menu.MainMenu;
import com.pmnm.roy.ui.menu.RulesMenu;

public class RulesButtonAction implements DoaUIAction {

	MainMenu mm;
	RulesMenu rm;
	ExitPopup ep;

	public RulesButtonAction(MainMenu mm, RulesMenu rm, ExitPopup ep) {
		this.mm = mm;
		this.rm = rm;
		this.ep = ep;
	}

	@Override
	public void execute() {
		rm.show();
	}
}