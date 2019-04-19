package com.pmnm.risk.ui.gameui;

import java.awt.Font;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.ui.UIInit;

public class TopPanel extends DoaPanel {

	private static final long serialVersionUID = -1014037154232695775L;

	private Season season = Season.WINTER;

	public TopPanel() {
		super(0f, 0f, 0, 0);
		show();
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("MainMenuTopRing"), 0, -6);
		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0, 51);
		g.drawImage(DoaSprites.get("seasonCircle"), (Main.WINDOW_WIDTH - DoaSprites.get("seasonCircle").getWidth()) / 2f, 0);
		g.drawImage(DoaSprites.get(season.toString()), (Main.WINDOW_WIDTH - DoaSprites.get(season.toString()).getWidth()) / 2f, 0);
		g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN, 26f));
		g.setColor(UIInit.FONT_COLOR);
		String turn = "TURN: " + GameManager.turnCount;
		g.drawString(turn, (Main.WINDOW_WIDTH - g.getFontMetrics().stringWidth(turn)) / 2f, 110);
	}

}
