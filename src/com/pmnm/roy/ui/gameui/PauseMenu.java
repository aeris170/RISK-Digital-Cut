package com.pmnm.roy.ui.gameui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.input.DoaKeyboard;
import doa.engine.maths.DoaVector;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;

@SuppressWarnings("serial")
public final class PauseMenu extends RoyMenu {

	private static final String SETTINGS_KEY 		= "SETTINGS";
	private static final String RULES_KEY 			= "RULES";
	private static final String EXIT_KEY 			= "EXIT";

	private static BufferedImage bg = DoaSprites.getSprite("escapeMenu");
	private static final DoaVector BG_LOCATION 		= new DoaVector((1920 - bg.getWidth()) / 2f, (1080 - bg.getHeight()) / 2f);

	private static final DoaVector SETTINGS_LOCATION 		= new DoaVector(1377f, 657f);
	private static final DoaVector RULES_LOCATION 			= new DoaVector(0f, 0f);
	private static final DoaVector EXIT_LOCATION 			= new DoaVector(1377f, 803f);
	private static final DoaVector MAIN_MENU_LOCATION 		= new DoaVector(1377f, 803f);
	
	RoyButton rulesButton;

	public PauseMenu() {

		/* Save Button */	

		/* --------------- */


		/* Load Button */	

		/* --------------- */
		
		/* Rules Button */
		rulesButton = RoyButton
			.builder()
			.textKey(RULES_KEY)
			.action(() -> {
				setVisible(false);
				UIConstants.getBackground().setVisible(false);
				UIConstants.getRulesMenu().setVisible(true);
			})
			.build();
		rulesButton.setPosition(RULES_LOCATION);
		addElement(rulesButton);
		/* --------------- */

		/* Main Menu Button */
		RoyButton mainMenuButton = RoyButton
			.builder()
			.textKey("BACK")
			.action(() -> {
				setVisible(false);
				UIConstants.getMainMenu().setVisible(true);	
			})
			.build();
		mainMenuButton.setPosition(MAIN_MENU_LOCATION);
		addElement(mainMenuButton); 
		/* --------------- */


		/* Exit Button */	

		/* --------------- */

		addComponent(new Script());
		addComponent(new Renderer());
	}

	private final class Script extends DoaScript {
		@Override
		public void tick() {
			if (DoaKeyboard.ESCAPE) {
				setVisible(!isVisible());
			}
		}
	}

	private final class Renderer extends DoaRenderer {
		@Override
		public void render() {
			if(!isVisible()) return;

			DoaGraphicsFunctions.setColor(new Color(0, 0, 0, 100));
			DoaGraphicsFunctions.fillRect(0, 0, 1920, 1080);
			
			DoaGraphicsFunctions.drawImage(bg, BG_LOCATION.x, BG_LOCATION.y, bg.getWidth(), bg.getHeight());
			
			DoaGraphicsFunctions.setColor(Color.WHITE);
			DoaGraphicsFunctions.drawString("PAUSE MENU", 840, 380);
		}
		
	}
}