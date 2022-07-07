package com.pmnm.roy.ui.menu;

import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIInit;

import doa.engine.graphics.DoaGraphicsContext;
import doa.engine.graphics.DoaSprites;
import doa.engine.ui.panel.DoaUIPanel;

public class PlayOnlineMenu extends DoaUIPanel {

	public PlayOnlineMenu() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		hostGameButton.addAction(() -> {
			hide();
			UIInit.hgm.show();
		});
		joinGameButton.addAction(() -> {
			hide();
			UIInit.jgm.show();
		});
		backButton.addAction(() -> {
			hide();
			UIInit.mm.show();
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

	private static final TextImageButton hostGameButton = Builders.TIBB.args(new DoaVectorF(1377f, 511f), UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
	        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "HOST_GAME", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
	private static final TextImageButton joinGameButton = Builders.TIBB.args(new DoaVectorF(1377f, 584f), UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
	        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "JOIN_GAME", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
	private static final TextImageButton backButton = Builders.TIBB.args(new DoaVectorF(1377f, 803f), UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
	        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "BACK", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
}