package com.pmnm.risk.ui.gameui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.ui.gameui.MiddleSectionScroll;

public class CardsButtonAction implements DoaUIAction {

	MiddleSectionScroll mss;

	public CardsButtonAction(MiddleSectionScroll mss) {
		this.mss = mss;
	}

	@Override
	public void execute() {
		mss.move();
	}
}