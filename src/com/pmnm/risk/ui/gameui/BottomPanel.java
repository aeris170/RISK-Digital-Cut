package com.pmnm.risk.ui.gameui;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.Main;

public class BottomPanel extends DoaPanel {

	private static final long serialVersionUID = -59674351675589726L;

	private static final DoaSprite MIDDLE = DoaSprites.get("gaugeBig");
	private static final DoaSprite LEFT = DoaSprites.get("gaugeLeft");
	private static final DoaSprite RIGHT = DoaSprites.get("gaugeRight");

	public BottomPanel() {
		super(0f, 0f, 0, 0);
		show();
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0, Main.WINDOW_HEIGHT - DoaSprites.get("MainMenuBottomRing").getHeight() + 6);

		g.drawImage(LEFT, Main.WINDOW_WIDTH * 0.304f, Main.WINDOW_HEIGHT - LEFT.getHeight());
		g.drawImage(RIGHT, Main.WINDOW_WIDTH * 0.577f, Main.WINDOW_HEIGHT - RIGHT.getHeight());

		g.drawImage(MIDDLE, (Main.WINDOW_WIDTH - MIDDLE.getWidth()) / 2f, Main.WINDOW_HEIGHT - MIDDLE.getHeight());

		// g.drawImage(DoaSprites.get("gaugeBig"), (width -
		// DoaSprites.get("gaugeRight").getWidth()) / 2f, height -
		// DoaSprites.get("gaugeBig").getHeight());
	}
}
