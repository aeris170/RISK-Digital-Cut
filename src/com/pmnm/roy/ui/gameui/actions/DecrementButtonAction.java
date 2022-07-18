package com.pmnm.roy.ui.gameui.actions;

import com.pmnm.roy.ui.gameui.BottomPanel;

public class DecrementButtonAction implements UIAction {

	private BottomPanel bottomPanel;

	public DecrementButtonAction(BottomPanel bottomPanel) {
		this.bottomPanel = bottomPanel;
	}

	@Override
	public void execute() {
		//bottomPanel.decrementIndex();
	}
}