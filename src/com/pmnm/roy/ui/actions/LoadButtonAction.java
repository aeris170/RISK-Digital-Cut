package com.pmnm.roy.ui.actions;

import com.pmnm.roy.ui.menu.ExitPopup;
import com.pmnm.roy.ui.menu.LoadGameMenu;
import com.pmnm.roy.ui.menu.MainMenu;
import com.pmnm.roy.ui.menu.PlayOfflineMenu;

import doa.engine.ui.action.DoaUIAction;

public class LoadButtonAction implements DoaUIAction {

	MainMenu mm;
	LoadGameMenu lm;
	ExitPopup ep;
	PlayOfflineMenu pom;

	public LoadButtonAction(MainMenu mm, LoadGameMenu lm, ExitPopup ep, PlayOfflineMenu pom) {
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
		// lm.show();
		/* try { GameInstance.loadGame(); } catch (ClassNotFoundException | IOException ex) {
		 * ex.printStackTrace(); } */
	}
}