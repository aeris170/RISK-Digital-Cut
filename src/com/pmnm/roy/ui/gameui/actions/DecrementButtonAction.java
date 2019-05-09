package com.pmnm.roy.ui.gameui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.roy.ui.gameui.BottomPanel;

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