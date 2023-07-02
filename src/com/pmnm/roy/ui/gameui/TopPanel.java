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
import com.pmnm.roy.ui.UIUtils;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaVector;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import doa.engine.task.DoaTaskGuard;
import doa.engine.task.DoaTasker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pmnm.risk.game.IPlayer;
import pmnm.risk.game.databasedimpl.RiskGameContext;

@SuppressWarnings("serial")
public class TopPanel extends RoyMenu {

	private DoaTaskGuard threeSecondGuard = new DoaTaskGuard();
	private float alpha;
	private float delta = 0.1f;

	private IPlayer currentPlayer;
	private String currentPlayerName;
	private Color currentPlayerColour;
	private BufferedImage currentSeasonImage;
	private int turnCount;

	private final RiskGameContext context;
	
	public TopPanel(final RiskGameContext context) {
		this.context = context;

		setzOrder(ZOrders.GAME_UI_Z);
		
		Renderer r = new Renderer();
		addComponent(new Script(r));
		addComponent(r);
	}

	@RequiredArgsConstructor
	private final class Script extends DoaScript {
		
		int counter = Globals.DEFAULT_TIME_SLICE;
		
		@NonNull private Renderer renderer;
		
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

			if (currentPlayer != context.getCurrentPlayer()) {
				currentPlayer = context.getCurrentPlayer();
				currentPlayerName = currentPlayer.getName();
				currentPlayerColour = currentPlayer.getColor();
				currentSeasonImage = DoaSprites.getSprite(Season.getCurrentSeason().toString());
				turnCount = (int) Math.ceil((context.getElapsedTurns() + 1) / (double) context.getNumberOfPlayers());
				renderer.turnFont = null;
				renderer.playerNameFont = null;
			}
			counter = 0;
		}
	}
	
	private final class Renderer extends DoaRenderer {

		private transient BufferedImage topRing = DoaSprites.getSprite("MainMenuTopRing");
		private transient BufferedImage bottomRing = DoaSprites.getSprite("MainMenuBottomRing");
		private transient BufferedImage seasonCircle = DoaSprites.getSprite("seasonCircle");

		private Font turnFont;
		private String turn;
		private int turnTextWidth;
		private int turnTextHeight;
		
		private Font playerNameFont;
		private String playerName;
		private int playerNameTextWidth;
		private int playerNameTextHeight;
		
		@Override
		public void render() {
			if (!isVisible()) return;
			if (turnFont == null) {
				turn = "TURN: " + turnCount;
				int[] size = DoaGraphicsFunctions.warp(seasonCircle.getWidth() * .70f, seasonCircle.getHeight() * .70f);
				DoaVector contentSize = new DoaVector(size[0], size[1]);
				turnFont = UIUtils.adjustFontToFitInArea(turn, contentSize);
				turnTextWidth = DoaGraphicsFunctions.unwarpX(UIUtils.textWidth(turnFont, turn));
				turnTextHeight = DoaGraphicsFunctions.unwarpY(UIUtils.textHeight(turnFont));
			}
			if (playerNameFont == null) {
				playerName = currentPlayerName;
				int[] size = DoaGraphicsFunctions.warp(seasonCircle.getWidth()* .70f, seasonCircle.getHeight() * .70f);
				DoaVector contentSize = new DoaVector(size[0], size[1]);
				playerNameFont = UIUtils.adjustFontToFitInArea(playerName, contentSize);
				playerNameTextWidth = DoaGraphicsFunctions.unwarpX(UIUtils.textWidth(playerNameFont, playerName));
				playerNameTextHeight = DoaGraphicsFunctions.unwarpY(UIUtils.textHeight(playerNameFont));
			}
			playerName = "DOA";
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
			DoaGraphicsFunctions.setColor(UIConstants.getTextColor());
			DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(alpha, 1)));
			DoaGraphicsFunctions.setFont(turnFont);
			DoaGraphicsFunctions.drawString(
				turn,
				(1920 - turnTextWidth) / 2f,
				110
			);

			DoaGraphicsFunctions.setColor(currentPlayerColour);
			DoaGraphicsFunctions.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(1 - alpha, 0)));
			DoaGraphicsFunctions.setFont(playerNameFont);
			DoaGraphicsFunctions.drawString(currentPlayerName,
				(1920 - playerNameTextWidth) / 2f,
				110
			);
			DoaGraphicsFunctions.setComposite(oldComposite);
		}
	}
}