package com.pmnm.risk.ui.gameui;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;

import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.engine.task.DoaTaskGuard;
import com.doa.engine.task.DoaTasker;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.main.GameManager;
import com.pmnm.risk.main.Main;
import com.pmnm.risk.ui.UIInit;

public class TopPanel extends DoaPanel {

	private static final long serialVersionUID = -1014037154232695775L;

	private Season season = Season.getSeason();

	private DoaTaskGuard threeSecondGuard = new DoaTaskGuard();
	private float alpha;
	private float delta = 0.1f;

	public TopPanel() {
		super(0f, 0f, 0, 0);
		show();
	}

	@Override
	public void tick() {
		if (threeSecondGuard.get()) {
			threeSecondGuard.set(false);
			DoaTasker.executeLater(() -> {
				threeSecondGuard.set(true);
				if (delta > 0) {
					delta = -0.1f;
				} else {
					delta = 0.1f;
				}
			}, 4000);
			for (int i = 0; i < 10; i++) {
				DoaTasker.executeLater(() -> alpha += delta, 100 * i);
			}
		}
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(DoaSprites.get("MainMenuTopRing"), 0, -6);
		g.drawImage(DoaSprites.get("MainMenuBottomRing"), 0, 51);
		g.drawImage(DoaSprites.get("seasonCircle"), (Main.WINDOW_WIDTH - DoaSprites.get("seasonCircle").getWidth()) / 2f, 0);
		g.drawImage(DoaSprites.get(season.toString()), (Main.WINDOW_WIDTH - DoaSprites.get(season.toString()).getWidth()) / 2f, 0);
		g.setFont(UIInit.UI_FONT.deriveFont(Font.PLAIN, 26f));
		g.setColor(UIInit.FONT_COLOR);

		Composite oldComposite = g.getComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(alpha, 1)));
		String turn = "TURN: " + (GameManager.turnCount + 1);
		g.drawString(turn, (Main.WINDOW_WIDTH - g.getFontMetrics().stringWidth(turn)) / 2f, 110);

		g.setColor(GameManager.currentPlayer.getColor());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(1 - alpha, 0)));
		String player = GameManager.currentPlayer.getName();
		g.drawString(player, (Main.WINDOW_WIDTH - g.getFontMetrics().stringWidth(player)) / 2f, 110);
		g.setComposite(oldComposite);

	}
}