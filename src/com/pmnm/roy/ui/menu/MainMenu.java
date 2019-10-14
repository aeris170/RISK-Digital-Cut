package com.pmnm.roy.ui.menu;

import com.doa.engine.graphics.DoaAnimation;
import com.doa.engine.graphics.DoaAnimations;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.scene.DoaScene;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.globals.Scenes;
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
		DoaScene f = Scenes.MENU_SCENE;
		System.out.print(f);
		TextImageButton playOfflineButton = Builders.TIBB.args(PLAY_OFFLINE_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
		        DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "PLAY_OFFLINE", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
		TextImageButton playOnlineButton = Builders.TIBB.args(PLAY_ONLINE_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
		        DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "PLAY_ONLINE", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
		TextImageButton settingsButton = Builders.TIBB.args(SETTING_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
		        DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "SETTINGS", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
		TextImageButton rulesButton = Builders.TIBB.args(RULES_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
		        DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "RULES", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
		TextImageButton exitButton = Builders.TIBB.args(EXIT_LOCATION, UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
		        DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "EXIT", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
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
		DoaAnimation riskLogoAnim = DoaAnimations.get("RiskLogoAnim");
		g.drawAnimation(riskLogoAnim, Main.WINDOW_WIDTH * 0.66979f, Main.WINDOW_HEIGHT * 0.20370f, riskLogoAnim.current().getWidth() * Main.WINDOW_WIDTH / 1920f,
		        riskLogoAnim.current().getHeight() * Main.WINDOW_HEIGHT / 1080f);
	}
}