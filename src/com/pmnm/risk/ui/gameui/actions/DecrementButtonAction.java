package com.pmnm.risk.ui.gameui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.ui.gameui.BottomPanel;

public class DecrementButtonAction implements DoaUIAction {

	private BottomPanel bottomPanel;

	public DecrementButtonAction(BottomPanel bottomPanel) {
		this.bottomPanel = bottomPanel;
	}

	@Override
	public void execute() {
		bottomPanel.decrementIndex();
	}
}