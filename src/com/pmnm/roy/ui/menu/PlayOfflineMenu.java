package com.pmnm.roy.ui.menu;

import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.graphics.DoaGraphicsContext;
import doa.engine.graphics.DoaSprites;
import doa.engine.ui.panel.DoaUIPanel;

public class PlayOfflineMenu extends DoaUIPanel {

	private static final long serialVersionUID = -8533259801260650400L;

	private static final TextImageButton newGameButton = Builders.TIBB.args(new DoaVectorF(1377f, 511f), UIInit.UIConstants.x, UIInit.UIConstants.y,
	        DoaSprites.get(UIConstants.BUTTON_IDLE_SPRITE), DoaSprites.get(UIConstants.BUTTON_HOVER_SPRITE), "NEW_GAME", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR).instantiate();
	private static final TextImageButton loadGameButton = Builders.TIBB.args(new DoaVectorF(1377f, 584f), UIInit.UIConstants.x, UIInit.UIConstants.y,
	        DoaSprites.get(UIConstants.BUTTON_IDLE_SPRITE), DoaSprites.get(UIConstants.BUTTON_HOVER_SPRITE), "LOAD_GAME", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR).instantiate();
	private static final TextImageButton backButton = Builders.TIBB.args(new DoaVectorF(1377f, 803f), UIInit.UIConstants.x, UIInit.UIConstants.y,
	        DoaSprites.get(UIConstants.BUTTON_IDLE_SPRITE), DoaSprites.get(UIConstants.BUTTON_HOVER_SPRITE), "BACK", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR).instantiate();

	public PlayOfflineMenu() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		newGameButton.addAction(() -> {
			hide();
			UIConstants.ngm.show();
		});
		loadGameButton.addAction(() -> {
			hide();
			UIConstants.lgm.show();
		});
		backButton.addAction(() -> {
			hide();
			UIConstants.mm.show();
		});
		add(newGameButton);
		add(loadGameButton);
		add(backButton);
		hide();
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {}
}