package com.pmnm.roy.ui.gameui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.image.BufferedImage;

import com.pmnm.risk.main.Main;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.scene.DoaObject;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import doa.engine.task.DoaTaskGuard;
import doa.engine.task.DoaTasker;

@SuppressWarnings("serial")
public class TopPanel extends DoaObject {

	public static TopPanel INSTANCE;

	private DoaTaskGuard threeSecondGuard = new DoaTaskGuard();
	private float alpha;
	private float delta = 0.1f;
	private Color timerColour;

	public TopPanel() {
		INSTANCE = this;
		addComponent(new Script());
		addComponent(new Renderer());
	}

	private final class Script extends DoaScript {

		@Override
		public void tick() {
			timerColour = Color.RED;	// GameManager.INSTANCE.currentPlayer.getColor();
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
					DoaTasker.executeLater(() -> alpha += delta, 100L * i);
				}
			}
			Season.updateSeason();
		}
		
	}
	
	private final class Renderer extends DoaRenderer {

		private transient BufferedImage topRing = DoaSprites.getSprite("MainMenuTopRing");
		private transient BufferedImage bottomRing = DoaSprites.getSprite("MainMenuBottomRing");
		private transient BufferedImage seasonCircle = DoaSprites.getSprite("seasonCircle");
		
		private float windowWidth = Main.WINDOW_WIDTH;
		private float windowHeight = Main.WINDOW_HEIGHT;
		
		@Override
		public void render() {
			//GameManager gm = GameManager.INSTANCE;
			// timer block
			if (timerColour != null) {
				float timer = 10f;	// GameManager.INSTANCE.timer;
				DoaGraphicsFunctions.setColor(Color.BLACK);
				DoaGraphicsFunctions.fillRect(0f, windowHeight * 0.027f, windowWidth, windowHeight * 0.021f);
				DoaGraphicsFunctions.setColor(timerColour);
				DoaGraphicsFunctions.fillRect(timer, windowHeight * 0.027f, windowWidth - timer * 2, windowHeight * 0.021f);
			}

			Composite oldComposite = DoaGraphicsFunctions.getComposite();
			DoaGraphicsFunctions.drawImage(topRing, 0, -6);
			DoaGraphicsFunctions.drawImage(bottomRing, 0, 51);
			DoaGraphicsFunctions.drawImage(seasonCircle, (windowWidth - seasonCircle.getWidth()) / 2f, 0);
			DoaGraphicsFunctions.drawImage(DoaSprites.getSprite(Season.getCurrentSeason().toString()), (windowWidth - DoaSprites.getSprite(Season.getCurrentSeason().toString()).getWidth()) / 2f, 0);
			DoaGraphicsFunctions.setFont(UIConstants.getFont().deriveFont(Font.PLAIN, 26f));
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());

			DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(alpha, 1)));
			String turn = "TURN: ???";	// "TURN: " + (int) Math.ceil((gm.turnCount + 1) / (double) gm.numberOfPlayers);
			DoaGraphicsFunctions.drawString(turn, (windowWidth - DoaGraphicsFunctions.getFontMetrics().stringWidth(turn)) / 2f, 110);

			//DoaGraphicsFunctions.setColor(gm.currentPlayer.getColor());
			DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(1 - alpha, 0)));
			String player = "PLAYER ???";	// gm.currentPlayer.getName();
			DoaGraphicsFunctions.drawString(player, (windowWidth - DoaGraphicsFunctions.getFontMetrics().stringWidth(player)) / 2f, 110);
			DoaGraphicsFunctions.setComposite(oldComposite);
		}
	}
}