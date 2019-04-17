package com.pmnm.risk.ui.gameui;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprite;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.button.DoaImageButton;
import com.doa.ui.panel.DoaPanel;
import com.pmnm.risk.ui.gameui.actions.CardsButtonAction;
import com.pmnm.risk.ui.gameui.actions.NextPhaseButtonAction;

public class MiddleSectionScroll extends DoaPanel {

	private static final long serialVersionUID = -3397313460439190782L;

	private static final float ACCELERATION = 0.064f;

	private static final DoaVectorF MIN = new DoaVectorF(616f, 489f);
	private static final DoaVectorF MAX = new DoaVectorF(616f, 929f);

	private static final DoaSprite TEXTURE = DoaSprites.get("scroll");
	private static final DoaSprite NEXT_PHASE_BUTTON_TEXTURE = DoaSprites.get("nextPhaseButtonIdle");
	private static final DoaSprite CARDS_BUTTON_TEXTURE = DoaSprites.get("cardsButtonIdle");
	private static final DoaSprite WIN_CONDITION_BUTTON_TEXTURE = DoaSprites.get("winConditionButtonIdle");

	private DoaImageButton nextPhaseButton = DoaHandler.instantiateDoaObject(DoaImageButton.class, 833f, 991f, NEXT_PHASE_BUTTON_TEXTURE.getWidth(),
	        NEXT_PHASE_BUTTON_TEXTURE.getHeight(), NEXT_PHASE_BUTTON_TEXTURE, DoaSprites.get("nextPhaseButtonHover"));
	private DoaImageButton cardsButton = DoaHandler.instantiateDoaObject(DoaImageButton.class, 935f, 991f, CARDS_BUTTON_TEXTURE.getWidth(),
	        CARDS_BUTTON_TEXTURE.getHeight(), CARDS_BUTTON_TEXTURE, DoaSprites.get("cardsButtonHover"));
	private DoaImageButton winConditionButton = DoaHandler.instantiateDoaObject(DoaImageButton.class, 1037f, 991f, WIN_CONDITION_BUTTON_TEXTURE.getWidth(),
	        WIN_CONDITION_BUTTON_TEXTURE.getHeight(), WIN_CONDITION_BUTTON_TEXTURE, DoaSprites.get("winConditionButtonHover"));

	boolean moving = false;

	public MiddleSectionScroll() {
		super(MAX.clone(), TEXTURE.getWidth(), TEXTURE.getHeight());
		add(nextPhaseButton);
		add(cardsButton);
		add(winConditionButton);
		nextPhaseButton.addAction(new NextPhaseButtonAction());
		cardsButton.addAction(new CardsButtonAction(this));
		show();
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
			nextPhaseButton.getPosition().add(velocity);
			cardsButton.getPosition().add(velocity);
			winConditionButton.getPosition().add(velocity);
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
	}

	@Override
	public void render(DoaGraphicsContext g) {
		g.drawImage(TEXTURE, position.x, position.y);
	}
}
