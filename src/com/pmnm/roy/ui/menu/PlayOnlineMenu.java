package com.pmnm.roy.ui.menu;

import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.menu.extensions.InputPopup;
import com.pmnm.roy.ui.menu.extensions.InputPopupDouble;

import doa.engine.maths.DoaVector;

@SuppressWarnings("serial")
public class PlayOnlineMenu extends RoyMenu {
	
	private static final DoaVector HOST_GAME_LOCATION 	= new DoaVector(1377f, 511f);
	private static final DoaVector JOIN_GAME_LOCATION 	= new DoaVector(1377f, 584f);
	private static final DoaVector BACK_LOCATION 		= new DoaVector(1377f, 803f);
	
	private InputPopup hostPopup;
	private InputPopupDouble joinPopup;

	public PlayOnlineMenu() {
		RoyButton hostGameButton = RoyButton
			.builder()
			.textKey("HOST_GAME")
			.action(() -> hostPopup.setVisible(true))
			.build();
		hostGameButton.setPosition(HOST_GAME_LOCATION);
		addElement(hostGameButton);
		
		RoyButton joinGameButton = RoyButton
			.builder()
			.textKey("JOIN_GAME")
			.action(() -> joinPopup.setVisible(true))
			.build();
		joinGameButton.setPosition(JOIN_GAME_LOCATION);
		addElement(joinGameButton);
		
		RoyButton backButton = RoyButton
			.builder()
			.textKey("BACK")
			.action(() -> {
				setVisible(false);
				UIConstants.getMainMenu().setVisible(true);
			})
			.build();
		backButton.setPosition(BACK_LOCATION);
		addElement(backButton);
		
		hostPopup = new InputPopup();
		addElement(hostPopup);
		
		joinPopup = new InputPopupDouble();
		addElement(joinPopup);
		
		setVisible(false);
	}
	
	@Override
	public void setVisible(boolean value) {
		super.setVisible(value);
		hostPopup.setVisible(false);
		joinPopup.setVisible(false);
	}
}
