package com.pmnm.roy.ui.menu;

import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.maths.DoaVector;

@SuppressWarnings("serial")
public class PlayOfflineMenu extends RoyMenu {

	private static final String NEW_GAME_KEY 	= "NEW_GAME";
	private static final String LOAD_GAME_KEY 	= "LOAD_GAME";
	private static final String BACK_KEY 		= "BACK";

	private static final DoaVector NEW_GAME_LOCATION 	= new DoaVector(1377f, 511f);
	private static final DoaVector LOAD_GAME_LOCATION 	= new DoaVector(1377f, 584f);
	private static final DoaVector BACK_LOCATION 		= new DoaVector(1377f, 803f);

	public PlayOfflineMenu() {
		RoyButton newGameButton = RoyButton
			.builder()
			.textKey(NEW_GAME_KEY)
			.action(source -> {
				setVisible(false);
				UIConstants.getNewGameMenuSP().setVisible(true);
			})
			.build();
		newGameButton.setPosition(NEW_GAME_LOCATION);
		addElement(newGameButton);

		RoyButton loadGameButton = RoyButton
			.builder()
			.textKey(LOAD_GAME_KEY)
			.action(source -> {
				setVisible(false);
				UIConstants.getLoadGameMenu().setVisible(true);
			})
			.build();
		loadGameButton.setPosition(LOAD_GAME_LOCATION);
		addElement(loadGameButton);

		RoyButton backButton = RoyButton
			.builder()
			.textKey(BACK_KEY)
			.action(source -> {
				setVisible(false);
				UIConstants.getMainMenu().setVisible(true);
			})
			.build();
		backButton.setPosition(BACK_LOCATION);
		addElement(backButton);
	}
}
