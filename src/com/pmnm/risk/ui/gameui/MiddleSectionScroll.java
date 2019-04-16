package com.pmnm.risk.ui.gameui;

import com.doa.engine.DoaObject;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;

public class MiddleSectionScroll extends DoaObject {

	private static final long serialVersionUID = -3397313460439190782L;

	private static final float ACCELERATION = 0.064f;

	private static final DoaVectorF MIN = new DoaVectorF(616f, 489f);
	private static final DoaVectorF MAX = new DoaVectorF(616f, 929f);

	private static final DoaSprite TEXTURE = DoaSprites.get("scroll");
	private static final DoaSprite NEXT_PHASE_BUTTON_TEXTURE = DoaSprites.get("nextPhaseButtonIdle");
	private static final DoaSprite CARDS_BUTTON_TEXTURE = DoaSprites.get("cardsButtonIdle");
	private static final DoaSprite WIN_CONDITION_BUTTON_TEXTURE = DoaSprites.get("winConditionButtonIdle");

	private DoaImageButton nextPhaseButton;
	private DoaImageButton cardsButton;
	private DoaImageButton winConditionButton;

	boolean moving = false;

	public MiddleSectionScroll() {
		super(MAX.clone(), TEXTURE.getWidth(), TEXTURE.getHeight(), DoaObject.STATIC_FRONT);
		nextPhaseButton = new DoaImageButton(833f, 991f, NEXT_PHASE_BUTTON_TEXTURE.getWidth(), NEXT_PHASE_BUTTON_TEXTURE.getHeight(), NEXT_PHASE_BUTTON_TEXTURE,
		        DoaSprites.get("nextPhaseButtonHover"));
		cardsButton = new DoaImageButton(935f, 991f, CARDS_BUTTON_TEXTURE.getWidth(), CARDS_BUTTON_TEXTURE.getHeight(), CARDS_BUTTON_TEXTURE,
		        DoaSprites.get("cardsButtonHover"));
		winConditionButton = new DoaImageButton(1037f, 991f, WIN_CONDITION_BUTTON_TEXTURE.getWidth(), WIN_CONDITION_BUTTON_TEXTURE.getHeight(),
		        WIN_CONDITION_BUTTON_TEXTURE, DoaSprites.get("winConditionButtonHover"));
	}

	public void move() {
		moving = true;
		if (position.y == MAX.y) {
			velocity.y = -1;
		}
		if (position.y == MIN.y) {
			velocity.y = 1;
		}
	}

	@Override
	public void tick() {
		if (moving) {
			if (velocity.y > 0) {
				velocity.y += ACCELERATION;
			} else if (velocity.y < 0) {
				velocity.y -= ACCELERATION;
			}
			position.add(velocity);
			nextPhaseButton.setPosition(nextPhaseButton.getPosition().add(velocity));
			cardsButton.setPosition(cardsButton.getPosition().add(velocity));
			winConditionButton.setPosition(winConditionButton.getPosition().add(velocity));
			if (position.y > MAX.y) {
				position.y = MAX.y;
				velocity.y = 0;
				moving = false;
			}
			if (position.y < MIN.y) {
				position.y = MIN.y;
				velocity.y = 0;
				moving = false;
			}
		}
		nextPhaseButton.tick();
		cardsButton.tick();
		winConditionButton.tick();
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(TEXTURE, position.x, position.y);
		nextPhaseButton.render(g);
		cardsButton.render(g);
		winConditionButton.render(g);
	}
}
