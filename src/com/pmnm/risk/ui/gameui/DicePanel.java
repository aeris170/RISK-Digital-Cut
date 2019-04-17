package com.pmnm.risk.ui.gameui;

import com.doa.engine.DoaHandler;
import com.doa.engine.graphics.DoaGraphicsContext;
import com.doa.engine.graphics.DoaSprites;
import com.doa.maths.DoaVectorF;
import com.doa.ui.DoaUIContainer;
import com.doa.ui.button.DoaImageButton;

public class DicePanel extends DoaUIContainer {

	private static final long serialVersionUID = 8009744806803376915L;

	private static final float ACCELERATION = 0.064f;

	private static final DoaVectorF MIN = new DoaVectorF(-100f, 400f);
	private static final DoaVectorF MAX = new DoaVectorF(0f, 600f);

	private DoaImageButton one = DoaHandler.instantiateDoaObject(DoaImageButton.class, -100f, 400f, 100, 70, DoaSprites.get("dice1"));
	private DoaImageButton two = DoaHandler.instantiateDoaObject(DoaImageButton.class, -100f, 470f, 100, 70, DoaSprites.get("dice2"));
	private DoaImageButton three = DoaHandler.instantiateDoaObject(DoaImageButton.class, -100f, 540f, 100, 70, DoaSprites.get("dice3"));
	private DoaImageButton blitz = DoaHandler.instantiateDoaObject(DoaImageButton.class, -100f, 610f, 100, 70, DoaSprites.get("diceBlitz"));

	private boolean moving = false;

	public DicePanel() {
		super(MIN.clone(), (int) (MAX.x - MIN.x), (int) (MAX.y - MIN.y));
		add(one);
		add(two);
		add(three);
		add(blitz);
		super.show();
	}

	@Override
	public void tick() {
		super.tick();
		if (moving) {
			if (velocity.x > 0) {
				velocity.x += ACCELERATION;
			} else if (velocity.x < 0) {
				velocity.x -= ACCELERATION;
			}
			position.add(velocity);
			one.getPosition().add(velocity);
			two.getPosition().add(velocity);
			three.getPosition().add(velocity);
			blitz.getPosition().add(velocity);
			if (position.x > MAX.x) {
				position.x = MAX.x;
				velocity.x = 0;
				moving = false;
			}
			if (position.x < MIN.x) {
				position.x = MIN.x;
				velocity.x = 0;
				moving = false;
			}
		}
	}

	@Override
	public void show() {
		moving = true;
		velocity.x = 1;
	}

	@Override
	public void hide() {
		moving = true;
		velocity.x = -1;
	}

	@Override
	public void render(DoaGraphicsContext g) {}
}