package com.pmnm.roy.ui.menu;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.io.IOException;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaKeyboard;
import com.doa.engine.task.DoaTasker;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.doa.utils.DoaUtils;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.GameInstance;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIInit;
import com.pmnm.roy.ui.gameui.BottomPanel;

public final class PauseMenu extends DoaPanel {

	private static final long serialVersionUID = 8401721289376342254L;

	public PauseMenu() {
		super(Main.WINDOW_WIDTH * 0.5f - DoaSprites.get("escapeMenu").getWidth() * 0.5f, Main.WINDOW_HEIGHT * 0.291f, (int) (Main.WINDOW_WIDTH * 0.240f),
		        (int) (Main.WINDOW_HEIGHT * 0.419f));

		saveButton.addAction(() -> {
			//todo add to languages
			saveButton.setText(Translator.getInstance().getTranslatedString("SAVING") + "SAVING...");
			new Thread(() -> {
				GameInstance.saveGame();
				saveButton.setText(Translator.getInstance().getTranslatedString("SAVED") + "SAVED!");
			}).start();
		});
		loadButton.addAction(() -> {
			try {
				GameInstance.loadGame();
			} catch (ClassNotFoundException | IOException ex) {
				ex.printStackTrace();
			}
		});
		rulesButton.addAction(() -> {
			UIInit.rm.show();
		});
		backButton.addAction(() -> {
			hide();
			UIInit.mm.show();
		});
		exitButton.addAction(() -> UIInit.ep.show());
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
	public void tick() {

	}

	@Override
	public void render(DoaGraphicsContext g) {
		Composite oldComposite = g.getComposite();
		g.setColor(Color.darkGray);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		g.fillRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		g.setComposite(oldComposite);
		g.drawImage(DoaSprites.get("escapeMenu"), position.x + 10, position.y, width, height);
	}

	private TextImageButton saveButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.342f),
	        (int) (Main.WINDOW_WIDTH * 0.202), (int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "SAVE_GAME",
	        UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	private TextImageButton loadButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.407f),
	        (int) (Main.WINDOW_WIDTH * 0.202), (int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "LOAD_GAME",
	        UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	private TextImageButton rulesButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.472f),
	        (int) (Main.WINDOW_WIDTH * 0.202), (int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "RULES",
	        UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	private TextImageButton backButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.537f),
	        (int) (Main.WINDOW_WIDTH * 0.202), (int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "BACK_M",
	        UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	private TextImageButton exitButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.602f),
	        (int) (Main.WINDOW_WIDTH * 0.202), (int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "EXIT",
	        UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
}