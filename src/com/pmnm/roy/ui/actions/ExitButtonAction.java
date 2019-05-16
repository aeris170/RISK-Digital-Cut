package com.pmnm.roy.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.roy.ui.ExitPopup;

public class ExitButtonAction implements DoaUIAction {

	ExitPopup ep;

	public ExitButtonAction(ExitPopup ep) {
		this.ep = ep;
	}

	@Override
	public void execute() {
		ep.setzOrder(1000);
		ep.show();
	}
}