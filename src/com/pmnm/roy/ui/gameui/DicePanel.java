package com.pmnm.roy.ui.gameui;

import java.awt.image.BufferedImage;

import com.pmnm.risk.globals.ZOrders;
import com.pmnm.roy.RoyImageButton;
import com.pmnm.roy.RoyMenu;

import doa.engine.core.DoaGraphicsFunctions;
import doa.engine.graphics.DoaSprites;
import doa.engine.maths.DoaVector;
import doa.engine.scene.elements.renderers.DoaRenderer;
import doa.engine.scene.elements.scripts.DoaScript;
import pmnm.risk.game.Conflict;
import pmnm.risk.game.Dice;
import pmnm.risk.game.IRiskGameContext.TurnPhase;
import pmnm.risk.game.databasedimpl.RiskGameContext;

@SuppressWarnings("serial")
public class DicePanel extends RoyMenu {

	private DoaVector panelPosition	= new DoaVector(-160f, 258f);
	
	private final DoaVector ONE_POSITION = new DoaVector(-111f, 367f);
	private final DoaVector TWO_POSITION = new DoaVector(-138f, 441f);
	private final DoaVector THREE_POSITION = new DoaVector(-139f, 524f);
	private final DoaVector BLITZ_POSITION = new DoaVector(-130f, 563f);

	private RoyImageButton one;
	private RoyImageButton two;
	private RoyImageButton three;
	private RoyImageButton blitz;
	
	private final RiskGameContext context;

	public DicePanel(final RiskGameContext context) {
		this.context = context;
		
		one = RoyImageButton.builder()
			.image(DoaSprites.getSprite("dice1Idle"))
			.hoverImage(DoaSprites.getSprite("dice1Hover"))
			.pressImage(DoaSprites.getSprite("dice1Hover"))
			.disabledImage(DoaSprites.getSprite("dice1Hover"))
			.action(source -> {
				Conflict conflict = context.setUpConflict(
					context.getAreas().getAttackerProvince().getProvince(),
					context.getAreas().getDefenderProvince().getProvince(),
					Dice.ATTACK_DICE_1);
				context.applyConflictResult(conflict.calculateResult());
			})
			.build();
		one.setPosition(ONE_POSITION);
		addElement(one);
		
		two = RoyImageButton.builder()
			.image(DoaSprites.getSprite("dice2Idle"))
			.hoverImage(DoaSprites.getSprite("dice2Hover"))
			.pressImage(DoaSprites.getSprite("dice2Hover"))
			.disabledImage(DoaSprites.getSprite("dice2Hover"))
			.action(source -> {
				Conflict conflict = context.setUpConflict(
					context.getAreas().getAttackerProvince().getProvince(),
					context.getAreas().getDefenderProvince().getProvince(),
					Dice.ATTACK_DICE_2);
				context.applyConflictResult(conflict.calculateResult());
			})
			.build();
		two.setPosition(TWO_POSITION);
		addElement(two);
		
		three = RoyImageButton.builder()
			.image(DoaSprites.getSprite("dice3Idle"))
			.hoverImage(DoaSprites.getSprite("dice3Hover"))
			.pressImage(DoaSprites.getSprite("dice3Hover"))
			.disabledImage(DoaSprites.getSprite("dice3Hover"))
			.action(source -> {
				Conflict conflict = context.setUpConflict(
					context.getAreas().getAttackerProvince().getProvince(),
					context.getAreas().getDefenderProvince().getProvince(),
					Dice.ATTACK_DICE_3);
				context.applyConflictResult(conflict.calculateResult());
			})
			.build();
		three.setPosition(THREE_POSITION);
		addElement(three);
		
		blitz = RoyImageButton.builder()
			.image(DoaSprites.getSprite("blitzIdle"))
			.hoverImage(DoaSprites.getSprite("blitzHover"))
			.pressImage(DoaSprites.getSprite("blitzHover"))
			.disabledImage(DoaSprites.getSprite("blitzHover"))
			.action(source -> {
				
			})
			.build();
		blitz.setPosition(BLITZ_POSITION);
		addElement(blitz);
		
		setzOrder(ZOrders.GAME_UI_Z);
		
		addComponent(new Script());
		addComponent(new Renderer());
	}

	private final class Script extends DoaScript {

		private final DoaVector MIN = new DoaVector(-160f, 258f);
		private final DoaVector MAX = new DoaVector(0f, 823f);
		
		private final float ACCELERATION = 0.064f;
		
		private float velocity = 0f;
		private boolean moving = false;
		
		@Override
		public void tick() {
			if (!isVisible()) return;

			if (context.getCurrentPhase() == TurnPhase.ATTACK
				&& context.getAreas().getAttackerProvince() != null
				&& context.getAreas().getDefenderProvince() != null) {
					show();
			}  else {
				hide();
			}
			
			if (moving) {
				if (velocity > 0) {
					velocity += ACCELERATION;
				} else if (velocity < 0) {
					velocity -= ACCELERATION;
				}
				if (panelPosition.x > MAX.x) {
					panelPosition.x = MAX.x;
					velocity = 0;
					moving = false;
				}
				if (panelPosition.x < MIN.x) {
					panelPosition.x = MIN.x;
					velocity = 0;
					moving = false;
				}
				
				panelPosition.x += velocity;
				setNewPosition(one);
				setNewPosition(two);
				setNewPosition(three);
				setNewPosition(blitz);
			}
		}
		
		private void show() {
			if (panelPosition.x != MAX.x) {
				moving = true;
				velocity = 1;
			}
		}
		
		private void hide() {
			if (panelPosition.x != MIN.x) {
				moving = true;
				velocity = -1;
			}
		}
		
		private void setNewPosition(RoyImageButton button) {
			DoaVector v = new DoaVector(button.getContentArea().x + velocity, button.getContentArea().y);
			button.setPosition(v);
		}
	}

	private final class Renderer extends DoaRenderer {

		private final BufferedImage BG = DoaSprites.getSprite("diceScroll");
		
		@Override
		public void render() {
			if (!isVisible()) return;
			
			DoaGraphicsFunctions.drawImage(BG, panelPosition.x, panelPosition.y);
		}
	}
}