package com.pmnm.roy.ui.menu;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaAnimations;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIInit;

public class MainMenu extends DoaPanel {

	private static final long serialVersionUID = -7825162499964842632L;

	private static final DoaVectorF PLAY_OFFLINE_LOCATION = new DoaVectorF(1377f, 511f);
	private static final DoaVectorF PLAY_ONLINE_LOCATION = new DoaVectorF(1377f, 584f);
	private static final DoaVectorF SETTING_LOCATION = new DoaVectorF(1377f, 657f);
	private static final DoaVectorF RULES_LOCATION = new DoaVectorF(1377f, 730f);
	private static final DoaVectorF EXIT_LOCATION = new DoaVectorF(1377f, 803f);

	public MainMenu() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		TextImageButton playOfflineButton = DoaHandler.instantiate(TextImageButton.class, PLAY_OFFLINE_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
		        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "PLAY_OFFLINE", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);
		TextImageButton playOnlineButton = DoaHandler.instantiate(TextImageButton.class, PLAY_ONLINE_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
		        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "PLAY_ONLINE", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);
		TextImageButton settingsButton = DoaHandler.instantiate(TextImageButton.class, SETTING_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
		        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "SETTINGS", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);
		TextImageButton rulesButton = DoaHandler.instantiate(TextImageButton.class, RULES_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
		        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "RULES", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);
		TextImageButton exitButton = DoaHandler.instantiate(TextImageButton.class, EXIT_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
		        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "EXIT", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);
		playOfflineButton.addAction(() -> {
			hide();
			UIInit.pofm.show();
		});
		playOnlineButton.addAction(() -> {
			hide();
			UIInit.ponm.show();
		});
		settingsButton.addAction(() -> {
			hide();
			UIInit.sm.show();
		});
		rulesButton.addAction(() -> {
			hide();
			UIInit.rm.show();
		});
		exitButton.addAction(() -> UIInit.ep.show());
		add(playOfflineButton);
		add(playOnlineButton);
		add(settingsButton);
		add(rulesButton);
		add(exitButton);
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawAnimation(DoaAnimations.get("RiskLogoAnim"), 1286, 220);
	}
}