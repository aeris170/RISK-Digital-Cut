package com.pmnm.risk.ui;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.Main;

public class MainMenu extends DoaPanel {

	private static final long serialVersionUID = -7825162499964842632L;

	private static final String FLEUR_DE_LIS = "FleurDeLis";

	private static final int FLEUR_WIDTH = DoaSprites.get(FLEUR_DE_LIS).getWidth() / 6;
	private static final int FLEUR_HEIGHT = DoaSprites.get(FLEUR_DE_LIS).getHeight() / 6;

	public MainMenu() {
		super(0f, 0f, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
	}

	@Override
	public void tick() {}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("MainMenuBackground"), position.x, position.y, width, height);
		for (int i = 0; i < Main.WINDOW_WIDTH; i += FLEUR_WIDTH) {
			g.drawImage(DoaSprites.get(FLEUR_DE_LIS), i, 0, FLEUR_WIDTH, FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(FLEUR_DE_LIS), i, FLEUR_HEIGHT, FLEUR_WIDTH, FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(FLEUR_DE_LIS), i, Main.WINDOW_HEIGHT - FLEUR_HEIGHT * 2d, FLEUR_WIDTH, FLEUR_HEIGHT);
			g.drawImage(DoaSprites.get(FLEUR_DE_LIS), i, (double) Main.WINDOW_HEIGHT - FLEUR_HEIGHT, FLEUR_WIDTH, FLEUR_HEIGHT);
		}
		g.drawImage(DoaSprites.get("MainMenuTopRing"), 0, FLEUR_HEIGHT * 1.5d);
		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0, Main.WINDOW_HEIGHT - FLEUR_HEIGHT * 1.5d - DoaSprites.get("MainMenuTopRing").getHeight());
		g.drawImage(DoaSprites.get("RiskLogo"), 1286, 279);
	}
}
