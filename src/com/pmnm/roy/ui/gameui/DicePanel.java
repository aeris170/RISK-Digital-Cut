package com.pmnm.roy.ui.gameui;

import java.awt.Rectangle;
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
import pmnm.risk.map.board.ProvinceHitArea;
import pmnm.risk.map.board.ProvinceHitAreas;

@SuppressWarnings("serial")
public class DicePanel extends RoyMenu {

	private DoaVector panelPosition = new DoaVector(-160f, 258f);
	
	private DoaVector ONE_POSITION = new DoaVector(51f, 109f);
	private DoaVector TWO_POSITION = new DoaVector(24f, 183f);
	private DoaVector THREE_POSITION = new DoaVector(24f, 266f);
	private DoaVector BLITZ_POSITION = new DoaVector(37f, 305f);

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
			.disabledImage(DoaSprites.getSprite("dice1Disabled"))
			.action(source -> doConflict(Dice.ATTACK_DICE_1))
			.build();
		one.setPosition(DoaVector.add(panelPosition, ONE_POSITION));
		addElement(one);
		
		two = RoyImageButton.builder()
			.image(DoaSprites.getSprite("dice2Idle"))
			.hoverImage(DoaSprites.getSprite("dice2Hover"))
			.pressImage(DoaSprites.getSprite("dice2Hover"))
			.disabledImage(DoaSprites.getSprite("dice2Disabled"))
			.action(source -> doConflict(Dice.ATTACK_DICE_2))
			.build();
		two.setPosition(DoaVector.add(panelPosition, TWO_POSITION));
		addElement(two);
		
		three = RoyImageButton.builder()
			.image(DoaSprites.getSprite("dice3Idle"))
			.hoverImage(DoaSprites.getSprite("dice3Hover"))
			.pressImage(DoaSprites.getSprite("dice3Hover"))
			.disabledImage(DoaSprites.getSprite("dice3Disabled"))
			.action(source -> doConflict(Dice.ATTACK_DICE_3))
			.build();
		three.setPosition(DoaVector.add(panelPosition, THREE_POSITION));
		addElement(three);
		
		blitz = RoyImageButton.builder()
			.image(DoaSprites.getSprite("blitzIdle"))
			.hoverImage(DoaSprites.getSprite("blitzHover"))
			.pressImage(DoaSprites.getSprite("blitzHover"))
			.disabledImage(DoaSprites.getSprite("blitzDisabled"))
			.action(source -> {})
			.build();
		blitz.setPosition(DoaVector.add(panelPosition, BLITZ_POSITION));
		blitz.setInteractionArea(new Rectangle(
			blitz.getContentArea().getBounds().x + 14,
			blitz.getContentArea().getBounds().y + 85,
			64,
			64
		));
		addElement(blitz);
		
		setzOrder(ZOrders.GAME_UI_Z);
		
		addComponent(new Script());
		addComponent(new Renderer());
	}
	
	private void doConflict(Dice dice) {
		ProvinceHitAreas areas = context.getAreas();
		ProvinceHitArea attackerArea = areas.getAttackerProvince();
		ProvinceHitArea defenderArea = areas.getDefenderProvince();
		if (attackerArea == null || defenderArea == null) { return; }
		Conflict conflict = context.setUpConflict(
			attackerArea.getProvince(),
			defenderArea.getProvince(),
			Dice.ATTACK_DICE_1);
		Conflict.Result result = conflict.calculateResult();
		context.applyConflictResult(result);
	}

	private final class Script extends DoaScript {

		private final float MIN = -160f;
		private final float MAX = 0f;
		
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
			} else {
				hide();
			}

			if (moving) {
				if (velocity > 0) {
					velocity += ACCELERATION;
				} else if (velocity < 0) {
					velocity -= ACCELERATION;
				}
				if (panelPosition.x > MAX) {
					panelPosition.x = MAX;
					velocity = 0;
					moving = false;
				}
				if (panelPosition.x < MIN) {
					panelPosition.x = MIN;
					velocity = 0;
					moving = false;
				}

				panelPosition.x += velocity;
				one.setPosition(new DoaVector(panelPosition.x + ONE_POSITION.x, panelPosition.y + ONE_POSITION.y));
				two.setPosition(new DoaVector(panelPosition.x + TWO_POSITION.x, panelPosition.y + TWO_POSITION.y));
				three.setPosition(new DoaVector(panelPosition.x + THREE_POSITION.x, panelPosition.y + THREE_POSITION.y));
				blitz.setPosition(new DoaVector(panelPosition.x + BLITZ_POSITION.x, panelPosition.y + BLITZ_POSITION.y));
			}
		}
		
		private void show() {
			if (moving) { return; }
			
			boolean canAttack = context.getAreas().getAttackerProvince().getProvince().canLaunchAttack();
			int numOfTroops = context.getAreas().getAttackerProvince().getProvince().getNumberOfTroops();
			one.setEnabled(canAttack);
			two.setEnabled(canAttack && numOfTroops > 2);
			three.setEnabled(canAttack && numOfTroops > 3);
			blitz.setEnabled(canAttack);
			
			if (panelPosition.x != MAX) {
				moving = true;
				velocity = Float.MIN_VALUE;
			}
		}
		
		private void hide() {
			if (moving) { return; }
			
			one.setEnabled(false);
			two.setEnabled(false);
			three.setEnabled(false);
			blitz.setEnabled(false);
			
			if (panelPosition.x != MIN) {
				moving = true;
				velocity = -Float.MIN_VALUE;
			}
		}
	}

	private final class Renderer extends DoaRenderer {

		private final BufferedImage BG = DoaSprites.getSprite("diceScroll");
		
		@Override
		public void render() {
			if (!isVisible()) return;
			
			DoaGraphicsFunctions.drawImage(BG, panelPosition.x, panelPosition.y, BG.getWidth(), BG.getHeight());
		}
	}
}