package com.pmnm.roy.ui.gameui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import com.pmnm.risk.globals.Scenes;
import com.pmnm.roy.RoyButton;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.ui.UIConstants;
import com.pmnm.roy.ui.ZOrders;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.input.DoaKeyboard;
import doa.engine.maths.DoaVector;
import doa.engine.scene.DoaSceneHandler;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import pmnm.risk.game.IRiskGameContext;

@SuppressWarnings("serial")
public final class PauseMenu extends RoyMenu {

	private static final String SAVE_KEY 		= "SAVE_GAME";
	private static final String LOAD_KEY 		= "LOAD_GAME";
	private static final String SETTINGS_KEY 	= "SETTINGS";
	private static final String RULES_KEY 		= "RULES";
	private static final String QUIT_KEY 		= "BACK_M";

	private static BufferedImage bg = DoaSprites.getSprite("escapeMenu");
	private static final DoaVector BG_LOCATION 		= new DoaVector((1920 - bg.getWidth()) / 2f, (1080 - bg.getHeight()) / 2f);

	private DoaVector SAVE_LOCATION;
	private DoaVector LOAD_LOCATION;
	private DoaVector SETTINGS_LOCATION = new DoaVector();
	private DoaVector RULES_LOCATION;
	private DoaVector QUIT_LOCATION;
	
	public PauseMenu(IRiskGameContext context, GameType type) {		
		float buttonsY = 0;
		float distanceBetweenButtons = 0;
		
		if (type == GameType.SINGLE_PLAYER) {
			buttonsY = 45f;
			distanceBetweenButtons = 75f;
			
			SAVE_LOCATION 		= new DoaVector(BG_LOCATION.x + 45, BG_LOCATION.y + buttonsY);
			LOAD_LOCATION 		= new DoaVector(BG_LOCATION.x + 45, BG_LOCATION.y + buttonsY + distanceBetweenButtons);
			SETTINGS_LOCATION 	= new DoaVector(BG_LOCATION.x + 45, BG_LOCATION.y + buttonsY + distanceBetweenButtons * 2);
			RULES_LOCATION 		= new DoaVector(BG_LOCATION.x + 45, BG_LOCATION.y + buttonsY + distanceBetweenButtons * 3);
			QUIT_LOCATION 		= new DoaVector(BG_LOCATION.x + 45, BG_LOCATION.y + buttonsY + distanceBetweenButtons * 4);
		}
		else if (type == GameType.MULTI_PLAYER) {
			buttonsY = 70f;
			distanceBetweenButtons = 125f;
			
			SETTINGS_LOCATION 	= new DoaVector(BG_LOCATION.x + 45, BG_LOCATION.y + buttonsY);
			RULES_LOCATION 		= new DoaVector(BG_LOCATION.x + 45, BG_LOCATION.y + buttonsY + distanceBetweenButtons);
			QUIT_LOCATION 		= new DoaVector(BG_LOCATION.x + 45, BG_LOCATION.y + buttonsY + distanceBetweenButtons * 2);
		}
		
		addButtons(type);
		
		setzOrder(ZOrders.PAUSE_Z);
		if (type == GameType.SINGLE_PLAYER) {
			addComponent(new ScriptSinglePlayer(context));
		} else if (type == GameType.MULTI_PLAYER) {
			addComponent(new ScriptMultiPlayer(context));
		}
		addComponent(new Renderer());
	}

	private void addButtons(GameType type) {
		if (type == GameType.SINGLE_PLAYER) {
			/* Save Button */	
			RoyButton saveButton = RoyButton
					.builder()
					.textKey(SAVE_KEY)
					.action(source -> {
						setVisible(false);
					})
					.build();
			saveButton.setPosition(SAVE_LOCATION);
			addElement(saveButton);
			/* --------------- */

			/* Load Button */
			RoyButton loadButton = RoyButton
					.builder()
					.textKey(LOAD_KEY)
					.action(source -> {
						setVisible(false);
					})
					.build();
			loadButton.setPosition(LOAD_LOCATION);
			addElement(loadButton);
			/* --------------- */
		}
		
		/* Settings Button */	
		RoyButton settingsButton = RoyButton
				.builder()
				.textKey(SETTINGS_KEY)
				.action(source -> {
					setVisible(false);
				})
				.build();
		settingsButton.setPosition(SETTINGS_LOCATION);
		addElement(settingsButton);
		/* --------------- */
		
		/* Rules Button */
		RoyButton rulesButton = RoyButton
			.builder()
			.textKey(RULES_KEY)
			.action(source -> {
				setVisible(false);
			})
			.build();
		rulesButton.setPosition(RULES_LOCATION);
		addElement(rulesButton);
		/* --------------- */

		/* Quit Button */
		RoyButton mainMenuButton = RoyButton
			.builder()
			.textKey(QUIT_KEY)
			.action(source -> {
				setVisible(false);
				DoaSceneHandler.loadScene(Scenes.MENU_SCENE);
				UIConstants.getMainMenu().setVisible(true);
			})
			.build();
		mainMenuButton.setPosition(QUIT_LOCATION);
		addElement(mainMenuButton); 
		/* --------------- */
	}

	private final class ScriptSinglePlayer extends DoaScript {
		
		private final IRiskGameContext context;
		
		private ScriptSinglePlayer(final IRiskGameContext context) {
			this.context = context;
		}
		
		@Override
		public void tick() {
			List<Character> chars = DoaKeyboard.getTypedChars();
			for (Character c : chars) {
				if (c == KeyEvent.VK_ESCAPE) {	// esc button
					setVisible(!isVisible());
					context.setPaused(isVisible());
				}
			}
		}
	}

	private final class ScriptMultiPlayer extends DoaScript {
		
		@SuppressWarnings("unused")
		private final IRiskGameContext context;
		
		private ScriptMultiPlayer(final IRiskGameContext context) {
			this.context = context;
		}
		
		@Override
		public void tick() {
			List<Character> chars = DoaKeyboard.getTypedChars();
			for (Character c : chars) {
				if (c == KeyEvent.VK_ESCAPE) {	// esc button
					setVisible(!isVisible());
				}
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
			
			//DoaGraphicsFunctions.setColor(Color.WHITE);
			//DoaGraphicsFunctions.drawString("PAUSE MENU", 840, 380);
		}
		
	}
}