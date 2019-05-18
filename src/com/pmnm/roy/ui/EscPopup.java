package com.pmnm.roy.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.io.IOException;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.input.DoaKeyboard;
import com.doa.engine.task.DoaTaskGuard;
import com.doa.engine.task.DoaTasker;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.doa.utils.DoaUtils;
import com.pmnm.risk.globals.localization.Translator;
import com.pmnm.risk.main.GameInstance;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.actions.ExitButtonAction;
import com.pmnm.roy.ui.actions.RulesButtonAction;
import com.pmnm.roy.ui.actions.SettingsButtonAction;
import com.pmnm.roy.ui.actions.LoadButtonAction;

public final class EscPopup extends DoaPanel {

	private static final long serialVersionUID = 8401721289376342254L;

	private DoaVectorF bounds = new DoaVectorF(Main.WINDOW_WIDTH * 0.300f, Main.WINDOW_HEIGHT * 0.036f);

	// pos width height
	TextImageButton saveButton = DoaHandler.instantiate(TextImageButton.class,
			new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.342f), (int) (Main.WINDOW_WIDTH * 0.202),
			(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"),
			"SAVE_GAME", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	TextImageButton loadButtonPop = DoaHandler.instantiate(TextImageButton.class,
			new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.407f), (int) (Main.WINDOW_WIDTH * 0.202),
			(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"),
			"LOAD_GAME", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	TextImageButton rulesButtonPop = DoaHandler.instantiate(TextImageButton.class,
			new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.472f), (int) (Main.WINDOW_WIDTH * 0.202),
			(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "RULES",
			UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	TextImageButton settingsButtonPop = DoaHandler.instantiate(TextImageButton.class,
			new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.537f), (int) (Main.WINDOW_WIDTH * 0.202),
			(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"),
			"SETTINGS", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);
	TextImageButton exitButtonPop = DoaHandler.instantiate(TextImageButton.class,
			new DoaVectorF(Main.WINDOW_WIDTH * 0.400f, Main.WINDOW_HEIGHT * 0.602f), (int) (Main.WINDOW_WIDTH * 0.202),
			(int) (Main.WINDOW_HEIGHT * 0.056f), DoaSprites.get("ButtonIdle"), DoaSprites.get("ButtonHover"), "EXIT",
			UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR, true);

	private boolean hidden = true;
	DoaTaskGuard escGuard = new DoaTaskGuard();

	public EscPopup(MainMenu mm, SettingsMenu sm, RulesMenu rm, LoadMenu lm, ExitPopup ep, PlayOfflineMenu pom) {
		super(Main.WINDOW_WIDTH * 0.376f, Main.WINDOW_HEIGHT * 0.291f, (int) (Main.WINDOW_WIDTH * 0.240f),
				(int) (Main.WINDOW_HEIGHT * 0.419f));

		exitButtonPop.addAction(new ExitButtonAction(ep));
		settingsButtonPop.addAction(new SettingsButtonAction(mm, sm, ep));
		rulesButtonPop.addAction(new RulesButtonAction(mm, rm, ep));
		loadButtonPop.addAction(new LoadButtonAction(mm, lm, ep, pom));
		saveButton.addAction(() -> {
			GameInstance.saveGame();
		});
		add(exitButtonPop);
		add(settingsButtonPop);
		add(rulesButtonPop);
		add(loadButtonPop);
		add(saveButton);
		//show();
		new Thread(() -> {
			while (true) {
				if (DoaKeyboard.ESCAPE) {
					hidden = !hidden;
					if (hidden) {
						hide();
					} else {
						show();
					}
					GameManager.INSTANCE.isPaused = !hidden;
					DoaUtils.sleepFor(190);
				}
				DoaUtils.sleepFor(10);
			}
		}).start();
	}

	@Override
	public void render(DoaGraphicsContext g) {
		if (!hidden) {
			Composite oldComposite = g.getComposite();
			g.setColor(Color.darkGray);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
			g.fillRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
			g.setComposite(oldComposite);

			g.drawImage(DoaSprites.get("escapeMenu"), position.x, position.y, width, height);

			exitButtonPop.render(g);
			settingsButtonPop.render(g);
			rulesButtonPop.render(g);
			loadButtonPop.render(g);
			saveButton.render(g);
		}
	}

	@Override
	public void show() {
		super.show();
		hidden = false;
	}

	@Override
	public void hide() {
		super.hide();
		hidden = true;
	}
}