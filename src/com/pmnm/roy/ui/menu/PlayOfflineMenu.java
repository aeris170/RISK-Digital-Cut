package com.pmnm.roy.ui.menu;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.globals.Builders;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.TextImageButton;
import com.pmnm.roy.ui.UIInit;

public class PlayOfflineMenu extends DoaPanel {

	private static final long serialVersionUID = -8533259801260650400L;

	private static final TextImageButton newGameButton = Builders.TIBB.args(new DoaVectorF(1377f, 511f), UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
	        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "NEW_GAME", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
	private static final TextImageButton loadGameButton = Builders.TIBB.args(new DoaVectorF(1377f, 584f), UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
	        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "LOAD_GAME", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();
	private static final TextImageButton backButton = Builders.TIBB.args(new DoaVectorF(1377f, 803f), UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
	        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "BACK", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR).instantiate();

	public PlayOfflineMenu() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		newGameButton.addAction(() -> {
			hide();
			UIInit.ngm.show();
		});
		loadGameButton.addAction(() -> {
			hide();
			UIInit.lgm.show();
		});
		backButton.addAction(() -> {
			hide();
			UIInit.mm.show();
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