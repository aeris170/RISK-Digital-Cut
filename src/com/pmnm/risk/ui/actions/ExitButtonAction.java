package com.pmnm.risk.ui.actions;

import com.doa.ui.action.DoaUIAction;

public class ExitButtonAction implements DoaUIAction {

	@Override
	public void execute() {
		System.exit(0);
	}
}