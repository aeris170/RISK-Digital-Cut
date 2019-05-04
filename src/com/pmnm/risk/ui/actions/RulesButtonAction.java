package com.pmnm.risk.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.ui.MainMenu;
import com.pmnm.risk.ui.RulesMenu;

public class RulesButtonAction implements DoaUIAction {

	MainMenu mm;
	RulesMenu rm;

	public RulesButtonAction(MainMenu mm, RulesMenu rm) {
		this.mm = mm;
		this.rm = rm;
	}

	@Override
	public void execute() {
		mm.hide();
		rm.show();
	}
}