package com.pmnm.risk.ui.gameui;

import java.awt.Font;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.ui.UIInit;

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

		g.drawImage(DoaSprites.get("garrisonHolder"), Main.WINDOW_WIDTH * 0.454f, Main.WINDOW_HEIGHT * 0.836f);
		g.drawImage(DoaSprites.get("garrisonHolderIcon"), Main.WINDOW_WIDTH * 0.527f, Main.WINDOW_HEIGHT * 0.842f);
		g.drawImage(DoaSprites.get("ownerHolder"), Main.WINDOW_WIDTH * 0.447f, Main.WINDOW_HEIGHT * 0.874f);
		g.drawImage(DoaSprites.get("ownerHolderIcon"), Main.WINDOW_WIDTH * 0.539f, Main.WINDOW_HEIGHT * 0.880f);
		g.drawImage(DoaSprites.get("provinceNameHolder"), Main.WINDOW_WIDTH * 0.436f, Main.WINDOW_HEIGHT * 0.912f);
		g.drawImage(DoaSprites.get("provinceNameHolderIcon"), Main.WINDOW_WIDTH * 0.552f, Main.WINDOW_HEIGHT * 0.918f);
		g.drawImage(DoaSprites.get("continentHolder"), Main.WINDOW_WIDTH * 0.430f, Main.WINDOW_HEIGHT * 0.950);
		g.drawImage(DoaSprites.get("continentHolderIcon"), Main.WINDOW_WIDTH * 0.559f, Main.WINDOW_HEIGHT * 0.953f);

		g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN, 25f));
		g.setColor(UIInit.FONT_COLOR);
		g.drawString("" + GameManager.clickedHitArea.getProvince().getTroops(), Main.WINDOW_WIDTH * 0.454f + DoaSprites.get("garrisonHolder").getWidth() / 2f
		        - g.getFontMetrics().stringWidth("" + GameManager.clickedHitArea.getProvince().getTroops()) / 2f, Main.WINDOW_HEIGHT * 0.858f);
		g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN, 15f));
		g.setColor(GameManager.clickedHitArea.getProvince().getOwner().getColor());
		g.drawString("" + GameManager.clickedHitArea.getProvince().getOwner().getName(), Main.WINDOW_WIDTH * 0.447f + DoaSprites.get("ownerHolder").getWidth() / 2f
		        - g.getFontMetrics().stringWidth("" + GameManager.clickedHitArea.getProvince().getOwner().getName()) / 2f, Main.WINDOW_HEIGHT * 0.843f + 60);
		g.setColor(UIInit.FONT_COLOR);
		g.drawString("" + GameManager.clickedHitArea.getProvince().getName().toUpperCase(),
		        Main.WINDOW_WIDTH * 0.436f + DoaSprites.get("provinceNameHolder").getWidth() / 2f
		                - g.getFontMetrics().stringWidth("" + GameManager.clickedHitArea.getProvince().getName().toUpperCase()) / 2f,
		        Main.WINDOW_HEIGHT * 0.845f + 100);
		g.drawString("" + GameManager.clickedHitArea.getProvince().getContinent().getName().toUpperCase(),
		        Main.WINDOW_WIDTH * 0.430f + DoaSprites.get("continentHolder").getWidth() / 2f
		                - g.getFontMetrics().stringWidth("" + GameManager.clickedHitArea.getProvince().getContinent().getName().toUpperCase()) / 2f,
		        Main.WINDOW_HEIGHT * 0.836f + 150);
	}
}
