package com.pmnm.roy.ui.gameui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.image.BufferedImage;

import com.pmnm.risk.globals.Globals;
import com.pmnm.risk.globals.ZOrders;
import com.pmnm.risk.main.Main;
import com.pmnm.roy.RoyMenu;
import com.pmnm.roy.ui.UIConstants;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import doa.engine.task.DoaTaskGuard;
import doa.engine.task.DoaTasker;
import pmnm.risk.game.databasedimpl.RiskGameContext;

@SuppressWarnings("serial")
public class TopPanel extends RoyMenu {

	private DoaTaskGuard threeSecondGuard = new DoaTaskGuard();
	private float alpha;
	private float delta = 0.1f;

	private String currentPlayerName;
	private Color currentPlayerColour;
	private BufferedImage currentSeasonImage;
	private int turnCount;

	private final RiskGameContext context;
	
	public TopPanel(final RiskGameContext context) {
		this.context = context;

		setzOrder(ZOrders.GAME_UI_Z);
		
		addComponent(new Script());
		addComponent(new Renderer());
	}

	private final class Script extends DoaScript {
		
		int counter = Globals.DEFAULT_TIME_SLICE;
		
		@Override
		public void tick() {
			if(!isVisible()) return;
			
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
			
			if(counter < Globals.DEFAULT_TIME_SLICE) {
				counter++;
				return;
			}

			currentPlayerName = context.getCurrentPlayer().getName();
			currentPlayerColour = context.getCurrentPlayer().getColor();
			currentSeasonImage = DoaSprites.getSprite(Season.getCurrentSeason().toString());
			turnCount = (int) Math.ceil((context.getElapsedTurns() + 1) / (double) context.getNumberOfPlayers());
			
			counter = 0;
		}
		
	}
	
	private final class Renderer extends DoaRenderer {

		private transient BufferedImage topRing = DoaSprites.getSprite("MainMenuTopRing");
		private transient BufferedImage bottomRing = DoaSprites.getSprite("MainMenuBottomRing");
		private transient BufferedImage seasonCircle = DoaSprites.getSprite("seasonCircle");

		@Override
		public void render() {
			if(!isVisible()) return;
			
			// timer block
			if (currentPlayerColour != null) {
				float timer = 0.0f;
				DoaGraphicsFunctions.setColor(Color.BLACK);
				DoaGraphicsFunctions.fillRect(0f, 1080 * 0.027f, 1920, 1080 * 0.021f);
				DoaGraphicsFunctions.setColor(currentPlayerColour);
				DoaGraphicsFunctions.fillRect(timer, 1080 * 0.027f, 1920 - timer * 2, 1080 * 0.021f);
			}

			Composite oldComposite = DoaGraphicsFunctions.getComposite();
			DoaGraphicsFunctions.drawImage(topRing,
				0, -6,
				topRing.getWidth(), topRing.getHeight()
			);
			DoaGraphicsFunctions.drawImage(bottomRing,
				0, 51,
				bottomRing.getWidth(), bottomRing.getHeight()
			);
			DoaGraphicsFunctions.drawImage(seasonCircle,
				(1920 - seasonCircle.getWidth()) / 2f, 0,
				seasonCircle.getWidth(), seasonCircle.getHeight()
			);
			DoaGraphicsFunctions.drawImage(currentSeasonImage,
				(1920 - currentSeasonImage.getWidth()) / 2f, 0,
				currentSeasonImage.getWidth(), currentSeasonImage.getHeight()
			);
			DoaGraphicsFunctions.setFont(UIConstants.getFont().deriveFont(Font.PLAIN, 26f));
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());

			DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(alpha, 1)));
			String turn = "TURN: " + turnCount;
			DoaGraphicsFunctions.drawString("TURN: " + turnCount, (1920 - DoaGraphicsFunctions.getFontMetrics().stringWidth(turn)) / 2f, 110);

			DoaGraphicsFunctions.setColor(currentPlayerColour);
			DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(1 - alpha, 0)));
			
			DoaGraphicsFunctions.drawString(currentPlayerName, (1920 - DoaGraphicsFunctions.getFontMetrics().stringWidth(currentPlayerName)) / 2f, 110);
			DoaGraphicsFunctions.setComposite(oldComposite);
		}
	}
}