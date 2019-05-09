package com.pmnm.roy.ui;

import com.doa.engine.graphics.DoaAnimations;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.Main;

public class MainMenu extends DoaPanel {

	private static final long serialVersionUID = -7825162499964842632L;

	public MainMenu() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
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