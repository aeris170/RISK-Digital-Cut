package com.pmnm.roy.ui;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaAnimations;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.Main;

public class PlayOfflineMenu extends DoaPanel {

	private static final long serialVersionUID = -8533259801260650400L;
	
	TextImageButton newGameButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(1377f, 511f), UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
	        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "NEW_GAME", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);
	TextImageButton loadGameButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(1377f, 584f), UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y,
	        DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE), DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "LOAD_GAME", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);
	TextImageButton backButton = DoaHandler.instantiate(TextImageButton.class, new DoaVectorF(1377f, 803f), UIInit.BUTTON_SIZE.x, UIInit.BUTTON_SIZE.y, DoaSprites.get(UIInit.BUTTON_IDLE_SPRITE),
	        DoaSprites.get(UIInit.BUTTON_HOVER_SPRITE), "BACK", UIInit.FONT_COLOR, UIInit.HOVER_FONT_COLOR);
	
	MainMenu mm;
	SinglePlayerMenu spm;
	
	public PlayOfflineMenu(MainMenu mm, SinglePlayerMenu spm) {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
		
		this.mm = mm;
		this.spm = spm;
		
		newGameButton.addAction(() -> {
			hide();
			mm.hide();
			spm.show();
		});
		
		backButton.addAction(() -> {
			hide();
			spm.hide();
			mm.show();
		});
		
		
		add(newGameButton);
		add(loadGameButton);
		add(backButton);
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("MainMenuBackground"), position.x, position.y, width, height);
		for (int i = 0; i < Main.WINDOW_WIDTH; i += UIInit.FLEUR_WIDTH) {
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, 0, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, UIInit.FLEUR_HEIGHT, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 2d, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(UIInit.FLEUR_DE_LIS), i, (double) Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT, UIInit.FLEUR_WIDTH, UIInit.FLEUR_HEIGHT);
		}
		g.drawImage(DoaSprites.get("MainMenuTopRing"), 0, UIInit.FLEUR_HEIGHT * 1.5d);
		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0, Main.WINDOW_HEIGHT - UIInit.FLEUR_HEIGHT * 1.5d - DoaSprites.get("MainMenuTopRing").getHeight());
		g.drawAnimation(DoaAnimations.get("RiskLogoAnim"), 1286, 220);

	}

}
