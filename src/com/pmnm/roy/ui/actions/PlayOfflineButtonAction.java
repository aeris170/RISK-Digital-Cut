package com.pmnm.roy.ui.actions;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.roy.ui.ExitPopup;
import com.pmnm.roy.ui.LoadMenu;
import com.pmnm.roy.ui.MainMenu;
import com.pmnm.roy.ui.PlayOfflineMenu;
import com.pmnm.roy.ui.SinglePlayerMenu;

public class PlayOfflineButtonAction implements DoaUIAction {

	MainMenu mm;
	PlayOfflineMenu pom;
	SinglePlayerMenu spm;
	ExitPopup ep;
	LoadMenu lm;

	public PlayOfflineButtonAction(MainMenu mm, PlayOfflineMenu pom, ExitPopup ep, LoadMenu lm) {
		this.mm = mm;
		this.pom = pom;
		this.ep = ep;
		this.lm = lm;
	}

	@Override
	public void execute() {
		mm.hide();
		ep.hide();
		lm.hide();
		pom.show();
	}
}