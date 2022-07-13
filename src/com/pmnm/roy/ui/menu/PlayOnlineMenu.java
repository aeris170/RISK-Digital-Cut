package com.pmnm.roy.ui.menu;

import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.graphics.DoaGraphicsContext;
import doa.engine.graphics.DoaSprites;
import doa.engine.ui.panel.DoaUIPanel;

public class PlayOnlineMenu extends DoaUIPanel {

	public PlayOnlineMenu() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		hostGameButton.addAction(() -> {
			hide();
			UIConstants.hgm.show();
		});
		joinGameButton.addAction(() -> {
			hide();
			UIConstants.jgm.show();
		});
		backButton.addAction(() -> {
			hide();
			UIConstants.mm.show();
		});
		add(hostGameButton);
		add(joinGameButton);
		add(backButton);
		hide();
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {}

	private static final long serialVersionUID = 1834343492692029824L;

	private static final TextImageButton hostGameButton = Builders.TIBB.args(new DoaVectorF(1377f, 511f), UIInit.UIConstants.x, UIInit.UIConstants.y,
	        DoaSprites.get(UIConstants.BUTTON_IDLE_SPRITE), DoaSprites.get(UIConstants.BUTTON_HOVER_SPRITE), "HOST_GAME", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR).instantiate();
	private static final TextImageButton joinGameButton = Builders.TIBB.args(new DoaVectorF(1377f, 584f), UIInit.UIConstants.x, UIInit.UIConstants.y,
	        DoaSprites.get(UIConstants.BUTTON_IDLE_SPRITE), DoaSprites.get(UIConstants.BUTTON_HOVER_SPRITE), "JOIN_GAME", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR).instantiate();
	private static final TextImageButton backButton = Builders.TIBB.args(new DoaVectorF(1377f, 803f), UIInit.UIConstants.x, UIInit.UIConstants.y,
	        DoaSprites.get(UIConstants.BUTTON_IDLE_SPRITE), DoaSprites.get(UIConstants.BUTTON_HOVER_SPRITE), "BACK", UIConstants.FONT_COLOR, UIConstants.HOVER_FONT_COLOR).instantiate();
}