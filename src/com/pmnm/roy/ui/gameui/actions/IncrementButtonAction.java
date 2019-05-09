package com.pmnm.roy.ui.gameui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.roy.ui.gameui.BottomPanel;

public class IncrementButtonAction implements DoaUIAction {

	private BottomPanel bottomPanel;

	public IncrementButtonAction(BottomPanel bottomPanel) {
		this.bottomPanel = bottomPanel;
	}

	@Override
	public void execute() {
		bottomPanel.incrementIndex();
	}
}