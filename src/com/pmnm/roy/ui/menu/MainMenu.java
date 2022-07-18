package com.pmnm.roy.ui.menu;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaAnimation;
import doa.engine.graphics.DoaAnimations;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.maths.DoaVector;

import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.menu.extensions.ExitPopup;

@SuppressWarnings("serial")
public class MainMenu extends RoyMenu {

	private static final DoaVector PLAY_OFFLINE_LOCATION 	= new DoaVector(1377f, 511f);
	private static final DoaVector PLAY_ONLINE_LOCATION 	= new DoaVector(1377f, 584f);
	private static final DoaVector SETTINGS_LOCATION 		= new DoaVector(1377f, 657f);
	private static final DoaVector RULES_LOCATION 			= new DoaVector(1377f, 730f);
	private static final DoaVector EXIT_LOCATION 			= new DoaVector(1377f, 803f);

	private ExitPopup ep;

	public MainMenu() {
		/* Play Offline Button */		
		RoyButton playOfflineButton = RoyButton
			.builder()
			.textKey("PLAY_OFFLINE")
			.action(() -> {
				setVisible(false);
				UIConstants.getPlayOfflineMenu().setVisible(true);	
			})
			.build();
		playOfflineButton.setPosition(PLAY_OFFLINE_LOCATION);
		addElement(playOfflineButton); 
		/* --------------- */
		
		/* Play Online Button */
		RoyButton playOnlineButton = RoyButton
			.builder()
			.textKey("PLAY_ONLINE")
			.action(() -> {
				setVisible(false);
				UIConstants.getPlayOnlineMenu().setVisible(true);
			})
			.build();
		playOnlineButton.setPosition(PLAY_ONLINE_LOCATION);
		addElement(playOnlineButton);
		/* --------------- */
		
		/* Settings Button */
		RoyButton settingsButton = RoyButton
			.builder()
			.textKey("SETTINGS")
			.action(() -> {
				setVisible(false);
				UIConstants.getSettingsMenu().setVisible(true);
			})
			.build();
		settingsButton.setPosition(SETTINGS_LOCATION);
		addElement(settingsButton);
		/* --------------- */
			
		/* Rules Button */
		RoyButton rulesButton = RoyButton
			.builder()
			.textKey("RULES")
			.action(() -> {
				setVisible(false);
				UIConstants.getBackground().setVisible(false);
				UIConstants.getRulesMenu().setVisible(true);
			})
			.build();
		rulesButton.setPosition(RULES_LOCATION);
		addElement(rulesButton);
		/* ------------ */
		
		/* Exit Button and related stuff */
		ep = new ExitPopup();
		addElement(ep);
		
		RoyButton exitButton = RoyButton
			.builder()
			.textKey("EXIT")
			.action(() -> ep.setVisible(true))
			.build();
		exitButton.setPosition(EXIT_LOCATION);
		addElement(exitButton);
		/* ----------------------------- */
		
		addComponent(new Renderer());
	}
	
	private final class Renderer extends DoaRenderer {

		@Override
		public void render() {
			if (!isVisible()) { return; }
			
			DoaAnimation riskLogoAnim = DoaAnimations.getAnimation("RiskLogoAnim");
			DoaGraphicsFunctions.drawAnimation(
				riskLogoAnim,
				1286,
				216,
				riskLogoAnim.current().getWidth(),
				riskLogoAnim.current().getHeight()
			);
		}
	}

	@Override
	public void setVisible(boolean isVisible) {
		super.setVisible(isVisible);
		/* popups should not be affected */
		ep.setVisible(false);
	}
}