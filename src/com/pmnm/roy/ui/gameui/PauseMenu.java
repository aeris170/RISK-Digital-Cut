package com.pmnm.roy.ui.gameui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaKeyboard;
import com.doa.engine.scene.DoaSceneHandler;
import com.doa.engine.task.DoaTasker;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.doa.utils.DoaUtils;
import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.globals.Scenes;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIConstants;

public final class PauseMenu extends DoaPanel {

	private static final long serialVersionUID = 8401721289376342254L;

	public PauseMenu() {
		super(Main.WINDOW_WIDTH * 0.5f - DoaSprites.get("escapeMenu").getWidth() * 0.5f, Main.WINDOW_HEIGHT * 0.291f, (int) (Main.WINDOW_WIDTH * 0.240f),
		        (int) (Main.WINDOW_HEIGHT * 0.419f));
		/* saveButton.addAction(() -> { // TODO add to languages
		 * saveButton.setText(Translator.getInstance().getTranslatedString("SAVING")); new Thread(() -> {
		 * GameInstance.saveGame();
		 * saveButton.setText(Translator.getInstance().getTranslatedString("SAVED")); }).start(); });
		 * loadButton.addAction(() -> { try { GameInstance.loadGame(); } catch (ClassNotFoundException |
		 * IOException ex) { ex.printStackTrace(); } }); */
		rulesButton.addAction(() -> UIConstants.rm.show());
		backButton.addAction(() -> {
			UIConstants.fb.show();
			UIConstants.mm.show();
			DoaSceneHandler.loadScene(Scenes.MENU_SCENE);
			Scenes.GAME_SCENE.clear();
		});
		exitButton.addAction(RiskGameScreenUI.ExitPopup::show);
		add(exitButton);
		add(backButton);
		add(rulesButton);
		add(loadButton);
		add(saveButton);
		DoaTasker.executeNow(() -> {
			while (true) {
				if (DoaKeyboard.ESCAPE) {
					if (isVisible) {
						hide();
						BottomPanel.signal();
						RiskGameScreenUI.ExitPopup.hide();
					} else {
						show();
					}
					GameManager.INSTANCE.isPaused = isVisible;
					DoaUtils.sleepFor(199);
				}
				DoaUtils.sleepFor(1);
			}
		});
		hide();
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		Composite oldComposite = g.getComposite();
		g.setColor(Color.darkGray);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		g.fillRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		g.setComposite(oldComposite);
		g.drawImage(DoaSprites.get("escapeMenu"), position.x + 10, position.y, width, height);
	}

	private TextImageButton saveButton = Builders.TIBB.args(new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.342f), (int) (Main.WINDOW_WIDTH * 0.202),
	        (int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "SAVE_GAME", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR, true)
	        .instantiate();
	private TextImageButton loadButton = Builders.TIBB.args(new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.407f), (int) (Main.WINDOW_WIDTH * 0.202),
	        (int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "LOAD_GAME", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR, true)
	        .instantiate();
	private TextImageButton rulesButton = Builders.TIBB.args(new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.472f), (int) (Main.WINDOW_WIDTH * 0.202),
	        (int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "RULES", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR, true)
	        .instantiate();
	private TextImageButton backButton = Builders.TIBB.args(new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.537f), (int) (Main.WINDOW_WIDTH * 0.202),
	        (int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "BACK_M", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR, true)
	        .instantiate();
	private TextImageButton exitButton = Builders.TIBB.args(new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.602f), (int) (Main.WINDOW_WIDTH * 0.202),
	        (int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "EXIT", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR, true)
	        .instantiate();
}