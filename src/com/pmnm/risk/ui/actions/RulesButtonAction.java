package com.pmnm.risk.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.ui.ExitPopup;
import com.pmnm.risk.ui.MainMenu;
import com.pmnm.risk.ui.RulesMenu;

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
		mm.hide();
		ep.hide();
		rm.show();
	}
}