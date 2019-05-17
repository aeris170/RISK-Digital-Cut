package com.pmnm.roy.ui.actions;

import java.io.IOException;

import com.doa.ui.action.DoaUIAction;
import com.pmnm.risk.main.GameInstance;
import com.pmnm.roy.ui.ExitPopup;
import com.pmnm.roy.ui.LoadMenu;
import com.pmnm.roy.ui.MainMenu;
import com.pmnm.roy.ui.PlayOfflineMenu;
import com.pmnm.roy.ui.RulesMenu;

public class LoadButtonAction implements DoaUIAction {

	MainMenu mm;
	LoadMenu lm;
	ExitPopup ep;
	PlayOfflineMenu pom;

	public LoadButtonAction(MainMenu mm, LoadMenu lm, ExitPopup ep, PlayOfflineMenu pom) {
		this.mm = mm;
		this.lm = lm;
		this.ep = ep;
		this.pom = pom;
	}

	@Override
	public void execute() {
		mm.hide();
		ep.hide();
		pom.hide();
		//lm.show();
		try {
			GameInstance.loadGame();
		} catch (ClassNotFoundException | IOException ex) {
			ex.printStackTrace();
		}
	}
}